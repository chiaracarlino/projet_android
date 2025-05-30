# EPF Android Project

## Description

This Android application is an online shop that allows users to browse, search, and view products by category. The app features:

- Displaying a list of products with images, titles, prices, and the ability to add favorites
- Real-time product search with filtering
- Put items in the cart
- Navigation between screens (product list, product details)
- Local favorites management
- Retrofit integration to fetch data from a REST API

## Key Features

- **Product display** in a grid with optimized pagination
- **Real-time search** by product name, description, or category
- **Category filtering** to easily browse the catalog
- **Favorites**: add or remove products from favorites
- **Product details** screen with full information
- **Cart** functionality to add products to the cart
- **Smooth navigation** using Android Navigation Component

## Architecture & Technologies

- Language: Kotlin
- Architecture: MVVM (Model-View-ViewModel)
- Android Jetpack components: ViewModel, LiveData, Navigation Component, Data Binding
- Networking: Retrofit2 with Kotlin coroutines
- UI: RecyclerView with GridLayoutManager, SearchView, Glide for image loading
- Favorites management using SharedPreferences
- Database: FakeStoreAPI (https://fakestoreapi.com)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/chiaracarlino/projet_android.
   
2. Open the project in Android Studio

3. Sync Gradle dependencies

4. Run the app on an emulator or physical device

5. Log in with username: mor_2314 and password:83r5^_

## Project Structure

com.epf.android_project
├── api           # Retrofit API service definitions
├── model         # Data models (e.g., Product)
├── repository    # API calls and data fetching logic
├── ui
│   ├──auth       # Authentication-related screens (login, register)
│   ├──cart       # Cart-related screens (list, checkout)
│   ├──favorites  # Favorites screen
│   ├──home       # Home screen (product list)
│   ├──product   # Product-related screens (list, details)
│   ├──scanner   # Product scanner
│   └──shop      # Shop screen (filtering, search)
├── utils         # Utilities (e.g., favorites management)


## Notes

- The REST API must be functional and accessible for the app to retrieve data
- Filtering is handled locally in the ViewModel for better performance
