package com.example.booksrepositoryapp.di

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.booksrepositoryapp.ui.accountDetails.AccountDetailsViewModel
import com.example.booksrepositoryapp.ui.addToCart.AddToCartViewModel
import com.example.booksrepositoryapp.ui.addressScreen.AddressListViewModel
import com.example.booksrepositoryapp.ui.auth.getStarted.GetStartedViewModel
import com.example.booksrepositoryapp.ui.auth.register.RegisterViewModel
import com.example.booksrepositoryapp.ui.bookCategory.BooksCategoryViewModel
import com.example.booksrepositoryapp.ui.bookDetails.BookDetailsViewModel
import com.example.booksrepositoryapp.ui.booksList.BooksListViewModel
import com.example.booksrepositoryapp.ui.checkout.CheckoutViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
val viewmodelModule = module {
    factory {
        GetStartedViewModel(
            get(),
        )
    }
    factory {
        RegisterViewModel(
            get(),
            get(),
        )
    }
    viewModel {
        BooksCategoryViewModel()
    }
    viewModel {
        BooksListViewModel(get(), get())
    }
    viewModel {
        BookDetailsViewModel(get(), get(), get(), get())
    }
    viewModel {
        AddToCartViewModel(get(), get())
    }
    viewModel {
        CheckoutViewModel(get(), get(), get())
    }
    viewModel {
        AddressListViewModel(get(), get(), get())
    }
    viewModel {
        AccountDetailsViewModel(get(), get(), get(), get())
    }
}