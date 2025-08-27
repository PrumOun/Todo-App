package com.example.todotasks.ui.floataction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MyFloatActionButton(
    clickable: Boolean = true,
    onClick: () -> Unit = {}
){
    Box(
        modifier = Modifier
            .background(
                color = Color.Black.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .size(58.dp).clip(RoundedCornerShape(12.dp))
            .clickable(clickable) {
                onClick.invoke()
            },
    ) {
        Text(
            text = "+",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}