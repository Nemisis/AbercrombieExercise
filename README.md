# Abercrombie Android Exercise

The following is a guide on how to run and test the Android Application.

## Running

You can run the app in one of two ways. The first is by opening the application in Android Studio. To do so:

 1. Open Android Studio.
 2. Click "File" -> "Open.."
 3. Navigate to the project in your file explorer and double click on the `build.gradle` file in the root.
 4. When Android Studio finishes opening, click the Run button. 

Another option is to open your terminal and navigate to the project and run the following:

    ./gradlew clean assemble installDebug


## Testing
To test the application, open your terminal and run the following command:

    ./gradlew connectedCheck

This will run the Android Instrumentation test. To run the JUnit test, run the following:

    ./gradlew test
