package com.example.main.Configuration;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.CentralProcessor;

public class SystemUsageMonitor {

    private static final SystemInfo systemInfo = new SystemInfo();
    private static final HardwareAbstractionLayer hardware = systemInfo.getHardware();
    private static final CentralProcessor processor = hardware.getProcessor();

    // Thresholds for determining health
    private static final double CPU_THRESHOLD_WARNING = 70.0; // in percentage
    private static final double CPU_THRESHOLD_CRITICAL = 90.0; // in percentage
    private static final long MEMORY_THRESHOLD_WARNING_MB = 512; // in MB
    private static final long MEMORY_THRESHOLD_CRITICAL_MB = 256; // in MB

    public static long getApplicationMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public static double getSystemCpuLoad() {
        // Create an array to store CPU ticks for each core
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // Introduce a small delay to allow the system to update the ticks
        try {
            Thread.sleep(1000);  // 1-second sleep to simulate time difference
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long[] ticks = processor.getSystemCpuLoadTicks();
        // Get the CPU load between ticks (CPU usage over time)
        double load = processor.getSystemCpuLoadBetweenTicks(prevTicks);
        return load * 100;  // Convert to percentage
    }

    public static long getAvailableMemory() {
        return hardware.getMemory().getAvailable();
    }

    public static long getTotalMemory() {
        return hardware.getMemory().getTotal();
    }

    public static String getHealthStatus() {
        double cpuLoad = getSystemCpuLoad();
        long availableMemoryMB = getAvailableMemory() / (1024 * 1024);

        // Determine health based on CPU load and available memory
        if (cpuLoad > CPU_THRESHOLD_CRITICAL || availableMemoryMB < MEMORY_THRESHOLD_CRITICAL_MB) {
            return "Critical";
        } else if (cpuLoad > CPU_THRESHOLD_WARNING || availableMemoryMB < MEMORY_THRESHOLD_WARNING_MB) {
            return "Warning";
        } else {
            return "Healthy";
        }
    }

    public static void printSystemUsage() {
        System.out.println("Application Memory Usage: " + getApplicationMemoryUsage() / (1024 * 1024) + " MB");
        System.out.println("System CPU Load: " + getSystemCpuLoad() + " %");
        System.out.println("Available Memory: " + getAvailableMemory() / (1024 * 1024) + " MB");
        System.out.println("Total System Memory: " + getTotalMemory() / (1024 * 1024) + " MB");
        System.out.println("System Health: " + getHealthStatus());
    }
}
