package vttp.batch5.paf.movies.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import vttp.batch5.paf.movies.services.MovieService;

@Component
public class Dataloader implements CommandLineRunner {
  // TODO: Task 2

  @Autowired
  private MovieService movieService;

  @Override
  public void run(String... args) throws Exception 
  {
    // Task 2.1 - Check if data loaded into my sql & mongo
    System.out.println(">>> Checking data loaded into my sql & mongo..");    

    if (!movieService.checkDataLoaded()) // if either SQL or Mongo not loaded, load data
    {
      System.out.println(">>> Data not loaded.. Loading data from zip file...");
      // load data
      movieService.loadDataFromZip();
    }
  }

}
