# Beispielprojekt: Architekturrichtlinien mit ArchUnit

Dieses Projekt demonstriert die Anwendung von Architekturrichtlinien in einer Kotlin/Java-Codebasis mithilfe von [ArchUnit](https://www.archunit.org/).

## Projektstruktur

- **controller**: Präsentationslogik
- **service**: Geschäftslogik
- **repository**: Datenzugriff
- **model**: Domänenklassen

## Technologien

- Kotlin & Java
- Gradle
- JUnit 5
- ArchUnit

## Architekturrichtlinien

Die Tests in `ArchitectureTest.kt` stellen sicher, dass:
- Controller nur von Service, Model, Java/Kotlin abhängen
- Service nur von Repository, Model, Java/Kotlin abhängen
- Repository nicht von Service oder Controller abhängt
- Keine Klasse von Controller abhängt

## Ausführen der Tests

```bash
./gradlew test
