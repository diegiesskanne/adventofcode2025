import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdventDay3 {
    private static HashMap<Integer, List<Integer>> map = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Hello, Advent of Code Day 3!");
        System.out.println(findJoltage2());
    }

    private static long findJoltage2() {
        List<String> lines = readLinesSafe("src/input3.txt");
        long res = 0;
        ArrayList<Character> resDigits = new ArrayList<>();
        for (String line : lines) {
            char[] chars = line.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                if (resDigits.size() == 6){
                    System.out.println("hi");
                }
                char[] rest = line.substring(j + 1).toCharArray();
                int a = checkIfTailContainsBiggerNumber(Integer.parseInt(String.valueOf(chars[j])), line.substring(j + 1).toCharArray(), line.length());
                int originalIndex;
                if (a != -1) {
                    List<Integer> indices = map.get(a);
                    originalIndex = indices.stream().max(Integer::compareTo).orElse(-1);
                } else {
                    originalIndex = -1;
                }
                boolean thereIsABetterCandidate = false;
                int thatManyDigitsLeft = line.length() - originalIndex;
                if(originalIndex != -1 && 12-resDigits.size() <= thatManyDigitsLeft) {
                    thereIsABetterCandidate = true;
                }
                if(thereIsABetterCandidate && (rest.length >= 12-resDigits.size())) {
                } else {
                    resDigits.add(chars[j]);
                }
                if(resDigits.size() == 12) {
                    res += arraylistToLong(resDigits);
                    resDigits.clear();
                    map.clear();
                    break;
                }
            }
        }
        return res;
    }

    static long arraylistToLong(ArrayList<Character> chars) {
        StringBuilder sb = new StringBuilder();
        for (Character c : chars) {
            sb.append(c);
        }
        return Long.parseLong(sb.toString());
    }

    private static int checkIfTailContainsBiggerNumber(int digit, char[] lineChars, int lineLength) {
        int offset = lineLength - lineChars.length;
        for (int i = 0; i < lineChars.length; i++) {
            int d = Integer.parseInt(String.valueOf(lineChars[i]));
            if(d > digit) {
                addToMap(d, i+offset);
                return d;
            }
        }
        return -1;
    }
    private static void addToMap(Integer key, Integer value) {
        // Check if the key already exists
        if (!map.containsKey(key)) {
            map.put(key, new ArrayList<>()); // Initialize the List if key doesn't exist
        }
        // Add the value to the List if it's not already present (optional uniqueness)
        if (!map.get(key).contains(value)) {
            map.get(key).add(value);
        }
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
