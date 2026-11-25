# Hacker News Crawler

A Java-based web crawler that extracts the top 30 stories from [Hacker News](https://news.ycombinator.com/) and provides REST API endpoints to filter and sort them according to specific criteria.

The application also records API usage in a PostgreSQL database, storing the request timestamp and the filter applied.

## Technologies

- **Java 21**
- **Spring Boot**
- **Spring Web MVC** – REST API
- **Jsoup** – HTML parsing and web scraping
- **Spring Data JPA / Hibernate** – persistence
- **PostgreSQL** – database
- **Maven** – dependency management
- **JUnit 5** – automated testing
- **Springdoc OpenAPI** – API documentation and Swagger UI

## Features

### 1. Web Scraping

The application retrieves the first 30 stories displayed on the Hacker News homepage.

For each story, it extracts:

- Rank
- Title
- Points
- Number of comments

### 2. Filtering

The application exposes two filtering options:

**Long Titles**
- Includes titles containing more than 5 words.
- Sorts results by number of comments in descending order.

**Short Titles**
- Includes titles containing 5 words or fewer.
- Sorts results by points in descending order.

Word counting is based on whitespace-separated tokens. Tokens containing no letters or numbers are excluded.

### 3. Usage Tracking

Every request to a filtering endpoint is recorded in PostgreSQL.

The usage log stores:
- Request ID
- Applied filter (`LONG_TITLES` or `SHORT_TITLES`)
- Request timestamp

## Project Structure

```text
src
├── main
│   ├── java/com/ely/hackernews
│   │   ├── controller
│	│	├── database
│   │   ├── entity
│   │   ├── model
│   │   ├── repository
│   │   ├── scraper
│   │   ├── service
│   │   └── HackerNewsCrawlerApplication.java
│   └── resources
│       └── application.properties
└── test
    └── java/com/ely/hackernews
        ├── scraper
        └── service
		└── HackerNewsCrawlerApplicationTests.java
```

## Prerequisites

Make sure you have the following installed:

- Java 21
- Maven
- PostgreSQL

## Configuration

### 1. Create the database

The SQL script required to create the database is available in:

```text
database/create_database.sql
```

Execute this script in PostgreSQL before starting the application.

Alternatively, you can create the database manually:

```sql
CREATE DATABASE hacker_news_db;
```

### 2. Configure environment variables

The application reads database credentials from environment variables.

Set the following variables before running the application.

**Windows PowerShell:**

```powershell
$env:DB_USERNAME="your_user_name"
$env:DB_PASSWORD="your_password"
```

Replace `your_user_name` and `your_password` with your PostgreSQL credentials.

The default database connection is configured in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hacker_news_db
spring.datasource.username=${DB_USERNAME:your_user_name}
spring.datasource.password=${DB_PASSWORD}
```

Hibernate automatically creates or updates the required database tables.

## Running the Application

Clone the repository:

```bash
git clone https://github.com/nullam/hacker-news-crawler.git
cd hacker-news-crawler
```

Run the automated tests:

```bash
mvn clean test
```

Start the application:

```bash
mvn spring-boot:run
```

The application will be available at:

```text
http://localhost:8080
```

## REST API Endpoints

### Get stories with long titles

**GET** `/api/news/long-titles`

Returns stories with more than 5 words in their titles, sorted by comments in descending order.

Example:

```bash
curl http://localhost:8080/api/news/long-titles
```

### Get stories with short titles

**GET** `/api/news/short-titles`

Returns stories with 5 words or fewer in their titles, sorted by points in descending order.

Example:

```bash
curl http://localhost:8080/api/news/short-titles
```

### Response example

```json
[
  {
    "number": 1,
    "title": "Example Hacker News Story",
    "points": 125,
    "comments": 42
  }
]
```

## API Documentation

The API documentation is automatically generated using OpenAPI 3 and Springdoc.

Swagger UI provides an interactive interface to explore and test the available REST endpoints.

Once the application is running, access the documentation at:

**Swagger UI:**
http://localhost:8080/swagger-ui.html

**OpenAPI JSON:**
http://localhost:8080/v3/api-docs

Through Swagger UI, you can:
- Explore the available endpoints.
- Review request and response structures.
- Execute API requests directly from the browser.

## Database Usage Logs

Every request to a filtering service method is recorded in PostgreSQL, including its timestamp and applied filter.

To inspect the records, execute:

```sql
SELECT id, requested_at, filter_applied
FROM usage_logs
ORDER BY requested_at DESC;
```

## Design Decisions

- **Separation of concerns:** The application separates scraping, business logic, REST endpoints, and persistence into dedicated components.
- **Jsoup:** Used to parse the Hacker News HTML and extract story information without introducing unnecessary dependencies.
- **Java Records:** `HackerNewsEntry` is modeled as an immutable record, providing a concise representation of scraped data.
- **Constructor injection:** Dependencies are injected through constructors, improving testability and reducing coupling.
- **Stream API:** Java Streams are used to filter and sort stories in a declarative way.
- **Enum-based filter tracking:** Filter types are represented by an enum and persisted as strings for readability and maintainability.
- **PostgreSQL persistence:** Spring Data JPA provides a simple mechanism to store usage information.

## Testing

Automated unit tests cover:

- Hacker News story extraction.
- Handling missing points and comments.
- Limiting extraction to the first 30 stories.
- Filtering stories by title word count.
- Sorting stories by comments or points.

Run all tests with:

```bash
mvn clean test
```

## Notes

- The scraper retrieves live data from Hacker News each time an endpoint is called.
- The application requires an active internet connection to access Hacker News.
- Database credentials should be provided through environment variables and must not be committed to the repository.
