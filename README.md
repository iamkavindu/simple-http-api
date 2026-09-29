# simple-http-api
A small Spring Boot service with a single endpoint that says hello — but only to
names starting with A through M.

## What it does
`GET /hello-world?name=<name>`

| Request                         | Status | Body                               |
|---------------------------------|--------|------------------------------------|
| `/hello-world?name=alice`       | 200    | `{ "message": "Hello Alice" }`     |
| `/hello-world?name=Mike`        | 200    | `{ "message": "Hello Mike" }`      |
| `/hello-world?name=nancy`       | 400    | `{ "error": "Invalid Input" }`     |
| `/hello-world?name=`            | 400    | `{ "error": "Invalid Input" }`     |
| `/hello-world`                  | 400    | `{ "error": "Invalid Input" }`     |

## Requirements
- Java 21
- Nothing else — the Maven wrapper (`./mvnw`) downloads Maven for you.

## Running the app
  ```bash
  ./mvnw spring-boot:run
  ```

The app starts on port 8080. Try it out:
  ```bash
  curl "http://localhost:8080/hello-world?name=alice"
  curl -i "http://localhost:8080/hello-world?name=zoe"
  ```

## Running the tests
  ```bash
  ./mvnw test
  ```

The build also runs a Spotless formatting check. If it complains, fix the
formatting with:
  ```bash
  ./mvnw spotless:apply
  ```

## Project layout (package-by-feature)
Repository structured around feature: _**greeting**_
  ```
  src/main/java/dev/iamkavindu/app/greeting
  ├── GreetingController.java        # HTTP layer — maps the request, nothing else
  ├── GreetingService.java           # validation and the greeting itself
  ├── GreetingException.java         # thrown when the input is rejected
  ├── GreetingExceptionHandler.java  # turns that exception into a 400 response
  ├── SuccessResponse.java           # { "message": ... }
  └── ErrorResponse.java             # { "error": ... }
  ```
Alternatively you could use following repository structure as well:
  ```
  src/main/java/dev/iamkavindu/app
  ├── controller
  │   └── GreetingController.java        # HTTP layer — maps the request, nothing else
  ├── service
  │   └── GreetingService.java           # validation and the greeting itself
  ├── exception
  │   ├── GreetingException.java         # thrown when the input is rejected
  │   └── GreetingExceptionHandler.java  # turns that exception into a 400 response
  └── dto
      ├── SuccessResponse.java           # { "message": ... }
      └── ErrorResponse.java             # { "error": ... }
  ```

## Assumptions
- **Spaces are ignored.** `"  bob  "` is treated as `"bob"`, and the greeting uses the trimmed name (`Hello Bob`). A name made up only of spaces counts as empty.
- **Case doesn't matter.** `Alice` and `alice` are both accepted.
- **Only the letters A–M are accepted.** Anything else as the first character (a digit, a symbol, or a letter outside plain A–Z like `É`) gets a 400. The
  spec only defines what happens for A–Z, so I chose to reject everything else
  rather than guess.
- **`name` is a single first name.** Multi-word input isn't rejected, but it isn't
  treated specially either.
- **Only the first letter is capitalized; the rest is kept as given.** `alice` gets
  `Hello Alice`, matching the spec's example. `mcKenzie` gets `Hello McKenzie` —
  nothing is lowercased, so mixed-case names aren't mangled.
- **The client sends the `name` parameter only once.** Repeated parameters aren't
  handled specially: Spring joins them, so `?name=alice&name=zoe` arrives as
  `"alice,zoe"` and gets `Hello Alice,zoe`.
- **Unexpected errors use Spring Boot's default 500 response.** Only input
  validation failures are mapped to the `{ "error": "Invalid Input" }` shape.
