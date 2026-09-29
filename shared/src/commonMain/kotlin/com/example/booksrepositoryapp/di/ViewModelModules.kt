package com.example.booksrepositoryapp.di

import com.example.booksrepositoryapp.ui.accountDetails.AccountDetailsViewModel
import com.example.booksrepositoryapp.ui.addToCart.AddToCartViewModel
import com.example.booksrepositoryapp.ui.addressScreen.AddressListViewModel
import com.example.booksrepositoryapp.ui.auth.getStarted.GetStartedViewModel
import com.example.booksrepositoryapp.ui.auth.register.RegisterViewModel
import com.example.booksrepositoryapp.ui.bookCategory.BooksCategoryViewModel
import com.example.booksrepositoryapp.ui.bookDetails.BookDetailsViewModel
import com.example.booksrepositoryapp.ui.booksList.BooksListViewModel
import com.example.booksrepositoryapp.ui.checkout.CheckoutViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val commonViewModelModule = module {
    factoryOf(::GetStartedViewModel)
    factoryOf(::RegisterViewModel)
    factoryOf(::BooksCategoryViewModel)
    factoryOf(::BooksListViewModel)
    factoryOf(::BookDetailsViewModel)
    factoryOf(::AddToCartViewModel)
    factoryOf(::CheckoutViewModel)
    factoryOf(::AddressListViewModel)
    factoryOf(::AccountDetailsViewModel)
}