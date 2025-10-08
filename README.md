# Squares Game — IVK Technical Assignment

Complete implementation on **Java 17 + Maven**.  
Consists of three modules:

- **engine** — shared game logic  
- **cli** — console version (Task 1)  
- **service** — REST service + simple web client (Tasks 2–3)  
- **Extras:** integrated **Swagger UI**

---

## Requirements

- **JDK 17+**
- **Maven 3.9+**

---

## Project Structure

```

ivk-squares/
├── pom.xml
├── engine/
├── cli/
└── service/

````

---

## Build

```bash
mvn -q clean package
````

---

## Task 1 — Console Game

**Run:**

```bash
java -jar cli/target/cli-1.0.0-all.jar
```

**Commands:**

```
HELP
GAME N, user|comp W|B, user|comp W|B
MOVE X, Y
EXIT
```

**Examples:**

```
HELP
GAME 5, user W, comp B
MOVE 2, 2
GAME 5, comp W, comp B
EXIT
```

**Game End Messages:**

* `Game finished. W wins!` / `Game finished. B wins!`
* `Game finished. Draw`

---

## Task 2 — REST Service

**Run:**

```bash
java -jar service/target/service-1.0.0.jar
# Default: http://localhost:8080
```

**Swagger UI:**
[http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)

**Endpoints:**

* `POST /api/{rules}/nextMove` — get next move
* `POST /api/{rules}/gameStatus` — check game status
  (`{rules}` = `squares`)

### DTO Formats

**BoardDto**

```json
{
  "size": 5,
  "data": "                         ",
  "nextPlayerColor": "w"
}
```

**SimpleMoveDto**

```json
{ "x": 2, "y": 3, "color": "w" }
```

**GameStatusDto**

```json
{ "status": 0, "color": "w", "message": "Ok" }
```

`status`: `0=IN_PROGRESS`, `1=WIN`, `2=DRAW`
`color`: indicates current player or winner

### Quick cURL Tests

```bash
# Game in progress
curl -s http://localhost:8080/api/squares/gameStatus \
  -H 'Content-Type: application/json' \
  -d '{"size":5,"data":"                         ","nextPlayerColor":"w"}'

# Next move
curl -s http://localhost:8080/api/squares/nextMove \
  -H 'Content-Type: application/json' \
  -d '{"size":5,"data":"                         ","nextPlayerColor":"w"}'

# Winning example (white 2x2 square)
curl -s http://localhost:8080/api/squares/gameStatus \
  -H 'Content-Type: application/json' \
  -d '{"size":5,"data":"          ww  ww          ","nextPlayerColor":"b"}'
```

---

## Task 3 — Web Client

Open: [http://localhost:8080](http://localhost:8080)

* Choose board size `N` and player color
* Click “New Game”
* Click cells to make moves — computer responds automatically

---

### Author

**Bogdan Mitrofanov**
GitHub: [https://github.com/mitrofanovbp](https://github.com/mitrofanovbp)
