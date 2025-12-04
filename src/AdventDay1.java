import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AdventDay1 {
    private static int count = 0;

    public static void main(String[] args) throws IOException {
        System.out.println("Hello, Advent of Code Day 1!");
        System.out.println(crackLock());
    }

    public static int crackLock() throws IOException {
        List<String> lines = readLinesSafe("src/input1.txt");
        //List<String> lines = List.of("R56", "L6", "L10", "R10", "R5", "R1000");
        int currentNum = 50;
        int res = 0;
        for (String line : lines) {
            String[] tmp = splitAfterFirst(line);
            int num = Integer.parseInt(tmp[1].trim());
            if(num > 100){
                count += num / 100;
                num = num % 100;
            }
            if(tmp[0].equals("R")) {
                currentNum = addOverZero(currentNum, num);
            } else if (tmp[0].equals("L")) {
                currentNum = subtractUnderZero(currentNum, num);
            }
            if (currentNum == 0) {
                res++;
            }
        }
        return res + count;
    }

    public static int addOverZero(int a, int b) {
        int tmp = a + b;
        if(tmp >= 100) {
            if (tmp != 100 && a != 0) {
                count++;
            }
            return tmp - 100;
        } else {
            return tmp;
        }
    }

    public static int subtractUnderZero(int a, int b) {
        int tmp = a - b;
        if(tmp < 0) {
            if (a != 0) {
                count++;
            }
            return tmp + 100;
        } else {
            return tmp;
        }
    }

    public static String[] splitAfterFirst(String s) {
        if (s == null || s.isEmpty()) return new String[]{"", ""};
        return new String[]{s.substring(0, 1), s.substring(1)};
    }

    public static List<String> readLinesSafe(String fileName) {
        Path path = Path.of(System.getProperty("user.dir")).resolve(fileName);
        if (!Files.exists(path)) {
            System.err.println("File not found: " + path);
            return List.of(); // or throw new IllegalStateException(...)
        }
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Failed to read file: " + e.getMessage());
            return List.of();
        }
    }
}
