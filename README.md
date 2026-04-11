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
# Using the database

Next, we want to set up our database.

**First**, set up an account with neon database: **https://neon.com/**

Now that we have a neon account, in our dashboard, we are going to connect with our project.


<img width="926" height="303" alt="image" src="https://github.com/user-attachments/assets/c564b5f9-4036-4b1f-b9ea-7de29567abcc" />

It will give us the following information:

<img width="926" height="672" alt="image" src="https://github.com/user-attachments/assets/b2db4813-bdd0-4f1d-9ee3-fe52cb435265" />


We want to parse the following information of the connection string. See example:

```
DB_URL: postgresql://ep-damp-art-am2q9s5x-pooler.c-5.us-east-1.aws.neon.tech/neondb?sslmode=require
DB_USERNAME: neondb_owner
DB_PASSWORD: The 16 character password(in photo is is hidden).
```
---
**Now** in VS code, we are going to add an .env file.

Let's **add our .env file into the backend**

<img width="311" height="250" alt="image" src="https://github.com/user-attachments/assets/f0f222b9-199b-49d0-a647-e67fa604647f" />

Now, enter in the information from the connection string into the .env file.

<img width="924" height="328" alt="image" src="https://github.com/user-attachments/assets/69ae7cb1-3960-4704-af4e-0b200038c739" />

Save the .env. 

We're ready to run the project!

---
# Running the project.

In **command prompt**, ensure the backend is up and running:
```bash
cd CPS406
cd backend
mvnw.cmd clean spring-boot:run
```
See 

Next, to view the webpage/frontend run the following.
```bash
cd CPS406
cd frontend
npm run dev
```
- hold the CTRL key and click onto the local link

---
# When demoing

The project has 3 different user types; client, customer representative, and cilent. Here are the set logins for each user type.

Developer account
```
username: dev@example.com
password:dev123
```

Customer Representative account
```
username: rep@example.com
password:rep123
```

Client account
```
username: client@example.com
password:client123
```


```
