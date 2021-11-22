package tech.isaev.ftptest;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.apache.commons.cli.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    // Colorize output
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    // Plain FTP
    private static void ftp(String server, Integer port, String user, String password) throws IOException {
        FTPClient ftp_client = new FTPClient();
        ftp_client.connect(server, port);
        ftp_client.login(user, password);
        ftp_client.disconnect();
    }

    // FTP with SSL
    private static void ftps(String server, Integer port, String user, String password) throws IOException {
        FTPSClient ftps_client = new FTPSClient(true);
        ftps_client.connect(server, port);
        ftps_client.login(user, password);
        ftps_client.disconnect();
    }

    // SFTP (SSH)
    private static void sftp(String server, String user, String password) throws IOException {
        SSHClient sftp_client = new SSHClient();
        sftp_client.addHostKeyVerifier(new PromiscuousVerifier());
        sftp_client.connect(server);
        sftp_client.authPassword(user, password);
        sftp_client.close();
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
        Option port = new Option("n", "port-number", true, "FTP Port (Only for FTP and FTPS. Not sFTP)");
        port.setRequired(false);
        options.addOption(port);

        // Debug
        Option debug = new Option("d", "debug", false, "Enable debug output");
        debug.setRequired(false);
        options.addOption(debug);

        // Parse options
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("ftptest", options);
            System.exit(1);
        }

        // Get values - required
        String ftp_server = cmd.getOptionValue("server");
        String ftp_user = cmd.getOptionValue("user");
        String ftp_password = cmd.getOptionValue("password");

        // Get optional port name, if not passed - default == 21
        int ftp_port;
        try {
            ftp_port = Integer.parseInt(cmd.getOptionValue("port-number"));
        } catch (java.lang.NumberFormatException e) {
            ftp_port = 21;
        }

        // If we see -d key - enable debug. Need more debug.
        boolean log_debug = cmd.hasOption("d");
        if (log_debug == Boolean.TRUE) {
            System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "TRACE");
        }

        // We need to init logger
        final Logger logger = LoggerFactory.getLogger(Main.class);

        // Main app work
        logger.info("Connect as " + ftp_user + " to " + ftp_server + ":" + ftp_port);

        // Plain FTP
        try {
            logger.info("Plain FTP");
            ftp(ftp_server, ftp_port, ftp_user, ftp_password);
            logger.info("Plain FTP - " + ANSI_GREEN + "OK" + ANSI_RESET);
        } catch (IOException e) {
            logger.error("Plain FTP - " + ANSI_RED + "FAILED" + ANSI_RESET);
            logger.debug(Arrays.toString(e.getStackTrace()));
        }

        // FTP with SSL/TLS
        try {
            logger.info("FTP with SSL/TLS");
            ftps(ftp_server, ftp_port, ftp_user, ftp_password);
            logger.info("FTP with SSL/TLS - " + ANSI_GREEN + "OK" + ANSI_RESET);
        } catch (IOException e) {
            logger.error("FTP with SSL/TLS - " + ANSI_RED + "FAILED" + ANSI_RESET);
            logger.debug(Arrays.toString(e.getStackTrace()));
        }

        // SFTP
        try {
            logger.info("SFTP");
            sftp(ftp_server, ftp_user, ftp_password);
            logger.info("SFTP - " + ANSI_GREEN + "OK" + ANSI_RESET);
        } catch (IOException e) {
            logger.error("SFTP - " + ANSI_RED + "FAILED" + ANSI_RESET);
            logger.debug(Arrays.toString(e.getStackTrace()));
        }
    }
}
