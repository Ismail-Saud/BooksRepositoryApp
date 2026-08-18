package com.example.booksrepositoryapp.ui.book_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

@Composable
fun BookDetailsScreenCompose(
    onBackClick: () -> Unit,
) {
    data class Book(
        val name: String,
        val author: String,
        val price: Double,
        val rating: Double,
        val category: String,
        val description: String,
    )
    val book = Book(
        name = "1984",
        author = "George Orwell",
        price = 18.0,
        rating = 4.6,
        category = "Classic",
        description = "-Oscar Wilde’s only novel is the dreamlike story of a young man who sells his soul for eternal youth and beauty. In this celebrated work Wilde forged a devastating portrait of the effects of evil and debauchery on a young aesthete in late-19th-century England. Combining elements of the Gothic horror novel and decadent French fiction, the book centers on a striking premise: As Dorian Gray sinks into a life of crime and gross sensuality, his body retains perfect youth and vigor while his recently painted portrait grows day by day into a hideous record of evil, which he must keep hidden from the world. For over a century, this mesmerizing tale of horror and suspense has enjoyed wide popularity. It ranks as one of Wilde's most important creations and among the classic achievements of its kind."
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = book.category,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Book title
        Text(
            text = book.name,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            modifier = Modifier.padding(
                top = 20.dp,
                bottom = 20.dp
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .width(130.dp)
                    .height(190.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(
                        R.drawable.book_cover_img
                    ),
                    contentDescription = book.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Book information
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {

                Text(
                    text = "Author : ${book.author}",
                    fontSize = 18.sp,
                    color = Color(0xFF212121)
                )

                Text(
                    text = "Category : ${book.category}",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )

                Text(
                    text = "Rating : ${book.rating}/5",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )

                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pricing:",
                        fontSize = 18.sp
                    )

                    Text(
                        text = " $${book.price}",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF111111)
                    )
                ) {
                    Text(
                        text = "Add to Cart",
                        fontSize = 18.sp
                    )
                }
            }
        }
        Text(
            text = "Description:",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            modifier = Modifier.padding(top = 24.dp)
        )

        Text(
            text = book.description,
            fontSize = 18.sp,
            color = Color(0xFF424242),
            lineHeight = 22.sp,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun BookDetailsPreview() {
    BooksRepositoryAppTheme {
        BookDetailsScreenCompose (
            onBackClick = {},
        )
    }
}