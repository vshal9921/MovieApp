package com.kinected.myapplication.data

import android.util.Log
import com.kinected.myapplication.common.getErrorMessage
import com.kinected.myapplication.network.ApiService
import com.kinected.myapplication.network.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class MovieRepo @Inject constructor(
    private val apiService: ApiService,
    private val movieDao: MovieDao
){

    suspend fun getMovieListFromApi(searchQuery: String, pageNo: Int) = safeNetworkCall {
        apiService.getMovieSearchResponse(searchQuery, pageNo)
    }

    suspend fun getMovieDetailsFromApi(movieId: Int) = safeNetworkCall {
        apiService.getMovieDetail(movieId)
    }

    suspend fun insertMovies(movieItem: ResultsItem) {
        movieDao.insertMovies(movieItem)
    }

    suspend fun getAllFavMovies(): List<ResultsItem> {
        return movieDao.getAllFavMovies()
    }

    fun getBookmarkedList(): Flow<List<Int>> {
        return movieDao.getBookmarkedList()
    }

    suspend fun doBookmark(id: Int) {
        return movieDao.doBookmark(id)
    }


    suspend fun <T: Any?> safeNetworkCall(
        apiCall: suspend () -> Response<T>
    ): Flow<NetworkResponse<T>> = flow {

        emit(NetworkResponse.Loading())

        val response = apiCall()

        if(response.isSuccessful){
            val data = response.body()
            emit(NetworkResponse.Success(data))
        }
        else {
            emit(NetworkResponse.Error(response.errorBody()?.getErrorMessage()))
            Log.d(" API Error", "API Error - ${response.raw()} ")
        }
    }.flowOn(Dispatchers.IO)

}

data class ErrorResponse(
    val status: Boolean,
    val message: String
)