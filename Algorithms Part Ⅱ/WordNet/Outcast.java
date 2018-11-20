import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**    
* @Title: Outcast.java  
* @Package   
* @Description: TODO
* @author tang 1101520766@qq.com 
* @date 2018年1月5日 下午5:14:43  
* @version V1.0  
*/

public class Outcast {
    private WordNet wordNet;

    public Outcast(WordNet wordnet) {
        // constructor takes a WordNet object
        this.wordNet = wordnet;
    }

    public String outcast(String[] nouns) {
        // given an array of WordNet nouns, return an outcast
        int[][] dis = new int[nouns.length][nouns.length];
        for (int i = 0; i < nouns.length; ++i) {
            dis[i][i] = 0;
            for (int j = i + 1; j < nouns.length; ++j) {
                dis[i][j] = dis[j][i] = wordNet.distance(nouns[i], nouns[j]);
            }
        }
        int max = 0, index = 0;
        for (int i = 0; i < nouns.length; ++i) {
            int sum = 0;
            for (int j = 0; j < nouns.length; ++j)
                sum += dis[i][j];
            if (max < sum) {
                max = sum;
                index = i;
            }
        }
        return nouns[index];
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
