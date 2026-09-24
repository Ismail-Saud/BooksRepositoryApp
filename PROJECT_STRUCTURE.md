# BooksRepositoryApp — Current Project Structure

This document reflects the project currently on disk. Generated `.gradle/`, `.kotlin/`, build directories, and release artifacts are omitted.

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
│           │   │   ├── mapper/{AddressMapper,BookMapper,BooksMapper,CartMapper,UserMapper}.kt
│           │   │   ├── repository/{AddressRepositoryImpl,BooksRepositoryImpl,CartRepositoryImpl,UserRepositoryImpl}.kt
│           │   │   ├── source/local/
│           │   │   │   ├── prefManager/GsonManager.kt
│           │   │   │   ├── room/{AppDatabase,DatabaseInstance}.kt
│           │   │   │   ├── room/converter/Converters.kt
│           │   │   │   ├── room/dao/{AddressDao,BooksDao,CartDao,UserDao}.kt
│           │   │   │   ├── room/entity/{AddressModel,BookDetailsModel,CartModel,UserModel}.kt
│           │   │   │   ├── sharedPref/PrefManager.kt
│           │   │   │   └── uiModels/{AddressUiModel,CartItem}.kt
│           │   │   ├── source/remote/firebase/
│           │   │   │   ├── authentication/{AuthRepository,UserProfile}.kt
│           │   │   │   └── firestore/{AddressModelFB,CartModelFB}.kt
│           │   │   ├── source/remote/ktor/
│           │   │   │   ├── ApiService.kt
│           │   │   │   └── dto/{Category.kt,bookDetailsResponse/*.kt,subjectsApiResponseModels/*.kt}
│           │   │   └── util/{RandomPriceAndRating.kt,refreshResult/RefreshResult.kt}
│           │   ├── di/{FirebaseModule,NetworkModule,RepositoryModules,RoomModule,ViewModelModule}.kt
│           │   ├── domain/
│           │   │   ├── model/{Address,Book,Cart,User}.kt
│           │   │   ├── repository/{AddressRepository,BooksRepository,CartRepository,UserRepository}.kt
│           │   │   └── usecase/{GetBooksUseCase,RefreshBooksUseCase}.kt
│           │   ├── factory/ViewModelScope.kt
│           │   ├── helper/{LocationHelper.kt,networkHelper/NetworkHelper.kt}
│           │   ├── navigation/{AppNavigation.kt,routes/Routes.kt}
│           │   └── ui/
│           │       ├── accountDetails/AccountDetails{Effect,Event,ScreenCompose,State,ViewModel}.kt
│           │       ├── addressScreen/{AddressListEffect,AddressListEvent,AddressListViewModel,AddressScreenCompose}.kt
│           │       ├── addToCart/AddToCart{Effect,Event,ScreenCompose,State,ViewModel}.kt
│           │       ├── auth/{getStarted/GetStarted*,register/Register*}.kt
│           │       ├── bookCategory/BooksCategory{Effect,Event,ScreenCompose,State,ViewModel}.kt
│           │       ├── bookDetails/BookDetails{Effect,Event,ScreenCompose,State,ViewModel}.kt
│           │       ├── booksList/{BooksList*,PriceFilterBottomSheetCompose}.kt
│           │       ├── checkout/Checkout{Effect,Event,ScreenCompose,State,ViewModel}.kt
│           │       ├── conformationBottomSheet/ConfirmationBottomSheetCompose.kt
│           │       ├── landingPage/LandingPageScreenCompose.kt
│           │       ├── loading/LoadingScreenCompose.kt
│           │       ├── main/MainActivity.kt
│           │       ├── maintenancePage/MaintenaceScreenCompose.kt
│           │       ├── successPayment/SuccessScreenCompose.kt
│           │       └── theme/{Color,Theme,Type}.kt
│           └── res/
│               ├── drawable/*
│               ├── layout/activity_main.xml
│               ├── mipmap-{anydpi-v26,hdpi,mdpi,xhdpi,xxhdpi,xxxhdpi}/*
│               ├── values/{array,colors,ic_launcher_background,strings,style,themes}.xml
│               └── xml/{backup_rules,data_extraction_rules,file_path}.xml
├── shared/
│   ├── build.gradle.kts
│   └── src/
│       ├── commonMain/kotlin/com/example/booksrepositoryapp/
│       │   ├── data/
│       │   │   ├── repository/BooksRepositoryImp.kt
│       │   │   ├── source/local/localDataSources/BooksLocalDataSource.kt
│       │   │   ├── source/remote/firebase/{authentication/AuthRepository.kt,firestore/{AddressModelFB,CartModelFB}.kt}
│       │   │   ├── source/remote/ktor/{ApiService.kt,httpClient/HttpClient.kt,dto/*}
│       │   │   └── util/refreshResult/RefreshResult.kt
│       │   ├── di/{NetworkModule,RepositoryModule}.kt
│       │   ├── domain/{model/*.kt,repository/*.kt,usecase/*.kt}
│       │   ├── helper/networkHelper/NetworkHelper.kt
│       │   ├── ui/auth/{getStarted/*.kt,register/*.kt}
│       │   └── util/RandomPriceAndRating.kt
│       ├── androidMain/
│       │   ├── AndroidManifest.xml
│       │   └── kotlin/com/example/booksrepositoryapp/
│       │       ├── data/mapper/{AddressMapper,BooksMapper,CartMapper}.kt
│       │       ├── data/source/local/room/{AppDatabase,DatabaseInstance}.kt
│       │       ├── data/source/local/room/{converter/Converters.kt,dao/BooksDao.kt,entity/BookDetailsModel.kt}
│       │       ├── data/source/local/roomDataSource/RoomBooksDataSource.kt
│       │       ├── data/source/remote/firebase/authentication/FirebaseAuthRepository.kt
│       │       ├── data/source/remote/ktor/httpClient/HttpClient.kt
│       │       ├── di/{NetworkModule,RepositoryModule,RoomModule}.kt
│       │       └── helper/networkHelper/AndroidNetworkHelper.kt
│       ├── nativeMain/kotlin/com/example/booksrepositoryapp/data/source/remote/ktor/httpClient/HttpClient.native.kt
│       ├── iosMain/kotlin/com/example/shared/Platform.ios.kt
│       ├── androidDeviceTest/kotlin/com/example/shared/ExampleInstrumentedTest.kt
│       └── androidHostTest/kotlin/com/example/shared/ExampleUnitTest.kt
├── gradle/{libs.versions.toml,gradle-daemon-jvm.properties,wrapper/gradle-wrapper.properties}
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
├── README.md
└── settings.gradle.kts
```

## Migration Targets

| Current location | Target/common placement |
|---|---|
| `app/.../domain`, `data/mapper`, `data/repository` | `shared/src/commonMain/...` |
| `app/.../data/source/remote/ktor` | `shared/src/commonMain/.../data/source/remote/ktor` |
| `app/.../ui` | `shared/src/commonMain/.../ui` or `presentation` |
| `app/.../navigation` | `shared/src/commonMain/.../navigation` |
| `app/.../di` | `shared/src/commonMain`, with platform modules in `androidMain`/`nativeMain` |
| Room database and DAOs | Shared interfaces/models; Android implementation in `shared/androidMain` |
| Shared preferences | Platform-specific Android/iOS source sets |
| Firebase | Shared contracts; Android/iOS implementations |
| `app/.../ui/main/MainActivity.kt` | Remains Android-specific in `app`; hosts shared Compose UI |

Brace notation is shorthand for individual files. For example, `{Address,Book}.kt` means `Address.kt` and `Book.kt`.
