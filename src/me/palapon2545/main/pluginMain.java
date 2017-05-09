package me.palapon2545.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import me.palapon2545.main.pluginMain;
import me.palapon2545.main.ActionBar;
import me.palapon2545.main.DelayLoadConfig;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;

public class pluginMain extends JavaPlugin implements Listener {

	public HashMap<String, Long> cooldowns = new HashMap<String, Long>();
	public final Logger logger = Logger.getLogger("Minecraft");
	public pluginMain plugin;
	public static DelayLoadConfig delayLoadConfig = null;
	public static Thread delayLoadConnfig_Thread = null;
	LinkedList<String> badWord = new LinkedList<String>();

	String sv = ChatColor.BLUE + "Server> " + ChatColor.GRAY;
	String portalpre = ChatColor.BLUE + "Portal> " + ChatColor.GRAY;
	String noperm = ChatColor.RED + "You don't have permission or op!";
	String wrongplayer = ChatColor.RED + "Player not found.";
	String type = ChatColor.GRAY + "Type: " + ChatColor.GREEN;
	String join = ChatColor.GREEN + "Join> ";
	String left = ChatColor.RED + "Left> ";
	String noi = "You don't have enough item.";
	String nom = "You don't have enough money";
	String lc = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Lucky" + ChatColor.YELLOW + ChatColor.BOLD + "Click> "
			+ ChatColor.WHITE;

	public void onDisable() {
		Bukkit.broadcastMessage(sv + "SMDMain System: " + ChatColor.RED + ChatColor.BOLD + "Disable");
		for (Player player1 : Bukkit.getOnlinePlayers()) {
			player1.playSound(player1.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 0);
		}
		saveConfig();
		delayLoadConfig.setRunning(false);
	}

	public void onEnable() {
		Bukkit.broadcastMessage(sv + "SMDMain System: " + ChatColor.GREEN + ChatColor.BOLD + "Enable");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		getConfig().options().copyDefaults(true);
		getConfig().set("warp", null);
		saveConfig();
		for (Player player1 : Bukkit.getOnlinePlayers()) {
			player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
		}
		delayLoadConfig = new DelayLoadConfig();
		delayLoadConnfig_Thread = new Thread(delayLoadConfig);
		delayLoadConnfig_Thread.start();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		String message = "";
		Player player = (Player) sender;
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String rank = playerData.getString("rank");
		if (CommandLabel.equalsIgnoreCase("setspawn") || CommandLabel.equalsIgnoreCase("SMDMain:setspawn")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.setspawn")) {
				Location pl = player.getLocation();
				double plx = pl.getX();
				double ply = pl.getY();
				double plz = pl.getZ();
				double plyaw = pl.getYaw();
				double plpitch = pl.getPitch();
				String plworld = pl.getWorld().getName();
				getConfig().set("spawn" + "." + "spawn" + ".world", plworld);
				getConfig().set("spawn" + "." + "spawn" + ".x", plx);
				getConfig().set("spawn" + "." + "spawn" + ".y", ply);
				getConfig().set("spawn" + "." + "spawn" + ".z", plz);
				getConfig().set("spawn" + "." + "spawn" + ".yaw", plyaw);
				getConfig().set("spawn" + "." + "spawn" + ".pitch", plpitch);
				saveConfig();
				player.sendMessage(ChatColor.BLUE + "Portal>" + ChatColor.GRAY + " Setspawn Complete!");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("spawn") || CommandLabel.equalsIgnoreCase("ts")
				|| CommandLabel.equalsIgnoreCase("SMDMain:spawn") || CommandLabel.equalsIgnoreCase("SMDMain:ts")) {
			String spawn = getConfig().getString("spawn");
			if (spawn != null) {
				Double x = getConfig().getDouble("spawn" + "." + "spawn" + ".x");
				Double y = getConfig().getDouble("spawn" + "." + "spawn" + ".y");
				Double z = getConfig().getDouble("spawn" + "." + "spawn" + ".z");
				float yaw = (float) getConfig().getDouble("spawn" + "." + "spawn" + ".yaw");
				float pitch = (float) getConfig().getDouble("spawn" + "." + "spawn" + ".pitch");
				String world = getConfig().getString("spawn" + "." + "spawn" + ".world");
				World p = Bukkit.getWorld(world);
				Location loc = new Location(p, x, y, z);
				loc.setPitch(pitch);
				loc.setYaw(yaw);
				player.teleport(loc);
				player.sendMessage(portalpre + "Teleported to " + ChatColor.YELLOW + "Spawn" + ChatColor.GRAY + ".");
				player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
			} else {
				player.sendMessage(portalpre + "Spawn location not found!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);

			}
		}
		// sethome

		if (CommandLabel.equalsIgnoreCase("sethome") || CommandLabel.equalsIgnoreCase("sh")
				|| CommandLabel.equalsIgnoreCase("SMDMain:sh") || CommandLabel.equalsIgnoreCase("SMDMain:sethome")) {
			if (f.exists()) {
				if (playerData.getString("home") != null) {
					player.sendMessage(portalpre + ChatColor.RED + "Maximum sethome reach.");
					player.sendMessage(portalpre + "Allow Sethome:" + ChatColor.YELLOW + " 1 home(s)");
					player.sendMessage(portalpre + "You need to remove your old house first");
					player.sendMessage(portalpre + type + "/removehome , /rh");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
				} else {
					Location pl = player.getLocation();
					double plx = pl.getX();
					double ply = pl.getY();
					double plz = pl.getZ();
					double plpitch = pl.getPitch();
					double plyaw = pl.getYaw();
					int x = (int) plx;
					int y = (int) ply;
					int z = (int) plz;
					String plw = pl.getWorld().getName();
					World w = Bukkit.getWorld(plw);
					try {
						playerData.createSection("home");
						playerData.set("home.world", plw);
						playerData.set("home.x", plx);
						playerData.set("home.y", ply);
						playerData.set("home.z", plz);
						playerData.set("home.pitch", plpitch);
						playerData.set("home.yaw", plyaw);
						playerData.save(f);
					} catch (IOException e) {
						e.printStackTrace();
					}
					Location loc = new Location(w, plx, ply, plz);
					player.setBedSpawnLocation(loc);
					player.sendMessage(portalpre + "Sethome complete.");
					player.sendMessage(portalpre + "At location " + ChatColor.YELLOW + x + ", " + y + ", " + z
							+ ChatColor.LIGHT_PURPLE + " at World " + ChatColor.GOLD + plw);
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("home") || CommandLabel.equalsIgnoreCase("h")
				|| CommandLabel.equalsIgnoreCase("SMDMain:home") || CommandLabel.equalsIgnoreCase("SMDMain:h")) {
			if (f.exists()) {
				if (playerData.getString("home") != null) {
					double x = playerData.getDouble("home.x");
					double y = playerData.getDouble("home.y");
					double z = playerData.getDouble("home.z");
					double pitch = playerData.getDouble("home.pitch");
					double yaw = playerData.getDouble("home.yaw");
					String world = playerData.getString("home.world");
					World p = Bukkit.getWorld(world);
					Location loc = new Location(p, x, y, z);
					loc.setPitch((float) pitch);
					loc.setYaw((float) yaw);
					player.teleport(loc);
					player.sendMessage(portalpre + "Teleported to " + ChatColor.YELLOW + "Home" + ChatColor.GRAY + ".");
					player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
				} else {
					player.sendMessage(portalpre + "You didn't sethome yet.");
					player.sendMessage(portalpre + type + "/sethome");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("locationhome") || CommandLabel.equalsIgnoreCase("lh")
				|| CommandLabel.equalsIgnoreCase("SMDMain:locationhome")
				|| CommandLabel.equalsIgnoreCase("SMDMain:lh")) {
			if (f.exists()) {
				if (playerData.getString("home") != null) {
					// Have Home
					int x = playerData.getInt("home.x");
					int y = playerData.getInt("home.y");
					int z = playerData.getInt("home.z");
					int pitch = playerData.getInt("home.pitch");
					int yaw = playerData.getInt("home.yaw");
					String world = playerData.getString("home.world");
					player.sendMessage(portalpre + ChatColor.AQUA + "===Home info===");
					player.sendMessage(portalpre + ChatColor.YELLOW + "World: " + ChatColor.GREEN + world);
					player.sendMessage(portalpre + ChatColor.YELLOW + "Location: " + ChatColor.GREEN + "X:" + x
							+ ChatColor.AQUA + " Y:" + y + ChatColor.YELLOW + " Z:" + z);
					player.sendMessage(portalpre + ChatColor.YELLOW + "Yaw: " + ChatColor.GREEN + yaw);
					player.sendMessage(portalpre + ChatColor.YELLOW + "Pitch: " + ChatColor.GREEN + pitch);
				} else {
					// Not set home yet
					player.sendMessage(portalpre + "You didn't sethome yet.");
					player.sendMessage(portalpre + type + "/sethome");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		// remove home
		if (CommandLabel.equalsIgnoreCase("removehome") || CommandLabel.equalsIgnoreCase("rh")
				|| CommandLabel.equalsIgnoreCase("SMDMain:rh") || CommandLabel.equalsIgnoreCase("SMDMain:removehome")) {
			if (f.exists()) {
				int x = playerData.getInt("home.x");
				int y = playerData.getInt("home.y");
				int z = playerData.getInt("home.z");
				String world = playerData.getString("home.world");
				if (playerData.getString("home") != null) {
					try {
						playerData.set("home", null);
						playerData.save(f);
					} catch (IOException e) {
						e.printStackTrace();
					}
					player.sendMessage(
							portalpre + "Remove home complete. [" + x + ", " + y + ", " + z + ", " + world + "]");
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 10, 1);
				} else {
					// Not set home yet
					player.sendMessage(portalpre + "You didn't sethome yet.");
					player.sendMessage(portalpre + type + "/sethome");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("warp") || CommandLabel.equalsIgnoreCase("SMDMain:warp")) {
			String warp = getConfig().getString("warp");
			if (warp != null) {
				double x = getConfig().getDouble("warp.x");
				double y = getConfig().getDouble("warp.y");
				double z = getConfig().getDouble("warp.z");
				double yaw = getConfig().getDouble("warp.yaw");
				double pitch = getConfig().getDouble("warp.pitch");
				String world = getConfig().getString("warp.world");
				World p = Bukkit.getWorld(world);
				Location loc = new Location(p, x, y, z);
				loc.setPitch((float) pitch);
				loc.setYaw((float) yaw);
				player.teleport(loc);
				player.sendMessage(
						portalpre + "Teleported to " + ChatColor.YELLOW + "Server Warp" + ChatColor.GRAY + ".");
				player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
			} else if (warp == null) {
				player.sendMessage(portalpre + ChatColor.YELLOW + "Server Warp " + ChatColor.GRAY + "isn't open");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
			}
		}
		if (CommandLabel.equalsIgnoreCase("setwarp") || CommandLabel.equalsIgnoreCase("SMDMain:setwarp")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.warp")) {
				if (f.exists()) {
					String pls = player.getName();
					if (args.length == 0) {
						Location pl = player.getLocation();
						double plx = pl.getX();
						double ply = pl.getY();
						double plz = pl.getZ();
						double plpitch = pl.getPitch();
						double plyaw = pl.getYaw();
						String plw = pl.getWorld().getName();
						getConfig().createSection("warp");
						getConfig().set("warp.world", plw);
						getConfig().set("warp.x", plx);
						getConfig().set("warp.y", ply);
						getConfig().set("warp.z", plz);
						getConfig().set("warp.pitch", plpitch);
						getConfig().set("warp.yaw", plyaw);
						saveConfig();
						player.sendMessage(portalpre + "Set server warp at your location completed.");
						for (Player player1 : Bukkit.getOnlinePlayers()) {
							player1.sendMessage(portalpre + ChatColor.YELLOW + pls + ChatColor.GREEN + " open"
									+ ChatColor.LIGHT_PURPLE + " server warp!");
							player1.sendMessage(portalpre + "Type: /warp to warp.");
							player1.playSound(player1.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 0);
						}
					}
				}
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);

			}
		}
		if (CommandLabel.equalsIgnoreCase("removewarp") || CommandLabel.equalsIgnoreCase("SMDMain:removewarp")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.warp")) {
				if (f.exists()) {
					if (getConfig().getConfigurationSection("warp") != null) {
						getConfig().set("warp", null);
						player.sendMessage(portalpre + "Remove server warp complete.");
						for (Player player1 : Bukkit.getOnlinePlayers()) {
							player1.sendMessage(
									portalpre + ChatColor.RED + "Closed" + ChatColor.LIGHT_PURPLE + " Server Warp.");
							player1.playSound(player1.getLocation(), Sound.BLOCK_ANVIL_BREAK, 10, 1);

						}

					} else if (getConfig().getConfigurationSection("warp") == null) {
						player.sendMessage(portalpre + "You didn't set server warp yet.");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);

					}
				}

			}
		}
		if (CommandLabel.equalsIgnoreCase("gamemode") || CommandLabel.equalsIgnoreCase("SMDMain:gamemode")
				|| CommandLabel.equalsIgnoreCase("gm") || CommandLabel.equalsIgnoreCase("SMDMain:gm")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.gamemode")) {
				if (args.length == 0) {
					player.sendMessage(sv + type + "/gamemode [mode] [player] (/gm)");
					player.sendMessage(ChatColor.GREEN + "Available Mode: ");
					player.sendMessage(ChatColor.WHITE + "- " + ChatColor.GREEN + "Survival , S , 0");
					player.sendMessage(ChatColor.WHITE + "- " + ChatColor.GREEN + "Creative , C , 1");
					player.sendMessage(ChatColor.WHITE + "- " + ChatColor.GREEN + "Adventure , A , 2");
					player.sendMessage(ChatColor.WHITE + "- " + ChatColor.GREEN + "Spectator , SP , 3");
				}
				if (args.length == 1) {
					if ((args[0].equalsIgnoreCase("1")) || (args[0].equalsIgnoreCase("c"))
							|| (args[0].equalsIgnoreCase("creative"))) {
						player.setGameMode(GameMode.CREATIVE);
						player.sendMessage(sv + "Your gamemode has been updated to " + ChatColor.GREEN + "Creative.");
						player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
					} else if ((args[0].equalsIgnoreCase("0")) || (args[0].equalsIgnoreCase("s"))
							|| (args[0].equalsIgnoreCase("survival"))) {
						player.setGameMode(GameMode.SURVIVAL);
						player.sendMessage(sv + "Your gamemode has been updated to " + ChatColor.YELLOW + "Survival.");
						player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
					} else if ((args[0].equalsIgnoreCase("2")) || (args[0].equalsIgnoreCase("a"))
							|| (args[0].equalsIgnoreCase("adventure"))) {
						player.setGameMode(GameMode.ADVENTURE);
						player.sendMessage(
								sv + "Your gamemode has been updated to " + ChatColor.LIGHT_PURPLE + "Adventure.");
						player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
					} else if ((args[0].equalsIgnoreCase("3")) || (args[0].equalsIgnoreCase("sp"))
							|| (args[0].equalsIgnoreCase("spectator"))) {
						player.setGameMode(GameMode.SPECTATOR);
						player.sendMessage(sv + "Your gamemode has been updated to " + ChatColor.AQUA + "Spectator.");
						player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
					}
				}
				if (args.length == 2) {
					if (Bukkit.getServer().getPlayer(args[1]) != null) {
						Player targetPlayer = player.getServer().getPlayer(args[1]);
						String targetPlayerName = targetPlayer.getName();
						if ((args[0].equalsIgnoreCase("1")) || (args[0].equalsIgnoreCase("c"))
								|| (args[0].equalsIgnoreCase("creative"))) {
							targetPlayer.setGameMode(GameMode.CREATIVE);
							targetPlayer.sendMessage(
									sv + "Your gamemode has been updated to " + ChatColor.GREEN + "Creative.");
							player.sendMessage(sv + ChatColor.YELLOW + targetPlayerName + "'s " + ChatColor.GRAY
									+ "gamemode has been updated to " + ChatColor.GREEN + "Creative.");
							player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
						} else if ((args[0].equalsIgnoreCase("0")) || (args[0].equalsIgnoreCase("s"))
								|| (args[0].equalsIgnoreCase("survival"))) {
							targetPlayer.setGameMode(GameMode.SURVIVAL);
							targetPlayer.sendMessage(
									sv + "Your gamemode has been updated to " + ChatColor.YELLOW + "Survival.");
							player.sendMessage(sv + ChatColor.YELLOW + targetPlayerName + "'s " + ChatColor.GRAY
									+ "gamemode has been updated to " + ChatColor.YELLOW + "Survival.");
							player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
						} else if ((args[0].equalsIgnoreCase("2")) || (args[0].equalsIgnoreCase("a"))
								|| (args[0].equalsIgnoreCase("adventure"))) {
							targetPlayer.setGameMode(GameMode.ADVENTURE);
							targetPlayer.sendMessage(
									sv + "Your gamemode has been updated to " + ChatColor.LIGHT_PURPLE + "Adventure.");
							player.sendMessage(sv + ChatColor.YELLOW + targetPlayerName + "'s " + ChatColor.GRAY
									+ "gamemode has been updated to " + ChatColor.LIGHT_PURPLE + "Adventure.");
							player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
						} else if ((args[0].equalsIgnoreCase("3")) || (args[0].equalsIgnoreCase("sp"))
								|| (args[0].equalsIgnoreCase("spectator"))) {
							targetPlayer.setGameMode(GameMode.SPECTATOR);
							targetPlayer.sendMessage(
									sv + "Your gamemode has been updated to " + ChatColor.AQUA + "Spectator.");
							player.sendMessage(sv + ChatColor.YELLOW + targetPlayerName + "'s " + ChatColor.GRAY
									+ "gamemode has been updated to " + ChatColor.AQUA + "Spectator.");
							player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
						}
					}
				}
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
			}
		}
		if (CommandLabel.equalsIgnoreCase("heal") || CommandLabel.equalsIgnoreCase("SMDMain:heal")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.heal")) {
				if (args.length == 0) {
					player.setFoodLevel(40);
					for (PotionEffect Effect : player.getActivePotionEffects()) {
						player.removePotionEffect(Effect.getType());
					}
					player.setHealth(20);
					player.setFoodLevel(40);
					player.sendMessage(sv + ChatColor.LIGHT_PURPLE + "You have been healed!");
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("all")) {
						for (Player p : Bukkit.getOnlinePlayers()) {
							for (PotionEffect Effect : p.getActivePotionEffects()) {
								p.removePotionEffect(Effect.getType());
							}
							p.setHealth(20);
							p.setFoodLevel(40);
							p.sendMessage(sv + ChatColor.LIGHT_PURPLE + "You have been healed!");
							p.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
						}
						player.sendMessage(sv + ChatColor.LIGHT_PURPLE + "You healed " + ChatColor.YELLOW
								+ "all online player" + "!");
					} else if (Bukkit.getServer().getPlayer(args[0]) != null) {
						Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
						String targetPlayerName = targetPlayer.getName();
						for (PotionEffect Effect : targetPlayer.getActivePotionEffects()) {
							targetPlayer.removePotionEffect(Effect.getType());
						}
						targetPlayer.setHealth(20);
						targetPlayer.setFoodLevel(40);
						targetPlayer.sendMessage(sv + ChatColor.LIGHT_PURPLE + "You have been healed!");
						targetPlayer.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
						player.sendMessage(sv + ChatColor.LIGHT_PURPLE + "You healed " + ChatColor.YELLOW
								+ targetPlayerName + "!");
					} else {
						player.sendMessage(sv + wrongplayer);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + type + "/heal [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);

			}
		}
		if (CommandLabel.equalsIgnoreCase("fly") || CommandLabel.equalsIgnoreCase("SMDMain:fly")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.fly")) {
				if (args.length == 0) {
					if (player.getAllowFlight() == false) {
						player.setAllowFlight(true);
						player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
						player.sendMessage(sv + "You " + ChatColor.GREEN + "grant " + ChatColor.YELLOW + playerName
								+ "'s ability " + ChatColor.GRAY + "to fly. ");
					} else if (player.getAllowFlight() == true) {
						player.setAllowFlight(false);
						player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 0);
						player.sendMessage(sv + "You " + ChatColor.RED + "revoke " + ChatColor.YELLOW + playerName
								+ "'s ability " + ChatColor.GRAY + "to fly. ");
					}
				} else if (args.length == 1) {
					if (Bukkit.getServer().getPlayer(args[0]) != null) {
						Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
						String targetPlayerName = targetPlayer.getName();
						if (player.getAllowFlight() == false) {
							targetPlayer.setAllowFlight(true);
							player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
							player.sendMessage(sv + "You " + ChatColor.GREEN + "grant " + ChatColor.YELLOW
									+ targetPlayerName + "'s ability " + ChatColor.GRAY + "to fly. ");
						} else if (player.getAllowFlight() == true) {
							targetPlayer.setAllowFlight(false);
							player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 0);
							player.sendMessage(sv + "You " + ChatColor.RED + "revoke " + ChatColor.YELLOW
									+ targetPlayerName + "'s ability " + ChatColor.GRAY + "to fly. ");
						}
					} else {
						player.sendMessage(sv + wrongplayer);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + type + "/fly [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("note")) {
			getConfig().set("", "");
		}
		if (CommandLabel.equalsIgnoreCase("stuck") || CommandLabel.equalsIgnoreCase("SMDMain:stuck")) {
			Location pl = player.getLocation();
			double x = pl.getX();
			double y = (pl.getY() + 0.1);
			double z = pl.getZ();
			double pitch = pl.getPitch();
			double yaw = pl.getYaw();
			World p = pl.getWorld();
			Location loc = new Location(p, x, y, z);
			loc.setPitch((float) pitch);
			loc.setYaw((float) yaw);
			player.teleport(loc);
			player.sendMessage(sv + ChatColor.YELLOW + "You have been resend your location.");
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
		}
		if (CommandLabel.equalsIgnoreCase("day") || CommandLabel.equalsIgnoreCase("SMDMain:day")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.time")) {
				World w = ((Player) sender).getWorld();
				player.sendMessage(sv + "Set time to " + ChatColor.GOLD + "Day " + ChatColor.GRAY + ChatColor.ITALIC
						+ "(1000 ticks)");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(1000);
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("midday") || CommandLabel.equalsIgnoreCase("SMDMain:midday")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.time")) {
				player.sendMessage(sv + "Set time to " + ChatColor.GOLD + "Midday " + ChatColor.GRAY + ChatColor.ITALIC
						+ "(6000 ticks)");
				World w = ((Player) sender).getWorld();
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(6000);
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("night") || CommandLabel.equalsIgnoreCase("SMDMain:night")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.time")) {
				World w = ((Player) sender).getWorld();
				player.sendMessage(sv + "Set time to " + ChatColor.GOLD + "Night " + ChatColor.GRAY + ChatColor.ITALIC
						+ "(13000 ticks)");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(13000);
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("midnight") || CommandLabel.equalsIgnoreCase("SMDMain:midnight")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.time")) {
				World w = ((Player) sender).getWorld();
				player.sendMessage(sv + "Set time to " + ChatColor.GOLD + "Midnight " + ChatColor.GRAY
						+ ChatColor.ITALIC + "(18000 ticks)");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(18000);
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("bc") || CommandLabel.equalsIgnoreCase("SMDMain:bc")
				|| CommandLabel.equalsIgnoreCase("broadcast") || CommandLabel.equalsIgnoreCase("SMDMain:broadcast")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.broadcast")) {
				if (args.length == 0 || args[0].isEmpty()) {
					player.sendMessage(sv + type + "/broadcast [text].");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				} else if (args.length != 0) {
					for (String part : args) {
						if (message != "")
							message += " ";
						message += part;
					}
					message = message.replaceAll("&", "§");
					Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Broadcast> " + ChatColor.WHITE + message);
					for (Player player1 : Bukkit.getOnlinePlayers()) {
						player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					}
				} else {
					player.sendMessage(sv + noperm);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("force") || CommandLabel.equalsIgnoreCase("SMDMain:force")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.force")) {
				if (args.length == 0 || args[0].isEmpty()) {
					player.sendMessage(sv + type + "/force [player] [message].");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				} else if (args.length != 0) {
					if (args[0].equalsIgnoreCase("all")) {
						message = "";
						for (int i = 1; i != args.length; i++)
							message += args[i] + " ";
						message = message.replaceAll("&", "§");
						for (Player p : Bukkit.getOnlinePlayers()) {
							p.chat(message);
						}
						player.sendMessage(sv + "You forced " + ChatColor.YELLOW + "all online player" + ChatColor.GRAY
								+ ": " + ChatColor.AQUA + message);
					} else if (Bukkit.getServer().getPlayer(args[0]) != null) {
						Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
						String targetPlayerName = targetPlayer.getName();
						message = "";
						for (int i = 1; i != args.length; i++)
							message += args[i] + " ";
						message = message.replaceAll("&", "§");
						targetPlayer.chat(message);
						player.sendMessage(sv + "You forced " + ChatColor.YELLOW + targetPlayerName + ChatColor.GRAY
								+ ": " + ChatColor.AQUA + message);
					} else {
						player.sendMessage(sv + wrongplayer);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + noperm);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("bedrock") || CommandLabel.equalsIgnoreCase("SMDMain:bedrock")) {
			ItemStack item = new ItemStack(Material.BEDROCK, 1);
			ItemMeta meta = item.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GREEN + "Right-Click" + ChatColor.YELLOW + " to check at placed location");
			lore.add(ChatColor.GREEN + "Left-Click" + ChatColor.YELLOW + " to check at clicked location");
			meta.setLore(lore);
			meta.setDisplayName(ChatColor.BOLD + "Bedrock");
			item.setItemMeta(meta);
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			player.getInventory().addItem(item);
			player.sendMessage(sv + "Here you are, Use " + ChatColor.YELLOW + "Bedrock" + ChatColor.GRAY
					+ " to check block-logging");
			player.sendMessage(
					sv + ChatColor.GREEN + "Right-Click" + ChatColor.YELLOW + " to check at placed location");
			player.sendMessage(
					sv + ChatColor.GREEN + "Left-Click" + ChatColor.YELLOW + " to check at clicked location");
			player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
		}

		if (CommandLabel.equalsIgnoreCase("ping") || CommandLabel.equalsIgnoreCase("SMDMain:ping")) {
			int ping = ((CraftPlayer) player).getHandle().ping;
			if (args.length == 0) {
				if (ping < 31) {
					ChatColor color = ChatColor.AQUA;
					player.sendMessage(sv + "Your ping is " + color + ping + ChatColor.GRAY + " ms.");
				}
				if (ping > 30 && ping < 81) {
					ChatColor color = ChatColor.GREEN;
					player.sendMessage(sv + "Your ping is " + color + ping + ChatColor.GRAY + " ms.");
				}
				if (ping > 80 && ping < 151) {
					ChatColor color = ChatColor.GOLD;
					player.sendMessage(sv + "Your ping is " + color + ping + ChatColor.GRAY + " ms.");
				}
				if (ping > 150 && ping < 501) {
					ChatColor color = ChatColor.RED;
					player.sendMessage(sv + "Your ping is " + color + ping + ChatColor.GRAY + " ms.");
				}
				if (ping > 500) {
					ChatColor color = ChatColor.DARK_RED;
					player.sendMessage(sv + "Your ping is " + color + ping + ChatColor.GRAY + " ms.");
				}
			} else if (args.length == 1) {
				if (player.getServer().getPlayer(args[0]) != null) {
					Player targetPlayer = player.getServer().getPlayer(args[0]);
					String targetPlayerName = targetPlayer.getName();
					int ping2 = ((CraftPlayer) targetPlayer).getHandle().ping;
					if (ping2 < 31) {
						ChatColor color = ChatColor.AQUA;
						player.sendMessage(sv + ChatColor.YELLOW + targetPlayerName + "'s ping" + ChatColor.GRAY
								+ " is " + color + ping2 + ChatColor.GRAY + " ms.");
					} else if (ping2 > 30 && ping < 81) {
						ChatColor color = ChatColor.GREEN;
						player.sendMessage(sv + ChatColor.YELLOW + targetPlayerName + "'s ping" + ChatColor.GRAY
								+ " is " + color + ping2 + ChatColor.GRAY + " ms.");
					} else if (ping2 > 80 && ping < 151) {
						ChatColor color = ChatColor.GOLD;
						player.sendMessage(sv + ChatColor.YELLOW + targetPlayerName + "'s ping" + ChatColor.GRAY
								+ " is " + color + ping2 + ChatColor.GRAY + " ms.");
					} else if (ping2 > 150 && ping < 501) {
						ChatColor color = ChatColor.RED;
						player.sendMessage(sv + ChatColor.YELLOW + targetPlayerName + "'s ping" + ChatColor.GRAY
								+ " is " + color + ping2 + ChatColor.GRAY + " ms.");
					} else if (ping2 > 500) {
						ChatColor color = ChatColor.DARK_RED;
						player.sendMessage(sv + ChatColor.YELLOW + targetPlayerName + "'s ping" + ChatColor.GRAY
								+ " is " + color + ping2 + ChatColor.GRAY + " ms.");
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "Server>" + ChatColor.GRAY + wrongplayer);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("world") || CommandLabel.equalsIgnoreCase("SMDMain:world")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.world")) {
				double x = player.getLocation().getX();
				double y = player.getLocation().getY();
				double z = player.getLocation().getZ();
				double pitch = player.getLocation().getPitch();
				double yaw = player.getLocation().getYaw();
				if (args.length < 3) {
					World w = Bukkit.getWorld(args[0]);
					if (w != null) {
						if (args.length == 1) {
							Location loc = new Location(w, x, y, z);
							loc.setPitch((float) pitch);
							loc.setYaw((float) yaw);
							player.teleport(loc);
							player.sendMessage(sv + "Sent " + ChatColor.YELLOW + playerName + ChatColor.GRAY
									+ " to world " + ChatColor.AQUA + args[0]);
							player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
						} else if (args.length == 2 && !args[1].isEmpty()) {
							if (Bukkit.getServer().getPlayer(args[1]) != null) {
								Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
								String targetPlayerName = targetPlayer.getName();
								Location loc = new Location(w, x, y, z);
								loc.setPitch((float) pitch);
								loc.setYaw((float) yaw);
								player.teleport(loc);
								player.sendMessage(sv + "Sent " + ChatColor.YELLOW + targetPlayerName + ChatColor.GRAY
										+ " to world " + ChatColor.AQUA + args[0]);
								targetPlayer.sendMessage(sv + "You have been sent to " + ChatColor.GREEN + args[1]
										+ ChatColor.GRAY + " by " + ChatColor.YELLOW + playerName);
								targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
							} else {
								player.sendMessage(sv + wrongplayer);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						} else {
							player.sendMessage(sv + type + "/world [world]");
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
						}
					} else {
						player.sendMessage(sv + "World " + ChatColor.YELLOW + args[1] + ChatColor.GRAY + " not found.");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + type + "/world [world]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("climate") || CommandLabel.equalsIgnoreCase("SMDMain:climate")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.climate")) {
				World w = ((Player) sender).getWorld();
				if (args.length == 0) {
					if (w.hasStorm() == true) {
						w.setThundering(false);
						w.setStorm(false);
						player.sendMessage(sv + "Set weather to " + ChatColor.GOLD + "Sunny");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					} else if (w.hasStorm() == false) {
						w.setThundering(false);
						w.setStorm(true);
						player.sendMessage(sv + "Set weather to " + ChatColor.AQUA + "Rain");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					}
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("sun")) {
						w.setThundering(false);
						w.setStorm(false);
						player.sendMessage(sv + "Set weather to " + ChatColor.GOLD + "Sunny");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					} else if (args[0].equalsIgnoreCase("storm")) {
						w.setThundering(true);
						w.setStorm(true);
						player.sendMessage(sv + "Set weather to " + ChatColor.DARK_AQUA + "Storm");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					} else if (args[0].equalsIgnoreCase("rain")) {
						w.setThundering(false);
						w.setStorm(true);
						player.sendMessage(sv + "Set weather to " + ChatColor.AQUA + "Rain");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					} else {
						player.sendMessage(sv + type + "/climate [sun/storm/rain]");
						player.sendMessage(sv + "Or " + ChatColor.GREEN + "/climate (toggle between sun and rain)");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_SNARE, 1, 1);
					}
				}
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);

			}
		}
		if (CommandLabel.equalsIgnoreCase("tpr") || CommandLabel.equalsIgnoreCase("SMDMain:tpr")
				|| CommandLabel.equalsIgnoreCase("tprequest") || CommandLabel.equalsIgnoreCase("SMDMain:tprequest")) {
			int tprq = playerData.getInt("Quota.TPR");
			if (args.length == 1) {
				if (tprq < 1) {
					player.sendMessage(portalpre + "You don't have enough " + ChatColor.YELLOW + "TPR Quota!");
					player.sendMessage(portalpre + "Use " + ChatColor.AQUA + "/buyquota TPR" + ChatColor.GRAY
							+ " to buy more quota.");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				} else {
					if (Bukkit.getServer().getPlayer(args[0]) != null) {
						Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
						String targetPlayerName = targetPlayer.getName();
						if (targetPlayerName == playerName) {
							player.sendMessage(portalpre + "You can't teleport to yourself!");
						} else if (targetPlayerName != playerName) {
							getConfig().set("Teleport." + targetPlayerName, playerName);
							saveConfig();
							player.sendMessage(portalpre + ChatColor.GREEN + "You sent teleportion request to "
									+ ChatColor.YELLOW + targetPlayerName);
							targetPlayer.sendMessage(portalpre + "Player " + ChatColor.YELLOW + playerName
									+ ChatColor.GRAY + " sent teleportion request to you");
							targetPlayer.sendMessage(portalpre + ChatColor.GREEN + "/tpaccept " + ChatColor.YELLOW
									+ playerName + ChatColor.GRAY + " to" + ChatColor.GREEN + " accept "
									+ ChatColor.GRAY + "teleportion request.");
							targetPlayer.sendMessage(portalpre + ChatColor.RED + "/tpdeny " + ChatColor.YELLOW
									+ playerName + ChatColor.GRAY + " to" + ChatColor.RED + " deny " + ChatColor.GRAY
									+ "teleportion request.");
						}
					} else {
						player.sendMessage(portalpre + wrongplayer);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				}
			} else {
				player.sendMessage(portalpre + type + "/tpr [player]");
			}
		}
		if (CommandLabel.equalsIgnoreCase("tpaccept") || CommandLabel.equalsIgnoreCase("SMDMain:tpaccept")) {
			if (args.length == 1) {
				if (Bukkit.getServer().getPlayer(args[0]) != null) {
					Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
					String targetPlayerName = targetPlayer.getName();
					File userdata1 = new File(
							Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
							File.separator + "PlayerDatabase");
					File f1 = new File(userdata1, File.separator + targetPlayerName + ".yml");
					FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
					int tprq = playerData1.getInt("Quota.TPR");
					if (getConfig().getString("Teleport." + playerName) == (targetPlayerName)) {
						getConfig().set("Teleport." + playerName, "None");
						saveConfig();
						player.sendMessage(portalpre + ChatColor.GREEN + "You accept teleportion request from "
								+ ChatColor.YELLOW + targetPlayerName + ".");
						targetPlayer.sendMessage(portalpre + "Player " + ChatColor.YELLOW + playerName + ChatColor.GREEN
								+ " accept " + ChatColor.GRAY + "your teleportion request.");
						double x = player.getLocation().getX();
						double y = player.getLocation().getY();
						double z = player.getLocation().getZ();
						double yaw = player.getLocation().getYaw();
						double pitch = player.getLocation().getPitch();
						World w = player.getLocation().getWorld();
						Location loc = new Location(w, x, y, z);
						loc.setPitch((float) pitch);
						loc.setYaw((float) yaw);
						targetPlayer.teleport(loc);
						try {
							playerData1.set("Quota.TPR", tprq - 1);
							playerData1.save(f1);
						} catch (IOException e) {
							e.printStackTrace();
						}
						int tprq2 = playerData1.getInt("Quota.TPR");
						targetPlayer
								.sendMessage(portalpre + "You have " + ChatColor.YELLOW + tprq2 + " TPR Quota left.");
					} else if (getConfig().getString("Teleport." + targetPlayerName) == ("None")) {
						player.sendMessage(portalpre + "You didn't have any request from anyone");
					} else {
						player.sendMessage(portalpre + "You don't have any teleportion request from " + ChatColor.YELLOW
								+ targetPlayerName + ".");
					}
				} else {
					player.sendMessage(portalpre + wrongplayer);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(portalpre + type + "/tpaccept [player]");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("tpdeny") || CommandLabel.equalsIgnoreCase("SMDMain:tpdeny")) {
			if (args.length == 1) {
				if (Bukkit.getServer().getPlayer(args[0]) != null) {
					Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
					String targetPlayerName = targetPlayer.getName();
					if (getConfig().getString("Teleport." + playerName) == (targetPlayerName)) {
						getConfig().set("Teleport." + playerName, "None");
						saveConfig();
						player.sendMessage(portalpre + ChatColor.RED + "You deny teleportion request from "
								+ ChatColor.YELLOW + targetPlayerName + ".");
						targetPlayer.sendMessage(portalpre + "Player " + ChatColor.YELLOW + playerName + ChatColor.RED
								+ " deny " + ChatColor.GRAY + "your teleportion request.");
					} else if (getConfig().getString("Teleport." + targetPlayerName).equalsIgnoreCase("None")) {
						player.sendMessage(portalpre + "You didn't have any request from anyone");
					} else {
						player.sendMessage(portalpre + "You don't have any teleportion request from " + ChatColor.YELLOW
								+ targetPlayerName + ".");
					}
				} else {
					player.sendMessage(portalpre + wrongplayer);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(portalpre + "Type: " + ChatColor.GREEN + "/tpdeny [player]");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("platewarp")) {
			Location loc = player.getLocation();
			loc.setY(loc.getY());
			Location locs = player.getLocation();
			locs.setY(loc.getY() - 2);
			Block block = loc.getBlock();
			Block blocks = locs.getBlock();
			String warping = playerData.getString("warping");
			if (block.getType() == Material.GOLD_PLATE || block.getType() == Material.IRON_PLATE) {
				if (blocks.getType() == Material.SIGN_POST || blocks.getType() == Material.WALL_SIGN) {
					Sign sign = (Sign) blocks.getState();
					if (sign.getLine(0).equalsIgnoreCase("[tp]")) {
						if (warping.equalsIgnoreCase("warp1")) {
							ActionBar a = new ActionBar(ChatColor.BLUE + "" + ChatColor.BOLD + "Teleport Charger: "
									+ ChatColor.GOLD + "▃ " + ChatColor.GRAY + "▄ ▅ ▆ ▇");
							a.sendToPlayer(player);
							player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, (float) 0.3
									);
							player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
							player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
							player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
							player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
							player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
							player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
							try {
								playerData.set("warping", "warp2");
								playerData.save(f);
							} catch (IOException e) {
								e.printStackTrace();
							}
							getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
								@Override
								public void run() {
									player.performCommand("platewarp");
								}
							}, 10);
						} else if (warping.equalsIgnoreCase("warp2")) {
							if (player.isSneaking() == false) {
								try {
									playerData.set("warping", "false");
									playerData.save(f);
									ActionBar a = new ActionBar(ChatColor.GREEN + "" + ChatColor.BOLD
											+ "Your teleport task has been canceled!");
									a.sendToPlayer(player);
									player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								ActionBar a = new ActionBar(ChatColor.BLUE + "" + ChatColor.BOLD + "Teleport Charger: "
										+ ChatColor.GOLD + "▃ ▄ " + ChatColor.GRAY + "▅ ▆ ▇");
								a.sendToPlayer(player);
								player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, (float) 0.5);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								try {
									playerData.set("warping", "warp3");
									playerData.save(f);
								} catch (IOException e) {
									e.printStackTrace();
								}
								getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
									@Override
									public void run() {
										player.performCommand("platewarp");
									}
								}, 10);
							}
						} else if (warping.equalsIgnoreCase("warp3")) {
							if (player.isSneaking() == false) {
								try {
									playerData.set("warping", "false");
									playerData.save(f);
									ActionBar a = new ActionBar(ChatColor.GREEN + "" + ChatColor.BOLD
											+ "Your teleport task has been canceled!");
									a.sendToPlayer(player);
									player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								ActionBar a = new ActionBar(ChatColor.BLUE + "" + ChatColor.BOLD + "Teleport Charger: "
										+ ChatColor.GOLD + "▃ ▄ ▅ " + ChatColor.GRAY + "▆ ▇");
								a.sendToPlayer(player);
								player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, (float) 0.7);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								try {
									playerData.set("warping", "warp4");
									playerData.save(f);
								} catch (IOException e) {
									e.printStackTrace();
								}
								getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
									@Override
									public void run() {
										player.performCommand("platewarp");
									}
								}, 10);
							}
						} else if (warping.equalsIgnoreCase("warp4")) {
							if (player.isSneaking() == false) {
								try {
									playerData.set("warping", "false");
									playerData.save(f);
									ActionBar a = new ActionBar(ChatColor.GREEN + "" + ChatColor.BOLD
											+ "Your teleport task has been canceled!");
									a.sendToPlayer(player);
									player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								ActionBar a = new ActionBar(ChatColor.BLUE + "" + ChatColor.BOLD + "Teleport Charger: "
										+ ChatColor.GOLD + "▃ ▄ ▅ ▆ " + ChatColor.GRAY + "▇");
								a.sendToPlayer(player);
								player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, (float) 0.9);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								try {
									playerData.set("warping", "warp5");
									playerData.save(f);
								} catch (IOException e) {
									e.printStackTrace();
								}
								getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
									@Override
									public void run() {
										player.performCommand("platewarp");
									}
								}, 10);
							}
						} else if (warping.equalsIgnoreCase("warp5")) {
							if (player.isSneaking() == false) {
								try {
									playerData.set("warping", "false");
									playerData.save(f);
									ActionBar a = new ActionBar(ChatColor.GREEN + "" + ChatColor.BOLD
											+ "Your teleport task has been canceled!");
									a.sendToPlayer(player);
									player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								ActionBar a = new ActionBar(ChatColor.BLUE + "" + ChatColor.BOLD + "Teleport Charger: "
										+ ChatColor.GOLD + "▃ ▄ ▅ ▆ ▇");
								a.sendToPlayer(player);
								player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, (float) 1.2);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								try {
									playerData.set("warping", "warpteleport");
									playerData.save(f);
								} catch (IOException e) {
									e.printStackTrace();
								}
								getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
									@Override
									public void run() {
										player.performCommand("platewarp");
									}
								}, 15);
							}
						} else if (warping.equalsIgnoreCase("warpteleport")) {
							if (block.getType() == Material.GOLD_PLATE || block.getType() == Material.IRON_PLATE) {
								Location loc2 = player.getLocation();
								loc2.setY(loc.getY() - 2);
								Block block2 = loc2.getBlock();
								if ((block2.getType() == Material.SIGN_POST
										|| block2.getType() == Material.WALL_SIGN)) {
									Location loc3 = player.getLocation();
									loc3.setY(loc.getY() - 3);
									Block block3 = loc3.getBlock();
									Sign sign1 = (Sign) block2.getState();
									Sign sign2 = (Sign) block3.getState();
									if (sign1.getLine(0).equalsIgnoreCase("[tp]")
											&& sign2.getLine(0).equalsIgnoreCase("[world]")) {
										if (block3.getType() == Material.SIGN_POST
												|| block3.getType() == Material.WALL_SIGN) {
											World world = Bukkit
													.getWorld(sign2.getLine(1) + sign2.getLine(2) + sign2.getLine(3));
											if (world != null) {
												Location pl = player.getLocation();
												double xh = Integer.parseInt(sign1.getLine(1));
												double yh = Integer.parseInt(sign1.getLine(2));
												double zh = Integer.parseInt(sign1.getLine(3));
												double x = xh + 0.5;
												double y = yh;
												double z = zh + 0.5;
												double yaw = pl.getYaw();
												double pitch = pl.getPitch();
												Location loca = new Location(world, x, y, z);
												loca.setPitch((float) pitch);
												loca.setYaw((float) yaw);
												player.teleport(loca);
											} else {
												Location pl = player.getLocation();
												double xh = Integer.parseInt(sign1.getLine(1));
												double yh = Integer.parseInt(sign1.getLine(2));
												double zh = Integer.parseInt(sign1.getLine(3));
												double x = xh + 0.5;
												double y = yh;
												double z = zh + 0.5;
												double yaw = pl.getYaw();
												double pitch = pl.getPitch();
												World world2 = pl.getWorld();
												Location loca = new Location(world2, x, y, z);
												loca.setPitch((float) pitch);
												loca.setYaw((float) yaw);
												player.teleport(loca);
											}
											player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 2);

										} else {
										}
									} else {
									}
								} else {
								}
							} else {
							}
							ActionBar a = new ActionBar(ChatColor.GREEN + "" + ChatColor.BOLD + "Teleport!");
							a.sendToPlayer(player);
							try {
								playerData.set("warping", "false");
								playerData.save(f);
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					}
				}
			} else {
				try {
					playerData.set("warping", "false");
					playerData.save(f);
					ActionBar a = new ActionBar(
							ChatColor.GREEN + "" + ChatColor.BOLD + "Your teleport task has been canceled!");
					a.sendToPlayer(player);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
		if (CommandLabel.equalsIgnoreCase("mute")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.mute")) {
				if (args.length > 1) {
					if (Bukkit.getServer().getPlayer(args[0]) != null) {
						Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
						String targetPlayerName = targetPlayer.getName();
						File userdata1 = new File(
								Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
								File.separator + "PlayerDatabase");
						File f1 = new File(userdata1, File.separator + targetPlayerName + ".yml");
						FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
						String muteis = playerData1.getString("mute.is");
						if (args[0].equalsIgnoreCase("SMD_SSG_PJ")) {
							player.sendMessage(sv + ChatColor.RED + "Nope. You can't mute SMD_SSG_PJ!");
							player.playSound(player.getLocation(), Sound.ENTITY_HORSE_DEATH, 1, 1);
						} else {
							if (muteis.equalsIgnoreCase("false")) {
								message = "";
								for (int i = 1; i != args.length; i++)
									message += args[i] + " ";
								message = message.replaceAll("&", "§");
								Bukkit.broadcastMessage(ChatColor.BLUE + "Chat> " + ChatColor.GRAY + "Player "
										+ ChatColor.YELLOW + playerName + ChatColor.RED + " revoke " + ChatColor.YELLOW
										+ targetPlayerName + "'s ability " + ChatColor.GRAY + "to chat. ");
								Bukkit.broadcastMessage(ChatColor.BLUE + "Chat> " + ChatColor.GRAY + "Reason: "
										+ ChatColor.YELLOW + message);
								targetPlayer.sendMessage(sv + "You have been muted.");
								targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,
										1, 1);
								try {
									playerData1.set("mute.is", "true");
									playerData1.set("mute.reason", message);
									playerData1.save(f1);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							if (muteis.equalsIgnoreCase("true")) {
								Bukkit.broadcastMessage(ChatColor.BLUE + "Chat> " + ChatColor.GRAY + "Player "
										+ ChatColor.YELLOW + playerName + ChatColor.GREEN + " grant " + ChatColor.YELLOW
										+ targetPlayerName + "'s ability " + ChatColor.GRAY + "to chat. ");
								player.sendMessage(sv + "You " + ChatColor.GREEN + "grant " + ChatColor.YELLOW
										+ targetPlayerName + "'s ability " + ChatColor.GRAY + "to chat. ");
								targetPlayer.sendMessage(sv + "You have been unmuted.");
								targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,
										1, 1);
								try {
									playerData1.set("mute.is", "false");
									playerData1.set("mute.reason", "none");
									playerData1.save(f1);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}

					} else {
						player.sendMessage(sv + wrongplayer);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + type + "/mute [player] [reason]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("warn")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.warn")) {
				if (args.length > 1) {
					if (args[0].equalsIgnoreCase("SMD_SSG_PJ")) {
						player.sendMessage(sv + ChatColor.RED + "Nope. You can't warn SMD_SSG_PJ!");
						player.playSound(player.getLocation(), Sound.ENTITY_HORSE_DEATH, 1, 1);
					} else {
						if (Bukkit.getServer().getPlayer(args[0]) != null) {
							Player targetPlayer = player.getServer().getPlayer(args[0]);
							String targetPlayerName = targetPlayer.getName();
							File userdata1 = new File(
									Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
									File.separator + "PlayerDatabase");
							File f1 = new File(userdata1, File.separator + targetPlayerName + ".yml");
							FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
							int countwarn = playerData1.getInt("warn");
							message = "";
							for (int i = 1; i != args.length; i++)
								message += args[i] + " ";
							message = message.replaceAll("&", "§");
							int countnew = countwarn + 1;
							if (countnew == 4) {
								countnew = 3;
								Bukkit.broadcastMessage(sv + targetPlayerName + " has been banned");
								Bukkit.broadcastMessage(sv + "Reason: " + ChatColor.YELLOW + message);
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
										"ban " + targetPlayerName + " " + message);
							} else {
								Bukkit.broadcastMessage(sv + targetPlayerName + " has been warned (" + countnew + ")");
								Bukkit.broadcastMessage(sv + "Reason: " + ChatColor.YELLOW + message);
							}
							try {
								playerData1.set("warn", countnew);
								playerData1.save(f1);
							} catch (IOException e) {
								e.printStackTrace();
							}
							for (Player p : Bukkit.getOnlinePlayers()) {
								p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
							}
						} else {
							player.sendMessage(sv + wrongplayer);
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
						}
					}
				} else {
					player.sendMessage(sv + type + "/warn [player] [reason]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("resetwarn")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.warn")) {
				if (args.length == 1) {
					if (Bukkit.getServer().getPlayer(args[0]) != null) {
						Player targetPlayer = player.getServer().getPlayer(args[0]);
						String targetPlayerName = targetPlayer.getName();
						File userdata1 = new File(
								Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
								File.separator + "PlayerDatabase");
						File f1 = new File(userdata1, File.separator + targetPlayerName + ".yml");
						FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
						message = "";
						for (int i = 1; i != args.length; i++) // i = argrement
																// if start
																// catch args[0]
																// type i = 0
							message += args[i] + " ";
						message = message.replaceAll("&", "§");
						Bukkit.broadcastMessage(sv + ChatColor.YELLOW + playerName + ChatColor.GRAY + " reset "
								+ targetPlayerName + "'s warned (0)");
						try {
							playerData1.set("warn", 0);
							playerData1.save(f1);
						} catch (IOException e) {
							e.printStackTrace();
						}
						for (Player p : Bukkit.getOnlinePlayers()) {
							p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
						}
					} else {
						player.sendMessage(sv + wrongplayer);
					}
				} else {
					player.sendMessage(sv + type + "/resetwarn [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("buyquota") || CommandLabel.equalsIgnoreCase("SMDMain:buyquota")) {
			double money = playerData.getDouble("money");
			int tprq = playerData.getInt("Quota.TPR");
			int lcq = playerData.getInt("Quota.LuckyClick");
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("tpr")) {
					if (money > 3000) {
						try {
							playerData.set("Quota.TPR", tprq + 15);
							playerData.set("money", money - 3000);
							playerData.save(f);
						} catch (IOException e) {
							e.printStackTrace();
						}
						player.sendMessage(sv + "You " + ChatColor.YELLOW + "paid 3000 Coins" + ChatColor.GRAY
								+ " to brought " + ChatColor.GREEN + "15x TPR Quota");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
					} else {
						player.sendMessage(sv + nom);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else if (args[0].equalsIgnoreCase("luckyclick")) {
					if (money > 1500) {
						try {
							playerData.set("Quota.LuckyClick", lcq + 3);
							playerData.set("money", money - 1500);
							playerData.save(f);
						} catch (IOException e) {
							e.printStackTrace();
						}
						player.sendMessage(sv + "You " + ChatColor.YELLOW + "paid 1500 Coins" + ChatColor.GRAY
								+ " to brought " + ChatColor.LIGHT_PURPLE + "3x LuckyClick Quota");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
					} else {
						player.sendMessage(sv + nom);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + type + "/buyquota [tpr/luckyclick]");
					player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
				}
			} else {
				player.sendMessage(sv + "Welcome to " + ChatColor.YELLOW + ChatColor.BOLD + "Quota's Shop");
				player.sendMessage(ChatColor.GREEN + "Pricing List " + ChatColor.WHITE + ":");
				player.sendMessage("- " + ChatColor.GREEN + "15x TPR Quota" + ChatColor.YELLOW + " 3000 Coin");
				player.sendMessage(
						"- " + ChatColor.LIGHT_PURPLE + "3x Lucky Click Quota" + ChatColor.YELLOW + " 1500 Coin");
				player.sendMessage(type + "/buyquota [tpr/luckyclick]");
				player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
			}

		}
		if (CommandLabel.equalsIgnoreCase("rank")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.rank")) {
				if (args.length == 2) {
					if (Bukkit.getServer().getPlayer(args[1]) != null) {
						Player targetPlayer = player.getServer().getPlayer(args[1]);
						String targetPlayerName = targetPlayer.getName();
						File userdata1 = new File(
								Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
								File.separator + "PlayerDatabase");
						File f1 = new File(userdata1, File.separator + targetPlayerName + ".yml");
						FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
						if (args[0].equalsIgnoreCase("staff")) {
							Bukkit.broadcastMessage(ChatColor.BLUE + "Rank> " + ChatColor.GRAY + "Player "
									+ ChatColor.YELLOW + targetPlayerName + ChatColor.GRAY
									+ "'s rank has been updated to " + ChatColor.DARK_BLUE + ChatColor.BOLD + "Staff");
							targetPlayer.setPlayerListName(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Staff"
									+ ChatColor.BLUE + targetPlayerName);
							targetPlayer.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Staff"
									+ ChatColor.BLUE + targetPlayerName);
							try {
								playerData1.set("rank", "staff");
								playerData1.save(f1);
							} catch (IOException e) {
								e.printStackTrace();
							}
							for (Player p : Bukkit.getOnlinePlayers()) {
								p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
							}
						} else if (args[0].equalsIgnoreCase("default")) {
							Bukkit.broadcastMessage(ChatColor.BLUE + "Rank> " + ChatColor.GRAY + "Player "
									+ ChatColor.YELLOW + targetPlayerName + ChatColor.GRAY
									+ "'s rank has been updated to " + ChatColor.BLUE + ChatColor.BOLD + "Default");
							targetPlayer.setPlayerListName(ChatColor.BLUE + targetPlayerName);
							targetPlayer.setDisplayName(ChatColor.BLUE + targetPlayerName);
							try {
								playerData1.set("rank", "default");
								playerData1.save(f1);
							} catch (IOException e) {
								e.printStackTrace();
							}
							for (Player p : Bukkit.getOnlinePlayers()) {
								p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
							}
						} else if (args[0].equalsIgnoreCase("vip")) {
							Bukkit.broadcastMessage(ChatColor.BLUE + "Rank> " + ChatColor.GRAY + "Player "
									+ ChatColor.YELLOW + targetPlayerName + ChatColor.GRAY
									+ "'s rank has been updated to " + ChatColor.GREEN + ChatColor.BOLD + "VIP");
							targetPlayer.setPlayerListName(ChatColor.GREEN + "" + ChatColor.BOLD + "VIP"
									+ ChatColor.DARK_GREEN + targetPlayerName);
							targetPlayer.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "VIP"
									+ ChatColor.DARK_GREEN + targetPlayerName);
							try {
								playerData1.set("rank", "vip");
								playerData1.save(f1);
							} catch (IOException e) {
								e.printStackTrace();
							}
							for (Player p : Bukkit.getOnlinePlayers()) {
								p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
							}
						} else if (args[0].equalsIgnoreCase("mvp")) {
							Bukkit.broadcastMessage(ChatColor.BLUE + "Rank> " + ChatColor.GRAY + "Player "
									+ ChatColor.YELLOW + targetPlayerName + ChatColor.GRAY
									+ "'s rank has been updated to " + ChatColor.AQUA + ChatColor.BOLD + "MVP");
							targetPlayer.setPlayerListName(ChatColor.AQUA + "" + ChatColor.BOLD + "MVP"
									+ ChatColor.DARK_AQUA + targetPlayerName);
							targetPlayer.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "MVP"
									+ ChatColor.DARK_AQUA + targetPlayerName);
							try {
								playerData1.set("rank", "mvp");
								playerData1.save(f1);
							} catch (IOException e) {
								e.printStackTrace();
							}
							for (Player p : Bukkit.getOnlinePlayers()) {
								p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
							}
						} else if (args[0].equalsIgnoreCase("admin")) {
							Bukkit.broadcastMessage(ChatColor.BLUE + "Rank> " + ChatColor.GRAY + "Player "
									+ ChatColor.YELLOW + targetPlayerName + ChatColor.GRAY + "'s rank been updated to "
									+ ChatColor.DARK_RED + ChatColor.BOLD + "Admin");
							targetPlayer.setPlayerListName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Admin"
									+ ChatColor.RED + targetPlayerName);
							targetPlayer.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Admin"
									+ ChatColor.RED + targetPlayerName);
							try {
								playerData1.set("rank", "admin");
								playerData1.save(f1);
							} catch (IOException e) {
								e.printStackTrace();
							}
							for (Player p : Bukkit.getOnlinePlayers()) {
								p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
							}

						} else if (args[0].equalsIgnoreCase("owner")) {
							Bukkit.broadcastMessage(ChatColor.BLUE + "Rank> " + ChatColor.GRAY + "Player "
									+ ChatColor.YELLOW + targetPlayerName + ChatColor.GRAY + "'s rank been updated to "
									+ ChatColor.GOLD + ChatColor.BOLD + "Owner");
							targetPlayer.setPlayerListName(ChatColor.GOLD + "" + ChatColor.BOLD + "Owner"
									+ ChatColor.YELLOW + targetPlayerName);
							targetPlayer.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Owner"
									+ ChatColor.YELLOW + targetPlayerName);
							try {
								playerData1.set("rank", "owner");
								playerData1.save(f1);
							} catch (IOException e) {
								e.printStackTrace();
							}
							for (Player p : Bukkit.getOnlinePlayers()) {
								p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 0);
							}

						} else {
							player.sendMessage(ChatColor.BLUE + "Rank> " + type
									+ "/rank [default|vip|mvp|staff|admin|owner] [player]");
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
						}
					} else {
						player.sendMessage(sv + ChatColor.RED + wrongplayer);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(
							ChatColor.BLUE + "Rank> " + type + "/rank [default|vip|mvp|staff|admin|owner] [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("status") || CommandLabel.equalsIgnoreCase("SMDMain:status")) {
			player.sendMessage(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "----[" + ChatColor.WHITE + "STATS"
					+ ChatColor.YELLOW + ChatColor.STRIKETHROUGH + "]----");
			player.sendMessage("Mute: ");
			String muteis = playerData.getString("mute.is");
			String mutere = playerData.getString("mute.reason");
			String freeze = playerData.getString("freeze");
			int countwarn = playerData.getInt("warn");
			int tprq = playerData.getInt("Quota.TPR");
			int lcq = playerData.getInt("Quota.LuckyClick");
			if (muteis.equalsIgnoreCase("true")) {
				player.sendMessage("  Status: " + ChatColor.RED + "TRUE");
				player.sendMessage("  Reason: " + ChatColor.YELLOW + mutere);
			}
			if (muteis.equalsIgnoreCase("false")) {
				player.sendMessage("  Status: " + ChatColor.GREEN + "FALSE");
				player.sendMessage("  Reason: " + ChatColor.GRAY + "You didn't get mute.");
			}
			player.sendMessage("Warn: " + ChatColor.YELLOW + countwarn + "/3 Left.");
			player.sendMessage("Rank: " + ChatColor.YELLOW + rank.toUpperCase());
			if (freeze.equalsIgnoreCase("true")) {
				player.sendMessage("Freeze: " + ChatColor.RED + "TRUE");
			}
			if (freeze.equalsIgnoreCase("false")) {
				player.sendMessage("Freeze: " + ChatColor.GREEN + "FALSE");
			}
			player.sendMessage("Quota");
			player.sendMessage("  - TPR: " + ChatColor.GREEN + tprq);
			player.sendMessage("  - LuckyClick: " + ChatColor.GREEN + lcq);
		}
		if (CommandLabel.equalsIgnoreCase("wiki") || CommandLabel.equalsIgnoreCase("SMDMain:wiki")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("rule")) {
					player.sendMessage(ChatColor.GREEN + "Topic: " + ChatColor.YELLOW + "RULE");
					player.sendMessage("1) ห้ามใช้บัคของใด ๆ ให้เป็นประโยชน์ส่วนบุคคล (เช่น ต่อตัว, แหก Private ฯลฯ)");
					player.sendMessage("  1.1) กรณีเป็นบัคของ Minecraft อยู่แล้ว ไม่นับเป็นความผิด");
					player.sendMessage("  1.2) กรณีที่ทุกคน ได้ประโยชน์ร่วมกัน (สาธารณะ) จะไม่นับเป็นความผิด");
					player.sendMessage("  1.3) หากพบเห็นบัคของ 'Plugin' ให้แจ้งที่ SMD_SSG_PJ จะมีรางวัลให้ด้วย");
					player.sendMessage(
							"2) ในการเตือนความผิดใด ๆ แก่ผู้เล่น ควรสอบถามความเป็นมาก่อน ว่าเกิดเหตุการณ์อะไรขึ้น ทำไมถึงทำแบบนั้น ฯลฯ ก่อนที่จะ Warn ทุกครั้ง หากมีเหตุผลที่ไม่เหมาะสมจะมีการพิจารณา");
					player.sendMessage(
							"3) ห้าม Mod ที่ทำให้มีความสามารถพิเศษเหนือกว่าผู้อื่น เช่น Kill Aura, XRay ส่วนพวก LightMap ถือว่าอนุโลมเนื่องจากไม่ทำให้ผู้เล่นอื่นเสียเปรียบมาก");
					player.sendMessage(
							"4) ห้ามขโมยของ - ทำลายสิ่งก่อสร้างของผู้อื่นก่อนได้รับอนุญาตจากเจ้าของ 'ยกเว้น' กรณีของกล่องสาธารณะ หากมีการระบุไว้ 'ชัดเจน' เจ้าของจะไม่สามารถแจ้งความผิดใด ๆ ได้");

				} else if (args[0].equalsIgnoreCase("warn")) {
					player.sendMessage(ChatColor.GREEN + "Topic: " + ChatColor.YELLOW + "WARN");
					player.sendMessage("Q: การ Warn คืออะไร?");
					player.sendMessage(
							"A: การ Warn คือการเตือนผู้เล่นเมื่อผู้เล่นกระทำผิดจากกฏที่ได้กำหนดไว้ (/wiki rule)");
					player.sendMessage("");
					player.sendMessage("Q: ถ้าโดน Warn เยอะ ๆ แล้วจะเกิดอะไรขึ้น?");
					player.sendMessage(
							"A: ทางเราจะให้โอกาสทั้งหมด 3 ครั้ง หากมีครั้งที่ 4 จะถือเป็นการโดนแบน โดยผู้เล่นสามารถปลด Ban ได้ในราคา 50 TrueWallet (ติดต่อ SMD_SSG_PJ) ได้ไม่เกิน 2 ครั้ง หากถูก Ban ครบ 3 ครั้งจะไม่มีสิทธิ์ในการปลดแบนอีกต่อไป");
					player.sendMessage("");
					player.sendMessage("Q: ผมถูก Warn โดยที่ไม่ได้กระทำผิด ต้องทำไงอะครับ?");
					player.sendMessage(
							"A: ให้ผู้ที่ถูก Warn แคปเหตุผลที่โดน Warn (Reason) แล้วติดต่อมายัง SMD_SSG_PJ ครับ โดยทางเราจะตรวจสอบความเป็นมาอีกครั้งและพิจารณาใหม่อีกครั้งครับ");
				} else if (args[0].equalsIgnoreCase("luckyclick")) {
					player.sendMessage(ChatColor.GREEN + "Topic: " + ChatColor.YELLOW + "LUCKYCLICK");
					player.sendMessage("LuckyClick คืออะไร");
					player.sendMessage(
							"LuckyClick เป็นระบบสุ่มของจากป้าย โดยมีทั้งของดีและของไม่ดี สามารถสุ่มได้ที่ป้าย [LuckyClick] ที่สปาว (ต้องมี Quota ก่อน สามารถซื้อได้โดยใช้คำสั่ง /buyquota luckyclick ในราคา 1500 Coin(s) ต่อ 3 LuckyClick Quota");
				} else {
					player.sendMessage(ChatColor.BLUE + "Wiki> " + ChatColor.GRAY + "Topic " + ChatColor.YELLOW
							+ args[0] + ChatColor.GRAY + " not found!");
				}
			} else {
				player.sendMessage(ChatColor.BLUE + "Wiki> " + ChatColor.GRAY + "Welcome to " + ChatColor.GREEN
						+ ChatColor.BOLD + "WIKI - The Information center");
				player.sendMessage(ChatColor.BLUE + "Wiki> " + ChatColor.GREEN + "Available Topic: " + ChatColor.YELLOW
						+ "Rule, Warn, LuckyClick");
				player.sendMessage(ChatColor.BLUE + "Wiki> " + ChatColor.GRAY + "Please choose your topic by type: "
						+ ChatColor.YELLOW + "/wiki [topic]");
				player.sendMessage(ChatColor.RED + "ADS> " + ChatColor.WHITE + "Wiki's Writter Wanted! Contact "
						+ ChatColor.LIGHT_PURPLE + "@SMD_SSG_PJ");
			}
		}
		if (CommandLabel.equalsIgnoreCase("call") || CommandLabel.equalsIgnoreCase("SMDMain:call")) {
			if (args.length == 1) {
				String UUID = player.getUniqueId().toString();
				String targetPlayer = args[0];
				File userdata1 = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
						File.separator + "PlayerDatabase");
				File f1 = new File(userdata1, File.separator + targetPlayer + ".yml");
				FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
				if (!f1.exists()) {
					player.sendMessage(sv + "That account not found.");
				}
				if (f1.exists()) {
					player.sendMessage(sv + "Account found");
					if (UUID.equalsIgnoreCase(playerData1.getString("uuid"))) {
						try {
							String rank1 = playerData1.getString("rank");
							int warn1 = playerData1.getInt("warn");
							String muteis = playerData1.getString("mute.is");
							String mutere = playerData1.getString("mute.reason");
							String freeze = playerData1.getString("freeze");
							int money = playerData1.getInt("money");
							int tprq = playerData1.getInt("Quota.TPR");
							int lcq = playerData1.getInt("Quota.LuckyClick");
							if (playerData1.getString("home") != null) {
								double x = playerData1.getDouble("home.x");
								double y = playerData1.getDouble("home.y");
								double z = playerData1.getDouble("home.z");
								double pitch = playerData1.getDouble("home.pitch");
								double yaw = playerData1.getDouble("home.yaw");
								String world = playerData1.getString("home.world");
								playerData.createSection("rank");
								playerData.set("rank", rank1);
								playerData.createSection("warn");
								playerData.set("warn", warn1);
								playerData.createSection("mute");
								playerData.set("mute.is", muteis);
								playerData.set("mute.reason", mutere);
								playerData.createSection("freeze");
								playerData.set("freeze", freeze);
								playerData.createSection("home");
								playerData.set("home.x", x);
								playerData.set("home.y", y);
								playerData.set("home.z", z);
								playerData.set("home.pitch", pitch);
								playerData.set("home.yaw", yaw);
								playerData.set("home.world", world);
								playerData.set("money", money);
								playerData.createSection("Quota");
								playerData.set("Quota.TPR", tprq);
								playerData.set("Quota.LuckyClick", lcq);
							} else {
								playerData.createSection("rank");
								playerData.set("rank", rank1);
								playerData.createSection("warn");
								playerData.set("warn", warn1);
								playerData.createSection("mute");
								playerData.set("mute.is", muteis);
								playerData.set("mute.reason", mutere);
								playerData.createSection("freeze");
								playerData.set("freeze", freeze);
								playerData.set("money", money);
								playerData.createSection("Quota");
								playerData.set("Quota.TPR", tprq);
								playerData.set("Quota.LuckyClick", lcq);
							}
							// playerData1.set("home", null);
							playerData.save(f);
						} catch (IOException e) {
							e.printStackTrace();
						}
						player.sendMessage(sv + ChatColor.GREEN + "You're same UUID with that account.");
						player.sendMessage(
								sv + ChatColor.GREEN + "Move data from old account to this account complete.");
						player.sendMessage(sv + ChatColor.GREEN + "Please re-join one time to make system save data.");
						player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					} else {
						player.sendMessage(sv + ChatColor.RED + "You're not same UUID with that account!");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				}
			} else {
				player.sendMessage(sv + type + "/call [old Name] (Lower and Upper have effect.)");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);

			}
		}
		if (CommandLabel.equalsIgnoreCase("givequota")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.givequota")) {
				if (args.length == 3) {
					String UUID = player.getUniqueId().toString();
					Player targetPlayer = player.getServer().getPlayer(args[0]);
					String targetPlayerName = targetPlayer.getName();
					File userdata1 = new File(
							Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
							File.separator + "PlayerDatabase");
					File f1 = new File(userdata1, File.separator + targetPlayerName + ".yml");
					FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
					int tprq = playerData1.getInt("Quota.TPR");
					int lcq = playerData1.getInt("Quota.LuckyClick");
					if (Bukkit.getServer().getPlayer(args[0]) != null) {
						if (args[1].equalsIgnoreCase("tpr")) {
							if (isInt(args[2])) {
								int tprqn = tprq + Integer.parseInt(args[2]);
								try {
									playerData1.set("Quota.TPR", tprqn);
									playerData1.save(f1);
								} catch (IOException e) {
									e.printStackTrace();
								}
								player.sendMessage(sv + "You gave " + ChatColor.AQUA + args[2] + "x TPR Quota to "
										+ ChatColor.YELLOW + targetPlayerName);
							} else {
								player.sendMessage(sv + ChatColor.YELLOW + args[2] + ChatColor.GRAY + " is not number");
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						} else if (args[1].equalsIgnoreCase("luckyclick")) {
							if (isInt(args[2])) {
								int lcqn = tprq + Integer.parseInt(args[2]);
								try {
									playerData1.set("Quota.LuckyClick", lcqn);
									playerData1.save(f1);
								} catch (IOException e) {
									e.printStackTrace();
								}
								player.sendMessage(sv + "You gave " + ChatColor.LIGHT_PURPLE + args[2]
										+ "x LuckyClick Quota to " + ChatColor.YELLOW + targetPlayerName);
							} else {
								player.sendMessage(sv + ChatColor.YELLOW + args[2] + ChatColor.GRAY + " is not number");
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						} else {
							player.sendMessage(sv + type + "/givequota [player] [type] [amount]");
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
						}
					} else {
						player.sendMessage(sv + wrongplayer);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + type + "/givequota [player] [type] [amount]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("adminchat") || CommandLabel.equalsIgnoreCase("ac")
				|| CommandLabel.equalsIgnoreCase("SMDMain:ac") || CommandLabel.equalsIgnoreCase("SMDMain:adminchat")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.adminchat")) {
				if (args.length != 0) {
					for (String part : args) {
						if (message != "")
							message += " ";
						message += part;
					}
					message = message.replaceAll("&", "§");
					for (Player p : Bukkit.getOnlinePlayers()) {
						if (p.isOp() || p.hasPermission("main.*") || p.hasPermission("main.adminchat")) {
							p.sendMessage(ChatColor.RED + "AdminChat> " + player.getDisplayName() + " "
									+ ChatColor.WHITE + message);
							p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
						} else {

						}
					}
				} else {
					player.sendMessage(sv + type + "/adminchat [message]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("freeze") || CommandLabel.equalsIgnoreCase("SMDMain:freeze")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.freeze")) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("SMD_SSG_PJ")) {
						player.sendMessage(sv + ChatColor.RED + "Nope. You can't freeze SMD_SSG_PJ!");
						player.playSound(player.getLocation(), Sound.ENTITY_HORSE_DEATH, 1, 1);
					} else {
						if (Bukkit.getServer().getPlayer(args[0]) != null) {
							Player targetPlayer = player.getServer().getPlayer(args[0]);
							String targetPlayerName = targetPlayer.getName();
							File userdata1 = new File(
									Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
									File.separator + "PlayerDatabase");
							File f1 = new File(userdata1, File.separator + targetPlayerName + ".yml");
							FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
							String freeze = playerData1.getString("freeze");
							if (freeze.equalsIgnoreCase("true")) {
								try {
									playerData1.set("freeze", "false");
									playerData1.save(f1);
								} catch (IOException e) {
									e.printStackTrace();
								}
								player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
								targetPlayer.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
								player.sendMessage(sv + "You " + ChatColor.GREEN + "grant " + ChatColor.YELLOW
										+ targetPlayerName + "'s ability " + ChatColor.GRAY + "to move.");
								targetPlayer.setAllowFlight(false);
							}
							if (freeze.equalsIgnoreCase("false")) {
								try {
									playerData1.set("freeze", "true");
									playerData1.save(f1);
								} catch (IOException e) {
									e.printStackTrace();
								}
								player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 0);
								targetPlayer.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
								player.sendMessage(sv + "You " + ChatColor.RED + "revoke " + ChatColor.YELLOW
										+ targetPlayerName + "'s ability " + ChatColor.GRAY + "to move.");
								targetPlayer.setAllowFlight(true);
							}
						}
					}
				} else {
					player.sendMessage(sv + type + "/freeze [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("mylist")) {
			String list = getConfig().getString("mylist");
			player.sendMessage(list.replaceAll("\n", System.getProperty("line.separator")));
		}
		if (CommandLabel.equalsIgnoreCase("setlist")) {
			if (player.getUniqueId().toString().equalsIgnoreCase("36827ea4-37ac-4907-add3-01d9ba091ef9")) {
				for (String part : args) {
					if (message != "")
						message += " ";
					message += part;
				}
				getConfig().set("mylist", message.replaceAll("\n", System.getProperty("line.separator")));
				saveConfig();
			} else {
				player.sendMessage("You're not SMD_SSG_PJ!");
			}
		}
		if (CommandLabel.equalsIgnoreCase("resetredeem") || CommandLabel.equalsIgnoreCase("SMDMain:resetredeem")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.resetredeem")) {
				getConfig().set("redeem", null);
				saveConfig();
				player.sendMessage(sv + ChatColor.GREEN + "Reset redeem complete.");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("redeem") || CommandLabel.equalsIgnoreCase("SMDMain:redeem")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("QUOTA2704")) {
					if (getConfig().getString("redeem." + playerName) == null
							|| getConfig().getString("redeem." + playerName).equalsIgnoreCase("false")) {
						int tprq = playerData.getInt("Quota.TPR");
						int lcq = playerData.getInt("Quota.LuckyClick");
						try {
							playerData.set("Quota.TPR", tprq + 15);
							playerData.set("Quota.LuckyClick", lcq + 15);
							playerData.save(f);
						} catch (IOException e) {
							e.printStackTrace();
						}
						player.sendMessage("");
						player.sendMessage(ChatColor.GREEN + "Here is your reward!");
						player.sendMessage(ChatColor.WHITE + "15x " + ChatColor.YELLOW + "TPR Quota");
						player.sendMessage(ChatColor.WHITE + "15x " + ChatColor.LIGHT_PURPLE + "LuckyClick Quota");
						player.sendMessage("");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
						getConfig().set("redeem." + playerName, "true");
						saveConfig();
					} else {
						player.sendMessage(sv + "You already earn reward from this code. " + ChatColor.YELLOW + "("
								+ args[0].toUpperCase() + ")");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + "Your redeem code is incorrect!");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + type + "/redeem [code]");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("reconfig") || CommandLabel.equalsIgnoreCase("SMDMain:reconfig")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.reconfig")) {
				if (args.length == 1) {
					if (Bukkit.getServer().getPlayer(args[0]) != null) {
						Player targetPlayer = player.getServer().getPlayer(args[0]);
						String targetPlayerName = targetPlayer.getName();
						String targetPlayerUUID = targetPlayer.getUniqueId().toString();
						File userdata1 = new File(
								Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
								File.separator + "PlayerDatabase");
						File f1 = new File(userdata1, File.separator + targetPlayerName + ".yml");
						FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
						try {
							playerData1.createSection("rank");
							playerData1.set("rank", "default");
							playerData1.createSection("warn");
							playerData1.set("warn", 0);
							playerData1.createSection("mute");
							playerData1.set("mute.is", "false");
							playerData1.set("mute.reason", "none");
							playerData1.set("home", null);
							playerData1.createSection("uuid");
							playerData1.set("uuid", targetPlayerUUID);
							playerData1.createSection("money");
							playerData1.set("money", 0);
							playerData1.createSection("Quota");
							playerData1.set("Quota.TPR", 0);
							playerData1.set("Quota.LuckyClick", 0);
							getConfig().set("redeem." + playerName, "false");
							saveConfig();
							playerData1.save(f1);
						} catch (IOException e) {
							e.printStackTrace();
						}
						Bukkit.broadcastMessage(sv + "Player " + ChatColor.YELLOW + targetPlayerName + "'s information "
								+ ChatColor.GRAY + "has been " + ChatColor.RED + "reset " + ChatColor.GRAY + "by "
								+ ChatColor.AQUA + playerName + ".");
						for (Player p : Bukkit.getOnlinePlayers()) {
							p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 2, 2);
						}
					} else {
						player.sendMessage(sv + wrongplayer);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + type + "/resetconfig [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("money") || CommandLabel.equalsIgnoreCase("SMDMain:money")) {
			double money = playerData.getDouble("money");
			if (money > 2100000000) {
				try {
					playerData.set("money", 2100000000);
					playerData.save(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
				player.sendMessage(
						sv + "You have money " + money + " that is more than " + ChatColor.YELLOW + "2,100,000,000");
				player.sendMessage(sv + "You balance will be set to " + ChatColor.YELLOW + "2,100,000,000"
						+ ChatColor.GRAY + " to make your balance safety from bug.");
			}
			int money1 = playerData.getInt("money");
			player.sendMessage(sv + "Your balance is " + ChatColor.YELLOW + money1 + " Coin(s)");
		}
		if (CommandLabel.equalsIgnoreCase("paymoney") || CommandLabel.equalsIgnoreCase("SMDMain:paymoney")) {
			double money = playerData.getDouble("money");
			if (args.length == 2) {
				if (Bukkit.getServer().getPlayer(args[0]) != null) {
					Player targetPlayer = player.getServer().getPlayer(args[0]);
					String targetPlayerName = targetPlayer.getName();
					File userdata1 = new File(
							Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
							File.separator + "PlayerDatabase");
					File f1 = new File(userdata1, File.separator + targetPlayerName + ".yml");
					FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
					double targetPlayerMoney = playerData1.getDouble("money");
					if (isInt(args[1])) {
						int paymoney = Integer.parseInt(args[1]);
						if (paymoney > 0 && paymoney < money) {
							try {
								playerData.set("money", money - paymoney);
								playerData.save(f);
							} catch (IOException e) {
								e.printStackTrace();
							}
							try {
								playerData1.set("money", targetPlayerMoney + paymoney);
								playerData1.save(f1);
							} catch (IOException e) {
								e.printStackTrace();
							}
							if (targetPlayerMoney > 2100000000) {
								try {
									playerData1.set("money", 2100000000);
									playerData1.save(f1);
								} catch (IOException e) {
									e.printStackTrace();
								}
								targetPlayer.sendMessage(sv + "You have money " + money + " that is more than "
										+ ChatColor.YELLOW + "2,100,000,000");
								targetPlayer.sendMessage(sv + "You balance will be set to " + ChatColor.YELLOW
										+ "2,100,000,000" + ChatColor.GRAY + " to make your balance safety from bug.");
							}
							player.sendMessage(sv + ChatColor.GRAY + "You paid " + ChatColor.GREEN + args[1]
									+ ChatColor.GRAY + " to " + ChatColor.YELLOW + targetPlayerName);
							targetPlayer.sendMessage(sv + ChatColor.GRAY + "You received " + ChatColor.GREEN + args[1]
									+ ChatColor.GRAY + " from " + ChatColor.YELLOW + playerName);
							player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
							targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
						} else if (paymoney < 0) {
							player.sendMessage(sv + "Payment need to more than 0");
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
						} else if (paymoney > money) {
							player.sendMessage(sv + "You don't have enough money");
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
						}
					} else {
						player.sendMessage(sv + ChatColor.YELLOW + args[1] + ChatColor.GRAY + " is not number");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + wrongplayer);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + type + "/paymoney [player] [amount]");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);

			}
		}
		return true;
	}

	@EventHandler
	public void JoinServer(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		player.sendMessage("");
		player.sendMessage(
				"§d§l[Announce]§f: หากเปลี่ยนชื่อใหม่ ให้พิมพ์ §a/call [ชื่อเดิม] §fเพื่อดึงข้อมูลจากชื่อเดิม");
		player.sendMessage("§d§l[Announce]§f: §eกรุณาเช็คกฏเรื่อย ๆ เผื่อมีการ Update ใหม่ (/wiki rule)");
		player.sendMessage("");
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		if (!f.exists()) {
			try {
				playerData.createSection("rank");
				playerData.set("rank", "default");
				playerData.createSection("warn");
				playerData.set("warn", 0);
				playerData.createSection("mute");
				playerData.set("mute.is", "false");
				playerData.set("mute.reason", "none");
				playerData.createSection("freeze");
				playerData.set("freeze", "false");
				playerData.createSection("uuid");
				playerData.set("uuid", player.getUniqueId().toString());
				playerData.createSection("money");
				playerData.set("money", 0);
				playerData.createSection("Quota");
				playerData.set("Quota.TPR", 0);
				playerData.set("Quota.LuckyClick", 0);
				getConfig().set("redeem." + playerName, "false");
				saveConfig();
				playerData.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.see")) {
			player.sendMessage("§aYou have permission 'main.see', You will see command that player using automatic.");
		}
		if (f.exists()) {
			try {
				playerData.createSection("uuid");
				playerData.set("uuid", player.getUniqueId().toString());
				playerData.createSection("warping");
				playerData.set("warping", "false");
				playerData.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String rank = playerData.getString("rank");
			int countwarn = playerData.getInt("warn");
			if (countwarn > 0) {
				player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "ALERT!" + ChatColor.RED
						+ " You have been warned " + ChatColor.YELLOW + countwarn + " time(s).");
				player.sendMessage(ChatColor.RED + "If you get warned 3 time, You will be " + ChatColor.DARK_RED
						+ ChatColor.BOLD + "BANNED.");
			}
			if (rank.equalsIgnoreCase("default")) {
				player.setPlayerListName(ChatColor.BLUE + playerName);
				player.setDisplayName(ChatColor.BLUE + playerName);
				event.setJoinMessage(join + ChatColor.BLUE + playerName);
			} else if (rank.equalsIgnoreCase("staff")) {
				player.setPlayerListName(
						ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Staff" + ChatColor.BLUE + playerName);
				player.setDisplayName(
						ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Staff" + ChatColor.BLUE + playerName);
				event.setJoinMessage(
						join + ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Staff" + ChatColor.BLUE + playerName);
			} else if (rank.equalsIgnoreCase("vip")) {
				player.setPlayerListName(
						ChatColor.GREEN + "" + ChatColor.BOLD + "VIP" + ChatColor.DARK_GREEN + playerName);
				player.setDisplayName(
						ChatColor.GREEN + "" + ChatColor.BOLD + "VIP" + ChatColor.DARK_GREEN + playerName);
				event.setJoinMessage(
						join + ChatColor.GREEN + ChatColor.BOLD + "VIP" + ChatColor.DARK_GREEN + playerName);
			} else if (rank.equalsIgnoreCase("mvp")) {
				player.setPlayerListName(
						ChatColor.AQUA + "" + ChatColor.BOLD + "MVP" + ChatColor.DARK_AQUA + player.getName());
				player.setDisplayName(
						ChatColor.AQUA + "" + ChatColor.BOLD + "MVP" + ChatColor.DARK_AQUA + player.getName());
				event.setJoinMessage(join + ChatColor.AQUA + ChatColor.BOLD + "MVP" + ChatColor.DARK_AQUA + playerName);
			} else if (rank.equalsIgnoreCase("admin")) {
				player.setPlayerListName(
						ChatColor.DARK_RED + "" + ChatColor.BOLD + "Admin" + ChatColor.RED + player.getName());
				player.setDisplayName(
						ChatColor.DARK_RED + "" + ChatColor.BOLD + "Admin" + ChatColor.RED + player.getName());
				event.setJoinMessage(
						join + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Admin" + ChatColor.RED + playerName);
			} else if (rank.equalsIgnoreCase("owner")) {
				player.setPlayerListName(
						ChatColor.GOLD + "" + ChatColor.BOLD + "Owner" + ChatColor.YELLOW + player.getName());
				player.setDisplayName(
						ChatColor.GOLD + "" + ChatColor.BOLD + "Owner" + ChatColor.YELLOW + player.getName());
				event.setJoinMessage(
						join + ChatColor.GOLD + "" + ChatColor.BOLD + "Owner" + ChatColor.YELLOW + playerName);
			}
		}
	}

	@EventHandler
	public void playerPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String freeze = playerData.getString("freeze");
		if (freeze.equalsIgnoreCase("true")) {
			event.setCancelled(true);
			ActionBar action = new ActionBar(ChatColor.AQUA + "You're " + ChatColor.BOLD + "FREEZING");
			action.sendToPlayer(player);
		}
	}

	@EventHandler
	public void playerBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String freeze = playerData.getString("freeze");
		if (freeze.equalsIgnoreCase("true")) {
			event.setCancelled(true);
			ActionBar action = new ActionBar(ChatColor.AQUA + "You're " + ChatColor.BOLD + "FREEZING");
			action.sendToPlayer(player);
		}
	}

	@EventHandler
	public void playerChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		String message2 = message.replaceAll("%", "%%");
		String messagem = message2.replaceAll("&", "§");
		String message1 = ChatColor.WHITE + ": " + messagem;
		Player player = event.getPlayer();
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String rank = playerData.getString("rank");
		String muteis = playerData.getString("mute.is");
		String mutere = playerData.getString("mute.reason");
		if (muteis.equalsIgnoreCase("true")) {
			player.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GRAY + "You have been muted.");
			player.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.YELLOW + "Reason: " + ChatColor.GRAY + mutere);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			event.setCancelled(true);
		} else {
			if (rank.equalsIgnoreCase("default")) {
				event.setFormat(ChatColor.BLUE + player.getName() + ChatColor.GRAY + message1);
			} else if (rank.equalsIgnoreCase("staff")) {
				event.setFormat(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Staff" + ChatColor.BLUE + playerName
						+ ChatColor.WHITE + message1);
			} else if (rank.equalsIgnoreCase("vip")) {
				event.setFormat(ChatColor.GREEN + "" + ChatColor.BOLD + "VIP" + ChatColor.DARK_GREEN + playerName
						+ ChatColor.WHITE + message1);
			} else if (rank.equalsIgnoreCase("mvp")) {
				event.setFormat(ChatColor.AQUA + "" + ChatColor.BOLD + "MVP" + ChatColor.DARK_AQUA + playerName
						+ ChatColor.WHITE + message1);
			} else if (rank.equalsIgnoreCase("admin")) {
				event.setFormat(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Admin" + ChatColor.RED + playerName
						+ ChatColor.WHITE + message1);
			} else if (rank.equalsIgnoreCase("owner")) {
				event.setFormat(ChatColor.GOLD + "" + ChatColor.BOLD + "Owner" + ChatColor.YELLOW + playerName
						+ ChatColor.WHITE + message1);
			} else {
				event.setFormat(ChatColor.BLUE + player.getName() + ChatColor.GRAY + message1);
			}
		}
	}

	@EventHandler
	public void PlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String freeze = playerData.getString("freeze");
		if (freeze.equalsIgnoreCase("true")) {
			event.setCancelled(true);
			ActionBar action = new ActionBar(ChatColor.AQUA + "You're " + ChatColor.BOLD + "FREEZING");
			action.sendToPlayer(player);
			player.setAllowFlight(true);
		}
	}

	@EventHandler
	public void PlayerCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		String playerDisplay = player.getDisplayName();
		String command = event.getMessage().replaceAll("&", "§");
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String freeze = playerData.getString("freeze");
		if (freeze.equalsIgnoreCase("true")) {
			event.setCancelled(true);
			ActionBar action = new ActionBar(ChatColor.AQUA + "You're " + ChatColor.BOLD + "FREEZING");
			action.sendToPlayer(player);
			Bukkit.broadcast(ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "CMD"
					+ ChatColor.DARK_PURPLE + "] " + ChatColor.RED + "(BLOCKED) " + playerDisplay + ChatColor.DARK_GRAY
					+ ": " + ChatColor.GREEN + command, "main.seecmd");
		}
		if (freeze.equalsIgnoreCase("false")) {
			Bukkit.broadcast(ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "CMD"
					+ ChatColor.DARK_PURPLE + "] " + ChatColor.RED + playerDisplay + ChatColor.GRAY + ": "
					+ ChatColor.GREEN + command, "main.seecmd");
		}
	}

	@EventHandler
	public void QuitServer(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String rank = playerData.getString("rank");
		if (rank.equalsIgnoreCase("default")) {
			event.setQuitMessage(left + ChatColor.BLUE + player.getName());
		} else if (rank.equalsIgnoreCase("staff")) {
			event.setQuitMessage(left + ChatColor.DARK_BLUE + ChatColor.BOLD + "Staff" + ChatColor.BLUE + playerName);
		} else if (rank.equalsIgnoreCase("vip")) {
			event.setQuitMessage(left + ChatColor.GREEN + ChatColor.BOLD + "VIP" + ChatColor.DARK_GREEN + playerName);
		} else if (rank.equalsIgnoreCase("mvp")) {
			event.setQuitMessage(left + ChatColor.AQUA + ChatColor.BOLD + "MVP" + ChatColor.DARK_AQUA + playerName);
		} else if (rank.equalsIgnoreCase("admin")) {
			event.setQuitMessage(left + ChatColor.DARK_RED + ChatColor.BOLD + "Admin" + ChatColor.RED + playerName);
		} else if (rank.equalsIgnoreCase("owner")) {
			event.setQuitMessage(left + ChatColor.GOLD + ChatColor.BOLD + "Owner" + ChatColor.YELLOW + playerName);
		}
		try {
			playerData.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		getConfig().set("Teleport." + playerName, "None");
		saveConfig();
	}

	@EventHandler
	public void PlayerClick(PlayerInteractEvent e) {
		Action act;
		act = e.getAction();
		if ((act == Action.RIGHT_CLICK_BLOCK) == false) {
			return;
		}
		Player player = e.getPlayer();
		Block block = e.getClickedBlock();
		Inventory inv = player.getInventory();
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		int money = playerData.getInt("money");
		if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) {
			Sign s = (Sign) block.getState();
			if (s.getLine(0).equalsIgnoreCase("[sell]") || s.getLine(0).equalsIgnoreCase("[buy]")) {
				String s0 = s.getLine(0).toLowerCase();
				int s1 = Integer.parseInt(s.getLine(1));
				String s2 = s.getLine(2).toLowerCase();
				double s3 = Integer.parseInt(s.getLine(3));
				if (!s0.isEmpty() && !s.getLine(1).isEmpty() && !s2.isEmpty() && !s.getLine(3).isEmpty()) {
					if (s2.equalsIgnoreCase("diamond")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.DIAMOND, s1)) {
								inv.removeItem(new ItemStack(Material.DIAMOND, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x "
										+ s2.toUpperCase().toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.DIAMOND, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("blaze_rod")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.BLAZE_ROD, s1)) {
								inv.removeItem(new ItemStack(Material.BLAZE_ROD, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.BLAZE_ROD, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("exp_bottle")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.EXP_BOTTLE, s1)) {
								inv.removeItem(new ItemStack(Material.EXP_BOTTLE, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.EXP_BOTTLE, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("packed_ice")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.PACKED_ICE, s1)) {
								inv.removeItem(new ItemStack(Material.PACKED_ICE, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.PACKED_ICE, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("slime_ball")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.SLIME_BALL, s1)) {
								inv.removeItem(new ItemStack(Material.SLIME_BALL, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.SLIME_BALL, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("book")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.BOOK, s1)) {
								inv.removeItem(new ItemStack(Material.BOOK, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.BOOK, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("spruce_log")) {
						ItemStack item = new ItemStack(Material.LOG, s1, (short) 1);
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(new ItemStack(item))) {
								inv.removeItem(new ItemStack(item));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(item));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("acacia_log")) {
						ItemStack item = new ItemStack(Material.LOG_2, s1, (short) 0);
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(item)) {
								inv.removeItem(new ItemStack(item));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(item));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("dark_oak_log")) {
						ItemStack item = new ItemStack(Material.LOG_2, s1, (short) 1);
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(new ItemStack(item))) {
								inv.removeItem(new ItemStack(item));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(item));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("jungle_log")) {
						ItemStack item = new ItemStack(Material.LOG, s1, (short) 3);
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(new ItemStack(item))) {
								inv.removeItem(new ItemStack(item));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(item));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("birch_log")) {
						ItemStack item = new ItemStack(Material.LOG, s1, (short) 2);
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(new ItemStack(item))) {
								inv.removeItem(new ItemStack(item));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(item));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("oak_log")) {
						ItemStack item = new ItemStack(Material.LOG, s1, (short) 0);
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(new ItemStack(item))) {
								inv.removeItem(new ItemStack(item));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(item));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("coal")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.COAL, s1)) {
								inv.removeItem(new ItemStack(Material.COAL, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.COAL, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("iron_ingot")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.IRON_INGOT, s1)) {
								inv.removeItem(new ItemStack(Material.IRON_INGOT, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.IRON_INGOT, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("gold_ingot")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.GOLD_INGOT, s1)) {
								inv.removeItem(new ItemStack(Material.GOLD_INGOT, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.GOLD_INGOT, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("ghast_tear")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.GHAST_TEAR, s1)) {
								inv.removeItem(new ItemStack(Material.GHAST_TEAR, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.GHAST_TEAR, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("redstone")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.REDSTONE, s1)) {
								inv.removeItem(new ItemStack(Material.REDSTONE, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.REDSTONE, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);

							}
						}
					}
					if (s2.equalsIgnoreCase("LAPIS_LAZURI")) {
						ItemStack lapis = new ItemStack(Material.INK_SACK, s1, (short) 4);
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(new ItemStack(lapis))) {
								inv.removeItem(new ItemStack(Material.INK_SACK, s1, (short) 4));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.INK_SACK, s1, (short) 4));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("quartz")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.QUARTZ, s1)) {
								inv.removeItem(new ItemStack(Material.QUARTZ, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.QUARTZ, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("emerald")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.EMERALD, s1)) {
								inv.removeItem(new ItemStack(Material.EMERALD, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.EMERALD, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("wheat")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.WHEAT, s1)) {
								inv.removeItem(new ItemStack(Material.WHEAT, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.WHEAT, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("carrot")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.CARROT_ITEM, s1)) {
								inv.removeItem(new ItemStack(Material.CARROT_ITEM, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.CARROT_ITEM, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("potato")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.POTATO_ITEM, s1)) {
								inv.removeItem(new ItemStack(Material.POTATO_ITEM, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.POTATO_ITEM, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("melon")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.MELON, s1)) {
								inv.removeItem(new ItemStack(Material.MELON, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.MELON, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("pumpkin")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.PUMPKIN, s1)) {
								inv.removeItem(new ItemStack(Material.PUMPKIN, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.PUMPKIN, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("reeds")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.SUGAR_CANE, s1)) {
								inv.removeItem(new ItemStack(Material.SUGAR_CANE, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.SUGAR_CANE, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("egg")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.EGG, s1)) {
								inv.removeItem(new ItemStack(Material.EGG, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.EGG, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("beetroot")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.BEETROOT, s1)) {
								inv.removeItem(new ItemStack(Material.BEETROOT, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.BEETROOT, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("cactus")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.CACTUS, s1)) {
								inv.removeItem(new ItemStack(Material.CACTUS, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.CACTUS, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("sea_lantern")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.SEA_LANTERN, s1)) {
								inv.removeItem(new ItemStack(Material.SEA_LANTERN, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.SEA_LANTERN, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("glowstone")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.GLOWSTONE, s1)) {
								inv.removeItem(new ItemStack(Material.GLOWSTONE, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.GLOWSTONE, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("saddle")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.SADDLE, s1)) {
								inv.removeItem(new ItemStack(Material.SADDLE, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.SADDLE, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("lead")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.LEASH, s1)) {
								inv.removeItem(new ItemStack(Material.LEASH, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.LEASH, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("ender_pearl")) {
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(Material.ENDER_PEARL, s1)) {
								inv.removeItem(new ItemStack(Material.ENDER_PEARL, s1));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(Material.ENDER_PEARL, s1));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("normal_fish")) {
						ItemStack item = new ItemStack(Material.RAW_FISH, s1, (short) 0);
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(new ItemStack(item))) {
								inv.removeItem(new ItemStack(item));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(item));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("salmon_fish")) {
						ItemStack item = new ItemStack(Material.RAW_FISH, s1, (short) 1);
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(new ItemStack(item))) {
								inv.removeItem(new ItemStack(item));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(item));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("clown_fish")) {
						ItemStack item = new ItemStack(Material.RAW_FISH, s1, (short) 2);
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(new ItemStack(item))) {
								inv.removeItem(new ItemStack(item));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(item));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (s2.equalsIgnoreCase("puffer_fish")) {
						ItemStack item = new ItemStack(Material.RAW_FISH, s1, (short) 3);
						if (s0.endsWith("[sell]") == true) {
							if (inv.contains(new ItemStack(item))) {
								inv.removeItem(new ItemStack(item));
								try {
									playerData.set("money", money + s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You get " + ChatColor.GREEN + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from selling " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + noi);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
						if (s0.endsWith("[buy]")) {
							if (money > s3) {
								inv.addItem(new ItemStack(item));
								try {
									playerData.set("money", money - s3);
									playerData.save(f);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								player.sendMessage(sv + "You paid " + ChatColor.GOLD + s3 + " Coin(s) " + ChatColor.GRAY
										+ "from buying " + ChatColor.AQUA + s1 + "x " + s2.toUpperCase());
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
							} else {
								player.sendMessage(sv + nom);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						}
					}
					if (money > 2100000000) {
						try {
							playerData.set("money", 2100000000);
							playerData.save(f);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						player.sendMessage(sv + "You have money " + money + " that is more than " + ChatColor.YELLOW
								+ "2,100,000,000");
						player.sendMessage(sv + "You balance will be set to " + ChatColor.YELLOW + "2,100,000,000"
								+ ChatColor.GRAY + " to make your balance safety from bug.");
					}
				} else {
					return;
				}
			}
			String s0 = s.getLine(0).toLowerCase();
			String s1 = s.getLine(1).toLowerCase();
			String s2 = s.getLine(2).toLowerCase();
			String s3 = s.getLine(3).toLowerCase();
			int lcq = playerData.getInt("Quota.LuckyClick");
			if (s.getLine(0).equalsIgnoreCase("[luckyclick]")) {
				if (lcq < 1) {
					player.sendMessage(sv + "You don't have enough quota!");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
					player.sendMessage(sv + "Use " + ChatColor.AQUA + "/buyquota LuckyClick" + ChatColor.GRAY
							+ " to buy more quota.");
				} else {
					try {
						playerData.set("Quota.LuckyClick", lcq - 1);
						playerData.save(f);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					if (s3.equalsIgnoreCase("§2§l50§0§l|§4§l50")) {
						int r = new Random().nextInt(20);
						if (r == 0) {
							int r1 = new Random().nextInt(6);
							if (r1 < 0) {
								r1 = 1;
								ItemStack item = new ItemStack(Material.DIAMOND, r1);
								player.getInventory().addItem(item);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " DIAMOND");
							}
							if (r1 > 0) {
								ItemStack item = new ItemStack(Material.DIAMOND, r1);
								player.getInventory().addItem(item);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " DIAMOND");
							}
						}
						if (r == 1) {
							int r1 = new Random().nextInt(16);
							if (r1 < 0) {
								r1 = 1;
								ItemStack item = new ItemStack(Material.IRON_INGOT, r1);
								player.getInventory().addItem(item);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " IRON_INGOT");
							}
							if (r1 > 0) {
								ItemStack item = new ItemStack(Material.IRON_INGOT, r1);
								player.getInventory().addItem(item);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " IRON_INGOT");
							}
						}
						if (r == 2) {
							int r1 = new Random().nextInt(501);
							if (r1 < 0) {
								r1 = 1;
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " Coin(s)");
							}
							if (r1 > 0) {
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " Coin(s)");
							}
							try {
								playerData.set("money", money + r1);
								playerData.save(f);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						if (r == 3) {
							int r1 = new Random().nextInt(21);
							if (r1 < 0) {
								r1 = 1;
								ItemStack item = new ItemStack(Material.EXP_BOTTLE, r1);
								player.getInventory().addItem(item);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " EXP_BOTTLE");
							}
							if (r1 > 0) {
								ItemStack item = new ItemStack(Material.EXP_BOTTLE, r1);
								player.getInventory().addItem(item);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " EXP_BOTTLE");
							}
						}
						if (r == 4) {
							int r1 = new Random().nextInt(11);
							if (r1 < 0) {
								r1 = 1;
								ItemStack item = new ItemStack(Material.GOLD_INGOT, r1);
								player.getInventory().addItem(item);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " GOLD_INGOT");
							}
							if (r1 > 0) {
								ItemStack item = new ItemStack(Material.GOLD_INGOT, r1);
								player.getInventory().addItem(item);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " GOLD_INGOT");
							}
						}
						if (r == 5) {
							int r1 = new Random().nextInt(201);
							if (r1 < 0) {
								r1 = 1;
							}
							try {
								playerData.set("money", money - r1);
								playerData.save(f);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							player.sendMessage(lc + ChatColor.RED + "BAD LUCK! " + ChatColor.WHITE + "You got "
									+ ChatColor.RED + "-" + r1 + " Coin(s)");
						}
						if (r == 6) {
							player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 10));
							player.sendMessage(lc + ChatColor.RED + "BAD LUCK! " + ChatColor.WHITE + "You got "
									+ ChatColor.RED + "30 second CONFUSED Effect");
						}
						if (r == 7) {
							player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1200, 10));
							player.sendMessage(lc + ChatColor.RED + "BAD LUCK! " + ChatColor.WHITE + "You got "
									+ ChatColor.RED + "1 minute SLOW_DIGGING Effect");
						}
						if (r == 8) {
							int r1 = new Random().nextInt(65);
							if (r1 < 0) {
								r1 = 1;
								ItemStack item = new ItemStack(Material.DIRT, r1);
								player.getInventory().addItem(item);
								player.sendMessage(lc + ChatColor.RED + "BAD LUCK! " + ChatColor.WHITE + "You got "
										+ ChatColor.RED + r1 + " Dirt");
							}
							if (r1 > 0) {
								ItemStack item = new ItemStack(Material.DIRT, r1);
								player.getInventory().addItem(item);
								player.sendMessage(lc + ChatColor.RED + "BAD LUCK! " + ChatColor.WHITE + "You got "
										+ ChatColor.RED + r1 + " Dirt");
							}
						}
						if (r == 9) {
							player.sendMessage(lc + ChatColor.RED + "BAD LUCK! " + ChatColor.WHITE + "You don't get "
									+ ChatColor.RED + "ANYTHING");
						}
						if (r == 10) {
							int r1 = new Random().nextInt(16);
							if (r1 < 0) {
								r1 = 1;
								ItemStack item = new ItemStack(Material.INK_SACK, r1, (short) 4);
								player.getInventory().addItem(item);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " LAPIS_LAZURI");
							}
							if (r1 > 0) {
								ItemStack item = new ItemStack(Material.INK_SACK, r1, (short) 4);
								player.getInventory().addItem(item);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " LAPIS_LAZURI");
							}
						}
						if (r == 11) {
							int r1 = new Random().nextInt(4);
							if (r1 < 0) {
								r1 = 1;
								ItemStack item = new ItemStack(Material.EMERALD, r1);
								player.getInventory().addItem(item);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " EMERALD");
							}
							if (r1 > 0) {
								ItemStack item = new ItemStack(Material.EMERALD, r1);
								player.getInventory().addItem(item);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " EMERALD");
							}
						}
						if (r == 12) {
							player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1200, 10));
							player.sendMessage(lc + ChatColor.RED + "Bad Luck! " + ChatColor.WHITE + "You got "
									+ ChatColor.RED + "1 Minute BLINDNESS Effect");
						}
						if (r == 13) {
							player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 10));
							player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
									+ ChatColor.YELLOW + "10 second REGENERATION Effect");
						}
						if (r == 14) {
							ItemStack item = new ItemStack(Material.GOLDEN_APPLE, 1);
							player.getInventory().addItem(item);
							player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
									+ ChatColor.YELLOW + "1 NORMAL_GOLDEN_APPLE");
						}
						if (r == 15) {
							player.sendMessage(lc + ChatColor.RED + "Bad Luck! " + ChatColor.WHITE + "You got "
									+ ChatColor.RED + "I'm Gay Message, LOL!");
							Bukkit.broadcastMessage(player.getDisplayName() + ChatColor.WHITE + ": I'm Gay~!");
						}
						if (r == 16) {
							int r1 = new Random().nextInt(21);
							if (r1 < 0) {
								r1 = 1;
								ItemStack item = new ItemStack(Material.REDSTONE, r1);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " REDSTONE");
								player.getInventory().addItem(item);
							}
							if (r1 > 0) {
								ItemStack item = new ItemStack(Material.REDSTONE, r1);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
										+ ChatColor.YELLOW + r1 + " REDSTONE");
								player.getInventory().addItem(item);
							}
						}
						if (r == 17) {
							player.sendMessage(lc + ChatColor.GREEN + "Good Luck! " + ChatColor.WHITE + "You got "
									+ ChatColor.YELLOW + "AIR " + ChatColor.GRAY + ChatColor.ITALIC + "(Seriously?)");
						}
						if (r == 18) {
							int r1 = new Random().nextInt(21);
							if (r1 < 0) {
								r1 = 1;
								ItemStack item = new ItemStack(Material.COAL, r1);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! (or not) " + ChatColor.WHITE
										+ "You got " + ChatColor.YELLOW + r1 + " COAL");
								player.getInventory().addItem(item);
							}
							if (r1 > 0) {
								ItemStack item = new ItemStack(Material.COAL, r1);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! (or not) " + ChatColor.WHITE
										+ "You got " + ChatColor.YELLOW + r1 + " COAL");
								player.getInventory().addItem(item);
							}
						}
						if (r == 19) {
							int r1 = new Random().nextInt(31);
							if (r1 < 0) {
								r1 = 1;
								ItemStack item = new ItemStack(Material.COBBLESTONE, r1);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! (or not) " + ChatColor.WHITE
										+ "You got " + ChatColor.YELLOW + r1 + " COBBLESTONE");
								player.getInventory().addItem(item);
							}
							if (r1 > 0) {
								ItemStack item = new ItemStack(Material.COBBLESTONE, r1);
								player.sendMessage(lc + ChatColor.GREEN + "Good Luck! (or not) " + ChatColor.WHITE
										+ "You got " + ChatColor.YELLOW + r1 + " COBBLESTONE");
								player.getInventory().addItem(item);
							}
						}
					}
					int lcq2 = playerData.getInt("Quota.LuckyClick");
					player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
					player.sendMessage(sv + "You have " + ChatColor.LIGHT_PURPLE + lcq2 + " Lucky Click Quota leff!");
				}
			}
		} else {
			return;
		}
	}

	@EventHandler
	public void PlayerChangeSign(SignChangeEvent event) {
		Player player = event.getPlayer();
		String line0 = event.getLine(0).toLowerCase();
		if (line0.endsWith("[tp]") || line0.endsWith("[sell]") || line0.endsWith("[buy]")
				|| line0.endsWith("[luckyclick")) {
			if (!player.isOp() && !player.hasPermission("main.sign")) {
				event.setLine(0, "§4§lSorry§r, but");
				event.setLine(1, "You §lneed §rperm.");
				event.setLine(2, "or op to create sign with");
				event.setLine(3, "'" + line0 + "'" + " prefix!");
				player.sendMessage(sv + noperm);
				Bukkit.broadcastMessage(sv + "Player " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY
						+ " try to create sign " + ChatColor.RED + ChatColor.BOLD + line0);
				return;
			}
		}
	}

	@EventHandler
	public void PlayerStandOnPlate(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		loc.setY(loc.getY());
		String warping = playerData.getString("warping");
		Block block = loc.getBlock();
		if (block.getType() == Material.GOLD_PLATE || block.getType() == Material.IRON_PLATE) {
			Location loc2 = player.getLocation();
			loc2.setY(loc.getY() - 2);
			Block block2 = loc2.getBlock();
			if ((block2.getType() == Material.SIGN_POST) || (block2.getType() == Material.WALL_SIGN)) {
				Sign sign = (Sign) block2.getState();
				if (sign.getLine(0).equalsIgnoreCase("[tp]")) {
					if (!warping.equalsIgnoreCase("false")) {

					} else {
						player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 10));
						ActionBar action = new ActionBar(
								ChatColor.YELLOW + "" + ChatColor.BOLD + "Hold " + ChatColor.GREEN + ChatColor.BOLD
										+ ChatColor.UNDERLINE + "Shift" + ChatColor.AQUA + " to use.");
						action.sendToPlayer(player);
					}
				}
			}
		}
	}

	@EventHandler
	public void PlayerUsePlate(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		Location loc = player.getLocation();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String warping = playerData.getString("warping");
		loc.setY(loc.getY());
		Block block = loc.getBlock();
		if (event.isSneaking() == true) {
			if (block.getType() == Material.GOLD_PLATE || block.getType() == Material.IRON_PLATE) {
				Location loc2 = player.getLocation();
				loc2.setY(loc.getY() - 2);
				Block block2 = loc2.getBlock();
				if ((block2.getType() == Material.SIGN_POST || block2.getType() == Material.WALL_SIGN)) {
					Location loc3 = player.getLocation();
					loc3.setY(loc.getY() - 3);
					Block block3 = loc3.getBlock();
					Sign sign = (Sign) block2.getState();
					Sign sign2 = (Sign) block3.getState();
					if (sign.getLine(0).equalsIgnoreCase("[tp]") && sign2.getLine(0).equalsIgnoreCase("[world]")) {
						if (block3.getType() == Material.SIGN_POST || block3.getType() == Material.WALL_SIGN) {
							World world = Bukkit.getWorld(sign2.getLine(1) + sign2.getLine(2) + sign2.getLine(3));
							if (warping.equalsIgnoreCase("false")) {
								try {
									playerData.set("warping", "warp1");
									playerData.save(f);
									player.performCommand("platewarp");
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								return;
							}
							/*
							 * if (world != null) { Location pl =
							 * player.getLocation(); double xh =
							 * Integer.parseInt(sign.getLine(1)); double yh =
							 * Integer.parseInt(sign.getLine(2)); double zh =
							 * Integer.parseInt(sign.getLine(3)); double x = xh
							 * + 0.5; double y = yh; double z = zh + 0.5; double
							 * yaw = pl.getYaw(); double pitch = pl.getPitch();
							 * Location loca = new Location(world, x, y, z);
							 * loca.setPitch((float) pitch); loca.setYaw((float)
							 * yaw); player.teleport(loca); } else { Location pl
							 * = player.getLocation(); double xh =
							 * Integer.parseInt(sign.getLine(1)); double yh =
							 * Integer.parseInt(sign.getLine(2)); double zh =
							 * Integer.parseInt(sign.getLine(3)); double x = xh
							 * + 0.5; double y = yh; double z = zh + 0.5; double
							 * yaw = pl.getYaw(); double pitch = pl.getPitch();
							 * World world2 = pl.getWorld(); Location loca = new
							 * Location(world2, x, y, z); loca.setPitch((float)
							 * pitch); loca.setYaw((float) yaw);
							 * player.teleport(loca); }
							 * player.playSound(player.getLocation(),
							 * Sound.ENTITY_CHICKEN_EGG, 10, 0);
							 */
						} else {
							return;
						}
					} else {
						return;
					}
				} else {
					return;
				}
			} else {
				return;
			}
		} else {
			return;
		}
	}

	public void playCircularEffect(Location location, Effect effect, boolean v) {
		for (int i = 0; i <= 8; i += ((!v && (i == 3)) ? 2 : 1))
			location.getWorld().playEffect(location, effect, i);
	}

	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}