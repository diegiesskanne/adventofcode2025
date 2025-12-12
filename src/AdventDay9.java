import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdventDay9 {

    public static void main(String[] args) {
        System.out.println("Hello, Advent of Code Day 9!");
        System.out.println(biggestRectangle2());
    }

    private static class Range {
        int[] start;
        int[] end;

        public Range(int[] start, int[] end) {
            this.start = start;
            this.end = end;
        }
    }

    private static long biggestRectangle2() {
        List<int[]> points = parseInput();

        List<Range> ranges = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            int[] current = points.get(i);
            int[] next = points.get((i + 1) % points.size());
            ranges.add(new Range(current, next));
        }

        long maxArea = 0;
        for (int i = 0; i < points.size(); i++) {
            int[] p1 = points.get(i);
            for (int j = i + 1; j < points.size(); j++) {
                int[] p2 = points.get(j);
                int length = Math.abs(p2[0] - p1[0]);
                int width = Math.abs(p2[1] - p1[1]);
                long area = (long) (length + 1) * (width + 1);
                if (area > maxArea) {
                    if(!recContainsRange(ranges, p1, p2)) {
                        maxArea = area;
                    }
                }
            }
        }
        return maxArea;
    }

    private static boolean recContainsRange(List<Range> ranges, int[] p1, int[] p2) {
        int x3 = Math.min(p1[0], p2[0]);
        int y3 = Math.min(p1[1], p2[1]);
        int x4 = Math.max(p1[0], p2[0]);
        int y4 = Math.max(p1[1], p2[1]);

        // Treat rectangle interior as open: exclude borders
        for (Range range : ranges) {
            int x1 = range.start[0];
            int y1 = range.start[1];
            int x2 = range.end[0];
            int y2 = range.end[1];

            boolean vertical = x1 == x2;

            if (vertical) {
                // Edge x = x1; must lie strictly inside rect in x, and overlap in y
                if (x3 < x1 && x1 < x4) {
                    int eyMin = Math.min(y1, y2);
                    int eyMax = Math.max(y1, y2);
                    int oyMin = y3;
                    int oyMax = y4;
                    if (Math.max(eyMin, oyMin) < Math.min(eyMax, oyMax)) {
                        return true;
                    }
                }
            } else {
                // Edge y = y1; must lie strictly inside rect in y, and overlap in x
                if (y3 < y1 && y1 < y4) {
                    int exMin = Math.min(x1, x2);
                    int exMax = Math.max(x1, x2);
                    int oxMin = x3;
                    int oxMax = x4;
                    if (Math.max(exMin, oxMin) < Math.min(exMax, oxMax)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static long biggestRectangle() {
        List<int[]> points = parseInput();
        long maxArea = 0;
        for (int i = 0; i < points.size(); i++) {
            int[] p1 = points.get(i);
            for (int j = i + 1; j < points.size(); j++) {
                int[] p2 = points.get(j);
                int length = Math.abs(p2[0] - p1[0]);
                int width = Math.abs(p2[1] - p1[1]);
                long area = (long) (length + 1) * (width + 1);
                if (area > maxArea) {
                    maxArea = area;
                }
            }
        }
        return maxArea;
    }

    private static List<int[]> parseInput() {
        String filePath = "src/input9.txt";

        List<int[]> coordinates = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into x, y, and z using "," as the delimiter
                String[] parts = line.trim().split(",");
                if (parts.length == 2) {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);

                    coordinates.add(new int[]{x, y});
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("error");
        }
        return coordinates;
    }
}
