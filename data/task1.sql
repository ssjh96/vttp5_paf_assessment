-- Write your Task 1 answers in this file

-- drop the database if exists
drop database if exists movies;

-- create the database
-- create database if not exists movies; -- already have drop database if exists
create database movies;

-- select the database
use movies;

-- create one or more tables
-- select "Creating RSVP table..." as msg; // cmdline
SELECT "CREATING IMDB...";
create table imdb (
    imdb_id varchar(16), -- this is the PK
    vote_average float default 0,
    vote_count int default 0,
    release_date date,
    revenue decimal(15,2) default 1000000,
    budget decimal(15,2) default 1000000,
    runtime int default 90,

    constraint pk_imdb_id primary key(imdb_id)
);

-- Grant fred access to the DB
SELECT "GRANTING ALL PRIVILEGES TO FRED..";
GRANT ALL PRIVILEGES ON movies.* TO 'fred'@'%';

-- Apply changes to privileges
FLUSH PRIVILEGES;





