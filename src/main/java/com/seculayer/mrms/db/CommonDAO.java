package com.seculayer.mrms.db;

import com.seculayer.mrms.managers.DBSessionManager;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

public class CommonDAO {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final SqlSessionFactory factory = DBSessionManager.getSqlSession();

    public void selectTestQuery(){
        SqlSession session = factory.openSession();
        try {
            Map<String, Object> map = session.selectOne("CommonMapper.selectTestQuery");
            logger.info("select test query : {}", map);
        } finally {
            session.close();
        }
    }
}
