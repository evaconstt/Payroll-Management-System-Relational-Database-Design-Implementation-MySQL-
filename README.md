# Design and Implementation of a Payroll Management System for the University of Crete

A relational database system that automates part of the payroll processes
of the University of Crete, developed as a team project for the course
ΗΥ-360 (Files and Databases), Department of Computer Science, University
of Crete.

## Overview

The system manages employee records, salary rules, and monthly payroll
payments for two categories of staff (administrative and academic),
each of which can be either permanent or contract-based. Salaries and
family allowances are computed according to a defined set of business
rules, and the system supports both predefined and ad hoc queries over
the payroll data.

## Tech Stack

- **Language:** Java
- **Database:** MySQL / MariaDB
- **Connectivity:** JDBC
- **Interface:** Java Swing

## Phase I — Conceptual & Logical Design

This phase covers the design of the database, documented in `docs/`:

- Entity-Relationship (ER) diagram
- Entity and relationship attributes, with primary keys
- Cardinality constraints
- Translation of the ER model into the relational model
- DDL statements for the resulting relations
- Integrity constraints and functional dependencies
- Key derivation from functional dependencies
- Normalization to Third Normal Form (3NF), preserving functional
  dependencies with no information loss
- SQL query design for the required reports
- Pseudocode for the core procedures

## Phase II — Implementation

This phase covers the working system:

- Java application (JDBC) implementing the core procedures:
  - Hiring a new permanent employee
  - Signing a new fixed-term contract
  - Updating employee information
  - Changing base salaries and allowances (no decreases allowed)
  - Termination / retirement of a permanent employee
  - Monthly payroll payment, with a detailed payroll statement per
    staff category
- Support for both ad hoc queries and predefined reports:
  - Payroll statement per staff category
  - Max / min / average salary per staff category
  - Average salary and allowance increase over a time period
  - Employee-specific payroll details
  - Total payroll amount per staff category
- *(Optional)* SQL views simplifying and organizing payroll reports

## Project Structure

\`\`\`
├── src/          # Java source code
├── sql/          # Database schema (DDL)
├── docs/         # Design report, ER diagram
├── lib/          # mysql-connector.jar
\`\`\`

## Setup & Usage

1. Install MySQL/MariaDB and create a database.
2. Run the script in `sql/` to create the schema.
3. Add `lib/mysql-connector.jar` to the project's classpath.
4. Update the database connection settings in the source code
   (host, database name, credentials).
5. Run the application from `src/`.

## Team

Developed by a team of 3 as coursework for ΗΥ-360, Winter Semester 2025.
