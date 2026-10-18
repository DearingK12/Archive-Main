package com.virtualos.filesystem;

import java.time.Instant;

public final class VirtualFile {
    private final String name;
    private String contents;
    private final Instant createdAt;

    public VirtualFile(String name, String contents) {
        this.name = name;
        this.contents = contents;
        this.createdAt = Instant.now();
    }

    public String name() { return name; }
    public String contents() { return contents; }
    public Instant createdAt() { return createdAt; }
    public void setContents(String contents) { this.contents = contents; }
}
