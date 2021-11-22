# java-ftp-test

Проверяет, какой протокол мы используем

* FTP
* FTPS
* SFTP

## Сборка

```bash
mvn clean package
```

## Использование

```bash
java -jar ./ftptest-VERSION-jar-with-dependencies.jar -s ftp.example.com -u username -p password [-n 2121] [-d]
 -d,--debug               Enable debug output
 -n,--port-number <arg>   FTP Port (Only for FTP and FTPS. Not sFTP)
 -p,--password <arg>      FTP Password
 -s,--server <arg>        Server DNS address or IP
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
