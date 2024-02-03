
# Custom Gallery App

## Overview

Welcome to the Custom Gallery App, a versatile Android application developed using Java. This project is designed to serve as a custom gallery with advanced features, providing users with the ability to select multiple photos, crop images, and efficiently manage their gallery content. As an additional highlight, this project showcases the integration of third-party libraries, including image cropper, image loading with Glide, and Gson for streamlined data processing.

## Tech Stack

- **Android:** Developed natively for the Android platform to ensure seamless integration and optimal performance.
- **Java:** The primary programming language used, providing robust support for Android development.
- **Viewpager:** Implementing ViewPager for smooth navigation between different screens, enhancing the user experience.
- **Fragments:** Organizing the UI into modular fragments for improved code structure and reusability.
- **Camera:** Take image from camera by opening system camera using intent.
- **3rd Party Libraries:**
  - **Image Cropper:** Integrating a third-party library to enable image cropping functionality.
  - **Glide:** Leveraging Glide for efficient and smooth image loading within the application.
  - **Gson:** Utilizing Gson for streamlined JSON data processing and manipulation.

## Features

- **Photo Selection:** Choose multiple photos from the device gallery with ease.
- **Image Cropping:** Crop selected images to suit specific preferences or aspect ratios.
- **Image Size Reduction:** Optimize storage by reducing the size of selected images.
- **ViewPager Navigation:** Swipe through images seamlessly using the ViewPager component.
- **Third-Party Integration:** Showcase of integrating external libraries for enhanced functionality.
- **Export as Library:** Developed with the capability to be exported and implemented as a third-party library.
  

Step 1. Add the JitPack repository to your build file
Gradle
````
allprojects {
		repositories {
			
			maven { url 'https://jitpack.io' }
		}
	}
````
Step 2. Add the dependency
```
dependencies {
	        implementation 'com.github.KruBakhal:CamGall_Library:1.0.0'
	}
````

## Usage

Clone the repository and open the project in Android Studio. Run it on an emulator or a physical device to explore the features of the Custom Gallery App. This project also serves as a demonstration of how to implement the custom gallery functionality as a third-party library in other projects.

Feel free to report issues, suggest improvements, or contribute to the development of this project. Whether you are using it as a standalone application or integrating it as a library, enjoy the enhanced photo management capabilities!


