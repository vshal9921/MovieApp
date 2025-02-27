package com.kinected.myapplication.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kinected.myapplication.ui.theme.ShadowBackground

@Composable
fun CommonLoader(modifier: Modifier = Modifier){

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ShadowBackground),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(50.dp))
    }

}