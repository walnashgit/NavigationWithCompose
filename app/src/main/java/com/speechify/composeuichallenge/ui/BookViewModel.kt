package com.speechify.composeuichallenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speechify.composeuichallenge.data.Book
import com.speechify.composeuichallenge.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(val booksRepository: BooksRepository): ViewModel() {

    private val _bookState = MutableStateFlow(BookScreenState())
    val bookState: StateFlow<BookScreenState> = _bookState.asStateFlow()

    fun getBooks() {
        _bookState.update { currentBooks ->
            currentBooks.copy(loading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val books = booksRepository.getBooks()
            _bookState.update { currentBooks ->
                currentBooks.copy(books = books, loading = false)
            }
        }
    }

    fun searchBooks(searchText: String) {
        // Need to debounce
        viewModelScope.launch(Dispatchers.IO) {
            val books = booksRepository.searchBook(searchText)
            _bookState.update { currentBooks ->
                currentBooks.copy(books = books, loading = false)
            }
        }
    }

    fun getBook(bookId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val book = booksRepository.getBook(bookId)
            _bookState.update { currentBooks ->
                currentBooks.copy(book = book, loading = false)
            }
        }
    }
}


data class BookScreenState (
    val books: List<Book> = listOf(),
    val book: Book? = null,
    val loading: Boolean = false
)