import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AdventDay8 {
    private static List<String> keys = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Hello, Advent of Code Day 8!");
        System.out.println(getDist());
    }

    private static long getCircuitProduct() {
        List<long[]> vectors = parseInput();
        long[][] arr = vectors.toArray(new long[vectors.size()][]);
        String[] keyss = keys.toArray(new String[0]);
        List<CustomVector> customVectors = new ArrayList<>();
        for (int i = 0; i < keyss.length; i++) {
            List<long[]> allButCurrent = new ArrayList<>(vectors);
            allButCurrent.remove(arr[i]);
            CustomVector customVector = new CustomVector(keyss[i]);
            for (long[] vector : allButCurrent) {
                double distance = getVectorDistance(arr[i], vector);
                String neighbor = formatVector(vector);
                customVector.addDistance(neighbor, distance);
            }
            customVectors.add(customVector);
        }

        Map<String, CustomVector> byName = new HashMap<>();
        for (CustomVector cv : customVectors) {
            byName.put(cv.getName(), cv);
        }

        double shortestDistance;
        List<List<String>> circuits = new ArrayList<>();
        List<String> connected;

        for (int i = 0; i < 1000; i++) {
            shortestDistance = Double.MAX_VALUE;
            connected = new ArrayList<>(2);

            // pick current global shortest edge among remaining distances
            for (CustomVector customVector : customVectors) {
                Map<String, Double> dists = customVector.getDistances();
                if (dists == null || dists.isEmpty()) {
                    continue;
                }
                for (Map.Entry<String, Double> e : dists.entrySet()) {
                    double val = e.getValue();
                    if (Double.isNaN(val)) {
                        continue;
                    }
                    if (val < shortestDistance) {
                        shortestDistance = val;
                        connected.clear();
                        connected.add(customVector.getName());
                        connected.add(e.getKey());
                    }
                }
            }

            if (connected.size() != 2) {
                break;
            }

            // remove the chosen edge from both endpoints
            CustomVector target1 = byName.get(connected.get(0));
            CustomVector target2 = byName.get(connected.get(1));
            if (target1 != null) {
                target1.removeDistanceByNeighbor(connected.get(1));
            }
            if (target2 != null) {
                target2.removeDistanceByNeighbor(connected.get(0));
            }

            boolean merged = false;
            List<List<String>> newCircuits = new ArrayList<>();

            int idxA = -1, idxB = -1;
            for (int k = 0; k < circuits.size(); k++) {
                List<String> circuit = circuits.get(k);
                if (circuit.contains(connected.get(0))) {
                    idxA = k;
                }
                if (circuit.contains(connected.get(1))) {
                    idxB = k;
                }
            }
            if (idxA != -1 && idxB != -1 && idxA != idxB) {
                List<String> circuitA = circuits.get(idxA);
                List<String> circuitB = circuits.get(idxB);
                List<String> mergedCircuit = new ArrayList<>(circuitA.size() >= circuitB.size() ? circuitA : circuitB);
                List<String> otherCircuit = circuitA.size() >= circuitB.size() ? circuitB : circuitA;

                for (String n : otherCircuit) {
                    if (!mergedCircuit.contains(n)) {
                        mergedCircuit.add(n);
                    }
                }

                newCircuits.add(mergedCircuit);
                for (int j = 0; j < circuits.size(); j++) {
                    if (j != idxA && j != idxB) {
                        newCircuits.add(circuits.get(j));
                    }
                }
            } else if (idxA != -1 && idxA == idxB) {
                // Both endpoints already in the same circuit; do nothing
                newCircuits.addAll(circuits);
            } else {
                // Build new circuits based on single membership or none
                for (List<String> circuit : circuits) {
                    if (circuit.contains(connected.get(0)) && !circuit.contains(connected.get(1))) {
                        List<String> newCircuit = new ArrayList<>(circuit);
                        newCircuit.add(connected.get(1));
                        newCircuits.add(newCircuit);
                        merged = true;
                    } else if (circuit.contains(connected.get(1)) && !circuit.contains(connected.get(0))) {
                        List<String> newCircuit = new ArrayList<>(circuit);
                        newCircuit.add(connected.get(0));
                        newCircuits.add(newCircuit);
                        merged = true;
                    } else {
                        newCircuits.add(circuit);
                    }
                }
                if (!merged) {
                    newCircuits.add(new ArrayList<>(connected));
                }
            }
            circuits = newCircuits;
        }
        List<Long> biggestLengths = new ArrayList<>();
        for (List<String> circuit : circuits) {
            biggestLengths.add((long) circuit.size());
        }
        biggestLengths.sort(Comparator.reverseOrder());
        long firstLength = biggestLengths.getFirst();
        return firstLength * biggestLengths.get(1) * biggestLengths.get(2);
    }

    private static long getDist() {
        List<long[]> vectors = parseInput();
        long[][] arr = vectors.toArray(new long[vectors.size()][]);
        String[] keyss = keys.toArray(new String[0]);
        List<CustomVector> customVectors = new ArrayList<>();
        for (int i = 0; i < keyss.length; i++) {
            List<long[]> allButCurrent = new ArrayList<>(vectors);
            allButCurrent.remove(arr[i]);
            CustomVector customVector = new CustomVector(keyss[i]);
            for (long[] vector : allButCurrent) {
                double distance = getVectorDistance(arr[i], vector);
                String neighbor = formatVector(vector);
                customVector.addDistance(neighbor, distance);
            }
            customVectors.add(customVector);
        }

        Map<String, CustomVector> byName = new HashMap<>();
        for (CustomVector cv : customVectors) {
            byName.put(cv.getName(), cv);
        }

        double shortestDistance;
        List<List<String>> circuits = new ArrayList<>();
        List<String> connected;

        while (true) {
            shortestDistance = Double.MAX_VALUE;
            connected = new ArrayList<>(2);

            // pick current global shortest edge among remaining distances
            for (CustomVector customVector : customVectors) {
                Map<String, Double> dists = customVector.getDistances();
                if (dists == null || dists.isEmpty()) {
                    continue;
                }
                for (Map.Entry<String, Double> e : dists.entrySet()) {
                    double val = e.getValue();
                    if (Double.isNaN(val)) {
                        continue;
                    }
                    if (val < shortestDistance) {
                        shortestDistance = val;
                        connected.clear();
                        connected.add(customVector.getName());
                        connected.add(e.getKey());
                    }
                }
            }

            if (connected.size() != 2) {
                break;
            }

            // remove the chosen edge from both endpoints
            CustomVector target1 = byName.get(connected.get(0));
            CustomVector target2 = byName.get(connected.get(1));
            if (target1 != null) {
                target1.removeDistanceByNeighbor(connected.get(1));
            }
            if (target2 != null) {
                target2.removeDistanceByNeighbor(connected.get(0));
            }

            boolean merged = false;
            List<List<String>> newCircuits = new ArrayList<>();

            int idxA = -1, idxB = -1;
            for (int k = 0; k < circuits.size(); k++) {
                List<String> circuit = circuits.get(k);
                if (circuit.contains(connected.get(0))) {
                    idxA = k;
                }
                if (circuit.contains(connected.get(1))) {
                    idxB = k;
                }
            }
            if (idxA != -1 && idxB != -1 && idxA != idxB) {
                List<String> circuitA = circuits.get(idxA);
                List<String> circuitB = circuits.get(idxB);
                List<String> mergedCircuit = new ArrayList<>(circuitA.size() >= circuitB.size() ? circuitA : circuitB);
                List<String> otherCircuit = circuitA.size() >= circuitB.size() ? circuitB : circuitA;

                for (String n : otherCircuit) {
                    if (!mergedCircuit.contains(n)) {
                        mergedCircuit.add(n);
                    }
                }

                newCircuits.add(mergedCircuit);
                for (int j = 0; j < circuits.size(); j++) {
                    if (j != idxA && j != idxB) {
                        newCircuits.add(circuits.get(j));
                    }
                }
            } else if (idxA != -1 && idxA == idxB) {
                // Both endpoints already in the same circuit; do nothing
                newCircuits.addAll(circuits);
            } else {
                // Build new circuits based on single membership or none
                for (List<String> circuit : circuits) {
                    if (circuit.contains(connected.get(0)) && !circuit.contains(connected.get(1))) {
                        List<String> newCircuit = new ArrayList<>(circuit);
                        newCircuit.add(connected.get(1));
                        newCircuits.add(newCircuit);
                        merged = true;
                    } else if (circuit.contains(connected.get(1)) && !circuit.contains(connected.get(0))) {
                        List<String> newCircuit = new ArrayList<>(circuit);
                        newCircuit.add(connected.get(0));
                        newCircuits.add(newCircuit);
                        merged = true;
                    } else {
                        newCircuits.add(circuit);
                    }
                }
                if (!merged) {
                    newCircuits.add(new ArrayList<>(connected));
                }
            }
            circuits = newCircuits;
            if(circuits.getFirst().size() == keys.size()) {
                break;
            }
        }
        List<long[]> firstTwo = new ArrayList<>();
        for (String s : connected) {
            String[] parts = s.split(",");
            if (parts.length >= 2) {
                long x = Long.parseLong(parts[0].trim());
                long y = Long.parseLong(parts[1].trim());
                firstTwo.add(new long[]{x, y});
            }
        }
        return firstTwo.getFirst()[0] * firstTwo.get(1)[0];
    }

    private static String formatVector(long[] vector) {
        return Arrays.toString(vector)
                .replace(" ", "")
                .replace("[", "")
                .replace("]", "");
    }

    private static double getVectorDistance(long[] vector1, long[] vector2) {
        double xDiff = Math.pow(vector2[0] - vector1[0], 2);
        double yDiff = Math.pow(vector2[1] - vector1[1], 2);
        double zDiff = Math.pow(vector2[2] - vector1[2], 2);
        return Math.sqrt(xDiff + yDiff + zDiff);
    }

    private static List<long[]> parseInput() {
        String filePath = "src/input8.txt";

        List<long[]> vectors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                keys.add(line.trim());
                // Split the line into x, y, and z using "," as the delimiter
                String[] parts = line.trim().split(",");
                if (parts.length == 3) {
                    // Parse x, y, z as long and store them in a long array
                    long x = Long.parseLong(parts[0]);
                    long y = Long.parseLong(parts[1]);
                    long z = Long.parseLong(parts[2]);

                    vectors.add(new long[]{x, y, z});
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("error");
        }
        return vectors;
    }

    public static class CustomVector {
        private String name;
        private Map<String, Double> distances;

        public CustomVector(String name) {
            this.name = name;
            this.distances = new HashMap<>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void addDistance(String neighbor, Double distance) {
            distances.put(neighbor, distance);
        }

        public void removeDistanceByNeighbor(String neighbor) {
            distances.remove(neighbor);
        }

        public Map<String, Double> getDistances() {
            return distances;
        }
    }
}
