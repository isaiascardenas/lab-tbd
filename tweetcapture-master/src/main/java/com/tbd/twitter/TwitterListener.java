package com.tbd.twitter;

import javax.annotation.PostConstruct;

import Utilities.ChileDetector;
import Utilities.SentimentSpanish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;


@Service
@Configurable
public class TwitterListener {
	@Autowired
	private TwitterStream twitterStream;
	@Autowired
	private MongoTemplate mongo;
	
	private SentimentSpanish sentimentSpanish;
		
	@PostConstruct
	public void run() {
		twitterStream.addListener(new StatusListener() {
			public void onStatus(Status status) {
					Tweet tweet = new Tweet();
					tweet.set_id(status.getId());
					tweet.setCreatedAt(status.getCreatedAt());
					tweet.setGeoLocation(status.getGeoLocation());
					tweet.setText(status.getText());
					tweet.setRetweetCount(status.getRetweetCount());
					tweet.setFavoriteCount(status.getFavoriteCount());
					tweet.setLocation(status.getUser().getLocation());
					tweet.setRetweet(status.isRetweet());
					tweet.setUserId(status.getUser().getId());
					tweet.setUserScreenName(status.getUser().getScreenName());
					tweet.setUserVerified(status.getUser().isVerified());
					tweet.setUserFollowersCount(status.getUser().getFollowersCount());
					tweet.setUserStatusesCount(status.getUser().getStatusesCount());
					tweet.setUserFriendsCount(status.getUser().getFriendsCount());
					sentimentSpanish.analyze(status.getText());
					tweet.setPositivePercent(sentimentSpanish.getPositivePercent()); //crear estos
					tweet.setNegativePercent(sentimentSpanish.getNegativePercent()); //crear estos
					tweet.setPositiveScore(sentimentSpanish.getPositiveScore()); //crear estos
					tweet.setNegativeScore(sentimentSpanish.getNegativeScore()); //crear estos
					tweet.setAnalysis(sentimentSpanish.getAnalysis()); //crear estos
					mongo.insert(tweet);
	        }

			@Override
			public void onException(Exception arg0) {
								
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
							
			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
								
			}

			@Override
			public void onStallWarning(StallWarning arg0) {
								
			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
								
			}			
		});
		
		this.sentimentSpanish = SentimentSpanish.getInstance();
		this.sentimentSpanish.loadWords();
		
		
		// se creara la lista con las palabras o frases claves
				String[ ] bolsa = new String[34];
				bolsa[0] = "Tenis";
				bolsa[1] = "Boxeo";
				bolsa[2] = "Volleyball";
				bolsa[3] = "Futbol Femenino";
				bolsa[4] = "Rugby";
				bolsa[5] = "Natacion";
				bolsa[6] = "Basketball";
				bolsa[7] = "Chino Rios";
				bolsa[8] = "Martin del Potro";
				bolsa[9] = "Tenista";
				bolsa[10] = "Basquetbolista";
				bolsa[11] = "Myke Tyson";
				bolsa[12] = "Floyd Mayweather";
				bolsa[13] = "Kobe Bryant";
				bolsa[14] = "Michael Phelps";
				bolsa[15] = "Colo Colo Femenino";
				bolsa[16] = "ColoColo Femenino";
				bolsa[17] = "Wilton Norman";
				bolsa[18] = "Muhammad Ali";
				bolsa[19] = "Ian Thorpe";
				bolsa[20] = "Nadador";
				bolsa[21] = "Cancha de tenis";
				bolsa[22] = "Marcelo Rios";
				bolsa[23] = "Basket";
				bolsa[24] = "Basquet";
				bolsa[25] = "Basquetball";
				bolsa[26] = "Basquetbol";
				bolsa[27] = "Volley";
				bolsa[28] = "Boxeador";
				bolsa[29] = "Boxista";
				bolsa[30] = "volley";
				bolsa[31] = "rugbista";
				//bolsa[31] = "Pauline Kantor Pupkin";
				//bolsa[32] = "Isabel Plá Jarufe";
				//bolsa[33] = "Mauricio José Rojas Mullor";
				
				
	    FilterQuery filter = new FilterQuery();
	    //filter.track(new String[]{"presidente piñera"});
	    filter.track(bolsa);
	    filter.language(new String[]{"es"});
	    twitterStream.filter(filter);
	}

	public TwitterStream getTwitterStream() {
		return twitterStream;
	}

	public void setTwitterStream(TwitterStream twitterStream) {
		this.twitterStream = twitterStream;
	}

	public MongoTemplate getMongo() {
		return mongo;
	}

	public void setMongo(MongoTemplate mongo) {
		this.mongo = mongo;
	}
}
