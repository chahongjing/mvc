create table user_info
(
	id INTEGER NOT NULL PRIMARY KEY autoincrement,
	code VARCHAR(20)
		UNIQUE,
	name VARCHAR(20),
	password VARCHAR(50),
	sex SMALLINT,
	is_system SMALLINT,
	birthday DATE,
	status SMALLINT,
	created_by INTEGER,
	created_on DATE,
	modified_by INTEGER,
	modified_on DATE
);

insert into user_info(code,name,password,sex,is_system,birthday,status) values('admin','系统管理员','8c16fa7743119806880db2cc4780f576',1,1,'1990-0212:10:23:59',0);