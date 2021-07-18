package com.abstractsun.boab;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "boab",
    mixinStandardHelpOptions = true,
    version = "boab 0.1.0",
    description = "Workspace root 🌳")
public class Main implements Callable<Integer> {
    @Parameters(index = "0", description = "init, ls, pwd, run [cmd ...], run -- [cmd -flags ...]") String command;
    @Parameters() String[] args;
    
    protected static final String NOT_A_BOAB_WORKSPACE = "Not a boab workspace. Create one in the current directory with `boab init`";
    
    protected static File getExistingBoabDirectory(File d) {
        d = d.getAbsoluteFile();
        while (d != null) {
            File boabDirectory = new File(d, ".boab");
            if (boabDirectory.exists()) {
                return boabDirectory;
            }
            d = d.getParentFile();
        }
        return null;
    }
    
    protected static File getWorkspaceRoot(File d) {
        File boabDirectory = getExistingBoabDirectory(d);
        if (boabDirectory == null) {
            return null;
        }
        return boabDirectory.getAbsoluteFile().getParentFile();
    }
    
    protected static boolean hasBoabDirectory(File d) {
        return getExistingBoabDirectory(d) != null;
    }
    
    @Override
    public Integer call() throws Exception {
        if (command.equals("init")) {
            File initDirectory = new File(".boab");
            if (initDirectory.exists()) {
                System.out.println(String.format("Boab workspace already exists at %1$s", initDirectory.getAbsoluteFile().toString()));
            }
            else {
                initDirectory.mkdir();
                System.out.println(String.format("Initialized boab workspace in %1$s", initDirectory.getAbsoluteFile().toString()));
            }
        }
        else if (command.equals("ls")) {
            File workspaceRoot = getWorkspaceRoot(new File(""));
            if (workspaceRoot == null) {
                System.out.println(NOT_A_BOAB_WORKSPACE);
                return 1;
            }
            else {
                for (File f : workspaceRoot.listFiles()) {
                    String fileName = f.getName();
                    if (fileName.startsWith(".")) {
                        continue;
                    }
                    System.out.println(fileName);
                }
            }
        }
        else if (command.equals("pwd")) {
            File workspaceRoot = getWorkspaceRoot(new File(""));
            if (workspaceRoot == null) {
                System.out.println(NOT_A_BOAB_WORKSPACE);
                return 1;
            }
            else {
                System.out.println(workspaceRoot.getAbsolutePath().toString());
            }
        }
        else if (command.equals("run")) {
            File workspaceRoot = getWorkspaceRoot(new File(""));
            if (workspaceRoot == null) {
                System.out.println(NOT_A_BOAB_WORKSPACE);
                return 1;
            }
            else {
                System.out.println(String.format("workspace root: %1$s", workspaceRoot.getAbsoluteFile().toString()));
                String[] command = Arrays.copyOfRange(args, 1, args.length);
                ProcessBuilder pb = new ProcessBuilder(command)
                    .directory(workspaceRoot)
                    .inheritIO();
                pb.environment().putAll(System.getenv());
                return pb
                        .start()
                        .waitFor();
            }
        }
        else {
            System.out.println(String.format("Unknown command %1$s", command));
        }
        return 0;
    }
    
    public static void main(String... args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
