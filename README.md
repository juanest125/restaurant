# Base project to implement Clean Architecture

## Before to init

We are going to explain the project's basic concepts, Beginning  with the external components, then the core business (Domain) and finally the application's configuration.

More info [Clean Architecture](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Architecture

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

This component is the Architecture's deepest internal module. It is the domain layer and contains the business's logic and rules through domain's models and entities.

## Use-Cases

This gradle module is in the domain layer and implements the system's use-cases, defines the logic application and reacts to the calls from the entry-points, Orchestrating the flow to entities.

## Infrastructure

### Helpers

This section contains general utilities used by Driven-Adapters and Entry-Points.
These utilities are not joined to a specific object. It uses the generic concepts to model the generic behaviors used by the different object repositories implemented.
Its implementation is based on the design pattern [Unit of Work and Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

These classes can't exist without an implementation from another class in the  **Driven-Adapters**

### Driven Adapters

The Driven-Adapters represents the external implementation to the domain layer. For example api-rest, soap, database, file and some other data source.

### Entry Points

The Entry-Points represents the input layer to the application or the business flow beginning.

## Application

This is the most external Architecture's module, and it is used to assembly the different modules, resolve dependencies and create de Beans used by the Domain's UseCases automatically. It injects to the specific instances the declared dependencies. Further, it is the start point to run the application (This module is the only one with a public static void main(String[] args) function).

**Spring boot can use the UseCase's beans thanks to a '@ComponentScan' located in this layer**
