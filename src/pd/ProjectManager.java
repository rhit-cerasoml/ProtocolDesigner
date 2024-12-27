package pd;

import pd.artifacts.Artifact;
import pd.artifacts.UtilityArtifacts;
import pd.dataobject.DataObject;
import pd.dataobject.Target;
import pd.util.OptionalString;
import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

import java.io.*;
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
    private TypeManager typeManager;

    private boolean canRevertCache = false; // Not saved - must be on an operation during this session

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
        this.typeManager = new TypeManager();
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

    public TypeManager getTypeManager(){
        return this.typeManager;
    }


    public void build() throws Exception {
        if(!buildDirectory.valid){
            throw new Exception("No specified build directory");
        }
        if(cacheDirectory.valid){
            cache();
        }
        for(Artifact utilityClass : UtilityArtifacts.artifacts){
            utilityClass.buildArtifact(buildDirectory.s);
        }
        for(DataObject obj : objects){
            obj.buildClass(Target.COMMON).buildArtifact(buildDirectory.s);
            obj.buildClass(Target.SERVER).buildArtifact(buildDirectory.s);
            obj.buildClass(Target.CLIENT).buildArtifact(buildDirectory.s);
        }
    }

    public void cache() throws Exception {
        if(!cacheDirectory.valid){
            throw new Exception("No specified cache directory");
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
        this.canRevertCache = true;
    }

    public void revertBuild() throws Exception {
        if(!this.canRevertCache){
            throw new Exception("No operation to revert");
        }
        if(!cacheDirectory.valid){
            throw new Exception("No specified cache directory");
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
            throw new Exception("No specified cache directory");
        }
        emptyDirectory(Path.of(cacheDirectory.s));
    }

    private void emptyDirectory(Path path) throws IOException {
        File file = new File(path.toString());
        if(!file.exists()) return;

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

    public void saveAs(String newProjectPath) throws IOException {
        this.projectFilePath = newProjectPath;
        save();
    }

    private byte[] buildSaveData() {
        SerializingOutputStream out = new SerializingOutputStream();

        this.buildDirectory.write(out);
        this.cacheDirectory.write(out);
        out.writeArrayList(objects);
        this.typeManager.serialize(out);

        return out.toByteArray();
    }

    private void loadFromSaveData(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {

        this.buildDirectory.read(in);
        this.cacheDirectory.read(in);
        this.objects = in.readArrayList(DataObject::new);
        this.typeManager = new TypeManager(in);

        for(DataObject dataObject : objects){
            dataObject.setTypeManager(this.typeManager);
        }

    }

}
