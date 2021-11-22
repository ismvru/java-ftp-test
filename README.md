# java-ftp-test

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
 -d,--debug               Enable debug output
 -fo,--ftp-only           Make request for plane FTP only
 -fso,--ftps-only         Make request for FTPs only
 -n,--port-number <arg>   FTP Port
 -p,--password <arg>      FTP Password
 -s,--server <arg>        Server DNS address or IP
 -sfo,--sftp-only         Make request for SFTP only
 -u,--user <arg>          FTP Username
```

Пример вывода

```bash
java -jar ./ftptest-VERSION-jar-with-dependencies.jar -s 127.0.0.1 -u username -p password
[main] INFO tech.isaev.ftptest.Main - Connect as user to 127.0.0.1:21
[main] INFO tech.isaev.ftptest.Main - Plain FTP
[main] ERROR tech.isaev.ftptest.Main - Plain FTP - [31mFAILED[0m
[main] INFO tech.isaev.ftptest.Main - FTP with SSL/TLS
[main] ERROR tech.isaev.ftptest.Main - FTP with SSL/TLS - [31mFAILED[0m
[main] INFO tech.isaev.ftptest.Main - SFTP
[main] INFO net.schmizz.sshj.transport.random.JCERandom - Creating new SecureRandom.
[main] ERROR tech.isaev.ftptest.Main - SFTP - [31mFAILED[0m
```
