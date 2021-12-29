package com.github.sanctum.death.construct;

import com.github.sanctum.labyrinth.formatting.Message;
import com.github.sanctum.labyrinth.formatting.MessageBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class FreshBuiltMessage extends MessageBuilder {

	private final EntityDamageEvent.DamageCause cause;
	private final Player builder;
	private final boolean forStreaks;

	public FreshBuiltMessage(Player builder, Message message, EntityDamageEvent.DamageCause cause, boolean forStreaks) {
		append(message);
		this.forStreaks = forStreaks;
		this.builder = builder;
		this.cause = cause;
	}

	public boolean isForStreaks() {
		return forStreaks;
	}

	public Player getBuilder() {
		return builder;
	}

	public EntityDamageEvent.DamageCause getCause() {
		return cause;
	}

}
