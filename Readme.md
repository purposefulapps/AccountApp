# Welcome to AccountApp

AccountApp is developed for the purpose of technical assessment, written in Android Studio, Kotlin.

## Features of AccountApp

AccountApp consists of 2 key features, Account landing page and Account Dashboard. 
From Account landing page, User may either log in or register new account. 
Upon login, User will be brought to Account Dashboard for account details.

## AccountApp Source Codes

AccountApp complete source codes are hosted in github. Clone repository with provided link: https://github.com/purposefulapps/AccountApp.git.

AccountApp consists of 2 Activities, HomeActivity and AccountActivity. 
HomeActivity consist of the landing page, Login and Register, while AccountActivity consists of Dashboard and Transfer. 
Each of the pages are fragment class, namely, LoginFragment, RegisterFragment, DashboardFragment and TransferFragment.

API calls to backend server are declared in the ApiInterface class. Data structures are declared in ModelClass. UserData object holds user information during the login session. 

At current stage, TransferFragment is not functional yet.

### Credit

Thank you OCBC for offering me this opportunity of assignment. 
And also to my wife whom manage the COVID warfare at home while I type away on the laptop.
