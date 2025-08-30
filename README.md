# Remedy Integration Platform

A Spring Boot REST API application that provides seamless integration between monitoring systems and BMC Remedy for automated incident creation and management.

## Overview

This platform serves as a bridge between monitoring systems and BMC Remedy's incident management system, enabling automatic incident creation, tracking, and management. The application is built with a modular architecture supporting both legacy and modern integration patterns.

## Features

- **REST API Integration**: Provides RESTful endpoints for incident creation from monitoring systems
- **BMC Remedy Integration**: Native integration with BMC Remedy using arAPI
- **Modular Architecture**: Pluggable module system for extending integrations
- **Secure Configuration**: AES encryption for sensitive configuration data
- **Multi-Environment Support**: Separate configurations for dev, test, and production
- **Connection Pooling**: Efficient connection management for BMC Remedy
- **Comprehensive Logging**: Structured logging with configurable levels
- **Spring Boot Framework**: Modern Java application framework with embedded server
- **WAR Deployment**: Supports both standalone and application server deployment

## Technical Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **BMC Remedy API (arAPI)**
- **Maven** for build management
- **Logback** for logging
- **Apache HttpClient** for HTTP communications

## Architecture

### Package Structure

```
net.cybermak.integration/
├── api/
│   ├── controller/          # REST controllers
│   ├── service/            # Business logic services
│   ├── model/requests/     # Request DTOs
│   └── utility/           # Utility classes (AES encryption)
├── config/                # Configuration management
├── core/                  # Core framework components
├── modules/               # Integration modules
└── remedy/                # BMC Remedy integration layer
    ├── connection/        # Connection management
    ├── form/              # Form handlers
    └── config/            # Remedy-specific configuration
```

### Key Components

- **SpringRestApplication**: Main Spring Boot application class
- **CreateIncidentController**: Primary REST endpoint for incident creation
- **CreateIncidentService**: Core business logic for Remedy integration
- **ModuleRegistry**: Registry for pluggable integration modules
- **RemedyConnectionManager**: Manages connections to BMC Remedy servers
- **AES**: Encryption utilities for sensitive configuration

## Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Access to BMC Remedy server
- Network connectivity to target Remedy instance

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd remedy-integration-platform
   ```

2. **Configure the application**
   ```bash
   cp src/main/resources/remedy.properties.template src/main/resources/remedy.properties
   ```
   
   Edit `remedy.properties` with your BMC Remedy server details:
   ```properties
   remedy.connection.server-name=your-remedy-server
   remedy.connection.username=your-username
   remedy.connection.password=your-password
   remedy.connection.port=6000
   ```

3. **Build the application**
   ```bash
   mvn clean package
   ```

4. **Run the application**
   ```bash
   # As Spring Boot application
   mvn spring-boot:run
   
   # Or deploy the WAR file
   cp target/remedy-integration-platform-1.0.0-SNAPSHOT.war /path/to/server/webapps/
   ```

## Configuration

### Environment-Specific Configuration

The application supports multiple environments through profile-specific configuration:

- `application-dev.yml` - Development environment
- `application-test.yml` - Testing environment  
- `application-prod.yml` - Production environment

### BMC Remedy Configuration

Configure your BMC Remedy connection in `remedy.properties`:

```properties
# Primary connection
remedy.connection.server-name=your-server
remedy.connection.username=your-username
remedy.connection.password=your-encrypted-password
remedy.connection.port=6000

# Connection pool settings
remedy.connection.pool.max-size=10
remedy.connection.pool.min-idle=2
remedy.connection.pool.max-wait-time=30000

# Retry configuration
remedy.connection.retry.max-attempts=3
remedy.connection.retry.delay=1000
```

### Form Mapping

Configure BMC Remedy form mappings:

```properties
# Form names
remedy.forms.incident=HPD:Help Desk
remedy.forms.workorder=WOI:WorkOrder

# Field mappings
remedy.fields.incident.summary=Short_Description
remedy.fields.incident.description=Detailed_Decription
remedy.fields.incident.priority=Priority
```

## API Documentation

### Create Incident Endpoint

**Endpoint**: `POST /api/remedyITSM/createIncident`

**Request Headers**:
```
Content-Type: application/json
```

**Request Body**:
```json
{
  "key": "10",
  "summary": "Server Down Alert",
  "description": "Critical server outage detected",
  "priority": "1-Critical",
  "submitter": "MonitoringSystem",
  "impact": "1-Extensive/Widespread",
  "urgency": "1-Critical"
}
```

**Response**:
```json
{
  "status": "success",
  "incidentId": "INC000001234567",
  "message": "Incident created successfully"
}
```

### Generic Integration Endpoint

**Endpoint**: `POST /api/integration/{module}`

Supports pluggable integration modules for extensible functionality.

## Build Commands

```bash
# Clean and build
mvn clean package

# Run tests
mvn test

# Run application locally
mvn spring-boot:run

# Compile only
mvn compile

# Generate WAR file
mvn clean package
# Output: target/remedy-integration-platform-1.0.0-SNAPSHOT.war
```