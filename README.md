<h1>Word Ladder</h1>



<!-- Program Description -->
This program allows you to find a word ladder between two given words using breadth-first search. A word ladder is a sequence of words formed by changing one letter at a time, where each intermediate word in the sequence is a valid word defined by a word bank.



<!-- How It Works -->
<h2><b>How It Works</b></h2>
<p>This program uses a graph to represent a dictionary of words. Each word is represented as a node in the graph, and two nodes are connected by an edge if the words differ by exactly one letter. The breadth-first search algorithm is used to find the shortest path from the starting word to the ending word in this graph.</p>

<p>The program first checks if the words are of the same character length. It then reads in the file containing a list of words for that character length. Next, it constructs a graph of nodes representing these words, and finds the shortest path from the starting word to the ending word, using the breadth-first seach algorithm.</p>



<!-- Limitations -->
<h2><b>Limitations</b></h2>
<p>This program has some limitations:</p>

- Both input words must have the same character length.
- It only works for words contained in the word bank.
  - A word bank exists for each character length between 3 and 9, inclusively.
  - A default word bank exists for character lengths, outside of the above range.
- It only finds the shortest path between two words, if there is one.
