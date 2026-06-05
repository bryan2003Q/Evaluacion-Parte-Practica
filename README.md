# Sistema de Gestión de Propiedades - Rama DDD

Este proyecto es parte de una prueba académica de Arquitectura de Software y contiene la implementación del sistema utilizando **Domain-Driven Design (DDD)**.

## Enfoque Arquitectónico Utilizado

El sistema está diseñado basándose en los principios fundamentales de DDD combinados con conceptos de Arquitectura Hexagonal y el patrón de **Casos de Uso (Use Cases)**. Las principales características de este enfoque son:

1. **Bounded Contexts (Contextos Acotados)**: El código no está organizado por capas técnicas genéricas (todos los controladores juntos, todos los modelos juntos), sino que se divide en módulos que representan contextos de negocio reales: `property` (Gestión de Propiedades) y `rental` (Gestión de Arriendos). Existe también un contexto `shared` para la capa de presentación consolidada.
2. **Aislamiento del Dominio**: La capa de `domain` es el corazón del sistema. Aquí residen las Entidades, los Aggregate Roots (`Property`, `Tenant`, `Rental`) y los Value Objects (`Address`, `PropertyStatus`). Esta capa no tiene dependencias técnicas con Spring Web (Controladores) ni con implementaciones de bases de datos externas.
3. **Casos de Uso (Application Layer)**: En lugar de tener grandes clases de servicios ("God Objects"), la capa de aplicación implementa el Principio de Responsabilidad Única (SRP) utilizando un patrón de "Casos de Uso". Cada acción específica del sistema (ej. `CreatePropertyUseCase`, `OccupyPropertyUseCase`) es una clase individual encargada de orquestar los llamados al dominio.
4. **Infraestructura como Detalle**: Los repositorios de JPA y los controladores de Spring Web (Thymeleaf) residen exclusivamente en la capa de `infrastructure`, ya que son considerados detalles técnicos que actúan como adaptadores (Adaptadores de Entrada/Salida) para interactuar con el exterior.

## Estructura de Directorios

```text
src/main/java/com/practica/prueba
├── PruebaApplication.java
│
├── property/                          <-- Bounded Context: Gestión de Propiedades
│   ├── application/                   <-- Casos de uso (Orquestación)
│   │   ├── CreatePropertyUseCase.java
│   │   ├── GetAllPropertiesUseCase.java
│   │   └── ...
│   ├── domain/                        <-- Lógica pura de negocio
│   │   └── model/
│   │       ├── Address.java           <-- Value Object
│   │       ├── Property.java          <-- Entidad / Aggregate Root
│   │       └── PropertyStatus.java    <-- Value Object / Enum
│   └── infrastructure/                <-- Integración con tecnologías externas
│       ├── persistence/               <-- Adaptador de Base de Datos
│       │   └── PropertyRepository.java
│       └── web/                       <-- Adaptador Web (Controladores HTTP)
│           └── PropertyController.java
│
├── rental/                            <-- Bounded Context: Gestión de Arriendos
│   ├── application/                   <-- Casos de uso de Arriendos e Inquilinos
│   │   ├── CreateRentalUseCase.java
│   │   ├── CreateTenantUseCase.java
│   │   └── ...
│   ├── domain/
│   │   └── model/
│   │       ├── Rental.java            <-- Entidad / Aggregate Root
│   │       └── Tenant.java            <-- Entidad / Aggregate Root
│   └── infrastructure/
│       ├── persistence/
│       │   ├── RentalRepository.java
│       │   └── TenantRepository.java
│       └── web/
│           ├── RentalController.java
│           └── TenantController.java
│
└── shared/                            <-- Bounded Context: Elementos transversales (BFF)
    └── infrastructure/
        └── web/
            └── DashboardController.java
```

### Descripción Breve de las Capas Internas

* **Domain**: Contiene las reglas de negocio, validaciones de estado y restricciones inmutables de los objetos. Es la representación pura de "qué" hace el negocio.
* **Application**: Contiene los Casos de Uso. Dicta los flujos del sistema y el "cómo" se llevan a cabo las acciones coordinando la base de datos con el Dominio.
* **Infrastructure**: Contiene todo el código que depende del framework (Spring Boot, Spring Data JPA, Thymeleaf, Base de Datos H2).
