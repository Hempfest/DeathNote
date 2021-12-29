package com.github.sanctum.death.construct;

import com.github.sanctum.labyrinth.data.FileList;
import com.github.sanctum.labyrinth.data.FileManager;
import com.github.sanctum.labyrinth.data.FileType;
import com.github.sanctum.labyrinth.formatting.FancyMessage;
import com.github.sanctum.labyrinth.formatting.Message;
import com.github.sanctum.labyrinth.library.Deployable;
import com.github.sanctum.labyrinth.library.StringUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.entity.EntityDamageEvent;

public final class Journal {

	private static Journal INSTANCE;

	private final Map<EntityDamageEvent.DamageCause, DeathNote> NOTES = new HashMap<>();
	private DeathNote killstreaks = new DeathNoteMessage(EntityDamageEvent.DamageCause.ENTITY_ATTACK){

		{

			register(new KillCounterKey()).deploy();

			FileManager manager = FileList.search(plugin).get("streak_message_data", FileType.JSON);
			for (String m : manager.getRoot().getNode(getCause().name()).getKeys(false)) {
				Message mess = manager.getRoot().getNode(getCause().name()).getNode(m).get(Message.class);
				if (mess != null) {
					write(new DeathMessage().append(mess)).deploy();
				}
			}

		}

		@Override
		public Deployable<Void> save() {
			return Deployable.of(null, unused -> {
				FileManager manager = FileList.search(plugin).get("streak_message_data", FileType.JSON);
				for (Message m : messages) {
					FancyMessage message = new FancyMessage();
					for (Message.Chunk c : m) {
						if (!StringUtils.use(ChatColor.stripColor(c.toComponent().toLegacyText())).containsIgnoreCase("[DEATH]")) {
							message.append(c);
						}
					}
					if (!message.isEmpty()) {
						manager.getRoot().getNode(getCause().name()).getNode(m.hashCode() + "").set(message);
						manager.getRoot().save();
					}
				}
			});
		}
	};

	Journal() {
		write(killstreaks).queue();
	}

	public Deployable<Void> write(DeathNote... note) {
		return Deployable.of(null, unused -> {
			if (note.length == 1) {
				if (note[0].getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
					killstreaks = note[0];
					return;
				}
			}
			for (DeathNote n : note) {
				NOTES.put(n.getCause(), n);
			}
		});
	}

	public Deployable<Void> clear() {
		return Deployable.of(null, unused -> NOTES.clear());
	}

	public Optional<DeathNote> read(EntityDamageEvent.DamageCause cause) {
		return Optional.ofNullable(NOTES.get(cause));
	}

	public DeathNote streaks() {
		return killstreaks;
	}

	public static Journal getInstance() {
		return INSTANCE != null ? INSTANCE : (INSTANCE = new Journal());
	}


}
