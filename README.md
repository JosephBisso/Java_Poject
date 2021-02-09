# Prüfung Programmieren 1 - Wintersemester 20/21

Die detaillierte Aufgabenbeschreibung finden Sie als PDF im StudIP.

## Ordnerstruktur
- src/main/java/ias: Vorgegebene Interfaces und Klassen. Dürfen **nicht** modifiziert werden!
- src/main/java/student: Ordner für Ihren Code.
- src/main/resources: Hier liegen die Checkstyle-Regeln. Diese können Sie automatisiert ausführen (siehe Ausführen & Testen).
- src/main/test/ias: Vorgegebene JUnit Tests zur Prüfung Ihres Codes.
- src/main/test/student: Hier können und sollten Sie weitere Tests anlegen, um Ihren Code selbstständig zu testen.
- Die Ordner ias und student werden in Java packages genannt. 
  Näheres hierzu finden Sie unter: https://docs.oracle.com/javase/tutorial/java/package/packages.html

## Ausführen & Testen
- ``./gradlew checkstyleMain --rerun-tasks``
  - Führt die Checkstyle-Regeln für alle Java-Dateien im Verzeichnis src/main/java/student aus.
- ``./gradlew test``
  - Führt alle JUnit Tests aus.
- ``./gradlew test --tests '*defineCard*'``
  - Führt nur die Tests für die Methode defineCard aus
- ``./gradlew check``
  - Führt die Checkstyle-Regeln und alle JUnit Tests aus.
- ``./gradlew jar``
  - Kompiliert das Projekt.
  - Das kompilierte Projekt kann mit dem Kommando ``java -jar build/libs/pruefung.jar`` ausgeführt werden. Dabei wird die main-Methode in der student/Main.java Klasse gestartet.
  - Es wird empfohlen ihren Code ausschließlich über reproduzierbare JUnit Tests zu prüfen.
- ``./gradlew run --console=plain --quiet``
  - Kompiliert und startet die main-Methode in der student/Main.java Klasse.

## Viel Erfolg!
