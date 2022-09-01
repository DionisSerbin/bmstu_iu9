use master;
go
if DB_ID (N'lab9') is not null
drop database lab9;
go

CREATE DATABASE lab9
ON PRIMARY
(
    NAME = lab9primary,
    FILENAME = '/var/opt/mssql/data/lab9primary.mdf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
),
(
    NAME = lab9secondary,
    FILENAME = '/var/opt/mssql/data/lab9secondary.ndf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
)
LOG ON
(
    NAME = lab9log,
    FILENAME = '/var/opt/mssql/data/lab9log.ldf',
    SIZE = 5,
    MAXSIZE = 20,
    FILEGROWTH = 5
);
GO

use lab9;
go

IF OBJECT_ID(N'School_new',N'U') is NOT NULL
	DROP TABLE School_new;
GO

IF OBJECT_ID(N'Uniq_School',N'F') IS NOT NULL
	ALTER TABLE School_new DROP CONSTRAINT Uniq_School
GO


CREATE TABLE School_new (
	SchoolID int IDENTITY(1,1) PRIMARY KEY ,
    Address varchar(100) NOT NULL,
    Rating float NULL ,
    SchoolType varchar(50) NULL DEFAULT ('Ordinary')
    CONSTRAINT Uniq_School UNIQUE (Address)
);
GO

IF OBJECT_ID(N'Teacher',N'U') is NOT NULL
	DROP TABLE Teacher;
GO

IF OBJECT_ID(N'FK_School_new', N'F') IS NOT NULL
    ALTER TABLE Teacher DROP CONSTRAINT FK_School_new
GO

IF OBJECT_ID(N'Uniq_teacher', N'F') IS NOT NULL
    ALTER TABLE Teacher DROP CONSTRAINT Uniq_teacher
GO

CREATE TABLE Teacher (
	TeacherID int IDENTITY(1,1) PRIMARY KEY,
	TeacherGender varchar(2) DEFAULT ('Unknown'),
	SchoolID int NULL,
	TeacherName varchar(100) DEFAULT ('Unknown'),
	CONSTRAINT FK_School_new FOREIGN KEY (SchoolID) REFERENCES School_new (SchoolID),
    CONSTRAINT Uniq_teacher UNIQUE (TeacherName, TeacherGender)
	);
GO



INSERT INTO School_new(Address, Rating, SchoolType)
    VALUES ('Moscow, bauman st., 8', 8.5, 'Licei'),
           ('Moscow, Serpuckovkaya st., 5', 9.1, 'Gumnasiya')
GO


INSERT INTO School_new(Address, Rating)
VALUES ('Saint-Petersburg, Nevskiy st., 9', 7.6),
           ('Lubertsy, Aviatorov st., 10', 6.0),
           ('Kazan, Moscow st., 10', 6.5)


INSERT INTO Teacher( TeacherGender, TeacherName, SchoolID)
    VALUES ( N'W', N'Antonina Nikolaevna',1),
            (N'M', N'Vasiliy Petrovich', 2),
           ( N'W', N'Tatyana Nikolaevna', 3),
           ( N'W', N'Irina Vasilevna', 4),
           ( N'W', N'Julia Viktorovna', 5)
GO


use lab9;
go

-- Триггер на удаление --

IF OBJECT_ID(N'Delete_school',N'TR') IS NOT NULL
	DROP TRIGGER Delete_school
go

CREATE TRIGGER Delete_school
	ON School_new
	INSTEAD OF DELETE
AS
	BEGIN
				UPDATE Teacher SET SchoolID = NULL WHERE SchoolID IN (SELECT SchoolID FROM deleted)

				-- DELETE l FROM SchoolID AS l INNER JOIN deleted AS d ON l.library_id = d.library_id
				DELETE FROM School_new WHERE SchoolID IN (SELECT SchoolID FROM deleted)
				IF (SELECT DISTINCT COUNT(*) FROM deleted) > 1
					PRINT N'Школы удалены из таблицы и все учителя из школ стали безработные'
				ELSE
					PRINT N'Школа удалена из таблицы и все учителя из школы стали безработные'
	END
go

/*DELETE FROM School_new WHERE schoolID in (1)
SELECT * FROM Teacher
SELECT * FROM School_new
go*/


-- Триггер на обновление --

IF OBJECT_ID(N'Update_info_school',N'TR') IS NOT NULL
	DROP TRIGGER Update_info_school
go

CREATE TRIGGER Update_info_school
	ON School_new
	AFTER UPDATE
AS
	BEGIN
		IF (UPDATE(Address) AND EXISTS (SELECT TOP 1 Address FROM deleted WHERE Address is not NULL))
			BEGIN;
				EXEC sp_addmessage 50002, 15,N'Изменение местоположения школы невозможно! Нужно ипсользовать: удаления данной школы и создания ей записи в таблице',@lang='us_english',@replace='REPLACE';
				RAISERROR(50002,15,-1)
			END;
		ELSE
			BEGIN;
				DECLARE @temp_table TABLE (
					SchoolID int PRIMARY KEY,
					add_rating float, add_address varchar(100),add_schoolType varchar(50),
					delete_rating float, delete_address varchar(100),delete_schoolType varchar(50)
				);

				INSERT INTO @temp_table(SchoolID, add_rating, add_address, add_schoolType,
				                        delete_rating, delete_address, delete_schoolType)
				SELECT A.SchoolID, A.Rating, A.Address, A.SchoolType,
				       B.Rating, B.Address, B.SchoolType
				FROM inserted A
				INNER JOIN deleted B ON A.SchoolID = B.SchoolID

				IF UPDATE(Address)
					PRINT N'Добавлен адрес'
				IF UPDATE(Rating)
					PRINT N'Изменен рейтинг'
				IF UPDATE(SchoolType)
					PRINT N'Изменен тип школы'

				DECLARE @number int;
				SET @number = (SELECT DISTINCT COUNT(*) FROM @temp_table);
				IF @number > 1
					PRINT N'у ' + CAST(@number AS VARCHAR(1)) + N' школ'
				ELSE
					PRINT N'у 1 школы'
		    END;
	END
go

UPDATE School_new SET Address = 'New address' WHERE Rating = 10
SELECT * FROM School_new
go

UPDATE School_new SET Rating = 10 WHERE schoolID = 2
SELECT * FROM School_new
go

-- Триггер на вставку --

IF OBJECT_ID(N'Add_school',N'TR') IS NOT NULL
	DROP TRIGGER Add_school
go

CREATE TRIGGER Add_school
	ON School_new
	AFTER INSERT
AS
	BEGIN
		IF (SELECT DISTINCT COUNT(*) FROM inserted) > 1
			PRINT N'Добавлены новые школы в таблицу'
		ELSE
			PRINT N'Добавлена новая школа в таблицу'
	END
go

INSERT INTO School_new(address, rating, schooltype)
VALUES ('Ryazan, Moscow st., 10',5,N'Licei'),
	   ('Krasnoyarsk, Moscow st., 10',6,N'Ordinary')
SELECT * FROM School_new
go

-- Представление

if OBJECT_ID(N'JoinSchoolView',N'V') is NOT NULL
	DROP VIEW JoinSchoolView;
go

CREATE VIEW JoinSchoolView AS
	SELECT t.TeacherGender as TeacherGender,t.TeacherName as TeacherName,
	       s.Address as SchoolAddress
	FROM School_new as s INNER JOIN Teacher as t ON s.SchoolID = t.SchoolID
go

IF OBJECT_ID(N'Add_View_school',N'TR') IS NOT NULL
	DROP TRIGGER Add_View_school
go

CREATE TRIGGER Add_View_school
	ON JoinSchoolView
	INSTEAD OF INSERT
AS
	BEGIN

		DECLARE @temp_table TABLE (
					add_TeacherName varchar(100), add_TeacherGender varchar(2), add_SchoolID int,
					add_Address varchar(100)
				);


		INSERT INTO @temp_table(add_TeacherName, add_TeacherGender, add_SchoolID, add_Address)
		SELECT A.TeacherName,A.TeacherGender,B.SchoolID, A.SchoolAddress
		FROM inserted A
		LEFT JOIN School_new B
		ON A.SchoolAddress = B.Address


		INSERT INTO School_new(Address)
		SELECT add_Address
		FROM @temp_table
		WHERE add_SchoolID IS NULL

		UPDATE @temp_table SET add_SchoolID = (SELECT SchoolID FROM School_new WHERE Address = add_Address)

		INSERT INTO Teacher(TeacherGender, SchoolID, TeacherName)
		SELECT add_TeacherGender, add_SchoolID, add_TeacherName
		FROM @temp_table

		PRINT N'Добавлены новые учителя'
	END
GO

INSERT INTO JoinSchoolView(TeacherGender, TeacherName, SchoolAddress)
VALUES ('M', 'Sergei Dmitrov', 'Kaliningrad, Serpuckovkaya st., 5'),
       ('W', 'Andrey Ozer', 'Tumen, Serkaya st., 5')

SELECT * FROM School_new
SELECT * FROM Teacher
GO

IF OBJECT_ID(N'Delete_View_school',N'TR') IS NOT NULL
	DROP TRIGGER Delete_View_school
go

CREATE TRIGGER Delete_View_school
	ON JoinSchoolView
	INSTEAD OF DELETE
AS
	BEGIN
	    --DELETE FROM School_new WHERE Address IN (SELECT SchoolAddress FROM deleted)
		DELETE FROM Teacher WHERE TeacherName IN (SELECT TeacherName FROM deleted)
	END
go

--Delete FROM JoinSchoolView WHERE (SchoolAddress =N'Kazan, Moscow st., 10' AND TeacherName = N'Julia Viktorovna')

IF OBJECT_ID(N'Update_View_school',N'TR') IS NOT NULL
	DROP TRIGGER Update_View_school
go

CREATE TRIGGER Update_View_school
	ON JoinSchoolView
	INSTEAD OF UPDATE
AS
	BEGIN

	    IF UPDATE(TeacherName)
				UPDATE Teacher SET TeacherName = (SELECT TeacherName FROM inserted) WHERE TeacherName IN (SELECT TeacherName FROM inserted)
	    IF UPDATE(TeacherGender)
				UPDATE Teacher SET TeacherGender = (SELECT TeacherGender FROM inserted ) WHERE TeacherName IN (SELECT TeacherName FROM inserted)
        IF UPDATE(SchoolAddress)
	    BEGIN
				EXEC sp_addmessage 50004, 15,N'Запрещено изменять адрес',@lang='us_english',@replace='REPLACE';
				RAISERROR(50004,15,-1)
		END
	END
GO

use lab9;

UPDATE JoinSchoolView SET TeacherGender = N'M' --WHERE TeacherName = N'Andrey Ozer'
SELECT * FROM Teacher
UPDATE JoinSchoolView SET TeacherName = TeacherName + 'x'
SELECT * FROM Teacher
