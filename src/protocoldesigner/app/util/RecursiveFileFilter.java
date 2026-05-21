package protocoldesigner.app.util;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public abstract class RecursiveFileFilter extends FileFilter {
    private final int maxDepth;
    private final String descriptor;
    public RecursiveFileFilter(int maxDepth, String descriptor){
        this.maxDepth = maxDepth;
        this.descriptor = descriptor;
    }

    @Override
    public boolean accept(File f) {
        return recurse(f, maxDepth);
    }

    private boolean recurse(File f, int depth){
        if(depth < 0) return true;
        if(f.isDirectory()){
            try {
                for (File subfile : f.listFiles()) {
                    if (recurse(subfile, depth - 1)) return true;
                }
            }catch (Exception e){
                return true;
            }
            return false;
        }else{
            return acceptFile(f);
        }
    }

    @Override
    public String getDescription() {
        return descriptor;
    }
    public abstract boolean acceptFile(File f);
}
