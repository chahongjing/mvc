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
insert into user_info(id,code,name,password,sex,is_system,birthday,status)
values(1, 'admin','系统管理员','8c16fa7743119806880db2cc4780f576',1,1,'1990-02 12:10:23:59',0),
(2, 'zjy','曾军毅','25c278f2b306449559f6b79b36c7b2e9',0,0,'1990-02 12:10:23:59',0);

create table role_info
(
	id INTEGER NOT NULL PRIMARY KEY autoincrement,
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

insert into menu(id, pid, name, code, url, seq, icon)
values(1, null, '后台管理', 'admin', '', 1, 'fa fa-cog fa-spin c66c'),
(2, 1, '用户管理', 'user', '/user/userList', 1, 'fa fa-address-book-o c393'),
(3, 1, '菜单管理', 'menu', '/admin/menuList', 2, 'fa fa-indent cfd7e14'),
(4, 1, '角色管理', 'role', '/admin/roleList', 3, 'fa fa-user c71a'),
(5, 1, '功能管理', 'function', '/admin/functionList', 4, 'fa fa-list-alt c933'),
(6, 1, '配置管理', 'configList', '/admin/configInfoList', 5, 'fa fa-puzzle-piece cc03'),
(7, 1, '日志管理', 'operLog', '/admin/operLogList', 6, 'fa fa-list-ul c82a'),
(8, 1, '键值管理', 'kvConfig', '/kvConfig/list', 7, 'fa fa-magnet c82a'),
(9, 1, '开关控制室', 'switchList', '/switch/list', 8, 'fa fa-toggle-on cfd7e14'),
(10, 1, '更新日志', 'upgradeLog', '/upgradeLog/list', 9, 'fa fa-file c393'),
(11, 1, 'redis操作', 'redisOpt', '/redis', 10, 'fa fa-puzzle-piece cc03');
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
insert into function_info(id, name, menu_id, code, path, seq) values(1, '用户列表', 2, 'user', '/user/userList', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(2, '用户编辑', 2, 'userEdit', '/user/userEdit', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(3, '菜单列表', 3, 'menuList', '/admin/menuList', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(4, '菜单编辑', 3, 'menuEdit', '/admin/menuEdit', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(5, '角色列表', 4, 'roleList', '/admin/roleList', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(6, '角色编辑', 4, 'roleEdit', '/admin/RoleEdit', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(7, '角色授权', 4, 'roleGrantPermission', '/admin/roleGrantPermission', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(8, '用户角色', 4, 'userRole', '/admin/userRole', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(9, '功能列表', 5, 'function', '/admin/functionList', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(10, '功能编辑', 5, 'functionedit', '/admin/functionEdit', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(11, '权限列表', 5, 'permissionList', '/admin/permissionList', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(12, '权限编辑', 5, 'permissionEdit', '/admin/permissionEdit', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(13, '配置列表', 6, 'configinfoList', '/admin/configInfoList', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(14, '配置编辑', 6, 'configinfoEdit', '/admin/configInfoEdit', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(15, '日志列表', 7, 'operLogList', '/admin/operLogList', 1);
insert into function_info(id, name, menu_id, code, path, seq) values(16, '日志编辑', 7, 'operLogEdit', '/admin/operLogEdit', 1);
insert into function_info(id, name, menu_id, code, path, seq) values(17, '键值列表', 8, 'kvConfig', '/kvConfig/list', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(18, '键值详情', 8, 'kvConfig_detail', '/kvConfig/edit', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(19, '开关控制室', 9, 'switchList', '/switch/list', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(20, '更新日志列表', 10, 'upgradeLog', '/upgradeLog/list', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(21, '更新日志预览', 10, 'upgradeLogPreview', '/upgradeLog/preview', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(22, '更新日志编辑', 10, 'upgradeLogEdit', '/upgradeLog/edit', 0);
insert into function_info(id, name, menu_id, code, path, seq) values(23, 'redis操作', 11, 'redisOpt', '/redis', 0);

create table permission
(
	id INTEGER NOT NULL PRIMARY KEY autoincrement,
	function_id INTEGER,
	name VARCHAR(50),
	code VARCHAR(50),
	seq INTEGER
);
insert into permission(id,function_id, name, code, seq)values(1,1,'添加用户','userList_add','1');
insert into permission(id,function_id, name, code, seq)values(2,1,'进入页面','userList_enter','0');
insert into permission(id,function_id, name, code, seq)values(3,1,'删除','userList_delete','2');
insert into permission(id,function_id, name, code, seq)values(4,1,'授权','userList_grant','0');
insert into permission(id,function_id, name, code, seq)values(5,1,'修改密码','userList_resetPassword','0');
insert into permission(id,function_id, name, code, seq)values(6,2,'进入页面','userEdit_enter','0');
insert into permission(id,function_id, name, code, seq)values(7,2,'保存','userEdit_save','0');
insert into permission(id,function_id, name, code, seq)values(8,3,'添加','menuList_add','1');
insert into permission(id,function_id, name, code, seq)values(9,3,'删除','menuList_delete','2');
insert into permission(id,function_id, name, code, seq)values(10,3,'进入页面','menuList_enter','0');
insert into permission(id,function_id, name, code, seq)values(11,4,'进入页面','menuEdit_enter','0');
insert into permission(id,function_id, name, code, seq)values(12,4,'保存','menuEdit_save','0');
insert into permission(id,function_id, name, code, seq)values(13,5,'添加','roleList_add','1');
insert into permission(id,function_id, name, code, seq)values(14,5,'删除','roleList_delete','2');
insert into permission(id,function_id, name, code, seq)values(15,5,'进入页面','roleList_enter','0');
insert into permission(id,function_id, name, code, seq)values(16,5,'授权','roleList_grant','0');
insert into permission(id,function_id, name, code, seq)values(17,6,'进入页面','roleEdit_enter','0');
insert into permission(id,function_id, name, code, seq)values(18,6,'保存','roleEdit_save','0');
insert into permission(id,function_id, name, code, seq)values(19,7,'进入页面','roleGrantPermission_enter','0');
insert into permission(id,function_id, name, code, seq)values(20,8,'进入页面','userRole_enter','0');
insert into permission(id,function_id, name, code, seq)values(21,9,'添加','functionList_add','1');
insert into permission(id,function_id, name, code, seq)values(22,9,'删除','functionList_delete','2');
insert into permission(id,function_id, name, code, seq)values(23,9,'添加权限点','functionList_addPermission','3');
insert into permission(id,function_id, name, code, seq)values(24,9,'进入页面','functionList_enter','0');
insert into permission(id,function_id, name, code, seq)values(25,9,'授权','functionList_grant','0');
insert into permission(id,function_id, name, code, seq)values(26,10,'进入页面','functionEdit_enter','0');
insert into permission(id,function_id, name, code, seq)values(27,10,'保存','functionEdit_save','0');
insert into permission(id,function_id, name, code, seq)values(28,11,'进入页面','permissionList_enter','0');
insert into permission(id,function_id, name, code, seq)values(29,11,'删除','permissionList_delete','0');
insert into permission(id,function_id, name, code, seq)values(30,11,'添加','permissionList_add','0');
insert into permission(id,function_id, name, code, seq)values(31,12,'保存','permissionEdit_save','0');
insert into permission(id,function_id, name, code, seq)values(32,12,'进入页面','permissionEdit_enter','0');
insert into permission(id,function_id, name, code, seq)values(33,13,'进入页面','configInfoList_enter','0');
insert into permission(id,function_id, name, code, seq)values(34,13,'删除','configInfoList_delete','0');
insert into permission(id,function_id, name, code, seq)values(35,13,'添加','configInfoList_add','0');
insert into permission(id,function_id, name, code, seq)values(36,14,'进入页面','configInfoEdit_enter','0');
insert into permission(id,function_id, name, code, seq)values(37,14,'保存','configInfo_save','0');
insert into permission(id,function_id, name, code, seq)values(38,15,'进入页面','operLogList_enter','0');
insert into permission(id,function_id, name, code, seq)values(39,15,'查看','operLogList_view','2');
insert into permission(id,function_id, name, code, seq)values(40,15,'删除','operLogList_delete','3');
insert into permission(id,function_id, name, code, seq)values(41,15,'清空日志','operLogList_deleteAll','1');
insert into permission(id,function_id, name, code, seq)values(42,16,'进入页面','operLogEdit_enter','0');
insert into permission(id,function_id, name, code, seq)values(43,17,'进入页面','kvConfig_enter','0');
insert into permission(id,function_id, name, code, seq)values(44,17,'删除','kvConfig_delete','0');
insert into permission(id,function_id, name, code, seq)values(45,17,'清空所有缓存','kvConfig_clear_cache','0');
insert into permission(id,function_id, name, code, seq)values(46,18,'保存','kvConfig_save','0');
insert into permission(id,function_id, name, code, seq)values(47,18,'进入页面','kvConfig_detail_enter','0');
insert into permission(id,function_id, name, code, seq)values(48,19,'进入页面','switchList_enter','0');
insert into permission(id,function_id, name, code, seq)values(49,20,'进入页面','upgradeLog_enter','0');
insert into permission(id,function_id, name, code, seq)values(50,21,'进入页面','upgradeLogPreview_enter','0');
insert into permission(id,function_id, name, code, seq)values(51,22,'进入页面','upgradeLogEdit_enter','0');
insert into permission(id,function_id, name, code, seq)values(52,23,'进入页面','redisOpt_enter','0');

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
	type tinyint
);