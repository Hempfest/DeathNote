package com.github.sanctum.death.construct;

import com.github.sanctum.death.event.DeathMessageFormatEvent;
import com.github.sanctum.labyrinth.formatting.Message;
import com.github.sanctum.labyrinth.library.Deployable;

public class StringKey implements TranslationKey {

	private final String target;
	private final String replace;

	public StringKey(String target, String replace) {
		this.target = target;
		this.replace = replace;
	}

	@Override
	public String getKey() {
		return target;
	}

	@Override
	public void format(DeathMessageFormatEvent messageFormatEvent) {
		for (Message.Chunk c : messageFormatEvent.getFinalMessage()) {
			c.replace(getKey(), replace);
		}
	}

	public Deployable<Void> addTo(DeathNote note) {
		return Deployable.of(null, unused -> note.register(this).queue());
	}

}
