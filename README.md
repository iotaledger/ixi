# IOTA Extension Interface (IXI)

## About

The IOTA Extension Interface provides an API that connects IXI modules to the [Ict](https://github.com/iotaledger/ict) core client.
IXI modules are applications built on top of the Tangle protocol.

## Creating an IXI

### Step 1: Clone this Repository

```shell
# clone the example source code
git clone https://github.com/iotaledger/ixi
```

You can also manually download the repository source code if you don't have **Git**.

### Step 2: Open the Project in your IDE

```shell
# if you are using intellij (requires Gradle)
gradle idea
```

### Step 3: Implement your IXI Module

This part is where you get creative. Implement your ideas in `src/main/java/org.iota.ixi/Ixi.java`. Make sure to change
the name from `example.ixi` to whatever your IXI module is called.

### Step 4: Build your IXI.jar

This step requires **Gradle**.

```shell
# move into the cloned repository (in which your build.gradle file is)
cd Desktop/ixi

# build the .jar file
gradle fatJar
```

You should now find your finished `ixi-{VERSION}.jar` file.

## Run the IXI module with your Ict

### Step 1: Enable the IXI in your `ict.cfg` file:

The `ict.cfg` file will be created the first time you start your Ict.
Just make sure that the `ixi_enabled` property is set to `true`:

```
ixi_enabled=true
```

### Step 2: Start your Ict client

```shell
java -jar ict-{VERSION}.jar
```

### Step 3: Start your IXI module

The name of your Ict is defined in the `ict.cfg` file.

```shell
java -jar ixi-{VERSION}.jar {ICT NAME}
```
