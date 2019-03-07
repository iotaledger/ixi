
**Programming IXI Modules**

  

# The IOTA eXtension Interface (IXI)

The IOTA eXtension Interface (IXI) is an API to build extensions for any IOTA node. These applications are called „IXI modules“. By abstracting from any internal implementation details such as communication, persistence or resource management, the IXI decouples these modules from the node logic. As a minimal interface, it enables simple and portable interaction with the Tangle without revealing unnecessary details. The IXI is designed with the principle of modularity in mind. This allows to customize an IOTA node by simply extending it with custom IXI modules.

Currently the IXI only supports modules written in Java. Support for other languages is on the roadmap.

# Getting Started

...

# IxiModule Structure

## Framework

```java
public class MyModule extends IxiModule {

    // the constructor must be public and have exactly one parameter of type Ixi
    public MyModule(Ixi ixi) {
        super(ixi);
    }

    @Override
    public void run() {
        // will be described later
    }
}

```

## Starting and Terminating

IXI modules are applications decoupled from the underlying node and as such they have to run in their own threads. A user might want to start, stop or restart a module. Each IXI module provides an internal mechanism to realize this.

### The Internal State Protocol
![the internal state protocol](https://svgshare.com/i/BKj.svg)

```java
@Override
public void onStart() {
    createUnicorns(); // 1st call
}

@Override
public void onStarted() {
    feedUnicorns(); // 2nd call
}

@Override
public void onTerminate() {
    killUnicorns(); // 3rd call
}

@Override
public void onTerminated() {
    buryUnicorns(); // 4th call
}
```

### The Main Method: `run()`

The `run` method is the main process in every module. It is called on every start and must finish to terminate the module. Usually it will be implemented with a `while(isRunning()) { ... }` loop:

```java
@Override
public void run() {
    log("waking up unicorns ...");
    while(isRunning()) {
        sendUnicornTransaction();
        feedUnicorns();
    }
    log("unicorns fell to sleep.");
}
```
This will ensure that the module is running until it is forced to terminate. `isRunning()` will return `true` if and only if the module is in state `running` (see state diagram).

### Sub-Workers

Sometimes you want to use more threads that are part of your IXI modules. In that case it is best practice to register these threads as sub-workers. They will be automatically taken care of and you won't have to reinvent your own restartable infrastructure.

```java
public MyModule(Ixi ixi) {
    super(ixi);
    subWorkers.add(new UnicornFeeder());
    subWorkers.add(new UnicornWatcher());
}

private class UnicornFeeder extends RestartableThread {
    // implementation
}

private class UnicornWatcher implements Restartable {
    // implementation
}
```

You can simply derive your sub-worker from `RestartableThread` which provides a ready-to-use framework. If that does not fit your situation, you can implement your own solution by with the `Restartable` interface.

## The Installation Process

Users can install new IXI modules from their web GUI simply by pressing a button. The node client will then download the IXI module, create a new instance of the module and then invoke its `install()` method a single time. Through this method the module can set-up everything it will require during its lifetime until it is uninstalled. This might include creating directories or downloading dependencies.

Once the user decides to remove a module through the web GUI, the `uninstall()` method will be called. It is responsible for cleaning up the mess created during the module‘s lifetime. This can include anything from deleting files and resetting environment variables to notifying remote servers.

```java
@Override
public void install() {  
    createDirectory(UNICORN_IXI_DIR_PATH);
}  
  
@Override  
public void uninstall() {  
    deleteDirectory(UNICORN_IXI_DIR_PATH);
}
```
# Sending Transactions

Sending a transaction happens in two steps. First you create a new transaction. Then you submit the transaction to the network. Without the latter, the transaction will not be added to your local Tangle.

```java
// 1) create a new transaction
TransactionBuilder builder = new TransactionBuilder();
builder.asciiMessage("I love unicorns! <3");
builder.tag= "UNICORN9FOR9PRESIDENT";
Transaction transaction = builder.buildWhileUpdatingTimestamp();

// 2) submit transaction to network
ixi.submit(transaction);
log("submitted transaction with hash: " + transaction.hash);
```

`builder.buildWhileUpdatingTimestamp()` will constantly update the timestamp while doing the proof-of-work. This is required so that the timestamp does not age too much in case proof-of-work takes longer. Currently the Ict network will ignore transactions which are older than 20 seconds. Therefore it is recommended to use this method. If you know what you are doing and want to set a custom timestamp, use `builder.build()` instead.

# Meta Data

Each IXI module must provide meta data. This meta data helps both the Ict node as well as its user to understand how the module is supposed to work. The meta data is specified in its own `module.json` file and must be included into the module's .jar file.

```json
{
  "name": "Example.ixi",
  "main_class": "my.package.MyModule",
  "description": "This module allows you to ...",
  "repository": "iotaledger/ixi",
  "gui_port": -1,
  "supported_versions": ["0.4-SNAPSHOT", "0.4"]
}
```
field | description
---|---
`name`| The name under which your IXI will appear.
`main_class`| Your IXI module class with package prefix.
`description`| Describe your module in 2-3 sentences.
`repository`| The GitHub repository you will later publish it to (format: username/repository).
`gui_port`| The port to visit your module's web GUI. Makes it easy for users to navigate from the Ict web GUI to your module's custom GUI. Set to `-1` if your module doesn't have one or to `0` if you host your GUI on `web/modules/{name}/`.
`supported_versions`| The name under which your IXI will appear.


# IxiContext

When writing more advanced modules, you will need custom user input and output at some point. To simplify this process and prevent that every module invents their own solutions, this whole process is streamlined through the `IxiContext`. This class allows you to make your module configurable from the official web GUI. Every module provides a context through `getContext()`. The default implementation does not allow for configuration. But we can change that by overwriting this method and returning a custom `ConfigurableIxiContext ` object:

```java
private int unicorns = 1;
private final MyContext myContext = new MyContext();

@Override
public IxiContext getContext() {
    return myContext;
}

private class MyContext extends ConfigurableIxiContext {
  
    private MyContext() {
        // submit the default configuration to the super constructor
        super(new JSONObject().put("unicorns", unicorns));
    }
  
    @Override
    protected void validateConfiguration(JSONObject newConfiguration) {
        // throw exception with helpful message if configuration is not allowed
        if(!newConfiguration.has("unicorns"))
            throw new IllegalArgumentException("field 'unicorns' is missing");
        if(!(newConfiguration.get("unicorns") instanceof Integer))
            throw new IllegalArgumentException("field 'unicorns' is not an integer");
        if(newConfiguration.getInt("unicorns") < 0)
            throw new IllegalArgumentException("field 'unicorns' cannot be negative");
    }
    
    @Override
    protected void applyConfiguration() {
        // configuration has already been validated, apply the changes
        unicorns = configuration.getInt("unicorns");
        log("You have " + unicorns + " unicorns!");
    }
}
```
  

# GossipListeners

Previously we learned about how the IXI module runs as a separate thread. However, sometimes you want to build an event-driven application. For this purpose you can register gossip listeners through the IXI which will be notified about new gossip events such as the receiving of a new transaction.

```java
private final GossipListener myListener = new CustomGossipListener();

@Override
public void onStart() {
    // register your listener
    ixi.addGossipListener(myListener);
}

@Override
public void onTerminate() {
    // don't forget to unregister the listener
    ixi.removeGossipListener(myListener);
}

private class CustomGossipListener implements GossipListener {

    @Override
    public void onGossipEvent(GossipEvent event) {
        log("received event with transaction: " + event.getTransaction().hash);
    }
}
```

# GossipPreprocessors

For certain cases it is required to catch gossip events and prevent them from propagating. gossip preprocessors are a more advanced technique which allow exactly this. Similar to gossip listeners they receive events. However, all gossip preprocessors are linked to a queue and whenever a new event appears, it is put into the start of this queue (the first preprocessor). The gossip events emitted by one preprocessor are input to its successor preprocessor. Only those events leaving the queue at the other end (the last preprocessor) are then forwarded to all gossip listeners. This gives gossip preprocessors the ability to intervene and catch events or inject new events.

To link the gossip preprocessors in correct order, each one specifies its position. They are then linked together sorted by that position in ascending order.

```java

private final GossipPreprocessor myGP = new GossipPreprocessor(1620);

@Override
public void onStart() {
    ixi.addGossipPreprocessor(myGP);
}

@Override
public void run() {
    try {
        while(isRunning()) {
            GossipEvent event = myGP.incoming.take();
            // only allow transactions with tag 'UNICORN9FOR9PRESIDENT' to be propagated
            if(event.getTransaction().tag().equals("UNICORN9FOR9PRESIDENT"))
                myGP.passOn(event);
        }
    } catch (InterruptedException e) { if(isRunning()) throw new RuntimeException(e); }
}

@Override
public void onTerminate() {
    ixi.addGossipPreprocessor(myGP);
    // interrupt otherwise myGP.incoming.take() will block termination until next event is received
    runningThread.interrupt();
}

```

# Publishing

## Compilation

To compile your IXI module simply run: `gradle ixi`. Make sure your module.json is included in the resulting .jar file.

## Releasing

1) Create a new release on your module's GitHub repository.
2) Attach the pre-compiled .jar file as asset to that release.
3) Adjust your `versions.json` file.