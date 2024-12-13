# ElectricURL Shortener project

2024-10-26

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
- A `gateway` module that knows how to call third APIs.
- A `delivery` module that knows how to expose the functionalities on
  the web.
- An `app` module that contains the main application, the configuration
  (i.e., it links `core`, `delivery`, and `repositories`), and the
  static assets (i.e., HTML files, JavaScript files, etc.).

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

A Google Safe Browsing and IPInfo key must be put in a .env file in app module like this (or env variables):
```
IPINFO_API_KEY=3400c66...
GOOGLE_API_KEY=AIzaSyAQAo1i4...
```

## Functionalities

The project offers a set of functionalities:

- **Create a short URL**. See in `core` the use case `CreateShortUrlUseCase` and in `delivery` the REST controller `UrlShortenerController`.
- **Redirect to a URL**. See in `core` the use case `RedirectUseCase` and in `delivery` the REST controller `UrlShortenerController`.
- **Log redirects**. See in `core` the use case `LogClickUseCase` and in `delivery` the REST controller `UrlShortenerController`.
- **Generate a QR Code for shortened URLs**. A QR code is shown in the frontend containing the shortened URL.
- **Analyze browser and platform**. HTTP headers are parsed and user browser and platform are identified during redirection.
- **Geolocation service**. Geographical location of users is collected based on their IP addresses.
- **Check URL accessibility**. URL is checked for reachability before shortening.
- **Google Safe Browsing Check**. URLs are checked for safety before shortening them.
- **CSV Upload for Bulk URL Shortening**. Allows users to upload a CSV file containing URLs and download a new CSV with the shortened URLs.
- **Redirection Limits**. Imposes a limit of 10 redirections per URL, preventing further redirects once the limit is reached.

The objects in the domain are:

- `ShortUrl`: the minimum information about a short URL
- `Redirection`: the remote URI and the redirection mode
- `ShortUrlProperties`: a handy way to extend data about a short URL
- `Click`: the minimum data captured when a redirection is logged
- `ClickProperties`: a handy way to extend data about a click
- `GeoLocation`: IP address and its country
- `BrowserPlatform`: user`s browser and platform

## Delivery

The above functionality is available through a simple API:

- `POST /api/link` which creates a short URL from data send by a form.
- `POST /api/upload-csv` for bulk processing of URLs through CSV uploads.
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

# First Project Report
## QR Code Generation
### Description
Generate a QR code for any shortened URL, offering an alternative access method.
### Libraries
We utilized the zxing.qrcode library for this feature. ZXing (Zebra Crossing) is an open-source library widely used for both encoding and decoding barcode formats, including QR codes. The zxing.qrcode module specifically simplifies generating and reading QR codes, which can convert text or URLs into QR code images and also decode existing QR codes. This makes it highly effective for use in URL shortening services, where generating a QR code is often required.
### How to run the PoC
When a user inputs a URL and clicks the "Shorten" button, the shortened URL appears along with the automatically generated QR code directly beneath it. This QR code is instantly usable without any additional steps.
### Tests
Three key tests have been implemented for this feature:
1. Valid URL and Size Test: Verifies that when a valid URL and size are provided, the QR code is correctly generated.
2. Invalid URL Test: Confirms that if an invalid URL (e.g., empty string) is provided, the QR code generation throws an exception.
3. Null URL Test: Similar to the invalid URL test, but checks that when a null URL is passed, an exception is also thrown.

## Browser and Platform Identification
### Description
Analyze HTTP headers to identify the browser (e.g., Chrome, Firefox) and platform (e.g., Windows, macOS, Linux) used during redirection requests.
### Libraries
We utilized the ua-parser library, a lightweight JavaScript tool that parses the User-Agent string in HTTP headers to identify the browser, operating system, and device type. It works on both client-side (in-browser) and server-side (Node.js), making it versatile for cross-platform use.
### How to run the PoC
When a user clicks a shortened URL, the system extracts and parses the User-Agent string from the request headers. The parsed browser and platform information are then stored in the ClickProperties object during the click event logging process.
### Tests
Three tests ensure the functionality of this feature:
1. Valid User-Agent Test: Ensures that valid User-Agent strings are correctly parsed into browser and platform information.
2. Invalid User-Agent Test: Checks that when an invalid or empty User-Agent string is provided, an exception is thrown.
3. Null Return Value Test: Verifies that if the parsing function returns null, default values for the browser and platform are used.

## Geolocation Service
### Description
Provide the client’s geographical location based on their IP address. This is useful for tracking both the user requesting a redirection and the user who clicked the shortened URL.
### API
We integrated the IPInfo API to retrieve geolocation information based on the user's IP address. This API performs HTTP requests and returns the country and other geolocated details of the given IP address.
### How to run the PoC
During both GET and POST requests, the remote IP address is extracted from the client request. The IP address is then used to query the IPInfo API, which returns the associated country. This information is stored in the ClickProperties for the click events or ShortUrlProperties when the short URL is created.
### Tests
Three tests ensure the reliability of the geolocation service:
1. Valid API Response Test: Ensures that when the API returns valid data, both the IP address and country are correctly retrieved.
2. Bogon IP Test: Confirms that when the API returns a bogon (private network) IP, the service returns "bogon" as the country.
3. API Error Handling Test: Ensures that when the API returns an error, an exception is thrown.

## URL Accessibility Check
### Description
Ensure that a URL is reachable before allowing it to be shortened.
### Libraries
We used WebClient, a non-blocking, reactive HTTP client provided by Spring for handling HTTP requests. It allows us to asynchronously perform GET requests to check the reachability of URLs. The client efficiently handles errors and supports multiple HTTP methods, making it ideal for URL validation.
### How to run the PoC
Before a URL is shortened, a GET request is made to verify its accessibility. If the URL is unreachable, an error message is displayed, and the URL is not processed for shortening.
### Tests
Two tests validate the functionality:
1. Reachable URL Test: Ensures that when a URL is reachable, it is successfully processed.
2. Unreachable URL Test: Verifies that when a URL is not reachable, an exception is thrown, and the URL is not shortened.

## Google Safe Browsing Check
### Description
Validate the safety of a URL using the Google Safe Browsing API, ensuring users are not redirected to malicious sites.
### API
We have used the Google Safe Browsing API which is free of charge. Given an url it returns if it's safe or not.
### How to run the PoC
Before an url is shortened, it is checked against Google Safe Browsing API. If it is dangerous, it returns an error and is not shortened.
### Tests
Two tests validate the functionality:
1. Malicius URL
2. Safe URL

## CSV Upload
### Description
Enable users to upload a CSV of URLs to shorten, and return a CSV of shortened URLs.
### Libraries
None
### How to run the PoC
Users can upload a CSV file by clicking the paper clip icon next to the URL input field. Once uploaded, the system processes the URLs, shortens them, and downloads a new CSV file containing both the original and shortened URLs.
### Tests
Four tests have been implemented:
1. Valid URL Test: Ensures that valid URLs in the CSV are processed and shortened correctly.
2. Invalid URL Test: Verifies that an exception is thrown when an invalid URL is encountered.
3. Empty URL Test: Confirms that no processing occurs if an empty URL is provided.
4. Multiple URLs Test: Tests that the system correctly processes multiple URLs in a single CSV file.

## Redirection Limits
### Description
Set limits on redirections, such as a maximum number of redirects over a set time or concurrent redirects for a URL or domain.
### Libraries
None
### How to run the PoC
A limit of 10 redirections per URL has been set. Once a user clicks the same shortened URL 10 times, an error message is displayed, and further redirects are blocked.
### Tests
Only one test has been implemented:
1. Redirect Limit Test: Verifies that the system correctly enforces the limit after 3 redirections, simulating a lower threshold to confirm the functionality.

# Issues Found

1. **GitHub Actions does not properly send SECRETS, causing them not to be injected into tests**  
   During the CI/CD process, GitHub Actions encountered issues where environment secrets were not being passed correctly to the test suite. This resulted in sensitive configuration variables not being available during the testing phase, leading to test failures and incomplete validation of the code.

2. **The core module had `bootJar`, which is incorrect since it should be a library**  
   The core module was configured to use `bootJar`, which is intended for applications rather than libraries. As this module is a library, the appropriate configuration would be to use `jar` instead, ensuring the module is packaged correctly for its intended purpose.

3. **Integration tests failed in the GitHub Actions workflow**  
   The integration tests within the GitHub Actions workflow consistently failed. This issue suggests potential misconfigurations or environment mismatches between local and CI environments, or problems with dependency management during the testing phase. This failure impacted the overall CI process, preventing successful builds and deployments.
