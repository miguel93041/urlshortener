# Getting Started with the URL Shortener project

2024-08-31

## System requirements

This application leverages cutting-edge technologies to deliver a robust
and versatile user experience:

1.  **Programming Language**: The application is written in [Kotlin
    2.0.20](https://kotlinlang.org/), a versatile, open-source,
    statically-typed language. Kotlin is renowned for its adaptability
    and is commonly used for Android mobile app development. Beyond
    that, it finds application in server-side development, making it a
    versatile choice.

2.  **Build System**: The application utilizes [Gradle
    8.5](https://gradle.org/) as its build system. Gradle is renowned
    for its flexibility in automating the software building process.
    This build automation tool streamlines tasks such as compiling,
    linking, and packaging code, ensuring consistency and reliability
    throughout development.

3.  **Framework**: The application employs [Spring Boot
    3.3.3](https://docs.spring.io/spring-boot/) as a framework. This
    technology requires Java 17 and is fully compatible up to and
    including Java 21. Spring Boot simplifies the creation of
    production-grade [Spring-based applications](https://spring.io/). It
    adopts a highly opinionated approach to the Spring platform and
    third-party libraries, enabling developers to initiate projects with
    minimal hassle.

## Overall structure

The structure of this project is heavily influenced by [the clean
architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html):

- A `core` module where we define the domain entities and the
  functionalities (also known as use cases, business rules, etc.). They
  do not know that this application has a web interface or that data is
  stored in relational databases.
- A `repositories` module that knows how to store domain entities in a
  relational database.
- A `delivery` module that knows how to expose the functionalities on
  the web.
- An `app` module that contains the main application, the configuration
  (i.e., it links `core`, `delivery`, and `repositories`), and the
  static assets (i.e., HTML files, JavaScript files, etc.).

```mermaid
flowchart LR;
    User-- HTTP -->Tomcat("Embedded<br>Web Server<br><b>Apache Tomcat")
    subgraph "Application <b>UrlShortener</b>"
        Tomcat== "Dynamic<br>resources" ==>Delivery("Module<br><b>delivery</b>")
        Tomcat== "Static<br>resources" ==>App("Module<br><b>app</b>")
        Tomcat~~~App("Module<br><b>app</b>")
        App-. configure .->Tomcat
        App-. configure .->Delivery
        App-. configure .->Core
        App-. configure .->Repositories
        subgraph Core [Module<br><b>core</b>]
            PortA("Port")==>UseCases("Use<br>Cases")
            UseCases==>PortB("Port")
        end
        PortB==>Repositories("Module<br><b>repositories</b>")
        Delivery==>PortA
    end
    Repositories-- JDBC -->Database[(Database)]
```

Usually, if you plan to add a new feature:

- You will add a new use case to the `core` module.
- If required, you will modify the persistence model in the
  `repositories` module.
- You will implement a web-oriented solution to expose it to clients in
  the `delivery` module.

Sometimes, your feature will not be as simple, and it may require:

- Connecting to a third party (e.g., an external server). In this case,
  you will add a new module named `gateway` responsible for such a task.
- An additional application. In this case, you can create a new
  application module (e.g., `app2`) with the appropriate configuration
  to run this second server.

Features that require connecting to a third party or having more than a
single app will be rewarded.

## Run

The application can be run as follows:

``` bash
./gradlew bootRun
```

Now you have a shortener service running at port 8080. You can test that
it works as follows:

``` bash
$ curl -v -d "url=http://www.unizar.es/" http://localhost:8080/api/link
*   Trying ::1:8080...
* Connected to localhost (::1) port 8080 (#0)
> POST /api/link HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.71.1
> Accept: */*
> Content-Length: 25
> Content-Type: application/x-www-form-urlencoded
> 
* upload completely sent off: 25 out of 25 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 201 
< Location: http://localhost:8080/tiny-6bb9db44
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Tue, 28 Sep 2021 17:06:01 GMT
< 
* Connection #0 to host localhost left intact
{"url":"http://localhost:8080/tiny-6bb9db44","properties":{"safe":true}}%   
```

And now, we can navigate to the shortened URL.

``` bash
$ curl -v http://localhost:8080/6bb9db44
*   Trying ::1:8080...
* Connected to localhost (::1) port 8080 (#0)
> GET /tiny-6bb9db44 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.71.1
> Accept: */*
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 307 
< Location: http://www.unizar.es/
< Content-Length: 0
< Date: Tue, 28 Sep 2021 17:07:34 GMT
< 
* Connection #0 to host localhost left intact
```

## Build and Run

The uberjar can be built and then run with:

``` bash
./gradlew build
java -jar app/build/libs/app-0.2024.1-SNAPSHOT.jar
```

## Functionalities

The project offers a minimum set of functionalities:

- **Create a short URL**. See in `core` the use case
  `CreateShortUrlUseCase` and in `delivery` the REST controller
  `UrlShortenerController`.

- **Redirect to a URL**. See in `core` the use case `RedirectUseCase`
  and in `delivery` the REST controller `UrlShortenerController`.

- **Log redirects**. See in `core` the use case `LogClickUseCase` and in
  `delivery` the REST controller `UrlShortenerController`.

The objects in the domain are:

- `ShortUrl`: the minimum information about a short URL
- `Redirection`: the remote URI and the redirection mode
- `ShortUrlProperties`: a handy way to extend data about a short URL
- `Click`: the minimum data captured when a redirection is logged
- `ClickProperties`: a handy way to extend data about a click

## Delivery

The above functionality is available through a simple API:

- `POST /api/link` which creates a short URL from data send by a form.
- `GET /{id}` where `{id}` identifies the short URL, deals with
  redirects, and logs use (i.e. clicks).

In addition, `GET /` returns the landing page of the system.

## Repositories

All the data is stored in a relational database. There are only two
tables.

- **shorturl** that represents short URLs and encodes in each row
  `ShortUrl` related data,
- **click** that represents clicks and encodes in each row `Click`
  related data.

## Reference Documentation

For further reference, please consider the following sections:

- [Official Gradle documentation](https://docs.gradle.org)
- [Spring Boot Gradle Plugin Reference
  Guide](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/)
- [Spring
  Web](https://docs.spring.io/spring-boot/reference/web/index.html)
- [Spring SQL
  Databases](https://docs.spring.io/spring-boot/reference/data/sql.html)

## Guides

The following guides illustrate how to use some features concretely:

- [Building a RESTful Web
  Service](https://spring.io/guides/gs/rest-service/)
- [Serving Web Content with Spring
  MVC](https://spring.io/guides/gs/serving-web-content/)
- [Building REST services with
  Spring](https://spring.io/guides/tutorials/rest/)
- [Accessing Data with
  JPA](https://spring.io/guides/gs/accessing-data-jpa/)


# Project Report: Proof of Concept (PoC) for URL Shortener

## Introduction
This report presents the Proof of Concept (PoC) for a URL shortener. Each feature outlined below has been implemented with minimal functionality to demonstrate its feasibility and validate its integration within the existing codebase. Each feature is documented with a brief description and usage instructions.

## 1. QR Code Generation
### Description
Generate a QR code for any shortened URL, offering an alternative access method.

### Usage
To use this feature, users simply provide a URL, and the system generates a corresponding QR code. Users can scan the QR code with any compatible device to access the original URL.

### Validation
- **Correctness**: Ensure that the QR code generated correctly corresponds to the shortened URL, and they properly redirect.
- **Scalability**: Analyze the response time when generating thousands of QR codes per second, and consider implementing load balancing or caching to mitigate server strain.
- **Professionalism**: Provide a clean code structure with detailed comments, and ensure test coverage for different QR code formats and URL lengths.

## 2. Browser and Platform Identification
### Description
Analyze HTTP headers to identify the browser (e.g., Chrome, Firefox) and platform (e.g., Windows, macOS, Linux) used during redirection requests.

### Usage
This feature works automatically upon each redirection request, capturing and logging the browser and platform information for each user accessing the shortened URL.

### Validation
- **Correctness**: Test cases to confirm the correct browser and OS identification for a variety of common user agents.
- **Scalability**: Conduct tests that simulate high traffic, and verify that browser/platform detection does not become a bottleneck. Consider using lightweight parsing libraries that handle HTTP headers at scale.
- **Professionalism**: Include unit tests to ensure coverage of different types of HTTP headers and platforms.

## 3. Geolocation Service
### Description
Provide the client’s geographical location based on their IP address. This is useful for tracking both the user requesting a redirection and the user who clicked the shortened URL.

### Usage
This feature activates automatically for each redirection, providing the location of users in terms of country and region based on their IP address.

### Validation
- **Correctness**: Test with various IPs to confirm the service returns the correct location data.
- **Scalability**: Simulate thousands of simultaneous redirections and measure how efficiently the geolocation service responds. Implement optimizations like result caching for frequent IPs to reduce external API calls.
- **Professionalism**: Include clear documentation on the implementation and handling of IP geolocation services, ensuring the use of appropriate data sources and security measures.

## 4. URL Accessibility Check
### Description
Ensure that a URL is reachable before allowing it to be shortened.

### Usage
Users submit a URL, which the system verifies for reachability before generating a shortened link. Unreachable URLs are flagged and rejected.

### Validation
- **Correctness**: Implement test cases to validate the accessibility of various URLs and ensure unreachable URLs are rejected.
- **Scalability**: Perform load testing to confirm the system can handle mass URL checks in a reasonable time frame.
- **Professionalism**: Include documentation explaining how URL accessibility is verified and ensure test coverage for both valid and invalid URLs.

## 5. Google Safe Browsing Check
### Description
Validate the safety of a URL using the Google Safe Browsing API, ensuring users are not redirected to malicious sites.

### Usage
Before URL shortening, each URL is evaluated for safety. If flagged as unsafe by Google Safe Browsing, the URL is rejected.

### Validation
- **Correctness**: Test with a variety of URLs to confirm that unsafe URLs are correctly flagged and rejected.
- **Scalability**: Use stress tests to simulate high traffic and verify the system's responsiveness when making API calls.
- **Professionalism**: Provide detailed documentation on the API integration and ensure that error handling and edge cases are well covered in tests. Ensure the API is utilized securely with key management practices.

## 6. CSV Upload
### Description
Enable users to upload a CSV of URLs to shorten, and return a CSV of shortened URLs.

### Usage
Users can upload a CSV file containing a list of URLs. The system processes each URL and provides a downloadable CSV with the shortened URLs.

### Validation
- **Correctness**: Test various CSV formats and ensure successful URL shortening and accurate return of results.
- **Scalability**: Ensure that the system can process CSV files containing thousands of URLs efficiently. This could involve implementing chunked processing or background jobs to prevent the system from being blocked by large uploads.
- **Professionalism**: Ensure code readability and include tests for different CSV edge cases, like empty rows or invalid URLs.

## 7. Redirection Limits
### Description
Set limits on redirections, such as a maximum number of redirects over a set time or concurrent redirects for a URL or domain.

### Usage
This feature is configured by the administrator to set redirection limits on URLs or domains. Limits prevent excessive redirects in a short period and can be monitored through system logs.

### Validation
- **Correctness**: Implement test cases to confirm that redirection limits are triggered and respected.
- **Scalability**: Simulate heavy redirection traffic to verify that limit enforcement remains consistent. Explore distributed data stores or sharded databases to manage redirection counters in large-scale systems.
- **Professionalism**: Provide documentation explaining the redirection limits and ensure the logic is well-structured, with clear test cases for edge cases and abuse prevention.
