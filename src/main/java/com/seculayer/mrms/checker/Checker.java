package com.seculayer.mrms.checker;

import com.seculayer.mrms.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.TimerTask;

abstract public class Checker extends TimerTask {
    protected Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected Request req = null;

    // builder pattern
    public Checker() {}

    public Checker req(Request req) {
        if (req != null) {
            req.start();
        }
        return this;
    }

    @Override
    public void run() {
        try {
            doCheck();
        } catch (CheckerException e) {
            logger.error("Checker-ERROR:", e);
        }
    }

    public abstract void doCheck() throws CheckerException;
}
