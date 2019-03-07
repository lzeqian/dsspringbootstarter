package io.github.jiaozi789.aop;

import io.github.jiaozi789.annotation.DataSourceRoute;
import io.github.jiaozi789.config.DataSourceRegister;
import io.github.jiaozi789.datasource.DataSourceContextHolder;
import io.github.jiaozi789.rule.SelectRule;
import io.github.jiaozi789.utils.ReflectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author 廖敏
 * 创建日期：  2019-03-04 14:14
 **/
@Aspect
public class DataSourceRouteAspect {
    @Pointcut("@annotation(io.github.jiaozi789.annotation.DataSourceRoute)")
    public void pointcut() {
    }
    @Autowired
    private DataSourceRegister dr;
    @Autowired
    private SelectRule selectRule;
    @Before(value = "pointcut()")
    public void routeKey(JoinPoint pjp) {
        Method method = resolveMethod(pjp);
        // 获取当前拦截方法上表示的RecordLog
        DataSourceRoute dataSourceRoute = method.getAnnotation(DataSourceRoute.class);
        //如果没有value表示是使用主从模式
        if(dataSourceRoute.value()==null || "".equalsIgnoreCase(dataSourceRoute.value())){
            Map<Object, Object> targetDataSource=dr.getTargetDataSources();
            String dataSource=null;
            if(dataSourceRoute.write()) {
                dataSource=selectRule.choose(ReflectUtils.getDestMapFromSrcMap(dr.getPrimayMap(),targetDataSource));
            }else if(dataSourceRoute.read()){
                dataSource=selectRule.choose(ReflectUtils.getDestMapFromSrcMap(dr.getSecondaryMap(),targetDataSource));
            }else{
                dataSource=selectRule.choose(targetDataSource);
            }
            DataSourceContextHolder.setDataSourceKey(dataSource);
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
