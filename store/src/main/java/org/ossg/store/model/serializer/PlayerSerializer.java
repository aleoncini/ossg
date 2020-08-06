package org.ossg.store.model.serializer;

import org.ossg.store.model.Player;

public interface PlayerSerializer<V> {
  
    /**
	 * Serialize the Player object
	 * 
	 * @param player
	 * @throws IllegalArgumentException in case player object contains invalid data
	 * @return
	 */
	public V serialize(Player player);
	
	/**
	 * Deserialize the specified object into a Player
	 * 
	 * @param serialized
	 * @throws IllegalArgumentException in case serialized object contains invalid data
	 * @return
	 */
	public Player deserialize(V serialized);

}