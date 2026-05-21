package protocoldesigner.library.util.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class FileUtils {
    public static void emptyDirectory(Path path) throws IOException {
        File file = new File(path.toString());
        if(!file.exists()) return;

        // Delete all contents of a path
        // Clever solution from: https://stackoverflow.com/a/46983076
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public static File getOrCreateFile(String pathString) throws IOException {
        File file = new File(pathString);
        if(!file.exists()) {
            Path path = Paths.get(file.getPath());
            Path parent = path.getParent();
            if(parent != null && Files.notExists(parent)) {
                Files.createDirectories(parent);
            }
            if(file.createNewFile()){
                return file;
            }
        }else{
            return file;
        }
        throw new RuntimeException("Failed to get or create " + pathString);
    }
}
