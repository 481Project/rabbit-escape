package rabbitescape.engine.behaviours;

import static rabbitescape.engine.ChangeDescription.State.*;

import rabbitescape.engine.Behaviour;
import rabbitescape.engine.BehaviourTools;
import rabbitescape.engine.ChangeDescription.State;
import rabbitescape.engine.Rabbit;
import rabbitescape.engine.World;
//<<<<<<< HEAD
import static rabbitescape.engine.Token.Type.*;
//=======
//>>>>>>> e06cb05ff3a5fffcaeb94efb6097de0fd441e330

public class Bombing extends Behaviour
{
    @Override
    public void cancel()
    {
    }

//<<<<<<< HEAD
    @Override
    public boolean checkTriggered( Rabbit rabbit, World world )
    {
        BehaviourTools t = new BehaviourTools( rabbit, world );
        return t.pickUpToken( bomb, true );
    }

    @Override
    public State newState( BehaviourTools t, boolean triggered )
    {
        if ( triggered )
        {
            return RABBIT_BOMBING;
        } 
    return null;
}
//=======
  /*  @Override
    public boolean checkTriggered( Rabbit rabbit, World world )
    {
        return world.fireAt( rabbit.x, rabbit.y );
    } */

  /*  @Override
    public State newState(
        BehaviourTools t, boolean triggered
        )
    {
        if ( triggered )
        {
            if ( t.rabbit.onSlope )
            {
                return RABBIT_BOMBING_ON_SLOPE;
            }
            else
            {
                return RABBIT_BOMBING;
            }
        }

//>>>>>>> e06cb05ff3a5fffcaeb94efb6097de0fd441e330
        return null;
    } */

    @Override
    public boolean behave( World world, Rabbit rabbit, State state )
    {
//<<<<<<< HEAD
        if(state==RABBIT_BOMBING){
		world.changes.killRabbit( rabbit );
		return true;
		
	}
	
//=======
        switch ( state )
        {
        case RABBIT_BOMBING:
        case RABBIT_BOMBING_ON_SLOPE:
        {
            world.changes.killRabbit( rabbit );
            return true;
        }
        default:
        {
            return false;
        }
        }
//>>>>>>> e06cb05ff3a5fffcaeb94efb6097de0fd441e330
    }
}
