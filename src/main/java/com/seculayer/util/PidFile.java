package com.seculayer.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class PidFile extends Thread {

  String name;
  private static final Log log = LogFactory.getLog(PidFile.class);
  private static FileLock lock = null;
  private static FileOutputStream pidFileOutput = null;
  private static final String DEFAULT_HOME;
  
  static {
      //use /tmp as a default, only if we can't create tmp files via Java.
    File home = new File(System.getProperty("java.io.tmpdir"), "CloudESM");
    try {
      File tmpFile = File.createTempFile("collector", "discovertmp");
      File tmpDir = tmpFile.getParentFile();
      tmpFile.delete();
      home = new File(tmpDir, "collector");
      home.mkdir();
    } catch(IOException e) {
      log.debug(ExceptionUtil.getStackTrace(e));
    } finally {    
    	DEFAULT_HOME = home.getAbsolutePath();
    }
  }

    public PidFile(String name) {
    this.name = name;
    try {
      init();
    } catch (IOException ex) {
      clean();
      System.exit(-1);
    }
  }

  public void init() throws IOException {
    String pidLong = ManagementFactory.getRuntimeMXBean().getName();
    String[] items = pidLong.split("@");
    String pid = items[0];
    String homePath = System.getProperty("CLOUDESM_HOME");
    if(homePath == null) {
    	homePath = DEFAULT_HOME;
    }
    StringBuffer pidFilesb = new StringBuffer();
    String pidDir = System.getProperty("PID_DIR");
    if (pidDir == null) {
      pidDir = homePath + File.separator + "pid" + File.separator + "run";
    }
    pidFilesb.append(pidDir).append(File.separator).append(name).append(".pid");
    try {
      File existsFile = new File(pidDir);
      if (!existsFile.exists()) {
        boolean success = (new File(pidDir)).mkdirs();
        if (!success) {
          throw (new IOException());
        }
      }
      File pidFile = new File(pidFilesb.toString());

      pidFileOutput = new FileOutputStream(pidFile);
      pidFileOutput.write(pid.getBytes());
      pidFileOutput.flush();
      FileChannel channel = pidFileOutput.getChannel();
      PidFile.lock = channel.tryLock();
      if (PidFile.lock != null) {
        log.debug("Initlization succeeded...");
      } else {
        throw (new IOException("Can not get lock on pid file: " + pidFilesb));
      }
    } catch (IOException ex) {
      System.out.println("Initialization failed: can not write pid file to " + pidFilesb);
      log.error("Initialization failed...");
      log.error(ex.getMessage());
      System.exit(-1);
      throw ex;

    }

  }

  public void clean() {
    String homePath = System.getProperty("CLOUDESM_HOME");
    if(homePath == null) {
    	homePath = DEFAULT_HOME;
    }
    StringBuffer pidFilesb = new StringBuffer();
    String pidDir = System.getenv("PID_DIR");
    if (pidDir == null) {
      pidDir = homePath + File.separator + "pid" + File.separator + "run";
    }
    pidFilesb.append(pidDir).append(File.separator).append(name).append(".pid");
    String pidFileName = pidFilesb.toString();

    File pidFile = new File(pidFileName);
    if (!pidFile.exists()) {
      log.error("Delete pid file, No such file or directory: " + pidFileName);
    } else {
      try {
        lock.release();
        pidFileOutput.close();
      } catch (IOException e) {
        log.error("Unable to release file lock: " + pidFileName);
      }
    }

    boolean result = pidFile.delete();
    if (!result) {
      log.error("Delete pid file failed, " + pidFileName);
    }
  }

  public void run() {
    clean();
  }
}
