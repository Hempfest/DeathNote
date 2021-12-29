package com.github.sanctum.death.construct;

import com.github.sanctum.labyrinth.data.service.Constant;
import com.github.sanctum.labyrinth.formatting.Message;
import com.github.sanctum.labyrinth.library.Deployable;
import java.util.Set;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.Nullable;

public interface DeathNote {

	DeathNote CONTACT = new DeathNoteMessage(EntityDamageEvent.DamageCause.CONTACT);
	DeathNote ENTITY_ATTACK = new DeathNoteMessage(EntityDamageEvent.DamageCause.ENTITY_ATTACK);
	DeathNote ENTITY_SWEEP_ATTACK = new DeathNoteMessage(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK);
	DeathNote PROJECTILE = new DeathNoteMessage(EntityDamageEvent.DamageCause.PROJECTILE);
	DeathNote SUFFOCATION = new DeathNoteMessage(EntityDamageEvent.DamageCause.SUFFOCATION);
	DeathNote FALL = new DeathNoteMessage(EntityDamageEvent.DamageCause.FALL);
	DeathNote FIRE = new DeathNoteMessage(EntityDamageEvent.DamageCause.FIRE);
	DeathNote FIRE_TICK = new DeathNoteMessage(EntityDamageEvent.DamageCause.FIRE_TICK);
	DeathNote FREEZING = new DeathNoteMessage(EntityDamageEvent.DamageCause.MELTING);
	DeathNote MELTING = new DeathNoteMessage(EntityDamageEvent.DamageCause.LAVA);
	DeathNote DROWNING = new DeathNoteMessage(EntityDamageEvent.DamageCause.DROWNING);
	DeathNote BLOCK_EXPLOSION = new DeathNoteMessage(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION);
	DeathNote ENTITY_EXPLOSION = new DeathNoteMessage(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
	DeathNote VOID = new DeathNoteMessage(EntityDamageEvent.DamageCause.VOID);
	DeathNote LIGHTNING = new DeathNoteMessage(EntityDamageEvent.DamageCause.LIGHTNING);
	DeathNote SUICIDE = new DeathNoteMessage(EntityDamageEvent.DamageCause.SUICIDE);
	DeathNote STARVATION = new DeathNoteMessage(EntityDamageEvent.DamageCause.STARVATION);
	DeathNote POISON = new DeathNoteMessage(EntityDamageEvent.DamageCause.POISON);
	DeathNote MAGIC = new DeathNoteMessage(EntityDamageEvent.DamageCause.MAGIC);
	DeathNote WITHER = new DeathNoteMessage(EntityDamageEvent.DamageCause.WITHER);
	DeathNote FALLING_BLOCK = new DeathNoteMessage(EntityDamageEvent.DamageCause.FALLING_BLOCK);
	DeathNote THORNS = new DeathNoteMessage(EntityDamageEvent.DamageCause.THORNS);
	DeathNote DRAGON_BREATH = new DeathNoteMessage(EntityDamageEvent.DamageCause.DRAGON_BREATH);
	DeathNote CUSTOM = new DeathNoteMessage(EntityDamageEvent.DamageCause.CUSTOM);
	DeathNote FLY_INTO_WALL = new DeathNoteMessage(EntityDamageEvent.DamageCause.FLY_INTO_WALL);
	DeathNote HOT_FLOOR = new DeathNoteMessage(EntityDamageEvent.DamageCause.HOT_FLOOR);
	DeathNote CRAMMING = new DeathNoteMessage(EntityDamageEvent.DamageCause.CRAMMING);
	DeathNote DRYOUT = new DeathNoteMessage(EntityDamageEvent.DamageCause.DRYOUT);

	Deployable<Void> write(Message message);

	Deployable<Void> save();

	Deployable<Void> register(TranslationKey... key);

	Set<TranslationKey> getTranslators();

	Message getMessage();

	EntityDamageEvent.DamageCause getCause();

	default boolean isEmpty() {
		return getMessage() == null || getMessage().isEmpty();
	}

	static DeathNote[] values() {
		return Constant.values(DeathNote.class).stream().map(Constant::getValue).toArray(DeathNote[]::new);
	}

	static @Nullable DeathNote valueOf(String name) {
		return Constant.values(DeathNote.class).stream().filter(note -> note.getName().equals(name)).findFirst().map(Constant::getValue).orElse(null);
	}

}
