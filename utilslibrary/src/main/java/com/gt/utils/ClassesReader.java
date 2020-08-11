package com.gt.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dalvik.system.DexFile;

public class ClassesReader {


    /**
     * 获取应用程序下的所有Dex文件
     *
     * @param context 上下文
     * @return Set<DexFile>
     */
    public static Set<DexFile> applicationDexFile(Context context) {
        return applicationDexFile(context.getPackageCodePath());
    }

    /**
     * 获取应用程序下的所有Dex文件
     *
     * @param packageCodePath 包路径
     * @return Set<DexFile>
     */
    public static Set<DexFile> applicationDexFile(String packageCodePath) {
        Set<DexFile> dexFiles = new HashSet<>();
        File dir = new File(packageCodePath).getParentFile();
        File[] files = dir.listFiles();
        for (File file : files) {
            try {
                String absolutePath = file.getAbsolutePath();
                if (!absolutePath.contains(".")) continue;
                String suffix = absolutePath.substring(absolutePath.lastIndexOf("."));
                if (!suffix.equals(".apk")) continue;
                DexFile dexFile = createDexFile(file.getAbsolutePath());
                if (dexFile == null) continue;
                dexFiles.add(dexFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dexFiles;
    }

    /**
     * 创建DexFile文件
     *
     * @param path 路径
     * @return DexFile
     */
    public static DexFile createDexFile(String path) {
        try {
            return new DexFile(path);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 读取类路径下的所有类
     *
     * @param context 上下文
     * @return List<Class>
     */
    public static List<Class<?>> reader(Context context) {
        return reader(context.getPackageName(), context.getPackageCodePath());
    }

    /**
     * 读取类路径下的所有类
     *
     * @param packageName     包名
     * @param packageCodePath 包路径
     * @return List<Class>
     */
    public static List<Class<?>> reader(String packageName, String packageCodePath) {
        List<Class<?>> classes = new ArrayList<>();
        Set<DexFile> dexFiles = applicationDexFile(packageCodePath);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (DexFile dexFile : dexFiles) {
            if (dexFile == null) continue;
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                try {
                    String currentClassPath = entries.nextElement();
                    if (currentClassPath == null || currentClassPath.isEmpty() || currentClassPath.indexOf(packageName) != 0)
                        continue;
                    Class<?> entryClass = Class.forName(currentClassPath, true, classLoader);
                    if (entryClass == null) continue;
                    classes.add(entryClass);
                } catch (Exception e) {
                }
            }
        }
        return classes;
    }

    /**
     * 读取包路径下的所有类
     *
     * @param packageCodePath 包路径
     * @return List<Class>
     */
    public static List<Class<?>> reader(String packageCodePath) {
        List<Class<?>> classes = new ArrayList<>();
        Set<DexFile> dexFiles = applicationDexFile(packageCodePath);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (DexFile dexFile : dexFiles) {
            if (dexFile == null) continue;
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                try {
                    String currentClassPath = entries.nextElement();
                    if (currentClassPath == null || currentClassPath.isEmpty())
                        continue;
                    Class<?> entryClass = Class.forName(currentClassPath, true, classLoader);
                    if (entryClass == null) continue;
                    classes.add(entryClass);
                } catch (Exception e) {
                } catch (Error e) {

                }
            }
        }
        return classes;
    }

    /**
     * 删除集合中没有指定注解的类
     *
     * @param classes
     * @param annotationType
     */
    public static List<Class<?>> deleteNotAnnotationClass(List<Class<?>> classes, Class<? extends Annotation> annotationType) {
        List<Class<?>> newClasses = new ArrayList<>();
        for (Class<?> aClass : classes) {
            if (aClass.getAnnotation(annotationType) != null) {
                newClasses.add(aClass);
            }
        }
        return newClasses;

    }

}
