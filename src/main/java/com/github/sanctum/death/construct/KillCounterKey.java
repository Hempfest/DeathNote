package com.github.sanctum.death.construct;

import com.github.sanctum.death.event.DeathMessageFormatEvent;
import com.github.sanctum.labyrinth.data.container.LabyrinthEntryMap;
import com.github.sanctum.labyrinth.data.container.LabyrinthMap;
import com.github.sanctum.labyrinth.formatting.Message;
import org.bukkit.entity.Player;

public class KillCounterKey implements TranslationKey {

	public static final LabyrinthMap<String, Integer> kills = new LabyrinthEntryMap<>();

	@Override
	public String getKey() {
		return "<streak>";
	}

	@Override
	public void format(DeathMessageFormatEvent e) {
		Player p = (Player) e.getKiller();
		assert p != null;
		Player v = e.getPlayer();

		if (!kills.containsKey(p.getUniqueId().toString())) {
			kills.put(p.getUniqueId().toString(), 1);
		} else {
			kills.put(p.getUniqueId().toString(), kills.get(p.getUniqueId().toString()) + 1);
		}

		for (Message.Chunk c : e.getFinalMessage()) {
			c.replace(getKey(), kills.get(p.getUniqueId().toString()).toString());
			c.replace("<player>", v.getDisplayName());
			c.replace("<killer>", p.getDisplayName());
		}
	}
}
