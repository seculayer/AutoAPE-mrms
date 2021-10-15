package com.seculayer.mrms.db;

import com.seculayer.mrms.managers.DBSessionManager;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.HashMap;
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

    public static void main(String[] args) {
        System.out.println(new CommonDAO().selectVarFuncList().toString());
    }
}
