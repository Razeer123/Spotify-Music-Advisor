# Spotify Music Advisor - Work in progress!

Creation of this Java app was inspired by JetBrains Academy idea, but the project was extended by me. It servers as a simple Music Advisor integrated with Spotify. Its purpose is to show possibilities of connecting Java apps to servers, displaying downloaded information and using external API. This app was written entirely by me. **More features will be added soon!**

# Features 🖥

- Authorize and connect to Spotify servers
- Download and display information about many categories, playlists and recently added songs
- Results are presented in customisable lists that can be switched with commands (next, prev)
- Users' data is stored in an SQL database, so registering and logging in after closing app is possible

# What's working ✅

- Full integration with Spotify Web API (app authorizes user and downloads data from Spotify servers)
- SQL support

<p align="center">
  <img src="https://i.imgur.com/0SjVwSl.png">
</p>

# Technologies used 🔧

- Java
- Spotify Web API
- com.sun.net.httpserver.HttpServer library (to connect with external servers)
- Google GSON (to handle requests provided by Spotify servers in JSON format)
- SQLite (to store users' data)

# To do 👨‍💻

- Making app polished, clean and easy to use
- Creating GUI
