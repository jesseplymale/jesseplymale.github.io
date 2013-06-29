@Grab('net.sf.opencsv:opencsv:2.3')
import au.com.bytecode.opencsv.*

System.in.withReader { reader ->
    // Set up a tab-separated reader
    def csvReader = new CSVReader(reader, '\t' as char)
    def rows = csvReader.readAll()
    System.out.withWriter { writer ->
        // Use the default comma-separated writer.
        // It also quotes every cell in the table by default
        CSVWriter csvWriter = new CSVWriter(writer)
        csvWriter.writeAll(rows)
    }
}
