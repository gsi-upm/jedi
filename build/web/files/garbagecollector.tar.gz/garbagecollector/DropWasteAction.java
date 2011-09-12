package jadex.bdi.examples.garbagecollector;

import jadex.application.space.envsupport.environment.IEnvironmentSpace;
import jadex.application.space.envsupport.environment.ISpaceAction;
import jadex.application.space.envsupport.environment.ISpaceObject;
import jadex.application.space.envsupport.environment.space2d.Grid2D;
import jadex.application.space.envsupport.math.IVector2;
import jadex.bridge.IComponentIdentifier;
import jadex.commons.SimplePropertyObject;

import java.util.Map;

/**
 *  Action for dropping waste on the robots field.
 */
public class DropWasteAction extends SimplePropertyObject implements ISpaceAction
{
	/**
	 * Performs the action.
	 * @param parameters parameters for the action
	 * @param space the environment space
	 * @return action return value
	 */
	public Object perform(Map parameters, IEnvironmentSpace space)
	{
//		System.out.println("drop waste action: "+parameters);
		
		Grid2D grid = (Grid2D)space;
		
		IComponentIdentifier owner = (IComponentIdentifier)parameters.get(ISpaceAction.ACTOR_ID);
		ISpaceObject so = grid.getAvatar(owner);
		IVector2 pos = (IVector2)so.getProperty(Grid2D.PROPERTY_POSITION);
		
		assert so.getProperty("garbage")!=null;

		ISpaceObject garb = (ISpaceObject)so.getProperty("garbage");
		grid.setPosition(garb.getId(), pos);
		so.setProperty("garbage", null);
		
//		System.out.println("Agent dropped garbage: "+owner+" "+pos);
		
		return null;
	}

	/**
	 * Returns the ID of the action.
	 * @return ID of the action
	 */
	public Object getId()
	{
		// todo: remove here or from application xml?
		return "drop";
	}
}
