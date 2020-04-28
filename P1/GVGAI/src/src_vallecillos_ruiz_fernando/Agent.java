package src_vallecillos_ruiz_fernando;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

//0 is up
//1 is right
//2 is down
//3 is left

public class Agent extends AbstractPlayer {
    AbstractPlayer jugador;
    public Agent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        boolean hay_gemas = stateObs.getResourcesPositions() != null;
        boolean hay_monster = stateObs.getNPCPositions() != null;

        if (!hay_gemas && hay_monster){
            jugador = new AgentSurvive2(stateObs, elapsedTimer);
        }else{
            jugador = new Agent2(stateObs, elapsedTimer);
        }
//        System.out.println("Remaining time constructor: "+ elapsedTimer.remainingTimeMillis());

    }

    public void init(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

    }

    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        Types.ACTIONS s = jugador.act(stateObs, elapsedTimer);
//        System.out.println("Remaining time act: "+ elapsedTimer.remainingTimeMillis());
       return s;
    }
}
