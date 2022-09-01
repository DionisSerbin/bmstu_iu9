USE lab6;
GO

IF OBJECT_ID(N'SchoolView',N'V') IS NOT NULL
	DROP VIEW SchoolView;
GO

CREATE VIEW SchoolView AS
	SELECT *
	FROM School
	WHERE Rating BETWEEN 7 AND 9;
GO

SELECT * FROM SchoolView
GO

IF OBJECT_ID(N'SchoolTeacherView',N'V') IS NOT NULL
	DROP VIEW SchoolTeacherView;
GO

CREATE VIEW SchoolTeacherView AS
    SELECT s.Address, s.Rating, s.SchoolType,
           t.TeacherGender, t.TeacherName
    FROM School_new AS s INNER JOIN Teacher AS t ON SchoolID = t.Teacher_School
GO

IF EXISTS (SELECT * FROM sys.indexes  WHERE name = N'Index_Teacher_Name')
    DROP INDEX Index_Teacher_Name ON Teacher;
GO

Create INDEX Index_Teacher_Name
    ON Teacher (TeacherName)
        INCLUDE (TeacherGender)
GO

SELECT TeacherGender, TeacherName FROM Teacher WHERE TeacherGender = 'W'

IF OBJECT_ID(N'TeacherIndexView',N'V') IS NOT NULL
	DROP VIEW TeacherIndexView;
GO

CREATE VIEW TeacherIndexView
    WITH SCHEMABINDING
    AS
    SELECT TeacherName, TeacherGender
    FROM dbo.Teacher
    WHERE TeacherGender = 'W'
GO

IF EXISTS (SELECT * FROM sys.indexes  WHERE name = N'Index_Teacher_Gender')
    DROP INDEX Index_Teacher_Gender ON Teacher;
GO

DROP INDEX Index_Teacher_Name ON Teacher
GO

CREATE UNIQUE CLUSTERED INDEX Index_Teacher_Gender
    ON TeacherIndexView (TeacherName, TeacherGender)
GO

SELECT * FROM TeacherIndexView
GO




