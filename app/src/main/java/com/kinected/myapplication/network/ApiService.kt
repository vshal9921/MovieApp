package com.kinected.myapplication.network

import com.kinected.myapplication.data.ApiConst
import com.kinected.myapplication.data.MovieResponse
import com.kinected.myapplication.data.ResultsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService{

    @GET(ApiConst.SEARCH)
    suspend fun getMovieSearchResponse(
        @Query(ApiConst.QUERY) query: String,
        @Query(ApiConst.PAGE) page: Int
    ): Response<MovieResponse>

    @GET(ApiConst.MOVIE_DETAIL + "/{movieId}")
    suspend fun getMovieDetail(
        @Path(ApiConst.MOVIE_ID) movieId: Int
    ): Response<ResultsItem>
}


