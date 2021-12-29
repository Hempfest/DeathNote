package com.github.sanctum.death;

import com.github.sanctum.death.construct.ReloadImpl;
import com.github.sanctum.labyrinth.task.TaskScheduler;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeathBukkitPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		ReloadImpl reload = ReloadImpl.initialize(this);
		reload.loadCommands().deploy();
		TaskScheduler.of(() -> reload.reloadDefaults().queue()).scheduleLater(120L);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

}
