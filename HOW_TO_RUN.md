# How to Run the Job Recruitment System

## Prerequisites

1. **Java JDK** (Java 8 or higher)
2. **MongoDB** running on `localhost:27017`
3. **Dependencies** (JAR files in `lib/` folders)

---

## 🖥️ Running the Server

### Option 1: Using Command Line

1. **Navigate to the project root directory:**
   ```bash
   cd D:\JobRecruitmentSystem
   ```

2. **Compile the server (if not already compiled):**
   ```bash
   javac -cp "RMIServer/lib/*;Shared/src;RMIServer/src" -d bin/Server RMIServer/src/Server/*.java RMIServer/src/Server/**/*.java
   ```

3. **Run the server:**
   ```bash
   java -cp "bin/Server;RMIServer/lib/*;Shared/bin" Server.Main
   ```

   **Or run directly:**
   ```bash
   java -cp "RMIServer/lib/*;Shared/bin;RMIServer/bin" Server.Main
   ```

### Option 2: Using IDE (VS Code / IntelliJ / Eclipse)

1. **Run Server Main:**
   - Main Class: `Server.Main`
   - Classpath should include:
     - `RMIServer/lib/*` (all JAR files)
     - `Shared/bin` (compiled shared classes)
     - `RMIServer/bin` (compiled server classes)

2. **Or use the launch configuration:**
   - In VS Code, use the "▶️ Run RMI Server" configuration from `.vscode/launch.json`

### What the Server Does:

- ✅ Checks MongoDB connection (must be running on `localhost:27017`)
- ✅ Starts RMI Registry on port `1099`
- ✅ Binds all services (AuthService, ApplicantService, RecruiterService, JobService, ApplicationService)
- ✅ Keeps running until you press `Ctrl+C`

**Expected Output:**
```
╔════════════════════════════════════════╗
║    Job Recruitment System - Server    ║
╚════════════════════════════════════════╝

🔍 Checking MongoDB connection...   ✅
✅ MongoDB is connected
🚀 Starting RMI Server...

╔════════════════════════════════════════╗
║     Job Recruitment System - SERVER   ║
╚════════════════════════════════════════╝

📦 Step 1: Checking MongoDB connection...
   ✅ MongoDB connected successfully

🔧 Step 2: Starting RMI Registry on port 1099...
   ✅ RMI Registry started

🏗️  Step 3: Creating service instances...
   ✅ All services created

🔗 Step 4: Binding services to RMI Registry...
   ✅ ApplicantService bound
   ✅ RecruiterService bound
   ✅ JobService bound
   ✅ ApplicationService bound
   ✅ AuthService bound

╔════════════════════════════════════════╗
║     ✅ SERVER RUNNING!                 ║
╚════════════════════════════════════════╝
```

---

## 💻 Running the Client

You have **two options** for the client:

### Option A: GUI Client (Recommended)

**Main Class:** `client.ui.WelcomePage`

#### Using Command Line:
```bash
java -cp "RMIClient/lib/*;Shared/bin;RMIClient/bin" client.ui.WelcomePage
```

#### Using IDE:
- Run the "WelcomePage" configuration or
- Run `client.ui.WelcomePage` directly

**What it does:**
- Opens a graphical user interface
- Shows Welcome Page with Sign In and Register buttons
- Connects to the RMI server automatically

---

### Option B: Console Client

**Main Class:** `client.Main`

#### Using Command Line:
```bash
java -cp "RMIClient/lib/*;Shared/bin;RMIClient/bin" client.Main
```

#### Using IDE:
- Run `client.Main` directly

**What it does:**
- Opens a console-based interface
- Uses `ConsoleUI` for text-based interaction

---

## 📋 Step-by-Step Running Instructions

### 1. Start MongoDB (if not running)

**Windows:**
```bash
mongod --port 27017 --dbpath D:\mongodb-data\JobRecruitmentDB
```

**Linux/Mac:**
```bash
mongod --port 27017 --dbpath /path/to/mongodb-data
```

### 2. Start the Server

Open a terminal/command prompt and run:
```bash
java -cp "RMIServer/lib/*;Shared/bin;RMIServer/bin" Server.Main
```

**Keep this terminal open!** The server must be running for the client to work.

### 3. Start the Client

Open a **new** terminal/command prompt and run:

**For GUI:**
```bash
java -cp "RMIClient/lib/*;Shared/bin;RMIClient/bin" client.ui.WelcomePage
```

**For Console:**
```bash
java -cp "RMIClient/lib/*;Shared/bin;RMIClient/bin" client.Main
```

---

## 🔧 Troubleshooting

### Server won't start:

1. **MongoDB not running:**
   - Error: `❌ Cannot start server: MongoDB is not running!`
   - Solution: Start MongoDB first (see step 1 above)

2. **Port 1099 already in use:**
   - Error: `RMI Registry already running`
   - Solution: This is usually fine, but if you get connection errors, kill the existing process:
     - Windows: `netstat -ano | findstr :1099` then `taskkill /PID <pid> /F`
     - Linux/Mac: `lsof -ti:1099 | xargs kill`

3. **ClassNotFoundException:**
   - Solution: Make sure all JAR files are in the classpath and Shared classes are compiled

### Client won't connect:

1. **Connection refused:**
   - Error: `Connection refused` or `Cannot connect to server`
   - Solution: Make sure the server is running first!

2. **ClassNotFoundException:**
   - Solution: Check that all dependencies are in the classpath

---

## 📁 Project Structure

```
JobRecruitmentSystem/
├── RMIServer/
│   ├── src/Server/          # Server source code
│   ├── lib/                 # Server dependencies (MongoDB driver, BCrypt)
│   └── bin/                 # Compiled server classes
├── RMIClient/
│   ├── src/client/          # Client source code
│   ├── lib/                 # Client dependencies
│   └── bin/                 # Compiled client classes
└── Shared/
    ├── src/shared/          # Shared interfaces and models
    └── bin/                 # Compiled shared classes
```

---

## 🎯 Quick Start (All-in-One)

**Terminal 1 (Server):**
```bash
cd D:\JobRecruitmentSystem
java -cp "RMIServer/lib/*;Shared/bin;RMIServer/bin" Server.Main
```

**Terminal 2 (Client - GUI):**
```bash
cd D:\JobRecruitmentSystem
java -cp "RMIClient/lib/*;Shared/bin;RMIClient/bin" client.ui.WelcomePage
```

---

## 💡 Tips

1. **Always start the server first** before running the client
2. **Keep the server terminal open** while using the client
3. **Use the GUI client** (`WelcomePage`) for the best user experience
4. **Check MongoDB** is running if you get database connection errors

---

## 🔍 Verifying Everything Works

1. Server shows: `✅ SERVER RUNNING!`
2. Client GUI opens with Welcome Page
3. You can click "Sign In" or "Register"
4. After login, you should see the appropriate menu (Recruiter Menu, Applicant Menu, etc.)

If you see the Recruiter Functions Menu after logging in as a Recruiter, everything is working correctly! ✅

