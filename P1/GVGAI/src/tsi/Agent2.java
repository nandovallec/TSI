package tsi;

import java.util.*;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Pair;
import tools.Vector2d;

import javax.swing.*;

//0 is up
//1 is right
//2 is down
//3 is left

public class Agent2 extends AbstractPlayer {
    Vector2d fescala;
    PairInteger portal = new PairInteger(0,0);;
    //	Node pos_actual = new Node(-1,-1,1);
    Stack<Types.ACTIONS> []devolver = new Stack[12];
    int devolver_index = 0;
    int [][] mapa;
    PairInteger [] gemas;
    PairInteger pos_actual = new PairInteger(0,0);
    int [] order_search = new int[12];

    void orderGemas(){
        int [] index = new int[12];
        int [][] dist = new int[12][12];
        ArrayList<Integer> op_path =  new ArrayList<Integer>();
        for(int i = 0; i < 12; i++){
            index[i] = i;
            dist[i][i] = 9999;
            for(int j = i+1; j< 12; j++){
                int row_dif = Math.abs(gemas[i].row - gemas[j].row);
                int col_dif = Math.abs(gemas[i].col - gemas[j].col);

//                int d = (int) (row_dif*1.5 + col_dif*1.5);
                int d = (int) (row_dif + col_dif);

                dist[i][j] = d;
                dist[j][i] = d;
            }
        }
        int result_index = 0;

        index[0] = -1;
        index[11] = -1;

        int selected = 0;
        int min_index = selected;

        order_search[0] = 0;
        order_search[order_search.length-1] = order_search.length-1;
        for(int w = 1; w < index.length-1; w++) {
            for (int i = 1; i < index.length - 1; i++) {
                if (index[i] != -1 && dist[selected][i] < dist[selected][min_index])
                    min_index = i;
            }
            index[selected] = -1;
            order_search[w] = min_index;
            selected = min_index;
//            System.out.println(w);
        }
        for( int k = 0; k < 40000; k++) {
        op_path.clear();
        //https://utd-ir.tdl.org/bitstream/handle/10735.1/2637/ECS-TR-EE-Vardhan-310316.85.pdf?sequence=7&isAllowed=y
//http://read.pudn.com/downloads194/ebook/911571/ebooks/Ernesto%20de%20Queiros%20Vieira%20Martins%20&%20Marta%20Margarida%20Braz%20Pascoal/martins00new.pdf
//        Yen's algorithm, implementation of k shortes path routing
        op_path.add(0);
        op_path.add(1);
        op_path.add(gemas.length-1);
        Integer []ex_ind = {2,3,4,5,6,7,8,9,10};
//        Integer []ex_ind = {7,10,2,3,5,9,4,6,8};

            List<Integer> list = Arrays.asList(ex_ind);
            Collections.shuffle(list);
            list.toArray(ex_ind);
//        for(int i  = 0; i < 9; i++)
//            System.out.print(", "+ex_ind[i]);
//        System.out.println("");

            for (int w = 0; w < gemas.length - 3; w++) {
//            int insertingIndex = w+2;
                int insertingIndex = ex_ind[w];
//            System.out.println(ex_ind[w]);

                int best_index = 1;
                int best_dist = dist[0][insertingIndex] + dist[insertingIndex][op_path.get(1)];
//            if(insertingIndex == 6)
//                System.out.println(best_dist);
                for (int i = 1; i < op_path.size() - 1; i++) {
                    int d = dist[op_path.get(i)][insertingIndex] + dist[insertingIndex][op_path.get(i + 1)];
                    if (d < best_dist) {
                        best_dist = d;
                        best_index = i + 1;
//                    if(insertingIndex == 6) {
//                        System.out.println("New best: "+ d+"   ind "+best_dist);
//                    }
                    }
                }
//            System.out.println("Añado "+insertingIndex+" en "+best_index);
                op_path.add(best_index, insertingIndex);
            }

        }
//        La distancia añadiendo 1 a 1 es de 129
//        La mejor distancia (optima) es de 114
            //        CAMINO OPTIMO
            int[] ex = {0, 10, 7, 8, 9, 6, 5, 4, 1, 3, 2, 11};
            order_search = ex;
//        System.out.println("");
//        System.out.println(op_path.toString());
            int op_dist = 0;
            int no_dist = 0;
            for (int i = 0; i < 11; i++) {
                op_dist += dist[op_path.get(i)][op_path.get(i + 1)];
                no_dist += dist[order_search[i]][order_search[i + 1]];
                order_search[i] = op_path.get(i);
//            order_search[i] = ex[i];
            }
//        System.out.println("Old Dist: "+ no_dist);
            System.out.println("New Dist: " + op_dist);

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
                        ex = 1;
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

//						System.out.println("Calc: " + (this.now.row + row) + ", " + (this.now.col + col) + "   ||   " + dire + "      || " + (node.g + node.h));

                        this.open.add(node);
                    }
                }
            }
        }
    }




    public Agent2(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) throws InterruptedException {
        long start = System.nanoTime();

        System.out.println("First: "+ elapsedTimer.elapsedMillis());
        gemas = new PairInteger[12];
        //Calculamos el factor de escala entre mundos (pixeles -> grid)
        fescala = new Vector2d(stateObs.getWorldDimension().width / stateObs.getObservationGrid().length ,
                stateObs.getWorldDimension().height / stateObs.getObservationGrid()[0].length);

        for (int i=0; i<11; ++i)
            devolver[i] = new Stack<Types.ACTIONS>();
        //Se crea una lista de observaciones de portales, ordenada por cercania al avatar
//        ArrayList<Observation>[] posiciones = stateObs.getPortalsPositions(stateObs.getAvatarPosition());
//        //Seleccionamos el portal mas proximo
//        ArrayList<Observation>[] gemas = stateObs.getResourcesPositions(stateObs.getAvatarPosition());

        ArrayList<Observation>[][] grid = stateObs.getObservationGrid();
//        facing = stateObs.getAvatarOrientation();
        mapa = new int[grid[0].length][grid.length];
        System.out.println(mapa.length+"   "+mapa[0].length);
        int n_gemas = 1;
        gemas[0] = pos_actual;
        gemas[11] = portal;
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
                    }else if(categoria == 1){
//                        System.out.println(n_gemas);
                        gemas[n_gemas] = new PairInteger(j, i);
                        n_gemas++;
                    }
                }
                else {
                    mapa[j][i] = 9;
                }
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
        System.out.println(portal.row +"  "+portal.col);
        AStar as = new AStar(mapa, pos_actual.row, pos_actual.col, dir);
        System.out.println("Ord1: "+ elapsedTimer.elapsedMillis());
//        for(int i = 0; i < 30; i++)
            orderGemas();
//        Thread.sleep(10000);
        System.out.println("Ord2: "+        elapsedTimer.remainingTimeMillis());

        Pair<Stack<Types.ACTIONS>, Integer> res;
        for(int i = 0; i < 11; i++) {
            as.newStart(gemas[order_search[i]].row, gemas[order_search[i]].col, dir);
            dir = as.findPathTo(gemas[order_search[i+1]].row, gemas[order_search[i+1]].col, i);
        }

        System.out.println("First: "+ elapsedTimer.elapsedMillis());
        System.out.println("Ord3: "+        elapsedTimer.remainingTimeMillis());
        long finish = System.nanoTime();
        double timeElapsed = (double)(finish - start)/(double)1000000;
        System.out.println("Total time: "+timeElapsed);
    }

    public void init(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

    }

    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        //Posicion del avatar
//        for(int i = 0; i < stateObs.getObservationGrid().length; i++)
//        	for(int j = 0 ; j < stateObs.getObservationGrid()[0].length; j++) {
//        		if(!stateObs.getObservationGrid()[i][j].isEmpty()) {
//	        		System.out.println("In "+i +"||"+j);
//	        		System.out.println(stateObs.getObservationGrid()[i][j]);
//        		}
//        	}
//    	System.out.println(mapa.get(0).size());
//		System.out.println(stateObs.getAvatarOrientation().x);


//		System.out.println(stateObs.getAvatarOrientation());
//
//		System.out.println("-------------------------------------------------");
//
//		if (stateObs.getResourcesPositions(stateObs.getAvatarPosition()) != null)
//			portal = (stateObs.getResourcesPositions(stateObs.getAvatarPosition()))[0].get(0).position;
//		else
//			portal = (stateObs.getPortalsPositions(stateObs.getAvatarPosition()))[0].get(0).position;
//
//		portal.x = Math.floor(portal.x / fescala.x);
//		portal.y = Math.floor(portal.y / fescala.y);
//		Vector2d avatar = new Vector2d(stateObs.getAvatarPosition().x / fescala.x,
//				stateObs.getAvatarPosition().y / fescala.y);

        if (!devolver[devolver_index].empty()){
            return devolver[devolver_index].pop();
        }else if(devolver_index < devolver.length-1) {
            devolver_index++;
            if (!devolver[devolver_index].empty())
                return devolver[devolver_index].pop();
        }
        return Types.ACTIONS.ACTION_NIL;

    }
}
