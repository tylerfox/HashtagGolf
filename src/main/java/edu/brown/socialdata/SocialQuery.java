package edu.brown.socialdata;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public interface SocialQuery {

  public void init();

  public int getCount(String queryString, int duration);

  default long timeElapsed(Date now, long time) {
    return TimeUnit.MILLISECONDS.toSeconds(now.getTime() - time);
  }

}
