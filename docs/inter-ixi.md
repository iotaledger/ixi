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

<img src="https://raw.githubusercontent.com/iotaledger/ixi/master/docs/assets/eee.svg?sanitize=true" />

### Virtual Function Calls

While EEE only allows for data to be sent one-way (from one entity to the entities subscribed to an environment), virtual function calls including both parameters and a return value can be realized on top. To do that, the caller submits a return enviornment to the service so it can answer the request effect with a response effect to the specified environment. A request identificator should be added as well, so the response can be associated with the respective request.

```
Entity:  [Effect]                                                            -> Environment
----------------------------------------------------------------------------------------------------------------
Caller:  [requestId=8231, a=3, b=4, return="Caller.processMultiplyResult()"] -> "Service.multiply()"
Service: [requestId=8231, result=12]                                         -> "Caller.processMultiplyResult()"
```

## Implementation

Each module, once injected, is able to expose its endpoints and publish effects to it. These endpoints and effects will be managed and processed automatically by IXI. In this sense, IXI represents the link between all the different modules. 

### Data structure

In order to handle all the endpoints with it's effects, it is recommended to use a mapping architecture as follows:

```java
Map<String, Set<EffectListener>> listenersByEnvironment
```
  
The key of this map represents the environment, with a link to all subscribed listeneres. As illustrated in the figure above, the environment key consists of the concatenation of the module's name acting as namespace and the actual functions name.

Besides that, effects will be appended/polled sequentially to/from the listeners' effect queue.

To be sure that a module publishes effects only to environments intended for it, we should consider also a security mechanism. This security mechanism could be provided direclty by IXI, by ensuring that the environment key actually comes from the module that is attempting to send its effect to it.

### Methods

To subscribe to an specific environment, following design is recommended:

```java
EffectListener listener = new EffectListenerQueue<String>();
ixi.addEffectListener(listener);
```

Effects can be accessed as follows:

```java
listener.getEffect()
```

To publish an effect to a specific environment, following method could be used:

```java
ixi.submitEffect(String environment, String effectTrytes);
```

