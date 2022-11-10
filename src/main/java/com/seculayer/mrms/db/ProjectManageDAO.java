package com.seculayer.mrms.db;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.Map;

public class ProjectManageDAO {
    private static final Logger logger = LogManager.getLogger();
    private static final SqlSessionFactory factory = DBSessionManager.getSqlSession();
    private static String mapperName = "ProjectManageMapper.";

    public void insertProjectInfo(Map<String, Object> map) {
        try (SqlSession session = factory.openSession()) {
            session.insert(mapperName + "insertProjectInfo", map);
            session.commit();
        }
    }

    public void insertEDAInfo(Map<String, Object> map) {
        try (SqlSession session = factory.openSession()) {
            session.insert(mapperName + "insertEDAInfo", map);
            session.commit();
        }
    }
    public void insertDPAnlsInfo(List<Map<String, Object>> listMap) {
        try (SqlSession session = factory.openSession()) {
            ObjectMapper mapper = new ObjectMapper();
            for (Map<String, Object> map : listMap) {
                try {
                    String daJsonStr = mapper.writeValueAsString(map.get("data_analysis_json"));
                    map.replace("data_analysis_json", daJsonStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                session.insert(mapperName + "insertDPAnlsInfo", map);
            }
            session.commit();
        }
    }

    public void insertAlgAnlsInfo(List<Map<String, Object>> listMap) {
        try (SqlSession session = factory.openSession()) {
            ObjectMapper mapper = new ObjectMapper();
            for (Map<String, Object> map : listMap) {
                try {
                    String metadataJsonStr = mapper.writeValueAsString(map.get("metadata_json"));
                    map.replace("metadata_json", metadataJsonStr);
                    String algJsonStr = mapper.writeValueAsString(map.get("alg_json"));
                    map.replace("alg_json", algJsonStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                session.insert(mapperName + "insertAlgAnlsInfo", map);
            }
            session.commit();
        }
    }

    public void insertMLParamInfo(List<Map<String, Object>> listMap) {
        try (SqlSession session = factory.openSession()) {
            ObjectMapper mapper = new ObjectMapper();
            for (Map<String, Object> map : listMap) {
                try {
                    String paramJsonStr = mapper.writeValueAsString(map.get("param_json"));
                    map.replace("param_json", paramJsonStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                session.insert(mapperName + "insertMLParamInfo", map);
            }
            session.commit();
        }
    }
    public void insertLearnHist(Map<String, Object> map) {
        try (SqlSession session = factory.openSession()) {
            session.insert(mapperName + "insertLearnHist", map);
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

    public List<Map<String, Object>> selectEDASchedule(String status) {
        List<Map<String, Object>> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectList(mapperName + "selectEDASchedule", status);
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

    public List<Map<String, Object>> selectAlgInfoList() {
        List<Map<String, Object>> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectList(mapperName + "selectAlgInfoList");
        }

        return rst;
    }

    public List<Map<String, Object>> selectParamInfo(String alg_id) {
        List<Map<String, Object>> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectList(mapperName + "selectParamInfo", alg_id);
        }

        return rst;
    }

    public List<Map<String, Object>> selectLearnReqList(Map<String, Object> map) {
        List<Map<String, Object>> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectList(mapperName + "selectLearnReqList", map);
        }

        return rst;
    }

    public Map<String, Object> selectDpAnlsInfo(Map<String, Object> map) {
        Map<String, Object> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectDpAnlsInfo", map);
        }

        return rst;
    }

    public Map<String, Object> selectMLParamInfo(Map<String, Object> map) {
        Map<String, Object> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectMLParamInfo", map);
        }

        return rst;
    }

    public String selectTargetField(Map<String, Object> map) {
        String rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectTargetField", map);
        }

        return rst;
    }

    public String selectProjectID(String histNo) {
        String rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectProjectID", histNo);
        }

        return rst;
    }

    public String selectLearnLog(String histNo) {
        String rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectLearnLog", histNo);
        }

        return rst;
    }

    public List<Map<String, Object>> selectHistNoStatus(String projectId) {
        List<Map<String, Object>> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectList(mapperName + "selectHistNoStatus", projectId);
        }

        return rst;
    }

    public void updateStatus(Map<String, Object> map) {
        try (SqlSession session = factory.openSession()) {
            session.update(mapperName + "updateProjectSttus", map);
            session.commit();
        }
    }

    public void updateEDAStatus(Map<String, Object> map) {
        try (SqlSession session = factory.openSession()) {
            session.update(mapperName + "updateEDAStatus", map);
            session.commit();
        }
    }

    public void updateUsedYN(Map<String, Object> map) {
        try (SqlSession session = factory.openSession()) {
            session.update(mapperName + "updateUsedYN", map);
            session.commit();
        }
    }

    public void updateLearnLog(Map<String, Object> map) {
        try (SqlSession session = factory.openSession()) {
            session.update(mapperName + "updateLearnLog", map);
            session.commit();
        }
    }

    public void updateBookmark(Map<String, Object> map) {
        try (SqlSession session = factory.openSession()) {
            session.update(mapperName + "updateBookmark", map);
            session.commit();
        }
    }

    public void updateModelAlias(Map<String, Object> map) {
        try (SqlSession session = factory.openSession()) {
            session.update(mapperName + "updateModelAlias", map);
            session.commit();
        }
    }

    public void updateSelectYN(Map<String, Object> map) {
        try (SqlSession session = factory.openSession()) {
            session.update(mapperName + "updateSelectNAll", map);
            session.commit();
        }
        try (SqlSession session = factory.openSession()) {
            session.update(mapperName + "updateSelectYOne", map);
            session.commit();
        }
    }
}
