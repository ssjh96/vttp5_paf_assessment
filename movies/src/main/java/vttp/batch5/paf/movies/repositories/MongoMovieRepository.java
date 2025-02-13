package vttp.batch5.paf.movies.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.batch5.paf.movies.utils.MongoParams;

@Repository
public class MongoMovieRepository 
{
    @Autowired
    private MongoTemplate mongoTemplate;

    // Helper method for Task 2.1
    public boolean checkMongoInserted()
    {
        long count = mongoTemplate.getCollection(MongoParams.C_IMDB).countDocuments();
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
 // db.imdb.insertMany(
//     {doc},
//      {doc},.... 
// )
//
// Insert them in batches of 25x {doc} above
 public void batchInsertMovies(List<JsonObject> filteredMovies) 
 {
    List<Document> docList = new ArrayList<>();

    for (JsonObject jMovie : filteredMovies)
    {
        String imdbId = jMovie.getString("imdb_id");
        String title = jMovie.getString("title");
        String director = jMovie.getString("director");
        String overview = jMovie.getString("overview");
        String tagline = jMovie.getString("tagline");
        String genres = jMovie.getString("genres");
        Integer imdbRating = jMovie.getInt("imdb_rating");
        Integer imdbVotes = jMovie.getInt("imdb_votes");

        Document doc = new Document().append("_id", imdbId) // imdb_id from jsonobject becomes primary key for collection
                                    .append("title", title)
                                    .append("director", director)
                                    .append("overview", overview)
                                    .append("tagline", tagline)
                                    .append("genres", genres)
                                    .append("imdb_rating", imdbRating)
                                    .append("imdb_votes",imdbVotes);
        
        docList.add(doc);

        if(docList.size() == 25)
        {
            String[] idArray = getImdbIds(docList);
            try {
                mongoTemplate.getCollection(MongoParams.C_IMDB).insertMany(docList);
                
            } catch (Exception e) {
                logError(idArray, e.getMessage(), new Date());
            }
            docList.clear();
            
        }
    }

    // after for loop but list not empty but less than 25, insert remaining
    if(!docList.isEmpty())
    {
        mongoTemplate.getCollection(MongoParams.C_IMDB).insertMany(docList);
    }

    System.out.println(">>> Mongo insert completed");
 }

 private String[] getImdbIds(List<Document> docList)
  {
    List<String>idList = new ArrayList<>();
    for(Document d : docList)
    {
      idList.add(d.getString("_id"));
    }
    
    String[] stringArray = idList.toArray(new String[0]);

    return stringArray;
  }

 // TODO: Task 2.4
 // You can add any number of parameters and return any type from the method
 // You can throw any checked exceptions from the method
 // Write the native Mongo query you implement in the method in the comments
 //
 // db.errors.insert({
 // ids: ['a0','a1','a2','a3','a4'],
 // error: '<e.getMessage()',
 // timestamp: 'java.util.Date of the exception'
 // })
 //    
 public void logError(String[] ids, String errorMsg, Date timestamp) 
 {
    Document toInsert = new Document();
    toInsert.put("ids", ids);
    toInsert.put("error", errorMsg);
    toInsert.put("date", new Date());

    mongoTemplate.insert(toInsert, MongoParams.C_ERRORS);
 }

 
 // TODO: Task 3
 // Write the native Mongo query you implement in the method in the comments
 //

//  db.imdb.aggregate([
//     {
//       "$group": {
//         "_id": "$director",
//         "movies_count": { "$sum": 1 }
//       }
//     },
//     {"$limit": 10}
//   ])
  

public List<Document> getProlificDirectors(int limit)
{
    GroupOperation groupByDirector = Aggregation.group("director")
                        .sum("movies_count").as("movies_count")

    LimitOperation limitOperation = Aggregation.limit(limit);

    Aggregation pipeLine = Aggregation.newAggregation(groupByDirector, limitOperation);

    AggregationResults<Document> aggregationResults = mongoTemplate.aggregate(pipeLine, MongoParams.C_IMDB, Document.class);

    return aggregationResults.getMappedResults();
}

}
