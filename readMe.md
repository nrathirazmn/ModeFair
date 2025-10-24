# 💳 PayFlow – Full-Stack Engineer Assessment

PayFlow is a simplified payment gateway prototype built as part of a full-stack engineering assessment.
It simulates merchant registration, login, API key management, and a demo checkout page that sends payment requests through a secure backend API.


## 🚀 Features

### 🖥 Backend (Spring Boot + Java 17)

* RESTful API built with Spring Boot 3
* Merchant registration & login with JWT authentication
* Automatic API key generation per merchant
* Payment simulation endpoint with basic transaction states
* CORS-enabled for local frontend
* JPA entities for `Merchant` and `Transaction`

### 💳 Frontend (HTML + Vanilla JS)

* **`portal/index.html`** – Merchant registration and login
* **`portal/dashboard.html`** – Merchant dashboard displaying transactions
* **`demo/checkout.html`** – Public checkout page using embedded `payflow.js` widget
* Auto-links API key between portal and checkout
* Modern form validation, password strength meter, and responsive layout

## 🛠 Tech Stack

| Layer          | Technology                                           |
| -------------- | ---------------------------------------------------- |
| **Backend**    | Java 17, Spring Boot 3.3.4, Spring Security, JPA, H2 |
| **Frontend**   | HTML, CSS, JavaScript (no frameworks)                |
| **Auth**       | JWT via `jjwt`                                       |
| **Build Tool** | Maven                                                |
| **Runtime**    | Tested with Java 17 and Java 21                      |

## ⚙️ Setup Instructions

### 1️⃣ Prerequisites

Make sure you have:

* [Java 17+](https://adoptium.net/)
* [Maven 3+](https://maven.apache.org/install.html)
* [Node.js](https://nodejs.org/) (for `npx serve`)

Check versions:

```bash
java -version
mvn -v
```

---

### 2️⃣ Clone the Repository

```bash
git clone <>
cd payflow/backend
```

---

### 3️⃣ Run the Backend Server

```bash
mvn spring-boot:run
```

✅ **Health check:**
Visit → [http://localhost:8080/api/health](http://localhost:8080/api/health)
Should return:

```
ok
```

### 4️⃣ Run the Frontend

Open another terminal:

```bash
cd payflow/frontend
npx serve .
```
---

## 🧭 Project Structure

```
payflow/
├── backend/
│   ├── src/main/java/com/payflow/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── repo/
│   │   ├── security/
│   │   └── PayflowApplication.java
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
│
├── frontend/
│   ├── assets/
│   │   ├── payflow.js
│   │   ├── ui-helpers.js
│   │   └── styles.css
│   ├── portal/
│   │   ├── index.html
│   │   └── dashboard.html
│   └── demo/
│       └── checkout.html
│
└── README.md
```

---

## 🧪 Usage Flow

1. Go to `/portal/index.html`
2. Register as a new merchant
   → You’ll receive a JWT token and a unique API key
3. Click **“Open demo with my key”**
   → Redirects to `/demo/checkout.html` with the key preloaded
4. Submit a test payment
   → Success will appear in your dashboard transaction table

---

## 🔒 Authentication Details

* Public endpoints:
  `/api/auth/register`, `/api/auth/login`, `/api/payments`, `/api/health`
* Protected endpoints:
  `/api/merchants/**`, `/api/transactions/**`
* After login, your token and API key are stored in `localStorage`
* Authenticated requests include:

  ```
  Authorization: Bearer <token>
  ```






