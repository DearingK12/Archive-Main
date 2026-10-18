package com.virtualos.filesystem;

import java.util.*;

public final class VirtualDirectory {
    private final String name;
    private final VirtualDirectory parent;
    private final Map<String, VirtualDirectory> directories = new TreeMap<>();
    private final Map<String, VirtualFile> files = new TreeMap<>();

    public VirtualDirectory(String name, VirtualDirectory parent) {
        this.name = name;
        this.parent = parent;
    }

    public String name() { return name; }
    public VirtualDirectory parent() { return parent; }
    public Collection<VirtualDirectory> directories() { return Collections.unmodifiableCollection(directories.values()); }
    public Collection<VirtualFile> files() { return Collections.unmodifiableCollection(files.values()); }
    public VirtualDirectory directory(String name) { return directories.get(name); }
    public VirtualFile file(String name) { return files.get(name); }
    public boolean contains(String name) { return directories.containsKey(name) || files.containsKey(name); }
    public boolean isEmpty() { return directories.isEmpty() && files.isEmpty(); }

    public void addDirectory(String name) {
        directories.put(name, new VirtualDirectory(name, this));
    }

    public void addFile(String name, String contents) {
        VirtualFile existing = files.get(name);
        if (existing == null) files.put(name, new VirtualFile(name, contents));
        else existing.setContents(contents);
    }

    public boolean removeFile(String name) { return files.remove(name) != null; }
    public boolean removeDirectory(String name) { return directories.remove(name) != null; }
}
