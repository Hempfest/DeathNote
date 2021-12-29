package com.github.sanctum.death.construct;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class CommandAbstraction extends Command {

	protected CommandAbstraction(@NotNull String name) {
		super(name);
	}

	public abstract boolean player(Player player, String label, String[] args);

	public abstract boolean console(CommandSender sender, String label, String[] args);

	public abstract List<String> tab(Player player, String alias, String[] args);

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

		if (!(sender instanceof Player)) {
			return console(sender, commandLabel, args);
		}
		return player((Player) sender, commandLabel, args);
	}

	@Override
	public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
		List<String> tab = tab((Player) sender, alias, args);
		if (tab != null) return tab;
		return super.tabComplete(sender, alias, args);
	}
}
