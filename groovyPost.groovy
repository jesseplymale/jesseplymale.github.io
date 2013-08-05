#!/usr/bin/env groovy

String title = args[0]
String cleanedTitle = title.replaceAll(~/[^a-zA-Z ]/, "")
String titleWithDashes = cleanedTitle.replace(' ', '-').toLowerCase()
String dateString = new Date().format('yyyy-MM-dd')
String fileName = "_posts/${dateString}-${titleWithDashes}.markdown".toString()
println "Creating file: ${fileName}"

new File(fileName).text = """---
layout: groovyPost
title: ${title}
tags: groovyExamples
categories: groovy
---

{% highlight bash %}
{% endhighlight %}
"""
