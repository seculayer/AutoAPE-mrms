package com.seculayer.mrms;

import com.seculayer.mrms.managers.MRMServerManager;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.lang.invoke.MethodHandles;

public class MRMServerMain {
    private static class SignalHandlerImpl implements SignalHandler {

        @Override
        public void handle(Signal signal) {
            Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
            MRMServerManager.getInstance().terminate();
            logger.info("MRManagementServer Main Terminate...");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("./conf/log4j.properties");
        Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

        // SIGNAL HANDLER
        SignalHandler signalHandler = new SignalHandlerImpl();
        Signal.handle(new Signal( "INT" ) , signalHandler);
        Signal.handle(new Signal( "TERM" ) , signalHandler);

        logger.info("######################################################################");
        logger.info("MRManagementServer Start...");
        MRMServerManager.getInstance().init();
        logger.info("######################################################################");
    }
}
