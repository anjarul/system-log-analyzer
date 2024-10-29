# system-log-analyzer

This project is a Scala-based log analysis tool that uses Akka HTTP to expose a REST API for
processing system logs.

# Requirements
Make sure you have the following tools installed:

* Java 8 or higher
* Scala 3.3.4
* SBT
* Git

# Setup and Installation
1. Clone the repository:
   * git clone git@github.com:anjarul/system-log-analyzer.git
   * cd systemLogAnalyzer

2. Install dependencies:
   * sbt update


# Configuration
The application reads configuration values from the application.conf file located in the 
`src/main/resources/application.conf`
Make sure to update the **logFilePath** value to point to the correct log file on your system.
