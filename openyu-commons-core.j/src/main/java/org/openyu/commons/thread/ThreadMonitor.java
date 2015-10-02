package org.openyu.commons.thread;

public interface ThreadMonitor {

  void setEnabled(boolean enabled);

  boolean isEnabled();

  void setPeriod(long period);

  long getPeriod();

}