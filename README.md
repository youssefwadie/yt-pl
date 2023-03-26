# yt-pl

A simple CLI tool to get the lengths of YouTube playlists

## Installation

* You must have JRE 17 or higher installed
* Download the [built](https://github.com/youssefwadie/yt-pl/releases) executable java archive

---

## Build
* Install JDK 17 or higher
* Clone and package the code with spring boot gradle plugin
```shell
git clone https://github.com/youssefwadie/yt-pl
cd yt-pl
./gradlew bootJar
```

---

## Usage
* Export the YouTube API key as an environment variable named `YOUTUBE_API_KEY`
* To set the log file path export an environment variable named `YT_PL_LOG_FILE`, defaults to ~/.yt-pl.log
* `yt-pl <playlistUrl>` -- Get the total length of the playlist with the given url
