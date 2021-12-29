package com.github.sanctum.death.construct;

import com.github.sanctum.death.DeathBukkitPlugin;
import com.github.sanctum.labyrinth.LabyrinthProvider;
import com.github.sanctum.labyrinth.api.Service;
import com.github.sanctum.labyrinth.formatting.FancyMessage;
import com.github.sanctum.labyrinth.gui.unity.construct.Menu;
import com.github.sanctum.labyrinth.gui.unity.impl.BorderElement;
import com.github.sanctum.labyrinth.gui.unity.impl.FillerElement;
import com.github.sanctum.labyrinth.gui.unity.impl.ItemElement;
import com.github.sanctum.labyrinth.gui.unity.impl.ListElement;
import com.github.sanctum.labyrinth.gui.unity.impl.MenuType;
import com.github.sanctum.labyrinth.library.RandomObject;
import com.github.sanctum.skulls.SkullType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageRenderer {

	private static final Plugin plugin = JavaPlugin.getProvidingPlugin(DeathBukkitPlugin.class);

	private static Material getMaterial(int hash) {
		return new RandomObject<>(Arrays.stream(Material.values()).filter(m -> !m.name().contains("LEGACY") && m != Material.AIR && m.isItem()).collect(Collectors.toList())).get(hash);
	}

	static String capital(String text) {
		String[] ar = text.split("_");
		StringBuilder string = new StringBuilder();
		for (String s : ar) {
			String t = String.valueOf(s.charAt(0)).toUpperCase(Locale.ROOT) + s.substring(1).toLowerCase(Locale.ROOT);
			if (s.equals(ar[ar.length - 1])) {
				string.append(t);
			} else {
				string.append(t).append(" ");
			}
		}
		return string.toString();
	}

	static String getBio(EntityDamageEvent.DamageCause cause) {
		switch (cause) {
			case CONTACT:
				return "Death by cactus.";
			case ENTITY_ATTACK:
				return "Death by another entity.";
			case ENTITY_SWEEP_ATTACK:
				return "Death by another entity with sweep attack.";
			case PROJECTILE:
				return "Death by flying object.";
			case SUFFOCATION:
				return "Death by no oxygen.";
			case FALL:
				return "Death by fall damage";
			case FIRE:
				return "Death by fire exposure.";
			case FIRE_TICK:
				return "Death by severe burn.";
			case MELTING:
				return "Death by snowman.";
			case LAVA:
				return "Death by lava.";
			case DROWNING:
				return "Death by water in the lungs.";
			case BLOCK_EXPLOSION:
				return "Death by block explosion.";
			case ENTITY_EXPLOSION:
				return "Death by another entity's explosion.";
			case VOID:
				return "Death by void free fall.";
			case LIGHTNING:
				return "Death by lightning";
			case SUICIDE:
				return "Death by suicide";
			case STARVATION:
				return "Death by starvation.";
			case POISON:
				return "Death by poison.";
			case MAGIC:
				return "Death by magic.";
			case WITHER:
				return "Death by wither.";
			case FALLING_BLOCK:
				return "Death by falling block.";
			case THORNS:
				return "Death by reversed damage.";
			case DRAGON_BREATH:
				return "Death by dragons breath.";
			case CUSTOM:
				return "Death by unknown circumstance";
			case FLY_INTO_WALL:
				return "Death by wall impact.";
			case HOT_FLOOR:
				return "Death by host floor.";
			case CRAMMING:
				return "Death by entity squishing.";
			case DRYOUT:
				return "Death by dry out.";
			default:
				return "You should never see this.";
		}
	}

	public static Menu outWithCause(FancyMessage message) {
		return MenuType.PAGINATED.build()
				.setTitle("Select a cause >")
				.setSize(Menu.Rows.FOUR)
				.setHost(plugin)
				.setStock(i -> {

					i.addItem(ed -> {
						ed.setElement(it -> it.setTitle("Next page").setItem(SkullType.ARROW_BLACK_RIGHT.get()).build());
						ed.setType(ItemElement.ControlType.BUTTON_NEXT);
						ed.setSlot(35);
					});

					i.addItem(ed -> {
						ed.setElement(it -> it.setTitle("Previous page").setItem(SkullType.ARROW_BLACK_LEFT.get()).build());
						ed.setType(ItemElement.ControlType.BUTTON_BACK);
						ed.setSlot(27);
					});

					ListElement<EntityDamageEvent.DamageCause> list = new ListElement<>(Arrays.asList(EntityDamageEvent.DamageCause.values()));
					list.setLimit(14);
					list.setPopulate((cause, item) -> {
						item.setElement(ed -> ed.setType(getMaterial(cause.hashCode())).setTitle(capital(cause.name()) + " &e»").setLore("&f" + getBio(cause)).build());
						item.setClick(click -> {
							click.setCancelled(true);
							LabyrinthProvider.getService(Service.MESSENGER).sendPluginMessage(plugin, new FreshBuiltMessage(click.getElement(), message, cause, false)).queue();
						});
					});
					i.addItem(list);

					FillerElement<?> filler = new FillerElement<>(i);
					filler.add(ed -> ed.setElement(it -> it.setTitle(" ").setItem(SkullType.COMMAND_BLOCK.get()).build()));
					i.addItem(filler);

					BorderElement<?> border = new BorderElement<>(i);
					for (Menu.Panel p : Menu.Panel.values()) {
						if (p == Menu.Panel.MIDDLE) continue;
						border.add(p, ed -> ed.setElement(it -> it.setTitle(" ").setItem(SkullType.ARROW_BLUE_UP.get()).build()));
					}
					i.addItem(border);

				})
				.join();
	}

	public static Menu control(FancyMessage message, boolean streak) {
		if (streak) {
			return MenuType.SINGULAR.build()
					.setTitle("Killstreak Message control")
					.setHost(plugin)
					.setSize(Menu.Rows.THREE)
					.setStock(i -> {

						i.addItem(ed -> {
							List<String> text = new ArrayList<>();
							for (BaseComponent b : message.build()) {
								text.add(b.toLegacyText());
							}
							ed.setElement(it -> it.setType(Material.GLASS_PANE).setTitle(String.join(" ", text)).build());
							ed.setClick(click -> {
								click.setCancelled(true);
								message.send(click.getElement()).queue();
							});
							ed.setSlot(13);
						});

						// add suggestion
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.PURPLE_DYE).setTitle("Add suggestible text to the current message section.").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setClick(click -> {
								click.setCancelled(true);
								addSuggest(message, true).open(click.getElement());
							});
							ed.setSlot(21);

						});
						// add copy
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.LIGHT_BLUE_DYE).setTitle("Add copyable text to the current message section.").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setClick(click -> {
								click.setCancelled(true);
								addCopy(message, true).open(click.getElement());
							});
							ed.setSlot(22);

						});
						// add hover
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.BLUE_DYE).setTitle("Add a hover message to the current message section.").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setClick(click -> {
								click.setCancelled(true);
								addHover(message, true).open(click.getElement());
							});
							ed.setSlot(23);

						});
						// add new section
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.GREEN_DYE).setTitle("Add a new message section to style.").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setClick(click -> {
								click.setCancelled(true);
								addSection(message, true).open(click.getElement());
							});
							ed.setSlot(5);
						});
						// add new space section
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.WHITE_DYE).setTitle("Add a new space section to the message.").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setClick(click -> {
								click.setCancelled(true);
								message.then(" ");
								new DeathMessage().then("You added text " + '"' + " " + '"' + " to the message.").send(click.getElement()).queue();
							});
							ed.setSlot(3);
						});
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.ANVIL).setTitle("&aComplete the message.").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setClick(click -> {
								click.setCancelled(true);
								click.getElement().closeInventory();
								LabyrinthProvider.getService(Service.MESSENGER).sendPluginMessage(plugin, new FreshBuiltMessage(click.getElement(), message, EntityDamageEvent.DamageCause.ENTITY_ATTACK, true)).queue();
							});
							ed.setSlot(4);
						});
						FillerElement<?> filler = new FillerElement<>(i);
						filler.add(ed -> ed.setElement(it -> it.setTitle(" ").setItem(SkullType.COMMAND_BLOCK.get()).build()));
						i.addItem(filler);

						BorderElement<?> border = new BorderElement<>(i);
						for (Menu.Panel p : Menu.Panel.values()) {
							if (p == Menu.Panel.MIDDLE) continue;
							border.add(p, ed -> ed.setElement(it -> it.setTitle(" ").setItem(SkullType.ARROW_BLUE_UP.get()).build()));
						}
						i.addItem(border);
					})
					.join();
		} else {
			return MenuType.SINGULAR.build()
					.setTitle("Message control")
					.setHost(plugin)
					.setSize(Menu.Rows.THREE)
					.setStock(i -> {

						i.addItem(ed -> {
							List<String> text = new ArrayList<>();
							for (BaseComponent b : message.build()) {
								text.add(b.toLegacyText());
							}
							ed.setElement(it -> it.setType(Material.GLASS_PANE).setTitle(String.join(" ", text)).build());
							ed.setClick(click -> {
								click.setCancelled(true);
								message.send(click.getElement()).queue();
							});
							ed.setSlot(13);
						});

						// add suggestion
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.PURPLE_DYE).setTitle("Add suggestible text to the current message section.").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setClick(click -> {
								click.setCancelled(true);
								addSuggest(message, false).open(click.getElement());
							});
							ed.setSlot(21);

						});
						// add copy
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.LIGHT_BLUE_DYE).setTitle("Add copyable text to the current message section.").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setClick(click -> {
								click.setCancelled(true);
								addCopy(message, false).open(click.getElement());
							});
							ed.setSlot(22);

						});
						// add hover
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.BLUE_DYE).setTitle("Add a hover message to the current message section.").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setClick(click -> {
								click.setCancelled(true);
								addHover(message, false).open(click.getElement());
							});
							ed.setSlot(23);

						});
						// add new section
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.GREEN_DYE).setTitle("Add a new message section to style.").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setClick(click -> {
								click.setCancelled(true);
								addSection(message, false).open(click.getElement());
							});
							ed.setSlot(5);
						});
						// add new space section
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.WHITE_DYE).setTitle("Add a new space section to the message.").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setClick(click -> {
								click.setCancelled(true);
								message.then(" ");
								new DeathMessage().then("You added text " + '"' + " " + '"' + " to the message.").send(click.getElement()).queue();
							});
							ed.setSlot(3);
						});
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.ANVIL).setTitle("&aComplete the message.").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setClick(click -> {
								click.setCancelled(true);
								// TODO: Open cause gallery
								outWithCause(message).open(click.getElement());
							});
							ed.setSlot(4);
						});
						FillerElement<?> filler = new FillerElement<>(i);
						filler.add(ed -> ed.setElement(it -> it.setTitle(" ").setItem(SkullType.COMMAND_BLOCK.get()).build()));
						i.addItem(filler);

						BorderElement<?> border = new BorderElement<>(i);
						for (Menu.Panel p : Menu.Panel.values()) {
							if (p == Menu.Panel.MIDDLE) continue;
							border.add(p, ed -> ed.setElement(it -> it.setTitle(" ").setItem(SkullType.ARROW_BLUE_UP.get()).build()));
						}
						i.addItem(border);
					})
					.join();
		}
	}

	public static Menu startMessage(String text, boolean streak) {
		final FancyMessage message = new FancyMessage().then(text);
		return control(message, streak);
	}

	public static Menu startMessage(boolean streak) {
		final FancyMessage message = new FancyMessage();
		return control(message, streak);
	}


	public static Menu addHover(FancyMessage message, boolean streak) {
		if (streak) {
			return MenuType.PRINTABLE.build()
					.setTitle("Add a hover message")
					.setHost(plugin)
					.setSize(Menu.Rows.ONE)
					.setStock(i -> {
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.CHORUS_FLOWER).setTitle("&7&oText here...").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setSlot(0);
						});
					})
					.join().addAction(click -> {
						if (click.getSlot() == 2) {
							click.setCancelled(true);
							message.hover(click.getParent().getName());
							new DeathMessage().then("You added text " + '"' + click.getParent().getName() + "&r" + '"' + " to the message.").send(click.getElement()).queue();
							control(message, true).open(click.getElement());
						}
					});
		} else {
			return MenuType.PRINTABLE.build()
					.setTitle("Add a hover message")
					.setHost(plugin)
					.setSize(Menu.Rows.ONE)
					.setStock(i -> {
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.CHORUS_FLOWER).setTitle("&7&oText here...").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setSlot(0);
						});
					})
					.join().addAction(click -> {
						if (click.getSlot() == 2) {
							click.setCancelled(true);
							message.hover(click.getParent().getName());
							new DeathMessage().then("You added text " + '"' + click.getParent().getName() + "&r" + '"' + " to the message.").send(click.getElement()).queue();
							control(message, false).open(click.getElement());
						}
					});
		}

	}

	public static Menu addSuggest(FancyMessage message, boolean streak) {
		if (streak) {
			return MenuType.PRINTABLE.build()
					.setTitle("Add a text suggestion to the message")
					.setHost(plugin)
					.setSize(Menu.Rows.ONE)
					.setStock(i -> {
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.CHORUS_FLOWER).setTitle("&7&oText here...").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setSlot(0);
						});
					})
					.join().addAction(click -> {
						if (click.getSlot() == 2) {
							click.setCancelled(true);
							message.suggest(click.getParent().getName());
							new DeathMessage().then("You added suggestion text " + '"' + click.getParent().getName() + "&r" + '"' + " to the message.").send(click.getElement()).queue();
							control(message, true).open(click.getElement());
						}
					});
		} else {
			return MenuType.PRINTABLE.build()
					.setTitle("Add a text suggestion to the message")
					.setHost(plugin)
					.setSize(Menu.Rows.ONE)
					.setStock(i -> {
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.CHORUS_FLOWER).setTitle("&7&oText here...").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setSlot(0);
						});
					})
					.join().addAction(click -> {
						if (click.getSlot() == 2) {
							click.setCancelled(true);
							message.suggest(click.getParent().getName());
							new DeathMessage().then("You added suggestion text " + '"' + click.getParent().getName() + "&r" + '"' + " to the message.").send(click.getElement()).queue();
							control(message, false).open(click.getElement());
						}
					});
		}
	}

	public static Menu addCopy(FancyMessage message, boolean streak) {
		if (streak) {
			return MenuType.PRINTABLE.build()
					.setTitle("Add copy text to the message")
					.setHost(plugin)
					.setSize(Menu.Rows.ONE)
					.setStock(i -> {
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.CHORUS_FLOWER).setTitle("&7&oText here...").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setSlot(0);
						});
					})
					.join().addAction(click -> {
						if (click.getSlot() == 2) {
							click.setCancelled(true);
							message.copy(click.getParent().getName());
							new DeathMessage().then("You added copyable text " + '"' + click.getParent().getName() + "&r" + '"' + " to the message.").send(click.getElement()).queue();
							control(message, true).open(click.getElement());
						}
					});
		} else {
			return MenuType.PRINTABLE.build()
					.setTitle("Add copy text to the message")
					.setHost(plugin)
					.setSize(Menu.Rows.ONE)
					.setStock(i -> {
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.CHORUS_FLOWER).setTitle("&7&oText here...").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setSlot(0);
						});
					})
					.join().addAction(click -> {
						if (click.getSlot() == 2) {
							click.setCancelled(true);
							message.copy(click.getParent().getName());
							new DeathMessage().then("You added copyable text " + '"' + click.getParent().getName() + "&r" + '"' + " to the message.").send(click.getElement()).queue();
							control(message, false).open(click.getElement());
						}
					});
		}
	}

	public static Menu addSection(FancyMessage message, boolean streak) {
		if (streak) {
			return MenuType.PRINTABLE.build()
					.setTitle("Append a new section")
					.setHost(plugin)
					.setSize(Menu.Rows.ONE)
					.setStock(i -> {
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.CHORUS_FLOWER).setTitle("&7&oText here...").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setSlot(0);
						});
					})
					.join().addAction(click -> {
						if (click.getSlot() == 2) {
							click.setCancelled(true);
							message.then(click.getParent().getName());
							new DeathMessage().then("You added text " + '"' + click.getParent().getName() + "&r" + '"' + " to the message.").send(click.getElement()).queue();
							control(message, true).open(click.getElement());
						}
					});
		} else {
			return MenuType.PRINTABLE.build()
					.setTitle("Append a new section")
					.setHost(plugin)
					.setSize(Menu.Rows.ONE)
					.setStock(i -> {
						i.addItem(ed -> {
							ed.setElement(it -> it.setType(Material.CHORUS_FLOWER).setTitle("&7&oText here...").build());
							ed.setType(ItemElement.ControlType.DISPLAY);
							ed.setSlot(0);
						});
					})
					.join().addAction(click -> {
						if (click.getSlot() == 2) {
							click.setCancelled(true);
							message.then(click.getParent().getName());
							new DeathMessage().then("You added text " + '"' + click.getParent().getName() + "&r" + '"' + " to the message.").send(click.getElement()).queue();
							control(message, false).open(click.getElement());
						}
					});
		}
	}


}
