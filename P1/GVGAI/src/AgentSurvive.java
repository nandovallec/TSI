package src_vallecillos_ruiz_fernando;

import java.util.*;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Pair;
import tools.Vector2d;

//0 is up
//1 is right
//2 is down
//3 is left

public class AgentSurvive extends AbstractPlayer {
    Vector2d fescala;
    AStar as;
    PairInteger portal = new PairInteger(0,0);;
    PairInteger [] monster;
    //	Node pos_actual = new Node(-1,-1,1);
    Stack<Types.ACTIONS> []devolver;
    int devolver_index = 0;
    int [][] mapa;
    PairInteger [] gemas;
    PairInteger pos_actual = new PairInteger(0,0);
    int [] order_search;
    int best_distance = 999;
    int [] indexes;
    int [][]dist;
    int n_monster = 0;
    boolean hay_monster = false;
    boolean recalculate = false;

    int n_gemas;

    public void branchBound(int [] part_sol, boolean [] chosen, int ac_dist, int level) {
        if(ac_dist> best_distance)
            return;

        if(level == part_sol.length-1){
//            System.out.println("Level "+ level);
            ac_dist += dist[part_sol[part_sol.length-2]][part_sol[part_sol.length-1]];
            if(ac_dist < best_distance){
                best_distance = ac_dist;
                order_search = part_sol.clone();
            }
            return;
        }
//        System.out.print("Actual path: ");
//        for(int i  = 0; i < part_sol.length; i++)
//            System.out.print(part_sol[i]+",  ");
//        System.out.println("");
        for(int  i = 1; i < indexes.length-1; i++){
            if (chosen[i] == false) {
                int sel_index = indexes[i];
                chosen[sel_index] = true;
                part_sol[level] = sel_index;
//                System.out.println("Adding d: "+part_sol[level-1]+ "  "+ sel_index);
                branchBound(part_sol, chosen, ac_dist + dist[part_sol[level-1]][sel_index], level + 1);
                chosen[sel_index] = false;
                part_sol[level] = -1;

            }
        }
        return;
    }

    void orderGemas(){
        dist = new int[n_gemas+2][n_gemas+2];
        ArrayList<Integer> op_path =  new ArrayList<Integer>();
        for(int i = 0; i < n_gemas+2; i++){
            dist[i][i] = 9999;
            for(int j = i+1; j< gemas.length; j++){
                int row_dif = Math.abs(gemas[i].row - gemas[j].row);
                int col_dif = Math.abs(gemas[i].col - gemas[j].col);

//                int d = (int) (row_dif*1.5 + col_dif*1.5);
                int d = (int) (row_dif + col_dif);

                dist[i][j] = d;
                dist[j][i] = d;
            }
        }
//        int result_index = 0;

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
//        System.out.println("The best path has distance: "+ best_distance);
//        System.out.print("Best path: ");
//        for(int i  = 0; i < order_search.length; i++)
//            System.out.print(order_search[i]+",  ");
//        System.out.println("");
////        index[0] = -1;
//        index[11] = -1;
//
//        int selected = 0;
//        int min_index = selected;
//
//

//        order_search[0] = 0;
//        order_search[order_search.length-1] = order_search.length-1;
//        for(int w = 1; w < index.length-1; w++) {
//            for (int i = 1; i < index.length - 1; i++) {
//                if (index[i] != -1 && dist[selected][i] < dist[selected][min_index])
//                    min_index = i;
//            }
//            index[selected] = -1;
//            order_search[w] = min_index;
//            selected = min_index;
//
//
//            System.out.println(w);
//        }
//        for( int k = 0; k < 40000; k++) {
//        op_path.clear();
//        //https://utd-ir.tdl.org/bitstream/handle/10735.1/2637/ECS-TR-EE-Vardhan-310316.85.pdf?sequence=7&isAllowed=y
////http://read.pudn.com/downloads194/ebook/911571/ebooks/Ernesto%20de%20Queiros%20Vieira%20Martins%20&%20Marta%20Margarida%20Braz%20Pascoal/martins00new.pdf
////        Yen's algorithm, implementation of k shortes path routing
//        op_path.add(0);
//        op_path.add(1);
//        op_path.add(gemas.length-1);
//        Integer []ex_ind = {2,3,4,5,6,7,8,9,10};
////        Integer []ex_ind = {7,10,2,3,5,9,4,6,8};
//
//            List<Integer> list = Arrays.asList(ex_ind);
//            Collections.shuffle(list);
//            list.toArray(ex_ind);
////        for(int i  = 0; i < 9; i++)
////            System.out.print(", "+ex_ind[i]);
////        System.out.println("");
//
//            for (int w = 0; w < gemas.length - 3; w++) {
////            int insertingIndex = w+2;
//                int insertingIndex = ex_ind[w];
////            System.out.println(ex_ind[w]);
//
//                int best_index = 1;
//                int best_dist = dist[0][insertingIndex] + dist[insertingIndex][op_path.get(1)];
////            if(insertingIndex == 6)
////                System.out.println(best_dist);
//                for (int i = 1; i < op_path.size() - 1; i++) {
//                    int d = dist[op_path.get(i)][insertingIndex] + dist[insertingIndex][op_path.get(i + 1)];
//                    if (d < best_dist) {
//                        best_dist = d;
//                        best_index = i + 1;
////                    if(insertingIndex == 6) {
////                        System.out.println("New best: "+ d+"   ind "+best_dist);
////                    }
//                    }
//                }
////            System.out.println("Añado "+insertingIndex+" en "+best_index);
//                op_path.add(best_index, insertingIndex);
//            }
//
//        }
////        La distancia añadiendo 1 a 1 es de 129
////        La mejor distancia (optima) es de 114
//            //        CAMINO OPTIMO
//            int[] ex = {0, 10, 7, 8, 9, 6, 5, 4, 1, 3, 2, 11};
////        int[] ex = {0,4,1,2,3,5,7,10,8,9,6, 11};
//
//        order_search = ex;
////        System.out.println("");
////        System.out.println(op_path.toString());
//            int op_dist = 0;
//            int no_dist = 0;
//            for (int i = 0; i < 11; i++) {
//                op_dist += dist[op_path.get(i)][op_path.get(i + 1)];
//                no_dist += dist[order_search[i]][order_search[i + 1]];
////                order_search[i] = op_path.get(i);
////            order_search[i] = ex[i];
//            }
//        System.out.println("Old Dist: "+ no_dist);
//        System.out.println("New Dist: " + op_dist);

//        for(int i = 0 ; i < order_search.length;i++){
//            System.out.println("Gema: "+ order_search[i]+ " =   "+gemas[order_search[i]].row+", "+gemas[order_search[i]].col);
//        }

//        CAMINO OPTIMO
//        int [] ex = {0,10,7,8,9,6,5,4,1,3,2,11};
//        order_search = ex;
//        System.out.println("");




//        for(int i =  0; i < dist.length; i++){
//            for(int j  = 0; j < dist[0].length; j++){
//                System.out.print(dist[i][j]+", ");
//            }
//            System.out.println("");
//        }

    }

//    void orderGemas(){
//        int [] result = new int[10];
//        int [] index = new int[12];
//        int [][] dist = new int[12][12];
//        for(int i = 0; i < 12; i++){
//            index[i] = i;
//            dist[i][i] = 9999;
//            for(int j = i+1; j< 12; j++){
//                int row_dif = Math.abs(gemas[i].row - gemas[j].row);
//                int col_dif = Math.abs(gemas[i].col - gemas[j].col);
//
//                int d = (int) (row_dif*1.5 + col_dif*1.5);
//                dist[i][j] = d;
//                dist[j][i] = d;
//            }
//        }
//        int result_index = 0;
//
//        index[0] = -1;
//        index[11] = -1;
//
//        int selected = 0;
//        int min_index = selected;
//
//        order_search[0] = 0;
//        order_search[order_search.length-1] = order_search.length-1;
//        for(int w = 1; w < index.length-1; w++) {
//            for (int i = 1; i < index.length - 1; i++) {
//                if (index[i] != -1 && dist[selected][i] < dist[selected][min_index])
//                    min_index = i;
//            }
//            index[selected] = -1;
//            order_search[w] = min_index;
//            selected = min_index;
//            System.out.println(w);
//        }
//
//        for(int i = 0 ; i < order_search.length;i++){
//            System.out.println("Gema: "+ order_search[i]+ " =   "+gemas[order_search[i]].row+", "+gemas[order_search[i]].col);
//        }
//
//        System.out.println("");
//
//
//
//
//        for(int i =  0; i < dist.length; i++){
//            for(int j  = 0; j < dist[0].length; j++){
//                System.out.print(dist[i][j]+", ");
//            }
//            System.out.println("");
//        }
//
//    }

    // Node class for convienience
    static class PairInteger {
        public int row, col;

        PairInteger(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    //    static class Node implements Comparable <Node>{
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
//        // Compare by f value (g + h)
//        @Override
//        public int compareTo(Node o) {
////           Node that = (Node) o;
//            return (int)((this.g + this.h ) - (o.g + o.h ));
//        }

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

    class AStar {
        private final PriorityQueue<Node> open;
        private final Set<Node> closed;
        //		private final List<PairInteger> path;
        private Stack<Types.ACTIONS> path;
        private final int[][] maze;
        private int[][] directions;
        private Node now;
        int rowStart;
        int colStart;
        private int rowEnd, colEnd;
        int it = 0;



        AStar(int[][] maze, int row, int col, int dir) {
            this.open = new PriorityQueue<Node>(new Comparator<Node>() {
                @Override
                public int compare(Node a, Node b) {
                    return Integer.compare((int) (a.g + a.h), (int) (b.g + b.h));
                }
            });
            this.closed = new HashSet<>();
            this.path = new Stack<Types.ACTIONS>();
            this.maze = maze;
            this.directions = new int[maze.length][maze[0].length];
            this.now = new Node(row, col, 0, 0);
            directions[row][col] = dir;
            this.rowStart = row;
            this.colStart = col;
        }

        public void newStart(int row, int col, int dir) {
            this.rowStart = row;
            this.colStart = col;
            directions[row][col] = dir;
            open.clear();
            closed.clear();
            path.clear();
            this.now = new Node(row, col, 0, 0);


        }

        /*
         ** Finds path to rowEnd/colEnd or returns null
         **
         ** @param (int) rowEnd coordinates of the target position
         ** @param (int) colEnd
         ** @return (List<Node> | null) the path
         */
        public int findPathTo(int rowEnd, int colEnd, int stack_index) {
            this.rowEnd = rowEnd;
            this.colEnd = colEnd;
            this.closed.add(this.now);
            addNeigborsToOpenList();
            while (this.now.row != this.rowEnd || this.now.col != this.colEnd) {
                if (this.open.isEmpty()) { // Nothing to examine
                    return -1;
                }
                this.now = this.open.poll(); // get first node (lowest f score)
//            this.open.remove(0); // remove it
                this.closed.add(this.now); // and add to the closed
                addNeigborsToOpenList();
            }
//			this.path.add(0, new PairInteger(this.now.row, this.now.col));
            int back_col = this.now.col;
            int back_row = this.now.row;

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
//				this.path.add(0, new PairInteger(back_row, back_col));
//            System.out.println(back_row+" , "+back_col);
            }
//            System.out.println("Total: " + it);
            return directions[rowEnd][colEnd];
        }

        /*
         ** Looks in a given List<> for a node
         **
         ** @return (bool) NeightborInListFound
         */
        private boolean findNeighborInList(List<Node> array, Node node) {
            return array.stream().anyMatch((n) -> (n.col == node.col && n.row == node.row));
        }

        /*
         ** Calulate distance between this.now and rowEnd/colEnd
         **
         ** @return (int) distance
         */
        private double distance(int drow, int dcol) {
            int row_dif = Math.abs(this.now.row + drow - this.rowEnd);
            int col_dif = Math.abs(this.now.col + dcol - this.colEnd);
//			if()
//			return 0;
            return row_dif*1.5 + col_dif*1.5; // else return "Manhattan distance"
        }

        private void addNeigborsToOpenList() {
            Node node;
            for (int col = -1; col <= 1; col++) {
                for (int row = -1; row <= 1; row++) {
                    if (col != 0 && row != 0) {
                        continue; // skip if diagonal movement is not allowed
                    }
                    int dire = 3;
                    if (col == 1) dire = 1;
                    else if (row == 1) dire = 2;
                    else if (row == -1) dire = 0;
                    int ex = 0;

                    if (directions[this.now.row][this.now.col] != dire)
                        ex = 2;
                    node = new Node(this.now.row + row, this.now.col + col, this.now.g + 1, this.distance(row, col));
                    if ((col != 0 || row != 0) // not this.now
                            && this.now.col + col >= 0 && this.now.col + col < this.maze[0].length // check maze boundaries
                            && this.now.row + row >= 0 && this.now.row + row < this.maze.length
                            && this.maze[this.now.row + row][this.now.col + col] != 4 // check if square is walkable
                            && !this.open.contains(node)
                            && !this.closed.contains(node)
                    ) { // if not already done
//                        System.out.println("First: "+ (node.g+1));
//                        node.g = node.parent.g + 1.; // Horizontal/vertical cost = 1.0
//                    System.out.println("Second: "+node.g);
//                    System.out.println("Row: "+maze.length + "Col: "+maze[0].length);
//                    System.out.println("Row: "+(this.now.col + col)+"  =   "+node.col);
                        it++;
                        directions[node.row][node.col] = dire;
//						System.out.println("Origin: " + this.now.row + ", " + this.now.col + "   ||   " + directions[this.now.row][this.now.col] + "      || " + (this.now.g + this.now.h));

//						node.g += ex * (node.h); // add movement cost for this square
                        node.g += ex; // add movement cost for this square

                        if(hay_monster){
                            for(int k = 0; k < n_monster; k++){
                                int man = Math.abs(node.row - monster[k].row) + Math.abs(node.col - monster[k].col); // return "Manhattan distance"
                                if((Math.abs(node.row - monster[k].row)<=5) &&(Math.abs(node.col - monster[k].col)<=4)) {
                                    if(man == 0)
                                        node.h += 100*6;
                                    else
                                        node.h += 100*(6/man);
//                                    node.g +=ex;
                                }
                                node.h += (3.0/(float)man)*5;
//                                System.out.println("Calc: " + (this.now.row + row) + ", " + (this.now.col + col) + "   ||   " + dire + "      || " + (node.g + node.h)+"   ||    "+man);

                            }
                        }
//						System.out.println("Calc: " + (this.now.row + row) + ", " + (this.now.col + col) + "   ||   " + dire + "      || " + (node.g + node.h));

                        this.open.add(node);
                    }
                }
            }
        }
    }




    public AgentSurvive(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) throws InterruptedException {
        long start = System.nanoTime();
        fescala = new Vector2d(stateObs.getWorldDimension().width / stateObs.getObservationGrid().length ,
                stateObs.getWorldDimension().height / stateObs.getObservationGrid()[0].length);
//        System.out.println("First: " + elapsedTimer.elapsedMillis());

        n_gemas = 4;

        gemas = new PairInteger[n_gemas+2];
        devolver = new Stack[n_gemas+1];
        order_search = new int [n_gemas+2];

        indexes = new int [n_gemas+2];
        for (int i = 0; i < n_gemas+1; ++i){
            devolver[i] = new Stack<Types.ACTIONS>();
            indexes[i] = i;
        }
        indexes[n_gemas+1] = n_gemas+1;
        //Se crea una lista de observaciones de portales, ordenada por cercania al avatar
//        ArrayList<Observation>[] posiciones = stateObs.getPortalsPositions(stateObs.getAvatarPosition());
//        //Seleccionamos el portal mas proximo
//        ArrayList<Observation>[] gemas = stateObs.getResourcesPositions(stateObs.getAvatarPosition());

        ArrayList<Observation>[][] grid = stateObs.getObservationGrid();
//        facing = stateObs.getAvatarOrientation();
        mapa = new int[grid[0].length][grid.length];
//        System.out.println(mapa.length+"   "+mapa[0].length);
        gemas[0] = pos_actual;

        int row_tercio = (grid[0].length-2)/3;
        int col_cuarto = (grid.length-2)/4;
//        System.out.println("Nuevo  "+grid.length+"   "+ grid[0].length);
//        gemas[1] = new PairInteger(row_tercio+1, col_cuarto+1);
//        gemas[2] = new PairInteger(row_tercio+1, col_cuarto*3 +1);
//        gemas[3] = new PairInteger(row_tercio*2+1, col_cuarto*3+1);
//        gemas[4] = new PairInteger(row_tercio*2+1, col_cuarto+1);

        gemas[1] = new PairInteger(2, 2);
        gemas[2] = new PairInteger(2, grid.length-3);
        gemas[3] = new PairInteger(grid[0].length-3, grid.length-3);
        gemas[4] = new PairInteger(grid[0].length-3, 2);






        gemas[n_gemas+1] = portal;
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0 ; j < grid[0].length; j++) {
                if(!grid[i][j].isEmpty()) {
                    int categoria = grid[i][j].get(0).category;
                    mapa[j][i] = categoria;
                    if(categoria == 0){
                        pos_actual.row = j;
                        pos_actual.col = i;
                    }else if(categoria == 2){
                        portal.row = j;
                        portal.col = i;
                    }
                }
                else {
                    mapa[j][i] = 9;
                }
            }
        }







        if(stateObs.getNPCPositions() != null){
            n_monster = stateObs.getNPCPositions()[0].size();
            hay_monster = true;
            monster = new PairInteger[n_monster];
            for(int i = 0; i < n_monster; i++){
                Vector2d monst = stateObs.getNPCPositions()[0].get(i).position;
                monster[i] = new PairInteger((int) (monst.y/fescala.y), (int) (monst.x/fescala.x));
//                System.out.println("Pos "+monster[i].row+"  "+monster[i].col);

//                for(int srow = -2; srow < 3; srow++){
//                    for(int scol = -2; scol < 3; scol++){
//                        if(Math.abs(srow)+Math.abs(scol) == 2) {
//                            int nrow = monster[i].row + srow;
//                            int ncol = monster[i].col + scol;
//                            if(nrow>=0 && ncol>=0 && nrow < mapa.length && ncol < mapa[0].length){
//                                mapa[nrow][ncol] = 4;
//                            }
//                        }
//                    }
//                }
            }
        }

//        for (int row = 0; row < mapa.length; row++) {
//            for (int col = 0; col < mapa[0].length; col++) {
//                System.out.print(mapa[row][col] + ", ");
//            }
//            System.out.println();
//        }

        int dir = (int)stateObs.getAvatarOrientation().x;
        if (dir == -1){
            dir = 3;
        }else if(dir!=1){
            if((int)stateObs.getAvatarOrientation().y == 1)
                dir = 2;
            else
                dir = 3;
        }
//        System.out.println(portal.row +"  "+portal.col);
        as = new AStar(mapa, pos_actual.row, pos_actual.col, dir);
//        System.out.println("Ord1: "+ elapsedTimer.elapsedMillis());
//        for(int i = 0; i < 30; i++)
        orderGemas();
//        Thread.sleep(10000);
//        System.out.println("Ord2: "+        elapsedTimer.remainingTimeMillis());

        Pair<Stack<Types.ACTIONS>, Integer> res;
        for(int i = 0; i < n_gemas+1; i++) {
            as.newStart(gemas[order_search[i]].row, gemas[order_search[i]].col, dir);
            dir = as.findPathTo(gemas[order_search[i+1]].row, gemas[order_search[i+1]].col, i);
        }

//        System.out.println("First: "+ elapsedTimer.elapsedMillis());
//        System.out.println("Ord3: "+        elapsedTimer.remainingTimeMillis());
        long finish = System.nanoTime();
        double timeElapsed = (double)(finish - start)/(double)1000000;
//        System.out.println("Total time: "+timeElapsed);

//        if(hay_monster){
//            for(int i = 0; i < n_monster; i++){
//                for(int srow = -2; srow < 3; srow++){
//                    for(int scol = -2; scol < 3; scol++){
//                        if(Math.abs(srow)+Math.abs(scol) == 2) {
//                            int nrow = monster[i].row + srow;
//                            int ncol = monster[i].col + scol;
//                            if(nrow>=0 && ncol>=0 && nrow < mapa.length && ncol < mapa[0].length){
//                                mapa[nrow][ncol] = 9;
//                            }
//                        }
//                    }
//                }
//            }
//        }
        recalculate = true;
    }

    public void init(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

    }

    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
//            int limit = 4;
            for (int i = 0; i < n_monster; i++) {
//
                Vector2d monst = stateObs.getNPCPositions()[0].get(i).position;
                monster[i].col = (int) (monst.x / fescala.x);
                monster[i].row = (int) (monst.y / fescala.y);
            }
////                System.out.println("Dif Row " + (Math.abs(gemas[order_search[devolver_index+1]].row - monster[i].row)) + "  Col " +  Math.abs(gemas[order_search[devolver_index+1]].col - monster[i].col));
//
//                if((Math.abs(gemas[order_search[devolver_index+1]].row - monster[i].row)<=limit) && (Math.abs(gemas[order_search[devolver_index+1]].col - monster[i].col)<=limit) && (devolver_index < order_search.length-3)) {
////                    System.out.println("CAMBIOOOOO");
//                    recalculate = true;
//                    int ex = order_search[devolver_index + 1];
//                    order_search[devolver_index + 1] = order_search[devolver_index + 2];
//                    order_search[devolver_index + 2] = ex;
//
////                    System.out.print("Cambio1 "+ ex+" y "+order_search[devolver_index + 1]+"   queda:  ");
////                    for(int x = devolver_index; x < order_search.length; x++)
////                        System.out.print(order_search[x]+" ,");
////                    System.out.println("");
//                }
//                if((Math.abs(gemas[order_search[devolver_index+1]].row - monster[i].row)<=limit) && (Math.abs(gemas[order_search[devolver_index+1]].col - monster[i].col)<=limit)  && devolver_index < order_search.length-4) {
////                    System.out.println("CAMBIOOOOO");
//                    recalculate = true;
//                    int ex = order_search[devolver_index + 1];
//                    order_search[devolver_index + 1] = order_search[devolver_index + 3];
//                    order_search[devolver_index + 3] = ex;
////                    System.out.print("Cambio2 "+ ex+" y "+order_search[devolver_index + 1]+"   queda:  ");
////                    for(int x = devolver_index; x < order_search.length; x++)
////                        System.out.print(order_search[x]+" ,");
////                    System.out.println("");
//                }
//                if((Math.abs(gemas[order_search[devolver_index+1]].row - monster[i].row)<=limit) && (Math.abs(gemas[order_search[devolver_index+1]].col - monster[i].col)<=limit)  && devolver_index < order_search.length-5) {
////                    System.out.println("CAMBIOOOOO");
//                    recalculate = true;
//                    int ex = order_search[devolver_index + 1];
//                    order_search[devolver_index + 1] = order_search[devolver_index + 4];
//                    order_search[devolver_index + 4] = ex;
////                    System.out.print("Cambio2 "+ ex+" y "+order_search[devolver_index + 1]+"   queda:  ");
////                    for(int x = devolver_index; x < order_search.length; x++)
////                        System.out.print(order_search[x]+" ,");
////                    System.out.println("");
//                }
//            }
////        int dist_gemas_mosnt [] = new int[n_gemas];
////        for (int i = 0; i < n_monster; i++) {
////            for(int k = 0; k < n_gemas; k++){
////                int man = Math.abs(gemas[k+1].row - monster[i].row) + Math.abs(gemas[k+1].col - monster[i].col);
////                dist_gemas_mosnt[k] += man;
////            }
////        }
////        int min_ind = 0;
////        for(int k = 0; k < n_gemas; k++) {
////            System.out.println("Dist" + dist_gemas_mosnt[k]);
////            if(dist_gemas_mosnt[k]>dist_gemas_mosnt[min_ind])
////                min_ind = k;
////        }
////        order_search[devolver_index + 1] = min_ind;
////
////
//
//            //Posicion del avatar
////        for(int i = 0; i < stateObs.getObservationGrid().length; i++)
////        	for(int j = 0 ; j < stateObs.getObservationGrid()[0].length; j++) {
////        		if(!stateObs.getObservationGrid()[i][j].isEmpty()) {
////	        		System.out.println("In "+i +"||"+j);
////	        		System.out.println(stateObs.getObservationGrid()[i][j]);
////        		}
////        	}
////    	System.out.println(mapa.get(0).size());
////		System.out.println(stateObs.getAvatarOrientation().x);
//
//
////		System.out.println(stateObs.getAvatarOrientation());
////
////		System.out.println("-------------------------------------------------");
////
////		if (stateObs.getResourcesPositions(stateObs.getAvatarPosition()) != null)
////			portal = (stateObs.getResourcesPositions(stateObs.getAvatarPosition()))[0].get(0).position;
////		else
////			portal = (stateObs.getPortalsPositions(stateObs.getAvatarPosition()))[0].get(0).position;
////
////		portal.x = Math.floor(portal.x / fescala.x);
////		portal.y = Math.floor(portal.y / fescala.y);
////		Vector2d avatar = new Vector2d(stateObs.getAvatarPosition().x / fescala.x,
////				stateObs.getAvatarPosition().y / fescala.y);
//
//
//
//
            Vector2d actual = stateObs.getAvatarPosition();
            int row_actual =  (int) (actual.y/fescala.y);
            int col_actual =  (int) (actual.x/fescala.x);



            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ArrayList<PairInteger> posibles = new ArrayList<PairInteger>();

        for(int row = -1; row <=1; row++){
            for(int col = -1; col <= 1; col++){
                if(Math.abs(col) != Math.abs(row)){
                    if(col_actual + col >= 0 && col_actual + col < mapa[0].length // check maze boundaries
                            && row_actual + row >= 0 && row_actual + row < mapa.length
                            && mapa[row_actual + row][col_actual + col] != 4){
                        posibles.add(new PairInteger(row_actual+row, col_actual+col));
//                        System.out.println("Posible: "+(row_actual+row)+"  ,  "+(col_actual+col));
                    }
                }
            }
        }
        posibles.add(new PairInteger(row_actual, col_actual));
//        System.out.println("");

        double best_d = 0;
        PairInteger best_p = null;

        for(int i = 0; i < posibles.size(); i++){
            PairInteger new_pos = posibles.get(i);
            double dist = Math.abs(new_pos.row - monster[0].row) + Math.abs(new_pos.col - monster[0].col);

            for(int k = 1; k < n_monster; k++){
                dist *= ((double)(Math.abs(new_pos.row - monster[k].row) + (double)Math.abs(new_pos.col - monster[k].col)));
            }

//            dist = (double)Math.pow((double)dist, 1.0/(double)n_monster);

            if(best_d < dist){
                best_d = dist;
                best_p = new_pos;
            }
//            System.out.println("Action "+new_pos.row+", "+new_pos.col+" has dist: "+dist);
        }

        if(best_p.row == row_actual && best_p.col == col_actual){
            devolver[devolver_index].push(Types.ACTIONS.ACTION_NIL);
        }else if(best_p.row> row_actual){
            devolver[devolver_index].push(Types.ACTIONS.ACTION_DOWN);
        }else if(best_p.row< row_actual){
            devolver[devolver_index].push(Types.ACTIONS.ACTION_UP);
        }else if(best_p.col> col_actual){
            devolver[devolver_index].push(Types.ACTIONS.ACTION_RIGHT);
        }else {
            devolver[devolver_index].push(Types.ACTIONS.ACTION_LEFT);
        }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (!devolver[devolver_index].empty()){
            return devolver[devolver_index].pop();
        }else if(devolver_index < devolver.length-1) {
//            devolver_index++;

//            int ex = order_search[devolver_index + 1];
//            order_search[devolver_index + 1] = order_search[devolver_index + 2];
//            order_search[devolver_index + 2] = order_search[devolver_index + 3];
//            order_search[devolver_index + 3] = order_search[devolver_index + 4];
//            order_search[devolver_index + 4] = ex;

            recalculate = true;
            if (!devolver[devolver_index+1].empty())
                return devolver[devolver_index+1].pop();
        }
        return Types.ACTIONS.ACTION_NIL;

    }
}
