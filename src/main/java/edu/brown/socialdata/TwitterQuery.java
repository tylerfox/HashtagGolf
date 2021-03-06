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
        .setOAuthConsumerKey("XXXXXXXXXX")
        .setOAuthConsumerSecret("XXXXXXXX")
        .setOAuthAccessToken("XXXXXXX")
        .setOAuthAccessTokenSecret("XXXXXXXX");
    TwitterFactory tf = new TwitterFactory(cb.build());
    twitter = tf.getInstance();
  }

  public long query(Query q, int duration) throws TwitterException {
    Date now = new Date();
    QueryResult result;
    result = twitter.search(q);
    List<Status> tweets = result.getTweets();
    int tweetsSize = tweets.size();
    for (int i = 0; i < tweetsSize; i++) {
      Status curStatus = tweets.get(i);
      if (timeElapsed(now, curStatus.getCreatedAt().getTime()) < duration) {
        numTweets++;
        // System.out.println("@" + curStatus.getUser().getScreenName() + ":"
        // + curStatus.getText());
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
    return -1;
  }

  @Override
  public int getCount(String queryString, int duration) throws TwitterException {
    Query q = new Query(queryString);
    q.setCount(100);
    q.setResultType(Query.RECENT);
    numTweets = 0;
    long maxId;
    while ((maxId = query(q, duration)) > 0 && numTweets < 1000) {
      q.setMaxId(maxId);
    }
    return numTweets;
  }
}
