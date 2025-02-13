package vttp.batch5.paf.movies.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.movies.utils.MongoParams;

@Repository
public class MongoMovieRepository 
{
    @Autowired
    private MongoTemplate mongoTemplate;

    // Helper method for Task 2.1
    public boolean checkMongoInserted()
    {
        long count = mongoTemplate.getCollection(MongoParams.C_MOVIES).countDocuments();
        System.out.println(">>>>> Mongo count: " + count);

        if (count <= 0)
        {
        return false;
        }

        return true;
    }

 // TODO: Task 2.3
 // You can add any number of parameters and return any type from the method
 // You can throw any checked exceptions from the method
 // Write the native Mongo query you implement in the method in the comments
 //
 //    native MongoDB query here
 //
 public void batchInsertMovies() {

 }

 // TODO: Task 2.4
 // You can add any number of parameters and return any type from the method
 // You can throw any checked exceptions from the method
 // Write the native Mongo query you implement in the method in the comments
 //
 //    native MongoDB query here
 //
 public void logError() {

 }

 // TODO: Task 3
 // Write the native Mongo query you implement in the method in the comments
 //
 //    native MongoDB query here
 //


}
