package com.seculayer.mrms;

import com.seculayer.mrms.managers.MRMServerManager;
import sun.misc.Signal;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import sun.misc.SignalHandler;


public class MRMServerMain {
    private static class SignalHandlerImpl implements SignalHandler {
        @Override
        public void handle(Signal signal) {
            Logger logger = LogManager.getLogger();
            MRMServerManager.getInstance().terminate();
            logger.info("MRManagementServer Main Terminate...");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        Logger logger = LogManager.getLogger();

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
