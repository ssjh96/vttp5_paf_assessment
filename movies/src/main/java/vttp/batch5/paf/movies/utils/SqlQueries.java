package vttp.batch5.paf.movies.utils;

public class SqlQueries 
{
    // check count in imdb
    public static final String SQL_CHECK_COUNT = "select count(*) as count from imdb;";    

    // Insert into imdb
    public static final String SQL_INSERT_RECORD = """
            insert into imdb 
                (imdb_id, vote_average, vote_count, release_date, revenue, budget, runtime)
            values
                (?, ?, ?, ?, ?, ?, ?)
            """;

    
}
