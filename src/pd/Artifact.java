package pd;

import pd.util.OptionalString;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Artifact {
    private String path;
    private ArrayList<Segment> segments;

    public Artifact(String path){
        this.path = path;
        segments = new ArrayList<>();
    }

    public void append(String s) {
        segments.add(new StringSegment(s));
    }

    public void appendFreeSegment(String segmentIdentifier) {
        segments.add(new FreeSegment(segmentIdentifier));
    }

    public String getPath(){
        return path;
    }

    public void buildArtifact(String buildDirectory) throws Exception {
        File file = new File(buildDirectory + "/" + getPath());
        if(!file.exists()) {
            Path path = Paths.get(file.getPath());
            Path parent = path.getParent();
            if(parent != null && Files.notExists(parent)) {
                Files.createDirectories(parent);
            }
            file.createNewFile();
            FileWriter fout = new FileWriter(file);
            fout.write(writeNew());
            fout.flush();
            fout.close();
            return;
        }
        String source = Files.readString(file.toPath());
        FileWriter fout = new FileWriter(file);
        fout.write(updateExisting(source));
        fout.flush();
        fout.close();
    }

    public String writeNew(){
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        boolean firstString = true;
        for(Segment segment : segments){
            if(segment.isFree()){
                if(!first){
                    sb.append("//----- Start segment : [");
                    sb.append(((FreeSegment)segment).identifier);
                    sb.append("] -----\n");
                }
                sb.append("//----- End segment : [");
                sb.append(((FreeSegment)segment).identifier);
                sb.append("] -----\n");
            }else{
                if(firstString) {
                    sb.append("// This is a partially generated file, please only modify code between a pair of start and end segments\n");
                    sb.append("// or above the first end segment. Please do not modify the start/end tags.\n");
                }
                sb.append(((StringSegment)segment).content);
                firstString = false;
            }
            first = false;
        }
        return sb.toString();
    }

    public String updateExisting(String source) throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        boolean firstString = true;
        for(Segment segment : segments){
            if(segment.isFree()){
                int startIndex = 0;
                int endIndex;
                if(!first){
                    String startTag = "//----- Start segment : [" +
                            ((FreeSegment) segment).identifier +
                            "] -----\n";
                    startIndex = source.indexOf(startTag);
                }
                String endTag = "//----- End segment : [" +
                        ((FreeSegment) segment).identifier +
                        "] -----\n";
                endIndex = source.indexOf(endTag);
                if(startIndex == -1 || endIndex == -1){
                    throw new Exception("segment " + ((FreeSegment) segment).identifier + " could not be found in source file for " + path);
                }
                sb.append(source.substring(startIndex, endIndex + endTag.length()));
            }else{
                if(firstString) {
                    sb.append("// This is a partially generated file, please only modify code between a pair of start and end segments\n");
                    sb.append("// or above the first end segment. Please do not modify the start/end tags.\n");
                }
                sb.append(((StringSegment)segment).content);
                firstString = false;
            }
            first = false;
        }
        return sb.toString();
    }

    private abstract static class Segment {
        abstract boolean isFree();
    }

    private static class StringSegment extends Segment {
        public String content;
        public StringSegment(String s){
            this.content = s;
        }

        @Override
        boolean isFree() {
            return false;
        }
    }

    private static class FreeSegment extends Segment {
        public String identifier;
        public FreeSegment(String segmentIdentifier) {
            this.identifier = segmentIdentifier;
        }

        @Override
        boolean isFree() {
            return true;
        }
    }
}
