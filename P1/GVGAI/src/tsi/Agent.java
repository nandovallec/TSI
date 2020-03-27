package tsi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

public class Agent extends AbstractPlayer {
	Vector2d fescala;
	Vector2d portal;
//	Node pos_actual = new Node(-1,-1,1);
	ArrayList<ArrayList<Integer>> mapa = new ArrayList<ArrayList<Integer>>();


	static class AStar {
		private final List<Node> open;
		private final List<Node> closed;
		private final List<Node> path;
		private final int[][] maze;
		private Node now;
		private final int xstart;
		private final int ystart;
		private int xend, yend;

		AStar(int[][] maze, int xstart, int ystart, int dirstart) {
			this.open = new ArrayList<>();
			this.closed = new ArrayList<>();
			this.path = new ArrayList<>();
			this.maze = maze;
			this.now = new Node(xstart, ystart, dirstart,0, 0,0);
			this.xstart = xstart;
			this.ystart = ystart;
		}

		static class Node implements Comparable<Node> {
			// Ideally, name the class after whatever you're actually using
			// the int pairs *for.*
			public int x, y, dir, extra;
			public double g, h;

			Node(Node parent, int xpos, int ypos, double g, double h, int dir) {
				this.parent = parent;
				this.x = xpos;
				this.y = ypos;
				this.g = g;
				this.h = h;
				this.dir = dir;
			}

			// Compare by f value (g + h)
			@Override
			public int compareTo(Node o) {
//			Node that = (Node) o;
				return (int) ((this.g + this.h + this.extra) - (o.g + o.h + o.extra));
			}
		}

		private static boolean findNeighborInList(List<Node> array, Node node) {
			return array.stream().anyMatch((n) -> (n.x == node.x && n.y == node.y));
		}

		private double distance(int dx, int dy) {
			return Math.abs(this.now.x + dx - this.xend) + Math.abs(this.now.y + dy - this.yend); // "Manhattan distance"
		}

		private void addNeigborsToOpenList() {
			Node node;
			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if (x != 0 && y != 0) {
						continue; // skip if diagonal movement is not allowed
					}
					node = new Node(this.now, this.now.x + x, this.now.y + y, this.now.g, this.distance(x, y));
					if ((x != 0 || y != 0) // not this.now
							&& this.now.x + x >= 0 && this.now.x + x < this.maze[0].length // check maze boundaries
							&& this.now.y + y >= 0 && this.now.y + y < this.maze.length
							&& this.maze[this.now.y + y][this.now.x + x] != -1 // check if square is walkable
							&& !findNeighborInList(this.open, node) && !findNeighborInList(this.closed, node)) { // if not already done
						node.g += 1.; // Horizontal/vertical cost = 1.0
						node.g += maze[this.now.y + y][this.now.x + x]; // add movement cost for this square


						this.open.add(node);
					}
				}
			}
			Collections.sort(this.open);
		}










	}





    public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		//Calculamos el factor de escala entre mundos (pixeles -> grid)
        fescala = new Vector2d(stateObs.getWorldDimension().width / stateObs.getObservationGrid().length , 
        		stateObs.getWorldDimension().height / stateObs.getObservationGrid()[0].length);      
      
        //Se crea una lista de observaciones de portales, ordenada por cercania al avatar
//        ArrayList<Observation>[] posiciones = stateObs.getPortalsPositions(stateObs.getAvatarPosition());
//        //Seleccionamos el portal mas proximo
//        ArrayList<Observation>[] gemas = stateObs.getResourcesPositions(stateObs.getAvatarPosition());
        
        ArrayList<Observation>[][] grid = stateObs.getObservationGrid();
//        facing = stateObs.getAvatarOrientation();

        for(int i = 0; i < stateObs.getObservationGrid().length; i++) {
        	mapa.add(new ArrayList<Integer>(stateObs.getObservationGrid()[0].length));
        	for(int j = 0 ; j < stateObs.getObservationGrid()[0].length; j++) {
        		if(!stateObs.getObservationGrid()[i][j].isEmpty()) {
        		    int categoria = grid[i][j].get(0).category;
	        		mapa.get(i).add(categoria);
	        		if(categoria == 0){
	        		    pos_actual.x = i;
	        		    pos_actual.y = j;
                    }
        		}
        		else {
	        		mapa.get(i).add(9);
        		}
        	}
        }
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
		System.out.println(stateObs.getAvatarOrientation());
        for(int j = 0; j < stateObs.getObservationGrid()[0].length; j++) {
        	for(int i = 0; i < stateObs.getObservationGrid().length; i++) {
        		System.out.print(mapa.get(i).get(j)+", ");
            }
            System.out.println();
        }

		System.out.println("-------------------------------------------------");

        if(stateObs.getResourcesPositions(stateObs.getAvatarPosition()) != null)
        	portal = (stateObs.getResourcesPositions(stateObs.getAvatarPosition()))[0].get(0).position;
    	else
        	portal = (stateObs.getPortalsPositions(stateObs.getAvatarPosition()))[0].get(0).position;

        portal.x = Math.floor(portal.x / fescala.x);
        portal.y = Math.floor(portal.y / fescala.y);
        Vector2d avatar =  new Vector2d(stateObs.getAvatarPosition().x / fescala.x, 
        		stateObs.getAvatarPosition().y / fescala.y);
        
        //Probamos las cuatro acciones y calculamos la distancia del nuevo estado al portal.
        Vector2d newPos_up = avatar, newPos_down = avatar, newPos_left = avatar, newPos_right = avatar;
        if (avatar.y - 1 >= 0) {
        	newPos_up = new Vector2d(avatar.x, avatar.y-1);
        }
        if (avatar.y + 1 <= stateObs.getObservationGrid()[0].length-1) {
        	newPos_down = new Vector2d(avatar.x, avatar.y+1);
        }
        if (avatar.x - 1 >= 0) {
        	newPos_left = new Vector2d(avatar.x - 1, avatar.y);
        }
        if (avatar.x + 1 <= stateObs.getObservationGrid().length - 1) {
        	newPos_right = new Vector2d(avatar.x + 1, avatar.y);
        }
        
        //Manhattan distance
        ArrayList<Integer> distances = new ArrayList<Integer>();
        distances.add((int) (Math.abs(newPos_up.x - portal.x) + Math.abs(newPos_up.y-portal.y)));
        distances.add((int) (Math.abs(newPos_down.x - portal.x) + Math.abs(newPos_down.y-portal.y)));
        distances.add((int) (Math.abs(newPos_left.x - portal.x) + Math.abs(newPos_left.y-portal.y)));
        distances.add((int) (Math.abs(newPos_right.x - portal.x) + Math.abs(newPos_right.y-portal.y)));      
       
        // Nos quedamos con el menor y tomamos esa accion. 
        int minIndex = distances.indexOf(Collections.min(distances));
        switch (minIndex) {
        	case 0:
				if (pos_actual.dir == 0) {
					mapa.get(pos_actual.x).set(pos_actual.y, 9);

					pos_actual.y-=1;
					mapa.get(pos_actual.x).set(pos_actual.y, 0);

				}
				pos_actual.dir = 0;
				return Types.ACTIONS.ACTION_UP;
        	case 1:
				if (pos_actual.dir == 2) {
					mapa.get(pos_actual.x).set(pos_actual.y, 9);

					pos_actual.y += 1;
					mapa.get(pos_actual.x).set(pos_actual.y, 0);
				}
				pos_actual.dir = 2;

				return Types.ACTIONS.ACTION_DOWN;
        	case 2:
				if (pos_actual.dir == 3) {
					mapa.get(pos_actual.x).set(pos_actual.y, 9);

					pos_actual.x -= 1;
					mapa.get(pos_actual.x).set(pos_actual.y, 0);
				}
				pos_actual.dir = 3;

				return Types.ACTIONS.ACTION_LEFT;
        	case 3:
				if (pos_actual.dir == 1) {
					mapa.get(pos_actual.x).set(pos_actual.y, 9);

					pos_actual.x += 1;
					mapa.get(pos_actual.x).set(pos_actual.y, 0);
				}
				pos_actual.dir = 1;

				return Types.ACTIONS.ACTION_RIGHT;
        	default:
        		return Types.ACTIONS.ACTION_NIL;
        }              
    }

}
