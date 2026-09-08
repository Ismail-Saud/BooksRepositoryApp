package com.example.booksrepositoryapp.data.mapper

import com.example.booksrepositoryapp.data.source.remote.firebase.firestore.CartModelFB
import com.example.booksrepositoryapp.domain.model.Cart

fun CartModelFB.toDomain(): Cart {
    return Cart(
        bookId = workId,
        title = title,
        author = author,
        price = price,
        coverId = coverId,
        category = category,
        quantity = quantity
    )
}

fun Cart.toFirestore(): CartModelFB {
    return CartModelFB(
        workId = bookId,
        title = title,
        author = author,
        price = price,
        coverId = coverId,
        category = category,
        quantity = quantity
    )
}
