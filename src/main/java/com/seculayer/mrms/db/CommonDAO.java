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

    private static String mapperName = "CommonMapper.";

    public void selectTestQuery(){
        String funcName = mapperName + "selectTestQuery";
        SqlSession session = factory.openSession();
        try {
            Map<String, Object> map = session.selectOne(funcName);
            logger.debug("select test query : {}", map);
        } finally {
            session.close();
        }
    }

    public List<Map<String, Object>> selectVarFuncList(){
        SqlSession session = factory.openSession();
        List<Map<String, Object>> map;
        try {
            map = session.selectList("CommonMapper.selectVarFuncList");
        } finally {
            session.close();
        }

        return map;
    }

    public void updateSttusCd(String histNo, String status, String taskIdx, String message){
        SqlSession session = factory.openSession();
        Map<String, Object> map = new HashMap<>();
        map.put("learn_sttus_cd", status);
        map.put("hist_no", histNo);
        map.put("task_idx", taskIdx);
        map.put("message", message);
        try{
            session.update("CommonMapper.updateSttusCd", map);
            session.commit();
        }
        finally {
            session.close();
        }
    }

    public Map<String, Object> selectSttusCd(String histNo){
        SqlSession session = factory.openSession();
        Map<String, Object> map = new HashMap<>();
        map.put("hist_no", histNo);
        Map<String, Object> rstMap;
        try {
            rstMap = session.selectOne("CommonMapper.selectSttusCd", map);
        } finally {
            session.close();
        }

        return rstMap;
    }

    public void updateEps(String histNo, double eps){
        SqlSession session = factory.openSession();
        Map<String, Object> map = new HashMap<>();
        map.put("eps", eps);
        map.put("hist_no", histNo);
        try{
            session.update("CommonMapper.updateEps", map);
            session.commit();
        }
        finally {
            session.close();
        }
    }

    public void updateLearnResult(Map map){
        SqlSession session = factory.openSession();
        try{
            session.update("CommonMapper.updateLearnResult", map);
            session.commit();
        }
        finally {
            session.close();
        }
    }

    public void updateStartTime(Map map){
        SqlSession session = factory.openSession();
        try{
            session.update("CommonMapper.updateStartTime", map);
            session.commit();
        }
        finally {
            session.close();
        }
    }

    public void updateEndTime(Map map){
        SqlSession session = factory.openSession();
        try{
            session.update("CommonMapper.updateEndTime", map);
            session.commit();
        }
        finally {
            session.close();
        }
    }
}
