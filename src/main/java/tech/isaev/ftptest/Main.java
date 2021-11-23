package tech.isaev.ftptest;

import java.io.IOException;
import java.util.Arrays;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Main class. Just execute it.
public class Main {
  // Colorize output
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String STATUS_FAILED = ANSI_RED + "FAILED" + ANSI_RESET;
  public static final String STATUS_OK = ANSI_GREEN + "OK" + ANSI_RESET;

  // Plain FTP
  private static void ftp(String server, Integer port,
                          String user, String password) throws IOException {
    FTPClient ftpClient = new FTPClient();
    ftpClient.connect(server, port);
    ftpClient.login(user, password);
    ftpClient.disconnect();
  }

  // FTP with SSL
  private static void ftps(String server, Integer port,
                           String user, String password) throws IOException {
    FTPSClient ftpsClient = new FTPSClient(true);
    ftpsClient.connect(server, port);
    ftpsClient.login(user, password);
    ftpsClient.disconnect();
  }

  // SFTP (SSH)
  private static void sftp(String server, Integer port,
                           String user, String password) throws IOException {
    SSHClient sftpClient = new SSHClient();
    sftpClient.addHostKeyVerifier(new PromiscuousVerifier());
    sftpClient.connect(server, port);
    sftpClient.authPassword(user, password);
    sftpClient.close();
  }

  // Main class
  public static void main(String[] args) {
    // Parse arguments
    Options options = new Options();

    // Server address
    Option server = new Option("s", "server", true, "Server DNS address or IP");
    server.setRequired(true);
    options.addOption(server);

    // Username
    Option user = new Option("u", "user", true, "FTP Username");
    user.setRequired(true);
    options.addOption(user);

    // Password
    Option password = new Option("p", "password", true, "FTP Password");
    password.setRequired(true);
    options.addOption(password);

    // FTP port
    Option port = new Option("n", "port-number", true, "FTP Port");
    port.setRequired(false);
    options.addOption(port);

    // Debug
    Option debugFlag = new Option("d", "debug", false, "Enable debug output");
    debugFlag.setRequired(false);
    options.addOption(debugFlag);

    // Plain FTP only
    Option ftpFlag = new Option("fo", "ftp-only", false, "Make request for plain FTP only");
    ftpFlag.setRequired(false);
    options.addOption(ftpFlag);

    // FTPs only
    Option ftpsFlag = new Option("fso", "ftps-only", false, "Make request for FTPs only");
    ftpsFlag.setRequired(false);
    options.addOption(ftpsFlag);

    // FTPs only
    Option sftpFlag = new Option("sfo", "sftp-only", false, "Make request for SFTP only");
    sftpFlag.setRequired(false);
    options.addOption(sftpFlag);

    // Parse options
    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      formatter.printHelp("ftptest", options);
      System.exit(1);
    }

    // Get values - required
    final String ftpServer = cmd.getOptionValue("server");
    final String ftpUser = cmd.getOptionValue("user");
    final String ftpPassword = cmd.getOptionValue("password");

    // Get optional port name, if not passed - default == 21
    int ftpPort;
    int sftpPort;
    try {
      ftpPort = Integer.parseInt(cmd.getOptionValue("port-number"));
      sftpPort = ftpPort;
    } catch (java.lang.NumberFormatException e) {
      ftpPort = 21;
      sftpPort = 22;
    }

    // If we see -d key - enable debug. Need more debug.
    if (cmd.hasOption("d")) {
      System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
    }

    // We can decide what to do.
    boolean doFtp = Boolean.TRUE;
    boolean doFtps = Boolean.TRUE;
    boolean doSftp = Boolean.TRUE;
    if (cmd.hasOption("ftp-only")) {
      doFtps = Boolean.FALSE;
      doSftp = Boolean.FALSE;
    }
    if (cmd.hasOption("ftps-only")) {
      doFtp = Boolean.FALSE;
      doSftp = Boolean.FALSE;
    }
    if (cmd.hasOption("sftp-only")) {
      doFtp = Boolean.FALSE;
      doFtps = Boolean.FALSE;
    }

    // We need to init logger
    final Logger logger = LoggerFactory.getLogger(Main.class);

    // Main app work
    logger.info("Connect as " + ftpUser + " to " + ftpServer + ":"
        + ftpPort + " FTP(s) and to " + ftpServer + ":" + sftpPort + " SFTP");

    // Plain FTP
    if (doFtp) {
      try {
        logger.info("Plain FTP");
        ftp(ftpServer, ftpPort, ftpUser, ftpPassword);
        logger.info("Plain FTP - " + STATUS_OK);
      } catch (IOException e) {
        logger.error("Plain FTP - " + STATUS_FAILED);
        logger.debug(Arrays.toString(e.getStackTrace()));
      }
    }
    // FTP with SSL/TLS
    if (doFtps) {
      try {
        logger.info("FTP with SSL/TLS");
        ftps(ftpServer, ftpPort, ftpUser, ftpPassword);
        logger.info("FTP with SSL/TLS - " + STATUS_OK);
      } catch (IOException e) {
        logger.error("FTP with SSL/TLS - " + STATUS_FAILED);
        logger.debug(Arrays.toString(e.getStackTrace()));
      }
    }
    // SFTP
    if (doSftp) {
      try {
        logger.info("SFTP");
        sftp(ftpServer, sftpPort, ftpUser, ftpPassword);
        logger.info("SFTP - " + STATUS_OK);
      } catch (IOException e) {
        logger.error("SFTP - " + STATUS_FAILED);
        logger.debug(Arrays.toString(e.getStackTrace()));
      }
    }
  }
}
