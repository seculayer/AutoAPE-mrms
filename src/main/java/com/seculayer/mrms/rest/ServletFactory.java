package com.seculayer.mrms.rest;

import com.seculayer.mrms.common.Constants;
import com.seculayer.mrms.rest.servlet.MRMSDummyServlet;
import com.seculayer.util.JsonUtil;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class ServletFactory {
    private static List<Class<?>> scan(String packageName){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');

        List<Class<?>> classes = new ArrayList<Class<?>>();
        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            URL res = resources.nextElement();

            for (File file : Objects.requireNonNull(new File(res.getFile()).listFiles())) {
                if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    classes.add(classLoader.loadClass(className));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return classes;
    }


    public static ServletHandler createServletHandler(){
        ServletHandler handler = new ServletHandler();
        // add handlers
        for (Class<?> servletClassName: ServletFactory.scan(Constants.REST_PACKAGES)){
            try {
                ServletHandlerAbstract servlet = (ServletHandlerAbstract) servletClassName.getConstructor().newInstance();
                String contextPath = String.valueOf(servletClassName.getField("ContextPath").get(servletClassName.getClass()));
                handler.addServletWithMapping(new ServletHolder(servlet), contextPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return handler;
    }

    public static Map<String, Object> getBodyFromJSON(HttpServletRequest httpServletRequest) throws IOException {
        return JsonUtil.getMapFromString(httpServletRequest.getReader());
    }
}
