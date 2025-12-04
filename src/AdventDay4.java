import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AdventDay4 {
    public static void main(String[] args) throws IOException {
        char[][] input = readLines();
        System.out.println(findRolls(input));
    }

    private static int findRolls(char[][] board) {
        int count = 0;
        while (true) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[i][j] == '@') {
                        if (isCorner(board, i, j)) {
                            board[i][j] = ',';
                            count++;
                        } else if (j == 0) {
                            int c = getCountOfRollsForLeftBound(board, i, j);
                            if (c < 4) {
                                board[i][j] = ',';
                                count++;
                            }
                        } else if (i == board.length - 1) {
                            int c = getCountOfRollsForLowerBound(board, i, j);
                            if (c < 4) {
                                board[i][j] = ',';
                                count++;
                            }
                        } else if (i == 0) {
                            int c = getCountOfRollsForUpperBound(board, i, j);
                            if (c < 4) {
                                board[i][j] = ',';
                                count++;
                            }
                        } else if (j == board.length - 1) {
                            int c = getCountOfRollsForRightBound(board, i, j);
                            if (c < 4) {
                                board[i][j] = ',';
                                count++;
                            }
                        } else {
                            int c = getCountOfAdjacentRollsForInner(board, i, j);
                            if (c < 4) {
                                board[i][j] = ',';
                                count++;
                            }
                        }
                    }
                }
            }
            boolean found1 = false;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[i][j] == ',') {
                        found1 = true;
                        board[i][j] = '.';
                    }
                }
            }
            if (!found1) {
                return count;
            }
        }
    }

    private static int getCountOfAdjacentRollsForInner(char[][] board, int row, int col) {
        int res = 0;
        if (board[row + 1][col] == '@') {
            res++;
        }
        if (board[row][col + 1] == '@') {
            res++;
        }
        if (board[row + 1][col + 1] == '@') {
            res++;
        }
        if (board[row - 1][col] == '@') {
            res++;
        }
        if (board[row][col - 1] == '@') {
            res++;
        }
        if (board[row - 1][col - 1] == '@') {
            res++;
        }
        if (board[row + 1][col - 1] == '@') {
            res++;
        }
        if (board[row - 1][col + 1] == '@') {
            res++;
        }
        return res;
    }

    private static int getCountOfRollsForLeftBound(char[][] board, int row, int col) {
        int res = 0;

        if (board[row - 1][col] == '@') res++;     // up
        if (board[row + 1][col] == '@') res++;     // down
        if (board[row][col + 1] == '@') res++;     // right
        if (board[row - 1][col + 1] == '@') res++; // up-right
        if (board[row + 1][col + 1] == '@') res++; // down-right

        return res;
    }

    private static int getCountOfRollsForRightBound(char[][] board, int row, int col) {
        int res = 0;

        if (board[row - 1][col] == '@') res++;     // up
        if (board[row + 1][col] == '@') res++;     // down
        if (board[row][col - 1] == '@') res++;     // left
        if (board[row - 1][col - 1] == '@') res++; // up-left
        if (board[row + 1][col - 1] == '@') res++; // down-left

        return res;
    }

    private static int getCountOfRollsForUpperBound(char[][] board, int row, int col) {
        int res = 0;

        if (board[row][col - 1] == '@') res++;     // left
        if (board[row][col + 1] == '@') res++;     // right
        if (board[row + 1][col] == '@') res++;     // down
        if (board[row + 1][col - 1] == '@') res++; // down-left
        if (board[row + 1][col + 1] == '@') res++; // down-right

        return res;
    }

    private static int getCountOfRollsForLowerBound(char[][] board, int row, int col) {
        int res = 0;

        if (board[row][col - 1] == '@') res++;     // left
        if (board[row][col + 1] == '@') res++;     // right
        if (board[row - 1][col] == '@') res++;     // up
        if (board[row - 1][col - 1] == '@') res++; // up-left
        if (board[row - 1][col + 1] == '@') res++; // up-right

        return res;
    }

    private static boolean isCorner(char[][] board, int row, int col) {
        return row == 0 && col == 0 ||
                row + 1 == board.length && col + 1 == board[0].length ||
                row == 0 && col + 1 == board[0].length ||
                row + 1 == board.length && col == 0;
    }

    private static char[][] readLines() throws IOException {
        var lines = Files.readAllLines(Path.of("src/input.txt"));

        // Create 2D char array
        char[][] result = new char[lines.size()][];

        // Convert each line to a char[]
        for (int i = 0; i < lines.size(); i++) {
            result[i] = lines.get(i).toCharArray();
        }

        return result;
    }
}
