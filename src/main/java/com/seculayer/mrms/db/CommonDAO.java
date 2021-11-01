package com.seculayer.mrms.db;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

public class CommonDAO {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final SqlSessionFactory factory = DBSessionManager.getSqlSession();

    private static final String mapperName = "CommonMapper.";

    // Select
    public void selectTestQuery(){
        String funcName = mapperName + "selectTestQuery";
        try (SqlSession session = factory.openSession()) {
            Map<String, Object> map = session.selectOne(funcName);
            logger.debug("select test query : {}", map);
        }
    }

    public List<Map<String, Object>> selectVarFuncList(){
        List<Map<String, Object>> map;
        try (SqlSession session = factory.openSession()) {
            map = session.selectList("CommonMapper.selectVarFuncList");
        }

        return map;
    }

    public Map<String, Object> selectSttusCd(Map<String, Object> map){
        Map<String, Object> rstMap;
        try (SqlSession session = factory.openSession()) {
            rstMap = session.selectOne("CommonMapper.selectSttusCd", map);
        }

        return rstMap;
    }

    public List<Map<String, Object>> selectModelsInfo(Map<String, Object> map){
        List<Map<String, Object>> rstListMap;
        try (SqlSession session = factory.openSession()) {
            rstListMap = session.selectList("CommonMapper.selectModelsInfo", map);
        }

        return rstListMap;
    }

    public List<Map<String, Object>> selectProjectsInfo(){
        List<Map<String, Object>> rstListMap;
        try (SqlSession session = factory.openSession()) {
            rstListMap = session.selectList("CommonMapper.selectProjectsInfo");
        }

        return rstListMap;
    }
    public String selectProjectsStatusCode(Map<String, Object> map){
        String rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectProjectsStatusCode", map);
        }
        return rst;
    }

    public Map<String, Object> selectWorkflowInfo(Map<String, Object> map){
        Map<String, Object> rstMap;
        try (SqlSession session = factory.openSession()) {
            rstMap = session.selectOne("CommonMapper.selectWorkflowInfo");
        }

        return rstMap;
    }

    public Map<String, Object> selectDatasetInfo(String dataset_id){
        Map<String, Object> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectDatasetInfo", dataset_id);
        }

        return rst;
    }

    public List<Map<String, Object>> selectDASchedule(String status) {
        List<Map<String, Object>> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectList(mapperName + "selectDASchedule", status);
        }

        return rst;
    }

    public Map<String, Object> selectDataAnlsInfo(Map<String, Object> map) {
        Map<String, Object> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectDataAnlsInfo", map);
        }

        return rst;
    }

    public Map<String, Object> selectDataAnlsInfoWithDataAnalsID(String dataAnalsID) {
        Map<String, Object> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectDataAnlsInfoWithDataAnalsID", dataAnalsID);
        }

        return rst;
    }

    public String selectUUID(){
        String rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectUUID");
        }
        return rst;
    }

    // Update
    public void updateSttusCd(Map<String, Object> map){
        try (SqlSession session = factory.openSession()) {
            session.update("CommonMapper.updateSttusCd", map);
            session.commit();
        }
    }

    public void updateEps(Map<String, Object> map){
        try (SqlSession session = factory.openSession()) {
            session.update("CommonMapper.updateEps", map);
            session.commit();
        }
    }

    public void updateLearnResult(Map<String, Object> map){
        try (SqlSession session = factory.openSession()) {
            session.update("CommonMapper.updateLearnResult", map);
            session.commit();
        }
    }

    public void updateStartTime(Map<String, Object> map){
        try (SqlSession session = factory.openSession()) {
            session.update("CommonMapper.updateStartTime", map);
            session.commit();
        }
    }

    public void updateEndTime(Map<String, Object> map){
        try (SqlSession session = factory.openSession()) {
            session.update("CommonMapper.updateEndTime", map);
            session.commit();
        }
    }

    public void updateDAStatus(Map<String, Object> map){
        try (SqlSession session = factory.openSession()) {
            session.update("CommonMapper.updateDAStatus", map);
            session.commit();
        }
    }


    // insert
    public void insertDataset(Map<String, Object> map) {
        try(SqlSession session = factory.openSession()) {
            session.insert("CommonMapper.insertDataset", map);
            session.commit();
        }
    }
    public void insertDataAnlsInfo(Map<String, Object> map) {
        try(SqlSession session = factory.openSession()) {
            session.insert("CommonMapper.insertDataAnlsInfo", map);
            session.commit();
        }
    }


    // delete
    public void deleteDataset(Map<String, Object> map) {
        try(SqlSession session = factory.openSession()) {
            session.delete("CommonMapper.deleteDataset", map);
            session.commit();
        }
    }

    public static void main(String[] args) {

        System.out.println(new CommonDAO().selectVarFuncList().toString());
    }
}
