# BooksRepositoryApp — Current Folder Structure

Generated from the repository contents on October 16, 2026. Generated `.git/`, `.gradle/`, `.kotlin/`, and build output folders are omitted.

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
│           ├── ic_launcher-playstore.png
│           ├── res/
│           │   ├── drawable/                         # Android-only drawable resources
│           │   ├── layout/activity_main.xml
│           │   ├── mipmap-{anydpi-v26,hdpi,mdpi,xhdpi,xxhdpi,xxxhdpi}/
│           │   ├── values/{array,colors,ic_launcher_background,strings,style,themes}.xml
│           │   └── xml/{backup_rules,data_extraction_rules,file_path}.xml
│           └── java/com/example/booksrepositoryapp/ui/theme/
│               ├── Color.kt
│               ├── Theme.kt
│               └── Type.kt
├── shared/
│   ├── build.gradle.kts
│   └── src/
│       ├── commonMain/
│       │   ├── composeResources/
│       │   │   ├── drawable/                          # Shared Compose image resources
│       │   │   └── values{-ur,}/strings.xml           # Shared strings (English & Urdu)
│       │   └── kotlin/
│       │       ├── com/example/booksrepositoryapp/
│       │       │   ├── data/
│       │       │   │   ├── repository/BooksRepositoryImp.kt
│       │       │   │   ├── source/local/localDataSources/BooksLocalDataSource.kt
│       │       │   │   ├── source/local/uiModels/AddressUiModel.kt
│       │       │   │   ├── source/remote/firebase/authentication/{AuthRepository.kt,UserProfile.kt}
│       │       │   │   ├── source/remote/ktor/{ApiService.kt,dto,httpClient/HttpClient.kt}
│       │       │   │   └── util/refreshResult/RefreshResult.kt
│       │       │   ├── di/{NetworkModule.kt,RepositoryModule.kt,ViewModelModules.kt}
│       │       │   ├── domain/{model,repository,usecase}/*.kt
│       │       │   ├── localization/{AppLanguage.kt,LanguageManager.kt,LanguagePreferences.kt,LocalAppLocale.kt}
│       │       │   ├── manager/
│       │       │   │   ├── cameraManager/CameraManager.kt
│       │       │   │   ├── locationManager/{LocationCoordinates.kt,LocationManager.kt}
│       │       │   │   └── networkManager/NetworkManager.kt
│       │       │   ├── navigation/{AppNavigation.kt,routes/Routes.kt}
│       │       │   ├── ui/
│       │       │   │   ├── accountDetails/*
│       │       │   │   ├── addressScreen/*
│       │       │   │   ├── addToCart/*
│       │       │   │   ├── auth/{getStarted,register}/*
│       │       │   │   ├── bookCategory/*
│       │       │   │   ├── bookDetails/*
│       │       │   │   ├── booksList/*
│       │       │   │   ├── checkout/*
│       │       │   │   ├── conformationBottomSheet/ConfirmationBottomSheet.kt
│       │       │   │   ├── landingPage/{LandingPage.kt,LandingPageResources.kt}
│       │       │   │   ├── loading/LoadingScreen.kt
│       │       │   │   ├── maintenancePage/MaintenanceScreen.kt
│       │       │   │   └── successPayment/SuccessScreen.kt
│       │       │   └── util/RandomPriceAndRating.kt
│       │       └── com/example/shared/Platform.kt
│       ├── commonTest/kotlin/com/example/booksrepositoryapp/
│       │   ├── data/repository/BooksRepositoryTest.kt
│       │   └── data/source/remote/ktor/ApiServiceTest.kt
│       ├── androidMain/
│       │   ├── AndroidManifest.xml
│       │   └── kotlin/
│       │       ├── com/example/booksrepositoryapp/
│       │       │   ├── BooksRepositoryApp.kt
│       │       │   ├── data/mapper/{AddressMapper.kt,BooksMapper.kt,CartMapper.kt,UserMapper.kt}
│       │       │   ├── data/repository/{AddressRepositoryImp.kt,CartRepositoryImpl.kt,UserRepositoryImpl.kt}
│       │       │   ├── data/source/local/room/{AppDatabase.kt,DatabaseInstance.kt,dao/BooksDao.kt,entity/BookDetailsModel.kt}
│       │       │   ├── data/source/local/roomDataSource/RoomBooksDataSource.kt
│       │       │   ├── data/source/remote/firebase/authentication/FirebaseAuthRepository.kt
│       │       │   ├── data/source/remote/firebase/firestore/{AddressModelFB.kt,CartModelFB.kt}
│       │       │   ├── data/source/remote/ktor/httpClient/HttpClient.kt
│       │       │   ├── di/{AndroidNetworkModule.kt,AndroidRepositoryModule.kt,FirebaseModule.kt,RoomModule.kt}
│       │       │   ├── localization/{LanguageDataStore.kt,LocalAppLocale.kt}
│       │       │   ├── main/MainActivity.kt
│       │       │   └── manager/
│       │       │       ├── AndroidLocationManager.kt
│       │       │       ├── cameraManager/AndroidCameraManager.kt
│       │       │       └── networkManager/AndroidNetworkManager.kt
│       │       └── com/example/shared/Platform.android.kt
│       ├── androidDeviceTest/kotlin/com/example/booksrepositoryapp/
│       │   ├── data/repository/FirebaseEmulatorIntegrationTest.kt
│       │   └── data/source/local/roomDataSource/RoomBooksLocalDataSourceTest.kt
│       ├── androidHostTest/kotlin/com/example/shared/ExampleUnitTest.kt
│       ├── nativeMain/kotlin/com/example/booksrepositoryapp/data/source/remote/ktor/httpClient/HttpClient.native.kt
│       └── iosMain/kotlin/com/example/shared/Platform.ios.kt
├── gradle/
│   ├── gradle-daemon-jvm.properties
│   ├── libs.versions.toml
│   └── wrapper/gradle-wrapper.{jar,properties}
├── .gitignore
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── PROJECT_STRUCTURE.md
├── README.md
└── settings.gradle.kts
```

## Module responsibilities

| Module/source set | Responsibility |
|---|---|
| `shared/src/commonMain` | Shared domain models, repositories, use cases, Ktor networking contracts, Compose UI, navigation contracts, localization manager, Koin DI modules, location/camera manager contracts, and shared resources |
| `shared/src/androidMain` | Android implementations for Room, Firebase (Auth, Firestore, Remote Config), Ktor OkHttp client, Android location & camera managers, DataStore locale handling, and Android entry point (`MainActivity`) |
| `shared/src/nativeMain` | Native Ktor HTTP client implementation |
| `shared/src/iosMain` | iOS platform implementation |
| `shared/src/commonTest` | Tests shared across platforms |
| `shared/src/androidDeviceTest` | Instrumentation and integration tests requiring an Android device or emulator (Firebase Emulator, Room) |
| `shared/src/androidHostTest` | Unit tests executed on the JVM host |
| `app` | Android application configuration, manifest, launcher resources, legacy Android resources, and theme files |

Notation: `{A,B}.kt` represents individual files, `*` represents all files in a directory, and a directory name followed by `/*` represents its current contents.
