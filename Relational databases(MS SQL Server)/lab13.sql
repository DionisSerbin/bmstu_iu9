
use master;
go

if DB_ID (N'lab13_1') is not null
drop database lab13_1;
go

create database lab13_1
ON (
    NAME = lab131primary,
    FILENAME = '/var/opt/mssql/data/lab131primary.mdf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
),
(
    NAME = lab131secondary,
    FILENAME = '/var/opt/mssql/data/lab131secondary.ndf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
)
LOG ON
(
    NAME = lab131log,
    FILENAME = '/var/opt/mssql/data/lab131log.ldf',
    SIZE = 5,
    MAXSIZE = 20,
    FILEGROWTH = 5
);
go

use master;
go

if DB_ID (N'lab13_2') is not null
drop database lab13_2;
go

create database lab13_2
on (
    NAME = lab132primary,
    FILENAME = '/var/opt/mssql/data/lab132primary.mdf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
),
(
    NAME = lab132secondary,
    FILENAME = '/var/opt/mssql/data/lab132secondary.ndf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
)
LOG ON
(
    NAME = lab132log,
    FILENAME = '/var/opt/mssql/data/lab132log.ldf',
    SIZE = 5,
    MAXSIZE = 20,
    FILEGROWTH = 5
);

use lab13_1;
go

if OBJECT_ID(N'Users',N'U') is NOT NULL
	DROP TABLE Users;
go

CREATE TABLE Users (
	user_info_id int PRIMARY KEY NOT NULL,
	e_mail varchar(50) NOT NULL,
	surname char(30) NOT NULL,
	name char(30) NOT NULL,
	patronymic char(35) NOT NULL,
	phone char(11) NOT NULL,
	CONSTRAINT Seq_users_more CHECK (user_info_id <= 3)
	);
go

use lab13_2;
go

if OBJECT_ID(N'Users',N'U') is NOT NULL
	DROP TABLE Users;
go

CREATE TABLE Users (
	user_info_id int PRIMARY KEY NOT NULL,
	e_mail varchar(50) NOT NULL,
	surname char(30) NOT NULL,
	name char(30) NOT NULL,
	patronymic char(35) NOT NULL,
	phone char(11) NOT NULL,
	CONSTRAINT Seq_users_more CHECK (user_info_id > 3)
	);
go

use lab13_1;
go

if OBJECT_ID(N'UserView',N'V') is NOT NULL
	DROP VIEW UserView;
go

CREATE VIEW UserView AS
	SELECT * FROM lab13_1.dbo.Users
	UNION ALL
	SELECT * FROM lab13_2.dbo.Users
go

use lab13_2;
go

if OBJECT_ID(N'UserView',N'V') is NOT NULL
	DROP VIEW UserView;
go

CREATE VIEW UserView AS
	SELECT * FROM lab13_1.dbo.Users
	UNION ALL
	SELECT * FROM lab13_2.dbo.Users
go

INSERT INTO UserView VALUES
	(1,'reaganusm@gmail.com','Serbin','Denis', 'Andreevich','88005553535'),
	(2,'kuzzia@mail.ru','Kuzovchikov','Maks','Petrovich','8800808080'),
	(3,'barashkov@ya.ru','Barashkov','Georgy','Nikolaevich','38567863281'),
	(4,'maxudov@ya.ru','Maxudov','Maxud','Maxudovich','8924731913'),
	(5,'zhuk@mail.ru','Zhuk','Dmitriy','Dmitrievich','89274287742')


SELECT * FROM UserView;

SELECT * from lab13_1.dbo.Users;
SELECT * from lab13_2.dbo.Users;


DELETE FROM UserView WHERE patronymic = 'Nikolaevich'

SELECT * from lab13_1.dbo.Users;
SELECT * from lab13_2.dbo.Users;


UPDATE UserView SET e_mail = 'reaganusmc@mail.ru' WHERE name = 'Denis'

SELECT * from lab13_1.dbo.Users;
SELECT * from lab13_2.dbo.Users;