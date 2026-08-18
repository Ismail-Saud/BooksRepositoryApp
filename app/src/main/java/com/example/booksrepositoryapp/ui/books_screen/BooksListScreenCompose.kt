package com.example.booksrepositoryapp.ui.books_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.ui.book_category.BookCategoryScreen
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

data class Book(
    val name: String,
    val author: String,
    val price: Double,
    val rating: Double,
    val category: String
)

@Composable
fun BooksListScreen (
    onBackClick: () -> Unit
) {
    var name by remember {
        mutableStateOf("name")
    }
    val books = listOf(
        Book(
            name = "1984",
            author = "George Orwell",
            price = 18.0,
            rating = 4.6,
            category = "Classic"
        ),
        Book(
            name = "1984",
            author = "George Orwell",
            price = 18.0,
            rating = 4.6,
            category = "Classic"
        ),
        Book(
            name = "1984",
            author = "George Orwell",
            price = 18.0,
            rating = 4.6,
            category = "Classic"
        ),
        Book(
            name = "1984",
            author = "George Orwell",
            price = 18.0,
            rating = 4.6,
            category = "Classic"
        ),
        Book(
            name = "1984",
            author = "George Orwell",
            price = 18.0,
            rating = 4.6,
            category = "Classic"
        ),Book(
            name = "1984",
            author = "George Orwell",
            price = 18.0,
            rating = 4.6,
            category = "Classic"
        ),
        Book(
            name = "1984",
            author = "George Orwell",
            price = 18.0,
            rating = 4.6,
            category = "Classic"
        ),
        Book(
            name = "1984",
            author = "George Orwell",
            price = 18.0,
            rating = 4.6,
            category = "Classic"
        ),
        Book(
            name = "1984",
            author = "George Orwell",
            price = 18.0,
            rating = 4.6,
            category = "Classic"
        ),
        Book(
            name = "1984",
            author = "George Orwell",
            price = 18.0,
            rating = 4.6,
            category = "Classic"
        ),
    )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it.trim()
                },
                label = {
                    Text("Name")
                },
                trailingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp, 0.dp)
            )
            IconButton(
                onClick = {}
            ) {
                Icon(
                    Icons.Default.FilterAlt,
                    contentDescription = "Filter"
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(books) { book ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
//                        .clickable {
//                            onCardClick()
//                        }
                    ,shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.book_cover_img),
                                contentDescription = book.name,
                                modifier = Modifier
                                    .height(115.dp)
                                    .width(85.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(Color(0xFF151515))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = book.category,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = book.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 2
                            )
                            Text(
                                text = book.author,
                                fontSize = 12.sp,
                                color = Color.LightGray,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            Spacer(
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "$${book.price}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BooksListScreenComposePreview() {
    BooksRepositoryAppTheme {
        BooksListScreen (
            onBackClick = {},
        )
    }
}