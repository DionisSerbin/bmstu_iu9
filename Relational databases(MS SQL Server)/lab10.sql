use master;
go
if DB_ID (N'lab10') is not null
drop database lab10;
go

CREATE DATABASE lab10
ON PRIMARY
(
    NAME = lab9primary,
    FILENAME = '/var/opt/mssql/data/lab10primary.mdf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
),
(
    NAME = lab10secondary,
    FILENAME = '/var/opt/mssql/data/lab10secondary.ndf',
    SIZE = 10,
    MAXSIZE = UNLIMITED,
    FILEGROWTH = 5
)
LOG ON
(
    NAME = lab10log,
    FILENAME = '/var/opt/mssql/data/lab10log.ldf',
    SIZE = 5,
    MAXSIZE = 20,
    FILEGROWTH = 5
);
GO

use lab10;
go
if OBJECT_ID(N'CardBalance',N'U') IS NOT NULL
	DROP TABLE CardBalance;
go

CREATE TABLE CardBalance (
	card_id int PRIMARY KEY,
	name nchar(50) NULL,
	surname nchar(50) NULL,
	card_number numeric(17) NULL,
	card_type nchar(25) NOT NULL,
	balance money NOT NULL
);
go

INSERT INTO CardBalance(card_id,card_number,card_type,balance)
VALUES (1,4716669340945103,N'Visa',CAST($1500.00 as money)),
	   (2,5410198144622144,N'Mastercard',CAST($2500.00 as money)),
	   (3,6011391679890134,N'Discover',CAST($700.00 as money)),
	   (4,377819086330733,N'American Express',CAST($260.00 as money))
go

use lab10;
go

-- 4 уровня изоляции
-- незавершенное чтение
-- завершенное чтение
-- воспроизводимое чтение
-- сериализуемость

-- 1 уровень изоляции
-- незавершенное чтение (гарантирует только отсутствие потерянных обновлений)
-- Если несколько параллельных транзакций пытаются измнять одну и ту же строку таблицы,
-- то в окончательном варианте строка будет иметь значение, определенное всем набором успшно выполненных транзакций.
--Транзакции выполняющие только чтение при данном уровне изоляции никогда не блокируются

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
    BEGIN TRANSACTION;
    UPDATE CardBalance SET balance = balance * 10 WHERE card_id = 3;
    SELECT * FROM CardBalance;
    SELECT * FROM sys.dm_tran_locks;
    COMMIT TRANSACTION;
go

-- 2 уровень изоляции
-- завершеное чтение(обеспечивается защита от «грязного» чтения);
--в процессе работы одной транзакции другая может быть успешно завершена и сделанные ею изменения зафиксированы
--читающая транзакция блокирует читаемые данные в разделяемом (shared) режиме,
-- в результате чего параллельная транзакция, пытающаяся изменить эти днные, приостанавливается,
-- а пишущая транзакция блокирует изменяемые данные для читающих транзакций, работающих на уровне read committed или более высоком,
-- до своего завершения, препятствую «грязному» чтению.
-- При изменении строки СУБД создаёт новую версию этой строки, продолжает работать изменившая данные транзакция, любой другой «читающей» транзакции
-- возвращается последняя зафиксированная версия. обеспечивается большая скорость так как предотвращается блокировки

SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    BEGIN TRANSACTION;
    SELECT * FROM CardBalance;
    WAITFOR DELAY '00:00:05';
    SELECT * FROM CardBalance;
    SELECT * FROM sys.dm_tran_locks;
    COMMIT TRANSACTION;
go

BEGIN TRANSACTION;
	SELECT * FROM CardBalance;
	UPDATE CardBalance SET balance = balance * 10 WHERE card_id = 3;

	SELECT * FROM CardBalance;
	SELECT * FROM sys.dm_tran_locks;
COMMIT TRANSACTION;
go

-- 3 уровень изоляции:
-- воспроизводимое чтение(читающая транзакция «не видит» изменения данных, которые были ею ранее прочитаны)
--никакая другая транзакция не может изменять данные, читаемые текущей транзакцией, пока та не окончена
-- Блокировки в разделяющем режиме применяются ко всем данным, считываемым любой инструкцией транзакции, и сохраняются до её завершения
-- Это запрещает другим транзакциям изменять строки, котрые были считаны незавершённой транзакцией
-- Однако другие транзакции могут вставлять новые строки, соответствующие условиям поиска инструкций, содержащиесся в текущей транзакции
-- При повторном запуске инструкции текущей транзакцией будут извлечены новые строки, что приведёт к фантомному чтению

SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
    BEGIN TRANSACTION;
    INSERT INTO CardBalance (card_id,card_number,card_type,balance) VALUES(5,869983803603635,N'Voyager',CAST($750.00 as money))
    SELECT * FROM CardBalance
    WAITFOR DELAY '00:00:10'
    SELECT * FROM CardBalance
    SELECT * FROM sys.dm_tran_locks;
    COMMIT TRANSACTION;
go

-- 4 уровень изоляции:
-- сериализуемость        (Самый высокий уровень изолированности);
-- Транзакции полностью изолируются друг от друга, каждая выполняется так, как будто параллельных транзакций не существует.
-- Только на этом уровне параллельные транзакции не подвержены эфекту «фантомного чтения».

SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
    BEGIN TRANSACTION;
INSERT INTO CardBalance (card_id,card_number,card_type,balance) VALUES(6,6011658933962846,N'MIR',CAST($1430.00 as money))
    SELECT * FROM CardBalance
    WAITFOR DELAY '00:00:05'
    SELECT * FROM CardBalance
    SELECT * FROM sys.dm_tran_locks;
    COMMIT TRANSACTION;
go

