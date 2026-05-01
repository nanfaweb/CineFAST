# CineFAST - Modern Cinema Booking Application

CineFAST is a premium Android application designed to provide a seamless movie booking experience. This project has been modernized from a basic prototype into a robust, data-driven application featuring secure authentication, real-time cloud synchronization, and local persistence.

## 🚀 Key Features

### 🔐 Secure Authentication & Session Management
- **Firebase Auth Integration**: Real-time user registration and login.
- **Persistent Sessions**: Automatic login on app restart via `SharedPreferences`.
- **User Profiles**: User names and emails are stored in Firebase Realtime Database and displayed in a customized navigation drawer.

### 🎬 Dynamic Movie Catalog
- **JSON-Driven Architecture**: Movie metadata, genres, durations, and trailer URLs are loaded dynamically from `assets/movies.json`.
- **Categorized Views**: Separate fragments for "Now Showing" and "Coming Soon" movies.

### 🍿 Smart Snack Ordering
- **SQLite Database**: Snack inventory and pricing are managed locally using a SQLite database (`cinefast_snacks.db`).
- **Selection Persistence**: Selected snack quantities are retained when navigating between booking screens.

### 🎫 Advanced Booking Flow
- **Interactive Seat Selection**: Visual seat grid for picking your preferred spots.
- **Real-time Persistence**: Confirmed bookings are pushed to Firebase Realtime Database under user-specific nodes (`bookings/{userId}`).
- **Booking History**: A dedicated "My Bookings" screen to view all past and future reservations.
- **Smart Cancellation**: Built-in logic that allows users to cancel future bookings while protecting historical records.

## 🛠️ Technology Stack

- **Language**: Java
- **UI Architecture**: Fragments with Navigation Drawer & XML Layouts
- **Backend/Database**: 
    - Firebase Authentication
    - Firebase Realtime Database
    - SQLite (Local persistence)
- **External Data**: JSON Assets
- **Tools**: Android Studio, Firebase BOM

## 📁 Project Structure

- `com.example.a1_smd`
    - `MainActivity.java`: The core orchestrator for fragment navigation and drawer logic.
    - `MyBookingsFragment.java`: Manages the Firebase-backed booking history.
    - `SnackDatabaseHelper.java`: Handles the SQLite logic for snack data.
    - `MovieJsonParser.java`: Utility class for parsing the movie catalog.
    - `SessionManager.java`: Handles login states and preference storage.

## 🔧 Setup & Installation

1. Clone the repository: `git clone <repo-url>`
2. Open the project in **Android Studio**.
3. Connect your Firebase project:
    - Download the `google-services.json` from the Firebase console.
    - Place it in the `app/` directory.
    - Enable **Email/Password Authentication** and **Realtime Database** in the Firebase console.
4. Synchronize Gradle and run the app on an emulator or physical device.

## 📸 Design Aesthetics
The application features a modern, high-contrast dark theme with:
- Glassmorphism UI elements.
- Vibrant accent colors for buttons and indicators.
- Smooth transitions between booking phases.
