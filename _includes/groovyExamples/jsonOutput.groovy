import groovy.json.*;

def stringToInt = [:]
("a".."d").eachWithIndex { value, index ->
    stringToInt.put value, index
}
def enclosingMap = [fourEntryMap: stringToInt, anotherEntry: "hi"]
println JsonOutput.prettyPrint(JsonOutput.toJson(enclosingMap))
