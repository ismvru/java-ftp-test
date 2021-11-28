# java-ftp-test

[![Build Status](https://ci.isaev.tech/api/badges/IsaevTech/java-ftp-test/status.svg)](https://ci.isaev.tech/IsaevTech/java-ftp-test)

[![Bugs](https://sonar.isaev.tech/api/project_badges/measure?project=IsaevTech%3Ajava-ftp-test&metric=bugs&token=3fed23cafcd4fa893571f94b6d2e9717ec6b2c4d)](https://sonar.isaev.tech/dashboard?id=IsaevTech%3Ajava-ftp-test)
[![Code Smells](https://sonar.isaev.tech/api/project_badges/measure?project=IsaevTech%3Ajava-ftp-test&metric=code_smells&token=3fed23cafcd4fa893571f94b6d2e9717ec6b2c4d)](https://sonar.isaev.tech/dashboard?id=IsaevTech%3Ajava-ftp-test)
[![Vulnerabilities](https://sonar.isaev.tech/api/project_badges/measure?project=IsaevTech%3Ajava-ftp-test&metric=vulnerabilities&token=3fed23cafcd4fa893571f94b6d2e9717ec6b2c4d)](https://sonar.isaev.tech/dashboard?id=IsaevTech%3Ajava-ftp-test)
[![Quality Gate Status](https://sonar.isaev.tech/api/project_badges/measure?project=IsaevTech%3Ajava-ftp-test&metric=alert_status&token=3fed23cafcd4fa893571f94b6d2e9717ec6b2c4d)](https://sonar.isaev.tech/dashboard?id=IsaevTech%3Ajava-ftp-test)

Проверяет, какой протокол мы используем

* FTP
* FTPS
* SFTP

## Сборка

Так как у нас 3 LTS релиза Java (8, 11 и 17) - собираем под все

TODO: [ ] Убрать Java 8 после марта 2022 г.

TODO: [ ] Убрать Java 11 после сентября 2023 г.

TODO: [x] Добавить Java 17 после того, как станет доступен Docker образ maven с Java 17.

```bash
mvn -f pom-1.8.xml clean package  # Java 8
mvn -f pom-11.xml clean package  # Java 11
mvn -f pom-17.xml clean package  # Java 17
```

## Артефакты

* `target/ftptest-VERSION-JAVA_VERSION-jar-with-dependencies.jar` - JAR со всеми зависимостями
* `target/ftptest-VERSION-JAVA_VERSION.jar` - JAR без зависимостей

**WARN** для использования JAR без зависимостей нужно, что бы в classpath были все зависимости из `pom.xml` (см. project.dependencies)

## Использование

```bash
java -jar ./ftptest-VERSION-JAVA_VERSION-jar-with-dependencies.jar -s ftp.example.com -u username -p password [-n 2121] [-d] [-fo] [-fso] [-sfo]
 -fo,--ftp-only           Make request for plain FTP only
 -fso,--ftps-only         Make request for FTPs only
 -n,--port-number <arg>   FTP Port
 -p,--password <arg>      FTP Password
 -s,--server <arg>        Server DNS address or IP
 -sfo,--sftp-only         Make request for SFTP only
 -u,--user <arg>          FTP Username
 -v,--verbose             Enable verbose output
```

Пример вывода

```bash
java -jar ./ftptest-VERSION-JAVA_VERSION-jar-with-dependencies.jar -s 127.0.0.1 -u username -p password
Connect as username to 127.0.0.1:21 FTP(s) and to 127.0.0.1:22 SFTP
===== FTP =====
FTP - FAILED
===== FTPS =====
FTPS - FAILED
===== SFTP =====
SFTP - OK

```

Пример вывода с `-v`

```bash
java -jar ./ftptest-VERSION-JAVA_VERSION-jar-with-dependencies.jar -s 127.0.0.1 -u username -p password
Connect as username to 127.0.0.1:21 FTP(s) and to 127.0.0.1:22 SFTP
===== FTP =====
FTP: Init FTP Client
FTP: Connect to FTP
FTP - FAILED
===== FTPS =====
FTPS: Init FTPS Client
FTPS: Connect to FTPS
FTPS - FAILED
===== SFTP =====
SFTP: Init SSH Client
SFTP: Connect to SSH
SFTP: Login to SFTP
SFTP: Init SFTP Client
SFTP: ls /
SFTP: Disconnect from SFTP
SFTP: Disconnect from SSH
SFTP - OK
```
