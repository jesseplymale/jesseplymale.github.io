---
layout: post
title: Unusual use of the 'as' keyword
tags: groovyExamples
categories: groovy
---

[Groovy's `as` keyword](http://mrhaki.blogspot.com/2009/09/groovy-goodness-as-keyword.html) is a bit mysterious to me.

* It's not a simple class cast. It calls methods behind the scenes to do a conversion.
* [You can override its behavior](http://mrhaki.blogspot.com/2009/11/groovy-goodness-define-your-own-type.html) through overriding `asType()`.
* If you use `as YourClass` with a list or map, it [will call the class constructor](http://mrhaki.blogspot.com/2009/09/groovy-goodness-using-lists-and-maps-as.html) for `YourClass` with the items in the list or map. This is a little weird to me (and not, in my opinion, a good thing to do--how much typing does it actually save?) but it explains why this would work (given the [constructor for Date that takes three int parameters](http://docs.oracle.com/javase/6/docs/api/java/util/Date.html#Date%28int,%20int,%20int%29)):

{% highlight groovy %}
def date = [113, 6, 29] as Date   // which is 2013-06-29
{% endhighlight %}

* Using `as <YourInterface>` on a closure or a map is like creating an anonymous class that implements `YourInterface`, [as I've mentioned before]({% post_url 2013-06-29-guava-comparators-objects %}).

