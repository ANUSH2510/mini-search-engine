## MiniCrawl — Web Crawler & Search Engine
MiniCrawl is a search engine built in Java that crawls real websites, builds an inverted index, and returns TF-IDF ranked results through a web interface.

## What It Does
MiniCrawl starts from a seed URL and autonomously crawls the web using BFS. For each page it visits, the parser extracts readable text and outgoing links using Jsoup.
Text flows to the Indexer which builds an inverted index stored in PostgreSQL — mapping every meaningful word to the pages it appears on, along with frequency counts.
When a user searches, results are retrieved from the index and returned through a Spring Boot web interface.

## Why I Built This
I built MiniCrawl because I wanted to understand how Google search works from the ground up — not just use it, but actually build a version of it myself.
Building it taught me how real systems handle edge cases like 404 errors, retry logic, relative URLs, rate limiting, and crawl persistence — things no tutorial 
ever covers.

## How It Works
Seed URL
   ↓
Crawler (BFS — Queue + HashSet)
   ↓
Parser (Jsoup — extracts text + links)
   ↓
Indexer (inverted index — word → pages)
   ↓
Ranker (TF frequency scoring)
   ↓
Search UI (Spring Boot + Thymeleaf)


---

## Tech Stack

| Tool        | Why                                                        |
|-------------|------------------------------------------------------------|
| Java        | Primary language, widely used in backend systems at scale  |
| Spring Boot | Bridge between Java search logic and the web               |
| Jsoup       | Parses raw HTML and extracts text and links from pages     |
| PostgreSQL  | Stores pages and inverted index, persists across restarts  |
| Thymeleaf   | HTML template engine that displays search results          |

---

## System Design

**Crawler** uses a LinkedList as a Queue for BFS traversal —
explores the web broadly rather than diving deep into one corner.
Visited URLs are tracked in a HashSet for O(1) duplicate detection.

**Parser** extracts page titles and paragraph text using Jsoup.
Ignores navbars, scripts, footers, and advertisements.
Relative URLs are resolved automatically using abs:href.

**Indexer** builds an inverted index mapping each word to the pages
it appears on, along with frequency counts. Stop words are filtered.
Words shorter than 3 characters are discarded. Everything is
normalized to lowercase before storing.

**Database** uses two tables. The page table stores crawled pages
with url, title, content, status, and crawled_at timestamp.
The index_entries table stores word, page_id, and frequency
with a Foreign Key connecting them.

---

## Database Schema
pages                            index_entries
┌─────────────┬───────────┐      ┌───────────┬─────────┐
│ id          │ BIGINT PK │      │ id        │ BIGINT  │
│ url         │ TEXT      │      │ word      │ VARCHAR │
│ title       │ TEXT      │◄─────│ page_id   │ BIGINT  │
│ content     │ TEXT      │      │ frequency │ INTEGER │
│ status      │ VARCHAR   │      └───────────┴─────────┘
│ crawled_at  │ TIMESTAMP │
└─────────────┴───────────┘


## Edge Cases Handled

| Scenario          | Solution                                        |
|-------------------|-------------------------------------------------|
| 404 Not Found     | Skip permanently — page does not exist          |
| 503 Server Error  | Retry up to 3 times with 1 second delay         |
| Slow pages        | 5 second timeout before marking as error        |
| Too many links    | Max 50 links extracted per page                 |
| Relative URLs     | Resolved using abs:href via Jsoup               |
| Duplicate URLs    | HashSet O(1) lookup prevents revisiting         |
| Crawler crash     | Status column allows resuming from pending pages|

---

## Current Stats
Pages crawled    → 50+
Index entries    → 45,788
Stop words       → filtered
Min word length  → 3 characters


## How To Run Locally
Prerequisites — JDK 17+, PostgreSQL, Maven

```bash
# 1. Clone the repository
git clone https://github.com/yourusername/minicrawl

# 2. Create the database
CREATE DATABASE search_engine;

# 3. Update application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/search_engine
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# 4. Run the project
mvn spring-boot:run

# 5. Open in browser
http://localhost:8080
```

## What I Learned

Building MiniCrawl taught me things no course covers — how BFS applies
to real web traversal, why inverted indexes make search fast, how TF
frequency scoring works, and how production systems handle failure
gracefully. Every component maps directly to concepts used at companies
like Google at scale.


## Author

Anush — B.Sc Computer Science, SCSVMV University
GitHub: github.com/ANUSH2510
