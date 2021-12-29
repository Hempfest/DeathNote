package com.github.sanctum.death.command;

import com.github.sanctum.death.construct.CommandAbstraction;
import com.github.sanctum.death.construct.MessageRenderer;
import com.github.sanctum.labyrinth.formatting.completion.SimpleTabCompletion;
import com.github.sanctum.labyrinth.formatting.completion.TabCompletionIndex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeathCommand extends CommandAbstraction {

	public DeathCommand() {
		super("deathnote");
	}

	@Override
	public boolean player(Player player, String label, String[] args) {
		if (args.length == 0) {
			MessageRenderer.startMessage(false).open(player);
		}
		if (args.length >= 1) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("streak")) {
					MessageRenderer.startMessage(true).open(player);
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("streak")) {
				MessageRenderer.startMessage(Arrays.stream(args).filter(s -> !s.equalsIgnoreCase(args[0])).collect(Collectors.joining(" ")), true).open(player);
				return true;
			}
			MessageRenderer.startMessage(String.join(" ", args), false).open(player);
		}
		return true;
	}

	@Override
	public boolean console(CommandSender sender, String label, String[] args) {
		return true;
	}

	@Override
	public List<String> tab(Player player, String alias, String[] args) {
		return SimpleTabCompletion.of(args).then(TabCompletionIndex.ONE, () -> {
			List<String> list = new ArrayList<>();
			if (player.hasPermission("deathnote")) {
				list.add("streak");
			}
			return list;
		}).get();
	}
}
