import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;

/**    
* @Title: WordNet.java  
* @Package edu.princeton.cs.algs4  
* @Description: TODO
* @author tang 1101520766@qq.com 
* @date 2018年1月5日 下午5:12:50  
* @version V1.0  
*/
public class WordNet {
    private Digraph graph = null;
    private List<String> synonyms = null;
    private Map<String, Set<Integer>> wordsIndexMp = null;
    private SAP sap = null;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        validate(synsets);
        validate(hypernyms);
        In in = new In(synsets);
        String[] lines = in.readAllLines();
        graph = new Digraph(lines.length);
        synonyms = new ArrayList<>();
        wordsIndexMp = new HashMap<>();
        for (int i = 0; i < lines.length; ++i) {
            String[] items = lines[i].split(",");
            synonyms.add(items[1]);
            String[] words = items[1].split(" ");
            for (String word : words) {
                if (wordsIndexMp.containsKey(word)) {
                    wordsIndexMp.get(word).add(i);
                } else {
                    Set<Integer> set = new HashSet<>();
                    set.add(i);
                    wordsIndexMp.put(word, set);
                }
            }
        }
        in = new In(hypernyms);
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] item = line.split(",");
            int first = Integer.parseInt(item[0]);
            for (int i = 1; i < item.length; ++i) {
                int n = Integer.parseInt(item[i]);
                if (n != first)
                    graph.addEdge(first, n);
            }
        }
        if (new DirectedCycle(graph).hasCycle())
            throw new IllegalArgumentException();
        int cnt = 0;
        for (int v = 0; v < graph.V(); ++v) {
            if (!graph.adj(v).iterator().hasNext()) {
                if (++cnt > 1)
                    throw new IllegalArgumentException();
            }
        }
        sap = new SAP(graph);
    }

    private void validate(Object object) {
        if (object == null)
            throw new IllegalArgumentException();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordsIndexMp.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        validate(word);
        return wordsIndexMp.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validate(nounA);
        validate(nounB);
        if (!(isNoun(nounA) && isNoun(nounB)))
            throw new IllegalArgumentException();
        return sap.length(wordsIndexMp.get(nounA), wordsIndexMp.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of
    // nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validate(nounA);
        validate(nounB);
        if (!(isNoun(nounA) && isNoun(nounB)))
            throw new IllegalArgumentException();
        int index = sap.ancestor(wordsIndexMp.get(nounA), wordsIndexMp.get(nounB));
        if (index == -1)
            return null;
        return synonyms.get(index);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String synsets = "E:/WorkSpace1/algorithm/wordnet/synsets.txt";
        String hypernyms = "E:/WorkSpace1/algorithm/wordnet/hypernymsManyPathsOneAncestor.txt";
        WordNet wordNet = new WordNet(synsets, hypernyms);

        // String[] words = new
        // In("E:/WorkSpace1/algorithm/wordnet/outcast8.txt").readAllStrings();
        // for (String s : words) {
        // for (String s1 : words) {
        // System.out.print(wordNet.distance(s, s1) + " ");
        // }
        // System.out.print(s);
        // System.out.println();
        // }
    }
}
