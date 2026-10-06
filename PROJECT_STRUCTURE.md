# BooksRepositoryApp — Current Folder Structure

Generated from the repository contents on October 6, 2026. Generated `.git/`, `.gradle/`, `.kotlin/`, and build output folders are omitted.

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
│           │   ├── values/{array,colors,strings,style,themes}.xml
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
│       │   │   └── values/strings.xml
│       │   └── kotlin/
│       │       ├── com/example/booksrepositoryapp/
│       │       │   ├── data/
│       │       │   │   ├── repository/BooksRepositoryImp.kt
│       │       │   │   ├── source/local/{localDataSources,uiModels}/*
│       │       │   │   ├── source/remote/firebase/{authentication,firestore}/*
│       │       │   │   ├── source/remote/ktor/{ApiService.kt,dto,httpClient}/*
│       │       │   │   └── util/refreshResult/RefreshResult.kt
│       │       │   ├── di/{NetworkModule,RepositoryModule,ViewModelModules}.kt
│       │       │   ├── domain/{model,repository,usecase}/*.kt
│       │       │   ├── manager/{cameraManager,locationManager,networkManager}/*
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
│       │       │   │   ├── landingPage/{LandingPage,LandingPageResources}.kt
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
│       │       │   ├── data/{mapper,repository}/*.kt
│       │       │   ├── data/source/local/{room,roomDataSource}/*
│       │       │   ├── data/source/remote/firebase/{authentication,firestore}/*
│       │       │   ├── data/source/remote/ktor/httpClient/HttpClient.kt
│       │       │   ├── di/{AndroidNetworkModule,AndroidRepositoryModule,FirebaseModule,RoomModule}.kt
│       │       │   ├── main/MainActivity.kt
│       │       │   ├── manager/{AndroidLocationManager,cameraManager,networkManager}/*
│       │       │   └── navigation/{AppNavigation.kt,routes/Routes.kt}
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
├── local.properties
├── PROJECT_STRUCTURE.md
├── README.md
└── settings.gradle.kts
```

## Module responsibilities

| Module/source set | Responsibility |
|---|---|
| `shared/src/commonMain` | Shared domain models, repositories, use cases, networking contracts, Compose UI, navigation contracts, managers, and shared resources |
| `shared/src/androidMain` | Android implementations for Room, Firebase, Ktor, dependency injection, managers, and the Android entry point |
| `shared/src/nativeMain` | Native Ktor HTTP client implementation |
| `shared/src/iosMain` | iOS platform implementation |
| `shared/src/commonTest` | Tests shared across platforms |
| `shared/src/androidDeviceTest` | Tests that require an Android device or emulator |
| `shared/src/androidHostTest` | Tests executed on the Android host/JVM |
| `app` | Android application configuration, manifest, launcher resources, legacy Android resources, and theme files |

Notation: `{A,B}.kt` represents individual files, `*` represents all files in a directory, and a directory name followed by `/*` represents its current contents.
