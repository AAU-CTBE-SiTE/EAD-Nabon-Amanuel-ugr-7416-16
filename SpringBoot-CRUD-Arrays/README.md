# SpringBoot-CRUD-Arrays

A Spring Boot REST API demonstrating full CRUD (Create, Read, Update, Partial Update, Delete) operations for an `Actor` resource using standard Java arrays (`Actor[]`) and loops for in-memory storage, without any external database or JPA/Hibernate.

## Technologies Used

- **Java 17**
- **Spring Boot 3.5.3** (`spring-boot-starter-web`)
- **Maven**
- **Java Arrays & Standard Loops** (in-memory storage)

## Project Structure

```text
SpringBoot-CRUD-Arrays/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/craft/crud/
    │   │   ├── SpringBootCrudArraysApplication.java
    │   │   ├── controller/
    │   │   │   └── ActorController.java
    │   │   ├── model/
    │   │   │   └── Actor.java
    │   │   └── service/
    │   │       └── ActorService.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/craft/crud/
            └── ActorControllerTest.java
```

## How to Run the Application

1. Navigate to the project folder:
   ```bash
   cd SpringBoot-CRUD-Arrays
   ```
2. Build and run with Maven:
   ```bash
   mvn spring-boot:run
   ```
   Or build the JAR and run it:
   ```bash
   mvn clean package
   java -jar target/springboot-crud-arrays-0.0.1-SNAPSHOT.jar
   ```
3. The server starts on `http://localhost:8080`.

## How Arrays and Loops Are Used for CRUD

In `ActorService`:
- **Storage**: Records are stored in a fixed-size array `private Actor[] actors = new Actor[10];`, initialized with a default actor (`ID: 1, Name: John Snow, Age: 30, Nationality: British`).
- **Tracking**: `actorCount` tracks how many array slots are currently occupied, and `nextId` increments monotonically on creation so newly assigned IDs never conflict with previously deleted IDs.
- **Read (`GET`)**: `getAllActors()` copies only the `actorCount` occupied elements into a new `Actor[actorCount]` array using a `for` loop so empty `null` slots are never returned. `getActorById(int id)` loops from `0` to `actorCount - 1` comparing `actors[i].getId() == id`.
- **Create (`POST`)**: Checks whether `actorCount == actors.length`. When full, `expandCapacity()` creates a new array of double the size and copies existing elements using a `for` loop. The new actor is inserted at `actors[actorCount]` and both `actorCount` and `nextId` are incremented.
- **Update (`PUT` & `PATCH`)**: Loops through `for (int i = 0; i < actorCount; i++)` to locate the matching actor by ID. `PUT` replaces all editable fields (`name`, `age`, `nationality`); `PATCH` updates only the fields supplied in the request while preserving the rest.
- **Delete (`DELETE`)**: Loops to locate the matching actor at index `i`, then shifts all subsequent elements one position to the left (`for (int j = i; j < actorCount - 1; j++) { actors[j] = actors[j + 1]; }`), clears the trailing reference (`actors[actorCount - 1] = null;`), and decrements `actorCount`. This prevents gaps and keeps subsequent reads consistent.

## API Endpoints

| Method | Endpoint | Description | Success Status | Error Status |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/actors` | Retrieve all actors | `200 OK` | - |
| `GET` | `/api/actors/{id}` | Retrieve one actor by ID | `200 OK` | `404 Not Found` |
| `POST` | `/api/actors` | Create a new actor | `201 Created` | `400 Bad Request` |
| `PUT` | `/api/actors/{id}` | Replace an entire actor | `200 OK` | `400 Bad Request` / `404 Not Found` |
| `PATCH` | `/api/actors/{id}` | Partially update an actor | `200 OK` | `400 Bad Request` / `404 Not Found` |
| `DELETE` | `/api/actors/{id}` | Delete an actor by ID | `204 No Content` | `404 Not Found` |

## Example Requests & Responses

### 1. GET All Actors
**Request:**
```http
GET /api/actors
```
**Response (`200 OK`):**
```json
[
  {
    "id": 1,
    "name": "John Snow",
    "age": 30,
    "nationality": "British"
  }
]
```

### 2. GET One Actor by ID
**Request:**
```http
GET /api/actors/1
```
**Response (`200 OK`):**
```json
{
  "id": 1,
  "name": "John Snow",
  "age": 30,
  "nationality": "British"
}
```
**Nonexistent ID (`GET /api/actors/999` -> `404 Not Found`):**
```text
Actor not found with ID: 999
```

### 3. POST Create an Actor
**Request:**
```http
POST /api/actors
Content-Type: application/json

{
  "name": "Emma Watson",
  "age": 34,
  "nationality": "British"
}
```
**Response (`201 Created`):**
```json
{
  "id": 2,
  "name": "Emma Watson",
  "age": 34,
  "nationality": "British"
}
```

### 4. PUT Update an Entire Actor
**Request:**
```http
PUT /api/actors/2
Content-Type: application/json

{
  "name": "Emma Charlotte Watson",
  "age": 35,
  "nationality": "English"
}
```
**Response (`200 OK`):**
```json
{
  "id": 2,
  "name": "Emma Charlotte Watson",
  "age": 35,
  "nationality": "English"
}
```

### 5. PATCH Partially Update an Actor
**Request:**
```http
PATCH /api/actors/1
Content-Type: application/json

{
  "age": 31
}
```
**Response (`200 OK`):**
```json
{
  "id": 1,
  "name": "John Snow",
  "age": 31,
  "nationality": "British"
}
```

### 6. DELETE an Actor
**Request:**
```http
DELETE /api/actors/2
```
**Response (`204 No Content`):**
Empty body.

**Nonexistent ID (`DELETE /api/actors/999` -> `404 Not Found`):**
```text
Actor not found with ID: 999
```
