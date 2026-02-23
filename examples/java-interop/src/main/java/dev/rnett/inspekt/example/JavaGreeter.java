package dev.rnett.inspekt.example;

public class JavaGreeter {
    private final String name;

    public JavaGreeter(String name) {
        this.name = name;
    }

    public String greet() {
        return "Hello from Java, " + name + "!";
    }

    public String getName() {
        return name;
    }
}
