package com.speechify.composeuichallenge.ui

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speechify.composeuichallenge.data.Book
import com.speechify.composeuichallenge.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(val booksRepository: BooksRepository): ViewModel() {

    private val _bookListState = MutableStateFlow(BookListState())
    val bookListState: StateFlow<BookListState> = _bookListState.asStateFlow()

    private val _bookDetailsState = MutableStateFlow(BookDetailsState())
    val bookDetailsState: StateFlow<BookDetailsState> = _bookDetailsState.asStateFlow()

    init {
        observeSearch()
    }

    fun getBooks() {
        _bookListState.update { currentBooks ->
            currentBooks.copy(loading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val books = booksRepository.getBooks()
            _bookListState.update { currentBooks ->
                currentBooks.copy(books = books, loading = false)
            }
        }
    }

    fun searchBooks(query: String) {
        _bookListState.update {
            it.copy(searchQuery = query)
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch(Dispatchers.IO) {
            _bookListState
                .map { it.searchQuery }
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) {
                        val books = booksRepository.getBooks()
                        _bookListState.update { currentBooks ->
                            currentBooks.copy(books = books)
                        }
                    } else {
                        val books = booksRepository.searchBook(query)
                        _bookListState.update {
                            it.copy(
                                books = books
                            )
                        }
                    }
                }
        }
    }

    fun getBookDetails(bookId: String) {
        _bookDetailsState.update {
            it.copy(loading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val book = booksRepository.getBook(bookId)
            _bookDetailsState.update {
                it.copy(book = book, loading = false)
            }
        }
    }
}

@Stable
data class BookListState (
    val books: List<Book> = listOf(),
    val searchQuery: String = "",
    val loading: Boolean = false
)

@Stable
data class BookDetailsState (
    val book: Book? = null,
    val loading: Boolean = true
)