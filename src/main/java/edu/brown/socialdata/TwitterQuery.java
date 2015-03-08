package edu.brown.socialdata;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterQuery {

  public static void query(String queryString) {
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true)
    .setOAuthConsumerKey("LdHRl9Lp7AaoxTRp6p316IHd5")
    .setOAuthConsumerSecret(
        "gkE5FZJv1xNbInq01TWJqJjZ2vXuy2ra2FMpJO0X4G4v7ppLeD")
        .setOAuthAccessToken(
            "285842636-zuA21Cw3DNlpn6abviuFyUHqEaY5m4tM9GAE8YiH")
            .setOAuthAccessTokenSecret(
                "PMOLTk5nCagNlgLmfkaf6lx5ijPm2wJLJh3wiWJjKsIHv");
    TwitterFactory tf = new TwitterFactory(cb.build());
    Twitter twitter = tf.getInstance();
    Query query = new Query(queryString);
    query.setCount(100);
    query.setResultType(Query.RECENT);
    QueryResult result;
    try {
      result = twitter.search(query);
      int numTweets = 0;
      Date now = new Date();
      for (Status status : result.getTweets()) {
        // if (timeElapsed(now, status.getCreatedAt().getTime()) < 50) {
        System.out.println("@" + status.getUser().getScreenName() + ":"
            + status.getText());
        numTweets++;
        // }
      }
      System.out.println("Number of Tweets:" + numTweets);
    } catch (TwitterException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static long timeElapsed(Date now, long time) {
    return TimeUnit.MILLISECONDS.toSeconds(now.getTime() - time);
  }
}
