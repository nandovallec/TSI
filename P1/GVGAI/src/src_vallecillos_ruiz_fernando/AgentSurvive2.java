package src_vallecillos_ruiz_fernando;

import java.util.*;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

//0 is up
//1 is right
//2 is down
//3 is left

public class AgentSurvive2 extends AbstractPlayer {
    /**
     * Path Calculator
     */
    AStar as;

    /**
     * Scale between pixels and coordinates
     */
    Vector2d fescala;

    /**
     * Variables to keep track of the whole map, and elements in them
     */
    int [][] mapa;
    int n_monster = 0;
    boolean hay_monster = false;
    PairInteger[] monster;
    int n_gemas;
    PairInteger[] gemas;
    PairInteger pos_actual = new PairInteger(0,0);
    PairInteger portal = new PairInteger(0,0);

    /**
     * Stack of the actions to return
     */
    Stack<Types.ACTIONS> []devolver;
    int devolver_index = 0;

    /**
     * Order of the search for gems and exit
     */
    int [] order_search;

    /**
     * Variables to help calculate the order of the gem
     */
    int best_distance = 999;
    int [] indexes;
    int [][]dist;

    /**
     * Variables to aid the search in special cases
     */
    boolean recalculate = false;
    int iterations = 0;
    int unlock_change = 0;
    boolean destino_final_block = false;

    /**
     * Calulate the optimal path from the initial point that passes through every gem
     * @param part_sol Keeps tracks of the partial solution
     * @param chosen Keeps which positions have been chosen before
     * @param ac_dist Accumulated distance up to the node
     * @param level Depth level of the tree
     */
    public void branchBound(int [] part_sol, boolean [] chosen, int ac_dist, int level) {
        // Prune the rest of the tree solution is not viable
        if(ac_dist> best_distance)
            return;

        // If there is a complete solution, compare it to the best one yet
        if(level == part_sol.length-1){
            ac_dist += dist[part_sol[part_sol.length-2]][part_sol[part_sol.length-1]];
            if(ac_dist < best_distance){
                best_distance = ac_dist;
                order_search = part_sol.clone();
            }
            return;
        }

        // Advance to next level with every possible path
        for(int  i = 1; i < indexes.length-1; i++){
            if (chosen[i] == false) {
                int sel_index = indexes[i];
                chosen[sel_index] = true;
                part_sol[level] = sel_index;
                branchBound(part_sol, chosen, ac_dist + dist[part_sol[level-1]][sel_index], level + 1);
                chosen[sel_index] = false;
                part_sol[level] = -1;

            }
        }
        return;
    }

    /**
     * Calulate the optimal path from the initial point that passes through every gem using
     * the branch and bound method.
     */
    void orderGemas(){
        // Create distance matrix and fill it
        dist = new int[n_gemas+2][n_gemas+2];
        for(int i = 0; i < n_gemas+2; i++){
            dist[i][i] = 9999;
            for(int j = i+1; j< gemas.length; j++){
                int row_dif = Math.abs(gemas[i].row - gemas[j].row);
                int col_dif = Math.abs(gemas[i].col - gemas[j].col);

                int d = (int) (row_dif + col_dif);

                dist[i][j] = d;
                dist[j][i] = d;
            }
        }

        // Use branch and bound method
        boolean [] chosen = new boolean[n_gemas+2];
        int [] sol = new int[n_gemas+2];
        sol[n_gemas+1] = n_gemas+1;
        chosen[0] = true;
        chosen[chosen.length-1] = true;
        sol[sol.length-1] = sol[sol.length-1];
        order_search = sol.clone();
        for(int i = 1; i < indexes.length-1; i++){
            int sel_index = indexes[i];
            chosen[sel_index] = true;
            sol[1] = sel_index;
            branchBound(sol, chosen, dist[0][sel_index], 2);
            chosen[sel_index] = false;

        }

    }

    /**
     * Pair class to locate an object in the grid
     */
    static class PairInteger {
        public int row, col;

        PairInteger(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    /**
     * Node class for convenience
     */
    static class Node {
        public Node parent;
        public int col, row;
        public double g;
        public double h;

        Node(int row, int col, double g, double h) {
            this.row = row;
            this.col = col;
            this.g = g;
            this.h = h;
        }

        @Override
        public boolean equals(Object obj) {
            Node o = (Node) obj;
            return (this.row == o.row) && (this.col == o.col);
        }

        @Override
        public int hashCode() {
            return 31 * this.row + 17 * this.col;
        }


    }

    /**
     * Path Calculator Class
     */
    class AStar {
        /**
         * Variables to keep open and closed nodes
         */
        private final PriorityQueue<Node> open;
        private final Set<Node> closed;

        /**
         * Matrix symbolizing map
         */
        private final int[][] maze;

        /**
         * Matrix indicating orientation in each cell
         * 0 up
         * 1 right
         * 2 down
         * 3 left
         */
        private int[][] directions;

        /**
         * Analyzing node
         */
        private Node now;

        /**
         * Start and finish positions
         */
        int rowStart, colStart;
        private int rowEnd, colEnd;

        /**
         * Variable for heuristic. The greater the safer but it will take longer.
         */
        int heuristic_limit = 8;


        /**
         * Creates path calculator
         * @param maze matrix identifying positions with what's inside it
         * @param row
         * @param col
         * @param dir orientation
         */
        AStar(int[][] maze, int row, int col, int dir) {
            this.open = new PriorityQueue<Node>(new Comparator<Node>() {
                @Override
                public int compare(Node a, Node b) {
                    return Integer.compare((int) (a.g + a.h), (int) (b.g + b.h));
                }
            });
            this.closed = new HashSet<>();
            this.maze = maze;
            this.directions = new int[maze.length][maze[0].length];
            this.now = new Node(row, col, 0, 0);
            directions[row][col] = dir;
            this.rowStart = row;
            this.colStart = col;
        }

        /**
         * Restart the variables to calculate a path from new positions
         * @param row
         * @param col
         * @param dir orientation
         */
        public void newStart(int row, int col, int dir) {
            this.rowStart = row;
            this.colStart = col;
            directions[row][col] = dir;
            open.clear();
            closed.clear();
            this.now = new Node(row, col, 0, 0);


        }

        /**
         * Finds path to rowEnd/colEnd and insert it into a stack
         * @param rowEnd Coordinates of the target position
         * @param colEnd
         * @param stack_index Index pointing the stack where the path should be saved
         * @return Orientation of avatar after path or -1 if couldn't find it
         */
        public int findPathTo(int rowEnd, int colEnd, int stack_index) {
            this.rowEnd = rowEnd;
            this.colEnd = colEnd;

            this.closed.add(this.now);
            // Start expanding until path found
            addNeigborsToOpenList();
            while (this.now.row != this.rowEnd || this.now.col != this.colEnd) {

                if (this.open.isEmpty()) { // No more nodes
                    return -1;
                }

                this.now = this.open.poll(); // get first node (lowest f score)
                this.closed.add(this.now); // and add to the closed

                addNeigborsToOpenList();
            }

            int back_col = this.now.col;
            int back_row = this.now.row;

            // Trace back the path and insert actions
            while (back_col != this.colStart || back_row != this.rowStart) {
                int d = directions[back_row][back_col];
                int new_d;
                if (d == 0) {
                    back_row += 1;
                    devolver[stack_index].push(Types.ACTIONS.ACTION_UP);
                    new_d = directions[back_row][back_col];
                    if(new_d != d){
                        devolver[stack_index].push(Types.ACTIONS.ACTION_UP);

                    }
                }else if (d == 2) {
                    back_row -= 1;
                    devolver[stack_index].push(Types.ACTIONS.ACTION_DOWN);
                    new_d = directions[back_row][back_col];
                    if(new_d != d){
                        devolver[stack_index].push(Types.ACTIONS.ACTION_DOWN);

                    }
                }else if (d == 1) {
                    back_col -= 1;
                    devolver[stack_index].push(Types.ACTIONS.ACTION_RIGHT);
                    new_d = directions[back_row][back_col];
                    if(new_d != d){
                        devolver[stack_index].push(Types.ACTIONS.ACTION_RIGHT);

                    }
                }else{
                    back_col += 1;
                    devolver[stack_index].push(Types.ACTIONS.ACTION_LEFT);
                    new_d = directions[back_row][back_col];
                    if(new_d != d){
                        devolver[stack_index].push(Types.ACTIONS.ACTION_LEFT);

                    }
                }
            }

            return directions[rowEnd][colEnd];
        }

        /**
         * Calulate distance between this.now and rowEnd/colEnd
         * @param drow
         * @param dcol
         * @return Manhattan Distance
         */
        private double distance(int drow, int dcol) {
            int row_dif = Math.abs(this.now.row + drow - this.rowEnd);
            int col_dif = Math.abs(this.now.col + dcol - this.colEnd);
            return row_dif*1.5 + col_dif*1.5;
        }

        /**
         * Add the possible neighbours to the queue
         */
        private void addNeigborsToOpenList() {
            Node node;
            for (int col = -1; col <= 1; col++) {
                for (int row = -1; row <= 1; row++) {
                    if (col != 0 && row != 0) {
                        continue; // skip if it is a diagonal movement
                    }

                    int dire = 3;

                    if (col == 1) dire = 1;
                    else if (row == 1) dire = 2;
                    else if (row == -1) dire = 0;

                    int ex = 0;

                    // Check if there is an extra turn needed to get to the neighbour
                    if (directions[this.now.row][this.now.col] != dire)
                        ex = 2;
                    node = new Node(this.now.row + row, this.now.col + col, this.now.g + 1, this.distance(row, col));
                    if ((col != 0 || row != 0) // not this.now
                            && this.now.col + col >= 0 && this.now.col + col < this.maze[0].length // check maze boundaries
                            && this.now.row + row >= 0 && this.now.row + row < this.maze.length
                            && this.maze[this.now.row + row][this.now.col + col] != 4 // check if square is walkable
                            && !this.open.contains(node)
                            && !this.closed.contains(node)
                    ) {

                        directions[node.row][node.col] = dire;
                        node.g += ex; // Add extra movement cost for this square

                        // Add heuristic against monsters
                        if(hay_monster){
                            for(int k = 0; k < n_monster; k++){

                                if((Math.abs(node.row - monster[k].row)<=heuristic_limit) &&(Math.abs(node.col - monster[k].col)<=heuristic_limit)) {
                                    int man = Math.abs(node.row - monster[k].row) + Math.abs(node.col - monster[k].col);    // Manhattan Distance

                                    if(man == 0)
                                        node.h += 100*4;
                                    else
                                        node.h += 100*(6.0/man);

                                    node.g +=ex;
                                }
                            }
                        }
                        // Add node to the list of open nodes
                        this.open.add(node);
                    }
                }
            }
        }
    }




    public AgentSurvive2(StateObservation stateObs, ElapsedCpuTimer elapsedTimer){
        // Set scala
        fescala = new Vector2d(stateObs.getWorldDimension().width / stateObs.getObservationGrid().length ,
                stateObs.getWorldDimension().height / stateObs.getObservationGrid()[0].length);

        // Set variables for special objects and create virtual gems
        n_gemas = 4;

        gemas = new AgentSurvive2.PairInteger[n_gemas+2];
        devolver = new Stack[n_gemas+1];
        order_search = new int [n_gemas+2];

        indexes = new int [n_gemas+2];
        for (int i = 0; i < n_gemas+1; ++i){
            devolver[i] = new Stack<Types.ACTIONS>();
            indexes[i] = i;
        }
        indexes[n_gemas+1] = n_gemas+1;

        ArrayList<Observation>[][] grid = stateObs.getObservationGrid();
        mapa = new int[grid[0].length][grid.length];

        // Position virtual gems at corners
        gemas[1] = new AgentSurvive2.PairInteger(1, 1);
        gemas[2] = new AgentSurvive2.PairInteger(1, grid.length-2);
        gemas[3] = new AgentSurvive2.PairInteger(grid[0].length-2, grid.length-2);
        gemas[4] = new AgentSurvive2.PairInteger(grid[0].length-2, 1);

        // Iterate over the map and save the values
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0 ; j < grid[0].length; j++) {
                if(!grid[i][j].isEmpty()) {
                    int categoria = grid[i][j].get(0).category;
                    mapa[j][i] = categoria;
                    if(categoria == 0){             // Avatar
                        pos_actual.row = j;
                        pos_actual.col = i;
                    }else if(categoria == 2){       // Exit
                        portal.row = j;
                        portal.col = i;
                    }
                }
                else {                              // Walkable
                    mapa[j][i] = 9;
                }
            }
        }

        gemas[0] = pos_actual;
        gemas[n_gemas+1] = portal;

        // Check for enemies and save positions
        if(stateObs.getNPCPositions() != null){
            n_monster = stateObs.getNPCPositions()[0].size();
            hay_monster = true;
            monster = new PairInteger[n_monster];
            for(int i = 0; i < n_monster; i++){
                Vector2d monst = stateObs.getNPCPositions()[0].get(i).position;
                monster[i] = new PairInteger((int) (monst.y/fescala.y), (int) (monst.x/fescala.x));
            }
        }

        // Get avatar's orientation
        int dir = (int)stateObs.getAvatarOrientation().x;
        if (dir == -1){
            dir = 3;
        }else if(dir!=1){
            if((int)stateObs.getAvatarOrientation().y == 1)
                dir = 2;
            else
                dir = 0;
        }

        // Initialize path calculator
        as = new AStar(mapa, pos_actual.row, pos_actual.col, dir);

        // Get gems search order
        orderGemas();

        // Calculate path between each gem following the order
        for(int i = 0; i < n_gemas+1; i++) {
            as.newStart(gemas[order_search[i]].row, gemas[order_search[i]].col, dir);
            dir = as.findPathTo(gemas[order_search[i+1]].row, gemas[order_search[i+1]].col, i);
        }

    }


    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        // Get actual position
        Vector2d actual = stateObs.getAvatarPosition();
        int row_actual =  (int) (actual.y/fescala.y);
        int col_actual =  (int) (actual.x/fescala.x);

        if(hay_monster) {
            iterations++;
            // Avoid circular exchanging case if monster is close to next 2 gems
            if(unlock_change == iterations){
                recalculate = false;
            }
            int limit = 4;
            for (int i = 0; i < n_monster; i++) {
                Vector2d monst = stateObs.getNPCPositions()[0].get(i).position;
                monster[i].col = (int) (monst.x / fescala.x);
                monster[i].row = (int) (monst.y / fescala.y);

                boolean rec_extra = recalculate;

                // Swap it for the next one
                if((Math.abs(gemas[order_search[devolver_index+1]].row - monster[i].row)<=limit) && (Math.abs(gemas[order_search[devolver_index+1]].col - monster[i].col)<=limit) && (devolver_index < order_search.length-3) && !recalculate && !destino_final_block) {
                    rec_extra = true;
                    int ex = order_search[devolver_index + 1];
                    order_search[devolver_index + 1] = order_search[devolver_index + 2];
                    order_search[devolver_index + 2] = ex;
                    unlock_change = iterations+4;
                }

                // Swap it for the one 2 positions ahead
                if((Math.abs(gemas[order_search[devolver_index+1]].row - monster[i].row)<=limit) && (Math.abs(gemas[order_search[devolver_index+1]].col - monster[i].col)<=limit) && (devolver_index < order_search.length-4) &&!destino_final_block) {
                    int ex = order_search[devolver_index + 1];
                    order_search[devolver_index + 1] = order_search[devolver_index + 3];
                    order_search[devolver_index + 3] = ex;
                }

                // Swap it for the one 3 positions ahead
                if((Math.abs(gemas[order_search[devolver_index+1]].row - monster[i].row)<=limit) && (Math.abs(gemas[order_search[devolver_index+1]].col - monster[i].col)<=limit) && (devolver_index < order_search.length-5) &&!destino_final_block) {
                    int ex = order_search[devolver_index + 1];
                    order_search[devolver_index + 1] = order_search[devolver_index + 4];
                    order_search[devolver_index + 4] = ex;
                }

                recalculate = rec_extra;
            }



            if( !((row_actual == gemas[order_search[devolver_index+1]].row) && (col_actual == gemas[order_search[devolver_index+1]].col))){
                // Get avatar's orientation
                int dir = (int)stateObs.getAvatarOrientation().x;
                if (dir == -1){
                    dir = 3;
                }else if(dir!=1){
                    if((int)stateObs.getAvatarOrientation().y == 1)
                        dir = 2;
                    else
                        dir = 0;
                }
                // Recalculate path to next objective taking into account new monster's positions
                devolver[devolver_index].clear();
                as.newStart(row_actual,col_actual, dir);
                dir = as.findPathTo(gemas[order_search[devolver_index+1]].row, gemas[order_search[devolver_index+1]].col, devolver_index);

                // Recalculate path to the next gem
                if(devolver_index+1 < devolver.length-2) {
                    devolver[devolver_index + 1].clear();
                    as.newStart(gemas[order_search[devolver_index + 1]].row, gemas[order_search[devolver_index + 1]].col, dir);
                    dir = as.findPathTo(gemas[order_search[devolver_index + 2]].row, gemas[order_search[devolver_index + 2]].col, devolver_index + 1);
                }
            }
        }


        // Return appropriate action
        if (!devolver[devolver_index].empty()){
            return devolver[devolver_index].pop();
        }else if(devolver_index < devolver.length-1) {
            if (!devolver[devolver_index].empty())
                return devolver[devolver_index].pop();
        }
        return Types.ACTIONS.ACTION_NIL;

    }

    public void init(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

    }

}


