---
layout: groovyPost
title: A Simple Markdown to HTML Conversion Script
tags: groovyExamples
categories: groovy
---

[Markdown](http://daringfireball.net/projects/markdown/) is a simple markup language which looks very readable in its "raw" form but can be easily converted to HTML. I use it to write most of the pages on this site. 

There are libraries in several languages which process Markdown. One popular one for Java is [Pegdown](http://pegdown.org/). Here's a simple Groovy script which takes a single Markdown file as input and outputs HTML.

{% highlight groovy %}
{% include groovyExamples/mdToHtml.groovy %}
{% endhighlight %}
