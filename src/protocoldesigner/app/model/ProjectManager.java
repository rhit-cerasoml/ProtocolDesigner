package protocoldesigner.app.model;

import protocoldesigner.app.generators.precompiler.artifacts.Artifact;
import protocoldesigner.app.generators.precompiler.artifacts.LibraryArtifactLoader;
import protocoldesigner.app.generators.precompiler.artifacts.library.LibraryModule;
import protocoldesigner.app.generators.precompiler.dataobject.DataObject;
import protocoldesigner.library.util.OptionalString;
import protocoldesigner.library.util.file.FileUtils;
import protocoldesigner.library.util.serial.Serializable;
import protocoldesigner.library.util.serial.SerializingInputStream;
import protocoldesigner.library.util.serial.SerializingOutputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Stream;

public class ProjectManager {
    private boolean isValid = false;

    private OptionalString buildDirectory = new OptionalString();
    private OptionalString cacheDirectory = new OptionalString();
    private TypeManager typeManager;
    private ArrayList<DataObject> objects;
    private HashSet<LibraryModule> libraries = new HashSet<>();

    private boolean canRevertCache = false; // Not saved - must be on an operation during this session

    public ProjectManager(String projectFilePath) {
        try {
            load(projectFilePath);
        }catch (Exception e){
            initDefault();
        }
        libraries.add(LibraryModule.UTIL);
        libraries.add(LibraryModule.NET);
        libraries.add(LibraryModule.POOL);
    }

    private void initDefault(){
        this.buildDirectory.invalidate();
        this.cacheDirectory.invalidate();
        this.objects = new ArrayList<>();
        this.typeManager = new TypeManager();
        this.isValid = true;
    }

    public boolean isValid(){
        return isValid;
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


        LibraryArtifactLoader libLoader = new LibraryArtifactLoader(libraries);

        for(Artifact utilityClass : libLoader.artifacts){
            utilityClass.buildArtifact(buildDirectory.s);
        }
        for(DataObject obj : objects){
            obj.buildClass().buildArtifact(buildDirectory.s);
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

        FileUtils.emptyDirectory(buildPath);

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
        FileUtils.emptyDirectory(Path.of(cacheDirectory.s));
    }
    private void load(String projectFilePath) throws IOException, SerializingInputStream.InvalidStreamLengthException {
        FileInputStream fin = new FileInputStream(projectFilePath);
        SerializingInputStream in = new SerializingInputStream(fin);
        loadFromSaveData(in);
        isValid = true;
    }

    public void save(String projectFilePath) throws IOException {
        File file = FileUtils.getOrCreateFile(projectFilePath);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(buildSaveData());
        fos.flush();
        fos.close();
    }

    private byte[] buildSaveData() {
        SerializingOutputStream out = new SerializingOutputStream();

        out.writeObj(this.buildDirectory);
        out.writeObj(this.cacheDirectory);
        this.typeManager.serialize(out);
        out.writeArrayList(objects);

        return out.toByteArray();
    }

    private void loadFromSaveData(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.buildDirectory = in.readObj(OptionalString::new);
        this.cacheDirectory = in.readObj(OptionalString::new);
        this.typeManager = new TypeManager(in);
        this.objects = in.readArrayList((input) -> new DataObject(input, typeManager));
    }
}
