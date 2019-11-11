package com.ytbdmhy.utils;

import com.ytbdmhy.pojo.InvokeTest;
import com.ytbdmhy.pojo.PoiInvokeTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射工具类
 */
public class ReflectionUtil {

    /**
     * 通过反射，根据Dao层对象、方法名、参数，执行Dao层方法
     * @param object Dao层对象
     * @param method 方法名
     * @param args 参数
     * @return
     */
    public static Object invoke(Object object, String method, Object... args) {
        Object result = null;
        Class<?> clazz = object.getClass();
        Method queryMethod = getMethod(clazz, method, args);
        if(queryMethod != null) {
            try {
                result = queryMethod.invoke(object, args);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw new NoSuchMethodException(clazz.getName() + " 类中没有找到 " + method + " 方法。");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 根据类、方法名、参数的数量和类型获取方法
     * @param clazz
     * @param name
     * @param args
     * @return
     */
    public static Method getMethod(Class<?> clazz, String name, Object[] args) {
        Method queryMethod = null;
        Method[] methods = clazz.getMethods();
        for(Method method : methods) {
            if(method.getName().equals(name)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if(parameterTypes.length == args.length) {
                    boolean isSameMethod = true;
                    for(int i = 0; i < parameterTypes.length; i++) {
                        if(!parameterTypes[i].equals(args[i].getClass()))
                            isSameMethod = false;
                    }
                    if(isSameMethod) {
                        queryMethod = method;
                        break ;
                    }
                }
            }
        }
        return queryMethod;
    }

    public static void test(Object object) {
        Class<?> clazz = object.getClass();
        Annotation[] annotations = clazz.getAnnotations();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
            Annotation[] fieldAnnotations = field.getAnnotations();
            System.out.println();
        }
        System.out.println("over");
    }

    public static void main(String[] args) {
        InvokeTest invokeTest = new InvokeTest();
        invokeTest.setAddress("test address");
        invokeTest.setAge(20);
        invokeTest.setName("test name");
        test(invokeTest);
    }
}
