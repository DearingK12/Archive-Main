package com.virtualos;

import com.virtualos.terminal.Terminal;

public final class Main {
    private Main() {}

    public static void main(String[] args) {
        System.out.println("Java Virtual OS v1.0");
        System.out.println("A Java-only virtual operating system simulation.");
        System.out.println("Type 'help' to see commands. Type 'exit' to quit.\n");
        new Terminal().run();
    }
}
