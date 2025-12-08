import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class AdventDay6 {

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to AdventDay6");
        System.out.println(doMath());
        System.out.println(doMath3());
    }

    private static long doMath3() throws IOException {

        List<String> lines = Files.readAllLines(Paths.get("src/input6.txt"));

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
        List<Long> list = new ArrayList<>();
        long res = 0;
        Character op = null;
        String tmp = "";
        for (int j = matrix[0].length -1; j >= 0; j--) {
            boolean newColumn = true;
            for (int i = 0; i < matrix.length; i++) {
                if(matrix[i][j] != ' ') {
                    newColumn = false;
                    if((matrix[i][j] != '+') && (matrix[i][j] != '*')) {
                        tmp += matrix[i][j];
                    }
                }
                if(i == matrix.length-1) {
                    if(newColumn || j == 0) {
                        if(j == 0) {
                            op = matrix[i][j];
                        }
                        list.add(Long.parseLong(tmp));
                        if (op == '+') {
                            res += list.stream().mapToLong(Long::longValue).sum();
                        }
                        if(op == '*') {
                            res += multiplyList(list);
                        }
                        list.clear();
                        op = null;
                        tmp = "";
                    } else {
                        if (matrix[i][j] == '+' || matrix[i][j] == '*') {
                            op = matrix[i][j];
                        }
                        if (matrix[i][j] == ' ') {
                            list.add(Long.parseLong(tmp));
                            tmp = "";
                        }
                    }
                }
            }
        }


        return res;
    }

    private static long multiplyList(List<Long> list) {
        long res = list.getFirst();
        list.removeFirst();
        for (Long l : list) {
            res *= l;
        }
        return res;
    }

    private static long doMath() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/input6.txt"));

        String[][] arr = new String[lines.size()][];

        for (int i = 0; i < lines.size(); i++) {
            arr[i] = lines.get(i).trim().split("\\s+");
        }

        int rows = arr.length;
        int cols = arr[0].length;

        String[][] transposed = new String[cols][rows];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                transposed[c][r] = arr[r][c];
            }
        }

        int newRows = transposed.length;
        int newCols = transposed[0].length;

        char[] operations = new char[newRows];
        for (int r = 0; r < newRows; r++) {
            operations[r] = transposed[r][newCols - 1].toCharArray()[0];
        }

        long res = 0;

        for (int i = 0; i < newRows; i++) {
            long rowresult = Long.parseLong(transposed[i][0]);
            for (int j = 1; j < newCols - 1; j++) {
                if (operations[i] == '+') {
                    rowresult = rowresult + Long.parseLong(transposed[i][j]);
                } else if (operations[i] == '*') {
                    rowresult = rowresult * Long.parseLong(transposed[i][j]);
                }
            }
            res += rowresult;
        }

        return res;
    }
}
