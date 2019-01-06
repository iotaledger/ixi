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

If you are using intellij, you might have to run `gradle idea`.

### Step 3: Implement your IXI Module

This part is where you get creative. Implement your ideas in **org.iota.ict.ixi.Module.java**.

### Step 4: Write your mode.json

This file contains all the meta data of our IXI module. So fill it out carefully.

```javascript
// your module.json file
{
  // the name under which your IXI will appear
  "name": "Example.ixi",
  
  // your IXI module class, must be in package 'org.iota.ict.ixi'
  "main_class": "org.iota.ict.ixi.Module",
  
  // Describe your module in 2-3 sentences.
  "description": "This module allows you to ...",
  
  // must match the GitHub repository you will later publish it to (format: username/repository)
  "repository": "iotaledger/ixi",
  
  // enable if you run a REST web GUI under host:4567/modules/{MODULE_NAME}
  // allows users to easily navigation from the ict web gui to your module's custom gui
  "web_gui": false,
  
  // all Ict versions your ixi is compatible with
  "supported_versions": ["0.4-SNAPSHOT", "0.4"]
}
```

### Step 5: Build your IXI.jar

To build your `ixi.jar` simply run `gradle ixi` (requires **Gradle**).

You should now find your finished l need

## Run the IXI module with your Ict

### Method A: Manual Download

Go to [releases](./releases) and download the latest **ixi.jar** file. Put it into the **modules/**
in your ict directory. Simply restart your Ict to load it.

### Method B: Download via Web GUI

Visit your Ict's web GUI (requires `web_gui=true` in **ict.cfg**) and go to "IXI MODULES". Click on the
**install** button and enter the GitHub URL or even just username/repository (e,g, iotaledger/chat.ixi).
Once your IXI module has been downloaded, it will be started.