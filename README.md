# MystiWax: Aesthetic Candle E-Commerce Platform

## 🛍️ What is MystiWax?

MystiWax is a high-end e-commerce platform for aesthetic candles. This project is a demonstration of a robust, modern **Java/Spring Boot** web application that focuses on a fast, user-friendly shopping experience.

It was designed to showcase full-stack development skills, from efficiently streaming product photos to dynamically loading inventory on the page.

## 🛠️ Key Technology & Architecture

This application uses a Server-Side Rendering (SSR) approach for maximum speed and SEO, backed by a powerful NoSQL database.

| Category | Component | What it Does | 
 | ----- | ----- | ----- | 
| **Backend Core** | **Spring Boot / Java** | The engine that runs the entire application, handling all the logic and data access. | 
| **Frontend Rendering** | **Thymeleaf** | Generates the final HTML page on the server, ensuring quick load times before sending it to the user's browser. | 
| **Data & Storage** | **MongoDB & GridFS** | MongoDB stores product details (name, price, stock). **GridFS** is specifically used to handle the large product photos efficiently. | 
| **Data Interface** | **RESTful API** | Provides separate, clean endpoints for product data (`/api/products`) and image streaming (`/image/{id}`). | 

## ✨ Features That Show Skill

* **Efficient Image Delivery:** Product pictures are streamed directly from **GridFS**, keeping the main database clean and handling large files gracefully.

* **Dynamic Inventory:** The homepage loads only a small, featured selection of products, while a separate **"View All"** page loads the entire dynamic catalog.

* **Clean Code Separation:** Logic is clearly split between **Page Controllers** (for UI pages) and a **REST Controller** (for data, perfect for future mobile app integration).

### Prerequisites

* **Java 17+** and **Maven**.

* Local **MongoDB** instance running (version 4.0+).
