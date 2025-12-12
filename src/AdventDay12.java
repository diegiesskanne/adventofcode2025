import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdventDay12 {

    public static void main(String[] args) throws Exception {
        System.out.println("Hello, Advent of Code Day 12!");
        System.out.println(countFits());
    }

    private static int countFits() {
        List<String> lines = readLines("src/input12.txt");
        List<String> relevantLines = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            if(i > 29) {
                relevantLines.add(lines.get(i));
            }
        }
        int i = 0;
        int firstNumber;
        int secondNumber;
        int[] counts;
        for (String line : relevantLines) {
            int[] linenumbers = parseEightInts(line);
            firstNumber = linenumbers[0];
            secondNumber = linenumbers[1];
            int product = firstNumber * secondNumber;
            counts = new int[] {
                    linenumbers[2],
                    linenumbers[3],
                    linenumbers[4],
                    linenumbers[5],
                    linenumbers[6],
                    linenumbers[7]
            };
            if(checkIfItFits(product, counts)) {
                i++;
            }
        }
        return i;
    }

    private static boolean checkIfItFits(int matrixSize, int[] counts) {
        int total = 0;
        for (int count : counts) {
            total += count * 9;
        }
        return total <= matrixSize;
    }

    private static List<String> readLines(String pathStr) {
        try {
            Path path = Paths.get(pathStr);
            if (!Files.exists(path)) {
                System.err.println("Path not found: " + pathStr);
                return Collections.emptyList();
            }
            return Files.readAllLines(path);
        } catch (Exception e) {
            System.err.println("Failed to read lines from " + pathStr + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Java
    public static int[] parseEightInts(String s) {
        // Expected format: "<A>x<B>:<C> <D> <E> <F> <G> <H>"
        int[] out = new int[8];

        int ix = s.indexOf('x');
        if (ix < 0) throw new IllegalArgumentException("Missing 'x'");

        int colon = s.indexOf(':', ix + 1);
        if (colon < 0) throw new IllegalArgumentException("Missing ':'");

        // 1st: until first 'x'
        out[0] = Integer.parseInt(s.substring(0, ix).trim());

        // 2nd: right after 'x' until ':'
        out[1] = Integer.parseInt(s.substring(ix + 1, colon).trim());

        // 3rd: after ':', until first space (or end)
        int spaceAfterColon = s.indexOf(' ', colon + 1);
        String third = spaceAfterColon >= 0
                ? s.substring(colon + 1, spaceAfterColon+3)
                : s.substring(colon + 1);
        out[2] = Integer.parseInt(third.trim());

        String tail = spaceAfterColon >= 0 ? s.substring(spaceAfterColon + 3).trim() : "";
        if (tail.isEmpty()) throw new IllegalArgumentException("Missing tail numbers");

        String[] parts = tail.split("\\s+");
        if (parts.length < 5) throw new IllegalArgumentException("Expected 5 tail numbers");

        for (int i = 0; i < 5; i++) {
            out[3 + i] = Integer.parseInt(parts[i]);
        }

        return out;
    }

}
