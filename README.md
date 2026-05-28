# Library Management System Backend

## Project Overview

The Library Management System Backend is a Spring Boot based REST API application designed to manage library operations for employees and administrators.

The system supports complete book lifecycle management including:

* Book inventory management
* Book request and approval workflow
* Issued books tracking
* Renewal management
* Return management
* Wishlist and waitlist handling
* Notifications and reminders
* Employee dashboard
* Analytics and monitoring
* Audit logging
* JWT authentication and role-based authorization

The backend follows layered architecture using:

* Controller Layer
* Service Layer
* Repository Layer
* DTO-based API responses
* Entity relationships using JPA/Hibernate

---

# Technologies Used

## Backend Framework

* Java 17
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate

## Database

* MySQL

## Authentication

* JWT Token Authentication
* Role-Based Authorization

## Utilities

* Lombok
* ModelMapper
* Cloudinary (Image Upload)
* Scheduler (@Scheduled)

## Build Tool

* Maven

---

# Roles in System

## ADMIN

Admin can:

* Manage books
* Approve/reject requests
* Monitor issued books
* View analytics
* Manage renewals
* Process returns

## EMPLOYEE

Employee can:

* Browse inventory
* Request books
* View issued books
* Add wishlist
* Receive notifications
* Request renewals

---

# Features Implemented

# 1. Authentication Module

## Features

* User Registration
* Login Authentication
* JWT Token Generation
* Role-Based Access
* Forgot Password with OTP
* Reset Password
* Profile Image Support

## Security

* Spring Security
* JWT Authentication Filter
* Role Authorization
* Protected APIs

---

# 2. Book Management Module

## Features

* Add Book
* Update Book
* Delete Book
* Get All Books
* Get Book By ID
* Search Books
* Filter By Category
* Upload Book Image

## Additional Logic

* ISBN uniqueness validation
* Automatic stock status update
* Available copies management
* Out of stock handling

---

# 3. Inventory Module

## Features

* Browse inventory
* Search books
* Category filtering
* View detailed book information
* Request issue book

## Request Workflow

Employee requests a book → Admin approves/rejects → Notification sent

---

# 4. Admin Request Management Module

## Features

* View pending requests
* Approve request
* Reject request
* Admin remarks
* Request processing date

## Approval Logic

When approved:

* BookIssue entry created
* Due date assigned (45 days)
* Available copies reduced
* Notification sent

## Rejection Logic

When rejected:

* Notification sent
* Employee can add to wishlist
* Employee can subscribe for availability notification

---

# 5. Issued Books Dashboard

## Features

* View all issued books
* Due date tracking
* Overdue detection
* Renewal eligibility
* Return eligibility
* Countdown timer

## Countdown Logic

Countdown starts during last 48 hours.

### Example

* 48 Hours Remaining
* 24 Hours Remaining
* Due Today
* Overdue

## Additional Features

* Remaining days calculation
* Overdue days calculation
* Real-time countdown support

---

# 6. Notification Dashboard

## Features

* Get all notifications
* Unread notification count
* Mark notification as read
* Mark all notifications as read
* Delete notification

## Notification Types

* BOOK_REQUEST
* REQUEST_APPROVED
* REQUEST_REJECTED
* DUE_REMINDER
* URGENT_REMINDER
* OVERDUE
* BOOK_AVAILABLE

---

# 7. Wishlist & Waitlist Module

## Features

* Add to wishlist
* Remove from wishlist
* Notify when available
* Waitlist handling

## Automatic Notification Logic

When admin increases available copies:

* Waiting users automatically notified

---

# 8. Renewal Request Module

## Features

* Request renewal
* Approve renewal
* Reject renewal

## Rules

* Only one renewal allowed
* Renewal duration = 15 days
* Renewal only for active issued books

---

# 9. Return Book Module

## Features

* Return issued books
* Update issue status
* Restore available copies
* Return condition support

## Return Conditions

* GOOD
* DAMAGED
* LOST

---

# 10. Analytics Module

## Features

* Total books
* Total issued books
* Total returned books
* Total overdue books
* Active users
* Most borrowed book

---

# 11. Monitoring Module

## Features

* Monitor all issued books
* View overdue books
* View returned books
* Employee-wise monitoring

---

# 12. Employee Dashboard

## Features

* Dashboard cards
* Recent activity
* Unread notifications
* Wishlist count
* Issued books count
* Overdue books count
* Quick actions

---

# 13. Scheduler Module

## Features

Automatic daily scheduler for:

* 48-hour reminder
* 24-hour reminder
* Overdue updates

## Additional Logic

* Prevent duplicate notifications
* Auto mark books as overdue

---

# 14. Audit Log Module

## Tracks

* Book requests
* Wishlist operations
* Request approvals/rejections
* Renewals
* Returns

---

# Entity Relationships

## Major Entities

* User
* Book
* BookIssue
* BookRequest
* BookReturn
* RenewalRequest
* Wishlist
* Waitlist
* Notification
* AuditLog
* LibraryPolicy
* Category

---

# Pagination

Pagination implemented internally across:

* Books
* Inventory
* Wishlist
* Notifications
* Issued books
* Admin requests
* Monitoring

## Logic

* Automatically limits responses to 10 records
* No frontend changes required

---

# API Architecture

## Layers

* Controller
* Service
* Repository
* DTO
* Entity

## DTO Usage

Separate DTOs used for:

* Request payloads
* Response payloads
* Dashboard responses
* Analytics responses

---

# Exception Handling

## Custom Exceptions

* ResourceNotFoundException
* IllegalStateException
* Unauthorized access handling

## Global Exception Handler

Centralized error response handling implemented.

---

# Image Upload

## Cloudinary Integration

Used for:

* Book image upload
* Profile image upload

---

# Scheduler Cron

```java
@Scheduled(cron = "0 0 9 * * *")
```

Runs daily at 9 AM.

---

# Database Used

## MySQL Tables

* users
* books
* book_issues
* book_requests
* book_returns
* renewal_requests
* notifications
* wishlists
* waitlists
* audit_logs
* categories
* library_policies

---

# Project Structure

```text
src/main/java
│
├── controller
├── service
├── serviceImpl
├── repository
├── dto
├── entity
├── config
├── security
├── scheduler
├── exception
```

---

# Security Features

* JWT Authentication
* Role-based authorization
* Protected endpoints
* Secure password handling
* OTP verification

---

# Future Enhancements

* Fine payment integration
* Email notifications
* PDF report generation
* Swagger documentation
* Docker deployment
* Unit testing
* Redis caching
* Microservices architecture

---

# Conclusion

The Library Management System Backend provides a complete enterprise-style backend solution for managing modern library workflows.

The project demonstrates:

* Clean architecture
* REST API development
* Spring Boot best practices
* Security implementation
* Scheduler automation
* Real-world business logic
* Pagination
* DTO architecture
* JPA relationships
* Production-ready coding standards
