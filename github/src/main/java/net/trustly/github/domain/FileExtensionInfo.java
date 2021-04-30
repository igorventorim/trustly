package net.trustly.github.domain;

import java.io.Serializable;
import java.util.Objects;

public class FileExtensionInfo implements Serializable {

    public String extension;
    public int count;
    public int lines;
    public long bytes;

    public FileExtensionInfo(String extension, Integer lines, Long bytes) {
        this.extension = extension;
        this.lines = Objects.nonNull(lines) ? lines : 0;
        this.bytes = Objects.nonNull(bytes) ? bytes : 0;
        this.count = 1;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getCount() {
        return count;
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public long getBytes() {
        return bytes;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }


    public void merge(FileExtensionInfo fileExtensionInfo) {

        if(!this.extension.equals(fileExtensionInfo.getExtension())){
            throw new RuntimeException("Merging is only allowed to the same extension");
        }

        this.count += fileExtensionInfo.getCount();
        this.bytes += fileExtensionInfo.getBytes();
        this.lines += fileExtensionInfo.getLines();

    }
}
