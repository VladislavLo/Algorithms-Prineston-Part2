package Week1;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet WordNet;

    public Outcast(WordNet wordnet) {
        WordNet = wordnet;
    }
    public String outcast(String[] nouns) {
        int[] distances = new int[nouns.length];
        for (int i = 0; i < nouns.length - 1; i++) {
            for (int j = i + 1; j < nouns.length; j++) {
                int distance = WordNet.distance(nouns[i], nouns[j]);
                distances[i] += distance;
                distances[j] += distance;
            }
        }
        int localMax = distances[0];
        int maxIndex = 0;
        for (int i = 1; i < distances.length; i++) {
            if (localMax < distances[i]) {
                maxIndex = i;
                localMax = distances[i];
            }
        }
        return nouns[maxIndex];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
