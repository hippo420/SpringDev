package app.springdev.system.util;

public class MemoryUtil {
    public static long usedMemoryInMB() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
    }
}
