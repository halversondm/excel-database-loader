# Excel Database Loader

## Overview

Excel Database Loader is a full-stack application designed to import, parse, and manage data from Excel files into a database. It provides a RESTful API for backend operations and a modern React-based UI for user interaction.

## Features

- Upload Excel files via the web interface
- Parse and validate Excel data
- Store parsed data in a relational database
- Search and manage imported records
- Dockerized deployment for both backend and frontend

## Project Structure

- `excel-database-loader-api/`: Java Spring Boot backend API
    - Handles file uploads, parsing, and database operations
    - Uses Maven for build management
    - Includes database changelogs and configuration files
- `excel-database-loader-ui/`: React frontend
    - User interface for uploading files and viewing data
    - Uses Vite for fast development and build
    - Tailwind CSS for styling

## Technologies Used

- Java, Spring Boot, Maven
- React, JavaScript, Vite, Tailwind CSS
- Docker, Docker Compose
- SQL (database migrations)
- Node.js, npm/yarn

## Getting Started

1. Clone the repository
2. Build and run the backend API (`excel-database-loader-api/`)
3. Build and run the frontend UI (`excel-database-loader-ui/`)
4. Use Docker Compose for easy setup

## License

This project is licensed under the MIT License.