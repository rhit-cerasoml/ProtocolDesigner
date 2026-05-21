package protocoldesigner.app.generators.precompiler.artifacts;

import protocoldesigner.app.generators.precompiler.artifacts.library.DirectoryModuleLoader;
import protocoldesigner.app.generators.precompiler.artifacts.library.LibraryModule;
import protocoldesigner.app.generators.precompiler.artifacts.library.LibraryModuleLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LibraryArtifactLoader {
    public ArrayList<Artifact> artifacts;

    private HashMap<LibraryModule, LibraryModuleLoader> loaders;

    public LibraryArtifactLoader(HashSet<LibraryModule> moduleSelection) throws RuntimeException {
        artifacts = new ArrayList<>();
        buildLibraryLoaders();
        for(LibraryModule selection : moduleSelection){
            if(loaders.containsKey(selection)){
                loaders.get(selection).load(artifacts, "/libraries");
            }else{
                throw new RuntimeException("Couldn't identify loader for library: " + selection);
            }
        }
    }

    private void buildLibraryLoaders(){
        loaders = new HashMap<>();
        loaders.put(LibraryModule.UTIL, new DirectoryModuleLoader(LibraryModule.UTIL, "./src/pd/library/", "util"));
        loaders.put(LibraryModule.NET, new DirectoryModuleLoader(LibraryModule.NET, "./src/pd/library/", "net"));
        loaders.put(LibraryModule.POOL, new DirectoryModuleLoader(LibraryModule.POOL, "./src/pd/library/", "pool"));
    }
}
