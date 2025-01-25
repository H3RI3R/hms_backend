package com.example.main.Configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class DatabaseBackup {


    @Value("${spring.database.name}")
    private String databaseName;

    @Value("${spring.backup.path}")
    private String backUpPath;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.backup.schedulerTimer}")
    private String backUpTimer;



    @Scheduled(cron = "${spring.backup.schedulerTimer}")
    public void backupDatabase() {

        deleteOldBackups();
        try {
            System.out.println("Backup scheduled task triggered.");

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String backupFileName = backUpPath + "/HMSBackup_" + timeStamp + ".sql";
            String command = String.format("pg_dump -U %s -F c -b -v -f \"%s\" \"%s\"", dbUsername, backupFileName, databaseName);

            // Execute the backup command
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            processBuilder.environment().put("PGPASSWORD", dbPassword);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read output from the process
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            // Check exit code and log the appropriate message
            String message = (process.waitFor() == 0)
                    ? "Backup created successfully: " + backupFileName
                    : "Backup failed with exit code: " + process.exitValue() + "\nCommand output: " + output;

            System.out.println(message);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred during backup.");
        }
    }



    private void deleteOldBackups() {
        try {
            File backupDir = new File(backUpPath);
            File[] backupFiles = backupDir.listFiles((dir, name) -> name.startsWith("HMSBackup_") && name.endsWith(".sql"));

            if (backupFiles != null) {
                for (File backupFile : backupFiles) {
                    if (backupFile.delete()) {
                        System.out.println("Deleted backup file: " + backupFile.getName());
                    } else {
                        System.err.println("Failed to delete backup file: " + backupFile.getName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred while deleting old backups.");
        }
    }

}
