#基于springboot的动态主从路由库
具体配置过程参考dsspringbootstart-test
项目依赖：
```XML
<dependency>
  <groupId>io.github.jiaozi789.tool</groupId>
  <artifactId>ds-spring-boot-starter</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
##多数据源配置
>注意该项目只支持jdbc数据源，不支持jpa
使用前禁用jpa
```java
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
 })
```
spring.yml配置多数据源：
```
spring:
  datasource:
    primary:
        username: root
        url: jdbc:mysql://localhost/test
        driver-class-name: com.mysql.jdbc.Driver
        password: 123456
        primary:  true
    second:
        username: root
        url: jdbc:mysql://localhost/test1
        driver-class-name: com.mysql.jdbc.Driver
        password: 123456
    thrid:
        username: root
        url: jdbc:mysql://localhost/test
        driver-class-name: com.mysql.jdbc.Driver
        password: 123456
```
> primary: true表示主数据库，目前只支持一个primary数据库，不设置为从数据库

springboot运行主类添加启用数据源路由
```java
@EnableDataSourceRoute
```
在需要切换数据源的service方法上添加
```java
@DataSourceRoute(write = true)
    public List<Map<String, Object>>  queryAll(){
        List<Map<String, Object>> query = jdbcTemplate.query("select * from user", new ColumnMapRowMapper());
        return query;
    }
```
> write=true表示使用主库
> read=true 表示使用从库
> name=yml定义的数据源名称，使用指定名称的数据源
