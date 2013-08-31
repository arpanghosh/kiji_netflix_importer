#!/usr/bin/python

import sys
import os

movie_dir_path = sys.argv[1] + "/training_set/"
movie_info_path = sys.argv[1] + "/movie_titles.txt"

movie_info_dict = {}

movie_info_file = open(movie_info_path, 'r')
movie_info_arr = movie_info_file.readlines()
for movie_info in movie_info_arr:
  movie_info_arr = movie_info.split(',')
  movie_info_dict[movie_info_arr[0]] = movie_info_arr[1] + "," + movie_info_arr[2].rstrip('\n')
movie_info_file.close()

movie_file_names = os.listdir(movie_dir_path)
new_movie_file = open("./flat_netflix_movie_ratings.txt", 'w')
for movie_file_name in movie_file_names:
  movie_file = open(movie_dir_path + movie_file_name, 'r')

  movie_id = movie_file.readline().rstrip('\n').rstrip(':')
  print "processing Movie #" + movie_id
  user_ratings = movie_file.readlines()
  new_movie_file.write(movie_id + "|" + movie_info_dict[movie_id] + '|' +'|'.join(user_ratings).replace('\n','') + '\n')
  
  movie_file.close()

new_movie_file.close() 
