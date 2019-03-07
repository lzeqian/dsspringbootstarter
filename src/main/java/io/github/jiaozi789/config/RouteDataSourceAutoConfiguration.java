package io.github.jiaozi789.config;

import io.github.jiaozi789.datasource.MultiDataSource;
import io.github.jiaozi789.aop.DataSourceRouteAspect;
import io.github.jiaozi789.rule.RandomRule;
import io.github.jiaozi789.rule.SelectRule;
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
 * @author 廖敏
 * 创建日期：  2019-03-04 14:02
 **/
@Configuration
@Order(10)
public class RouteDataSourceAutoConfiguration {

    @Primary
    @Bean(name = "dynamicDataSource")
    public AbstractRoutingDataSource roundRobinDataSouceProxy(@Autowired DataSourceRegister dataSourceConfiguration) {
        MultiDataSource multiDataSource=new MultiDataSource();
        multiDataSource.setTargetDataSources(dataSourceConfiguration.getTargetDataSources());
        multiDataSource.setDefaultTargetDataSource(dataSourceConfiguration.getTargetDataSources().values().iterator().next());
        return multiDataSource;
    }
    @ConditionalOnMissingBean
    @Bean
    public JdbcTemplate jdbcTemplate(@Autowired DataSource dataSources){
        return new JdbcTemplate(dataSources);
    }
    @ConditionalOnMissingBean
    @Bean
    public SelectRule selectRule(){
        return new RandomRule();
    }
    @Bean
    public DataSourceRegister dataSourceRegister(){
        return new DataSourceRegister();
    }
    @Bean
    public DataSourceRouteAspect recordLogAspect() {
        return new DataSourceRouteAspect();
    }
}
