package com.github.sanctum.death.construct;

import static com.github.sanctum.death.construct.DeathNote.*;

import com.github.sanctum.death.DeathBukkitPlugin;
import com.github.sanctum.death.listener.PlayerEventListener;
import com.github.sanctum.labyrinth.LabyrinthProvider;
import com.github.sanctum.labyrinth.command.CommandRegistration;
import com.github.sanctum.labyrinth.data.Registry;
import com.github.sanctum.labyrinth.formatting.string.RandomHex;
import com.github.sanctum.labyrinth.library.Deployable;
import org.bukkit.command.Command;

public class ReloadImpl {

	private final DeathBukkitPlugin plugin;

	ReloadImpl(DeathBukkitPlugin plugin) {
		this.plugin = plugin;
	}

	public static ReloadImpl initialize(DeathBukkitPlugin plugin) {
		return new ReloadImpl(plugin);
	}

	public Deployable<Void> loadCommands() {
		return Deployable.of(null, unused -> new Registry<>(Command.class).source(plugin).filter("com.github.sanctum.death.command").operate(CommandRegistration::use));
	}

	public Deployable<Void> reloadDefaults() {
		return Deployable.of(null, unused -> {

			Journal journal = Journal.getInstance();
			journal.clear().deploy();

			CONTACT.write(new DeathMessage().then("Whoa <player> has never heard of the 6ft rule. Whats next, no mask?").style(new RandomHex())).queue();
			ENTITY_ATTACK.write(new DeathMessage().then("<player> was like, totally owned by <killer>").style(new RandomHex())).queue();
			ENTITY_ATTACK.write(new DeathMessage().then("<killer> is haunting <player>'s dreams").style(new RandomHex())).queue();
			ENTITY_ATTACK.write(new DeathMessage().then("<player> should maybe not pick a fight with <killer> next time.").style(new RandomHex())).queue();
			PROJECTILE.write(new DeathMessage().then("Player <player> was gunned down by <killer>.").style(new RandomHex())).queue();
			SUFFOCATION.write(new DeathMessage().then("Player <player> died of heavy fart compression").style(new RandomHex())).queue();
			FALL.write(new DeathMessage().then("Lol <player> sucks at free falling").style(new RandomHex())).queue();
			FIRE.write(new DeathMessage().then("Quick! <player> needs some milk!").style(new RandomHex())).queue();
			FIRE_TICK.write(new DeathMessage().then("It's too late for milk <player>...").style(new RandomHex())).queue();
			MELTING.write(new DeathMessage().then("<player> *terminator exit thumbs up*").style(new RandomHex())).queue();
			FREEZING.write(new DeathMessage().then("<player> kind of pissed off frosty the snow man again...").style(new RandomHex())).queue();
			DROWNING.write(new DeathMessage().then("Player <player> thinks they're aqua man... They're not aqua man..").style(new RandomHex())).queue();
			BLOCK_EXPLOSION.write(new DeathMessage().then("<player> was sent off gloriously by <killer>").style(new RandomHex())).queue();
			ENTITY_EXPLOSION.write(new DeathMessage().then("<player> + <killer> = Dead <player>").style(new RandomHex())).queue();
			ENTITY_SWEEP_ATTACK.write(new DeathMessage().then("<killer> sept the floor with <player>").style(new RandomHex())).queue();
			VOID.write(new DeathMessage().then("Player <player> went looking for jesus in the void.").style(new RandomHex())).queue();
			LIGHTNING.write(new DeathMessage().then("Player <player> was smitten, or was it schmity? Whatever.. lightning go brr").style(new RandomHex())).queue();
			SUICIDE.write(new DeathMessage().then("Player <player> cut length ways :/").style(new RandomHex())).queue();
			STARVATION.write(new DeathMessage().then("Player <player> died of non fertility").style(new RandomHex())).queue();
			POISON.write(new DeathMessage().then("Player <player> got roofied.").style(new RandomHex())).queue();
			MAGIC.write(new DeathMessage().then("Alla-ka-za..<player> is now dead...").style(new RandomHex())).queue();
			WITHER.write(new DeathMessage().then("<player> touched the... butt..")).queue();
			FALLING_BLOCK.write(new DeathMessage().then("")).queue();

			for (DeathNote note : DeathNote.values()) {
				note.register(TranslationKey.PLAYER, TranslationKey.KILLER).deploy();
			}

			journal.write(DeathNote.values()).queue();


			if (LabyrinthProvider.getInstance().getEventMap().get("Fundamental") == null) {
				LabyrinthProvider.getInstance().getEventMap().subscribeAll(plugin, new PlayerEventListener());
			}

		});
	}

}
