USE master;
GO

IF DB_ID (N'lab5') is not null
DROP DATABASE lab5;
GO

CREATE DATABASE lab5
ON PRIMARY
(
    NAME = lab5primary,
    FILENAME = '/var/opt/mssql/data/lab5primary.mdf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
),
(
    NAME = lab5secondary,
    FILENAME = '/var/opt/mssql/data/lab5secondary.ndf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
)
LOG ON
(
    NAME = lab5log,
    FILENAME = '/var/opt/mssql/data/mylog5.ldf',
    SIZE = 5,
    MAXSIZE = 20,
    FILEGROWTH = 5
);
GO

USE lab5
GO

CREATE TABLE School(
    SchoolID int NOT NULL PRIMARY KEY ,
    Address varchar(100) NOT NULL,
    Rating float NOT NULL,
    SchoolType varchar(50) NOT NULL
)
GO

INSERT INTO School(SchoolID, Address, Rating, SchoolType)
VALUES ('0','Moscow, bauman st., 8','9.5', 'Licei')
GO

-- SELECT * FROM School
-- GO

use master;
GO

ALTER DATABASE lab5
    ADD FILEGROUP lab5_fg
GO

ALTER DATABASE lab5
ADD FILE
(
	NAME = lab5_new,
	FILENAME = '/var/opt/mssql/data/lab5_new.ndf',
	SIZE = 10MB,
	MAXSIZE = 100MB,
	FILEGROWTH = 5MB
)
TO FILEGROUP lab5_fg
GO

ALTER DATABASE lab5
	MODIFY FILEGROUP lab5_fg DEFAULT ;
GO

-- BACKUP DATABASE lab5
--     TO DISK = 'var/opt/mssql/data/lab5.Bak' WITH FORMAT ,
--     NAME  = 'Databse backup'


USE lab5
GO

CREATE TABLE Groupp
(
    GroupID int NOT NULL,
    GroupSpecialization varchar(50) NOT NULL ,
    SchoolID int NOT NULL FOREIGN KEY REFERENCES School(SchoolID),
    GroupName varchar(50) NOT NULL
)

INSERT INTO Groupp(GroupID, GroupSpecialization, SchoolID, GroupName)
VALUES ('1','Physical and math profile','0', '9 B')
GO

-- SELECT * FROM Groupp
-- GO

ALTER DATABASE lab5
	MODIFY FILEGROUP [primary] DEFAULT ;
GO

SELECT * INTO lab5.dbo.School_new
FROM School
GO

USE lab5;
GO

DROP TABLE Groupp
GO

ALTER DATABASE lab5
   REMOVE FILE lab5_new
GO

ALTER DATABASE lab5
    REMOVE FILEGROUP lab5_fg;
GO

USE lab5;
GO

CREATE SCHEMA school_schema
GO

ALTER SCHEMA school_schema TRANSFER dbo.School
GO

DROP TABLE school_schema.School
DROP SCHEMA school_schema
GO


-- use master
-- GO

--
-- RESTORE DATABASE lab5
-- FROM DISK = 'var/opt/mssql/data/lab5.Bak'  WITH REPLACE ;
-- GO
