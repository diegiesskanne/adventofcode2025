import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AdventDay7 {
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to AdventDay7");
        System.out.println(countPaths());
    }

    private static long countSplits() throws IOException {
        char[][] matrix = getMatrix();
        long splitCount = 0;
        CopyOnWriteArrayList<Integer> lastIndexes = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<Integer> lastIndexesCopy = new CopyOnWriteArrayList<>();
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[r].length; c++) {
                if (r % 2 == 0) {
                    if (matrix[r][c] == 'S') {
                        lastIndexes.add(c);
                    }
                    if (matrix[r][c] == '^' && lastIndexes.contains(c)) {
                        lastIndexes.addIfAbsent(c-1);
                        lastIndexes.addIfAbsent(c+1);
                        lastIndexes.remove(Integer.valueOf(c));
                        splitCount++;
                    }
                    if (matrix[r][c] == '^' && lastIndexesCopy.contains(c)) {
                        lastIndexesCopy.remove(Integer.valueOf(c));
                    }
                }
            }
            if (lastIndexesCopy.isEmpty() && r != 0) {
                return splitCount;
            }
            lastIndexesCopy.addAll(lastIndexes);
        }

        return splitCount;
    }

    private static char[][] getRays() throws IOException {
        char[][] matrix = getMatrix();
        CopyOnWriteArrayList<Integer> lastIndexes = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<Integer> lastSplitterIndexes = new CopyOnWriteArrayList<>();
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[r].length; c++) {
                if (r % 2 == 0) {
                    if (matrix[r][c] == 'S') {
                        lastIndexes.add(c);
                    }
                    if (matrix[r][c] == '^' && lastIndexes.contains(c)) {
                        lastIndexes.addIfAbsent(c-1);
                        matrix[r][c-1] = '|';
                        lastIndexes.addIfAbsent(c+1);
                        matrix[r][c+1] = '|';
                        lastIndexes.remove(Integer.valueOf(c));
                        lastSplitterIndexes.add(c);
                    }
                } else {
                    if (lastIndexes.contains(c)) {
                        matrix[r][c] = '|';
                    }
                    if (lastSplitterIndexes.contains(c)) {
                        matrix[r][c] = '.';
                    }
                }
                if(r%2==1){
                    lastSplitterIndexes.clear();
                }
            }
        }

        return matrix;
    }

    private static long countPaths() throws IOException {
        char[][] matrix = getRays();
        long[] pathsThatHit = new long[matrix[0].length];
        for (int c = 0; c < matrix[0].length; c++) {
            if(matrix[0][c] == 'S') {
                pathsThatHit[c]++;
            }
        }
        for (int r = 0; r < matrix.length; r+=2) {
            for (int c = 0; c < matrix[r].length; c++) {
                if(matrix[r][c] == '^') {
                    if(matrix[r-1][c] == '|') {
                        pathsThatHit[c-1] += pathsThatHit[c];
                        pathsThatHit[c+1] += pathsThatHit[c];
                        pathsThatHit[c] = 0;
                    }
                }
            }
        }
        long a = 0;
        for(int i = 0; i < pathsThatHit.length; i++){
            a += pathsThatHit[i];
        }
        return a;
    }


    private static char[][] getMatrix() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/input7.txt"));

        int maxWidth = 0;
        for (String line : lines) {
            if (line.length() > maxWidth) {
                maxWidth = line.length();
            }
        }

        char[][] matrix = new char[lines.size()][maxWidth];

        for (int r = 0; r < lines.size(); r++) {
            String line = lines.get(r);
            for (int c = 0; c < maxWidth; c++) {
                if (c < line.length()) {
                    matrix[r][c] = line.charAt(c);
                } else {
                    matrix[r][c] = ' ';
                }
            }
        }
        return  matrix;
    }
}
