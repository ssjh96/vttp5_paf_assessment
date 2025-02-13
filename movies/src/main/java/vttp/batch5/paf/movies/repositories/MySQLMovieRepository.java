package vttp.batch5.paf.movies.repositories;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;
import vttp.batch5.paf.movies.utils.SqlQueries;

@Repository
public class MySQLMovieRepository 
{
  @Autowired 
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private MongoMovieRepository mongoMovieRepository;

  public boolean checkSqlInserted()
  {
    SqlRowSet rs = jdbcTemplate.queryForRowSet(SqlQueries.SQL_CHECK_COUNT);

    rs.next();
    String countString = rs.getString("count");
    Integer count = Integer.parseInt(countString);
    System.out.println(">>>>> SQL count: " + count);

    if(count <= 0)
    {
      return false;
    }
  
    return true;
  }
  // TODO: Task 2.3
  // You can add any number of parameters and return any type from the method
  public void batchInsertMovies(List<JsonObject> filteredMovies) 
  {
    List<Object[]> batchArgs = new ArrayList<>();

    for (JsonObject jMovie : filteredMovies)
    {
      String imdbId = jMovie.getString("imdb_id");
      float voteAverage = (float)jMovie.getJsonNumber("vote_average").doubleValue();
      Integer voteCount = jMovie.getInt("vote_count");
      String releaseDate = jMovie.getString("release_date");
      Double revenue = jMovie.getJsonNumber("revenue").doubleValue();
      Double budget = jMovie.getJsonNumber("budget").doubleValue();
      Integer runtime = jMovie.getInt("runtime");
      
      Object[] args = {imdbId, voteAverage, voteCount, releaseDate, revenue, budget, runtime};
      
      batchArgs.add(args);

      if (batchArgs.size() == 25) // if batch is 25, commence insertion
      {
        String[] idArray = getImdbIds(batchArgs);

        try 
        {
          jdbcTemplate.batchUpdate(SqlQueries.SQL_INSERT_RECORD, batchArgs);
          
        } catch (Exception e) {
          mongoMovieRepository.logError(idArray, e.getMessage(), new Date());
        }
        batchArgs.clear(); // clear args from the list and repopulate 
      }
    }

    if(!batchArgs.isEmpty())
    {
      jdbcTemplate.batchUpdate(SqlQueries.SQL_INSERT_RECORD, batchArgs); // insert remainder into db
    }

    System.out.println(">>> MySql insert completed");
  }

  private String[] getImdbIds(List<Object[]> batchArgs)
  {
    List<String>idList = new ArrayList<>();
    for(Object[] objArray : batchArgs)
    {
      idList.add(String.valueOf(objArray[0]));
    }
    String[] stringArray = idList.toArray(new String[0]);

    return stringArray;
  }

  // TODO: Task 3


}
