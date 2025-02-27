package com.kinected.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.kinected.myapplication.data.ApiConst
import com.kinected.myapplication.data.ResultsItem
import com.kinected.myapplication.layouts.CommonLoader
import com.kinected.myapplication.layouts.MovieViewmodel
import com.kinected.myapplication.layouts.toYear
import com.kinected.myapplication.network.NetworkResponse
import com.kinected.myapplication.ui.theme.DarkBackground
import com.kinected.myapplication.ui.theme.MyApplicationTheme
import com.kinected.myapplication.ui.theme.ShadowBackground
import com.kinected.myapplication.ui.theme.WhiteDesc
import com.kinected.myapplication.ui.theme.WhiteHeading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movieViewmodel: MovieViewmodel by viewModels()
        val movieId = intent.getIntExtra("movie_id", 0)

        movieViewmodel.getMovieDetailsFromApi(movieId)

        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                ) { innerPadding ->
                    MovieDetailLayout(modifier = Modifier.padding(innerPadding), movieViewmodel)
                }
            }
        }
    }
}

@Composable
fun MovieDetailLayout(modifier: Modifier, movieViewmodel: MovieViewmodel){

    val context = LocalContext.current
    val TAG = "MovieApp"

    var movieDetail by remember { mutableStateOf(ResultsItem()) }
    var isLoading by remember { mutableStateOf(false) }

    val movieDetailState = movieViewmodel.movieResponseFlow.collectAsState().value

    when(movieDetailState){

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
                Toast.makeText(context, movieDetailState.message, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "$TAG - Error")
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()){

        Column (modifier = modifier.fillMaxSize()
            .background(Color.Black)){

            Column (
                modifier = Modifier.fillMaxWidth()
                    .height(360.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = DarkBackground)
            ){

                Box(modifier = Modifier.fillMaxWidth().height(300.dp)){
                    Image(
                        painter = rememberAsyncImagePainter( ApiConst.IMAGE_BASE_URL + movieDetailState.data?.posterPath),
                        contentDescription = "image",
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        contentScale = ContentScale.Crop
                    )

                    Image(
                        painter = rememberAsyncImagePainter(if(movieDetailState.data?.bookmark == true) R.drawable.ic_bookmark else R.drawable.ic_unbookmark),
                        contentDescription = "image",
                        modifier = Modifier
                            .padding(8.dp)
                            .background(color = ShadowBackground, shape = CircleShape)
                            .padding(4.dp)
                            .size(24.dp)
                            .align(Alignment.BottomEnd)
                            .clickable { movieDetailState.data?.id?.let {
                                movieViewmodel.doBookmark(
                                    it
                                )
                            } }
                    )
                }

                Row (
                    modifier = Modifier.weight(1f).padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Text(
                        text = movieDetailState.data?.title ?: "",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.WhiteHeading
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    if(movieDetailState.data?.releaseDate != null && movieDetailState.data.releaseDate.isNotEmpty()){
                        Text(
                            text = movieDetailState.data.releaseDate.toYear(),
                            maxLines = 1,
                            modifier = Modifier
                                .border(width = 1.dp, color = Color.Green, shape = RoundedCornerShape(20.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            style = TextStyle(color = Color.Green, fontSize = 16.sp)
                        )
                    }
                }
            }

            Column (
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){

                Text(
                    text = stringResource(R.string.rating),
                    style = MaterialTheme.typography.WhiteHeading
                )

                Text(
                    text = "${movieDetailState.data?.voteAverage}",
                    style = MaterialTheme.typography.WhiteHeading
                )

                Text(
                    text = stringResource(R.string.overview),
                    style = MaterialTheme.typography.WhiteHeading
                )

                Text(
                    text = "${movieDetailState.data?.overview}",
                    style = MaterialTheme.typography.WhiteDesc
                )
            }
        }

        if(isLoading){
            CommonLoader(modifier = Modifier.fillMaxSize())
        }
    }
}