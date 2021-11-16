package com.seculayer.mrms.rest;

import com.seculayer.mrms.common.Constants;
import com.seculayer.util.JsonUtil;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ServletFactory {
    private static List<Class<?>> scan(){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        String path = Constants.REST_PACKAGES.replace('.', '/');

        List<Class<?>> classes = new ArrayList<>();
        for (String subPack : Constants.REST_SUB_PACKS){
            String path = (Constants.REST_PACKAGES + "." + subPack).replace('.', '/');

            try {
                URL res = classLoader.getResource(path);
                File packDir;
                if (res.toString().contains("jar")) {
                    URL jar = ServletFactory.class.getProtectionDomain().getCodeSource().getLocation();
                    Path jarFile = Paths.get(jar.toString().substring("file:".length()));
                    FileSystem fs = FileSystems.newFileSystem(jarFile, null);
                    DirectoryStream<Path> directoryStream = Files.newDirectoryStream(fs.getPath(path));
                    for(Path p: directoryStream) {
                        String classPath = p.toString();
                        if (classPath.endsWith(".class")) {
                            String className = classPath.substring(0, classPath.length() - 6).replace("/", ".");
                            Class<?> servletClass = ServletFactory.classLoad(classLoader, className);
                            if (servletClass != null)
                                classes.add(servletClass);
                        }
                    }
                } else {
                    packDir = new File(Objects.requireNonNull(res.getFile()));
                    for (File file : Objects.requireNonNull(packDir.listFiles())) {
                        if (file.getName().endsWith(".class")) {
                            String className = Constants.REST_PACKAGES + "." + subPack + "." + file.getName().substring(0, file.getName().length() - 6);
                            Class<?> servletClass = ServletFactory.classLoad(classLoader, className);
                            if (servletClass != null)
                                classes.add(servletClass);
                        }
                    }
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return classes;
    }

    public static Class<?> classLoad(ClassLoader classLoader, String className){
        try {
            return classLoader.loadClass(className);
        } catch (Exception e){}
        return null;
    }

    public static ServletHandler createServletHandler(){
        ServletHandler handler = new ServletHandler();
        // add handlers
        for (Class<?> servletClassName: ServletFactory.scan()){
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

    public static List<Map<String, Object>> getBodyFromListJSON(HttpServletRequest httpServletRequest) throws IOException {
        return JsonUtil.getListMapFromString(httpServletRequest.getReader());
    }
}
