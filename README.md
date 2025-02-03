# HarmonyHound
A simple and yet powerful telegram bot to recognize your lovely songs

## Table of content
- [Demo](#demo)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Dependencies](#dependencies)
- [To-Do List](#to-do-list)
- [Contributing](#contributing)

## Demo

## Features
- Multiple language support (Russian and English)
- Music recognition within files
- Support for telegram audio and video messages
- Search for additional information (album's name, artist's name, year of publication, etc.) for the recognized song

## Installation
1. Clone the GitHub repository
    ```
   https://github.com/MasoNord/HarmonyHound.git
   ```
2. Open the project folder from IDEA of your choice, but I highly recommend you to use Intellij IDE
3. Create application.properties file in a resources folder, if you haven't done yet, and copy the following template:
    ```
    spring.application.name=harmony-hound
    spring.mvc.hiddenmethod.filter.enabled=true

    telegram.api-url=https://api.telegram.org/
    telegram.webhook-path= <your webhook path>
    telegram.bot-name=@HarmonyHoundBot
    telegram.bot-token= <your telegram bot>
   
    server.port=5000
   
    drive.application.name= <your application name>
    drive.tokens.directory.path= <your path for credentials>
    credentials.file.path=credentials.json
   
    spring.datasource.url=jdbc:postgresql://localhost:5432/harmonydb
    spring.datasource.username= <your username>
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.datasource.password= <your password>
    spring.jpa.hibernate.ddl-auto=update
    spring.datasource.hikari.connnectionTimeout=20000
    spring.datasource.hikari.maximumPoolSize=5

    shazam.api-key= <your shazam api key>
    shazam.url=https://shazam-song-recognition-api.p.rapidapi.com/recognize/file
    rapid.api=shazam-song-recognition-api.p.rapidapi.com
   
    ffmpeg.path= <your ffmpeg path>
    ffprobe.path= <your ffprobe path>
   ```
4. How to fill an application.properties file
   1. Telegram section
      1. You have to specify your exact telegram bot token, you can get it from BotFather
      2. Next thing is to set webhook-path, for testing I used a ngrok client, if got that way, then you
      simply need to copy a ngrok client's http address and paste to telegram.webhook-path
      3. The last step is to set up a webhook path, you just need to open the following http address in you browser:
        ```
        https://api.telegram.org/bot<your telegram bot token>/setWebhook?url=<your telegram webhook path>
        ```
   2. Google Drive Section
      1. Set your application name
      2. Set path where will be stored your "Stored Credential" file  
      3. Set credentials.json path, but I would recommend you to use the default path
      (put a credentials.json in resources folder)
      4. If you stuck here, please visit the official documentation
      https://developers.google.com/drive/api/quickstart/java
   3. Rapid API section
      1. Sing up to https://rapidapi.com/
      2. Search for Shazam Song Recognition API
      3. Get your shazam api key
   4. Ffmpeg section
      1. Install ffmpeg on your PC and set your installation path for ffmpeg and ffprobe
 
## Usage
1. Find the bot by typing the following username:
```@HarmonyHoundBot```
2. Start the bot and choose the language of your choice
3. Send either a telegram voice or video message to the bot, or in case you have a file with a liked song which
you want to recognize, just send it to the bot and he'll do his job :)
4. In case you have some questions or misunderstandings tag /help command in the bot
5. If you saw some bugs or unexpected responses from the bot, 
please send me a message to the telegram account https://t.me/masonord

## Dependencies

## To-Do List

## Contributing





