## AgriConnect - Backend

This is the backend API for AgriConnect, a platform connecting agricultural cooperatives with buyers and giving cooperatives tools to manage products, inventory, members, and communication digitally. This repository covers the API only — the Next.js frontend lives in a separate repository.

The backend exposes a REST API consumed by three client-facing apps: the public marketplace, the cooperative portal, and the admin console. It does not process payments or handle buyer accounts; its job is authentication, business logic, data persistence, and integration with SMS and AI chatbot services.

Tech stack


Architecture

Standard layered structure:

Controller  →  Service  →  Repository  →  Database


Controller : REST endpoints, request/response handling,
Service :business logic (approval workflows, RBAC checks, stock rules)
Repository : Spring Data JPA interfaces
Entity : JPA-mapped domain objects
Security : JWT filter, Spring Security config, permission evaluation
Integration : SMS and chatbot API clients, isolated from core business logic

In summary of what the system will be having

Database schema 
Authentication & JWT issuing
Role-based access control
Cooperative registration & approval endpoints
Product & inventory management endpoints
Member registry endpoints
Sales recording endpoints
Reports & analytics endpoints
SMS integration
AI chatbot integration
