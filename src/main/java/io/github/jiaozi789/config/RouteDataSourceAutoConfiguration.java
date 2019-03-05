package io.github.jiaozi789.config;

import io.github.jiaozi789.aop.DataSourceRouteAspect;
import io.github.jiaozi789.datasource.MultiDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

/**
 * @Author 廖敏
 * @Date 2019-03-04 14:02
 **/
@Configuration
@Order(10)
public class RouteDataSourceAutoConfiguration {

    @Primary
    @Bean(name = "dynamicDataSource")
    public AbstractRoutingDataSource roundRobinDataSouceProxy(@Autowired DataSourceRegister dataSourceConfiguration) {
        MultiDataSource multiDataSource=new MultiDataSource();
        multiDataSource.setTargetDataSources(dataSourceConfiguration.targetDataSources);
        if(dataSourceConfiguration.masterName!=null){
            multiDataSource.setDefaultTargetDataSource(dataSourceConfiguration.targetDataSources.get(dataSourceConfiguration.masterName));
        }
        return multiDataSource;
    }
    @Bean
    public JdbcTemplate jdbcTemplate(@Autowired DataSource dataSources){
        return new JdbcTemplate(dataSources);
    }
    @Bean
    public DataSourceRouteAspect recordLogAspect() {
        return new DataSourceRouteAspect();
    }
}
