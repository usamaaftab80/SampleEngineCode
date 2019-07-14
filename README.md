# README #

## A Simple Demo Project ##
* Use as a template for new engines
* Example code in a simplified context

### To run this project locally, do the following: ###
* Clone repository
* Open Eclipse
* Select File > Import...
* Select Maven > Existing Maven Project
* Context click on Project in Project Explorer and select Run As > Maven Install
* Context click on Project in Project Explorer and select Debug As > Run with Jetty (Set Context Path to EngineSeed)

### Fix the "No connection specified for project. No database-specific validation will be performed." Problem ###
* Context click on Project in Project Explorer and select Properties
* Select JPA
* Under Platform, choose EclipseLink 2.5.x
* Under JPA implementation, Type, choose User Library and select EclipseLink 2.5.2
* Under Connection, click "Add connection..." and create a new SQL Server Connection Profile named "Dev SQL Server"
* Click "Next"
* Download sqljdbc4-4.0.jar and save to a new folder called jpa in the root of the Eclipse installation
* Create a new Driver for the Database "Microsoft SQL Server 2008 JDBC Driver" using this jar and the correct database connection details as listed in the POM file and persistence.xml
* Complete documentation... Problem should go away