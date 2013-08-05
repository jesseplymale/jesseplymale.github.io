---
layout: groovyPost
title: Renaming Multiple Files
tags: groovyExamples
categories: groovy
---

Want to rename multiple files in your current directory?

{% highlight bash %}
groovy -e 'new File(".").eachFileMatch(~/.*md/) { it.renameTo(it.name.replace(".md", ".markdown")) }'
{% endhighlight %}
