1. baseframework_new：公共jar包，包含基础类和服务
2. entity：基础类，实体，po，vo，dto，枚举，常量等
3. api: 对外接口，引用entity
4. common：公共功能，引用entity
5. dao：引用entity，common
6. service：服务，引用dao
7. web：应用，引用service

|mvc|[【8088】](http://localhost:8088)|springboot-mvc, shiro+permission，vue，动态数据源(先区分mapper，再区分master,slave)，重复请求注解，大数据量下载，mybatis枚举映射，pagehelper分页，打印sql日志拦截|