package com.redeyetechguy.kiji;


import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.kiji.mapreduce.KijiTableContext;
import org.kiji.mapreduce.bulkimport.KijiBulkImporter;
import org.kiji.schema.EntityId;

import java.io.IOException;

public class MovieBulkImporter extends KijiBulkImporter<LongWritable, Text>
{
    private static Logger _logger;
    private Rating _rating;


    @Override
    public void setup(KijiTableContext context) throws IOException {
        super.setup(context);
        //Configuring log4j with defaults to log on console
        BasicConfigurator.configure();
        _logger = Logger.getLogger(MovieBulkImporter.class);
        _rating = Rating.newBuilder()
                .setRating(0)
                .setUserId("")
                .setTimestamp("")
                .build();
    }


    @Override
    public void produce(LongWritable key, Text value, KijiTableContext context) throws IOException {

        try {
            //parse string
            String[] attributes = value.toString().split("\\|");

            //Generate row key
            String movieId = attributes[0];
            EntityId entityId = context.getEntityId(movieId);


            //Extract movie attributes
            String[] movieInfo = attributes[1].split(",");
            int movieReleaseYear = Integer.parseInt(movieInfo[0]);
            String movieName = movieInfo[1];

            //Write movie attributes
            context.put(entityId, "info", "movie_id", movieId);
            context.put(entityId, "info", "movie_name", movieName);
            context.put(entityId, "info", "movie_release_year", movieReleaseYear);
            context.put(entityId, "info", "similar_movies", null);

            //Extract & write ratings for this movie
            for (int i = 2; i < attributes.length; i++){
                try {
                    String[] ratingInfo = attributes[i].split(",");
                    _rating.setUserId(ratingInfo[0]);
                    _rating.setRating(Integer.parseInt(ratingInfo[1]));
                    _rating.setTimestamp(ratingInfo[2]);
                    context.put(entityId, "rating", _rating.getUserId().toString(), _rating);
                } catch (Exception e) {
                    _logger.error(e);
                }
            }

        } catch (Exception e){
            _logger.error(e);
        }
    }
}
