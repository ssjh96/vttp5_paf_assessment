package vttp.batch5.paf.movies.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MainController 
{

  @GetMapping(path = "/summary")
  public String getMethodName(@RequestParam String param) {
      return new String();
  }
  
  // TODO: Task 3
   

  
  // TODO: Task 4


}
