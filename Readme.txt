===============================================================================
           AI-POWERED REAL-TIME QUIZ APPLICATION - README
===============================================================================

An interactive, AI-powered real-time quiz application built with Spring Boot 4
(Java 21) for the backend and React 18 for the frontend. The application
features real-time multiplayer quizzes, AI-generated content via Google Gemini
and ONNX models, secure JWT authentication, and an admin dashboard.

===============================================================================
 SOURCE CODE
===============================================================================

Online Repository Link:
  https://github.com/I-Love-yuigahama/FYP2_APP_FOR_SUBMIT
  (This link will remain valid for at least 1 year after submission.)

===============================================================================
 FEATURES
===============================================================================

- User Authentication & Authorization
    Secure login, sign-up, and password recovery using OTP via email
    (Gmail SMTP). Passwords are encrypted. JWT tokens are used for
    session management.

- Real-Time Multiplayer Quizzes
    Join rooms via room codes, wait in lobbies, answer questions in
    real-time, and view live leaderboards using WebSockets
    (STOMP over SockJS).

- AI-Generated Content
    Integrated with Google Gemini API for dynamic quiz question
    generation. Uses Hugging Face Tokenizer and ONNX Runtime for
    local AI-powered grading and inference.

- Admin Dashboard
    Manage users and quiz questions efficiently through a dedicated
    admin interface.

- Profile Management
    Users can manage their profiles and view past quiz scores.

- Rate Limiting
    API rate limiting using Bucket4j to prevent abuse.

===============================================================================
 TECH STACK & LIBRARIES
===============================================================================

--- Backend (/FYP2) ---

  Category                 Technology / Library            Version
  -----------------------  ------------------------------  ----------
  Framework                Spring Boot                     4.0.1
  Language                 Java (JDK)                      21+
  Build Tool               Apache Maven (wrapper included) -
  Database                 MySQL + Spring Data JPA         8.0+
  Security                 Spring Security + JWT (jjwt)    0.13.0
  Real-Time                Spring WebSockets (STOMP)       -
  AI/ML - DJL API          Deep Java Library               0.27.0
  AI/ML - Tokenizer        DJL Hugging Face Tokenizers     0.27.0
  AI/ML - Inference        ONNX Runtime                    1.17.0
  AI/ML - Generation       Google GenAI (Gemini)           1.0.0
  Object Mapping           ModelMapper                     3.2.6
  Object Mapping           MapStruct + Processor           1.6.3
  Rate Limiting            Bucket4j (JDK17+ Core)          8.14.0
  Code Generation          Lombok                          (managed)
  Env Configuration        spring-dotenv                   4.0.0
  Email                    Spring Boot Starter Mail        -
  Validation               Spring Boot Starter Validation  -
  Database Connector       MySQL Connector/J               (managed)

--- Frontend (/fyp2-frontend) ---

  Category                 Technology / Library            Version
  -----------------------  ------------------------------  ----------
  Framework                React                           18.3.1
  Tooling                  Create React App (react-scripts) 5.0.1
  Routing                  React Router DOM                6.30.3
  HTTP Client              Axios                           1.14.0
  WebSocket (STOMP)        @stomp/stompjs                  7.3.0
  WebSocket (SockJS)       sockjs-client                   1.6.1
  Testing                  @testing-library/react          16.3.2

===============================================================================
 PREREQUISITES & TOOLS
===============================================================================

Ensure the following tools are installed on your machine before proceeding:

  Tool              Required Version     Notes
  ----------------  -------------------  ----------------------------------------
  Java JDK          21 or higher         Required to compile and run the backend.
  Node.js           18.x or higher       Required to run the React frontend.
  npm               9.x or higher        Bundled with Node.js.
  MySQL Server      8.0 or higher        Required for the application database.
  Git               Any recent version   Optional. For cloning from Git.
  IDE (Backend)     IntelliJ IDEA        Recommended for Java/Spring Boot.
  IDE (Frontend)    VS Code              Recommended for React development.

NOTE: Apache Maven does NOT need to be installed separately. The project
includes a Maven Wrapper (mvnw / mvnw.cmd) that automatically downloads
the correct Maven version.

===============================================================================
 DOWNLOAD LINKS FOR TOOLS
===============================================================================

  Java JDK 21     : https://adoptium.net/temurin/releases/?version=21
  Node.js 18+     : https://nodejs.org/en/download/
  MySQL 8.0       : https://dev.mysql.com/downloads/mysql/
  MySQL Workbench : https://dev.mysql.com/downloads/workbench/ (optional GUI)
  Git             : https://git-scm.com/downloads
  IntelliJ IDEA   : https://www.jetbrains.com/idea/download/
  VS Code         : https://code.visualstudio.com/download

===============================================================================
 PROJECT STRUCTURE
===============================================================================

project-root/
|
+-- FYP2/                          # Spring Boot Backend
|   +-- src/
|   |   +-- main/
|   |       +-- java/com/example/FYP2/
|   |       |   +-- Config/        # Security, JWT, WebSocket, CORS configs
|   |       |   +-- Controller/    # REST API & WebSocket controllers
|   |       |   +-- DTO/           # Data Transfer Objects
|   |       |   +-- Entity/        # JPA Entity classes (database models)
|   |       |   +-- Inteface/      # Service interfaces
|   |       |   +-- Repository/    # Spring Data JPA repositories
|   |       |   +-- Service/       # Business logic & AI services
|   |       |   +-- Fyp2Application.java  # Main application entry point
|   |       +-- resources/
|   |           +-- application.properties  # Spring Boot configuration
|   |           +-- models/                 # AI model files (ONNX + tokenizer)
|   +-- .env                       # Environment variables (see below)
|   +-- pom.xml                    # Maven dependencies
|   +-- mvnw / mvnw.cmd            # Maven Wrapper
|   +-- Dockerfile                 # Docker build configuration
|
+-- fyp2-frontend/                 # React Frontend
|   +-- src/
|   |   +-- Components/
|   |   |   +-- adminpage/         # Admin dashboard components
|   |   |   +-- auth/              # Login, sign-up, forgot password
|   |   |   +-- common/            # Shared/reusable components
|   |   |   +-- userpage/          # User-facing quiz & profile components
|   |   |   +-- Config/            # API configuration
|   |   |   +-- service/           # API service layer
|   |   |   +-- useQuizSocket.js   # WebSocket hook for real-time quizzes
|   |   +-- App.js                 # Main application with routing
|   |   +-- index.js               # React entry point
|   +-- .env                       # Frontend environment variables
|   +-- package.json               # npm dependencies
|
+-- db/
|   +-- FYP_DB.sql                 # Database dump (schema + seed data)
|
+-- source_dataset/                # Self-collected dataset
|   +-- FYP2_Biology_DATASET_csv.csv   # Biology quiz questions (CSV)
|   +-- Q_Img/                         # Question images (49 PNG files)
|
+-- User_Manual.docx / .pdf        # User manual documentation
+-- README.md                      # README (Markdown version)
+-- Readme.txt                     # README (Plain text version - this file)

===============================================================================
 SETUP & EXECUTION INSTRUCTIONS
===============================================================================

---------------------------------------------------------------------------
 STEP 1: DATABASE SETUP (MySQL)
---------------------------------------------------------------------------

1. Start MySQL Server
   Ensure your MySQL 8.0 server is running on localhost:3306.

2. Create the Database
   Open MySQL Workbench or a MySQL terminal and run:

       CREATE DATABASE fyp2;

3. Import the Database Dump
   Import the provided SQL dump file to populate the schema and seed data.

   Via Command Line:
       mysql -u root -p fyp2 < db/FYP_DB.sql

   Via MySQL Workbench:
       - Open MySQL Workbench -> Connect to your server
       - Go to Server -> Data Import
       - Select "Import from Self-Contained File" -> Browse to db/FYP_DB.sql
       - Set Default Target Schema to "fyp2"
       - Click "Start Import"

---------------------------------------------------------------------------
 STEP 2: BACKEND SETUP (Spring Boot)
---------------------------------------------------------------------------

1. Navigate to the backend directory:

       cd FYP2

2. Configure environment variables:
   Create or edit the .env file in the FYP2/ directory with these variables:

       # Database Configuration
       DB_URL=jdbc:mysql://localhost:3306/fyp2?useSSL=false&serverTimezone=UTC
       DB_USERNAME=root
       DB_PASSWORD=your_mysql_password

       # JWT Secret Key (Base64-encoded, at least 256-bit)
       JWT_SECRET=your_base64_encoded_jwt_secret_key

       # Google Gemini API Key (for AI quiz generation)
       GeminiAPIKey=your_gemini_api_key

       # Email Configuration (Gmail SMTP for OTP)
       Sender_Mail=your_email@gmail.com
       Mail_PASSWORD=your_gmail_app_password

       # CORS Allowed Origins
       CORS_ALLOWED_ORIGINS=http://localhost:3000

   HOW TO GENERATE A JWT SECRET KEY:
       Open a PowerShell terminal and run:
           [Convert]::ToBase64String(
             (1..64 | ForEach-Object { Get-Random -Minimum 0 -Maximum 256 }) -as [byte[]]
           )
       Copy the output string and paste it as JWT_SECRET.

   HOW TO OBTAIN A GMAIL APP PASSWORD:
       1. Go to your Google Account -> Security -> 2-Step Verification
          (must be enabled)
       2. Search for "App passwords" -> Generate a new app password for "Mail"
       3. Use the generated 16-character password as Mail_PASSWORD

   HOW TO OBTAIN A GEMINI API KEY:
       1. Go to https://aistudio.google.com/apikey
       2. Create a new API key and paste it as GeminiAPIKey

3. Build and run the backend:

       On Windows:
           mvnw.cmd spring-boot:run

       On Mac/Linux:
           ./mvnw spring-boot:run

   The backend will start on http://localhost:8080.

   NOTE: On first run, Maven will automatically download all required
   dependencies defined in pom.xml. This may take a few minutes depending
   on your internet speed.

---------------------------------------------------------------------------
 STEP 3: FRONTEND SETUP (React)
---------------------------------------------------------------------------

1. Open a NEW terminal and navigate to the frontend directory:

       cd fyp2-frontend

2. Configure environment variables:
   Create or verify the .env file in the fyp2-frontend/ directory:

       REACT_APP_API_URL=http://localhost:8080

3. Install dependencies:

       npm install

   NOTE: This may take a few minutes on first run as all Node packages
   are downloaded.

4. Start the React development server:

       npm start

   The frontend will start on http://localhost:3000.

5. Open your browser and navigate to http://localhost:3000 to access
   the application.

===============================================================================
 DATASET INFORMATION
===============================================================================

This project uses a self-collected Biology quiz dataset. All dataset files
are included within the source code archive (no separate download required):

  File / Folder                                  Description
  ---------------------------------------------  --------------------------------
  source_dataset/FYP2_Biology_DATASET_csv.csv    Biology quiz questions & answers
                                                 in CSV format (55 questions)
  source_dataset/Q_Img/                          49 question images (PNG format)

Additionally, the backend includes pre-trained AI model files for local
inference (also included in the source code archive):

  File                                           Description          Size
  ---------------------------------------------  -------------------  ---------
  FYP2/src/main/resources/models/model.onnx      ONNX model (AI)      ~86.2 MB
  FYP2/src/main/resources/models/tokenizer.json  HF tokenizer config  ~455 KB
  FYP2/src/main/resources/models/tokenizer_config.json  Tokenizer settings  ~350 B

No external dataset download is required. All dataset and model files
are included in the source code archive.

===============================================================================
 ENVIRONMENT VARIABLES REFERENCE
===============================================================================

--- Backend (FYP2/.env) ---

  Variable               Required   Description
  ---------------------  --------   -----------------------------------------
  DB_URL                 Yes        JDBC URL for MySQL database
  DB_USERNAME            Yes        MySQL username (e.g., root)
  DB_PASSWORD            Yes        MySQL password
  JWT_SECRET             Yes        Base64-encoded key for JWT token signing
  GeminiAPIKey           Yes        Google Gemini API key for AI features
  Sender_Mail            Yes        Gmail address for sending OTP emails
  Mail_PASSWORD          Yes        Gmail App Password (16-character)
  CORS_ALLOWED_ORIGINS   Yes        Allowed frontend origins (comma-separated)

--- Frontend (fyp2-frontend/.env) ---

  Variable               Required   Description
  ---------------------  --------   -----------------------------------------
  REACT_APP_API_URL      Yes        Backend API base URL

===============================================================================
 DEFAULT PORTS
===============================================================================

  Service            URL
  -----------------  --------------------------
  Backend API        http://localhost:8080
  Frontend (React)   http://localhost:3000
  MySQL Database     localhost:3306

===============================================================================
 TROUBLESHOOTING
===============================================================================

PROBLEM: mvnw permission denied (Mac/Linux)
  -> Run: chmod +x mvnw   (in the FYP2/ directory)

PROBLEM: MySQL connection refused
  -> Ensure MySQL server is running and the credentials in .env are correct.
  -> Verify the database "fyp2" has been created.

PROBLEM: Port 8080 already in use
  -> Stop the other process using port 8080, or change the port by adding
     server.port=<NEW_PORT> in application.properties.

PROBLEM: Port 3000 already in use
  -> Stop the other process or let React choose an alternative port
     when prompted.

PROBLEM: npm install fails
  -> Ensure Node.js 18+ is installed. Try deleting node_modules/ and
     package-lock.json, then run npm install again.

PROBLEM: Gemini API errors
  -> Verify that GeminiAPIKey in .env is valid and has quota remaining.
  -> Check https://aistudio.google.com/apikey for API key status.

PROBLEM: Email OTP not sending
  -> Ensure Gmail 2-Step Verification is enabled and Mail_PASSWORD is
     a valid App Password (not your regular Gmail login password).

PROBLEM: CORS errors in browser
  -> Ensure CORS_ALLOWED_ORIGINS in backend .env matches the frontend
     URL exactly (e.g., http://localhost:3000).

PROBLEM: ONNX model loading fails
  -> Ensure src/main/resources/models/model.onnx exists and is not
     corrupted (~86 MB). Re-download the source code archive if needed.

PROBLEM: Maven build fails with "Unsupported class file major version"
  -> Ensure you are using Java JDK 21 or higher. Check with: java -version

===============================================================================
 LICENSE
===============================================================================

This project was developed as a Final Year Project (FYP).

===============================================================================
