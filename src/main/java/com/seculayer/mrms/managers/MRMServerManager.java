package com.seculayer.mrms.managers;

import com.seculayer.mrms.db.CommonDAO;
import com.seculayer.util.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.lang.invoke.MethodHandles;

public class MRMServerManager {
    // base variables
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Configuration conf = new Configuration(false);

    // REST server
    private HTTPServerManager httpServer;

    private MRMServerManager(){}

    // Singleton pattern
    private static class Singleton {
        private static final MRMServerManager instance = new MRMServerManager();
    }

    public static MRMServerManager getInstance() { return Singleton.instance; }
    public Configuration getConfiguration() { return conf; }

    // initialize
    public void init(){
        try {
            // Configuration load
            FileInputStream fis = new FileInputStream("./conf/mrms-conf.xml");
            conf.addResource(fis);

            // HTTP Server
            httpServer = new HTTPServerManager();
            httpServer.start();

            // Database Test
            CommonDAO commonDAO = new CommonDAO();
            commonDAO.selectTestQuery();

        } catch (Exception e) {
            logger.error(String.format("initialize error : %s", e));
            e.printStackTrace();
        }
    }

    public void terminate() {
        try {
            httpServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
