/*
 * RequestProduction.java Generated by Protege plugin Beanynizer. Changes will
 * be lost!
 */
package jadex.bdi.examples.marsworld_classic;

import jadex.base.fipa.IComponentAction;


/**
 *  Java class for concept RequestProduction of mars_beans ontology.
 */
public class RequestProduction implements IComponentAction
{
	//-------- attributes ----------

	/** Attribute for slot target. */
	protected Target	target;

	//-------- constructors --------

	/**
	 *  Default Constructor.
	 *  Create a new RequestProduction.
	 */
	public RequestProduction()
	{
	}

	//-------- accessor methods --------

	/**
	 *  Get the target of this RequestProduction.
	 * @return target
	 */
	public Target getTarget()
	{
		return this.target;
	}

	/**
	 *  Set the target of this RequestProduction.
	 * @param target the value to be set
	 */
	public void setTarget(Target target)
	{
		this.target = target;
	}

	//-------- object methods --------

	/**
	 *  Get a string representation of this RequestProduction.
	 *  @return The string representation.
	 */
	public String toString()
	{
		return "RequestProduction(" + ")";
	}
}
