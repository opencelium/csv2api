CSV to API
===========

Dynamically generate RESTful APIs from static CSVs. Provides JSON

What Problem This Solves
------------------------

The simplicity with which CSV files can be created has made them the default data format for bulk data. It is comparatively more difficult to create an API to share the same data atomically and transactionally.

What You Need
-------------

* JDK 17 or later
* Gradle 6.8+

How To Start
------------

1. Download and unzip the source repository for this guide, or clone it using Git: `git clone` https://github.com/opencelium/csv2api
2. By default, the embedded server starts on `port 8080` but you are able to change `server.port` property in csvtoapi/src/main/resources/application.yml
3. Go to root folder of the project `csvtoapi` and type command `gradle build` in terminal 
4. Switching to `/build/libs` and then running `java -jar artifactname`


Arguments
---------

* `source`: the URL to the source CSV

Example Usage
-------------

All examples use [data from HERE](https://people.sc.fsu.edu/~jburkardt/data/csv)

### Get CSV as JSONP (default behavior)
http://[Csv2APIServer]:8080?source=https://people.sc.fsu.edu/~jburkardt/data/csv/deniro.csv
