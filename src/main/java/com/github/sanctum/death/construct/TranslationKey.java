package com.github.sanctum.death.construct;

import com.github.sanctum.death.event.DeathMessageFormatEvent;
import com.github.sanctum.labyrinth.formatting.Message;
import org.bukkit.entity.Player;

public interface TranslationKey {

	TranslationKey PLAYER = new TranslationKey() {

		@Override
		public String getKey() {
			return "<player>";
		}

		@Override
		public void format(DeathMessageFormatEvent messageFormatEvent) {
			for (Message.Chunk c : messageFormatEvent.getFinalMessage()) {
				c.replace(getKey(), messageFormatEvent.getPlayer().getDisplayName());
			}
		}
	};

	TranslationKey KILLER = new TranslationKey() {

		@Override
		public String getKey() {
			return "<killer>";
		}

		@Override
		public void format(DeathMessageFormatEvent messageFormatEvent) {
			for (Message.Chunk c : messageFormatEvent.getFinalMessage()) {
				if (!(messageFormatEvent.getKiller() instanceof Player)) {
					if (messageFormatEvent.getKiller() != null) {
						c.replace(getKey(), "a " + messageFormatEvent.getKiller().getName());
					}
				} else {
					c.replace(getKey(), ((Player) messageFormatEvent.getKiller()).getDisplayName());
				}
			}
		}
	};

	String getKey();

	void format(DeathMessageFormatEvent messageFormatEvent);

}
