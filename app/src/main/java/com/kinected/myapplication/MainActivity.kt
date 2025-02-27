package com.kinected.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kinected.myapplication.data.ResultsItem
import com.kinected.myapplication.layouts.CommonLoader
import com.kinected.myapplication.layouts.MovieItem
import com.kinected.myapplication.layouts.MovieViewmodel
import com.kinected.myapplication.network.NetworkResponse
import com.kinected.myapplication.ui.theme.MyApplicationTheme
import com.kinected.myapplication.ui.theme.WhiteHeading
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movieViewmodel: MovieViewmodel by viewModels()

        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    MainLayout(
                        modifier = Modifier
                            .background(Color.Black)
                            .padding(innerPadding),
                        movieViewmodel = movieViewmodel
                    )
                }
            }
        }
    }
}

@Composable
fun MainLayout(modifier: Modifier, movieViewmodel: MovieViewmodel){

    val context = LocalContext.current
    val TAG = "MovieApp"
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val movieListState = movieViewmodel.movieSearchResponseFlow.collectAsState().value

    LaunchedEffect(key1 = movieListState.data?.results?.size) {

        movieListState.data?.results?.forEach {
            movieViewmodel.insertMovies(it)
        }
    }

    when(movieListState){

        is NetworkResponse.Init -> {}

        is NetworkResponse.Loading -> {
            isLoading = true
        }

        is NetworkResponse.Success -> {

            LaunchedEffect(key1 = Unit) {
                isLoading = false
            }
        }

        is NetworkResponse.Error -> {
            LaunchedEffect(key1 = Unit) {
                Toast.makeText(context, movieListState.message, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "$TAG - Error")
            }
        }
    }

    LaunchedEffect(searchQuery) {
        delay(1000L)
        if (searchQuery.isNotEmpty()) {
            movieViewmodel.callMovieSearchApi(searchQuery, 1)
        }
    }

    Box(modifier = Modifier.fillMaxSize()){

        Column(modifier = modifier.fillMaxSize()) {

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Search...") },
                singleLine = true
            )

            if(movieListState.data != null && movieListState.data.results.isNotEmpty()){
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier.fillMaxWidth()
                ) {
                    items(movieListState.data.results){ movieItem ->

                        MovieItem(
                            movieItem = movieItem,
                            modifier = Modifier.clickable {

                                if(movieItem.id == null){
                                    Toast.makeText(context, R.string.error_movie_id, Toast.LENGTH_SHORT).show()
                                }
                                else {
                                    val intent = Intent(context, MovieDetailActivity::class.java)
                                    intent.putExtra("movie_id", movieItem.id)
                                    context.startActivity(intent)
                                }
                            },
                            onClickBookMark = {
                                Log.d(TAG, "$TAG - id =  ${movieItem.id}")
                                movieItem.id?.let { movieViewmodel.doBookmark(it) }
                            }
                        )
                    }
                }
            }

            else if(movieListState.data?.results.isNullOrEmpty() && searchQuery.isNotEmpty() && ! isLoading && movieListState !is NetworkResponse.Init){
                Text(
                    text = stringResource(R.string.error_no_result),
                    style = MaterialTheme.typography.WhiteHeading.copy(textAlign = TextAlign.Center),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        if(isLoading){
            CommonLoader(modifier = Modifier.fillMaxSize())
        }
    }
}