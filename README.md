# StreetCare Application - Run Instructions
This document provides step-by-step instructions to run the
StreetCare application, which includes a backend API and a frontend
interface.
---
## BACKEND - Spring Boot API
### Technologies Used
- Java 21
- Spring Boot 3.4.3
- Maven
- MySQL 9.2.0
- Lombok
- Google Maps API (used via frontend)
- Axios (used in frontend)
- Recommended IDE: IntelliJ IDEA
### Prerequisites
- Java JDK 21
- Maven
- MySQL Server (version 8.0 or above)
- IntelliJ IDEA (recommended)
### Setup Instructions
1. **Import Project into IntelliJ IDEA**
- Open IntelliJ.
- Select **File > Open** and choose the backend project folder.
- IntelliJ will detect it as a Maven project and download
required dependencies.
2. **Create Database**
- Start your MySQL server.
- Open a terminal and execute:
```
mysql -u root -p < "Dump20250502 (1).sql"
```
- Ensure MySQL credentials in the `application.properties` file
are accurate.
3. **Configure `application.properties`**
Located at `src/main/resources/application.properties`, verify:
```
spring.application.name=Streetcare Backend
spring.datasource.url=jdbc:mysql://localhost:3306/
report_potholesdb
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLD
ialect
spring.jackson.serialization.write-dates-as-timestamps=false
```
4. **Run the Application**
- In IntelliJ, right-click the main class annotated with
`@SpringBootApplication` and select **Run
'StreetBackendApplication'**.
- Alternatively, use **Build > Rebuild Project** and run the
application.
5. **Run Backend Tests (Optional)**
- Right-click the `test` folder and choose **Run 'All Tests'**,
or run individual test classes.
---
## FRONTEND - Vue 3 with Vite
### Technologies Used
- JavaScript (Vue 3)
- HTML5 & CSS3
- Vite (build tool)
- Axios
- Vue Router
- Pinia (limited use)
### Prerequisites
- Node.js (version 18+)
- npm (Node Package Manager)
### Setup Instructions
1. **Extract and Open Folder**
- Unzip the frontend project.
- Navigate to the folder in terminal:
```
cd streetcare-frontend
```
2. **Install Dependencies**
```
npm install
```
3. **Start Development Server**
```
npm run dev
```
4. **Access the Application**
- Once started, a message will display a local URL, such as:
```
Local: http://localhost:5173/
```
- Open this URL in your web browser.
### Notes
- The frontend must be run **while the backend is also running** to
access full functionality.
- Ensure both frontend and backend are running concurrently for API
communication via Axios.
---
## Software Summary
| Component | Tool/Runtime | Version |
|-------------|-------------------|--------------------|
| Backend | Java JDK | 21 | | Spring Boot | 3.4.3 | | Maven | Latest | | MySQL | 8.0+ | Frontend | Node.js | 18+ | | npm | Bundled with Node | Development | IntelliJ IDEA | (Backend) | | Visual Studio C | (Frontend) |
|
|
|
|
|
|
|
---
## Final Notes
- Ensure ports used by the backend (typically 8080) and frontend
(typically 5173) are free and not blocked by firewall or other
applications.
- If any API calls fail in the frontend, check CORS settings or
proxy configuration.
