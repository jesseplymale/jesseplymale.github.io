---
layout: post
title: Groovy Scripts on the Command Line
tags: groovyExamples
categories: groovy
---

You can use Groovy on the command-line to do things that you might otherwise do with `awk`.

Helpful command-line options include:

* `-e`
    * The basic option. Include some code in quotes after this, and it gets executed.

{% highlight bash %}
> groovy -e 'println(new Date())'
Mon Jul 22 16:09:43 CDT 2013
{% endhighlight %}

* `-n`
    * Used in conjunction with the `-e`, this iterates over every line of the file (or files) that you specify on the command line, *or* pipe in via standard in. 
        * Each line is put into the variable `line`. 
        * The current line number (starting at `1`) is in the variable `count`. 

So, assume the following file is saved in `temp.csv`:

{% highlight bash %}
firstName,lastName,url
Jesse,Plymale,http://www.jesseplymale.com
Bill,Gates,ftp://www.microsoft.com
Barack,Obama,https://www.whitehouse.gov
{% endhighlight %}

Then, we can do the following: _(Note the space between the double quote and ending single quote. It complains if the space is not there. I think that is because of the shell.)_
 
{% highlight bash %}
> groovy -ne 'println "${count}   ${line.length()}" ' temp.csv
1   22
2   41
3   34
4   39
{% endhighlight %}

* `-a`
    * This turns on "autosplit", which allows you to specify a regex pattern to split the line on. The split line is put in the variable `split`. (You also still have access to the variable `line` for the unsplit line.)
 
* `-p`
    * This prints out whatever you return from the commandline. Thus you will see in the example below (which uses `-a`, `-p`, and `-e` options, with a piped-in file) that we don't have to call `println`.

{% highlight bash %}
> groovy -a ',' -p -e 'if(count>1) "The host for ${split[0]} is:\t${new URL(split[2]).host}" ' < temp.csv
The host for Jesse is:    www.jesseplymale.com
The host for Bill is:     www.microsoft.com
The host for Barack is:   www.whitehouse.gov
{% endhighlight %}
