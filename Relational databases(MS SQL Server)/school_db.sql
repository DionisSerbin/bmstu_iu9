USE master;
GO

IF DB_ID (N'labSchool') IS NOT NULL
DROP DATABASE labSchool;
GO

CREATE DATABASE labSchool
ON PRIMARY
(
    NAME = labSchoolprimary,
    FILENAME = '/var/opt/mssql/data/labSchoolprimary.mdf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
),
(
    NAME = labSchoolsecondary,
    FILENAME = '/var/opt/mssql/data/labSchoolsecondary.ndf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
)
LOG ON
(
    NAME = labSchoollog,
    FILENAME = '/var/opt/mssql/data/labSchoollog.ldf',
    SIZE = 5,
    MAXSIZE = 20,
    FILEGROWTH = 5
);
GO

USE labSchool;
GO

IF OBJECT_ID(N'NotOrdinarySchoolTeacherIndexView',N'V') IS NOT NULL
	DROP VIEW NotOrdinarySchoolTeacherIndexView;
GO

IF EXISTS (SELECT * FROM sys.indexes  WHERE name = N'Index_Not_Ordinary_School_Teacher_View')
    DROP INDEX Index_Not_Ordinary_School_Teacher_View ON Teacher;
GO

IF OBJECT_ID(N'FK_Group_Lesson',N'F') IS NOT NULL
	ALTER TABLE Lesson DROP CONSTRAINT FK_Group_Lesson
GO

IF OBJECT_ID(N'FK_Subject_Lesson',N'F') IS NOT NULL
	ALTER TABLE Lesson DROP CONSTRAINT FK_Subject_Lesson
GO

IF OBJECT_ID(N'FK_Teacher_Lesson',N'F') IS NOT NULL
	ALTER TABLE Lesson DROP CONSTRAINT FK_Teacher_Lesson
GO

if OBJECT_ID(N'Lesson',N'U') is NOT NULL
	DROP TABLE Lesson;
GO

if OBJECT_ID(N'Subject',N'U') is NOT NULL
	DROP TABLE Subject;
GO

IF OBJECT_ID(N'FK_Group_Student',N'F') IS NOT NULL
	ALTER TABLE Student DROP CONSTRAINT FK_Group_Student
GO

if OBJECT_ID(N'Student',N'U') is NOT NULL
	DROP TABLE Student;
GO

IF OBJECT_ID(N'FK_School_Group',N'F') IS NOT NULL
	ALTER TABLE Groupp DROP CONSTRAINT FK_School_Group
GO

if OBJECT_ID(N'Groupp',N'U') is NOT NULL
	DROP TABLE Groupp;
GO

IF OBJECT_ID(N'FK_School_Teacher',N'F') IS NOT NULL
	ALTER TABLE Teacher DROP CONSTRAINT FK_School_Teacher
GO

IF OBJECT_ID(N'Teacher',N'U') is NOT NULL
	DROP TABLE Teacher;
GO

if OBJECT_ID(N'School',N'U') is NOT NULL
	DROP TABLE School;
GO

CREATE TABLE School(
    schoolID int NOT NULL IDENTITY(1,1) PRIMARY KEY ,
    address varchar(100) NOT NULL,
    rating float NOT NULL CHECK (rating > 0 AND rating < 10),
    schoolType varchar(50) DEFAULT ('Ordinary')
)
GO

CREATE TABLE Groupp
(
    groupID int NOT NULL IDENTITY(1,1) PRIMARY KEY,
    groupSpecialization varchar(50) NOT NULL DEFAULT ('No specialization'),
    schoolID int NULL,
     CONSTRAINT FK_School_Group FOREIGN KEY (schoolID) REFERENCES School(schoolID)
    ON UPDATE CASCADE,
    groupName varchar(50) NOT NULL
)

CREATE TABLE Teacher (
	teacherID int IDENTITY(1,1) PRIMARY KEY,
	teacherAddress varchar(100) DEFAULT ('Unknown'),
	teacherPhoneNumber varchar(11) UNIQUE ,
	teacherGender varchar(2) DEFAULT ('Unknown'),
	teacherDateBith date DEFAULT ('Unknown'),
	schoolID int NULL,
	teacherName varchar(100) DEFAULT ('Unknown'),
	CONSTRAINT FK_School_Teacher FOREIGN KEY (schoolID) REFERENCES School (schoolID)
	ON UPDATE CASCADE
	);
GO

CREATE TABLE Student (
    studentID int IDENTITY(1,1) PRIMARY KEY,
    studentAddress varchar(100) DEFAULT ('Unknown'),
	studentPhoneNumber varchar(11) UNIQUE ,
	studentGender varchar(2) DEFAULT ('Unknown'),
	studentDateBith date DEFAULT ('Unknown'),
	studentName varchar(100) DEFAULT ('Unknown'),
	averageScore float NOT NULL CHECK (averageScore > 0 AND averageScore < 10),
	groupID int NULL ,
	CONSTRAINT FK_Group_Student FOREIGN KEY (groupID) REFERENCES Groupp (groupID)
    ON UPDATE CASCADE
    ON DELETE SET NULL
)
GO

CREATE TABLE Subject (
    subjectID int IDENTITY(1,1) PRIMARY KEY,
    hoursPerWeek int DEFAULT (0),
    subjectName varchar(50) DEFAULT ('Unknown'),
)
GO

CREATE TABLE Lesson (
    lessonID int IDENTITY(1,1) PRIMARY KEY,
    cabinet int DEFAULT (0),
    timeStart time DEFAULT ('00:00'),
    timeEnd time DEFAULT ('00:01'),
    weekday varchar(50),
    groupID int NULL,
    teacherID int NULL ,
    subjectID int NULL ,
    CONSTRAINT FK_Group_Lesson FOREIGN KEY (groupID) REFERENCES Groupp (groupID)
    ON UPDATE CASCADE
    ON DELETE CASCADE ,
    CONSTRAINT FK_Teacher_Lesson FOREIGN KEY (teacherID) REFERENCES Teacher (teacherID)
    ON DELETE SET NULL,
    CONSTRAINT FK_Subject_Lesson FOREIGN KEY (subjectID) REFERENCES Subject (subjectID)
    ON DELETE SET NULL
)
GO

INSERT INTO School(address, rating, schoolType)
    VALUES (N'Moscow, bauman st., 8', 8.5, 'Licei'),
           (N'Moscow, Serpuckovkaya st., 5', 9.1, 'Gumnasiya')
GO

INSERT INTO School(Address, Rating)
    VALUES (N'Saint-Petersburg, Nevskiy st., 9', 7.6)
GO

INSERT INTO Teacher(teacherAddress, teacherPhoneNumber,
                    teacherGender, teacherDateBith, schoolID, teacherName)
    VALUES ( 'Moscow, bauman st., 10', '89269098365',
            N'W','1975-05-21',
            1, N'Antonina Nikolaevna'),
            ('Moscow, bauman st., 24','89269078455',
             N'M', '1987-10-05',
             1, N'Vasiliy Petrovich'),
           ( 'Moscow, Serpuckovkaya st., 15', '89269068721',
            N'W','1979-08-30',
            2, N'Tatyana Nikolaevna'),
           ( N'Moscow, Serpuckovkaya st., 20', '89269095473',
            N'W','1980-04-15',
            2, N'Irina Vasilevna'),
           ( N'Saint-Petersburg, Nevskiy st., 1', '89269094411',
            N'W','1987-02-01',
            3, N'Julia Viktorovna'),
           ( N'Saint-Petersburg, Nevskiy st., 5', '89269097657',
            N'M','2001-11-24',
            3, N'Evgeniy Maksimov'),
            ( 'Moscow, Taganskaya st., 1', '89269097647',
            N'M','2000-12-12',
            2, N'Nikolai Maksimov')
GO

INSERT INTO Groupp(groupSpecialization, schoolID, groupName)
VALUES (N'Maths and phys', 1, N'9 B'),
       (N'Social economic', 3, N'10 A')
GO

INSERT INTO Groupp(schoolID, groupName)
    VALUES ( 2, '5 V')
GO

INSERT INTO Student(studentAddress, studentPhoneNumber, studentGender,
                    studentDateBith, studentName, averageScore, groupID)
VALUES (N'Moscow, bauman st., 10', '89269067421',
        N'M', '2005-10-04',
        N'Vladislav Shevelev', 6.5, 1),
       (N'Moscow, bauman st., 11', '89269068632',
        N'M', '2005-04-17',
        N'Andrey Ozer', 7.0, 1),
       (N'Moscow, Serpuckovkaya st., 5', '89269064653',
        N'M', '2004-11-19',
        N'Slava Kalkaev', 8.4, 2),
       (N'Moscow, Serpuckovkaya st., 6', '89269043454',
        N'W', '2004-12-10',
        N'Sasha Shulpina', 9.5, 2),
       (N'Saint-Petersburg, Nevskiy st., 3', '89264033565',
        N'W', '2010-01-02',
        N'Natalia Dzivalkovkaya', 8.4, 3),
       (N'Saint-Petersburg, Nevskiy st., 1', '89269066766',
        N'W', '2000-07-09',
        N'Valeria Kondratova', 8.8, 3)
GO

INSERT INTO Subject(hoursPerWeek, subjectName)
VALUES (6,N'Maths'),
       (2,N'Informatics'),
       (2,N'Chemistry'),
       (1,N'Geography'),
       (4,N'Russian language'),
       (3,N'Phisics')
GO

INSERT INTO Lesson(cabinet, timeStart, timeEnd, weekday, groupID, teacherID, subjectID)
VALUES (121, '08:30','09:15',
        'monday', 1, 1, 4),
       (305, '12:00','12:45',
        'tuesday', 1, 2, 3),
       (208,'13:30','14:15',
        'wednesday', 2, 3, 1),
       (407,'08:30','09:15',
        'thursday', 2, 4, 2),
       (105, '12:00','12:45',
        'friday',3, 5, 5),
       (330, '13:30', '14:15',
        'saturday', 3, 6,6)
GO


CREATE VIEW NotOrdinarySchoolTeacherIndexView
    WITH SCHEMABINDING
    AS
    SELECT t.teacherID, t.teacherAddress, t.teacherPhoneNumber,
           t.teacherGender, t.teacherDateBith, t.teacherName,
           s.schoolID, s.address, s.schoolType
    FROM dbo.School AS s INNER JOIN dbo.Teacher AS t on s.schoolID = t.schoolID
    WHERE schoolType != 'Ordinary'
GO

CREATE UNIQUE CLUSTERED INDEX Index_Not_Ordinary_School_Teacher_View
    ON NotOrdinarySchoolTeacherIndexView (teacherName, schoolType)
GO

SELECT teacherName, schoolType FROM NotOrdinarySchoolTeacherIndexView
GO

IF OBJECT_ID(N'dbo.select_procedure', N'P') IS NOT NULL
	DROP PROCEDURE dbo.select_procedure
GO

CREATE PROCEDURE dbo.select_procedure
	@cursor CURSOR VARYING OUTPUT
    AS
	SET @cursor = CURSOR
	FORWARD_ONLY STATIC FOR
	SELECT Address, Rating, SchoolType
	FROM School

	OPEN @cursor;
GO

IF OBJECT_ID(N'dbo.select_proc_with_new_column', N'P') IS NOT NULL
	DROP PROCEDURE dbo.select_proc_with_new_column
GO

CREATE PROCEDURE dbo.select_proc_with_new_column
	@cursor CURSOR VARYING OUTPUT
AS
	SET @cursor = CURSOR
	FORWARD_ONLY STATIC FOR
	SELECT Address, Rating, SchoolType
	FROM School

	OPEN @cursor;

GO

DECLARE @school_rating_cursor CURSOR;
EXECUTE dbo.select_proc_with_new_column @cursor = @school_rating_cursor OUTPUT;

FETCH NEXT FROM @school_rating_cursor;
WHILE (@@FETCH_STATUS = 0)
	BEGIN
		FETCH NEXT FROM @school_rating_cursor;
	END

CLOSE @school_rating_cursor;
DEALLOCATE @school_rating_cursor;
GO

IF OBJECT_ID(N'high_rating',N'FN') IS NOT NULL
	DROP FUNCTION high_rating
go

CREATE FUNCTION high_rating(@a float)
	RETURNS varchar(20)
	AS
		BEGIN
			DECLARE @result varchar(20)
			IF (@a > 8)
				SET @result = 'high rating school'
			ELSE
				SET @result = 'low rating school'
			RETURN (@result)
		END;
go

IF OBJECT_ID(N'dbo.rating_procedure',N'P') IS NOT NULL
	DROP PROCEDURE dbo.rating_procedure
GO

CREATE PROCEDURE dbo.rating_procedure
AS
	DECLARE @rating_cursor CURSOR;
	DECLARE	@table_address varchar(100);
	DECLARE @table_rating float;
	DECLARE @table_schoolType varchar(50);

	EXECUTE dbo.select_procedure @cursor = @rating_cursor OUTPUT;

	FETCH NEXT FROM @rating_cursor INTO @table_address, @table_rating, @table_schoolType

	WHILE (@@FETCH_STATUS = 0)
	BEGIN
		IF (dbo.high_rating (@table_rating) = 'high rating school')
			PRINT @table_address + ' ' + @table_schoolType + ' :' + N' is high rating school'
		ELSE
			PRINT @table_address + ' ' + @table_schoolType + ' :' + N' is low rating school'
		FETCH NEXT FROM @rating_cursor INTO @table_address, @table_rating, @table_schoolType;
	END

	CLOSE @rating_cursor;
	DEALLOCATE @rating_cursor;

GO

EXECUTE dbo.rating_procedure
GO

IF OBJECT_ID(N'my_table_function',N'TF') IS NOT NULL
	DROP FUNCTION my_table_function
go

CREATE FUNCTION my_table_function()
	RETURNS TABLE
AS
	RETURN (
		SELECT Address, Rating, SchoolType
		FROM School
		WHERE (dbo.high_rating (Rating) = 'high rating school')
	)
GO

ALTER PROCEDURE dbo.select_proc_with_new_column
	@cursor CURSOR VARYING OUTPUT
AS
	SET @cursor = CURSOR
	FORWARD_ONLY STATIC FOR
	SELECT * FROM dbo.my_table_function()
	OPEN @cursor;
GO

DECLARE @school_table_cursor CURSOR;
EXECUTE dbo.select_proc_with_new_column @cursor = @school_table_cursor OUTPUT;

FETCH NEXT FROM @school_table_cursor;
WHILE (@@FETCH_STATUS = 0)
	BEGIN
		FETCH NEXT FROM @school_table_cursor;
	END

CLOSE @school_table_cursor;
DEALLOCATE @school_table_cursor;
GO

CREATE TRIGGER Trigger_Delete_School
    ON School
    INSTEAD OF DELETE
    AS
    BEGIN
        UPDATE Teacher SET schoolID = NULL WHERE schoolID IN (SELECT schoolID FROM deleted)
        UPDATE Groupp SET schoolID = NULL WHERE schoolID IN (SELECT schoolID FROM deleted)
        UPDATE Student SET groupID = NULL WHERE groupID in (SELECT groupID FROM Groupp WHERE schoolID is NULL)
        DELETE FROM Lesson WHERE groupID in (SELECT groupID FROM  Groupp WHERE Groupp.schoolID is NULL)
		DELETE FROM School WHERE schoolID IN (SELECT schoolID FROM deleted)

		PRINT N'Школы удалены из таблицы, все учителя из школ стали безработные, группы расформированны, уроки отменены'

    END
GO

--DELETE From School WHERE schoolID = 1
SELECT * FROM Student WHERE averageScore > 8
UPDATE Student SET averageScore = 8 WHERE averageScore BETWEEN 0 and 8
--DELETE FROM Groupp WHERE  groupID = 1
--DELETE FROM Teacher WHERE teacherID = 1
--DELETE FROM Subject WHERE subjectName = 'Maths'
SELECT DISTINCT schoolID FROM Teacher
SELECT studentName FROM Student ORDER BY averageScore DESC
SELECT studentName, AVG(averageScore) as AVG FROM Student GROUP BY studentName HAVING AVG(averageScore) > 9

SELECT studentName, averageScore FROM Student WHERE studentGender = 'M'
UNION SELECT studentName,averageScore FROM dbo.Student ORDER BY averageScore DESC

SELECT teacherName, teacherAddress FROM Teacher
INNER JOIN dbo.Lesson L on Teacher.teacherID = L.teacherID

SELECT * FROM Groupp WHERE groupName LIKE '5%'

SELECT * FROM Teacher WHERE teacherName is NOT NULL

SELECT schoolType, address, rating FROM School WHERE EXISTS(SELECT schoolType as ST, address as A, rating as R WHERE rating > 9) ORDER BY rating DESC











