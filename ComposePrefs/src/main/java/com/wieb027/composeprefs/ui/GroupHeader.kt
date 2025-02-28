package com.wieb027.composeprefs.ui

import androidx.annotation.Size
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun GroupHeader(
    title: String,
    color: Color = MaterialTheme.colors.primary,
    fontSize: TextUnit,
    modifier: Modifier,
    contentAlignment: Alignment
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        Text(
            text = title,
            color = color,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold
        )
    }
}