# 🍹 BarMate

BarMate is a Java-based microservices application (Spring Boot + Spring Cloud) that helps users manage their home bar: ingredients, drink recipes, shopping lists, and usage analytics. The system also integrates external APIs (TheCocktailDB, OpenAI) and Python scripts for report generation and data visualization.

## 🎯 Project Goal

To streamline the organization of home bar resources by automating recipes, planning purchases, personalizing user preferences, and tracking ingredient usage.

## ✅ Architecture & Main Modules

The project consists of several microservices, each responsible for a specific domain:

### 📦 Core Microservices

- `inventory-service` – ingredient and stock management
- `recipe-service` – drink recipe database and smart suggestions
- `shopping-service` – shopping list generation
- `user-service` – user preferences, history, and authentication
- `analytics-service` – statistics, charts, and cost analysis

### 🔧 Shared Infrastructure

- `config-server` – centralized configuration (Spring Cloud Config)
- `discovery-service` – service registry (Eureka)
- `gateway-service` – API gateway (routing and authentication)
- `auth-service` – Keycloak integration
- `database-setup` – containerized databases (Docker)

---

## 📦 inventory-service – ingredients

Manages the bar’s ingredient stock, with filtering and integration capabilities.

**Features:**

- Full CRUD operations (REST API)
- Filtering by name and category (ALCOHOL, MIXER, FRUIT, OTHER)
- Validation and OpenAPI documentation
- AssertJ-based tests

---

## 📦 recipe-service – recipes & suggestions

Handles drink recipes and integrates with the external TheCocktailDB API.

**Features:**

- Create and edit recipes with ingredient lists
- Filtering by name, tags, or alcohol type
- Import and sync recipes from TheCocktailDB
- Suggest drinks based on available stock
- AssertJ-based tests and OpenAPI docs

---

## 📦 shopping-service – shopping lists

Generates shopping lists based on missing ingredients for selected recipes.

**Features:**

- Full CRUD for shopping lists and items
- Missing ingredient calculation based on inventory and recipes
- Manual item adding/checking
- AssertJ-based tests and OpenAPI docs

---

## 📦 user-service – users & personalization

Manages user profiles, preferences, favorites, and drink history. Integrates with Keycloak for authentication.

**Features:**

- User registration/login with Keycloak (JWT)
- Edit taste preferences
- Favorite recipes and drink history
- Role-based access control
- AssertJ-based tests and OpenAPI docs

---

## 📦 analytics-service – analytics & charts

Collects usage statistics and integrates with Python scripts for reports and visual charts.

**Features:**

- Track prepared drinks and ingredient usage
- Cost and frequency analysis
- Generate visual reports (e.g., top 5 drinks, weekly costs)
- Integration with PythonChartService
- AssertJ-based tests and OpenAPI docs

---

## ⚙️ Technologies

- Java 17, Spring Boot, Spring Cloud, Spring Security
- PostgreSQL / InfluxDB
- Docker, Docker Compose
- Python (data analysis and chart generation)
- Keycloak (user authentication and management)
- OpenAPI (REST documentation)
- TheCocktailDB API

---

## 🧪 Testing

Each microservice includes unit and integration tests using **AssertJ** and **Spring Test**. Sample data is available via `data.sql` or Java initializer.

---

<sub>🇵🇱 Polish version below</sub>

---

# 🍹 BarMate

BarMate to mikroserwisowa aplikacja oparta na technologii Java (Spring Boot + Spring Cloud), która pomaga użytkownikom zarządzać domowym barem: składnikami, przepisami na drinki, listami zakupów oraz analizą użycia. System wykorzystuje również zewnętrzne API (TheCocktailDB, OpenAI) oraz skrypty w Pythonie do generowania raportów i wizualizacji danych.

## 🎯 Cel projektu

Ułatwienie organizacji zasobów domowego baru przez automatyzację przepisów, planowanie zakupów, personalizację preferencji i monitorowanie zużycia składników.

## ✅ Architektura i główne moduły

Projekt składa się z wielu mikroserwisów, z których każdy odpowiada za inną funkcjonalność:

### 📦 Core Microservices

- `inventory-service` – zarządzanie składnikami i stanem magazynowym
- `recipe-service` – baza przepisów i sugestie drinków
- `shopping-service` – generowanie list zakupów
- `user-service` – preferencje użytkownika, historia, autoryzacja
- `analytics-service` – statystyki, wykresy i analiza kosztów

### 🔧 Shared Infrastructure

- `config-server` – centralna konfiguracja (Spring Cloud Config)
- `discovery-service` – rejestracja usług (Eureka)
- `gateway-service` – brama API (routing, uwierzytelnianie)
- `auth-service` – integracja z Keycloak
- `database-setup` – konteneryzacja baz danych (Docker)

---

## 📦 inventory-service – składniki

Zarządza stanem składników w barze oraz umożliwia filtrowanie i integrację z innymi usługami.

**Funkcje:**

- CRUD dla składników (REST API)
- Filtrowanie po nazwie i kategorii (ALCOHOL, MIXER, FRUIT, OTHER)
- Walidacja danych i dokumentacja OpenAPI
- Testy z użyciem AssertJ

---

## 📦 recipe-service – przepisy i sugestie

Obsługuje przepisy drinków oraz integrację z zewnętrznym API (TheCocktailDB).

**Funkcje:**

- Tworzenie i edycja przepisów z listą składników
- Filtrowanie po nazwie, tagach, typie alkoholu
- Import i synchronizacja z TheCocktailDB
- Logika sugestii drinków na podstawie dostępnych składników
- Testy i dokumentacja OpenAPI

---

## 📦 shopping-service – lista zakupów

Generuje listy zakupów na podstawie brakujących składników z wybranych przepisów.

**Funkcje:**

- CRUD dla list i pozycji zakupowych
- Obliczanie braków na podstawie inventory i recipe-service
- Możliwość dodawania/odznaczania pozycji ręcznie
- Testy i dokumentacja OpenAPI

---

## 📦 user-service – użytkownicy i personalizacja

Zarządza danymi użytkownika, preferencjami i historią drinków. Integruje się z Keycloak.

**Funkcje:**

- Rejestracja i logowanie z Keycloak (JWT)
- Edycja preferencji smakowych
- Ulubione przepisy i historia przygotowań
- Autoryzacja ról i dostępów
- Testy i dokumentacja OpenAPI

---

## 📦 analytics-service – analizy i wykresy

Zbiera dane statystyczne i integruje się ze skryptami Pythona do generowania raportów.

**Funkcje:**

- Zbieranie danych o przygotowywanych drinkach i zużyciu składników
- Analiza kosztów i częstotliwości użycia
- Generowanie wykresów i raportów (np. top 5 drinków, koszty tygodniowe)
- Integracja z PythonChartService
- Testy i dokumentacja OpenAPI

---

## ⚙️ Technologie

- Java 17, Spring Boot, Spring Cloud, Spring Security
- PostgreSQL / InfluxDB
- Docker, Docker Compose
- Python (analiza danych i generowanie wykresów)
- Keycloak (uwierzytelnianie i zarządzanie użytkownikami)
- OpenAPI (dokumentacja REST)
- TheCocktailDB API

---

## 🧪 Testowanie

Każdy mikroserwis zawiera testy jednostkowe i integracyjne wykorzystujące **AssertJ** i **Spring Test**. Dane przykładowe dostępne są poprzez `data.sql` lub Java initializer.
