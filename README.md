# Sistema de Gestión de Propiedades - Rama Código Espagueti

Este proyecto es parte de una prueba académica de Arquitectura de Software y contiene la implementación del sistema utilizando la **Arquitectura de Código Espagueti**.

## ¿Qué es el Código Espagueti?

El término "Código Espagueti" describe un estilo de programación donde **no existe ninguna separación de responsabilidades**. El código está tan enredado entre sí que, al intentar seguir el flujo del programa, se salta de un lugar a otro sin ninguna estructura clara, igual que intentar seguir un solo hilo en un plato de espagueti.

En este estilo:
- No hay capas definidas (ni Presentación, ni Servicio, ni Repositorio).
- No hay módulos ni contextos de negocio separados.
- La lógica de negocio, el acceso a la base de datos y la presentación HTTP conviven en el mismo lugar.
- Todo lo que necesita la aplicación está escrito en el menor número de archivos posible.

## Enfoque Utilizado: Un Solo Archivo

El principio central de esta rama es que **toda la aplicación vive en un único archivo Java**: `MainController.java`.

Dentro de ese archivo, sin ninguna carpeta de organización, se encuentran definidas secuencialmente:

1. Las **Entidades JPA** (`Property`, `Tenant`, `Rental`) que mapean las tablas de la base de datos.
2. El **Enum de estado** (`PropertyStatus`) para los valores permitidos de una propiedad.
3. Las **interfaces de Repositorio** (`PropertyRepo`, `TenantRepo`, `RentalRepo`) que permiten el acceso a H2.
4. El **Controlador principal** (`MainController`) que maneja absolutamente todas las rutas HTTP del sistema.

## Estructura de Archivos

```text
src/main/java/com/practica/prueba
├── PruebaApplication.java
└── MainController.java       <-- TODO EL SISTEMA AQUÍ
    │
    ├── class Property        (Entidad JPA)
    ├── enum PropertyStatus   (Estados posibles)
    ├── class Tenant          (Entidad JPA)
    ├── class Rental          (Entidad JPA)
    ├── interface PropertyRepo(Repositorio Spring Data)
    ├── interface TenantRepo  (Repositorio Spring Data)
    ├── interface RentalRepo  (Repositorio Spring Data)
    │
    └── class MainController  (Controlador único con TODAS las rutas)
        ├── GET  /                    → Dashboard
        ├── GET  /properties          → Listar propiedades
        ├── POST /properties          → Crear propiedad
        ├── GET  /properties/new      → Formulario nueva propiedad
        ├── GET  /properties/{id}/edit→ Formulario editar propiedad
        ├── POST /properties/{id}     → Actualizar propiedad
        ├── GET  /tenants             → Listar inquilinos
        ├── GET  /tenants/new         → Formulario nuevo inquilino
        ├── POST /tenants             → Crear inquilino
        ├── GET  /rentals             → Listar arriendos
        ├── GET  /rentals/new         → Formulario nuevo arriendo
        ├── POST /rentals             → Crear arriendo (con regla de negocio inline)
        └── POST /rentals/{id}/finish → Finalizar arriendo (con regla inline)
```

## Cómo se Aplican las Reglas de Negocio

En lugar de existir un Servicio o un Caso de Uso dedicado, las reglas de negocio están escritas **directamente dentro del método del controlador**. Por ejemplo, la regla *"solo se puede arrendar una propiedad disponible"* se verifica así, dentro del mismo método que recibe la petición HTTP:

```java
@PostMapping("/rentals")
public String createRental(@RequestParam Long propertyId, @RequestParam Long tenantId, Model model) {
    // La regla de negocio está mezclada directamente aquí
    Property p = propertyRepo.findById(propertyId).orElseThrow(...);
    if (p.getStatus() == PropertyStatus.OCCUPIED) {
        model.addAttribute("error", "La propiedad no está disponible.");
        return "rentals/form";
    }
    p.setStatus(PropertyStatus.OCCUPIED);
    propertyRepo.save(p);
    rentalRepo.save(new Rental(propertyId, tenantId));
    return "redirect:/rentals";
}
```

No hay ninguna abstracción intermedia: el controlador consulta directamente el repositorio, aplica la regla y persiste los cambios en la misma función.

## Ventajas y Desventajas

| Aspecto | Código Espagueti |
|---|---|
| **Velocidad inicial** | Muy rápida (todo en un lugar) |
| **Curva de aprendizaje** | Muy baja (no hay abstracciones) |
| **Mantenibilidad** | Muy mala (todo está acoplado) |
| **Testabilidad** | Imposible (no se puede probar aisladamente) |
| **Escalabilidad** | Nula (agregar funciones rompe todo) |
| **Legibilidad a largo plazo** | Muy mala (el archivo crece sin control) |

## Comparación entre las Tres Arquitecturas

| | Código Espagueti | Monolítico por Capas | DDD |
|---|---|---|---|
| **Archivos Java** | 1 | ~15 | ~26 |
| **Organización** | Ninguna | Por tipo técnico | Por contexto de negocio |
| **Separación de responsabilidades** | No existe | Parcial | Estricta |
| **Lógica de negocio en** | El controlador | El Service | La Entidad/Agregado |
| **Facilidad de pruebas** | Nula | Media | Alta |
| **Recomendado para** | Prototipos desechables | Proyectos académicos / CRUDs simples | Sistemas empresariales complejos |