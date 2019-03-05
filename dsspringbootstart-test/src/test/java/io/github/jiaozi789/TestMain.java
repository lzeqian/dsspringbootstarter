package io.github.jiaozi789;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * @Author 廖敏
 * @Date 2019-03-01 10:17
 **/
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
 }
        )
public class TestMain {
    public TestMain(){

    }
    public void TestMain(){

    }
    public static void main(String[] args) {
        SpringApplication.run(TestMain.class);
    }
}
