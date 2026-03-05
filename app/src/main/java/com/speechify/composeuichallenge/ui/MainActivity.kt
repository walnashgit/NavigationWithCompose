package com.speechify.composeuichallenge.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.speechify.composeuichallenge.ui.theme.ComposeUIChallengeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeUIChallengeTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = BookList) {
                    composable<BookList> {
                        BookScreen(onDetailsClicked = { bookId ->
                            navController.navigate(BookDetails(bookId = bookId))
                        })
                    }
                    composable<BookDetails> { backStackEntry ->
                        val bookDetails: BookDetails = backStackEntry.toRoute()
                        BookDetailScreen(bookDetails.bookId)
                    }
                }
            }
        }
    }
}

@Serializable
object BookList

@Serializable
data class BookDetails(val bookId: String)