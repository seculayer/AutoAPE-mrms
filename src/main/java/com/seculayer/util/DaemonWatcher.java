package com.seculayer.util;

public class DaemonWatcher extends PidFile {
  private static DaemonWatcher instance = null;
  
  public synchronized static DaemonWatcher createInstance(String name) {
    if(instance == null) {
      instance = new DaemonWatcher(name);
      Runtime.getRuntime().addShutdownHook(instance);
    }
    return instance;
  }
  
  public static DaemonWatcher getInstance() {
    return instance;
  }
  
  private DaemonWatcher(String name) {
    super(name);
  }
  
  public static void bailout(int status) {
    System.exit(status);
  }
}
