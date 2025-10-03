# Игра «Квадраты» — ТЗ АО «ИВК» (Задания 1–3)

Полное решение на **Java 17 + Maven**:

- **engine** — общий движок
- **cli** — консольная игра (Задание 1).
- **service** — REST-сервис + простой веб-клиент (Задания 2–3).
- **Дополнительно:** Подключён Swagger UI.

## Требования

- JDK **17+**
- Maven **3.9+**

## Структура

```

ivk-squares/
pom.xml
engine/
cli/
service/

````

## Сборка всего проекта

```bash
mvn -q clean package
````

---

## Задание 1 — консольная игра

**Запуск:**

```bash
java -jar cli/target/cli-1.0.0-all.jar
```

**Команды:**

```
HELP
GAME N, user|comp W|B, user|comp W|B
MOVE X, Y
EXIT
```

**Примеры:**

```
HELP
GAME 5, user W, comp B
MOVE 2, 2
GAME 5, comp W, comp B
EXIT
```

Сообщения окончания:

* `Game finished. W wins!` / `Game finished. B wins!`
* `Game finished. Draw`

---

## Задание 2 — REST-сервис

**Запуск:**

```bash
java -jar service/target/service-1.0.0.jar
# по умолчанию http://localhost:8080
```

**Swagger UI:**
[http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)

**Эндпоинты (совместимы с описанием из ТЗ):**

* `POST /api/{rules}/nextMove`  — следующий ход.
* `POST /api/{rules}/gameStatus` — статус партии.

`{rules}` указывать как `squares`.

### Форматы DTO

* **BoardDto**

  ```json
  {
    "size": 5,
    "data": "                         ",
    "nextPlayerColor": "w"
  }
  ```

  `data` — строка длиной `size*size`, по строкам сверху вниз, слева направо;
  символы: `"w"` (белые), `"b"` (чёрные), `" "` (пробел = пусто).

* **SimpleMoveDto**

  ```json
  { "x": 2, "y": 3, "color": "w" }
  ```

* **GameStatusDto**

  ```json
  { "status": 0, "color": "w", "message": "Ok" }
  ```

  где `status`: `0=IN_PROGRESS`, `1=WIN`, `2=DRAW`;
  `color`: при `0` — чей ход; при `1` — победитель; при `2` — пустая строка.

### Быстрые проверки (cURL)

**Игра идёт:**

```bash
curl -s http://localhost:8080/api/squares/gameStatus \
  -H 'Content-Type: application/json' \
  -d '{"size":5,"data":"                         ","nextPlayerColor":"w"}'
```

**Следующий ход:**

```bash
curl -s http://localhost:8080/api/squares/nextMove \
  -H 'Content-Type: application/json' \
  -d '{"size":5,"data":"                         ","nextPlayerColor":"w"}'
```

**Победа белых (квадрат 2×2 по центру):**

```bash
curl -s http://localhost:8080/api/squares/gameStatus \
  -H 'Content-Type: application/json' \
  -d '{"size":5,"data":"          ww  ww          ","nextPlayerColor":"b"}'
```

**Ничья (пример 3×3):**

```bash
curl -s http://localhost:8080/api/squares/gameStatus \
  -H 'Content-Type: application/json' \
  -d '{"size":3,"data":"wbwwbwbwb","nextPlayerColor":"w"}'
```

---

## Задание 3 — веб-клиент

Открыть: **[http://localhost:8080](http://localhost:8080)**

* Выберите размер `N` и цвет игрока.
* Нажмите «Новая игра».
* Кликайте пустые клетки — компьютер отвечает автоматически.

---

### Автор: Богдан Митрофанов

GitHub: https://github.com/mitrofanovbp