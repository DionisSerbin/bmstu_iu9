USE master;
GO

IF DB_ID (N'lab6') IS NOT NULL
DROP DATABASE lab6;
GO

CREATE DATABASE lab6
ON PRIMARY
(
    NAME = lab6primary,
    FILENAME = '/var/opt/mssql/data/lab6primary.mdf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
),
(
    NAME = lab6secondary,
    FILENAME = '/var/opt/mssql/data/lab6secondary.ndf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
)
LOG ON
(
    NAME = lab6log,
    FILENAME = '/var/opt/mssql/data/lab6log.ldf',
    SIZE = 5,
    MAXSIZE = 20,
    FILEGROWTH = 5
);
GO

USE lab6;
GO

if OBJECT_ID(N'Groupp',N'U') is NOT NULL
	DROP TABLE Groupp;
GO

if OBJECT_ID(N'School',N'U') is NOT NULL
	DROP TABLE School;
GO

CREATE TABLE School(
    SchoolID int IDENTITY(1,1) PRIMARY KEY ,
    Address varchar(100) NOT NULL,
    Rating float NOT NULL CHECK (Rating > 0 AND Rating < 10),
    SchoolType varchar(50) DEFAULT ('Ordinary')
)
GO

INSERT INTO School(Address, Rating, SchoolType)
    VALUES ('Moscow, bauman st., 8', 8.5, 'Licei'),
           ('Moscow, Serpuhovskaya st., 5', 9.1, 'Gumnasiya')
GO

INSERT INTO School(Address, Rating)
    VALUES ('Saint-Petersburg, Nevskiy st., 9', 7.6),
           ('Lubertsy, Aviatorov st., 10', 6.0),
           ('Kazan, Moscow st., 10', 6.5)
GO

SELECT SCOPE_IDENTITY() AS [SCOPE_IDENTITY()],
        IDENT_CURRENT('dbo.School') AS [IDENT_CURRENT('School')]
GO

SELECT * FROM School WHERE Rating > 7
GO

if OBJECT_ID(N'Groupp',N'U') is NOT NULL
	DROP TABLE Groupp;
GO

CREATE TABLE Groupp
(
    GroupID UNIQUEIDENTIFIER PRIMARY KEY DEFAULT (NEWID()),
    GroupSpecialization varchar(50) NOT NULL DEFAULT ('No specialization'),
    SchoolID int NOT NULL FOREIGN KEY REFERENCES School(SchoolID),
    GroupName varchar(50) NOT NULL
)

INSERT INTO Groupp(GroupSpecialization, SchoolID, GroupName)
VALUES ('Maths and phys', 2, '9 B'),
        ('Social economic', 3, '5 G')
GO

INSERT INTO Groupp(GroupID, SchoolID, GroupName)
    VALUES ('294B4012-1617-482A-8058-F73039852768', 4, '4 A')
GO

SELECT * FROM Groupp
GO

IF EXISTS (SELECT * FROM sys.sequences WHERE NAME = N'MySequence' AND TYPE='SO')
    DROP SEQUENCE MySequence
GO

CREATE SEQUENCE MySequence
	START WITH 0
	INCREMENT BY 1
	MAXVALUE 10;
GO

IF OBJECT_ID(N'City',N'U') IS NOT NULL
	DROP TABLE City;
GO

CREATE TABLE City (
	CityID int PRIMARY KEY NOT NULL,
	CityName varchar(50) DEFAULT (N'City'),
	);
GO

INSERT INTO City(CityID, CityName)
VALUES (NEXT VALUE FOR DBO.MySequence,N'Moscow'),
	   (NEXT VALUE FOR DBO.MySequence,N'Kazan'),
	   (NEXT VALUE FOR DBO.MySequence,N'London'),
	   (NEXT VALUE FOR DBO.MySequence,N'Washington'),
	   (NEXT VALUE FOR DBO.MySequence,N'Paris')
GO

SELECT * From City
GO

IF OBJECT_ID(N'FK_School_new',N'F') IS NOT NULL
	ALTER TABLE Teacher DROP CONSTRAINT FK_School_new
GO

IF OBJECT_ID(N'School_new',N'U') is NOT NULL
	DROP TABLE School_new;
GO

CREATE TABLE School_new (
	SchoolID int NOT NULL PRIMARY KEY ,
    Address varchar(100) NOT NULL,
    Rating float NOT NULL CHECK (Rating > 0 AND Rating < 10),
    SchoolType varchar(50) NOT NULL DEFAULT ('Ordinary')
);
GO

INSERT INTO School_new(SchoolID, Address, Rating, SchoolType)
    VALUES (1,'Moscow, bauman st., 8', 8.5, 'Licei'),
           (2,'Moscow, Serpuckovkaya st., 5', 9.1, 'Gumnasiya')
GO

INSERT INTO School_new(SchoolID,Address, Rating)
    VALUES (3,'Saint-Petersburg, Nevskiy st., 9', 7.6),
           (4,'Lubertsy, Aviatorov st., 10', 6.0),
           (5,'Kazan, Moscow st., 10', 6.5)
GO

IF OBJECT_ID(N'Teacher',N'U') is NOT NULL
	DROP TABLE Teacher;
GO

CREATE TABLE Teacher (
	TeacherID int IDENTITY(0,1) PRIMARY KEY,
	TeacherGender varchar(2) DEFAULT ('Unknown'),
	Teacher_School int NULL,
	TeacherName varchar(100) DEFAULT ('Unknown'),
	CONSTRAINT FK_School_new FOREIGN KEY (Teacher_School) REFERENCES School_new (SchoolID)
	 ON UPDATE CASCADE
     --ON UPDATE SET NULL
	--ON UPDATE SET DEFAULT
	ON DELETE CASCADE
	--ON DELETE SET NULL
	--ON DELETE SET DEFAULT
	);
GO

INSERT INTO Teacher( TeacherGender, TeacherName, Teacher_School)
    VALUES ( N'W', N'Antonina Nikolaevna',1),
            (N'M', N'Vasiliy Petrovich', 2),
           ( N'W', N'Tatyana Nikolaevna', 3),
           ( N'W', N'Irina Vasilevna', 4),
           ( N'W', N'Julia Viktorovna', 5)
GO

DELETE FROM School_new WHERE SchoolID = 3
UPDATE School_new SET SchoolID = 9 WHERE SchoolID = 5
GO





