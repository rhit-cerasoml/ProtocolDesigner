package protocoldesigner.library.util.appcache;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class ApplicationCache {

    private static String appName = "";
    private static Path appCachePath = null;
    public static void setAppName(String appName){
        ApplicationCache.appName = appName;
        appCachePath = getCachePath();
    }

    public static Path getCachePath() {
        if(appName.isEmpty()){
            throw new RuntimeException("Application path not set.\nPlease call ApplicationCache.setAppName(String appname) to assign an appname.");
        }
        return getCachePath(appName);
    }
    private static Path getCachePath(String appName) {
        String osName = System.getProperty("os.name");
        FileSystem fileSystem = FileSystems.getDefault();

        // macOS
        if (osName.toLowerCase().contains("mac")) {
            return fileSystem.getPath(System.getProperty("user.home"), "Library", "Caches");
        }

        // Linux
        if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return fileSystem.getPath(System.getProperty("user.home"), ".cache");
        }

        // Windows
        if (osName.contains("indows")) {
            return fileSystem.getPath(System.getenv("LOCALAPPDATA"), "Caches").resolve(appName);
        }

        // A reasonable fallback
        return fileSystem.getPath(System.getProperty("user.home"), "caches");
    }
}
