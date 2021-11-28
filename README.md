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

```bash
mvn clean package
```

## Артефакты

* `target/ftptest-VERSION-jar-with-dependencies.jar` - JAR со всеми зависимостями
* `target/ftptest-VERSION.jar` - JAR без зависимостей

**WARN** для использования JAR без зависимостей нужно, что бы в classpath были все зависимости из `pom.xml` (см. project.dependencies)

## Использование

```bash
java -jar ./ftptest-VERSION-jar-with-dependencies.jar -s ftp.example.com -u username -p password [-n 2121] [-d] [-fo] [-fso] [-sfo]
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
java -jar ./ftptest-VERSION-jar-with-dependencies.jar -s 127.0.0.1 -u username -p password
Connect as username to 127.0.0.1:21 FTP(s) and to 127.0.0.1:22 SFTP
===== Plain FTP =====
Plain FTP - FAILED
===== FTP with SSL/TLS =====
FTP with SSL/TLS - FAILED
===== SFTP =====
SFTP - OK
```

Пример вывода с `-v`

```bash
java -jar ./ftptest-VERSION-jar-with-dependencies.jar -s 127.0.0.1 -u username -p password
Connect as username to 127.0.0.1:21 FTP(s) and to 127.0.0.1:22 SFTP
===== Plain FTP =====
FTP: Init FTP Client
FTP: Connect to FTP
Plain FTP - FAILED
===== FTP with SSL/TLS =====
FTPS: Init FTPS Client
FTPS: Connect to FTPS
FTP with SSL/TLS - FAILED
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