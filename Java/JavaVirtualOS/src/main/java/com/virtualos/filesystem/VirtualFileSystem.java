package com.virtualos.filesystem;

import java.util.*;

public final class VirtualFileSystem {
    private final VirtualDirectory root = new VirtualDirectory("/", null);

    public VirtualFileSystem() {
        root.addDirectory("home");
        root.addDirectory("tmp");
        root.addDirectory("system");
        root.directory("home").addDirectory("admin");
        root.directory("home").addDirectory("guest");
        root.directory("system").addFile("welcome.txt",
                "Welcome to Java Virtual OS!\nThis is a simulated file system.");
        root.directory("home").directory("admin").addFile("readme.txt",
                "You are in the admin home directory.");
        root.directory("home").directory("guest").addFile("hello.txt",
                "Hello, guest!");
    }

    public List<String> list(String path) {
        VirtualDirectory directory = findDirectory(path);
        if (directory == null) return List.of("Directory not found: " + path);
        List<String> names = new ArrayList<>();
        directory.directories().stream().map(n -> n.name() + "/").forEach(names::add);
        directory.files().stream().map(VirtualFile::name).forEach(names::add);
        if (names.isEmpty()) names.add("(empty)");
        return names;
    }

    public boolean isDirectory(String path) {
        return findDirectory(path) != null;
    }

    public String makeDirectory(String path) {
        if (path.equals("/")) return "Root directory already exists.";
        String parentPath = parent(path);
        String name = leaf(path);
        VirtualDirectory parent = findDirectory(parentPath);
        if (parent == null) return "Parent directory does not exist.";
        if (parent.contains(name)) return "A file or directory with that name already exists.";
        parent.addDirectory(name);
        return "Created directory " + path;
    }

    public String writeFile(String path, String contents) {
        if (path.equals("/")) return "Cannot write a file over the root directory.";
        VirtualDirectory parent = findDirectory(parent(path));
        if (parent == null) return "Parent directory does not exist.";
        String name = leaf(path);
        if (parent.directory(name) != null) return "A directory with that name already exists.";
        parent.addFile(name, contents);
        return "Saved " + path;
    }

    public String readFile(String path) {
        VirtualFile file = findFile(path);
        return file == null ? "File not found: " + path : file.contents();
    }

    public String remove(String path) {
        if (path.equals("/")) return "Cannot remove the root directory.";
        VirtualDirectory parent = findDirectory(parent(path));
        if (parent == null) return "Parent directory does not exist.";
        String name = leaf(path);
        if (parent.removeFile(name)) return "Removed file " + path;
        VirtualDirectory child = parent.directory(name);
        if (child == null) return "No file or directory found: " + path;
        if (!child.isEmpty()) return "Directory is not empty.";
        parent.removeDirectory(name);
        return "Removed directory " + path;
    }

    public void printTree(String path) {
        VirtualDirectory start = findDirectory(path);
        if (start == null) {
            System.out.println("Directory not found: " + path);
            return;
        }
        System.out.println(start.name().equals("/") ? "/" : start.name() + "/");
        printChildren(start, "");
    }

    private void printChildren(VirtualDirectory directory, String prefix) {
        List<VirtualDirectory> dirs = new ArrayList<>(directory.directories());
        List<VirtualFile> files = new ArrayList<>(directory.files());
        for (VirtualDirectory child : dirs) {
            System.out.println(prefix + "├── " + child.name() + "/");
            printChildren(child, prefix + "│   ");
        }
        for (VirtualFile file : files) {
            System.out.println(prefix + "└── " + file.name());
        }
    }

    private VirtualDirectory findDirectory(String path) {
        VirtualDirectory current = root;
        if (path.equals("/") || path.isBlank()) return root;
        for (String part : path.split("/")) {
            if (part.isBlank()) continue;
            current = current.directory(part);
            if (current == null) return null;
        }
        return current;
    }

    private VirtualFile findFile(String path) {
        VirtualDirectory parent = findDirectory(parent(path));
        return parent == null ? null : parent.file(leaf(path));
    }

    private String parent(String path) {
        int index = path.lastIndexOf('/');
        if (index <= 0) return "/";
        return path.substring(0, index);
    }

    private String leaf(String path) {
        int index = path.lastIndexOf('/');
        return path.substring(index + 1);
    }
}
