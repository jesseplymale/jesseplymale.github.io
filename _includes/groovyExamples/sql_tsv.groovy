@Grab('net.sf.opencsv:opencsv:2.3')
import au.com.bytecode.opencsv.CSVWriter
import groovy.sql.Sql
import java.sql.ResultSet
  
def (connectionString, username, password) = ["", "", ""]
Sql sql = Sql.newInstance(connectionString, username, password, 'com.mysql.jdbc.Driver')
  
String queryString = 'select id, login from users'
   
// Write out the results of the SQL query to stdout, in Excel CSV format,
// with the column names as the first line
System.out.withWriter { stdoutWriter ->
  CSVWriter csvWriter = new CSVWriter(stdoutWriter)
  sql.query(queryString) { ResultSet rs ->
    // CSVWriter has a convenient method for dealing with ResultSets
    csvWriter.writeAll(rs, true)
  }
}
