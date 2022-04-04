# Skintker

<img src="/results/logo.png" width="800" align="left">


<br>
Skintker its an app for Android devices developed to help people with skin issues in the process of
tracking which elements of their daily routine may cause those issues. The user can save a log every
day and track every past log easily. Those logs can be extracted into an external CSV in order to
analyze them with an external tool.

The app requires a Google authentication. This is used to bind in Firebase the user and its data in
order to preserve the tracked logs if the user uninstall the app or clear the data.

## 🛠 Tech Sacks

- 100% Kotlin
- 100% Jetpack Compose
- Jetpack
  - Compose
  - ViewModel
  - LiveData
- Room
- Firebase
  - Auth: user authentication
  - Firestore: database storage
- Clean architecture
- Unit tests + mocks

This project is built in **Kotlin** and uses **Koin** as an injection framework, building its views
with **Jetpack Compose**. It uses the main jetpack components like **ViewModel** or  **LiveData**
and a local **Room** database. The app provides a login with a Google account through **Firebase**,
which is also used to store the users data in the **Firestore Database**.

## ✅ Available Features

- Light and Dark themes.
- Login with Google account.
- Save your log through a guided survey.
- Logs saved locally through Room and in the Firestore database in remote. Data is saved for each
  individual user.
- Check the history of logs performed.
- Edit a log reported.
- A resume with stats and conclusions from the data reported.
- Export all the logs into a CSV file.
- App configuration panel.

## ☑️ TODO
- [ ] Notification to remind that the log isn't reported yet.

## :iphone: Previews

<img src="/results/questions.gif" width="260"><img src="/results/home_history.jpg" width="260">

## MAD Score

<img src="/results/summary.png" width="1500">

https://madscorecard.withgoogle.com/scorecard/share/1068502332/