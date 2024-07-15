
# Booking API

This is a simple Booking API built using Eclipse Vert.x. The API allows clients to create and retrieve flight bookings.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Endpoints](#endpoints)
- [Testing](#testing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Installation

To run this project, ensure you have Java 17 and Maven installed on your system.

1. Clone the repository:

    ```sh
    git clone https://github.com/yourusername/vertx-emirates-app.git
    cd vertx-emirates-app
    ```

2. Build the project:

    ```sh
    mvn clean install
    ```

## Usage

To start the server, run:

```sh
mvn exec:java -Dexec.mainClass="org.example.MainVerticle"
```

The server will start and listen on port 8080.

## Endpoints

### POST /book

Create a new booking.

- **URL**: `/book`
- **Method**: `POST`
- **Headers**: `Content-Type: application/json`
- **Body**:

    ```json
    {
      "name": "John Doe",
      "flightNumber": "EK123"
    }
    ```

- **Response**:

    ```json
    {
      "status": "success",
      "message": "Booking confirmed for John Doe on flight EK123"
    }
    ```

### GET /bookings

Retrieve all bookings.

- **URL**: `/bookings`
- **Method**: `GET`
- **Response**:

    ```json
    [
      {
        "name": "John Doe",
        "flightNumber": "EK123"
      },
      {
        "name": "Jane Smith",
        "flightNumber": "BA456"
      }
    ]
    ```

## Testing

To run the tests, use:

```sh
mvn test
```

The tests will deploy the `BookingVerticle` and perform unit tests to ensure the API is working as expected.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [Vert.x](https://vertx.io/) - Reactive applications on the JVM.
