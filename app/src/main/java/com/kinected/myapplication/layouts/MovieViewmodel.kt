package com.kinected.myapplication.layouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinected.myapplication.data.MovieRepo
import com.kinected.myapplication.data.MovieResponse
import com.kinected.myapplication.data.ResultsItem
import com.kinected.myapplication.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewmodel @Inject constructor(
    private val movieRepo: MovieRepo
) : ViewModel() {

    init {
        getBookmarkedList()
    }

    private val _movieSearchResponseFlow = MutableStateFlow<NetworkResponse<MovieResponse>>(NetworkResponse.Init())
    val movieSearchResponseFlow : StateFlow<NetworkResponse<MovieResponse>> = _movieSearchResponseFlow

    private val _movieResponseFlow: MutableStateFlow<NetworkResponse<ResultsItem>> = MutableStateFlow(NetworkResponse.Init())
    val movieResponseFlow : StateFlow<NetworkResponse<ResultsItem>> = _movieResponseFlow

    private val _bookmarkedList = MutableStateFlow<List<Int>>(emptyList())
    val bookmarkedList: StateFlow<List<Int>> = _bookmarkedList

    fun callMovieSearchApi(searchQuery: String, pageNo: Int){

        viewModelScope.launch(Dispatchers.IO) {

            movieRepo.getMovieListFromApi(searchQuery, pageNo).collectLatest { response ->

                if(response is NetworkResponse.Success){
                    val moviesWithBookmark = response.data?.results?.map { movie ->
                        movie.copy(bookmark = _bookmarkedList.value.contains(movie.id))
                    } ?: emptyList()

                    val movieProcessedResponse = response.data?.copy(results = moviesWithBookmark)

                    _movieSearchResponseFlow.value = NetworkResponse.Success(movieProcessedResponse)
                }
                else {
                    _movieSearchResponseFlow.value = response
                }
            }
        }
    }

    fun getMovieDetailsFromApi(movieId: Int){

        viewModelScope.launch {
            combine(
                movieRepo.getMovieDetailsFromApi(movieId),
                _bookmarkedList
            ) { response, bookmarks ->
                if (response is NetworkResponse.Success) {
                    // Update bookmark status reactively
                    val movieDetails = response.data?.copy(
                        bookmark = bookmarks.contains(movieId)
                    )
                    NetworkResponse.Success(movieDetails)
                } else {
                    response
                }
            }.collectLatest { updatedResponse ->
                _movieResponseFlow.value = updatedResponse
            }
        }
    }

    fun insertMovies(movieItem: ResultsItem) {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.insertMovies(movieItem)
        }
    }

    fun doBookmark(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.doBookmark(id)
        }
    }

    fun getBookmarkedList() {

        viewModelScope.launch(Dispatchers.IO) {
            movieRepo.getBookmarkedList().collectLatest { bookmarks ->

                _bookmarkedList.value = bookmarks

                // Safely cast to Success and ensure data is not null
                val updatedMovies = (_movieSearchResponseFlow.value as? NetworkResponse.Success<MovieResponse>)
                    ?.data
                    ?.results
                    ?.map { movie ->
                        movie.copy(bookmark = bookmarks.contains(movie.id))
                    }.orEmpty()

                val movieProcessedResponse = MovieResponse().copy(results = updatedMovies)

                _movieSearchResponseFlow.value = NetworkResponse.Success(movieProcessedResponse)
            }
        }
    }
}