use master;
go
if DB_ID (N'lab8') is not null
drop database lab8;
go

CREATE DATABASE lab8
ON PRIMARY
(
    NAME = lab8primary,
    FILENAME = '/var/opt/mssql/data/lab8primary.mdf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
),
(
    NAME = lab8secondary,
    FILENAME = '/var/opt/mssql/data/lab8secondary.ndf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
)
LOG ON
(
    NAME = lab8log,
    FILENAME = '/var/opt/mssql/data/lab8log.ldf',
    SIZE = 5,
    MAXSIZE = 20,
    FILEGROWTH = 5
);
GO

use lab8;
go

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

DECLARE @school_cursor CURSOR;
EXECUTE dbo.select_procedure @cursor = @school_cursor OUTPUT;

FETCH NEXT FROM @school_cursor;
WHILE (@@FETCH_STATUS = 0)
BEGIN
	FETCH NEXT FROM @school_cursor;
END

CLOSE @school_cursor;
DEALLOCATE @school_cursor;
GO

IF OBJECT_ID(N'generate_random',N'FN') IS NOT NULL
	DROP FUNCTION generate_random
go

IF OBJECT_ID(N'view_number',N'V') IS NOT NULL
	DROP VIEW view_number
go

CREATE VIEW view_number AS
	SELECT CAST(CAST(NEWID() AS binary(3)) AS INT) AS NextID
go

CREATE FUNCTION generate_random(@a int,@b int)
	RETURNS int
	AS
		BEGIN
			DECLARE @number int
			SELECT TOP 1 @number = NextID from view_number
			SET @number = @number % @b + @a
			RETURN (@number)
		END;
go

IF OBJECT_ID(N'dbo.select_proc_with_new_column', N'P') IS NOT NULL
	DROP PROCEDURE dbo.select_proc_with_new_column
GO

CREATE PROCEDURE dbo.select_proc_with_new_column
	@cursor CURSOR VARYING OUTPUT
AS
	SET @cursor = CURSOR
	FORWARD_ONLY STATIC FOR
	SELECT Address, Rating, SchoolType,dbo.generate_random(1,100) as RandomNumber
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
		SELECT Address, Rating, SchoolType,dbo.generate_random(1,100) as RandomNumber
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