CREATE TABLE movies WITH DESCRIPTION 'netflix movies'
ROW KEY FORMAT HASH PREFIXED(4)
WITH LOCALITY GROUP default
  WITH DESCRIPTION 'Main locality group' (
  MAXVERSIONS = 1,
  TTL = FOREVER,
  INMEMORY = false,
  COMPRESSED WITH NONE,
  FAMILY info WITH DESCRIPTION 'movie info' (
    movie_id "string" WITH DESCRIPTION 'unique id of movie',
    movie_name "string" WITH DESCRIPTION 'name of movie',
    movie_release_year "int" WITH DESCRIPTION 'year in which movie released'
  ),
  MAP TYPE FAMILY rating CLASS com.redeyetechguy.kiji.Rating WITH DESCRIPTION 'movie ratings given by customer'
);