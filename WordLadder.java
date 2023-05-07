import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;

/**
 * A class that represents a word ladder between two words by creating a graph of words, then using breadth-first search
 * to find the shortest path between them.
 * <p>The WordLadder class also defines a WordNode nested class that represents a node in the graph.</p>
 * @author Adam Zieman
 */
public class WordLadder {

    /**
     * A class that represents a node in a graph of words.
     * <p>Each WordNode has a word, a list of neighboring WordNodes, and a boolean value that indicates whether it has
     * been visited.</p>
     */
    static class WordNode {
        /**
         * The word represented by this node.
         */
        String word;

        /**
         * A list of neighboring WordNodes.
         */
        List<WordNode> neighbors;

        /**
         * A boolean value that indicates whether this WordNode has been visited.
         */
        boolean visited;

        /**
         * Constructs a new WordNode object with the specified word.
         * <p>The list of neighboring nodes is initialized as an empty ArrayList, and the visited status is set to
         * false.</p>
         * @param word the word to be represented by this WordNode object.
         */
        public WordNode(String word) {
            this.word = word;
            this.neighbors = new ArrayList<>();
            this.visited = false;
        }

        /**
         * Adds a neighboring WordNode to this node's list of neighbors, and adds this node to the neighboring node's
         * list of neighbors as well.
         * @param neighbor the WordNode to be added as a neighbor to this node.
         */
        public void addNeighbor(WordNode neighbor) {
            this.neighbors.add(neighbor);
            neighbor.neighbors.add(this);
        }
    }

    /**
     * Main method for finding a word ladder between two words.
     * <p>Prompts the user to input the start and end words, reads in the appropriate file of words based on the length
     * of start, creates a graph with the words, and uses breadth-first search to find a path from start to end.</p>
     * @param args an array of command-line arguments for the application. Not utilized in this program!
     */
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


        /* Iterate over each word in the path and print with separator if it's not the last word, otherwise print alone
        on a new line. */
        for (int index = 0; index < path.size(); index++) {
            if (index != path.size() - 1) {
                System.out.print(path.get(index) + " -> ");
            } else {
                System.out.println(path.get(index));
            }
        }
    }

    /**
     * Reads a file containing a list of words of a specific length and populates a given HashMap with WordNode objects.
     * @param wordlist a HashMap containing WordNode objects to be populated with new WordNode objects.
     * @param start the starting word that determines the length of the words to be read from the file.
     * @throws RuntimeException if the file specified by the file path cannot be read
     */
    public static void readFile(HashMap<String, WordNode> wordlist, String start) throws RuntimeException {
        // Determine the file path based on the length of the starting word
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

        // Read each word from the file and add its WordNode object to the wordlist
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
        }
        // Throw a runtime exception if the file cannot be read
        catch (Exception e) {
            throw new RuntimeException("ERROR! Unable to read file: " + filePath, e);
        }
    }

    /**
     * Checks whether two words are neighbors by comparing the number of different characters between them.
     * @param word1 the first word to check for being a neighbor of the second word.
     * @param word2 the second word to check for being a neighbor of the first word.
     * @return 'true' if the number of differing characters between word1 and word2 is 1. Otherwise, 'false'.
     */
    static boolean isNeighbor(String word1, String word2) {
        int diffCount = 0; // Initialize the count of differing characters to 0

        // Loop through each character of the first word
        for (int i = 0; i < word1.length(); i++) {

            // Increments a counter for every character different from word1 to word2
            if (word1.charAt(i) != word2.charAt(i)) {
                diffCount++;

                // Return false and exit the function if the counter exceeds 1
                if (diffCount > 1) {
                    return false;
                }
            }
        }

        // Returns 'true' if there is 1 different character between word1 and word2. Otherwise, returns 'false'.
        return diffCount == 1;
    }

    /**
     * Performs a breadth-first search algorithm to find the shortest path between two given words.
     * @param wordlist a HashMap containing WordNodes as values, with the word string as key.
     * @param start the starting word of the search.
     * @param end the ending word of the search.
     * @return a List of words representing the shortest path from start to end, if one exists. Otherwise, null.
     */
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

            // Adds all unvisited neighbors to the queue and update their paths
            for (WordNode neighbor : currNode.neighbors) {
                if (!neighbor.visited) {
                    neighbor.visited = true;
                    queue.offer(neighbor);
                    path.put(neighbor, currNode);
                }
            }
        }

        // Return null if a path from start to end could not be found.
        return null;
    }
}
