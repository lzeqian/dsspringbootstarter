package io.github.jiaozi789.datasource;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 数据源路由类 本身这就是数据源的集合
 * 属性 targetDataSources 可以存放通过名称标识的数据源集合
 * 决定返回哪个数据源
 * @author 廖敏
 * 创建日期：  2019-03-01 9:55
 **/
public class MultiDataSource extends AbstractRoutingDataSource {
    /**
     * 从targetDataSources选择一个返回 注意这里需要返回key而不是返回DataSource
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getCurrentDataSourceKey();
    }
}
