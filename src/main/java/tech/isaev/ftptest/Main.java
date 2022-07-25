package tech.isaev.ftptest;

import java.io.IOException;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
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

// Main class. Just execute it.
public class Main {
  // Colorize output
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  public static final String STATUS_FAILED = ANSI_RED + "FAILED" + ANSI_RESET;
  public static final String STATUS_OK = ANSI_GREEN + "OK" + ANSI_RESET;
  public static final String STATUS_SKIPPED = ANSI_YELLOW + "SKIPPED" + ANSI_RESET;
  public static final String ACTION_FTP = "FTP";
  public static final String ACTION_FTPS = "FTPS";
  public static final String ACTION_SFTP = "SFTP";

  // Plain FTP
  private static void ftp(String server, Integer port,
                          String user, String password, Boolean verbose) throws IOException {
    if (Boolean.TRUE.equals(verbose)) {
      System.out.println(ACTION_FTP + ": Init FTP Client");
    }
    FTPClient ftpClient = new FTPClient();
    if (Boolean.TRUE.equals(verbose)) {
      System.out.println(ACTION_FTP + ": Connect to FTP");
    }
    ftpClient.connect(server, port);
    if (Boolean.TRUE.equals(verbose)) {
      System.out.println(ACTION_FTP + ": Login to FTP");
    }
    ftpClient.login(user, password);
    if (Boolean.TRUE.equals(verbose)) {
      System.out.println(ACTION_FTP + ": Disconnect from FTP");
    }
    ftpClient.disconnect();
    ftpClient.close();
  }

  // FTP with SSL
  private static void ftps(String server, Integer port,
                           String user, String password, Boolean verbose) throws IOException {
    if (Boolean.TRUE.equals(verbose)) {
      System.out.println(ACTION_FTPS + ": Init FTPS Client");
    }
    FTPSClient ftpsClient = new FTPSClient(true);
    if (Boolean.TRUE.equals(verbose)) {
      System.out.println(ACTION_FTPS + ": Connect to FTPS");
    }
    ftpsClient.connect(server, port);
    if (Boolean.TRUE.equals(verbose)) {
      System.out.println(ACTION_FTPS + ": Login to FTPS");
    }
    ftpsClient.login(user, password);
    if (Boolean.TRUE.equals(verbose)) {
      System.out.println(ACTION_FTPS + ": Disconnect from FTPS");
    }
    ftpsClient.disconnect();
    ftpsClient.close();
  }

  // SFTP (SSH)
  private static void sftp(String server, Integer port,
                           String user, String password, Boolean verbose) throws IOException {
    if (Boolean.TRUE.equals(verbose)) {
      System.out.println(ACTION_SFTP + ": Init SSH Client");
    }
    SSHClient sshClient = new SSHClient();
    sshClient.setTimeout(5);
    sshClient.setConnectTimeout(5);
    sshClient.addHostKeyVerifier(new PromiscuousVerifier());
    if (Boolean.TRUE.equals(verbose)) {
      System.out.println(ACTION_SFTP + ": Connect to SSH");
    }
    sshClient.connect(server, port);
    if (Boolean.TRUE.equals(verbose)) {
      System.out.println(ACTION_SFTP + ": Login to SFTP");
    }
    sshClient.authPassword(user, password);
    if (Boolean.TRUE.equals(verbose)) {
      System.out.println(ACTION_SFTP + ": Init SFTP Client");
    }
    SFTPClient sftpClient = sshClient.newSFTPClient();
    if (Boolean.TRUE.equals(verbose)) {
      System.out.println(ACTION_SFTP + ": ls /");
    }
    sftpClient.ls("/");
    if (Boolean.TRUE.equals(verbose)) {
      System.out.println(ACTION_SFTP + ": Disconnect from SFTP");
    }
    sftpClient.close();
    if (Boolean.TRUE.equals(verbose)) {
      System.out.println(ACTION_SFTP + ": Disconnect from SSH");
    }
    sshClient.close();
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
    Option verboseFlag = new Option("v", "verbose", false, "Enable verbose output");
    verboseFlag.setRequired(false);
    options.addOption(verboseFlag);

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

    // More verbose output
    Boolean verbose;
    if (cmd.hasOption("v")) {
      verbose = Boolean.TRUE;
    } else {
      verbose = Boolean.FALSE;
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

    // Main app work
    System.out.println("Connect as " + ftpUser + " to " + ftpServer + ":"
        + ftpPort + " FTP(s) and to " + ftpServer + ":" + sftpPort + " SFTP");

    // Plain FTP
    System.out.println("===== " + ACTION_FTP + " =====");
    if (doFtp) {
      try {
        ftp(ftpServer, ftpPort, ftpUser, ftpPassword, verbose);
        System.out.println(ACTION_FTP + " - " + STATUS_OK);
      } catch (IOException e) {
        System.out.println(ACTION_FTP + " - " + STATUS_FAILED);
      }
    } else {
      System.out.println(ACTION_FTP + " - " + STATUS_SKIPPED);
    }

    // FTP with SSL/TLS
    System.out.println("===== " + ACTION_FTPS + " =====");
    if (doFtps) {
      try {
        ftps(ftpServer, ftpPort, ftpUser, ftpPassword, verbose);
        System.out.println(ACTION_FTPS + " - " + STATUS_OK);
      } catch (IOException e) {
        System.out.println(ACTION_FTPS + " - " + STATUS_FAILED);
      }
    } else {
      System.out.println(ACTION_FTPS + " - " + STATUS_SKIPPED);
    }

    // SFTP
    System.out.println("===== " + ACTION_SFTP + " =====");
    if (doSftp) {
      try {
        sftp(ftpServer, sftpPort, ftpUser, ftpPassword, verbose);
        System.out.println(ACTION_SFTP + " - " + STATUS_OK);
      } catch (IOException e) {
        System.out.println(ACTION_SFTP + " - " + STATUS_FAILED);
      }
    } else {
      System.out.println(ACTION_SFTP + " - " + STATUS_SKIPPED);
    }

  }
}
