# CPS 406: Team 25 SBL Project

This is the executable steps for demoing sprint 2.

---

## Installing dependecies - Windows Machine

**Java version 21 is required for this project to run.**

In powershell, use winget to **install java version 21**.
```bash
winget install oracle.jdk.21
```

 OR install JDK 21 directly from the oracle site.
 https://www.oracle.com/ca-en/java/technologies/downloads/ 

 Verify your version is up to date by checking ```java -version``` in command prompt

 ---
 
Now clone the repository, and run the following commands in your command prompt.

```bash
git clone https://github.com/xena-p/CPS406.git
```

To install **frontend** dependecies
```bash
cd CPS406
cd frontend
npm install
```

To install **backend** dependecies
```bash
cd CPS406
cd backend
mvnw.cmd spring-boot:run
```
---
# Running the project.
---

In **command prompt** backend is up and running:
```bash
cd CPS406
cd backend
mvnw.cmd clean spring-boot:run
```

Next, in front end run the following.
```bash
cd CPS406
cd frontend
npm run dev
```
hold the CTRL key and click onto the local link
