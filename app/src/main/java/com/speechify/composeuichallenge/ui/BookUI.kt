package com.speechify.composeuichallenge.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.speechify.composeuichallenge.data.Book

@Composable
fun BookScreen(onDetailsClicked: (String) -> Unit, viewModel: BookViewModel = hiltViewModel()) {
    val bookListState by viewModel.bookListState.collectAsStateWithLifecycle()
    if (bookListState.loading) {
        Loading()
    } else {
        BookListScreen(
            bookListState,
            onDetailsClicked,
            viewModel::searchBooks
        )
    }
    LaunchedEffect(Unit) {
        if (bookListState.books.isEmpty())
            viewModel.getBooks()
    }
}

@Composable
fun Loading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(32.dp),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun BookListScreen(
    bookListState: BookListState,
    onDetailsClick: (String) -> Unit,
    onSearchTextEntered: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        // Search Bar
        OutlinedTextField(
            value = bookListState.searchQuery,
            onValueChange = {
                onSearchTextEntered(it)
            },
            placeholder = { Text("Search for books...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(
                items = bookListState.books,
                key = { book ->
                    book.id
                }
            ) { book ->
                BookItem(book, onDetailsClick)
            }
        }
    }
}

@Composable
fun BookItem(
    book: Book,
    onDetailsClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = book.imageUrl,
            contentDescription = book.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(3f / 4f)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = book.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = book.author,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${book.rating} (${book.reviewCount})",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            onClick = { onDetailsClick(book.id) },
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text("Details")
        }
    }
}

@Composable
fun BookDetailScreen(bookId: String, viewModel: BookViewModel = hiltViewModel()) {
    val bookDetailsState by viewModel.bookDetailsState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getBookDetails(bookId)
    }
    if (bookDetailsState.loading) {
        Loading()
    } else {
        bookDetailsState.book?.let {
            BookDetail(it)
        }
    }
}

@Composable
fun BookDetail(
    book: Book,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(32.dp))

        AsyncImage(
            model = book.imageUrl,
            contentDescription = book.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = book.name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = book.author,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = book.description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


@Preview
@Composable
fun PreviewLoading() {
    Loading()
}

@Preview
@Composable
fun PreviewBookList() {
    BookListScreen(
        bookListState = BookListState(
            books = listOf(
                Book(
                    id = "1",
                    name = "Book 1",
                    author = "Author 1",
                    imageUrl = "",
                    description = "Description 1",
                    rating = 3.5f,
                    reviewCount = 123
                ),
                Book(
                    id = "2",
                    name = "Book 2",
                    author = "Author 2",
                    imageUrl = "",
                    description = "Description 2",
                    rating = 4.5f,
                    reviewCount = 223
                )
            )
        ),
        onDetailsClick = {},
        onSearchTextEntered = {}
    )
}