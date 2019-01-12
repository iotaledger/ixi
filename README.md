# IOTA eXtension Interface (IXI)

## About

The IOTA eXtension Interface is a minimal interface to build tangle applications (IXI modules) on top of the [Ict](https://github.com/iotaledger/ict) core client very easily.

## Creating an IXI Module

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

### Step 4: Write your module.json

This file contains all the meta data of our IXI module. So fill it out carefully.

```javascript
// your module.json file
{
  // the name under which your IXI will appear
  "name": "Example.ixi",
  
  // your IXI module class with package prefix
  "main_class": "my.package.MyModule",
  
  // Describe your module in 2-3 sentences.
  "description": "This module allows you to ...",
  
  // must match the GitHub repository you will later publish it to (format: username/repository)
  "repository": "iotaledger/ixi",
  
  // enter the port to visit your module's web GUI
  // set to -1 if your module doesn't have one
  // makes it easy for users to navigate from the ict web gui to your module's custom gui
  "gui_port": -1,
  
  // all Ict versions your ixi is compatible with
  "supported_versions": ["0.4-SNAPSHOT", "0.4"]
}
```

### Step 5: Write your versions.json

The **versions.json** file in your master directory allows Ict clients to check whether there are any
updates for your module or which version of your module they should use for their Ict version.

```javascript
{
  // "ICT-VERSION": "MODULE-VERSION"
  "0.4": "1.0",
  "0.4.1": "1.1",
  "0.5": "1.2"
}
```

### Step 6: Build your IXI.jar

To build your `ixi.jar` simply run `gradle ixi` (requires **Gradle**).

## Run the IXI module with your Ict

### Method A: Manual Download

Go to [releases](./releases) and download the latest **ixi.jar** file. Put it into the **modules/**
in your ict directory. Simply restart your Ict to load it.

### Method B: Download via Web GUI

Visit your Ict's web GUI (requires `web_gui=true` in **ict.cfg**) and go to "IXI MODULES". Click on the
**install** button and enter the GitHub URL or even just username/repository (e,g, iotaledger/chat.ixi).
Once your IXI module has been downloaded, it will be started.
