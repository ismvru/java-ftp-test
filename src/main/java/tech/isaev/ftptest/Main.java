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
    private static void sftp(String server, Integer port, String user, String password) throws IOException {
        SSHClient sftp_client = new SSHClient();
        sftp_client.addHostKeyVerifier(new PromiscuousVerifier());
        sftp_client.connect(server, port);
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
        Option port = new Option("n", "port-number", true, "FTP Port");
        port.setRequired(false);
        options.addOption(port);

        // Debug
        Option debug_flag = new Option("d", "debug", false, "Enable debug output");
        debug_flag.setRequired(false);
        options.addOption(debug_flag);

        // Plain FTP only
        Option ftp_flag = new Option("ftp-only", false, "Make request for plane FTP only");
        ftp_flag.setRequired(false);
        options.addOption(ftp_flag);

        // FTPs only
        Option ftps_flag = new Option("ftps-only", false, "Make request for FTPs only");
        ftps_flag.setRequired(false);
        options.addOption(ftps_flag);

        // FTPs only
        Option sftp_flag = new Option("sftp-only", false, "Make request for SFTP only");
        sftp_flag.setRequired(false);
        options.addOption(sftp_flag);

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
        int ftp_port, sftp_port;
        try {
            ftp_port = Integer.parseInt(cmd.getOptionValue("port-number"));
            sftp_port = ftp_port;
        } catch (java.lang.NumberFormatException e) {
            ftp_port = 21;
            sftp_port = 22;
        }

        // If we see -d key - enable debug. Need more debug.
        if (cmd.hasOption("d")) {
            System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
        }

        // We can decide what to do.
        boolean do_ftp = Boolean.TRUE, do_ftps = Boolean.TRUE, do_sftp = Boolean.TRUE;
        if (cmd.hasOption("ftp-only")) {
            do_ftps = Boolean.FALSE;
            do_sftp = Boolean.FALSE;
        }
        if (cmd.hasOption("ftps-only")) {
            do_ftp = Boolean.FALSE;
            do_sftp = Boolean.FALSE;
        }
        if (cmd.hasOption("sftp-only")) {
            do_ftp = Boolean.FALSE;
            do_ftps = Boolean.FALSE;
        }

        // We need to init logger
        final Logger logger = LoggerFactory.getLogger(Main.class);

        // Main app work
        logger.info("Connect as " + ftp_user + " to " + ftp_server + ":" + ftp_port + " FTP(s) and to " + ftp_server + ":" + sftp_port + " SFTP");

        // Plain FTP
        if (do_ftp) {
            try {
                logger.info("Plain FTP");
                ftp(ftp_server, ftp_port, ftp_user, ftp_password);
                logger.info("Plain FTP - " + ANSI_GREEN + "OK" + ANSI_RESET);
            } catch (IOException e) {
                logger.error("Plain FTP - " + ANSI_RED + "FAILED" + ANSI_RESET);
                logger.debug(Arrays.toString(e.getStackTrace()));
            }
        }
        // FTP with SSL/TLS
        if (do_ftps) {
            try {
                logger.info("FTP with SSL/TLS");
                ftps(ftp_server, ftp_port, ftp_user, ftp_password);
                logger.info("FTP with SSL/TLS - " + ANSI_GREEN + "OK" + ANSI_RESET);
            } catch (IOException e) {
                logger.error("FTP with SSL/TLS - " + ANSI_RED + "FAILED" + ANSI_RESET);
                logger.debug(Arrays.toString(e.getStackTrace()));
            }
        }
        // SFTP
        if (do_sftp) {
            try {
                logger.info("SFTP");
                sftp(ftp_server, sftp_port, ftp_user, ftp_password);
                logger.info("SFTP - " + ANSI_GREEN + "OK" + ANSI_RESET);
            } catch (IOException e) {
                logger.error("SFTP - " + ANSI_RED + "FAILED" + ANSI_RESET);
                logger.debug(Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
