# CPSC 411 – Notes App (Jetpack Compose)

This project is an Android Notes application built for **CPSC 411** using **Kotlin** and **Jetpack Compose**.  
The app demonstrates modern Android development practices including **MVVM architecture**, **Firebase Authentication**, **Room database**, **StateFlow**, and **persistent user settings**.

---

## 📱 Features

### 🔐 Authentication
- Email & password **Sign Up** and **Login** using Firebase Authentication
- Input validation:
  - Valid email format
  - Password minimum length (6 characters)
- Loading indicators during authentication
- User-friendly error messages
- Persistent login (user stays logged in after app restart)
- Sign out functionality

---

### 🗂️ Folder & Notes Management
- Create, view, and delete **folders**
- Create, view, edit, and delete **notes**
- Notes are linked to folders (one-to-many relationship)
- Deleting a folder deletes all its notes (cascade delete)

---

### 🔍 Search, Filter, and Sort
- Search folders by name
- Search notes by title or content
- Sort folders alphabetically (A–Z / Z–A)
- Sort notes by:
  - Title (A–Z / Z–A)
  - Last modified date (Newest / Oldest)

---

### 👤 Profile
- View logged-in email
- Edit and save a **display name**
- Display name is persisted using **DataStore Preferences**
- Home screen greets the user using their display name

---

### 🎨 UI & UX
- Built entirely with **Jetpack Compose** and **Material Design 3**
- 7+ functional screens:
  - Login
  - Sign Up
  - Home
  - Profile
  - Folder List
  - Notes List
  - Add/Edit Note
- Confirmation dialogs for destructive actions (delete)
- Empty states with helpful messages
- Consistent styling and layout

---

## 🏗️ Architecture
- **MVVM (Model–View–ViewModel)** architecture
- Repository pattern for data access
- `StateFlow` for reactive UI updates
- `viewModelScope` with Kotlin Coroutines
- Separation of concerns between UI, ViewModels, and data layers

---

## 🛠️ Tech Stack
- **Kotlin**
- **Jetpack Compose**
- **Material 3**
- **Firebase Authentication**
- **Room Database**
- **DataStore Preferences**
- **StateFlow & Coroutines**

---

## 🚀 Project Setup Guide

### Prerequisites
- Android Studio (Hedgehog or newer recommended)
- Android SDK 24+
- Internet connection (for Firebase Authentication)

---

### 1️⃣ Clone the Repository

git clone https://github.com/SakethKakarla99/CPSC411aFinal.git

### 2️⃣ **Open in Android Studio**
1) Open Android Studio
2) Select Open
3) Choose the cloned project folder
4) Let Gradle sync complete

### 3️⃣ Firebase Setup
1) Create a Firebase project in the Firebase Console
2) Add an Android app with: package name matching the project
3) Download google-services.json
4) Place it in app/google-services.json
5) Enable Email/Password Authentication in Firebase

### 4️⃣ Build & Run
1) Select an emulator or physical device
2) Run the project
3) Create an account and start using the app


## 📸 Screenshot for each Screen

### Login Screen

### Home Screen

### Folder List

### Notes List

### Profile

### Notes

### Add Note

### Edit Note



