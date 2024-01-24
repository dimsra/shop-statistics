# shop-statistics

## Overview

This Scala application is designed to process orders and gather statistics based on placed orders. One key feature is
the consideration of the age of products, achieved by grouping orders based on the creation date of products.

## Project Structure

```
.
├── MainApplication.scala
├── models
│   ├── Item.scala
│   ├── OrderGroup.scala
│   ├── Order.scala
│   └── Product.scala
├── repository
│   └── OrderRepository.scala
├── services
│   └── OrderProcessor.scala
└── tables
    └── Tables.scala
```

- **`MainApplication.scala`**: Serves as the entry point, handling command-line arguments, and initiating order
  processing.

- **`models`**: Contains case classes representing different entities (orders, products, items, age intervals, and order
  groups).

- **`repository`**: Houses the `OrderRepository` class, responsible for database interactions and order filtering.

- **`services`**: Includes the `OrderProcessor` class, managing order processing, statistic calculation, and product-age
  assignment.

- **`tables`**: Contains Slick table definitions for database entities.

## Approach

### Database Interaction

The `OrderRepository` leverages Slick, a modern database query and access library for Scala. This choice was made to
provide a type-safe and concise way to interact with the database. The repository retrieves filtered order IDs and
associated product information efficiently.

### Order Processing Logic

The `OrderProcessor` class encapsulates the order processing logic. Products are assigned to age intervals based on
their creation date, and statistics are calculated for each interval. This approach ensures clear separation of
concerns, making the codebase modular and maintainable.

## Prerequisites

- Scala
- SBT (Scala Build Tool)
- Configured Database (details in `application.conf`)

## How to Run

1. **Clone the repository:**

    ```bash
    git clone https://github.com/dimsra/shop-statistics.git
    cd shop-statistics
    ```

2. **Build the project:**

    ```bash
    sbt compile
    ```

3. **Run the application:**

    ```bash
    sbt "run <startDateTime> <endDateTime> <interval1> <interval2> ..."
    ```

   Replace `<startDateTime>`, `<endDateTime>`, `<interval1>`, `<interval2>`, etc., with the appropriate values.

## Usage

The application processes orders based on user-defined criteria. Below is an example command and its output:

```bash
$ sbt "run 2018-01-01T00:00:00 2024-01-01T00:00:00 0-1 2-3 4-8 >9"
0-1 months: 0 orders
2-3 months: 1 orders
4-8 months: 0 orders
>9 months: 5 orders
```
Statistics are outputted, showing the number of orders within specified product age intervals.

## Exercise Solution

The provided solution addresses the exercise requirements by:

- Filtering orders based on the specified date-time range.
- Retrieving associated product information for the filtered orders.
- Calculating the age of each product in months and assigning it to appropriate age intervals.
- Generating statistics for each interval, indicating the number of orders.

## Scaling and Future Enhancements:

- Consider adopting Akka for highly concurrent, distributed, and fault-tolerant systems, utilizing actors for efficient task management.
- Implement input validations to enhance robustness, preventing unexpected errors in command-line arguments, database inputs, and data integrity.
- Explore advanced databse technologies or caching mechanisms, like Redis, for optimized data management based on application needs.
- Implement a comprehensive logging system using tools like Logback or SLF4J for effective debugging and monitoring.
- Explore containerized deployment with Docker and Kubernetes for enhanced scalability, portability, and ease of management.

## Notes

- Configure the database connection details in `application.conf`.

## Author

- Sara Javaherian (sara.javaherian01@gmail.com)
