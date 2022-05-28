create table user_info
(
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	code VARCHAR(20)
		UNIQUE,
	name VARCHAR(20),
	password VARCHAR(50),
	sex SMALLINT,
	type SMALLINT,
	birthday DATE,
	status SMALLINT,
	created_by INTEGER,
	created_on DATE,
	modified_by INTEGER,
	modified_on DATE
);
insert into user_info(id,code,name,password,sex,type,birthday,status,created_on)
values(1, 'admin','系统管理员','8c16fa7743119806880db2cc4780f576',1,0,'1990-02-12 10:23:59',0,'2022-05-26 10:23:59'),
(2, 'zjy','曾军毅','25c278f2b306449559f6b79b36c7b2e9',0,1,'1990-02-12 10:23:59',0,'2022-05-26 10:23:59');

create table role_info
(
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	name varchar(50),
	code varchar(50),
	seq int
);
insert into role_info(id,name, code, seq)
values(1,'管理员','admin',0),(2, '普通用户','user',1);

create table user_role
(
	user_id INTEGER,
	role_id INTEGER
);
insert into user_role(user_id, role_id)
values(1,1),(2,2);


create table menu
(
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	pid INTEGER,
	name VARCHAR(50),
	code VARCHAR(50),
	url VARCHAR(200),
	seq INTEGER,
	icon VARCHAR(100)
);
insert into menu(id, pid, name, code, url, seq, icon)
values(1, null, '后台管理', 'admin', '', 1, 'fa fa-cog fa-spin c66c'),
(2, 1, '用户管理', 'user', '/user/userList', 1, 'fa fa-address-book-o c393'),
(3, 1, '菜单管理', 'menu', '/admin/menuList', 2, 'fa fa-indent cfd7e14'),
(4, 1, '角色管理', 'role', '/admin/roleList', 3, 'fa fa-user c71a'),
(5, 1, '功能管理', 'function', '/admin/functionList', 4, 'fa fa-list-alt c933'),
(6, 1, '日志管理', 'operateLog', '/admin/operateLogList', 6, 'fa fa-list-ul c82a'),
(7, 1, '键值管理', 'kvConfig', '/kvConfig/list', 7, 'fa fa-magnet c82a'),
(8, 1, '开关控制室', 'switch', '/switch/list', 8, 'fa fa-toggle-on cfd7e14'),
(9, 1, '更新日志', 'upgradeLog', '/upgradeLog/list', 9, 'fa fa-file c393'),
(10, 1, 'redis操作', 'redisOpt', '/redis', 10, 'fa fa-puzzle-piece cc03');

create table function_info
(
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	name varchar(50),
	menu_id INTEGER,
	code varchar(50),
	path varchar(200),
	seq int
);
insert into function_info(id, name, menu_id, code, path, seq) values(1, '用户列表', 2, 'userList', '/user/userList', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(2, '用户编辑', 2, 'userEdit', '/user/userEdit', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(3, '菜单列表', 3, 'menuList', '/admin/menuList', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(4, '菜单编辑', 3, 'menuEdit', '/admin/menuEdit', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(5, '角色列表', 4, 'roleList', '/admin/roleList', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(6, '角色编辑', 4, 'roleEdit', '/admin/RoleEdit', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(7, '角色授权', 4, 'grantPermission', '/admin/roleGrantPermission', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(8, '用户角色', 4, 'userRole', '/admin/userRole', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(9, '功能列表', 5, 'functionList', '/admin/functionList', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(10, '功能编辑', 5, 'functionEdit', '/admin/functionEdit', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(11, '权限列表', 5, 'permissionList', '/admin/permissionList', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(12, '权限编辑', 5, 'permissionEdit', '/admin/permissionEdit', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(13, '日志列表', 6, 'operateLogList', '/admin/operateLogList', 1);
insert into function_info(id, name, menu_id, code, path, seq) values(14, '日志编辑', 6, 'operateLogEdit', '/admin/operateLogEdit', 1);
insert into function_info(id, name, menu_id, code, path, seq) values(15, '键值列表', 7, 'kvConfigList', '/kvConfig/list', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(16, '键值详情', 7, 'kvConfigDetail', '/kvConfig/edit', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(17, '开关控制室', 8, 'switchList', '/switch/list', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(18, '更新日志列表', 9, 'upgradeLogList', '/upgradeLog/list', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(19, '更新日志预览', 9, 'upgradeLogPreview', '/upgradeLog/preview', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(20, '更新日志编辑', 9, 'upgradeLogEdit', '/upgradeLog/edit', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(21, 'redis操作', 10, 'redisOptList', '/redis', 0);

create table permission
(
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	function_id INTEGER,
	name VARCHAR(50),
	code VARCHAR(50) unique ,
	seq INTEGER,
	type SMALLINT,
	target_id INTEGER
);
-- 菜单权限 select 'insert into permission(id,function_id, name, code, seq, type, target_id)values(' || id || ',null,'''||name||''','''||code||''','||seq||',1,'||id||');' from menu
insert into permission(id,function_id, name, code, seq, type, target_id)values(1,null,'后台管理','admin',1,1,1);
insert into permission(id,function_id, name, code, seq, type, target_id)values(2,null,'用户管理','user',1,1,2);
insert into permission(id,function_id, name, code, seq, type, target_id)values(3,null,'菜单管理','menu',2,1,3);
insert into permission(id,function_id, name, code, seq, type, target_id)values(4,null,'角色管理','role',3,1,4);
insert into permission(id,function_id, name, code, seq, type, target_id)values(5,null,'功能管理','function',4,1,5);
insert into permission(id,function_id, name, code, seq, type, target_id)values(6,null,'日志管理','operateLog',6,1,6);
insert into permission(id,function_id, name, code, seq, type, target_id)values(7,null,'键值管理','kvConfig',7,1,7);
insert into permission(id,function_id, name, code, seq, type, target_id)values(8,null,'开关控制室','switch',8,1,8);
insert into permission(id,function_id, name, code, seq, type, target_id)values(9,null,'更新日志','upgradeLogList',9,1,9);
insert into permission(id,function_id, name, code, seq, type, target_id)values(10,null,'redis操作','redisOptList',10,1,10);
-- 页面权限 select 'insert into permission(id,function_id, name, code, seq, type, target_id)values(' || ((select count(1) from menu) + id) || ','|| id ||','''||name||''','''||code||''','||seq||',2,'||id||');' from function_info

-- 功能权限
insert into permission(id,function_id, name, code, seq, type, target_id)values(32,1,'添加用户','userList_add','1',3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(33,1,'删除','userList_delete','2',3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(34,1,'授权','userList_grant',0,3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(35,1,'修改密码','userList_resetPassword',0,3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(36,2,'保存','userEdit_save',0,3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(37,3,'添加','menuList_add','1',3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(38,3,'删除','menuList_delete','2',3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(39,4,'保存','menuEdit_save',0,3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(40,5,'添加','roleList_add','1',3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(41,5,'删除','roleList_delete','2',3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(42,5,'授权','roleList_grant',0,3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(43,6,'保存','roleEdit_save',0,3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(44,9,'添加','functionList_add','1',3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(45,9,'删除','functionList_delete','2',3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(46,9,'添加权限点','functionList_addPermission','3',3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(47,9,'授权','functionList_grant',0,3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(48,10,'保存','functionEdit_save',0,3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(49,11,'删除','permissionList_delete',0,3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(50,11,'添加','permissionList_add',0,3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(51,12,'保存','permissionEdit_save',0,3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(52,13,'查看','operateLogList_view','2',3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(53,13,'删除','operateLogList_delete','3',3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(54,13,'清空日志','operateLogList_deleteAll','1',3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(55,15,'删除','kvConfig_delete',0,3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(56,15,'清空所有缓存','kvConfig_clear_cache',0,3,null);
insert into permission(id,function_id, name, code, seq, type, target_id)values(57,16,'保存','kvConfig_save',0,3,null);

create table role_permission
(
	role_id INTEGER,
	permission_id INTEGER
);
insert into role_permission(role_id, permission_id) select 1, id from permission;

create table user_permission
(
    user_id INTEGER,
    permission_id INTEGER,
    include_type SMALLINT
);

create table operate_log
(
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    controller VARCHAR2(300),
    method VARCHAR2(300),
    log_level SMALLINT,
    content VARCHAR2(4000),
    created_on TIMESTAMP
);
create table kv_config
(
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    code varchar(40) not null,
    value varchar(2000),
    create_Time date,
    memo varchar(200)
);

create table kv_config_log
(
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    code varchar(40),
    value varchar(2000),
    kv_id INTEGER,
    create_time date,
    create_by INTEGER
);

create table upgrade_log
(
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    upgrade_time Date,
    title varchar(100),
    content varchar(2000),
    create_time Date
);