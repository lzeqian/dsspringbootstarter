package io.github.jiaozi789.config;

import io.github.jiaozi789.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置加载类
 * @author 廖敏
 * 创建日期：  2019-03-01 10:14
 **/
@Component
@Order(9)
public class DataSourceRegister {
    /**
     * spring配置环境
     */
    @Autowired
    private StandardEnvironment env;
    /**
     * bean注册工厂
     */
    @Autowired
    private DefaultListableBeanFactory ac;
    /**
     * 数据源配置格式化
     */
    private HashMap<String,List<String>> dataSoureNames;
    public String masterName;
    public Map<Object, Object> targetDataSources=new HashMap<>();
    /**
     * 数据源前缀
     */
    private String prefix="spring.datasource.";
    private static Field getField(Object object,String field)  {
        Class clazz = object.getClass();
        while (clazz != null){
            try {
                Field declaredField = clazz.getDeclaredField(field);
                return declaredField;
            } catch (NoSuchFieldException e) {
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }
    private static Method getMethod(Object object,String methodName)  {
        Class clazz = object.getClass();
        while (clazz != null){
            try {
                Method declaredMethod = clazz.getDeclaredMethod(methodName,String.class);
                return declaredMethod;
            } catch (NoSuchMethodException e) {
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }
    /**
     * 注册数据源bean
     */
    public void initDataSourceBean(){
        dataSoureNames.keySet().forEach(item->{
            try {
                List<String> strings = dataSoureNames.get(item);
                String configPreName=prefix+item;
                String type=env.getProperty(configPreName+".type");
                Class dataSourceCLass=null;
                if(type!=null && !"".equals(type.trim())) {
                    dataSourceCLass=((Class<DataSource>) Class.forName(type));

                }else{
                   // dataSourceCLass=(com.alibaba.druid.pool.xa.DruidXADataSource.class);
                    dataSourceCLass=(com.alibaba.druid.pool.DruidDataSource.class);
                }
                Object dataSource = dataSourceCLass.newInstance();
                targetDataSources.put(item,dataSource);
                strings.forEach(k->{
                    String property=k.split(configPreName+".")[1];
                    try {
                        Field declaredField = getField(dataSource,property);
                        if(declaredField!=null){
                            declaredField.setAccessible(true);
                            declaredField.set(dataSource,env.getProperty(k));
                        }else{
                            Method method = getMethod(dataSource, "set" + StringUtils.toPeak(StringUtils.initCap(property),"-"));
                            if(method!=null){
                                method.invoke(dataSource,env.getProperty(k));
                            }
                        }

                    } catch (Exception e) {

                    }

                });

                //手工添加bean定义
                AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(dataSourceCLass).setScope("singleton").getBeanDefinition();
                ac.registerBeanDefinition(item,beanDefinition);
                //添加自己实例化的bean 注意必须在定义之后
                ac.registerSingleton(item,dataSource);
                String primary=configPreName+".primary";
                String ifPrimary = env.getProperty(primary);
                if("true".equalsIgnoreCase(ifPrimary)){
                    //beanDefinition.setPrimary(true);
                    masterName=item;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 解析数据源配置
     */
    public void parseDataSourceConf(){
        env.getPropertySources().forEach(item->{
            if(item instanceof OriginTrackedMapPropertySource){
                OriginTrackedMapPropertySource orps=(OriginTrackedMapPropertySource)item;
                if(orps.getName().contains("application.yml")){
                    String[] propertyNames = orps.getPropertyNames();
                    for (String propertyName: propertyNames ) {
                        if(propertyName.startsWith(prefix)){
                            String suffixPart=propertyName.substring(prefix.length());
                            String suffix=(suffixPart.substring(0,suffixPart.indexOf(".")));
                            List<String> strings=null;
                            if(dataSoureNames.containsKey(suffix)){
                                strings= dataSoureNames.get(suffix);
                            }else{
                               strings=new ArrayList<>();
                               dataSoureNames.put(suffix,strings);
                            }
                            strings.add(propertyName);
                        }
                    }

                }
            }
        });
    }
    @PostConstruct
    public void init(){
        dataSoureNames=new HashMap<>();
        parseDataSourceConf();
        initDataSourceBean();
    }
}
