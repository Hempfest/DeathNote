package com.github.sanctum.death.listener;

import com.github.sanctum.death.DeathBukkitPlugin;
import com.github.sanctum.death.construct.DeathMessage;
import com.github.sanctum.death.construct.DeathNote;
import com.github.sanctum.death.construct.FreshBuiltMessage;
import com.github.sanctum.death.construct.Journal;
import com.github.sanctum.death.construct.KeyManager;
import com.github.sanctum.death.event.DeathMessageFormatEvent;
import com.github.sanctum.labyrinth.data.LabyrinthPluginChannel;
import com.github.sanctum.labyrinth.data.LabyrinthPluginMessage;
import com.github.sanctum.labyrinth.data.LabyrinthPluginMessageEvent;
import com.github.sanctum.labyrinth.event.custom.LabeledAs;
import com.github.sanctum.labyrinth.event.custom.Subscribe;
import com.github.sanctum.labyrinth.event.custom.Vent;
import com.github.sanctum.labyrinth.formatting.string.SpecialID;
import com.github.sanctum.labyrinth.library.Mailer;
import java.util.Optional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

@LabeledAs("Fundamental")
public class PlayerEventListener implements Listener {

	@Subscribe(priority = Vent.Priority.READ_ONLY)
	public void onFormat(DeathMessageFormatEvent formatter) {
		DeathNote note = formatter.getNote();
		note.getTranslators().forEach(key -> key.format(formatter));
		KeyManager manager = KeyManager.getInstance();
		manager.getKeys().forEach(key -> key.format(formatter));
	}

	@Subscribe
	public void onReceive(LabyrinthPluginMessageEvent e) {
		LabyrinthPluginMessage<?> message = e.getMessage();
		if (e.getChannel().equals(LabyrinthPluginChannel.DEFAULT)) {
			if (message.getPlugin().getName().equals("DeathNote")) {
				FreshBuiltMessage fresh = (FreshBuiltMessage) message.getMessage();
				if (fresh.isForStreaks()) {
					Optional.of(Journal.getInstance()).ifPresent(j -> {
						j.streaks().write(new DeathMessage().append(fresh)).queue();
						j.streaks().save().queue();
					});
				} else {
					Journal.getInstance().read(fresh.getCause()).ifPresent(n -> {
						n.write(new DeathMessage().append(fresh)).queue();
						n.save().queue();
					});
				}
				SpecialID id = SpecialID.builder().setLength(16).build(fresh.toJson());
				Mailer mailer = Mailer.empty(JavaPlugin.getPlugin(DeathBukkitPlugin.class));
				mailer.accept(fresh.getBuilder());
				mailer.chat("&aSuccessfully registered message &e(&f#&2" + id.subSequence(0, 6) + "&e)").queue();
				mailer.info("- Logged new message @" + id + " to cause " + fresh.getCause() + " from " + fresh.getBuilder().getName()).queue();
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDeath(PlayerDeathEvent event) {
		EntityDamageEvent damage = event.getEntity().getLastDamageCause();
		if (damage == null) return;
		event.setDeathMessage(null);
		Journal.getInstance().read(damage.getCause()).ifPresent(note -> {
			if (damage instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) damage;
				if (ev.getDamager() instanceof Player && ev.getEntity() instanceof Player) {
					if (!Journal.getInstance().streaks().isEmpty()) {
						note = Journal.getInstance().streaks();
					}
				}
				Entity entity;
				if (ev.getDamager() instanceof Projectile && ((Projectile) ev.getDamager()).getShooter() != null && ((Projectile) ev.getDamager()).getShooter() instanceof Entity) {
					entity = (Entity) ((Projectile) ev.getDamager()).getShooter();
				} else {
					entity = ev.getDamager();
				}
				DeathMessageFormatEvent e = new Vent.Call<>(new DeathMessageFormatEvent(event.getEntity(), entity, note)).run();
				if (!e.isCancelled()) {
					e.getFinalMessage().send(player -> true).deploy();
				}
			} else {
				if (damage instanceof EntityDamageByBlockEvent) {
					EntityDamageByBlockEvent ev = (EntityDamageByBlockEvent) damage;
					DeathMessageFormatEvent e = new Vent.Call<>(new DeathMessageFormatEvent(event.getEntity(), damage.getEntity(), note, ev.getDamager())).run();
					if (!e.isCancelled()) {
						e.getFinalMessage().send(player -> true).deploy();
					}
				} else {
					DeathMessageFormatEvent e = new Vent.Call<>(new DeathMessageFormatEvent(event.getEntity(), damage.getEntity(), note)).run();
					if (!e.isCancelled()) {
						e.getFinalMessage().send(player -> true).deploy();
					}
				}
			}
		});

	}

}
