# GetStreamPerusal
GetStreamPerusal, as the name suggests, is a perusal of the [GetStream SDK](https://github.com/GetStream/stream-chat-android).
It is a messaging application that is created to showcase some of the most recent trends in android
development such as [Jetpack Compose](https://developer.android.com/jetpack/compose) and [Material 3](https://m3.material.io/).
The back end functionality is provided mostly by GetStream SDK when it comes to the messaging, and 
AWS when it comes to Authentication and object storage.

## Light Theme
<img src="./readme/recording.gif" width="720" alt="Recording of application walk-through." />

## Dark Theme
<img src="./readme/dark_theme.gif" width="360" alt="Recording of application walk-through in dark theme." />

## Key Libraries/Skills
The main objective of this project is to gain practice of the libraries/skills mentioned below. A
short explanation as to why I decided to learn about Jetpack Compose is provided [here](https://www.notion.so/GetStreamPerusal-7a904f515577440594c73d24b34f848c?pvs=4#a7ced8a585674368bf1be1398a2ffad7). 

- Jetpack Compose + Compose Navigation
- Material3
- Flow
- Data store
- AWS (API Gateway, Lambda, DynamoDB, S3)

## Tasks
I used Notion to organise the [tasks](https://wandering-emperor-514.notion.site/80922b12ddda46d3a3553b7b28a5fe51?v=25bd3124e1dc4b718f4c99ad4f973d7a) 
for this project.

## Disclaimer
The application is **not to be used for production** and is merely done as a **POC**. Furthermore, 
the application is not compilable without `Env.kt` which is included in the .gitignore file. The 
`Env.kt` file contains the address of the api endpoints which enables users to sign up, by providing
a JSON Web Token, and change their profile picture.

## License
```xml
MIT License

Copyright (c) 2023 6879756e

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```