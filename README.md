# java-ftp-test

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=ismvru_java-ftp-test&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=ismvru_java-ftp-test)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=ismvru_java-ftp-test&metric=bugs)](https://sonarcloud.io/summary/new_code?id=ismvru_java-ftp-test)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=ismvru_java-ftp-test&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=ismvru_java-ftp-test)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=ismvru_java-ftp-test&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=ismvru_java-ftp-test)
![Docker Pulls](https://img.shields.io/docker/pulls/ismv/java-ftp-test)
![Docker Image Size (tag)](https://img.shields.io/docker/image-size/ismv/java-ftp-test/latest)

Проверяет, какой протокол мы используем

* FTP
* FTPS
* SFTP

## Сборка

### Java

```bash
mvn clean package
```

### Docker

```bash
docker build .
```

## Артефакты

* `target/ftptest-VERSION-JAVA_VERSION-jar-with-dependencies.jar` - JAR со всеми зависимостями
* `target/ftptest-VERSION-JAVA_VERSION.jar` - JAR без зависимостей

**WARN** для использования JAR без зависимостей нужно, что бы в classpath были все зависимости из `pom.xml` (см. project.dependencies)

## Использование

TIP! Использование в Docker аналогично, но вместо `java -jar ./ftptest-VERSION-JAVA_VERSION-jar-with-dependencies.jar` указывается `docker run -it IMAGE:TAG`

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
