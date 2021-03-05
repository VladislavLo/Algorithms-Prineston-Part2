package Week1;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    // constructor takes the name of th> two input files
    private HashMap<Integer, String> id2words = new HashMap<>();
    private HashMap<String, ArrayList<Integer>> word2ids = new HashMap<>();
    private SAP sap;


    public WordNet(String synsets, String hypernyms) {
        readSynsets(synsets);
        readHypernyms(hypernyms);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return word2ids.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("word is null");
        }
        return word2ids.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("not existing in wordnet noun.");
        }
        ArrayList<Integer> idsA = word2ids.get(nounA);
        ArrayList<Integer> idsB = word2ids.get(nounB);
        return sap.length(idsA, idsB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("not existing in wordnet noun.");
        }
        ArrayList<Integer> idsA = word2ids.get(nounA);
        ArrayList<Integer> idsB = word2ids.get(nounB);
        int ancestor = sap.ancestor(idsA, idsB);
        return id2words.get(ancestor);
    }

    private void readSynsets(String path) {
        In in = new In(path);
        String line = in.readLine();
        while (line != null) {
            String[] parts = line.split(",");
            if (parts.length < 2) {
                continue;
            }
            Integer id = Integer.parseInt(parts[0]);
            String words = parts[1];
            id2words.put(id, words);
            String[] splitted = words.split(" ");
            for (String word : splitted) {
                ArrayList<Integer> values = word2ids.get(word);
                if (values != null) {
                    values.add(id);
                }
                else {
                    ArrayList<Integer> ids = new ArrayList<>();
                    ids.add(id);
                    word2ids.put(word, ids);
                }
            }
            line = in.readLine();
        }
    }

    private void readHypernyms(String path) {
        In in = new In(path);
        Digraph digraph = new Digraph(id2words.size());
        String line = in.readLine();
        while (line != null) {
            String[] parts = line.split(",");
            Integer edge = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                digraph.addEdge(edge, Integer.parseInt(parts[i]));
            }
            line = in.readLine();
        }
        checkDigraphCorrectness(digraph);
        sap = new SAP(digraph);
    }

    private void checkDigraphCorrectness(Digraph g) {
        DirectedCycle cycle = new DirectedCycle(g);
        if (cycle.hasCycle()) {
            throw new IllegalArgumentException("Cycle detected");
        }
        int roots = 0;
        for (int i = 0; i < g.V(); i++) {
            if (g.outdegree(i) == 0) {
                roots++;
            }
        }
        if (roots != 1) {
            throw new IllegalArgumentException("Invalid number of roots");
        }
    }

    public static void main(String[] args) {
        if (args.length > 1) {
            WordNet wordNet = new WordNet(args[0], args[1]);
            wordNet.isNoun("a");
        }
    }
}
