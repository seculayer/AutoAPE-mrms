package com.seculayer.mrms.db;

import com.seculayer.mrms.managers.DBSessionManager;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectManageDAO {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final SqlSessionFactory factory = DBSessionManager.getSqlSession();
    private static String mapperName = "ProjectManageMapper.";

    public void insertProjectInfo(Map<String, Object> map) {
        try (SqlSession session = factory.openSession()) {
            session.insert(mapperName + "insertProjectInfo", map);
            session.commit();
        }
    }

    public List<Map<String, Object>> selectLearningProjectList(Map<String, Object> map) {
        List<Map<String, Object>> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectList(mapperName + "selectLearningProject", map);
        }

        return rst;
    }

    public void updateStatus(Map<String, Object> map) {
        try (SqlSession session = factory.openSession()) {
            session.update(mapperName + "updateProjectSttus", map);
            session.commit();
        }
    }
}
