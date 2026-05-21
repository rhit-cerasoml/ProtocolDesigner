package protocoldesigner.app.generators.precompiler.artifacts.library;

import protocoldesigner.app.generators.precompiler.artifacts.Artifact;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class DirectoryModuleLoader extends LibraryModuleLoader {
    private String directoryPath;
    private String targetPath;
    public DirectoryModuleLoader(LibraryModule module, String directoryPath, String targetPath){
        super(module);
        this.directoryPath = directoryPath;
        this.targetPath = targetPath;
    }
    @Override
    public void load(ArrayList<Artifact> artifacts, String libPath) {
        Path readPath = Paths.get(directoryPath + targetPath);
        Path buildRelativePath = Paths.get(directoryPath);
        try(Stream<Path> stream = Files.walk(readPath)){
            stream.filter(Files::isRegularFile).forEach(sourcePath -> {
                Artifact content = new Artifact(libPath + "/" + buildRelativePath.relativize(sourcePath));
                content.appendFreeSegment("package");
                try {
                    content.append(Files.readString(sourcePath));
                }catch (Exception e){
                    System.out.println("Failed to copy library file: " + sourcePath);
                }
                artifacts.add(content);
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
