package io.github.jiaozi789.rule;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author 廖敏
 * 创建时间 2019-03-07 10:42
 **/
public interface SelectRule {
    /**
     *
     * @param targetDataSource 表示对应的库名称和DataSource的键值对映射
     * @return 返回数据源名称
     */
    public String choose(Map<Object, Object> targetDataSource);
}
