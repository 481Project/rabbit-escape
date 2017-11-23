package rabbitescape.engine.behaviours;

import static rabbitescape.engine.Token.Type.*;

import static rabbitescape.engine.ChangeDescription.State.*;




import rabbitescape.engine.Behaviour;
import rabbitescape.engine.BehaviourTools;
import rabbitescape.engine.ChangeDescription.State;
import rabbitescape.engine.Rabbit;
import rabbitescape.engine.World;

public class Joker extends Behaviour {
	
	

	
	@Override
	public boolean checkTriggered(Rabbit rabbit, World world) {
		//System.out.println("check triggered");
		BehaviourTools t = new BehaviourTools( rabbit, world );
		return t.pickUpToken( joker,true);
		
	}

	@Override
	public State newState(BehaviourTools t, boolean triggered) {
		
		if ( triggered==true ) 
        {
			//System.out.println("newState");
            return RABBIT_JOKERING;
        }
        return null;
		
	
	}
	
	
	

	

	@Override
	public boolean behave(World world, Rabbit rabbit, State state) {
		if ( state == RABBIT_JOKERING)
        {	
			world.changes.killRabbit(rabbit);
        		++world.num_saved;
            
            return true;
        }

        return false;

	}

	

	@Override
	public void cancel() {

	}

}
