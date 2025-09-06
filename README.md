# Sporty Group - Home assignment Backend Service

## 📌 Overview

A backend service that collects events from different sportsbook providers, converts them into a unified event schema, and streams the standardized data into a message queue.

---
## 🛠 Tech Stack
- **Java 21**
- **Spring Boot 3**
- **Maven**
- **Docker**
---

## 🚀 Build & Run via Scripts
You can run the service using bash scripts. 
All scripts located in root folder.

For run all-test 

```bash
./run-all-tests.sh
```

For build and run provider-api on port 8099
```bash
./run-provider-api.sh
```

## 🐳 Build & Run via Docker Compose
You can run the service using **Docker Compose**.
Docker-compose file located in root folder. By default service start on **http://localhost:8099**

### Build and Start:
```bash
docker compose up -d --build
```

### Check logs:
```bash
docker compose logs
```

### Stop and remove containers:
```bash
docker compose down
```

### Restart without rebuilding:
```bash
docker compose up -d
```
---
## 📥 Example Requests

After starting the service (via scripts or via Docker Compose), you can send test requests to provider endpoints.

### Alpha provider requests:

```bash
curl -X POST localhost:8089/provider-alpha/feed \
  -H 'Content-Type: application/json' \
  -d '{"msg_type":"odds_update","event_id":"ev123","values":{"1":2.0,"X":3.1,"2":3.8}}'
```

```bash
curl -X POST localhost:8089/provider-alpha/feed \
  -H 'Content-Type: application/json' \
  -d '{ "msg_type": "settlement", "event_id": "ev123", "outcome": "1" }'
```

### Beta provider requests:
```bash
curl -X POST localhost:8089/provider-alpha/feed \
  -H 'Content-Type: application/json' \
  -d '{ "type": "ODDS", "event_id": "ev321", "odds": { "home": 2, "draw": 1.1, "away": 4.0 }}'
```
```bash
curl -X POST localhost:8080/provider-beta/feed \
  -H 'Content-Type: application/json' \
  -d '{ "type": "SETTLEMENT", "event_id": "ev321", "result": "away" }'
```