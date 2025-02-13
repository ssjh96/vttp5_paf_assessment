package vttp.batch5.paf.movies.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.movies.utils.SqlQueries;

@Repository
public class MySQLMovieRepository 
{
  @Autowired 
  private JdbcTemplate jdbcTemplate;

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
  public void batchInsertMovies() {
   
  }
  
  // TODO: Task 3


}
