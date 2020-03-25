package tsi;

import java.util.ArrayList;
import java.util.Collections;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.Vector2d;

public class Agent extends AbstractPlayer {
	Vector2d fescala;
	Vector2d portal;
	Vector2d gema;
	ArrayList<ArrayList<Integer>> mapa = new ArrayList<ArrayList<Integer>>();

	
    public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
		//Calculamos el factor de escala entre mundos (pixeles -> grid)
        fescala = new Vector2d(stateObs.getWorldDimension().width / stateObs.getObservationGrid().length , 
        		stateObs.getWorldDimension().height / stateObs.getObservationGrid()[0].length);      
      
        //Se crea una lista de observaciones de portales, ordenada por cercania al avatar
        ArrayList<Observation>[] posiciones = stateObs.getPortalsPositions(stateObs.getAvatarPosition());
//        //Seleccionamos el portal mas proximo
        ArrayList<Observation>[] gemas = stateObs.getResourcesPositions(stateObs.getAvatarPosition());
        
        ArrayList<Observation>[][] grid = stateObs.getObservationGrid();
        for(int i = 0; i < stateObs.getObservationGrid().length; i++) {
        	mapa.add(new ArrayList<Integer>());
        	for(int j = 0 ; j < stateObs.getObservationGrid()[0].length; j++) {
        		if(!stateObs.getObservationGrid()[i][j].isEmpty()) {
//	        		System.out.println("In "+i +"||"+j);
//	        		System.out.println(grid[i][j].get(0).category);
	        		mapa.get(i).add(grid[i][j].get(0).category);
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
        		return Types.ACTIONS.ACTION_UP;
        	case 1:  
        		return Types.ACTIONS.ACTION_DOWN;
        	case 2:  
        		return Types.ACTIONS.ACTION_LEFT;
        	case 3:  
        		return Types.ACTIONS.ACTION_RIGHT;
        	default:
        		return Types.ACTIONS.ACTION_NIL;
        }              
    }

}
