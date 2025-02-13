package vttp.batch5.paf.movies.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;
import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;

@Service
public class MovieService 
{
  @Autowired
  private MongoMovieRepository mongoMovieRepository;

  @Autowired
  private MySQLMovieRepository mySQLMovieRepository;

  public static final String zipFileName = "movies_post_2010.zip";
  public static final String jsonFileName = "movies_post_2010.json"; // file inside the zip

  // TODO: Task 2
  public boolean checkDataLoaded()
  {
    // Check if data inserted into both DB
    Boolean loadedMongo = mongoMovieRepository.checkMongoInserted();
    System.out.println(">>> Mongo Loaded: " + loadedMongo);

    Boolean loadedSql = mySQLMovieRepository.checkSqlInserted();
    System.out.println(">>> Sql Loaded: " + loadedSql);

    if (!loadedMongo || !loadedSql) // if either false
    {
      return false;
    }

    return true;
  }

  public void loadDataFromZip() throws IOException
  {
    System.out.println(">>> Loading zip file: " + zipFileName);

    // movies/src/main/resources/data/movies_post_2010.zip
    Path p = Paths.get("data", zipFileName);
    String absPath = p.toAbsolutePath().toString();

    File f = new File(absPath);

    if(!f.exists())
    {
      System.out.println("Zip file not found at: " + p.toAbsolutePath().toString());

      // catch exception later
    }

    System.out.println("Zip file found: " + absPath);

    List<JsonObject> jFilteredMoviesList = new ArrayList<>();

    // Try with resources (separated by ;)
    try (ZipFile zf = new ZipFile(f)) 
    { 
      ZipEntry entry = zf.getEntry(jsonFileName);

      try (InputStream is = zf.getInputStream(entry);
          BufferedReader br = new BufferedReader(new InputStreamReader(is))) 
      {
        String line;
        while((line=br.readLine()) != null)
        {
          JsonReader jReader = Json.createReader(new StringReader(line));
          JsonObject jRecord = jReader.readObject();

          JsonObject jFiltered = filterMovies(jRecord);
          if(jFiltered.containsKey("error"))
          {
            continue;
          }

          jFilteredMoviesList.add(jFiltered);
        }
      } catch (Exception e) {
        System.out.println(">>> ZipFile error: " + e.getMessage());
      }
    } catch (Exception e) {
      System.out.println(">>> is/br error: " + e.getMessage());
    }

    System.out.println(">>> Filtered Movie List Size: " + jFilteredMoviesList.size()); 

    mySQLMovieRepository.batchInsertMovies(jFilteredMoviesList);
    mongoMovieRepository.batchInsertMovies(jFilteredMoviesList);
  }
  


  private JsonObject filterMovies(JsonObject record) // filter 0, "", false
  {
    JsonObjectBuilder job = Json.createObjectBuilder();

    // Filter year
    String releasedYearStr = record.getString("release_date").substring(0, 4);
    Integer releasedYearInt = Integer.parseInt(releasedYearStr);

    if (releasedYearInt < 2018)
    {
      return job.add("error", "year < 2018").build(); // skip record
    }

    return job.add("title", record.getString("title", ""))
              .add("vote_average", filterFloatFromRecord(record, "vote_average"))
              .add("vote_count", record.getInt("vote_count", 0))
              .add("status", record.getString("status", ""))
              .add("release_date", record.getString("release_date", ""))
              .add("revenue", filterDoubleFromRecord(record, "revenue"))
              .add("runtime", record.getInt("runtime", 0))
              .add("budget", filterDoubleFromRecord(record, "budget"))
              .add("imdb_id", record.getString("imdb_id", ""))
              .add("original_language", record.getString("original_language", ""))
              .add("overview", record.getString("overview", ""))
              .add("popularity", record.getInt("popularity", 0))
              .add("tagline", record.getString("tagline", ""))
              .add("genres", record.getString("genres", ""))
              .add("spoken_languages", record.getString("spoken_languages", ""))
              .add("casts", record.getString("casts", ""))
              .add("director", record.getString("director", ""))
              .add("imdb_rating", record.getInt("imdb_rating", 0))
              .add("imdb_votes", record.getInt("imdb_votes", 0))
              .add("poster_path", record.getString("poster_path", ""))
              
              .build();
  }

  private Float filterFloatFromRecord(JsonObject record, String column)
  {
    try {

      Float filteredFloat = (float) record.getJsonNumber(column).doubleValue();
      return filteredFloat;
      
    } catch (Exception e) {
      return 0f;
    }
  }

  private Double filterDoubleFromRecord (JsonObject record, String column)
  {
    try { 
      Double filteredDouble = record.getJsonNumber(column).doubleValue();
      return filteredDouble;
    } 
    catch (Exception e) {
      return 0.0;
    }
  }

  // TODO: Task 3
  // You may change the signature of this method by passing any number of parameters
  // and returning any type
  public void getProlificDirectors() {
  }


  // TODO: Task 4
  // You may change the signature of this method by passing any number of parameters
  // and returning any type
  public void generatePDFReport() {

  }

}
