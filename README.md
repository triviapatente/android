# Trivia Patente - Android
##### Table of Contents
1. [Introduction](#intro)
2. [Getting started](#getstarted)
3. [Contributing](#contribute)
4. [Authors](#authors)
5. [License](#license)

<a name="intro"></a>
## Introduction
[TriviaPatente](https://triviapatente.github.io) is a mobile application that makes it easier and funnier to learn the theory of the driving license, connecting you with your friends.
This repository contains the native Android application.
|Screenshots|||
|:----- |:----- |:----- |
|<img src="https://github.com/triviapatente/triviapatente.github.io/blob/main/images/screen1.png" alt="drawing" width="300"/>|<img src="https://github.com/triviapatente/triviapatente.github.io/blob/main/images/screen2.png" alt="drawing" width="300"/>|<img src="https://github.com/triviapatente/triviapatente.github.io/blob/main/images/screen3.png" alt="drawing" width="300"/>|

<a name="getstarted"><a/>
## Getting started
This project was a fun pet project developed by us back in 2017. We managed to exhume the code and make it run again in order, but the technologies are quite old! Yet, cool.

### Prerequisites
The app dates to when Java was mainstream for Android development 😄:
||Version|
|:----- |:----- |
|Android|minSdkVersion: 16, targetSdkVersion: 30|

After that, the code should compile fine.
### Preparing the environment
In order to run the application properly, you need to deploy the [backend](https://github.com/triviapatente/backend) as well.

After you setup the backend, deploy it anywhere and modify this line in [strings.xml](https://github.com/triviapatente/android/blob/master/TriviaPatente/app/src/main/res/values/strings.xml):
```
<string name="baseUrl">YOUR_BASE_URL_HERE/</string>
```
### Executing
Build the app with Android Studio and challenge your friends at who knows the driver licence theory better 😜.

<a name="contribute"><a/>
## Contribute
We still need to set up an easy way to contribute, and provide a list of updates that might improve the project. You can save your ☕️s until then or, you
can drop an [email](mailto:luigi.donadel@gmail.com) to help us:
+ Set up coding style guidelines
+ Wiki
+ Documentation
+ Set up contribution workflow

<a name="authors"><a/>
### Authors
This project was developed with ❤️ and a giant dose of curiosity and passion from some very young folks (we were 20 at the time), in 2017 as a side project.
||Authors|
|:----- |:----- |
|<img src="https://avatars.githubusercontent.com/u/7453120?v=4" alt="drawing" width="50"/>|[Luigi Donadel](https://luigidonadel.com)|
|<img src="https://avatars.githubusercontent.com/u/20773447?v=4" alt="drawing" width="50"/>|[Antonio Terpin](https://antonioterpin.com)|
|<img src="https://media.licdn.com/dms/image/C4D03AQGvkKpgIYl6jg/profile-displayphoto-shrink_200_200/0/1517931535631?e=1695859200&v=beta&t=uiddasmwI5VnP5TYdeuWd57geP_DArgR7vONoI901hk" alt="drawing" width="50"/>|[Gabriel Ciulei](https://www.linkedin.com/in/gabriel-ciulei)|

<a name="license"><a/>
## License
This project is licensed under the MIT License - see the [LICENSE](https://github.com/triviapatente/android/blob/master/LICENSE) file for details.
