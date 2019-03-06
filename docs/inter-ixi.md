# Inter-IXI Communication with EEE

## Abstract

Inter-IXI communication allows IXI modules to exchange data with each other. Some IXI modules might act as dependencies for other IXI modules or be plugged together. The communication mechanism should be designed with modularity in mind.

This document specifies such a communication mechanism realized with EEE (Environment Entity Effect). Modules can expose their functions as entities. Each such entity can publish trit vectors to environments. Additionally, entities can subscribe to environments to listen to any trit vectors. This enables IXI modules to indirectly react to and invoke each other's functions.

## EEE - Environment Entity Effect

### Environment

An environment (equivalent to "topic" in MQTT) is a category for effects. Each effect is part of an environment. All entities which have subscribed to a specific environment will be notified about new effects published to that environment.

### Entity

An entity can be anything that interfaces to an environment - either by publishing or subscribing to an environment. There are no restrictions: every entity is free to subscribe or publish to any environment.

### Effect

An effect is a message to be shared between entities through an environment. Each effect originates from an entity and is published to an environment from where it is distributed to entities subscribed to that environment.

## EEE Applied to IXI

The environment is realized through a tag-like string (character sequence) while effects are realized through trit vectors. Entities will be implemented as an interface and can therefore be applied to anything. For the simplicity of this document we will assume that entities are functions in IXI modules; although in Java they would be implemented as classes which would indirectly invoke the module's functions. Each function published to an environment whose string is a concatenation of the module's name acting as namespace and the actual functions name.

<img src="https://svgur.com/i/Ba8.svg" />