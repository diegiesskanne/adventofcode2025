import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class AdventDay2 {
    public static void main(String[] args) {
        System.out.println("Hello, Advent of Code Day 2!");
        System.out.println(crackLock());
    }

    public static long crackLock() {
        List<String> lines = readFile("src/input2.txt");
        long res = 0;
        for (String line : lines) {
            long[] numbers = splitIntoTwoInts(line);
            for (long i = numbers[0]; i <= numbers[1]; i++) {
                String tmp = String.valueOf(i);
                int t = getInvalidCount(tmp);
                if (t == 1) {
                    res += i;
                }
            }
        }
        return res;
    }
    private static int getInvalidCount(String tmp) {
        return help(tmp.substring(0,1), tmp.substring(1), (int) Math.ceil((tmp.length()/2.0)));
    }

    private static int help(String left, String right, int length) {
        if(left.equals(right)) {
            return 1;
        }
        if(left.length() == length) {
            return 0;
        }
        if(isSequenced(left, right)) {
            return 1;
        } else {
            return help(left + right.charAt(0), right.substring(1), length);
        }
    }

    private static boolean isSequenced(String head, String rest) {
        if (rest.isEmpty()) {
            return true;
        }
        if(head.length() > rest.length()) {
            return false;
        }
        if(!(head.equals(rest.substring(0,head.length())))) {
            return false;
        }
        return isSequenced(head, rest.substring(head.length()));
    }

    private static String[] splitInHalf(String s) {
        if (s == null) throw new IllegalArgumentException("input is null");
        int len = s.length();
        int mid = len / 2; // even: equal halves; odd: first half shorter
        String first = s.substring(0, mid);
        String second = s.substring(mid);
        return new String[] { first, second };
    }

    private static long[] splitIntoTwoInts(String s) {
        String[] parts = s.split("-", 2);
        long a = Long.parseLong(parts[0].trim());
        long b = Long.parseLong(parts[1].trim());
        return new long[] { a, b };
    }

    public static List<String> readFile(String fileName) {
        Path path = Path.of(System.getProperty("user.dir")).resolve(fileName);
        if (!Files.exists(path)) {
            System.err.println("File not found: " + path);
            return List.of();
        }
        try (var stream = Files.lines(path, StandardCharsets.UTF_8)) {
            var first = stream.findFirst();
            if (first.isEmpty()) {
                return List.of();
            }
            String line = first.get();
            String[] parts = line.split("\\s*,\\s*");
            return List.of(parts);
        } catch (IOException e) {
            System.err.println("Failed to read file: " + e.getMessage());
            return List.of();
        }
    }
}
