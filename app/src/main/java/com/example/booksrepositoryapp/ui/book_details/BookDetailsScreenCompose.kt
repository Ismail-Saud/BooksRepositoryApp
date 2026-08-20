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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.booksrepositoryapp.R
import com.example.booksrepositoryapp.data.local.room.entity.BookDetailsModel
import com.example.booksrepositoryapp.ui.theme.BooksRepositoryAppTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookDetailsScreenCompose(
    book: BookDetailsModel,
    onBackClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
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
        Text(
            text = book.title,
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
                if (book.coverId != 0) {
                    GlideImage(
                        model = "https://covers.openlibrary.org/b/id/${book.coverId}-L.jpg",
                        contentDescription = book.title,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.book_cover_img),
                        contentDescription = book.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
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
            text = book.description ?: "",
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
            book = BookDetailsModel(
                workId = "OL123456W",
                title = "The Great Gatsby",
                author = "F. Scott Fitzgerald",
                price = 25.99,
                rating = 4.5,
                category = "Fiction",
                description = "A classic novel about wealth, love, and the American dream.",
                coverId = 123456
            ),
            onBackClick = {},
            onAddToCartClick = {}
        )
    }
}