package com.haseeb.themoviedb.data.models

data class MovieResponse(val results:ArrayList<MovieObject?>,
                        val page:Int,
                        val total_results:Int,
                        val total_pages:Int)