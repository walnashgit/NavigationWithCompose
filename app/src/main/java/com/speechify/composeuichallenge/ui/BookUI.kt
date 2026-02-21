package com.speechify.composeuichallenge.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.Image
import coil3.compose.AsyncImage
import com.speechify.composeuichallenge.data.Book

@Composable
fun Loading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}


@Composable
fun BookList(modifier: Modifier = Modifier, books: List<Book>) {
    LazyColumn {
        itemsIndexed(
            items = books,
            key = { index, book ->
                book.id
            }
        ) { index, book ->
            Row() {
                AsyncImage(
                    model = book.imageUrl,
                    contentDescription = ""
                )
                Column() {
                    Text(text = book.name)
                    Text(text = book.author)
                    Text(text = "${book.rating} (${book.reviewCount})")
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewLoading() {
    Loading()
}