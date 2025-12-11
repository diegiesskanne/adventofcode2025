import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdventDay11 {

    public static void main(String[] args) throws Exception {
        //System.out.println(findShortestPath(parsedLines));
        System.out.println(findAllPaths());
    }

    public static class Node {
        public final String current;
        public final List<Node> next;

        public Node(String name) {
            this.current = name;
            this.next = new ArrayList<>();
        }

        public Node(String current, List<Node> next) {
            this.current = current;
            this.next = next != null ? next : new ArrayList<>();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node other = (Node) o;
            return Objects.equals(this.current, other.current);
        }
    }

    private static final java.util.Map<String, Long> memo = new java.util.HashMap<>();

    private static long findAllPaths() throws Exception {
        List<Node> nodes = readNodesFromFile("src/input11.txt");
        Node startNode = null;
        for (Node node : nodes) {
            if (Objects.equals(node.current, "dac")) {
                startNode = node;
                break;
            }
        }
        if (startNode == null) {
            return 0;
        }
        List<Node> path = new ArrayList<>();
        path.add(startNode);
        return help(startNode, path);
    }

    private static long help(Node startNode, List<Node> visited) {
        if (Objects.equals(startNode.current, "out")) {
            return 1;
        }
        Long cached = memo.get(startNode.current);
        if (cached != null) {
            return cached;
        }
        long total = 0;
        for (Node node : startNode.next) {
            if (!visited.contains(node)) {
                visited.add(node);
                total += help(node, visited);
                visited.removeLast();
            }
        }
        memo.put(startNode.current, total);
        return total;
    }

    public static List<Node> readNodesFromFile(String filename) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(filename));

        java.util.Map<String, Node> nodeMap = new java.util.HashMap<>();
        java.util.Map<String, List<String>> edgeMap = new java.util.HashMap<>();

        for (String line : lines) {
            String[] parts = line.split(":");
            String current = parts[0].trim();
            nodeMap.computeIfAbsent(current, Node::new);

            List<String> nextNames = new ArrayList<>();
            if (parts.length > 1) {
                for (String s : parts[1].trim().split("\\s+")) {
                    if (!s.isEmpty()) nextNames.add(s);
                }
            }
            edgeMap.put(current, nextNames);
            for (String name : nextNames) {
                nodeMap.computeIfAbsent(name, Node::new);
            }
        }

        for (java.util.Map.Entry<String, List<String>> e : edgeMap.entrySet()) {
            Node from = nodeMap.get(e.getKey());
            for (String toName : e.getValue()) {
                from.next.add(nodeMap.get(toName));
            }
        }

        return new ArrayList<>(nodeMap.values());
    }
}
