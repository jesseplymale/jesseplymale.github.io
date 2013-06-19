---
layout: groovy
title: Getting Started
---

## Setting up Groovy

* If you are on a Unix-type platform (including Mac OSX), use [GVM](http://gvmtool.net/) to easily install Groovy.
  
  * You can also install [Grails](http://grails.org/) through GVM, if desired.

* If you are developing Java projects in Maven and want Groovy to be able to easily access dependencies from
  your own personal repository, you just need to [modify your Grape settings.](http://groovy.codehaus.org/Grape)

  * Grape lets you use `@Grab` annotations to easily pull Maven dependencies into your scripts (using a Maven-like
    tool called [Ivy](http://ant.apache.org/ivy/).

* If you need libraries on your Groovy classpath (like database drivers):

  * Put them in your regular Java `$CLASSPATH` environment variable or run Groovy with a `-cp` option (just like Java) 
  
  * Put it in your `~/.groovy/lib` directory (create the directory if it doesn't exist)

## A Groovy Development Environment

* [IntelliJ IDEA](http://www.jetbrains.com/idea/) has good Groovy support. For example:

  * If you define a variable with a specific type instead of with `def`, you get method auto-completion.

  * Run scripts within the GUI.

  * Hit `Alt-Enter` on a `@Grab` annotation to pull in all the artifacts into your Intellij module's library.
  
* You can run [`groovysh`](http://groovy.codehaus.org/Groovy+Shell) for a simple command-line interface.

  * You can run `groovyConsole` for a graphical version. If you are a Grails user and start `grails console`, it will
    give you [a `groovyConsole` which has been initialized with the Grails runtime and has access to all the classes
    from your Grails app.](http://grails.org/doc/2.0.4/ref/Command%20Line/console.html)

  * *Beware!* If you get a `groovy.lang.MissingPropertyException` when you try accessing a variable, don't get frustrated.
   The most confusing thing about Groovy's shell/console is [how you define variables.](http://groovy.codehaus.org/Groovy+Shell#GroovyShell-Variables) This has to do with [something called the *binding*.](http://groovy.codehaus.org/Scoping+and+the+Semantics+of+%22def%22)

    * Basically, the binding is something that you only have in Groovy scripts and in the Groovy shell. 
      A binding variable is accessible to methods in your script without having to be passed in as a 
      method parameter.  On the Groovy shell, **only binding variables persist from line to line**.

    * To define a binding variable, just leave off the `def` (or whatever other type) in front of your variable when you declare it. 
    
    * My advice: Leave off the `def` when you are using the Groovy shell. Use it everywhere else.
  
