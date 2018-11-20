import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

/**    
* @Title: Solver.java  
* @Package   
* @Description: TODO
* @author tang 1101520766@qq.com 
* @date 2017年12月26日 下午8:24:00  
* @version V1.0  
*/
public class Solver {
    private int move = -1;
    private boolean solvable = false;
    private ArrayList<Board> solution = new ArrayList<>();
    private Board initial = null;
    private Board modified = null;
    private static final Comparator<Node> MAN = new Comparator<Node>() {
        @Override
        public int compare(Node o1, Node o2) {
            if (o1.man < o2.man)
                return -1;
            else if (o1.man > o2.man)
                return 1;
            return 0;
        }
    };

    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        if (initial == null)
            throw new IllegalArgumentException();
        this.initial = initial;
        modified = initial.twin();
        solve();
    }

    private class Node {
        private Board board = null;
        private Node prev = null;
        private int moves = 0;
        private int man = 0;
        private int ham = 0;

        public Node(Board board, Node prev, int moves) {
            this.board = board;
            this.prev = prev;
            this.moves = moves;
            this.man = moves + board.manhattan();
            this.ham = moves + board.hamming();
        }

        public boolean visited(Board board) {
            Node p = prev;
            if (p != null && p.board.equals(board))
                return true;
            return false;
        }
    }

    private void solve() {
        MinPQ<Node> init = new MinPQ<>(MAN);
        init.insert(new Node(initial, null, 0));
        MinPQ<Node> twin = new MinPQ<>(MAN);
        twin.insert(new Node(modified, null, 0));
        Node cur = null;
        while (!init.isEmpty() && !twin.isEmpty()) {
            cur = init.delMin();
            if (cur.board.isGoal()) {
                solvable = true;
                break;
            }
            step(init, cur);
            cur = twin.delMin();
            if (cur.board.isGoal()) {
                solvable = false;
                break;
            }
            step(twin, cur);
        }
        if (solvable) {
            while (cur != null) {
                solution.add(0, cur.board);
                cur = cur.prev;
            }
            move = solution.size() - 1;
        }
    }

    private void step(MinPQ<Node> pq, Node cur) {
        for (Board next : cur.board.neighbors()) {
            if (!cur.visited(next)) {
                Node node = new Node(next, cur, cur.moves + 1);
                pq.insert(node);
            }
        }
    }

    public boolean isSolvable() {
        // is the initial board solvable?
        return solvable;
    }

    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
        return move;
    }

    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        if (solvable)
            return new Iterable<Board>() {
                @Override
                public Iterator<Board> iterator() {
                    return solution.iterator();
                }
            };
        return null;
    }

    public static void main(String[] args) {
        // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board + "" + board.hamming());
        }
    }
}
