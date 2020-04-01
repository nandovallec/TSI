package tsi;

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

public class Agent extends AbstractPlayer {
    AbstractPlayer jugador;
    public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        boolean hay_gemas = stateObs.getResourcesPositions() != null;
        boolean hay_monster = stateObs.getNPCPositions() != null;
//        System.out.println(hay_gemas);
        if (!hay_gemas && hay_monster){
            jugador = new AgentSurvive2(stateObs, elapsedTimer);
        }else{
            jugador = new Agent2(stateObs, elapsedTimer);
        }
    }

    public void init(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

    }

    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
       return jugador.act(stateObs, elapsedTimer);
    }
}
