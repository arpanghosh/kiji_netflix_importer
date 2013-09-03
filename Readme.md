1) Download the Netflix dataset from http://www.lifecrunch.biz/archives/207.
You will get a tarball called 'nf_prize_dataset.tar.gz'. 'Untar' it to a convenient location.

2) The 'untarred' directory has another tarball 'training_set.tar'. 'Untar' this one as well.

3) Run the provided 'flatten_netflix_dataset.py' script. It takes the path of the 'untarred'
netflix dataset folder as an argument. It generates a single 1.9GB text file, by flattening the
17770 movie files in the 'untarred' 'training_set' directory, in which each line contains all
the ratings given to a unique movie. It also pulls in the movie name and release year for each
movie by looking it up from the 'movie_titles.txt' file in the 'untarred' netflix dataset
directory

4) The generated file will be called 'flat_netflix_movie_ratings.txt' and will appear in the
current working directory.

5) Each line(movie) of the file has the following format:

movie_id|movie_release_year,movie_name|user1_id,user1_rating,user1_rating_date|
user2_id,user2_rating,user2_rating_date|user3_id,user3_rating,user3_rating_date|
.........................|userN_id,userN_rating,userN_rating_date


6) Create a Kiji table called 'movies' to store this data using the included layout file (netflix_schema.ddl):
kiji-schema-shell --file="Path to netflix_schema.ddl"


7) Run 'mvn clean package'


8) Load the 'flat_netflix_movie_ratings.txt' file into HDFS:
hadoop fs -copyFromLocal <local_path> <Hadoop fs path>


9) The following command runs the included MovieBulkImporter to load 'flat_netflix_movie_ratings.txt' into
the just created 'movies' table:
kiji jar <project root dir>/target/netflix_import-1.0-SNAPSHOT.jar com.redeyetechguy.kiji.Driver <Hadoop fs path to directory containing flat_netflix_movie_ratings.txt> <Kiji URI of 'movies' table>

