package com.zjk.hy.utils;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 对象拷贝工具类
 *
 * @author moerlong
 */
public class BeanUtils {

    /**
     * 对象拷贝
     * @param from 原对象
     * @param to   目标对象
     */
    public static <F, T> void copyProperties(F from, T to) {
        if (from == null || to == null) {
            throw new NullPointerException("相互拷贝的对象不能为Null！");
        }
        // 获取类和父类的属性
        copy(from, to);
    }

    /**
     * 将对象内容拷贝到指定class类型的对象中
     * @param from
     * @param clazz
     * @param <F>
     * @param <T>
     * @return
     */
    public static <F,T> T copyProperties(F from,Class<T> clazz) {
        if(clazz==null){
            throw new NullPointerException("目标对象的Class不能为Null！");
        }
        if (from == null) {
            throw new NullPointerException("拷贝的源对象不能为Null！");
        }
        T to = null;
        try {
            to = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("对象实例化失败!");
        }
        // 获取类和父类的属性
        copy(from, to);
        return to;
    }


    private static <F, T> void copy(F from, T to) {
        List<Field> fromfields = getFields(from);
        baseCopy(to, from, fromfields, fieldToMap(getFields(to)));
    }

    /**
     * 获取一个对象的字段，包括所有父类的
     * @param from
     * @param <F>
     * @return
     */
    public static <F> List<Field> getFields(F from) {
        List<Field> fromfields = new ArrayList<>();
        Class tempClass = from.getClass();
        while (tempClass != null) {
            fromfields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }
        return fromfields;
    }

    /**
     * 具体的拷贝方法
     * @param to
     * @param from
     * @param fromfields
     * @param toFieldMap
     * @param <F>
     * @param <T>
     */
    private static <F, T> void baseCopy(T to, F from, List<Field> fromfields, Map<String, Field> toFieldMap) {
        Gson go = new Gson();
        for (Field field : fromfields) {
            try {
                field.setAccessible(true);
                Object value = field.get(from);
                if (value != null) {
                    // 获取该类的成员变量
                    Field f = toFieldMap.get(field.getName());
                    if(f!=null){
                        // 取消语言访问检查
                        f.setAccessible(true);
                        // 给变量赋值
                        f.set(to, go.fromJson(go.toJson(value), f.getGenericType()));
                    }
                }
            } catch (Exception e) { }
        }
    }

    private static <T> Map<String, Field> fieldToMap(List<Field> tofields) {
        Map<String, Field> map = new HashMap<String, Field>();
        for (Field field : tofields) {
            try {
                map.put(field.getName(),field);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return map;
    }
}
