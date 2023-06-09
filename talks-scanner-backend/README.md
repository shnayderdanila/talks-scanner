# talks-scanner-backend

## Description

This is API for talks scanner. Allows manage topics, ideas.

## How to start

### Postgresql

To connect to the database you need to move your CA.pem file to postgresql default directory for SSL certificates. CA.pem should rename to root.crt.

Default directory for OS
    
    Linux: ~/.postgresql/root.crt
    Windows: %APPDATA%\postgresql\root.crt



### Flyway

Flyway maintains a database based on resources/db/migration/*.sql file's

All sql code should be in this folder

Sql files must be properly named: V(version)__description.sql

Example: V1__initTable.sql

Flyway stores the history of migrations in the database by creating the table flyway_schema_history

Spring automatically runs migrations on application startup 

