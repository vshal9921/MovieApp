package com.kinected.myapplication.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.kinected.myapplication.R
import com.kinected.myapplication.data.ApiConst
import com.kinected.myapplication.data.ResultsItem
import com.kinected.myapplication.ui.theme.DarkBackground
import com.kinected.myapplication.ui.theme.ShadowBackground
import com.kinected.myapplication.ui.theme.WhiteDesc
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MovieItem(movieItem: ResultsItem, modifier: Modifier, onClickBookMark: ()-> Unit) {

    Column (
        modifier = modifier.fillMaxWidth()
            .height(360.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = DarkBackground)
    ){

        Box(modifier = Modifier.fillMaxWidth().height(300.dp)){
            Image(
                painter = rememberAsyncImagePainter( ApiConst.IMAGE_BASE_URL + movieItem.posterPath),
                contentDescription = "image",
                modifier = Modifier.fillMaxWidth().height(300.dp),
                contentScale = ContentScale.Crop
            )

            Image(
                painter = rememberAsyncImagePainter(if(movieItem.bookmark) R.drawable.ic_bookmark else R.drawable.ic_unbookmark),
                contentDescription = "image",
                modifier = Modifier
                    .padding(8.dp)
                    .background(color = ShadowBackground, shape = CircleShape)
                    .padding(4.dp)
                    .size(24.dp)
                    .align(Alignment.BottomEnd)
                    .clickable { onClickBookMark() }
            )
        }

        Row (
            modifier = Modifier.weight(1f).padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){

            Text(
                text = movieItem.title ?: "",
                maxLines = 2,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.WhiteDesc
            )

            Spacer(modifier = Modifier.width(8.dp))

            if(! movieItem.releaseDate.isNullOrEmpty()){
                Text(
                    text = movieItem.releaseDate.toYear(),
                    maxLines = 1,
                    modifier = Modifier
                        .border(width = 1.dp, color = Color.Green, shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    style = TextStyle(color = Color.Green, fontSize = 16.sp)
                )
            }
        }
    }
}

fun String.toYear(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = formatter.parse(this)
    val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    return yearFormat.format(date!!)
}

@Preview(showBackground = true)
@Composable
fun PreviewMovieItem() {
    //MovieItem(imageUrl = "", name = "India", releaseDate = "2023-12-23", Modifier, {})
}