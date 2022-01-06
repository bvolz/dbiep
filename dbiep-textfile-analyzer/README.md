# DBIEP: TextFile Analyzer (TFAnalyzer)

The *TFAnalyzer* is a tool for generating schema information out of regular text files such as CSV, TSV or plain text files with a fixed column size.
The schema is stored in a JSON file and may later be used by succeeding tools, e.g. the database importer.

## Configuration

### Commandline Options

TODO

### Logging

TFAnalyzer uses [SLF4J API][2] as logging interface and [Logback][1] an implementation. 
Thus, all information on how to define the concrete logging setting may be taken from [Logback][1].

As TFAnalzyer ships with a default logging configuration, you need to tell Logback to use your configuration.
This may be achieved by adding a property to the command line when invoking TFAnalyzer: `-Dlogback.configurationFile=/path/to/your/logback.xml`


[1]: https://logback.qos.ch "Logback"
[2]: https://www.slf4j.org "Simple Logging Facade for Java (SLF4J)"