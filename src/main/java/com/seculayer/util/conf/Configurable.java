package com.seculayer.util.conf;

/** Something that may be configured with a {@link Configuration}. */
public interface Configurable {

  /** Set the configuration to be used by this object. */
  void setConf(Configuration conf);

  /** Return the configuration used by this object. */
  Configuration getConf();
}
