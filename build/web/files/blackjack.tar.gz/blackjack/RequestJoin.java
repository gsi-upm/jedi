/*
 * RequestJoin.java Generated by Protege plugin Beanynizer. Changes will be lost!
 */
package jadex.bdi.examples.blackjack;

import jadex.base.fipa.IComponentAction;


/**
 *  Java class for concept RequestJoin of blackjack_beans ontology.
 */
public class RequestJoin implements IComponentAction
{
	//-------- attributes ----------

	/** Attribute for slot timeout. */
	protected int timeout;

	/** Attribute for slot player. */
	protected Player player;

	//-------- constructors --------

	/**
	 *  Default Constructor. <br>
	 *  Create a new <code>RequestJoin</code>.
	 */
	public RequestJoin()
	{
	}

	//-------- accessor methods --------

	/**
	 *  Get the timeout of this RequestJoin.
	 * @return timeout
	 */
	public int getTimeout()
	{
		return this.timeout;
	}

	/**
	 *  Set the timeout of this RequestJoin.
	 * @param timeout the value to be set
	 */
	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	/**
	 *  Get the player of this RequestJoin.
	 * @return player
	 */
	public Player getPlayer()
	{
		return this.player;
	}

	/**
	 *  Set the player of this RequestJoin.
	 * @param player the value to be set
	 */
	public void setPlayer(Player player)
	{
		this.player = player;
	}

	//-------- additional methods --------

	/**
	 *  Get a string representation of this RequestJoin.
	 *  @return The string representation.
	 */
	public String toString()
	{
		return "RequestJoin(" + ")";
	}

}