package io.github.jiaozi789.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @Author 廖敏
 * @Date 2019-03-01 10:19
 **/
@RestController
public class TestController {
    @Autowired
    private List<DataSource> c;
    @RequestMapping("/")
    public String h(){
        return c.toString();
    }
    @Autowired
    private UserServiceImpl usi;
    @RequestMapping("/get")
    public List<Map<String, Object>> get(){
        return usi.queryAll();
    }
}
