package io.github.jiaozi789.rule;

import io.github.jiaozi789.datasource.DataSourceContextHolder;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Random;

/**
 * @author 廖敏
 * 创建时间 2019-03-07 11:04
 **/
public class RandomRule implements SelectRule {
    @Override
    public String choose(Map<Object, Object> targetDataSource) {
        int i=0;
        int index=new Random().nextInt(targetDataSource.size());
        for (Object dataSourceName : targetDataSource.keySet()) {
            if(i==index){
                return dataSourceName.toString();
            }
            i++;
        }
        return null;
    }
}
