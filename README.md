# Attendance Management Program

It is a desktop program developed using PostgreSQL databases, the Java programming language, and JavaFX to create interfaces

## Requirements

To run the program you need:

1.PostgreSQL database to store and retrieve data.
2.Netbeans 8.2 IDE to open the project.

## Steps
### Install database 
1. Download the PostgreSQL database and install it.
2. Create new database with any name as "attendance_management".
3. Create new schema but its name must be "managment" to run successfully.
4. From project folder open tables.sql and takes all commands to create tables.
5. In the project's folder you can take all commands in insert-data.sql to insert data to test the program.

### Open Project
#### Create Connection
1. After download netbeans 8.2 ide you can open the project using it.
2. Create new connection with database from Services->Databases->right click->New Connection.
3. From "Driver" choose the last option New Driver->Add->choose the file postgresql-42.6.0.jar from project folder->Libraries Driver->Database Driver.
4. Click Ok->Next.
5. Make sure the host is 127.0.0.1 or localhost and port 5432 if you did not change it during installation.
6. set Database that you created it in this case we set "attendance_management".
7. Enter username and password that you put them during installation then Next.
8. Create the schema that must be "managment".
9. You can change the name of connection as you like.
#### Edit DatabaseConnect.java
1. Open the DatabaseConnect.java .
2. Modify the url you can get it from the connection that created .
3. Right click on connection then "Properties" you will find "Database URL" then copy and past it.
4. Modify username and password that you put them during installation.



After following the steps properly, the program will work correctly and properly without any problems.
