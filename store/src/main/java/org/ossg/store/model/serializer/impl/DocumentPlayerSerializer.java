package org.ossg.store.model.serializer.impl;

import javax.enterprise.context.ApplicationScoped;

import org.bson.Document;

import org.ossg.store.model.Player;
import org.ossg.store.model.serializer.PlayerSerializer;

@ApplicationScoped
public class DocumentPlayerSerializer implements PlayerSerializer<Document>  {
    
    @Override
	public Document serialize(Player player) {

		if (player == null
				|| (player.getId() == null) 
	            || (player.getId().length() == 0) 
	            || (player.getName() == null) 
	            || (player.getName().length() == 0) 
	            || (player.getEmail() == null) 
	            || (player.getEmail().length() == 0) 
	        ) {
	            throw new IllegalArgumentException("Player object contains invalid data");
	        }

	        return new Document("id", player.getId())
	                .append("name", player.getName())
	                .append("email", player.getEmail());
	}

	@Override
	public Player deserialize(Document document) {
		
		if (document == null
				|| (document.getString("id") == null)
				|| (document.getString("id").length() == 0)
				|| (document.getString("name") == null)
				|| (document.getString("name").length() == 0)
				|| (document.getString("email") == null)
				|| (document.getString("email").length() == 0)
			) {
			throw new IllegalArgumentException("Unable to desiarilize Player object. Document contains invalid data");
        }

        String id = document.getString("id");
        String name = document.getString("name");
        String email = document.getString("email");

        return new Player()
        			.setId(id)
        			.setName(name)
        			.setEmail(email);
	}

}