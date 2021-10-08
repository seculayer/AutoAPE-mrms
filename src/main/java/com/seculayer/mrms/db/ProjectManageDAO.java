package com.seculayer.mrms.db;

import com.seculayer.mrms.managers.DBSessionManager;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

public class ProjectManageDAO {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final SqlSessionFactory factory = DBSessionManager.getSqlSession();
    private static String mapperName = "ProjectManageMapper.";

    public void insertProjectInfo(Map<String, Object> map){
        SqlSession session = factory.openSession();
        try {
            session.insert(mapperName + "insertProjectInfo", map);
            session.commit();
        } finally {
            session.close();
        }
    }
}
