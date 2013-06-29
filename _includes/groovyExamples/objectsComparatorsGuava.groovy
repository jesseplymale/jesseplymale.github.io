@Grab('com.google.guava:guava:14.0.1')
import com.google.common.collect.ComparisonChain
import groovy.transform.ToString

@ToString(includeNames=true)
class Person {
    String name
    int age
}

def people = [
    new Person(name: "Tom", age: 30), 
    new Person(name: "Sally", age: 25),
    new Person(name: "Tom", age: 24)
] 

println "Unsorted: ${people}"

def peopleComparator = [compare: { o1, o2 ->
    ComparisonChain.start()
        .compare(o1.name, o2.name)
        .compare(o1.age, o2.age)
        .result()
}] as Comparator

people.sort(peopleComparator)
println "Sorted: ${people}"
