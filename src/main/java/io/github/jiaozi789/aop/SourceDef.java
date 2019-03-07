package io.github.jiaozi789.aop;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 廖敏
 * 创建时间 2019-03-07 10:49
 **/
@Data
public class SourceDef {
    private String dataSourceName;
    private List<String> sourceConfList=new ArrayList<>();
    private boolean ifPrimary;
}
