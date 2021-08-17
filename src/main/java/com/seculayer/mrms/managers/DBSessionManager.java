package com.seculayer.mrms.managers;

import com.seculayer.util.cipher.AES256;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.Reader;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

public class DBSessionManager {
    static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static SqlSessionFactory sqlSession = null;

    static {
        try {
            Reader reader = Resources.getResourceAsReader("com/seculayer/mrms/resources/mybatis-configuration.xml");

            File file = new File(System.getProperty("CLOUDAI_HOME", "."), "conf/db.properties");
            Properties properties = new Properties();

            try (FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {
                properties.load(fis);
            } catch (Exception e) {
                logger.error("properties load e:{}", e.getMessage(), e);
            }
            AES256 encUtil = new AES256();

            properties.setProperty("jdbc.username", encUtil.decrypt(properties.getProperty("jdbc.username")));
            properties.setProperty("jdbc.password", encUtil.decrypt(properties.getProperty("jdbc.password")));
            sqlSession = new SqlSessionFactoryBuilder().build(reader, "AI", properties);

        } catch (Exception e) {
            logger.error("{}", e.getMessage(), e);
        }
    }

    public static SqlSessionFactory getSqlSession() {
        return sqlSession;
    }
}
