package io.github.jiaozi789.test;

import io.github.jiaozi789.annotation.DataSourceRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author 廖敏
 * @Date 2019-03-04 13:12
 **/
@Service
@Configuration
public class UserServiceImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DataSourceRoute(write = true)
    public List<Map<String, Object>>  queryAll(){
        List<Map<String, Object>> query = jdbcTemplate.query("select * from user", new ColumnMapRowMapper());
        return query;
    }
}
