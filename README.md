# 🎬 Movie App  

An Android application that allows users to **search for movies**, **view detailed information**, and **manage their favorite movies**. The app interacts with a public movie database API to fetch and display movie data, ensuring a smooth and user-friendly experience.  

---

## ✨ Features  
- **🔍 Search Movies:** Search for movies by title using a search bar.  
- **📖 Movie Details:** View detailed information about a selected movie, including full description, rating, and cast.  
- **❤️ Favorites Management:** Mark/unmark movies as favorites and persist this state locally.  
- **✅ Robust Validation:** Handle API errors, rate limiting, and edge cases gracefully.  
- **💡 Intuitive UI/UX:** Clean and responsive design with smooth transitions and loading indicators.  

---

## 🛠️ Tech Stack & Libraries  
- **Language:** Kotlin  
- **Architecture:** MVVM (Model-View-ViewModel)  
- **Networking:** Retrofit  
- **JSON Parsing:** Gson  
- **Local Storage:** Room Database  
- **Dependency Injection:** Hilt  
- **Image Loading:** Coil  
- **UI Framework:** Jetpack Compose  

---

## 🌐 API Integration  
This app uses [The Movie Database (TMDb) API](https://www.themoviedb.org/documentation/api) to fetch movie data.  

### 🔑 To use the API:  
1. **Register and get an API key** from [TMDb](https://www.themoviedb.org/documentation/api).  
2. **Add the API key** to the `gradle.properties` file:  
    ```properties
    tmdb_api_key = "YOUR_API_KEY_HERE"
    ```  

---

## 🏗️ Architecture Overview  
The app follows the **MVVM (Model-View-ViewModel)** architecture:  
- **Model:** Manages data and business logic (API calls and Room DB interactions).  
- **View:** UI components built using Jetpack Compose.  
- **ViewModel:** Connects Model and View, handling UI state and business logic.  

---

## 🙏 Acknowledgements  
- [The Movie Database (TMDb)](https://www.themoviedb.org/) for providing the movie data.  
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for the modern UI framework.  

---

## 📧 Contact  
For any queries or support, feel free to reach out:  
- **Email:** [vshal9921@gmail.com](mailto:vshal9921@gmail.com)  
- **LinkedIn:** [vshal9921](https://www.linkedin.com/in/vshal9921)  
