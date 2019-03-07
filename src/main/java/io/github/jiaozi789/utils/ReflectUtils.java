package io.github.jiaozi789.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 廖敏
 * 创建时间 2019-03-07 10:59
 **/
public class ReflectUtils {
    public static Field getField(Object object, String field)  {
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
    public static Method getMethod(Object object, String methodName)  {
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
    public static int count(List list, String proName, Object proValue){
        int count=0;

        for (Object obj:list) {
            Field field = getField(obj, proName);
            field.setAccessible(true);
            try {
                if(field.get(obj).equals(proValue)){
                    count++;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return count;
    }
    public static List getEqualAtList(List list, String proName, Object proValue){
        int count=0;
        List rlist=new ArrayList();
        for (Object obj:list) {
            Field field = getField(obj, proName);
            field.setAccessible(true);
            try {
                if(field.get(obj).equals(proValue)){
                    rlist.add(obj);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return rlist;
    }
    public static Map getEqualAtMap(Map map, String proName, Object proValue){
        Map rMap=new HashMap();
        for (Object key:map.keySet()) {
            Object value = map.get(key);
            if(value.equals(proValue)){
                    rMap.put(key,value);
            }

        }
        return rMap;
    }
    public static Map getEqualAtMapObj(Map map, String proName, Object proValue){
        Map rMap=new HashMap();
        for (Object key:map.keySet()) {
            Object value = map.get(key);
            Field field = getField(value, proName);
            field.setAccessible(true);

            try {
                if(field.get(value).equals(proValue)){
                    rMap.put(key,value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return rMap;
    }

    public static List<Map> getEqualOrNotEqualAtMapObj(Map map, String proName, Object proValue){
        Map equalMap=new HashMap();
        Map notEqualMap=new HashMap();
        for (Object key:map.keySet()) {
            Object value = map.get(key);
            Field field = getField(value, proName);
            field.setAccessible(true);

            try {
                if(field.get(value).equals(proValue)){
                    equalMap.put(key,value);
                }else{
                    notEqualMap.put(key,value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        List<Map> lm=new ArrayList<>();
        lm.add(equalMap);
        lm.add(notEqualMap);
        return lm;
    }
    public static Map getDestMapFromSrcMap(Map srcMap, Map destMap){
        Map rdestMap=new HashMap();
        srcMap.keySet().forEach(key->{
            rdestMap.put(key,destMap.get(key));
        });
        return rdestMap;
    }
}
