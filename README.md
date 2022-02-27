# Skintker

Skintker its an app for Android devices developed to help people with skin issues in the process of
tracking which elements of their daily routine may cause those issues. The user can save a log every
day and track every past log easily. Those logs can be extracted into an external CSV in order to
analyze them with an external tool.

The app requires a Google authentication. This is used to bind in Firebase the user and its data in
order to preserve the tracked logs if the user uninstall the app or clear the data.

## :clipboard: Techincal description

This project is built in **Kotlin** and uses **Koin** as an injection framework, building its views
with **Android Jetpack Compose**. It uses the main jetpack components like **ViewModel** or **
LiveData** and a local **Room** database. The app provides a login with a Google account through **
Firebase**, which is also used to store the users data in the **Firestore Database**.

## :iphone: Screenshots

<img src="/results/questions.gif" width="260"><img src="/results/home_history.jpg" width="260">


