package io.github.jiaozi789.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 管理当前线程的key
 * @author 廖敏
 * 创建日期：  2019-03-01 10:01
 **/
public class DataSourceContextHolder {
    private static ThreadLocal<String> currentThreadKey=new ThreadLocal<String>();
    public static void addMaster(String name){

    }
    public static void setDataSourceKey(String key){
        currentThreadKey.set(key);
    }
    public static String getCurrentDataSourceKey(){
        return currentThreadKey.get();
    }
    public static void clearDataSourceKey(){
        currentThreadKey.remove();
    }
}
