package com.seculayer.util.conf;


import com.seculayer.util.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class LogConfiguration extends Configuration {
  static Logger log = LogManager.getLogger(LogConfiguration.class);

  private String chukwaHome, chukwaConf;

  public String getChukwaHome() {
    return chukwaHome;
  }

  public String getChukwaConf() {
    return chukwaConf;
  }

  public LogConfiguration() {
    this(true);
  }

  public LogConfiguration(boolean loadDefaults) {
    super();
    if (loadDefaults) {

      chukwaHome = System.getenv("CHUKWA_HOME");
      if (chukwaHome == null) {
        chukwaHome = ".";
      }

      if (!chukwaHome.endsWith("/")) {
        chukwaHome = chukwaHome + File.separator;
      }
      chukwaConf = System.getenv("CHUKWA_CONF_DIR");
      if (chukwaConf == null) {
        chukwaConf = chukwaHome + "conf" + File.separator;
      }

      log.info("chukwaConf is " + chukwaConf);

      super.addResource(new Path(chukwaConf + "/chukwa-common.xml"));
      log.debug("added chukwa-agent-conf.xml to ChukwaConfiguration");

      super.addResource(new Path(chukwaConf + "/chukwa-agent-conf.xml"));
      log.debug("added chukwa-agent-conf.xml to ChukwaConfiguration");

      super.addResource(new Path(chukwaConf + "/chukwa-collector-conf.xml"));
      log.debug("added chukwa-collector-conf.xml to ChukwaConfiguration");

      super.addResource(new Path(chukwaConf + "/chukwa-demux-conf.xml"));
      log.debug("added chukwa-demux-conf.xml to ChukwaConfiguration");
    }
  }

}
