package com.github.sanctum.death.event;

import com.github.sanctum.death.construct.DeathNote;
import com.github.sanctum.labyrinth.event.custom.Vent;
import com.github.sanctum.labyrinth.formatting.Message;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class DeathMessageFormatEvent extends Vent {

	private final Player player;
	private final Entity killer;
	private final DeathNote note;
	private final Message message;
	private Block b;

	public DeathMessageFormatEvent(Player player, Entity killer, DeathNote note) {
		this.player = player;
		this.killer = killer;
		this.note = note;
		this.message = note.getMessage();
	}

	public DeathMessageFormatEvent(Player player, Entity killer, DeathNote note, Block b) {
		this(player, killer, note);
		this.b = b;
	}

	public Block getBlock() {
		if (b != null) {
			return b;
		}
		if (killer != null) {
			return killer.getLocation().getBlock();
		}
		return player.getLocation().getBlock();
	}

	public Player getPlayer() {
		return player;
	}

	public Message getFinalMessage() {
		return this.message;
	}

	public @Nullable Entity getKiller() {
		return this.killer;
	}

	public DeathNote getNote() {
		return note;
	}
}
