# 🕵️ Lost and Found App – SIT708 Task 9.1P

A feature-rich Android application developed as part of SIT708, designed to help users report lost items or list found things within their community. This version expands on previous iterations by adding geo-spatial features like Google Maps visualization and radius-based filtering.

## ✨ Features

- 🗺️ **Interactive Map View:** Visualize all reported lost and found items on a Google Map with automatic camera centering and bounding.
- 📍 **Radius-based Search:** Filter the list of items based on their proximity to your current GPS location (in kilometers).
- 📸 **Image Support:** Attach photos to your adverts. Images are automatically resized and compressed for optimal SQLite storage and performance.
- 🔍 **Google Places Autocomplete:** Precise location selection using the Google Places SDK for consistent address reporting.
- 📡 **Live GPS Location:** One-touch current location acquisition using the `FusedLocationProviderClient`.
- 📝 **Create Adverts:** Post details including item type (Lost/Found), name, phone, description, category, and date.
- 📋 **View & Filter Items:** A searchable list of all adverts managed via `RecyclerView`, with filtering by category and radius.
- 🗑️ **Remove Items:** Delete an advert once the item has been successfully recovered or returned.
- 💾 **Local Persistence:** Data persists in an SQLite database (version 3) with support for geo-coordinates and BLOB images.

---

## 🚀 How to Run the Project

### Prerequisites

- 🛠️ **Android Studio** (Ladybug or newer recommended)
- 📱 **Android SDK** (API 24 or higher)
- ☕ **Java 11**
- 🔑 **Google Maps API Key:** Ensure the API key in `AndroidManifest.xml` is valid and has the Maps SDK for Android and Places API enabled.

### Steps

1. **📥 Clone the repository**
   ```bash
   git clone https://github.com/your-username/LostAndFound-9.1P.git
   ```

2. **📂 Open in Android Studio**
   - **File** → **Open** → select the folder `91`

3. **🔄 Sync Gradle**
   - Click **File** → **Sync Project with Gradle Files** (or the elephant icon)

4. **🏗️ Build the project**
   - **Build** → **Make Project** (`Ctrl+F9`)

5. **▶️ Run the app**
   - Connect a physical device (USB debugging enabled) or start an emulator (API 24+)
   - Click the green **Run** button (▶️)

6. **🎮 Use the app**
   - **Create Advert:** Fill in item details. Use "Get Current Location" or type in the location field for Autocomplete suggestions.
   - **Radius Search:** On the "Show All Items" screen, enter a radius in km and click "Apply Radius" to see nearby items.
   - **Map View:** Click "Show on Map" from the main menu to see all items positioned globally.
   - **Remove:** Tap an item in the list and click "Remove" once the item is found.