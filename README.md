# DISYS Energy Community

## Setup

### Voraussetzungen

Folgende Programme müssen installiert sein:

* Java 17
* Maven
* Docker
* Docker Compose
* IntelliJ IDEA (empfohlen)

---

## Projekt starten

### 1. Projekt klonen

```bash
git clone https://github.com/rafaelkhan/disys-energy-community-w.git 
cd disys-energy-community-w
```

---

### 2. Maven Dependencies laden

Im Hauptordner:

```bash
mvn clean install
```

Falls notwendig die einzelnen Services separat builden.

---

## Docker starten

In den docker Ordner wechseln:

```bash
cd docker
```

Danach Docker Compose starten:

```bash
docker compose up --build
```

Container stoppen:

```bash
docker compose down
```

---

# Services

Das Projekt besteht aus mehreren Services:

* current-percentage-service
* usage-service
* energy-producer
* energy-user
* rest-api
* gui

Jeder Service besitzt ein eigenes `pom.xml`.

---

# Services lokal starten

In den jeweiligen Service wechseln und starten:

```bash
mvn spring-boot:run
```

Beispiel:

```bash
cd rest-api
mvn spring-boot:run
```

---

# GUI starten

In den gui Ordner wechseln:

```bash
cd gui
```

Danach starten:

```bash
mvn javafx:run
```

---

# RabbitMQ

RabbitMQ wird für die Kommunikation zwischen den einzelnen Services verwendet.

RabbitMQ läuft gemeinsam mit der PostgreSQL-Datenbank über Docker Compose.

Verwendete Ports:

```text
5672 -> RabbitMQ
15672 -> RabbitMQ Management UI
5432 -> PostgreSQL
```

RabbitMQ Management UI:

```text
http://localhost:15672
```

PostgreSQL Konfiguration:

```text
Database: energydb
User: disysuser
Password: disyspw
```
---

# Technologien

* Java
* Spring Boot
* JavaFX
* Maven
* Docker
* RabbitMQ
* REST API

---

# Projektstruktur

```text
disys-energy-community-w
│
├── current-percentage-service
├── docker
├── energy-producer
├── energy-user
├── gui
├── rest-api
└── usage-service
```
