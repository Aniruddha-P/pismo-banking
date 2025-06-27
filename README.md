# 🏦 Pismo Banking Application

A microservice-based banking platform that allows users to **create accounts** and perform financial operations including **purchases**, **withdrawals**, **credits**, and **debits**.

---

## 🚀 Getting Started

### 📦 Prerequisites

- Docker installed and running
- Git installed

### 📥 Clone the Repository

```bash
git clone https://github.com/Aniruddha-P/pismo-banking.git
```

```bash
cd pismo-banking
```

### ▶ Start the Application

```bash
sh start.sh
```

### ⏹ Stop the Application

```bash
sh stop.sh
```

---

## 📘 API Documentation

- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

---

## 📡 API Reference

### ➕ Create an Account

```http
POST /accounts
```

**Request Body:**

| Parameter         | Type   | Description                                  |
|------------------|--------|----------------------------------------------|
| `document_number` | string | **Required.** Unique document identification |

---

### 🔍 Retrieve Account Information

```http
GET /accounts/{accountId}
```

**Path Parameter:**

| Parameter     | Type    | Description                   |
|--------------|---------|-------------------------------|
| `account_id` | integer | **Required.** Account ID      |

---

### 💳 Create a Transaction

```http
POST /transactions
```

**Request Body:**

| Parameter           | Type         | Description                                                |
|---------------------|--------------|------------------------------------------------------------|
| `account_id`        | integer      | **Required.** ID of the account for this transaction       |
| `operation_type_id` | integer      | **Required.** Type of operation (purchase, withdraw, etc.) |
| `amount`            | BigDecimal   | **Required.** Transaction amount                           |

---

## 🧪 Postman Collection

Access the Postman API collection here:  
📎 [Download Collection](https://github.com/Aniruddha-P/pismo-banking/blob/main/src/main/resources/postman/PismoBankingAPIs.postman_collection.json)

---

## ⚙️ Optimizations

- Integrate **Resilience4j** for API rate limiting and Fault Tolerance patterns
- Integrate **Mapped Diagnostic Context - MDC** for enhanced request tracing and logging context
- Structured exception handling for consistent error responses - Error Codes
- Integrate **Liquibase/Flyway** automate changes to version-control DB schema changes

---

## 📝 Assumptions

- Uses **MySQL** as the database engine
- Custom exceptions include:
    - `AccountNotFoundException`
    - `OperationNotFoundException`
    - `InappropriateAmountException`
- Transaction amounts are capped at **10,000**

---

## 📌 Technologies Used

- Java
- Spring Boot
- Jakarta Validation
- MySQL
- Docker
- Swagger (OpenAPI)
- Postman

---