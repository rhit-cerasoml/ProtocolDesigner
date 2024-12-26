package pd;

import pd.dataobject.DataObject;
import pd.util.OptionalString;
import pd.util.SerializingInputStream;
import pd.util.SerializingOutputStream;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

public class ProjectManager {
    private String projectFilePath;
    private OptionalString buildDirectory = new OptionalString();
    private OptionalString cacheDirectory = new OptionalString();
    private ArrayList<DataObject> objects;

    public ProjectManager(String projectFilePath) {
        this.projectFilePath = projectFilePath;
        try {
            load();
        }catch (Exception e){
            initDefault();
        }
    }

    private void initDefault(){
        this.buildDirectory.invalidate();
        this.cacheDirectory.invalidate();
        this.objects = new ArrayList<>();
    }

    public void setBuildDirectory(String buildDirectory) throws Exception {
        this.buildDirectory.set(buildDirectory);
        validateCacheDirectory();
    }

    public void setCacheDirectory(String cacheDirectory) throws Exception {
        this.cacheDirectory.set(cacheDirectory);
        validateCacheDirectory();
    }

    private void validateCacheDirectory() throws Exception {
        if(!this.cacheDirectory.valid || !this.buildDirectory.valid){
            return;
        }
        Path buildPath = Path.of(buildDirectory.s);
        Path cachePath = Path.of(cacheDirectory.s);

        Path walk = cachePath;
        do{
            if(walk.equals(buildPath)){
                this.cacheDirectory.invalidate();
                throw new Exception("Invalid cache path: within build path");
            }
        }while((walk = walk.getParent()) != null);

        walk = buildPath;
        do{
            if(walk.equals(cachePath)){
                this.cacheDirectory.invalidate();
                throw new Exception("Invalid cache path: contains build path");
            }
        }while((walk = walk.getParent()) != null);
    }

    public void addDataObject(DataObject dataObject){
        objects.add(dataObject);
    }

    public void build() throws Exception {
        if(!buildDirectory.valid){
            throw new Exception("No specified build directory!");
        }
        if(cacheDirectory.valid){
            cache();
        }
        for(DataObject obj : objects){
            obj.buildNewBaseClass().buildArtifact(buildDirectory.s);
            obj.buildNewClientClass().buildArtifact(buildDirectory.s);
            obj.buildNewServerClass().buildArtifact(buildDirectory.s);
        }
    }

    public void cache() throws Exception {
        if(!cacheDirectory.valid){
            throw new Exception("No specified cache directory!");
        }

        Path buildPath = Path.of(buildDirectory.s);
        Path cachePath = Path.of(cacheDirectory.s);

        if(Files.notExists(buildPath)) return;

        clearCache();

        Stream<Path> paths = Files.walk(buildPath);
        paths.forEach((path -> {
            try {
                Files.copy(path, cachePath.resolve(buildPath.relativize(path)));
            } catch (IOException e) {
                throw new RuntimeException("Unable to copy file to cache: " + e);
            }
        }));
        paths.close();
    }

    public void revertBuild() throws Exception {
        if(!cacheDirectory.valid){
            throw new Exception("No specified cache directory!");
        }

        Path buildPath = Path.of(buildDirectory.s);
        Path cachePath = Path.of(cacheDirectory.s);

        emptyDirectory(buildPath);

        Stream<Path> paths = Files.walk(cachePath);
        paths.forEach((path -> {
            try {
                Files.copy(path, buildPath.resolve(cachePath.relativize(path)));
            } catch (IOException e) {
                throw new RuntimeException("Unable to copy file from cache: " + e);
            }
        }));
        paths.close();
    }

    private void clearCache() throws Exception {
        if(!cacheDirectory.valid){
            throw new Exception("No specified cache directory!");
        }

        emptyDirectory(Path.of(cacheDirectory.s));
    }

    private void emptyDirectory(Path path) throws IOException {
        // Delete all contents of a path
        // Clever solution from: https://stackoverflow.com/a/46983076
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    private void load() throws IOException, SerializingInputStream.InvalidStreamLengthException {
        FileInputStream fin = new FileInputStream(projectFilePath);
        SerializingInputStream in = new SerializingInputStream(fin);
        loadFromSaveData(in);
    }

    public void save() throws IOException {
        File file = new File(projectFilePath);
        if(!file.exists()) {
            Path path = Paths.get(file.getPath());
            Path parent = path.getParent();
            if(parent != null && Files.notExists(parent)) {
                Files.createDirectories(parent);
            }
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(buildSaveData());
        fos.flush();
        fos.close();
    }

    private byte[] buildSaveData() {
        SerializingOutputStream out = new SerializingOutputStream();

        this.buildDirectory.write(out);
        this.cacheDirectory.write(out);
        out.writeArrayList(objects);

        return out.toByteArray();
    }

    private void loadFromSaveData(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {

        this.buildDirectory.read(in);
        this.cacheDirectory.read(in);
        this.objects = in.readArrayList(DataObject::new);

    }

    public void saveAs(String newProjectPath) throws IOException {
        this.projectFilePath = newProjectPath;
        save();
    }
}
