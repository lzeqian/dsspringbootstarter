# 基于springboot的动态主从路由库
具体例子程序参考dsspringbootstart-test<br/>
添加maven快照仓库
```XML
https://oss.sonatype.org/content/repositories/snapshots
```
项目依赖：
```XML
<dependency>
  <groupId>io.github.jiaozi789.tool</groupId>
  <artifactId>ds-spring-boot-starter</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
## 多数据源配置
禁用jpa方式使用
```java
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
 })
 
```
不禁用jpa的方式
```java
@SpringBootApplication
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
> primary: true表示主数据库
> 不指定type设置数据源类型，默认使用druid

## springboot中使用
springboot主类添加启用数据源路由
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
>@DataSourceRoute(write = true)表示使用主库<br/>
>@DataSourceRoute(read = true)read=true 表示使用从库<br/>
>@DataSourceRoute(value=yml定义的数据源名称，使用指定名称的数据源)<br/>
  比如 value=primary 将使用名称为primary的数据源<br/>
>@DataSourceRoute 表示所有库都提供服务，没有主从<br/>
>不添加注解，默认使用第一个数据源<br/>

## 多数据源负载算法
默认采用随机算法
```java
@Bean
    public SelectRule selectRule(){
        return new RandomRule();
    }
```
可以实现SelectRule接口自定义算法
SelectRule接口的实现方法choose
```java
//targetDataSource 表示对应的库名称和DataSource的键值对映射
public String choose(Map<Object, Object> targetDataSource);
```
