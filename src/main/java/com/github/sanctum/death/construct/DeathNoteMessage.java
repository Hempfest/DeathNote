package com.github.sanctum.death.construct;

import com.github.sanctum.death.DeathBukkitPlugin;
import com.github.sanctum.labyrinth.data.FileList;
import com.github.sanctum.labyrinth.data.FileManager;
import com.github.sanctum.labyrinth.data.FileType;
import com.github.sanctum.labyrinth.formatting.FancyMessage;
import com.github.sanctum.labyrinth.formatting.Message;
import com.github.sanctum.labyrinth.library.Deployable;
import com.github.sanctum.labyrinth.library.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

class DeathNoteMessage implements DeathNote {

	private Message mostRecentAdded;
	private final EntityDamageEvent.DamageCause cause;
	protected final List<Message> messages = new ArrayList<>();
	private final Set<TranslationKey> keys = new HashSet<>();
	protected final Plugin plugin = JavaPlugin.getPlugin(DeathBukkitPlugin.class);

	DeathNoteMessage(EntityDamageEvent.DamageCause cause) {
		this.cause = cause;
		FileManager manager = FileList.search(plugin).get("message_data", FileType.JSON);
		FancyMessage message = new FancyMessage();
		for (String m : manager.getRoot().getNode(cause.name()).getKeys(false)) {
			Message mess = manager.getRoot().getNode(cause.name()).getNode(m).get(Message.class);
			if (mess != null) {
				message.append(mess);
			}
		}
		if (!message.isEmpty()) {
			messages.add(new DeathMessage().append(message));
		}
	}

	@Override
	public Deployable<Void> write(Message message) {
		return Deployable.of(null, unused -> {
			this.mostRecentAdded = message;
			messages.add(message);
		});
	}

	@Override
	public Deployable<Void> save() {
		return Deployable.of(null, unused -> {
			if (this.mostRecentAdded != null) {
				FileManager manager = FileList.search(plugin).get("message_data", FileType.JSON);
				FancyMessage message = new FancyMessage();
				for (Message.Chunk c : mostRecentAdded) {
					if (!StringUtils.use(ChatColor.stripColor(c.toComponent().toLegacyText())).containsIgnoreCase("[DEATH]")) {
						message.append(c);
					}
				}
				if (!message.isEmpty()) {
					manager.getRoot().getNode(getCause().name()).getNode(mostRecentAdded.hashCode() + "").set(message);
					manager.getRoot().save();
				}
			}
		});
	}

	@Override
	public Deployable<Void> register(TranslationKey... key) {
		return Deployable.of(null, unused -> Collections.addAll(keys, key));
	}

	@Override
	public Set<TranslationKey> getTranslators() {
		return keys;
	}

	@Override
	public Message getMessage() {
		return messages.get(new Random().nextInt(Math.max(1, messages.size())));
	}

	@Override
	public EntityDamageEvent.DamageCause getCause() {
		return this.cause;
	}
}
