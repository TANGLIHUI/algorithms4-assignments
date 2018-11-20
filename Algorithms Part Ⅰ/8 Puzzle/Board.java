import java.util.ArrayList;
import java.util.Iterator;

import edu.princeton.cs.algs4.StdRandom;

/**    
* @Title: Board.java  
* @Package   
* @Description: TODO
* @author tang 1101520766@qq.com 
* @date 2017年12月26日 下午8:14:30  
* @version V1.0  
*/

public class Board {
    private final int[][] blocks;
    private int man = -1;
    private int ham = -1;
    private Board twin = null;

    public Board(int[][] blocks) {
        // construct a board from an n-by-n array of blocks
        this.blocks = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; ++i)
            for (int j = 0; j < blocks[i].length; ++j)
                this.blocks[i][j] = blocks[i][j];
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension() {
        // board dimension n
        return blocks.length;
    }

    public int hamming() {
        // number of blocks out of placefor (int i = 0; i < blocks.length; ++i)
        if (ham == -1) {
            ham = 0;
            for (int i = 0; i < blocks.length; ++i)
                for (int j = 0; j < blocks[i].length; ++j)
                    if (blocks[i][j] != 0 && blocks[i][j] != i * blocks[i].length + j + 1)
                        ++ham;
        }
        return ham;
    }

    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        if (man == -1) {
            man = 0;
            for (int i = 0; i < blocks.length; ++i)
                for (int j = 0; j < blocks[i].length; ++j)
                    if (blocks[i][j] != 0 && blocks[i][j] != i * blocks[i].length + j + 1) {
                        int row = blocks[i][j] / blocks.length;
                        int col = blocks[i][j] % blocks.length - 1;
                        if (col == -1) {
                            col = blocks.length - 1;
                            row--;
                        }
                        man += Math.abs(row - i) + Math.abs(col - j);
                    }
        }
        return man;
    }

    public boolean isGoal() {
        // is this board the goal board?
        return hamming() == 0;
    }

    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
        if (twin == null) {
            twin = new Board(blocks);
            int x1 = StdRandom.uniform(blocks.length);
            int y1 = StdRandom.uniform(blocks.length);
            int x2 = StdRandom.uniform(blocks.length);
            int y2 = StdRandom.uniform(blocks.length);
            while (x1 == x2 && y1 == y2 || blocks[x1][y1] == 0 || blocks[x2][y2] == 0) {
                if (blocks[x1][y1] == 0 || x1 == x2 && y1 == y2) {
                    x1 = StdRandom.uniform(blocks.length);
                    y1 = StdRandom.uniform(blocks.length);
                }
                if (blocks[x2][y2] == 0 || x1 == x2 && y1 == y2) {
                    x2 = StdRandom.uniform(blocks.length);
                    y2 = StdRandom.uniform(blocks.length);
                }
            }
            twin.blocks[x1][y1] = blocks[x2][y2];
            twin.blocks[x2][y2] = blocks[x1][y1];
        }
        return twin;
    }

    public boolean equals(Object y) {
        // does this board equal y?
        if (y == null || y.getClass() != this.getClass())
            return false;
        int[][] that = ((Board) y).blocks;
        for (int i = 0; i < blocks.length; ++i) {
            if (blocks.length != that.length)
                return false;
            for (int j = 0; j < blocks[i].length; ++j) {
                if (blocks.length != that[i].length)
                    return false;
                if (that[i][j] != blocks[i][j])
                    return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        // all neighboring boards
        return new Iterable<Board>() {
            private ArrayList<Board> list = new ArrayList<>();

            private boolean valite(int i, int j) {
                return i >= 0 && j >= 0 && i < blocks.length && j < blocks.length;
            }

            @Override
            public Iterator<Board> iterator() {
                int x = 0, y = 0;
                for (int i = 0; i < blocks.length; ++i)
                    for (int j = 0; j < blocks[i].length; ++j)
                        if (blocks[i][j] == 0) {
                            x = i;
                            y = j;
                            break;
                        }
                int[][] pos = { { x, y - 1 }, { x, y + 1 }, { x - 1, y }, { x + 1, y } };
                for (int i = 0; i < 4; ++i)
                    if (valite(pos[i][0], pos[i][1])) {
                        Board board = new Board(blocks);
                        board.blocks[pos[i][0]][pos[i][1]] = blocks[x][y];
                        board.blocks[x][y] = blocks[pos[i][0]][pos[i][1]];
                        list.add(board);
                    }
                return list.iterator();
            }
        };
    }

    public String toString() {
        // string representation of this board (in the output format specified
        // below)
        String string = "" + blocks.length + "\n";
        for (int i = 0; i < blocks.length; ++i) {
            for (int j = 0; j < blocks[i].length; ++j)
                string += " " + blocks[i][j] + " ";
            string += "\n";
        }
        return string;
    }

    public static void main(String[] args) {
        // unit tests (not graded)
        int[][] a = { { 1, 2, 3, 4, 5 }, { 6, 7, 8, 9, 10 }, { 11, 12, 13, 0, 14 }, { 16, 17, 18, 19, 15 },
                { 21, 22, 23, 24, 20 } };
        int[][] b = { { 1, 2, 3, 4, 5 }, { 6, 7, 8, 9, 10 }, { 11, 12, 13, 19, 14 }, { 16, 17, 18, 15, 0 },
                { 21, 22, 23, 24, 20 } };
        Board board = new Board(a);
        System.out.println(board);
        System.out.println(board.manhattan());
        System.out.println(board.hamming());
        board = new Board(b);
        System.out.println(board);
        System.out.println(board.manhattan());
        System.out.println(board.hamming());
        System.out.println(board.isGoal());
    }
}
