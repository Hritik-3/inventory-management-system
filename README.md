# Inventory Management System – Backend

Spring Boot backend powering an enterprise-grade **Inventory Management System** that manages Procurement, Inventory, Production, Supplier, and Admin workflows with secure role-based access.

---

## 🚀 Tech Stack

- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Role-Based Access Control (RBAC)
- Oracle Database
- JPA / Hibernate
- REST APIs
- Maven

---

## 👥 User Roles

The system supports the following roles with controlled API access:

- Admin  
- Procurement Officer  
- Inventory Manager  
- Production Manager  
- Supplier  

Authorization is enforced using **JWT + Spring Security RBAC**.

---

## 🔥 Core Features

✔ Secure Authentication & Authorization  
✔ JWT Token-Based Security  
✔ Role-Based Access Control  
✔ Procurement Order Management  
✔ Inventory Stock Tracking  
✔ Production Scheduling  
✔ Supplier Order Processing  
✔ Reporting & Alerts  

---

## 🏗 System Workflow

Procurement Officer creates order →  
Inventory Manager validates stock →  
Production Manager schedules production →  
Supplier involved if raw materials are insufficient →  
System updates reports and inventory in real time.

---

## 📡 Main API Modules

- Authentication Controller (Login / JWT)
- User & Role Management
- Procurement Orders
- Inventory Management
- Production Scheduling
- Supplier Management
- Reports & Dashboards

---

## 🔗 Frontend Repository

Angular Frontend:  
👉 https://github.com/Hritik-3/inventory-management-frontend

---

## ⚙️ How to Run Locally

### 1️⃣ Configure Database

Update `application.properties` with your Oracle DB credentials:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=your_username
spring.datasource.password=your_password
