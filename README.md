1. baseframework_new：公共jar包，包含基础类和服务
2. entity：基础类，实体，po，vo，dto，枚举，常量等，引用baseframework_new
3. api: 对外接口
4. common：公共功能，引用baseframework_new
5. dao：引用entity
6. service：服务，引用dao,common
7. web：应用，引用service
8. rpc_service：对外提供的远程服务引入api，service,zk版本3.8
8. rpc_consumer：调用远程api服务,zk版本3.8

|mvc|[【8088】](http://localhost:8088)|springboot-mvc, shiro+permission，vue，动态数据源(先区分mapper，再区分master,slave)，重复请求注解，大数据量下载，mybatis枚举映射,mybatis-plus，pagehelper分页，打印sql日志拦截|

spring-shiro；
1. Subject.loging(UsernamePasswordToken)登录
2. 进入AuthorizingRealm.doGetAuthenticationInfo方法，通过用户名从后台拿到用户信息，组装SimpleAuthenticationInfo，包括用户名，用户数据库密码，盐值。如果返回null，则是未找到用户信息，登录失败
3. 进入HashedCredentialsMatcher对用户输入的密码和数据库取出的密码进行对比，用户输入密码会自动加密

spring-security:
1. UsernamePasswordAuthenticationFilter中配置的登录地址，登录访问此地址，进入UsernamePasswordAuthenticationFilter的attemptAuthentication方法进行认证，此方法返回Authentication，包括用户输入的账号和密码，注意，此密码不用加密
2. 然后调用authenticationManager.authenticate方法进行验证
3. 验证时会在UserDetailsService通过用户名从数据库取出该用户，并将数据库的用户名，密码等信息组装成UserDetails信息返回，这里可以抛出相关异常，如未找到用户
4. 调用path:authenticationManager(ProviderManager).authenticate-->AbstractUserDetailsAuthenticationProvider.authenticate-->DaoAuthenticationProvider.retrieveUser-->DaoAuthenticationProvider.additionalAuthenticationChecks校验密码，这里会对用户输入的密码进行盐值加密再比较


``` shell
# 8080为后端控制台端口，csp.sentinel.dashboard.server将自己也注册到监控平台
java -Dserver.port=8200 -Dcsp.sentinel.dashboard.server=localhost:8200 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar
```