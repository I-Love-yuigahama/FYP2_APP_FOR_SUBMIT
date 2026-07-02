# AI-Powered Real-Time Quiz Application

An interactive, AI-powered real-time quiz application built with **Spring Boot 4** (Java 21) for the backend and **React 18** for the frontend. The application features real-time multiplayer quizzes, AI-generated content via Google Gemini and ONNX models, secure JWT authentication, and an admin dashboard.

---

## Source Code

**Online Repository Link:**  
[https://github.com/I-Love-yuigahama/FYP2_APP_FOR_SUBMIT](https://github.com/I-Love-yuigahama/FYP2_APP_FOR_SUBMIT)  
*(This link will remain valid for at least 1 year after submission.)*

---

## Features

| Feature | Description |
|---|---|
| **User Authentication & Authorization** | Secure login, sign-up, and password recovery using OTP via email (Gmail SMTP). JWT-based session management. |
| **Real-Time Multiplayer Quizzes** | Join rooms via room codes, wait in lobbies, answer questions in real-time, and view live leaderboards using WebSockets (STOMP over SockJS). |
| **AI-Generated Content** | Integrated with Google Gemini API for dynamic quiz generation. Uses Hugging Face Tokenizer and ONNX Runtime for local AI-powered grading. |
| **Admin Dashboard** | Manage users and quiz questions efficiently through a dedicated admin interface. |
| **Profile Management** | Users can manage their profiles and view past quiz scores. |
| **Rate Limiting** | API rate limiting using Bucket4j to prevent abuse. |

---

## Tech Stack & Libraries

### Backend (`/FYP2`)

| Category | Technology / Library | Version |
|---|---|---|
| Framework | Spring Boot | 4.0.1 |
| Language | Java (JDK) | 21+ |
| Build Tool | Apache Maven (wrapper included) | — |
| Database | MySQL + Spring Data JPA | 8.0+ |
| Security | Spring Security + JWT (jjwt) | 0.13.0 |
| Real-Time | Spring WebSockets (STOMP) | — |
| AI/ML - DJL API | Deep Java Library | 0.27.0 |
| AI/ML - Tokenizer | DJL Hugging Face Tokenizers | 0.27.0 |
| AI/ML - Inference | ONNX Runtime | 1.17.0 |
| AI/ML - Generation | Google GenAI (Gemini) | 1.0.0 |
| Object Mapping | ModelMapper | 3.2.6 |
| Object Mapping | MapStruct + Processor | 1.6.3 |
| Rate Limiting | Bucket4j (JDK17+ Core) | 8.14.0 |
| Code Generation | Lombok | (managed) |
| Env Configuration | spring-dotenv | 4.0.0 |
| Email | Spring Boot Starter Mail | — |
| Validation | Spring Boot Starter Validation | — |
| Database Connector | MySQL Connector/J | (managed) |

### Frontend (`/fyp2-frontend`)

| Category | Technology / Library | Version |
|---|---|---|
| Framework | React | 18.3.1 |
| Tooling | Create React App (react-scripts) | 5.0.1 |
| Routing | React Router DOM | 6.30.3 |
| HTTP Client | Axios | 1.14.0 |
| WebSocket (STOMP) | @stomp/stompjs | 7.3.0 |
| WebSocket (SockJS) | sockjs-client | 1.6.1 |
| Testing | @testing-library/react | 16.3.2 |

---

## Prerequisites & Tools

Ensure the following tools are installed before proceeding:

| Tool | Required Version | Notes |
|---|---|---|
| Java JDK | 21 or higher | Required to compile and run the backend. |
| Node.js | 18.x or higher | Required to run the React frontend. |
| npm | 9.x or higher | Bundled with Node.js. |
| MySQL Server | 8.0 or higher | Required for the application database. |
| Git | Any recent version | Optional. For cloning from Git. |
| IDE (Backend) | IntelliJ IDEA | Recommended for Java/Spring Boot. |
| IDE (Frontend) | VS Code | Recommended for React development. |

> **Note:** Apache Maven does **NOT** need to be installed separately. The project includes a Maven Wrapper (`mvnw` / `mvnw.cmd`) that automatically downloads the correct Maven version.

---

## Download Links for Tools

| Tool | Download Link |
|---|---|
| Java JDK 21 | https://adoptium.net/temurin/releases/?version=21 |
| Node.js 18+ | https://nodejs.org/en/download/ |
| MySQL 8.0 | https://dev.mysql.com/downloads/mysql/ |
| MySQL Workbench | https://dev.mysql.com/downloads/workbench/ (optional GUI) |
| Git | https://git-scm.com/downloads |
| IntelliJ IDEA | https://www.jetbrains.com/idea/download/ |
| VS Code | https://code.visualstudio.com/download |

---

## Project Structure

```
project-root/
│
├── FYP2/                              # Spring Boot Backend
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/FYP2/
│   │       │   ├── Config/            # Security, JWT, WebSocket, CORS configs
│   │       │   ├── Controller/        # REST API & WebSocket controllers
│   │       │   ├── DTO/               # Data Transfer Objects
│   │       │   ├── Entity/            # JPA Entity classes (database models)
│   │       │   ├── Inteface/          # Service interfaces
│   │       │   ├── Repository/        # Spring Data JPA repositories
│   │       │   ├── Service/           # Business logic & AI services
│   │       │   └── Fyp2Application.java  # Main entry point
│   │       └── resources/
│   │           ├── application.properties
│   │           └── models/            # AI model files (ONNX + tokenizer)
│   ├── .env                           # Environment variables
│   ├── pom.xml                        # Maven dependencies
│   ├── mvnw / mvnw.cmd                # Maven Wrapper
│   └── Dockerfile                     # Docker build configuration
│
├── fyp2-frontend/                     # React Frontend
│   ├── src/
│   │   ├── Components/
│   │   │   ├── adminpage/             # Admin dashboard components
│   │   │   ├── auth/                  # Login, sign-up, forgot password
│   │   │   ├── common/               # Shared/reusable components
│   │   │   ├── userpage/             # User-facing quiz & profile components
│   │   │   ├── Config/               # API configuration
│   │   │   ├── service/              # API service layer
│   │   │   └── useQuizSocket.js      # WebSocket hook for real-time quizzes
│   │   ├── App.js                     # Main application with routing
│   │   └── index.js                   # React entry point
│   ├── .env                           # Frontend environment variables
│   └── package.json                   # npm dependencies
│
├── db/
│   └── FYP_DB.sql                     # Database dump (schema + seed data)
│
├── source_dataset/                    # Self-collected dataset
│   ├── FYP2_Biology_DATASET_csv.csv   # Biology quiz questions (CSV)
│   └── Q_Img/                         # Question images (49 PNG files)
│
├── User_Manual.docx / .pdf            # User manual documentation
├── README.md                          # This file
└── Readme.txt                         # Plain text version
```

---

## Setup & Execution Instructions

### Step 1: Database Setup (MySQL)

1. **Start MySQL Server**  
   Ensure your MySQL 8.0 server is running on `localhost:3306`.

2. **Create the Database**  
   Open MySQL Workbench or a MySQL terminal and run:
   ```sql
   CREATE DATABASE fyp2;
   ```

3. **Import the Database Dump**  
   Import the provided SQL dump to populate the schema and seed data.

   **Via Command Line:**
   ```bash
   mysql -u root -p fyp2 < db/FYP_DB.sql
   ```

   **Via MySQL Workbench:**
   - Open MySQL Workbench → Connect to your server
   - Go to **Server → Data Import**
   - Select **"Import from Self-Contained File"** → Browse to `db/FYP_DB.sql`
   - Set **Default Target Schema** to `fyp2`
   - Click **"Start Import"**

---

### Step 2: Backend Setup (Spring Boot)

1. **Navigate to the backend directory:**
   ```bash
   cd FYP2
   ```

2. **Configure environment variables:**  
   Create or edit the `.env` file in the `FYP2/` directory:

   ```properties
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
   ```

   <details>
   <summary><b>How to generate a JWT Secret Key</b></summary>

   Open a PowerShell terminal and run:
   ```powershell
   [Convert]::ToBase64String(
     (1..64 | ForEach-Object { Get-Random -Minimum 0 -Maximum 256 }) -as [byte[]]
   )
   ```
   Copy the output string and paste it as `JWT_SECRET`.
   </details>

   <details>
   <summary><b>How to obtain a Gmail App Password</b></summary>

   1. Go to your Google Account → Security → **2-Step Verification** (must be enabled)
   2. Search for **"App passwords"** → Generate a new app password for "Mail"
   3. Use the generated 16-character password as `Mail_PASSWORD`
   </details>

   <details>
   <summary><b>How to obtain a Gemini API Key</b></summary>

   1. Go to [https://aistudio.google.com/apikey](https://aistudio.google.com/apikey)
   2. Create a new API key and paste it as `GeminiAPIKey`
   </details>

3. **Build and run the backend:**

   **Windows:**
   ```cmd
   mvnw.cmd spring-boot:run
   ```

   **Mac/Linux:**
   ```bash
   ./mvnw spring-boot:run
   ```

   The backend will start on **http://localhost:8080**.

   > **Note:** On first run, Maven will automatically download all required dependencies. This may take a few minutes.

---

### Step 3: Frontend Setup (React)

1. **Open a NEW terminal** and navigate to the frontend directory:
   ```bash
   cd fyp2-frontend
   ```

2. **Configure environment variables:**  
   Create or verify the `.env` file in `fyp2-frontend/`:
   ```properties
   REACT_APP_API_URL=http://localhost:8080
   ```

3. **Install dependencies:**
   ```bash
   npm install
   ```
   > This may take a few minutes on first run.

4. **Start the React development server:**
   ```bash
   npm start
   ```

   The frontend will start on **http://localhost:3000**.

5. **Open your browser** and navigate to [http://localhost:3000](http://localhost:3000) to access the application.

---

## Dataset Information

This project uses a **self-collected** Biology quiz dataset. All dataset files are included within the source code archive — **no separate download is required**.

| File / Folder | Description |
|---|---|
| `source_dataset/FYP2_Biology_DATASET_csv.csv` | Biology quiz questions & answers in CSV format (55 questions) |
| `source_dataset/Q_Img/` | 49 question images (PNG format) |

The backend also includes pre-trained AI model files for local inference:

| File | Description | Size |
|---|---|---|
| `FYP2/src/main/resources/models/model.onnx` | ONNX model for AI grading | ~86.2 MB |
| `FYP2/src/main/resources/models/tokenizer.json` | Hugging Face tokenizer config | ~455 KB |
| `FYP2/src/main/resources/models/tokenizer_config.json` | Tokenizer settings | ~350 B |

---

## Environment Variables Reference

### Backend (`FYP2/.env`)

| Variable | Required | Description |
|---|---|---|
| `DB_URL` | Yes | JDBC URL for MySQL database |
| `DB_USERNAME` | Yes | MySQL username (e.g., `root`) |
| `DB_PASSWORD` | Yes | MySQL password |
| `JWT_SECRET` | Yes | Base64-encoded key for JWT token signing |
| `GeminiAPIKey` | Yes | Google Gemini API key for AI features |
| `Sender_Mail` | Yes | Gmail address for sending OTP emails |
| `Mail_PASSWORD` | Yes | Gmail App Password (16-character) |
| `CORS_ALLOWED_ORIGINS` | Yes | Allowed frontend origins |

### Frontend (`fyp2-frontend/.env`)

| Variable | Required | Description |
|---|---|---|
| `REACT_APP_API_URL` | Yes | Backend API base URL |

---

## Default Ports

| Service | URL |
|---|---|
| Backend API | http://localhost:8080 |
| Frontend (React) | http://localhost:3000 |
| MySQL Database | localhost:3306 |

---

## Troubleshooting

| Problem | Solution |
|---|---|
| `mvnw` permission denied (Mac/Linux) | Run `chmod +x mvnw` in the `FYP2/` directory |
| MySQL connection refused | Ensure MySQL server is running and credentials in `.env` are correct. Verify the `fyp2` database exists. |
| Port 8080 already in use | Stop the other process or add `server.port=<NEW_PORT>` in `application.properties` |
| Port 3000 already in use | Stop the other process or let React choose an alternative port when prompted |
| `npm install` fails | Ensure Node.js 18+ is installed. Delete `node_modules/` and `package-lock.json`, then run `npm install` again. |
| Gemini API errors | Verify `GeminiAPIKey` in `.env` is valid and has quota remaining |
| Email OTP not sending | Ensure Gmail 2-Step Verification is enabled. `Mail_PASSWORD` must be an App Password, not your login password. |
| CORS errors in browser | Ensure `CORS_ALLOWED_ORIGINS` in backend `.env` matches the frontend URL exactly |
| ONNX model loading fails | Ensure `models/model.onnx` exists and is not corrupted (~86 MB). Re-download if needed. |
| Maven build fails with "Unsupported class file major version" | Ensure you are using Java JDK 21+. Check with `java -version`. |

---

## License

This project was developed as a Final Year Project (FYP).
