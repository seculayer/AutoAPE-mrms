package com.seculayer.mrms.db;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
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

    public List<Map<String, Object>> selectLearningModelList(Map<String, Object> map) {
        List<Map<String, Object>> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectList(mapperName + "selectLearningModel", map);
        }

        return rst;
    }

    public List<Map<String, Object>> selectProjectSchedule(String status) {
        List<Map<String, Object>> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectList(mapperName + "selectProjectSchedule", status);
        }

        return rst;
    }

    public Map<String, Object> selectDataAnalsId(String datasetID) {
        Map<String, Object> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectDataAnalsId", datasetID);
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
