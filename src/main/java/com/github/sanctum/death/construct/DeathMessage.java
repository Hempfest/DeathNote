package com.github.sanctum.death.construct;

import com.github.sanctum.labyrinth.formatting.FancyMessage;
import com.github.sanctum.labyrinth.formatting.TextChunk;
import java.util.Objects;

public class DeathMessage extends FancyMessage {

	public DeathMessage() {
		super();
		super.append(new TextChunk(new Prefix.Builder().start("&f[").middle("&b&l&nDEATH").end("&f]").toString() + " "));
	}

	@Override
	public int hashCode() {
		return Objects.hash(toJson());
	}
}
