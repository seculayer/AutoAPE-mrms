package com.seculayer.mrms.db;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
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

    public Map<String, Object> selectInfrSttusCd(Map<String, Object> map){
        Map<String, Object> rstMap;
        try (SqlSession session = factory.openSession()) {
            rstMap = session.selectOne("CommonMapper.selectInfrSttusCd", map);
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

    public Map<String, Object> selectProjectInfo(String projectID){
        Map<String, Object> rstListMap;
        try (SqlSession session = factory.openSession()) {
            rstListMap = session.selectOne("CommonMapper.selectProjectInfo", projectID);
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

    public Map<String, Object> selectDataAnlsID(Map<String, Object> map){
        Map<String, Object> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectDataAnlsID", map);
        }
        return rst;
    }

    public Map<String, Object> selectAlgInfo(Map<String, Object> map) {
        Map<String ,Object> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectAlgInfo", map);
        }
        return rst;
    }

    public String selectDatasetFormat(String dataAnalsID) {
        String rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectDatasetFormat", dataAnalsID);
        }

        return rst;
    }

    public String selectEvalResult(String learnHistNo) {
        String rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectEvalResult", learnHistNo);
        }

        return rst;
    }

    public List<Map<String, Object>> selectInferenceSchedule(String inferenceStatus) {
        List<Map<String, Object>> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectList(mapperName + "selectInferenceSchedule", inferenceStatus);
        }

        return rst;
    }

    public Map<String, Object> selectLearnHistInfo(String learnHistNo) {
        Map<String, Object> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectLearnHistInfo", learnHistNo);
        }

        return rst;
    }

    public List<Map<String, Object>> selectInferenceInfo(String project_id) {
        List<Map<String, Object>> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectList(mapperName + "selectInferenceInfo", project_id);
        }

        return rst;
    }

    public List<Map<String, Object>> selectInferenceInfoDataset(String dataset_id) {
        List<Map<String, Object>> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectList(mapperName + "selectInferenceInfoDataset", dataset_id);
        }

        return rst;
    }

    public List<Map<String, Object>> selectRunInference(String learn_hist_no) {
        List<Map<String, Object>> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectList(mapperName + "selectRunInference", learn_hist_no);
        }

        return rst;
    }

    public String selectInferenceLog(String infr_hist_no) {
        String rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectOne(mapperName + "selectInferenceLog", infr_hist_no);
        }

        return rst;
    }

    public List<Map<String, Object>> selectXAISchedule(String xaiStatus) {
        List<Map<String, Object>> rst;
        try (SqlSession session = factory.openSession()) {
            rst = session.selectList(mapperName + "selectXAISchedule", xaiStatus);
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

    public void updateInferenceSttusCd(Map<String, Object> map){
        try (SqlSession session = factory.openSession()) {
            session.update("CommonMapper.updateInferenceSttusCd", map);
            session.commit();
        }
    }

    public void updateXAISttusCd(Map<String, Object> map){
        try (SqlSession session = factory.openSession()) {
            session.update("CommonMapper.updateXAISttusCd", map);
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

    public void updateStartInfrTime(Map<String, Object> map){
        try (SqlSession session = factory.openSession()) {
            session.update("CommonMapper.updateStartInfrTime", map);
            session.commit();
        }
    }

    public void updateEndInfrTime(Map<String, Object> map){
        try (SqlSession session = factory.openSession()) {
            session.update("CommonMapper.updateEndInfrTime", map);
            session.commit();
        }
    }

    public void updateDAStatus(Map<String, Object> map){
        try (SqlSession session = factory.openSession()) {
            session.update("CommonMapper.updateDAStatus", map);
            session.commit();
        }
    }

    public void updateEvalResult(Map<String, Object> map){
        try (SqlSession session = factory.openSession()) {
            session.update("CommonMapper.updateEvalResult", map);
            session.commit();
        }
    }

    public void updateInferenceLog(Map<String, Object> map){
        try (SqlSession session = factory.openSession()) {
            session.update("CommonMapper.updateInferenceLog", map);
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
    public void insertInferenceInfo(Map<String, Object> map) {

        try(SqlSession session = factory.openSession()) {
            session.insert("CommonMapper.insertInferenceInfo", map);
            session.commit();
        }
    }
    public void insertXaiInfo(Map<String, Object> map) {

        try (SqlSession session = factory.openSession()) {
            session.insert("CommonMapper.insertXaiInfo", map);
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
    public void deleteDataAnalysis(Map<String, Object> map) {
        try(SqlSession session = factory.openSession()) {
            session.delete("CommonMapper.deleteDataAnalysis", map);
            session.commit();
        }
    }

    public static void main(String[] args) {

        System.out.println(new CommonDAO().selectVarFuncList().toString());
    }
}
