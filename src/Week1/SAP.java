package Week1;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph G;

    public SAP(Digraph G) {
        this.G = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        return calculatePathLengthToNearestAncestor(bfsv, bfsw);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        return searchNearestAncestor(bfsv, bfsw);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        return calculatePathLengthToNearestAncestor(bfsv, bfsw);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);
        int distToAncestor = INFINITY;
        int ancestorVertex = -1;
        for (int i = 0; i < G.V(); i++) {
            if (!bfsv.hasPathTo(i) || !bfsw.hasPathTo(i)) {
                continue;
            }
            int distToCurrentAncestor = bfsv.distTo(i) + bfsw.distTo(i);
            if (distToCurrentAncestor < distToAncestor) {
                distToAncestor = distToCurrentAncestor;
                ancestorVertex = i;
            }
        }
        return ancestorVertex;
    }

    private int searchNearestAncestor(BreadthFirstDirectedPaths bfsv, BreadthFirstDirectedPaths bfsw) {
        int distToAncestor = INFINITY;
        int ancestorVertex = -1;
        for (int i = 0; i < G.V(); i++) {
            if (!bfsv.hasPathTo(i) || !bfsw.hasPathTo(i)) {
                continue;
            }
            int distToCurrentAncestor = bfsv.distTo(i) + bfsw.distTo(i);
            if (distToCurrentAncestor < distToAncestor) {
                distToAncestor = distToCurrentAncestor;
                ancestorVertex = i;
            }
        }
        return ancestorVertex;
    }

    private int calculatePathLengthToNearestAncestor(BreadthFirstDirectedPaths bfsv, BreadthFirstDirectedPaths bfsw) {
        int distToAncestor = INFINITY;
        for (int i = 0; i < G.V(); i++) {
            if (!bfsv.hasPathTo(i) || !bfsw.hasPathTo(i)) {
                continue;
            }
            int distToCurrentAncestor = bfsv.distTo(i) + bfsw.distTo(i);
            if (distToCurrentAncestor < distToAncestor) {
                distToAncestor = distToCurrentAncestor;
            }
        }
        return distToAncestor == INFINITY ? -1 : distToAncestor;
    }

    private void validateVertex(int v) {
        int V = G.V();
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V-1));
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new NullPointerException("argument is null");
        }
        int V = G.V();
        for (int v : vertices) {
            validateVertex(v);
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
