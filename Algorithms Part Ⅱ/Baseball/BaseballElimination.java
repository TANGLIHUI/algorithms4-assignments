import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private int n = 0;
    private Map<String, Integer> teams = null;
    private ArrayList<String> name = null;
    private int[] wins = null;
    private int[] loss = null;
    private int[] left = null;
    private int[][] game = null;
    private boolean[] eliminate = null;
    private Map<String, List<String>> cert = null;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();
        teams = new HashMap<>(n);
        name = new ArrayList<String>();
        wins = new int[n];
        loss = new int[n];
        left = new int[n];
        game = new int[n][n];
        eliminate = new boolean[n];
        for (int i = 0; i < n; ++i) {
            name.add(in.readString());
            teams.put(name.get(i), i);
            wins[i] = in.readInt();
            loss[i] = in.readInt();
            left[i] = in.readInt();
            for (int j = 0; j < n; ++j)
                game[i][j] = in.readInt();
        }
        cert = new HashMap<>();
        if (n > 1)
            for (int i = 0; i < n; ++i) {
                List<String> eliminate_team = null;
                for (int j = 0; j < n; ++j) {
                    if (i != j && wins[i] + left[i] < wins[j]) {
                        if (eliminate_team == null) {
                            eliminate_team = new ArrayList<>();
                        }
                        eliminate_team.add(name.get(j));
                    }
                }
                if (eliminate_team == null) {
                    FlowNetwork network = generateFlowNetWork(i);
                    FordFulkerson maxFlow = new FordFulkerson(network, n, n + 1);
                    for (int k = 0; k < n; ++k) {
                        if (k != i)
                            if (maxFlow.inCut(k)) {
                                if (eliminate_team == null)
                                    eliminate_team = new ArrayList<>();
                                eliminate_team.add(name.get(k));
                            }
                    }
                }
                if (eliminate_team != null) {
                    eliminate[i] = true;
                    cert.put(name.get(i), eliminate_team);
                }
            }
    }

    private FlowNetwork generateFlowNetWork(int team) {
        int vs = n * n;
        FlowNetwork network = new FlowNetwork(vs);
        int cnt = n + 2;
        for (int i = 0; i < n; ++i) {
            if (i == team)
                continue;
            FlowEdge edge = new FlowEdge(i, n + 1, wins[team] + left[team] - wins[i]);
            network.addEdge(edge);
            for (int j = i + 1; j < n; ++j) {
                if (team == j)
                    continue;
                FlowEdge edgeS = new FlowEdge(n, cnt, game[i][j]);
                network.addEdge(edgeS);
                FlowEdge edgeH = new FlowEdge(cnt, i, Double.MAX_VALUE);
                network.addEdge(edgeH);
                FlowEdge edgeO = new FlowEdge(cnt, j, Double.MAX_VALUE);
                network.addEdge(edgeO);
                ++cnt;
            }
        }
        return network;
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return name;
    }

    // number of wins for given team
    public int wins(String team) {
        validate(team);
        return wins[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        validate(team);
        return loss[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validate(team);
        return left[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validate(team1);
        validate(team2);
        return game[teams.get(team1)][teams.get(team2)];
    }

    private void validate(String team) {
        if (!teams.containsKey(team))
            throw new IllegalArgumentException();
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        validate(team);
        return eliminate[teams.get(team)];
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validate(team);
        return cert.get(team);
    }

    public static void main(String[] args) {
        String filename = "E:/WorkSpace1/algorithm/src/baseball/teams4.txt";
        BaseballElimination division = new BaseballElimination(filename);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}