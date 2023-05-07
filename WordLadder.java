import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;


public class WordLadder {
    static class WordNode {
        String word;
        List<WordNode> neighbors;
        boolean visited;

        public WordNode(String word) {
            this.word = word;
            this.neighbors = new ArrayList<>();
            this.visited = false;
        }

        public void addNeighbor(WordNode neighbor) {
            this.neighbors.add(neighbor);
            neighbor.neighbors.add(this);
        }
    }
    public static void main(String[] args) {
        String start, end;      // The words on which the ladder is based
        Scanner keyboard = new Scanner(System.in);

        // The words in the file, WordNode is class to represent nodes in our graph
        HashMap<String, WordNode> wordlist = new HashMap<>();

        // Read in the two words
        System.out.println("Enter the beginning word");
        start = keyboard.next();
        System.out.println("Enter the ending word");
        end = keyboard.next();
        keyboard.close();

        // Check length of the words
        if (start.length() != end.length()) {
            System.err.println("ERROR! Words not the same length.");
            System.exit(1);
        }

        // Read in the appropriate file of words based on the length of start
        readFile(wordlist, start);

        // Search the graph
        List<String> path = breadthFirstSearch(wordlist, start, end);

        if (path == null) {
            System.err.println("No word ladder found");
            System.exit(1);
        }

        for (int index = 0; index < path.size(); index++) {
            if (index != path.size() - 1) {
                System.out.print(path.get(index) + " -> ");
            } else {
                System.out.println(path.get(index));
            }
        }
    }

    public static void readFile(HashMap<String, WordNode> wordlist, String start) {
        String filePath;
        int len = start.length();

        filePath = switch (len) {
            case 3 -> "Resources/words.3";
            case 4 -> "Resources/words.4";
            case 5 -> "Resources/words.5";
            case 6 -> "Resources/words.6";
            case 7 -> "Resources/words.7";
            case 8 -> "Resources/words.8.8";
            case 9 -> "Resources/words.9.9";
            default -> "Resources/words";
        };

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.length() != len) {
                    continue;
                }

                // Create a new WordNode for the word and add it to the wordlist
                WordNode node = new WordNode(line);
                wordlist.put(line, node);

                // Find all neighbors of the new word and add them to the graph
                for (String neighbor : wordlist.keySet()) {
                    if (isNeighbor(line, neighbor)) {
                        WordNode neighborNode = wordlist.get(neighbor);
                        node.addNeighbor(neighborNode);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR! Unable to read file: " + filePath);
            System.exit(1);
        }
    }

    static boolean isNeighbor(String word1, String word2) {
        int diffCount = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                diffCount++;
                if (diffCount > 1) {
                    return false;
                }
            }
        }
        return diffCount == 1;
    }

    public static List<String> breadthFirstSearch(HashMap<String, WordNode> wordlist, String start, String end) {
        // Check if start and end words are in the wordlist
        if (!wordlist.containsKey(start) || !wordlist.containsKey(end)) {
            return null;
        }

        // Create a queue for the breadth-first search
        Queue<WordNode> queue = new LinkedList<>();
        WordNode startNode = wordlist.get(start);
        startNode.visited = true;
        queue.offer(startNode);

        // Create a map to keep track of the path from the start word to each visited node
        Map<WordNode, WordNode> path = new HashMap<>();
        path.put(startNode, null);

        // Perform the breadth-first search
        while (!queue.isEmpty()) {
            WordNode currNode = queue.poll();
            if (currNode.word.equals(end)) {
                // Found the end word, so construct the path and return it
                List<String> result = new ArrayList<>();
                while (currNode != null) {
                    result.add(0, currNode.word);
                    currNode = path.get(currNode);
                }
                return result;
            }

            // Add all unvisited neighbors to the queue and update their paths
            for (WordNode neighbor : currNode.neighbors) {
                if (!neighbor.visited) {
                    neighbor.visited = true;
                    queue.offer(neighbor);
                    path.put(neighbor, currNode);
                }
            }
        }

        // Could not find a path from start to end
        return null;
    }
}
