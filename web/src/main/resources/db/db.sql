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

insert into user_info(code,name,password,sex,is_system,birthday,status)
values('admin','系统管理员','8c16fa7743119806880db2cc4780f576',1,1,'1990-0212:10:23:59',0),
('zjy','曾军毅','25c278f2b306449559f6b79b36c7b2e9',0,0,'1990-0212:10:23:59',0);

create table role_info
(
	id INTEGER NOT NULL PRIMARY KEY autoincrement,
	name varchar(50),
	code varchar(50),
	seq int
);

insert into role_info(name, code, seq)
values('管理员','admin',0),('普通用户','user',1);

create table user_role
(
	user_id INTEGER,
	role_id INTEGER
);


insert into user_role(user_id, role_id)
values(1,1),(2,2);

create table menu
(
	id INTEGER NOT NULL PRIMARY KEY autoincrement,
	pid INTEGER,
	name VARCHAR(50),
	code VARCHAR(50),
	url VARCHAR(200),
	seq INTEGER,
	icon VARCHAR(100)
);

create table function_info
(
	id INTEGER NOT NULL PRIMARY KEY autoincrement,
	name varchar(50),
	menu_id INTEGER,
	code varchar(50),
	path varchar(200),
	seq int
);

create table permission
(
	id INTEGER NOT NULL PRIMARY KEY autoincrement,
	function_id INTEGER,
	name VARCHAR(50),
	code VARCHAR(50),
	seq INTEGER
);

create table role_permission
(
	role_id INTEGER,
	permission_id INTEGER
);


create table user_permission
(
	user_id INTEGER,
	permission_id INTEGER,
	type tinyint
);