package com.example.booksrepositoryapp.localization

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

private const val LANGUAGE_DATA_STORE_FILE = "language.preferences_pb"

fun createLanguageDataStore(
    context: Context
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            context.applicationContext.filesDir
                .resolve(LANGUAGE_DATA_STORE_FILE)
                .absolutePath
                .toPath()
        }
    )
}