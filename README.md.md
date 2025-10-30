
# GitCLI

A simple, Java-based command-line (CLI) tool to fetch and display the recent public activity for a specified GitHub user. Built for demonstrating modern, robust CLI application development, including smart caching, error handling, and a professional user interface.


## Demo

![Application Demo](demo_gif_cli.gif)


# Features

⬩Professional CLI: A polished interface using Picocli with commands, optional flags (--type), and auto-generated help (--help) and version (--version) messages.

⬩Smart File-Based Caching: A persistent, 10-minute file-based cache (/cache folder) automatically reduces API calls and provides instant results for recent searches.

⬩Activity Filtering: Easily filter the activity feed by any event type (e.g., -t PushEvent).

⬩Robust Error Handling: Graceful, user-friendly error messages for common issues like "User Not Found."

## Tech Stack

**Core:** Java 21, Spring Boot 3.5.7

**CLI:** Picocli

**Build** Apache Maven

**Libraries:**  Jackson (JSON Parsing)
## Run Locally

Clone the project

```bash
  git clone https://github.com/swayraj/github-activity-cli.git
```

Go to the project directory

```bash
  cd github-activity-cli
```

Build the Executable Jar

```bash
  mvn clean package
```

Run 

```bash
# Get help with commands
java -jar target/github-activity-cli-0.0.1-SNAPSHOT.jar --help
```


## Authors

- [@Swaraj Dhayarkar](https://github.com/swayraj)

