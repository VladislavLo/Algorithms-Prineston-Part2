package Week4;

import Week4.BoggleBoard;

import java.util.ArrayList;
import java.util.HashSet;

public class BoggleSolver {
    private int[] delta_y = { -1, -1, -1, 0, 0, 0, 1, 1, 1 };
    private int[] delta_x = { -1, 0, 1, -1, 0, 1, -1, 0, 1 };
    private final int R = 26;
    private final TrieNode root = new TrieNode();
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary) {
            TrieNode current = root;
            for (int i = 0; i < word.length(); i++) {
                int letterIndex = word.charAt(i) - 'A';
                if (current.next[letterIndex] == null) {
                    current.next[letterIndex] = new TrieNode();
                }
                current = current.next[letterIndex];
            }
            current.isEnd = true;
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int N = board.rows();
        int M = board.cols();
        boolean visited[][] = new boolean[N][M];
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                dfs(i, j, root, board, visited, "", set);
            }
        }
        return set;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (contains(word)) {
            return getScore(word);
        }
        else {
            return 0;
        }
    }

    private void dfs(int i,
                     int j,
                     TrieNode node,
                     BoggleBoard board,
                     boolean[][] visited,
                     String word,
                     HashSet<String> words) {
        char letter = board.getLetter(i, j);
        if (node == null || node.next[letter - 'A'] == null) {
            return;
        }

        String newWord;
        if (letter == 'Q') {
            newWord = word + "QU";
            node = node.next['Q' - 'A'];
            if (node.next['U' - 'A'] == null) {
                return;
            }
            node = node.next['U' - 'A'];
        }
        else {
            newWord = word + letter;
            node = node.next[letter - 'A'];
        }

        if (node.isEnd && newWord.length() > 2) {
            words.add(newWord);
        }

        visited[i][j] = true;
        Iterable<Cell> cells = getNextCells(i, j, board.rows(), board.cols(), visited);
        for (Cell cell : cells) {
            dfs(cell.row, cell.column, node, board, visited, newWord, words);
        }
        visited[i][j] = false;
    }

    private Iterable<Cell> getNextCells(int i,
                                       int j,
                                       int height,
                                       int width,
                                       boolean[][] visited) {
        ArrayList<Cell> result = new ArrayList<>();
        for (int k = 0; k < delta_x.length; k++) {
            int row = i + delta_y[k];
            int column = j + delta_x[k];
            if (row == i && column == j) continue;
            if (row >= 0 && row < height && column >= 0 && column < width
                    && !visited[row][column]) {
                Cell element = new Cell(row, column);
                result.add(element);
            }
        }
        return result;
    }

    private class TrieNode {
        public TrieNode[] next = new TrieNode[R];
        public boolean isEnd = false;
    }

    private class Cell {
        public Cell(int i, int j) {
            row = i;
            column = j;
        }

        public final int row;
        public final int column;
    }

    private boolean contains(String s) {
        TrieNode node = root;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (node.next[c - 'A'] == null) {
                return false;
            }
            node = node.next[c - 'A'];
        }
        return node.isEnd;
    }

    private int getScore(String s) {
        int len = s.length();

        if (len <= 2) {
            return 0;
        }
        else if (len <= 4) {
            return 1;
        }
        else if (len == 5) {
            return 2;
        }
        else if (len == 6) {
            return 3;
        }
        else if (len == 7) {
            return 5;
        }
        else {
            return 11;
        }
    }
}
