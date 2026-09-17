# QuipBill

### Bill, Analyze, and Grow

QuipBill is a **multi-tenant SaaS billing and inventory management platform** designed for small and medium-sized businesses. It helps shop owners manage products, inventory, invoices, sales, and business insights from a single application.

The backend is built with **Java and Spring Boot**, with **PostgreSQL** as the primary database and **JWT-based authentication and authorization** for securing APIs.

## 🚀 Live Application

**Website:** https://quipbill.vercel.app

**Backend:** Deployed on Render

**Repository:** https://github.com/jitsingha570/QuipBill

---

## ✨ Features

### 🔐 Authentication & Authorization

* User registration and login
* JWT-based authentication
* Access and refresh token mechanism
* Role-based access control
* Protected REST APIs
* Password hashing using BCrypt
* Tenant/business context in authenticated requests

### 🧾 Billing & Invoicing

* Create and manage invoices
* Billing history
* GST support
* Automatic invoice calculations
* Product-based billing
* Invoice data stored in PostgreSQL
* Barcode-based product identification

### 📦 Inventory Management

* Add and manage products
* Track stock availability
* Product search and autocomplete
* Barcode support
* Inventory updates during billing
* Database indexing for frequently accessed data

### 📊 Business Dashboard

* Sales overview
* Billing statistics
* Inventory insights
* Business-level data isolation
* Dashboard-based business monitoring

### 🏢 Multi-Tenant Architecture

QuipBill is designed around a multi-tenant SaaS model where business data is isolated using tenant/business context.

Authenticated requests carry the required business context, and backend services use that context while retrieving or modifying data.

This helps prevent one business from accessing another business's invoices, products, or inventory.

### 📱 Progressive Web App

* Mobile-friendly interface
* Responsive dashboard
* PWA support
* Offline-oriented user experience

---

## 🛠️ Tech Stack

### Backend

* Java 17
* Spring Boot 4
* Spring Web
* Spring Security
* Spring Data JPA
* Hibernate
* JWT
* Maven

### Database

* PostgreSQL
* JPA/Hibernate
* Database indexing
* Query optimization

### Frontend

* React
* JavaScript
* Tailwind CSS
* Progressive Web App

### DevOps & Tools

* Docker
* Git
* GitHub
* GitHub Actions
* Render
* Vercel
* Postman

### Other Technologies

* BCrypt
* JSON Web Tokens
* ZXing Barcode Library
* Email/OTP integration

---

## 🏗️ Architecture

QuipBill follows a layered backend architecture:

```text
                    ┌─────────────────────┐
                    │     React / PWA     │
                    │      Frontend       │
                    └──────────┬──────────┘
                               │
                               │ REST API
                               ▼
                    ┌─────────────────────┐
                    │    Spring Boot      │
                    │      Backend        │
                    └──────────┬──────────┘
                               │
                ┌──────────────┼──────────────┐
                ▼              ▼              ▼
          ┌──────────┐  ┌──────────┐  ┌───────────┐
          │ Security │  │ Services │  │ Controllers│
          └──────────┘  └────┬─────┘  └───────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Spring Data JPA │
                    │   Repository    │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │   PostgreSQL    │
                    └─────────────────┘
```

The backend separates responsibilities into:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

This keeps API handling, business logic, and database operations separated.

---

## 🔑 Authentication Flow

QuipBill uses JWT-based authentication.

```text
User Login
    ↓
Validate Credentials
    ↓
Generate Access Token
    ↓
Generate Refresh Token
    ↓
Client Stores Tokens
    ↓
Client Sends Access Token
    ↓
JWT Validation
    ↓
Extract User / Business Context
    ↓
Authorize Request
    ↓
Access Business Data
```

Protected APIs validate the JWT before allowing access to business resources.

---

## 🗄️ Database Design

PostgreSQL is used as the primary relational database.

Major application entities include concepts such as:

```text
User
Business / Tenant
Product
Inventory
Invoice
Invoice Item
```

Relationships between entities are handled using JPA/Hibernate.

Indexes are used on frequently queried fields to reduce unnecessary database scanning and improve query performance.

---

## 🔒 Security

Security was considered at multiple layers:

* BCrypt password hashing
* JWT authentication
* Role-based authorization
* Protected REST endpoints
* Tenant/business-level data isolation
* Environment variables for sensitive configuration
* Validation of incoming API data

Sensitive configuration such as database credentials and secret keys should be provided through environment variables rather than committed to Git.

---

## 🐳 Docker

QuipBill includes Docker support for packaging the backend application.

Build the application:

```bash
./mvnw clean package
```

Build the Docker image:

```bash
docker build -t quipbill .
```

Run the container:

```bash
docker run -p 8080:8080 quipbill
```

---

## ⚙️ Local Development

### Prerequisites

Make sure you have:

* Java 17+
* Maven
* PostgreSQL
* Node.js and npm
* Git
* Docker *(optional)*

### 1. Clone the repository

```bash
git clone https://github.com/jitsingha570/QuipBill.git
cd QuipBill
```

### 2. Configure environment variables

Create your environment configuration using the provided example:

```bash
cp .env.example .env
```

Configure the required database, JWT, email, and application variables.

> Never commit real secrets, passwords, JWT keys, or API keys to GitHub.

### 3. Run the backend

```bash
./mvnw spring-boot:run
```

For Windows:

```bash
mvnw.cmd spring-boot:run
```

The backend will start on the configured Spring Boot port.

---

## 🧪 API Testing

REST APIs can be tested using **Postman**.

Typical API flow:

```text
Register
   ↓
Login
   ↓
Receive JWT
   ↓
Send JWT with Authorization header
   ↓
Access protected APIs
```

Example:

```http
Authorization: Bearer <access-token>
```

---

## 🔄 CI/CD

GitHub Actions is used as part of the project's development and deployment workflow.

Typical workflow:

```text
Developer
    ↓
Git Push
    ↓
GitHub Repository
    ↓
GitHub Actions
    ↓
Build
    ↓
Test / Validation
    ↓
Deployment
```

This helps automate repetitive build and deployment steps.

---

## 📁 Project Structure

```text
QuipBill/
│
├── .github/
│   └── workflows/
│
├── .mvn/
│   └── wrapper/
│
├── docs/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ...
│   │   └── resources/
│   │
│   └── test/
│
├── .env.example
├── Dockerfile
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

## 📈 Performance Considerations

As the application handles business data such as products, invoices, and inventory records, database performance is important.

The project uses:

* PostgreSQL indexing
* JPA repository abstraction
* Optimized database queries
* Pagination where appropriate
* Tenant-aware data access
* Layered service architecture

The goal is to avoid unnecessary full-table scans and keep frequently used business operations responsive as data grows.

---

## 💡 Key Engineering Challenges

### 1. Multi-Tenant Data Isolation

A major design concern was ensuring that one business cannot access another business's data.

The solution is to associate business resources with their tenant/business context and enforce that context during authenticated API requests.

### 2. Secure Authentication

Instead of sending credentials with every request, QuipBill uses JWT-based authentication.

The server validates the token before allowing access to protected resources.

### 3. Inventory Consistency

Billing and inventory are connected business operations.

When an invoice is created, the corresponding inventory changes must remain consistent with the transaction.

### 4. Database Performance

As invoices and products increase, inefficient queries can become expensive.

Indexes and query optimization are therefore used for frequently accessed data.

---

## 🔮 Future Improvements

Planned improvements include:

* Redis caching
* Background job processing
* Improved observability and logging
* Centralized exception handling
* More comprehensive automated testing
* Advanced analytics
* Subscription/billing plans
* Better offline synchronization
* Horizontal scaling
* Message queue based processing

---

## 🎯 Why QuipBill?

QuipBill was built as a practical project to understand how a real-world SaaS backend works beyond basic CRUD applications.

The project focuses on:

* REST API design
* Spring Boot
* Authentication & authorization
* JWT
* Multi-tenancy
* Relational database design
* Query optimization
* Docker
* CI/CD
* Cloud deployment
* Backend architecture

---

## 👨‍💻 Author

**Jit Singha**

Computer Science Engineering Student
Backend / Software Development

GitHub: https://github.com/jitsingha570

---

## ⭐ Project

If you find the project useful or interesting, consider giving the repository a star.

**QuipBill — Bill, Analyze, and Grow.**
