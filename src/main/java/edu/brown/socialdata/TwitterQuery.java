package edu.brown.socialdata;

import java.util.Date;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterQuery implements SocialQuery {

  ConfigurationBuilder cb;
  Twitter twitter;
  int numTweets;

  public TwitterQuery() {
    init();
  }

  @Override
  public void init() {
    cb = new ConfigurationBuilder();
    cb.setDebugEnabled(true)
        .setOAuthConsumerKey("LdHRl9Lp7AaoxTRp6p316IHd5")
        .setOAuthConsumerSecret(
            "gkE5FZJv1xNbInq01TWJqJjZ2vXuy2ra2FMpJO0X4G4v7ppLeD")
        .setOAuthAccessToken(
            "285842636-zuA21Cw3DNlpn6abviuFyUHqEaY5m4tM9GAE8YiH")
        .setOAuthAccessTokenSecret(
            "PMOLTk5nCagNlgLmfkaf6lx5ijPm2wJLJh3wiWJjKsIHv");
    TwitterFactory tf = new TwitterFactory(cb.build());
    twitter = tf.getInstance();
  }

  public long query(Query q, int duration) {
    try {
      Date now = new Date();
      QueryResult result;
      result = twitter.search(q);
      List<Status> tweets = result.getTweets();
      int tweetsSize = tweets.size();
      for (int i = 0; i < tweetsSize; i++) {
        Status curStatus = tweets.get(i);
        if (timeElapsed(now, curStatus.getCreatedAt().getTime()) < duration) {
          numTweets++;
          //System.out.println("@" + curStatus.getUser().getScreenName() + ":"
            //  + curStatus.getText());
          int elapsedTime = (int) timeElapsed(now, curStatus.getCreatedAt()
              .getTime());
         // System.out.println(elapsedTime + "seconds ago");
          if (i == tweetsSize - 1) {
            if (elapsedTime < duration) {
              return curStatus.getId();
            } else {
              return 0;
            }
          }
        } else {
          return 0;
        }
      }
    } catch (TwitterException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return -1;
  }

  @Override
  public int getCount(String queryString, int duration) {
    Query q = new Query(queryString);
    q.setCount(100);
    q.setResultType(Query.RECENT);
    numTweets = 0;
    long maxId;
    while ((maxId = query(q, duration)) > 0) {
      q.setMaxId(maxId);
    }
    //System.out.println(numTweets);
    return numTweets;
  }
}
