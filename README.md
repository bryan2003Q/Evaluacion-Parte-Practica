# Sistema de Gestión de Propiedades - Rama Monolítico por Capas

Este proyecto es parte de una prueba académica de Arquitectura de Software y contiene la implementación del sistema utilizando la **Arquitectura Monolítica por Capas** (también conocida como N-Tier o arquitectura de 3 capas).

## Enfoque Arquitectónico Utilizado

A diferencia de la rama de *Domain-Driven Design (DDD)* donde el código se agrupa por "contexto de negocio" (Propiedades, Arriendos), en la Arquitectura por Capas el código se organiza estrictamente por su **responsabilidad técnica horizontal**.

Cada capa tiene un rol tecnológico específico y existe una regla estricta de dependencias: una capa superior solo puede comunicarse con la capa inmediatamente inferior. El flujo de información (y las dependencias de código) siempre va hacia abajo:

```text
Capa de Presentación (Controllers) 
         ↓
Capa de Negocio (Services)
         ↓
Capa de Acceso a Datos (Repositories)
         ↓
Base de Datos (H2)
```

### Características Fundamentales de nuestra implementación:

1. **El Modelo Anémico (Anti-patrón en DDD, estándar aquí)**: 
   Las entidades en el paquete `model` (`Property`, `Tenant`, `Rental`) son simples contenedores de datos. Solo tienen atributos, getters y setters generados por Lombok. No tienen "inteligencia" ni lógica de negocio (no saben cómo ocuparse o liberarse por sí mismas).
2. **Servicios Centralizados (Fat Services)**: 
   Toda la "inteligencia" de la aplicación (las reglas de negocio) reside en las clases del paquete `service`. Por ejemplo, `RentalService` es quien tiene la responsabilidad de verificar si la propiedad está disponible, cambiar manualmente su estado y crear el arriendo. El modelo de datos es completamente pasivo.
3. **Agrupación Técnica, no de Negocio**: 
   Al abrir la carpeta `controller/`, verás controladores de propiedades, de inquilinos y de arriendos mezclados. La cohesión es técnica (todos son controladores), pero el aislamiento entre los módulos de negocio se pierde.

## Estructura de Directorios Detallada

```text
src/main/java/com/practica/prueba
├── PruebaApplication.java
│
├── model/                         <-- Capa de Datos (Entidades Anémicas)
│   ├── Property.java              (Solo atributos y getters/setters)
│   ├── PropertyStatus.java
│   ├── Tenant.java
│   └── Rental.java
│
├── repository/                    <-- Capa de Acceso a Datos (Data Access Layer)
│   ├── PropertyRepository.java    (Interfaces Spring Data JPA)
│   ├── TenantRepository.java
│   └── RentalRepository.java
│
├── service/                       <-- Capa de Lógica de Negocio (Business Layer)
│   ├── PropertyService.java       (CRUD y operaciones simples)
│   ├── TenantService.java
│   └── RentalService.java         (Contiene las reglas fuertes de arrendamiento)
│
└── controller/                    <-- Capa de Presentación (Presentation Layer)
    ├── DashboardController.java   (Orquesta vistas consolidadas)
    ├── PropertyController.java    (Recibe peticiones HTTP, devuelve HTML)
    ├── TenantController.java
    └── RentalController.java
```

## Ejemplo del Flujo de Datos (Crear un Arriendo)

Para entender cómo interactúan las capas de forma práctica, este es el "viaje" de los datos cuando un usuario decide arrendar una propiedad:

1. **Presentación:** El usuario envía un formulario HTML desde el navegador. El `RentalController` intercepta la petición HTTP `POST`.
2. **Delegación:** El `Controller` no tiene permitido aplicar lógica de negocio, así que extrae los parámetros (ID de propiedad e ID de inquilino) y se los pasa al `RentalService`.
3. **Reglas de Negocio:** El `RentalService` toma el control total:
   - Consulta al `PropertyRepository` para obtener los datos de la propiedad desde la base de datos.
   - Verifica la regla de negocio: *¿La propiedad está ocupada?* Si es así, interrumpe el flujo y lanza un error.
   - Si está libre, el servicio modifica el estado de la propiedad llamando a `property.setStatus(OCCUPIED)`.
   - Crea en memoria un nuevo objeto `Rental`.
4. **Persistencia:** El `RentalService` invoca al `RentalRepository` y al `PropertyRepository` para guardar los cambios de forma transaccional en H2.
5. **Respuesta:** El flujo vuelve hacia arriba al `Controller`, quien decide qué vista mostrar a continuación (una redirección a la lista de arriendos).



S