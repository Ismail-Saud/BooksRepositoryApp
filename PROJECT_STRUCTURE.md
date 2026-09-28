# BooksRepositoryApp — Current Folder Structure

Generated from the repository contents on September 28, 2026. Generated `.git/`, `.gradle/`, `.kotlin/`, build folders, and `app/release/` are omitted.

```text
BooksRepositoryApp/
├── app/
│   ├── build.gradle.kts
│   ├── google-services.json
│   ├── proguard-rules.pro
│   └── src/
│       ├── androidTest/java/com/example/booksrepositoryapp/ExampleInstrumentedTest.kt
│       ├── test/java/com/example/booksrepositoryapp/ExampleUnitTest.kt
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/com/example/booksrepositoryapp/
│           │   ├── BooksRepositoryApp.kt
│           │   ├── data/
│           │   │   ├── mapper/{AddressMapper,BookMapper,CartMapper,UserMapper}.kt
│           │   │   ├── repository/{AddressRepositoryImpl,BooksRepositoryImpl,CartRepositoryImpl,UserRepositoryImpl}.kt
│           │   │   ├── source/local/
│           │   │   │   ├── room/
│           │   │   │   │   ├── AppDatabase.kt
│           │   │   │   │   ├── DatabaseInstance.kt
│           │   │   │   │   ├── dao/BooksDao.kt
│           │   │   │   │   └── entity/BookDetailsModel.kt
│           │   │   │   └── uiModels/AddressUiModel.kt
│           │   │   ├── source/remote/
│           │   │   │   ├── firebase/authentication/{AuthRepository,UserProfile}.kt
│           │   │   │   ├── firebase/firestore/{AddressModelFB,CartModelFB}.kt
│           │   │   │   └── ktor/
│           │   │   │       ├── ApiService.kt
│           │   │   │       └── dto/{Category.kt,bookDetailsResponse/*.kt,subjectsApiResponseModels/*.kt}
│           │   │   └── util/{RandomPriceAndRating.kt,refreshResult/RefreshResult.kt}
│           │   ├── di/{FirebaseModule,NetworkModule,RepositoryModules,RoomModule,ViewModelModule}.kt
│           │   ├── domain/{model/*.kt,repository/*.kt,usecase/*.kt}
│           │   ├── helper/{LocationHelper.kt,networkHelper/NetworkHelper.kt}
│           │   ├── navigation/{AppNavigation.kt,routes/Routes.kt}
│           │   └── ui/
│           │       ├── accountDetails/*
│           │       ├── addressScreen/*
│           │       ├── addToCart/*
│           │       ├── auth/{getStarted/*,register/*}
│           │       ├── bookCategory/*
│           │       ├── bookDetails/*
│           │       ├── booksList/*
│           │       ├── checkout/*
│           │       ├── conformationBottomSheet/*
│           │       ├── landingPage/LandingPageScreenCompose.kt
│           │       ├── loading/LoadingScreenCompose.kt
│           │       ├── main/MainActivity.kt
│           │       ├── maintenancePage/MaintenaceScreenCompose.kt
│           │       ├── successPayment/SuccessScreenCompose.kt
│           │       └── theme/{Color,Theme,Type}.kt
│           └── res/{drawable,layout,mipmap-*,values,xml}/*
├── shared/
│   ├── build.gradle.kts
│   └── src/
│       ├── commonMain/
│       │   ├── kotlin/com/example/booksrepositoryapp/
│       │   │   ├── data/
│       │   │   │   ├── repository/BooksRepositoryImp.kt
│       │   │   │   ├── source/local/localDataSources/BooksLocalDataSource.kt
│       │   │   │   ├── source/remote/firebase/authentication/AuthRepository.kt
│       │   │   │   ├── source/remote/firebase/firestore/{AddressModelFB,CartModelFB}.kt
│       │   │   │   ├── source/remote/ktor/{ApiService.kt,httpClient/HttpClient.kt,dto/*}
│       │   │   │   └── util/refreshResult/RefreshResult.kt
│       │   │   ├── di/{NetworkModule,RepositoryModule}.kt
│       │   │   ├── domain/{model/*.kt,repository/*.kt,usecase/*.kt}
│       │   │   ├── helper/networkHelper/NetworkHelper.kt
│       │   │   ├── ui/auth/{getStarted/*.kt,register/*.kt}
│       │   │   └── util/RandomPriceAndRating.kt
│       │   ├── resources/drawable/*
│       │   └── kotlin/com/example/shared/Platform.kt
│       ├── androidMain/
│       │   ├── AndroidManifest.xml
│       │   └── kotlin/com/example/
│       │       ├── booksrepositoryapp/data/mapper/{AddressMapper,BooksMapper,CartMapper}.kt
│       │       ├── booksrepositoryapp/data/source/local/room/*
│       │       ├── booksrepositoryapp/data/source/local/roomDataSource/RoomBooksDataSource.kt
│       │       ├── booksrepositoryapp/data/source/remote/firebase/{authentication/FirebaseAuthRepository.kt,firestore/{AddressModelFB,CartModelFB}.kt}
│       │       ├── booksrepositoryapp/data/source/remote/ktor/httpClient/HttpClient.kt
│       │       ├── booksrepositoryapp/di/{FirebaseModule,NetworkModule,RepositoryModule,RoomModule}.kt
│       │       ├── booksrepositoryapp/helper/networkHelper/AndroidNetworkHelper.kt
│       │       └── shared/Platform.android.kt
│       ├── nativeMain/kotlin/com/example/booksrepositoryapp/data/source/remote/ktor/httpClient/HttpClient.native.kt
│       ├── iosMain/kotlin/com/example/shared/Platform.ios.kt
│       ├── androidDeviceTest/kotlin/com/example/shared/ExampleInstrumentedTest.kt
│       └── androidHostTest/kotlin/com/example/shared/ExampleUnitTest.kt
├── gradle/{libs.versions.toml,gradle-daemon-jvm.properties,wrapper/gradle-wrapper.properties}
├── .gitignore
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
├── README.md
└── settings.gradle.kts
```

## Migration Summary

| Current code | Shared/platform destination |
|---|---|
| Domain models, repositories, use cases | `shared/src/commonMain` |
| Ktor API, DTOs, mappers, and shared repository logic | `shared/src/commonMain` |
| Compose UI and shared navigation | `shared/src/commonMain` |
| Room models/DAOs and Android database implementation | `shared/src/androidMain` |
| Firebase implementations | Shared contracts plus Android/iOS implementations |
| Android preferences and Android-only helpers | `shared/src/androidMain` |
| Native HTTP client | `shared/src/nativeMain` |
| `MainActivity.kt` | Remains Android-specific in `app` |

Notation: `{A,B}.kt` means the individual files `A.kt` and `B.kt`; `*` means all files currently inside that directory.
