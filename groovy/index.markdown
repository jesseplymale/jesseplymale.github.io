---
layout: groovy
title: Groovy
---

{% for post in site.categories.groovy %}
<div class="post">
    <div class="postHeader">
        <h3><a href="{{post.url}}">{{ post.title }}</a></h3>
        <span class="postDate">({{ post.date | date: "%Y-%m-%d" }})</span>
    </div>
    <div class="postContent">
        {{post.content}}
    </div>
</div>
{% endfor %}