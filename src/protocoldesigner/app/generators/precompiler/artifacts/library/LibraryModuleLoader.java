package protocoldesigner.app.generators.precompiler.artifacts.library;


import protocoldesigner.app.generators.precompiler.artifacts.Artifact;

import java.util.ArrayList;

public abstract class LibraryModuleLoader {
    LibraryModule module;
    public LibraryModuleLoader(LibraryModule module){
        this.module = module;
    }
    public abstract void load(ArrayList<Artifact> artifacts, String libPath);
}
