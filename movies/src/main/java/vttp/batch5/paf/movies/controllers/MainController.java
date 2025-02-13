package vttp.batch5.paf.movies.controllers;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.json.JsonArray;
import vttp.batch5.paf.movies.services.MovieService;


@Controller
public class MainController 
{

  @Autowired
  private MovieService movieService;
  @GetMapping(path = "/summary")
  public ResponseEntity<String> getProlificDirectors(@RequestParam int count) {
      JsonArray result = movieService.getProlificDirectors();

      return ResponseEntity.ok(result.toString());
  }
  
  // TODO: Task 3
   

  
  // TODO: Task 4


}
