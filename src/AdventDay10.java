import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ssclab.pl.milp.*;
import static org.ssclab.pl.milp.ConsType.*;
import static org.ssclab.pl.milp.LP.NaN;
import org.ssclab.pl.milp.Solution;


public class AdventDay10 {

    public static void main(String[] args) throws Exception {
        List<ParsedLine> parsedLines = parseFile(Path.of("src/input10.txt"));
        //System.out.println(findShortestPath(parsedLines));
        System.out.println(findShortestPath2(parsedLines));
    }

    private static double findShortestPath2(List<ParsedLine> parsedLines) throws Exception {
        List<Double> shortestLengths = new ArrayList<>();
        for (ParsedLine parsedLine : parsedLines) {
            int[] tailNonList = parsedLine.tail.stream().mapToInt(i -> i).toArray();
            int[] start = new int[tailNonList.length];
            double res = solveWithMilp(parsedLine);
            shortestLengths.add(res);
        }
        return shortestLengths.stream().mapToDouble(l -> l).sum();
    }

    public static double solveWithMilp(ParsedLine pl) throws Exception {
        int m = pl.groups.size();                  // number of groups
        int n = pl.tail.size();                    // target vector length

        // Objective: minimize sum_j x_j
        double[] c = new double[m];
        Arrays.fill(c, 1.0);

        // Equality constraints: sum_j A[i][j] * x_j == tail[i]
        double[][] A = new double[n][m];
        double[] b = new double[n];
        ConsType[] rel = new ConsType[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                A[i][j] = pl.groups.get(j).contains(i) ? 1.0 : 0.0;
            }
            b[i] = pl.tail.get(i);
            rel[i] = ConsType.EQ;
        }

        LinearObjectiveFunction fo = new LinearObjectiveFunction(c, GoalType.MIN);
        ListConstraints constraints = new ListConstraints();
        for (int i = 0; i < A.length; i++) {
            constraints.add(new Constraint(A[i], rel[i], b[i]));
        }

        // x_j >= 0
        for (int j = 0; j < m; j++) {
            double[] row = new double[m];
            row[j] = 1.0;
            constraints.add(new Constraint(row, GE, 0.0));
        }

        // Integer variables marker row: 1 for integer vars, 0 otherwise
        double[] intRow = new double[m];
        Arrays.fill(intRow, 1.0);
        constraints.add(new Constraint(intRow, INT, 0.0));

        MILP milp = new MILP(fo, constraints);
        SolutionType solution_type = milp.resolve();

        if (solution_type == SolutionType.OPTIMAL) {
            Solution solution = milp.getSolution();
            double[] x = solution.getValuesSolution();
            double sum = Arrays.stream(x).sum();
            return Math.rint(sum);
        }
        return NaN;
    }

//    private static int help2(int shortest, int currentLen, int[] current, int[] result, List<List<Integer>> groups) {
//        if (currentLen >= shortest || currentLen > 20) {
//            return shortest;
//        }
//        for (int i = 0; i < current.length; i++) {
//            if (current[i] > result[i]) {
//                return shortest;
//            }
//        }
//        int[] original = current.clone();
//        for (List<Integer> group : groups) {
//            for (Integer i : group) {
//                current[i] += 1;
//            }
//            if (Arrays.equals(current, result)) {
//                shortest = Math.min(shortest, currentLen);
//            } else {
//                int candidate = help2(shortest, currentLen + 1, current, result, groups);
//                shortest = Math.min(shortest, candidate);
//            }
//            current = original.clone();
//        }
//        return shortest;
//    }

    private static int findShortestPath(List<ParsedLine> parsedLines) {
        List<Integer> shortestLengths = new ArrayList<>();
        for (ParsedLine parsedLine : parsedLines) {
            boolean[] start = new boolean[parsedLine.pattern.length];
            int res = help(Integer.MAX_VALUE, 0, start, parsedLine.pattern, parsedLine.groups);
            shortestLengths.add(res+1);
        }
        return shortestLengths.stream().mapToInt(l -> l).sum();
    }

    private static int help(int shortest, int currentLen, boolean[] current, boolean[] result, List<List<Integer>> groups) {
        if (currentLen >= shortest || currentLen > 10) {
            return shortest;
        }
        boolean[] original = current.clone();
        for (List<Integer> group : groups) {
            for (Integer i : group) {
                current[i] = !current[i];
            }
            if (Arrays.equals(current, result)) {
                shortest = Math.min(shortest, currentLen);
            } else {
                int candidate = help(shortest, currentLen + 1, current, result, groups);
                shortest = Math.min(shortest, candidate);
            }
            current = original.clone();
        }
        return shortest;
    }

    public static class ParsedLine {
        public final boolean[] pattern;               // boolean[] from []
        public final List<List<Integer>> groups;      // all () as lists of integers
        public final List<Integer> tail;              // {} as integers

        public ParsedLine(boolean[] pattern, List<List<Integer>> groups, List<Integer> tail) {
            this.pattern = pattern;
            this.groups = groups;
            this.tail = tail;
        }
    }

    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\[(.*?)]");
    private static final Pattern PAREN_GROUP_PATTERN = Pattern.compile("\\((.*?)\\)");
    private static final Pattern CURLY_PATTERN = Pattern.compile("\\{(.*?)}");

    public static List<ParsedLine> parseFile(Path path) throws IOException {
        List<ParsedLine> result = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                // Parse boolean[] from []
                Matcher mBracket = BRACKET_PATTERN.matcher(line);
                if (!mBracket.find()) {
                    throw new IllegalArgumentException("Missing bracketed pattern: " + line);
                }
                String bracketContent = mBracket.group(1).trim();
                char[] chars = bracketContent.toCharArray();
                boolean[] pattern = new boolean[chars.length];
                for (int i = 0; i < chars.length; i++) {
                    char c = chars[i];
                    if (c != '.' && c != '#') {
                        throw new IllegalArgumentException("Invalid pattern char '" + c + "' in: " + bracketContent);
                    }
                    pattern[i] = (c == '#');
                }

                // Parse all () groups into List<List<Integer>>
                List<List<Integer>> groups = new ArrayList<>();
                Matcher mParen = PAREN_GROUP_PATTERN.matcher(line);
                while (mParen.find()) {
                    String content = mParen.group(1).trim();
                    if (content.isEmpty()) {
                        groups.add(new ArrayList<>()); // empty group
                    } else {
                        groups.add(parseIntegerList(content));
                    }
                }

                // Parse {} into List<Integer>
                Matcher mCurly = CURLY_PATTERN.matcher(line);
                List<Integer> tail = new ArrayList<>();
                if (mCurly.find()) {
                    String curlyContent = mCurly.group(1).trim();
                    if (!curlyContent.isEmpty()) {
                        tail = parseIntegerList(curlyContent);
                    }
                }

                result.add(new ParsedLine(pattern, groups, tail));
            }
        }
        return result;
    }

    private static List<Integer> parseIntegerList(String content) {
        String[] toks = content.split("\\s*,\\s*");
        List<Integer> list = new ArrayList<>(toks.length);
        for (String tok : toks) {
            if (!tok.isEmpty()) list.add(Integer.parseInt(tok));
        }
        return list;
    }
}
