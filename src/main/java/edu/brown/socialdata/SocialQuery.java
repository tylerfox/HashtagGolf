package edu.brown.socialdata;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import twitter4j.TwitterException;

public interface SocialQuery {

  /**
   * Initializes a Social Querier.
   */
  public void init();

  /**
   * Gets the relevant count of whatever social network queried.
   * @param queryString the word or phrase to query
   * @param duration the amount time to query for
   * @return the number of words found from the query
   * @throws TwitterException if a twitter exception was thrown
   */
  public int getCount(String queryString, int duration) throws TwitterException;

  /**
   * The time that has taken.
   * @param now the start of the time
   * @param time the amount of time elapsed
   * @return the difference in time between them
   */
  default long timeElapsed(Date now, long time) {
    return TimeUnit.MILLISECONDS.toSeconds(now.getTime() - time);
  }

}
