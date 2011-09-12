package jadex.bdi.examples.hunterprey.cleverprey;

import jadex.application.space.envsupport.environment.ISpaceAction;
import jadex.application.space.envsupport.environment.ISpaceObject;
import jadex.application.space.envsupport.environment.space2d.Grid2D;
import jadex.application.space.envsupport.environment.space2d.Space2D;
import jadex.application.space.envsupport.math.IVector2;
import jadex.bdi.examples.hunterprey.MoveAction;
import jadex.bdi.runtime.Plan;

import java.util.HashMap;
import java.util.Map;

/**
 *  Try to run away from a hunter.
 */
public class EscapePlan extends Plan
{
	//-------- methods --------

	/**
	 *  The plan body.
	 */
	public void body()
	{
		Grid2D	env	= (Grid2D)getBeliefbase().getBelief("env").getFact();
		ISpaceObject	myself	= (ISpaceObject)getBeliefbase().getBelief("myself").getFact();
		ISpaceObject[]	hunters	= (ISpaceObject[])getBeliefbase().getBeliefSet("seen_hunters").getFacts();

		String	move	= MoveAction.getAvoidanceDirection(env,
			(IVector2)myself.getProperty(Space2D.PROPERTY_POSITION), hunters);
		SyncResultListener srl	= new SyncResultListener();
		Map params = new HashMap();
		params.put(ISpaceAction.ACTOR_ID, getComponentIdentifier());
		params.put(MoveAction.PARAMETER_DIRECTION, move);
		env.performSpaceAction("move", params, srl);
		try
		{
			srl.waitForResult();
		}
		catch(RuntimeException e)
		{
			// When move fails ignore exception.
			fail();
		}
	}
}
