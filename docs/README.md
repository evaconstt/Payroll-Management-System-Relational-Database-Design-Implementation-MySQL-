###Phase I — Conceptual Modeling

The first phase covers the design of a complete conceptual model. The
report includes:

- A complete Entity-Relationship (ER) diagram
- Attributes (name, type) of all entities and relationships
- Primary keys
- Explanations of non-obvious attributes and relationships
- Cardinality constraints
- Translation of the model into the relational model
- DDL (Data Definition Language) statements for the resulting relations
- Integrity constraints and functional dependencies
- Key derivation based on functional dependencies
- Normalization to Third Normal Form (3NF), preserving functional
  dependencies with no loss of information
- SQL query descriptions for the required database queries
- Pseudocode description of the procedures

###Phase II — Implementation

The second phase covers the implementation of the system. The report
includes:

- Sample results from executing the procedures
- A user manual for the application
- A description of the implementation's limitations and possible
  improvements
- The full source code implementing the procedures defined in Phase I

The system was implemented in **Java**, using **JDBC** for database
connectivity, with **MySQL/MariaDB** as the database management system
and a graphical interface built with **Java Swing**.
