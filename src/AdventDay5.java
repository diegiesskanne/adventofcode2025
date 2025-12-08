import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdventDay5 {
    HashMap<Integer, Integer> rangesMap = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Hello, Advent of Code Day 5!");
        ReadResult rr = readLinesSplitByEmpty();
        //System.out.println(findSpoiledIngredients(rr));
        System.out.println(findFreshIngredientsCount(rr));
    }

    private static int findSpoiledIngredients(ReadResult rr) {
        int res = 0;
        List<long[]> ranges = new ArrayList<>();
        for (String range : rr.ranges) {
            long[] bounds = splitIntoTwoLongs(range);
            ranges.add(bounds);
        }
        ranges.sort(java.util.Comparator.comparingLong(a -> a[0]));
        List<long[]> merged = mergeRanges(ranges);
        for (long num : rr.ingredients) {
            if (binarySearch(num, merged)) {
                res++;
            }
        }
        return res;
    }

    private static long findFreshIngredientsCount(ReadResult rr) {
        long res = 0;
        List<long[]> ranges = new ArrayList<>();
        for (String range : rr.ranges) {
            long[] bounds = splitIntoTwoLongs(range);
            ranges.add(bounds);
        }
        ranges.sort(java.util.Comparator.comparingLong(a -> a[0]));
        List<long[]> merged = mergeRanges(ranges);
        for (long[] range : merged) {
            res += (range[1] - range[0] + 1);
        }
        return res;
    }

    private static boolean binarySearch(long target, List<long[]> sortedRangesList) {
        int left = 0;
        int right = sortedRangesList.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            long[] range = sortedRangesList.get(mid);
            if (target < range[0]) {
                right = mid - 1;
            } else if (target >= range[0]) {
                if(target <= range[1]) {
                    return true;
                }
                left = mid + 1;
            }
        }
        return false;
    }

    record ReadResult(String[] ranges, long[] ingredients) {}

    private static List<long[]> mergeRanges(List<long[]> sortedRangesList) {
        List<long[]> merged = new ArrayList<>();
        for (long[] cur : sortedRangesList) {
            if (merged.isEmpty()) {
                merged.add(new long[] { cur[0], cur[1] });
                continue;
            }
            long[] last = merged.getLast();
            if (cur[0] <= last[1] + 1) {
                last[1] = Math.max(last[1], cur[1]);
            } else {
                merged.add(new long[] { cur[0], cur[1] });
            }
        }
        return merged;
    }

    private static ReadResult readLinesSplitByEmpty() {
        List<String> firstPart = new ArrayList<>();
        List<Long> secondPart = new ArrayList<>();
        boolean afterEmpty = false;

        try (BufferedReader br = new BufferedReader(new FileReader("src/input5.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!afterEmpty) {
                    if (line.trim().isEmpty()) {
                        afterEmpty = true;
                    } else {
                        firstPart.add(line);
                    }
                } else {
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    secondPart.add(Long.parseLong(line.trim()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + "src/input5.txt", e);
        }

        String[] strings = firstPart.toArray(new String[0]);
        long[] longs = new long[secondPart.size()];
        for (int i = 0; i < secondPart.size(); i++) {
            longs[i] = secondPart.get(i);
        }
        return new ReadResult(strings, longs);
    }

    private static long[] splitIntoTwoLongs(String s) {
        String[] parts = s.split("-", 2);
        long a = Long.parseLong(parts[0].trim());
        long b = Long.parseLong(parts[1].trim());
        if (a > b) {
            long t = a; a = b; b = t;
        }
        return new long[] { a, b };
    }
}
