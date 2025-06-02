package com.foodora;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("runTest")) {
            if (args.length != 2) {
                System.err.println("Usage: runTest <test-scenario-file>");
                System.exit(1);
            }
            runTestScenario(args[1]);
        } else {
            runInteractiveMode();
        }
    }

    private static void runTestScenario(String testFile) {
        try {
            // Create output file name based on input file
            String outputFile = testFile.replace(".txt", "output.txt");
            
            // Set up output redirection to both console and file
            PrintStream fileOut = new PrintStream(new FileOutputStream(outputFile));
            PrintStream multiOut = new MultiOutputStream(System.out, fileOut);
            PrintStream oldOut = System.out;
            System.setOut(multiOut);

            // Read and execute commands from test file
            try (BufferedReader reader = new BufferedReader(new FileReader(testFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) {
                        continue;  // Skip empty lines and comments
                    }
                    System.out.println("> " + line);  // Echo command
                    executeCommand(line);
                }
            }

            // Restore original output
            System.setOut(oldOut);
            fileOut.close();
            System.out.println("Test scenario completed. Output saved to: " + outputFile);

        } catch (IOException e) {
            System.err.println("Error running test scenario: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void runInteractiveMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to MyFoodora System!");
        System.out.println("Type 'help' for available commands or 'exit' to quit");
        
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            
            if (line.equalsIgnoreCase("exit")) {
                break;
            }
            
            if (!line.isEmpty()) {
                executeCommand(line);
            }
        }
        
        System.out.println("Goodbye!");
        scanner.close();
    }

    private static void executeCommand(String line) {
        try {
            String[] parts = line.split("\\s+");
            if (parts.length == 0) {
                return;
            }
            
            String command = parts[0];
            String[] commandArgs = new String[parts.length - 1];
            System.arraycopy(parts, 1, commandArgs, 0, parts.length - 1);
            
            CLUI.executeCommand(command, commandArgs);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Helper class to output to multiple streams
    private static class MultiOutputStream extends PrintStream {
        private final PrintStream second;

        public MultiOutputStream(PrintStream first, PrintStream second) {
            super(first);
            this.second = second;
        }

        @Override
        public void write(byte[] buf, int off, int len) {
            try {
                super.write(buf, off, len);
                second.write(buf, off, len);
            } catch (Exception e) {
                System.err.println("Error writing to multiple streams: " + e.getMessage());
            }
        }

        @Override
        public void write(int b) {
            try {
                super.write(b);
                second.write(b);
            } catch (Exception e) {
                System.err.println("Error writing to multiple streams: " + e.getMessage());
            }
        }
    }
} 