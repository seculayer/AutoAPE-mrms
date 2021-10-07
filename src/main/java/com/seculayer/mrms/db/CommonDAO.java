package com.seculayer.mrms.db;

import com.seculayer.mrms.managers.DBSessionManager;
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

    public List<Map<String, Object>> selectCVTFunction(){
        String funcName = mapperName + "selectCVTFunction";
        SqlSession session = factory.openSession();
        List<Map<String, Object>> map = null;
        try {
            map = session.selectList(funcName);
            logger.debug("select list query : {}", map);
        } finally {
            session.close();
        }
        return map;
    }

    public static void main(String[] args) {
        System.out.println(new CommonDAO().selectCVTFunction().toString());
    }
}
