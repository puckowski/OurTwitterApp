package com.mobiledev.puckowski.mobilefinal3;

import android.util.Log;

import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Daniel on 12/11/2015.
 */
public class TwitterParser
{
    private final String DEFAULT_CONTENT_SEARCH = "News";

    public static String CONSUMER_KEY = "P3McglNekPzD04Erbh8ETISuA";
    public static String CONSUMER_SECRET = "3sZFio6Lid1T3UGgbIJJ8bCUC9GA0wsKoJARm7nnJ33VnXRI41";
    public static String ACCESS_TOKEN = "4417407844-tfwKOWfDSpBIqoVO05y7s8PktglWpgX06Lh0GtK";
    public static String ACCESS_TOKEN_SECRET = "oE826vnArS7LmNnIfAUpCtg2dLrKQ9kWe5IjruxGrf2hg";

    ArrayList<Status> mTweets;
    boolean mBusyWait;

    public ArrayList<Status> getTweetsContainingText(final String textToSearch)
    {
        mBusyWait = true;

        Thread twitterNetworkThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                        .setOAuthConsumerKey(TwitterParser.CONSUMER_KEY)
                        .setOAuthConsumerSecret(TwitterParser.CONSUMER_SECRET)
                        .setOAuthAccessToken(TwitterParser.ACCESS_TOKEN)
                        .setOAuthAccessTokenSecret(TwitterParser.ACCESS_TOKEN_SECRET);

                TwitterFactory tf = new TwitterFactory(cb.build());
                Twitter twitter = tf.getInstance();

                try
                {
                    Query query = null;

                    if(textToSearch == null || textToSearch.isEmpty())
                    {
                        query = new Query(DEFAULT_CONTENT_SEARCH);
                    }
                    else
                    {
                        query = new Query(textToSearch);
                    }

                    QueryResult result;
                    result = twitter.search(query);
                    mTweets = (ArrayList<Status>) result.getTweets();

                    for (Status tweet : mTweets)
                    {
                        Log.v("Tweet: ", "@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
                    }

                    mBusyWait = false;
                }
                catch (final TwitterException twitterException)
                {
                    //twitterException.printStackTrace();

                    System.out.println("Failed to search tweets: " + twitterException.getMessage());
                }
            }
        });

        twitterNetworkThread.start();

        while(mBusyWait)
        {

        }

        return mTweets;
    }
}
