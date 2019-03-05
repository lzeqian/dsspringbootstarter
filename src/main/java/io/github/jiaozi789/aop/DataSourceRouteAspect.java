package io.github.jiaozi789.aop;

import io.github.jiaozi789.annotation.DataSourceRoute;
import io.github.jiaozi789.config.DataSourceRegister;
import io.github.jiaozi789.datasource.DataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Random;

/**
 * @Author 廖敏
 * @Date 2019-03-04 14:14
 **/
@Aspect
public class DataSourceRouteAspect {
    @Pointcut("@annotation(io.github.jiaozi789.annotation.DataSourceRoute)")
    public void pointcut() {
    }
    @Autowired
    private DataSourceRegister dr;
    @Before(value = "pointcut()")
    public void routeKey(JoinPoint pjp) {
        Method method = resolveMethod(pjp);
        // 获取当前拦截方法上表示的RecordLog
        DataSourceRoute dataSourceRoute = method.getAnnotation(DataSourceRoute.class);

        if(dataSourceRoute.value()==null || "".equalsIgnoreCase(dataSourceRoute.value())){
            if(dataSourceRoute.write()){
                DataSourceContextHolder.setDataSourceKey(dr.masterName);
            }
            else if(dataSourceRoute.read()){
                Map<Object, Object> targetDataSource=dr.targetDataSources;
                int length=(dr.masterName==null?targetDataSource.size():targetDataSource.size()-1);
                int index=new Random().nextInt(length);
                int i=0;
                for (Object dataSourceName : targetDataSource.keySet()) {
                    String dataSourceNameStr=(String)dataSourceName;
                    if(!dataSourceNameStr.equalsIgnoreCase(dr.masterName)){
                        if(i==index){
                            DataSourceContextHolder.setDataSourceKey(dataSourceNameStr);
                            break;
                        }
                        i++;
                    }
                }
            }else{
                DataSourceContextHolder.setDataSourceKey(null);
            }
        }else{
            DataSourceContextHolder.setDataSourceKey(dataSourceRoute.value());
        }


    }
    private Method resolveMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = joinPoint.getTarget().getClass();

        Method method = getDeclaredMethodFor(targetClass, signature.getName(),
                signature.getMethod().getParameterTypes());
        if (method == null) {
            throw new IllegalStateException("Cannot resolve target method: " + signature.getMethod().getName());
        }
        return method;
    }
    private Method getDeclaredMethodFor(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getDeclaredMethodFor(superClass, name, parameterTypes);
            }
        }
        return null;
    }
}
