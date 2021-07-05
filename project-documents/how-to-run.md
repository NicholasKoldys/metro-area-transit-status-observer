## How To Run the application.

If you are just interested in running the project quickly, you can use the following commands in your command line to run the application..
```
cd "metro-area-transit-status-observer"
```
```
gradlew run
```
The application may take some time if you have not used gradle with this  project's version before.

Building the project for its .jar file is not recommended as the build will not have an attached database.  Please use the application with its source only and run through gradle's ```gradlew run``` command.
<br />
<br />
The database used is HSQLDB and is only used for testing purposes and to keep the applications database off the web.  The database will create a folder ```DB_MATSO``` and load the database structure dynamically through java code.