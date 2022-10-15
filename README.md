# SBMA_ShakeIT
System Based Mobile Applications projekt work

## Firebase
The app uses firebase to store data. 
* Create new firebase project: https://firebase.google.com/docs/android/setup
* Register the app with firebase
  * Project overview -> add app (Android)
  * Enter package name. ApplicationId from module level gradle.
  * Download and move the Firebase Android configuration file (google-services.json) into the module (app-level) root directory
  
After firebase project is created and the app registered
* Enable firebase authentication
  * Go to firebase project -> Authentication -> Sign-in method -> Choose Email/Password and enable it
* Create firestore -> Firestore database
  * Allow read/write from rules tab
  * Create collections: friendRequest, shakes, users
