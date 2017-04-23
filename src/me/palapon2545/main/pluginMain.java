package me.palapon2545.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import me.palapon2545.main.pluginMain;
import me.palapon2545.main.ActionBar;
import me.palapon2545.main.DelayLoadConfig;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;

public class pluginMain extends JavaPlugin implements Listener {

	public HashMap<String, Long> cooldowns = new HashMap<String, Long>();
	public final Logger logger = Logger.getLogger("Minecraft");
	public pluginMain plugin;
	public static DelayLoadConfig delayLoadConfig = null;
	public static Thread delayLoadConnfig_Thread = null;
	LinkedList<String> badWord = new LinkedList<String>();

	String serverpre = ChatColor.BLUE + "Server> " + ChatColor.GRAY;
	String portalpre = ChatColor.BLUE + "Portal> " + ChatColor.GRAY;
	String noperm = ChatColor.RED + "You don't have permission or op!";
	String sendtoworld = ChatColor.GRAY + "Sent you to world ";
	String wrongplayer = ChatColor.RED + "Player not found.";
	String type = ChatColor.GRAY + "Type: " + ChatColor.GREEN;
	String join = ChatColor.GREEN + "Join> ";
	String left = ChatColor.RED + "Left> ";

	public void onDisable() {
		Bukkit.broadcastMessage(serverpre + "SMDMain System: " + ChatColor.RED + ChatColor.BOLD + "Disable");
		for (Player player1 : Bukkit.getOnlinePlayers()) {
			player1.playSound(player1.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 0);
		}
		saveConfig();
		delayLoadConfig.setRunning(false);
	}

	public void onEnable() {
		Bukkit.broadcastMessage(serverpre + "SMDMain System: " + ChatColor.GREEN + ChatColor.BOLD + "Enable");
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
		boolean owner = rank.equalsIgnoreCase("owner");
		if (CommandLabel.equalsIgnoreCase("setspawn") || CommandLabel.equalsIgnoreCase("ss")
				|| CommandLabel.equalsIgnoreCase("SMDMain:ss") || CommandLabel.equalsIgnoreCase("SMDMain:setspawn")) {
			if (player.isOp() || player.hasPermission("Main.setspawn")) {
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
				player.sendMessage(serverpre + noperm);
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
			if (player.isOp() || player.hasPermission("Main.warp")) {
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
							player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 0);
						}
					}
				}
			} else {
				player.sendMessage(serverpre + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);

			}
		}
		if (CommandLabel.equalsIgnoreCase("removewarp") || CommandLabel.equalsIgnoreCase("SMDMain:removewarp")) {
			if (player.isOp() || player.hasPermission("Main.warp")) {
				if (f.exists()) {
					if (getConfig().getConfigurationSection("warp") != null) {
						getConfig().set("warp", null);
						player.sendMessage(portalpre + "Remove server warp complete.");
						for (Player player1 : Bukkit.getOnlinePlayers()) {
							player1.sendMessage(
									portalpre + ChatColor.RED + "Closed" + ChatColor.LIGHT_PURPLE + " Server Warp.");
							player1.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 10, 1);

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
			if (player.isOp() || player.hasPermission("Main.gamemode")) {
				if (args.length == 0) {
					player.sendMessage(serverpre + type + "/gamemode [mode] [player] (/gm)");
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
						player.sendMessage(
								serverpre + "Your gamemode has been updated to " + ChatColor.GREEN + "Creative.");
						player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
					} else if ((args[0].equalsIgnoreCase("0")) || (args[0].equalsIgnoreCase("s"))
							|| (args[0].equalsIgnoreCase("survival"))) {
						player.setGameMode(GameMode.SURVIVAL);
						player.sendMessage(
								serverpre + "Your gamemode has been updated to " + ChatColor.YELLOW + "Survival.");
						player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
					} else if ((args[0].equalsIgnoreCase("2")) || (args[0].equalsIgnoreCase("a"))
							|| (args[0].equalsIgnoreCase("adventure"))) {
						player.setGameMode(GameMode.ADVENTURE);
						player.sendMessage(serverpre + "Your gamemode has been updated to " + ChatColor.LIGHT_PURPLE
								+ "Adventure.");
						player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
					} else if ((args[0].equalsIgnoreCase("3")) || (args[0].equalsIgnoreCase("sp"))
							|| (args[0].equalsIgnoreCase("spectator"))) {
						player.setGameMode(GameMode.SPECTATOR);
						player.sendMessage(
								serverpre + "Your gamemode has been updated to " + ChatColor.AQUA + "Spectator.");
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
									serverpre + "Your gamemode has been updated to " + ChatColor.GREEN + "Creative.");
							player.sendMessage(serverpre + ChatColor.YELLOW + targetPlayerName + "'s " + ChatColor.GRAY
									+ "gamemode has been updated to " + ChatColor.GREEN + "Creative.");
							player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
						} else if ((args[0].equalsIgnoreCase("0")) || (args[0].equalsIgnoreCase("s"))
								|| (args[0].equalsIgnoreCase("survival"))) {
							targetPlayer.setGameMode(GameMode.SURVIVAL);
							targetPlayer.sendMessage(
									serverpre + "Your gamemode has been updated to " + ChatColor.YELLOW + "Survival.");
							player.sendMessage(serverpre + ChatColor.YELLOW + targetPlayerName + "'s " + ChatColor.GRAY
									+ "gamemode has been updated to " + ChatColor.YELLOW + "Survival.");
							player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
						} else if ((args[0].equalsIgnoreCase("2")) || (args[0].equalsIgnoreCase("a"))
								|| (args[0].equalsIgnoreCase("adventure"))) {
							targetPlayer.setGameMode(GameMode.ADVENTURE);
							targetPlayer.sendMessage(serverpre + "Your gamemode has been updated to "
									+ ChatColor.LIGHT_PURPLE + "Adventure.");
							player.sendMessage(serverpre + ChatColor.YELLOW + targetPlayerName + "'s " + ChatColor.GRAY
									+ "gamemode has been updated to " + ChatColor.LIGHT_PURPLE + "Adventure.");
							player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
						} else if ((args[0].equalsIgnoreCase("3")) || (args[0].equalsIgnoreCase("sp"))
								|| (args[0].equalsIgnoreCase("spectator"))) {
							targetPlayer.setGameMode(GameMode.SPECTATOR);
							targetPlayer.sendMessage(
									serverpre + "Your gamemode has been updated to " + ChatColor.AQUA + "Spectator.");
							player.sendMessage(serverpre + ChatColor.YELLOW + targetPlayerName + "'s " + ChatColor.GRAY
									+ "gamemode has been updated to " + ChatColor.AQUA + "Spectator.");
							player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
						}
					}
				}
			} else {
				player.sendMessage(serverpre + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
			}
		}
		if (CommandLabel.equalsIgnoreCase("heal") || CommandLabel.equalsIgnoreCase("SMDMain:heal")) {
			if (player.isOp() || player.hasPermission("Main.heal")) {
				if (args.length == 0) {
					player.setFoodLevel(40);
					for (PotionEffect Effect : player.getActivePotionEffects()) {
						player.removePotionEffect(Effect.getType());
					}
					player.setHealth(20);
					player.setFoodLevel(40);
					player.sendMessage(serverpre + ChatColor.LIGHT_PURPLE + "You have been healed!");
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("all")) {
						for (Player p : Bukkit.getOnlinePlayers()) {
							for (PotionEffect Effect : p.getActivePotionEffects()) {
								p.removePotionEffect(Effect.getType());
							}
							p.setHealth(20);
							p.setFoodLevel(40);
							p.sendMessage(serverpre + ChatColor.LIGHT_PURPLE + "You have been healed!");
							p.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
						}
						player.sendMessage(serverpre + ChatColor.LIGHT_PURPLE + "You healed " + ChatColor.YELLOW
								+ "all online player" + "!");
					} else if (Bukkit.getServer().getPlayer(args[0]) != null) {
						Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
						String targetPlayerName = targetPlayer.getName();
						for (PotionEffect Effect : targetPlayer.getActivePotionEffects()) {
							targetPlayer.removePotionEffect(Effect.getType());
						}
						targetPlayer.setHealth(20);
						targetPlayer.setFoodLevel(40);
						targetPlayer.sendMessage(serverpre + ChatColor.LIGHT_PURPLE + "You have been healed!");
						targetPlayer.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
						player.sendMessage(serverpre + ChatColor.LIGHT_PURPLE + "You healed " + ChatColor.YELLOW
								+ targetPlayerName + "!");
					} else {
						player.sendMessage(serverpre + wrongplayer);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(serverpre + type + "/heal [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(serverpre + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);

			}
		}
		if (CommandLabel.equalsIgnoreCase("fly") || CommandLabel.equalsIgnoreCase("SMDMain:fly")) {
			if (player.isOp() || player.hasPermission("Main.fly")) {
				if (args.length == 0) {
					if (player.getAllowFlight() == false) {
						player.setAllowFlight(true);
						player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
						player.sendMessage(serverpre + "You " + ChatColor.GREEN + "grant " + ChatColor.YELLOW
								+ playerName + "'s ability " + ChatColor.GRAY + "to fly. ");
					} else if (player.getAllowFlight() == true) {
						player.setAllowFlight(false);
						player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 0);
						player.sendMessage(serverpre + "You " + ChatColor.RED + "revoke " + ChatColor.YELLOW
								+ playerName + "'s ability " + ChatColor.GRAY + "to fly. ");
					}
				} else if (args.length == 1) {
					if (Bukkit.getServer().getPlayer(args[0]) != null) {
						Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
						String targetPlayerName = targetPlayer.getName();
						if (player.getAllowFlight() == false) {
							targetPlayer.setAllowFlight(true);
							player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
							player.sendMessage(serverpre + "You " + ChatColor.GREEN + "grant " + ChatColor.YELLOW
									+ targetPlayerName + "'s ability " + ChatColor.GRAY + "to fly. ");
						} else if (player.getAllowFlight() == true) {
							targetPlayer.setAllowFlight(false);
							player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 0);
							player.sendMessage(serverpre + "You " + ChatColor.RED + "revoke " + ChatColor.YELLOW
									+ targetPlayerName + "'s ability " + ChatColor.GRAY + "to fly. ");
						}
					} else {
						player.sendMessage(serverpre + wrongplayer);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(serverpre + type + "/fly [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(serverpre + noperm);
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
			player.sendMessage(serverpre + ChatColor.YELLOW + "You have been resend your location.");
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
		}
		if (CommandLabel.equalsIgnoreCase("day") || CommandLabel.equalsIgnoreCase("SMDMain:day")) {
			if (player.isOp() || player.hasPermission("Main.time")) {
				World w = ((Player) sender).getWorld();
				player.sendMessage(serverpre + "Set time to " + ChatColor.GOLD + "Day " + ChatColor.GRAY
						+ ChatColor.ITALIC + "(1000 ticks)");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(1000);
			} else {
				player.sendMessage(serverpre + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("midday") || CommandLabel.equalsIgnoreCase("SMDMain:midday")) {
			if (player.isOp() || player.hasPermission("Main.time")) {
				player.sendMessage(serverpre + "Set time to " + ChatColor.GOLD + "Midday " + ChatColor.GRAY
						+ ChatColor.ITALIC + "(6000 ticks)");
				World w = ((Player) sender).getWorld();
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(6000);
			} else {
				player.sendMessage(serverpre + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("night") || CommandLabel.equalsIgnoreCase("SMDMain:night")) {
			if (player.isOp() || player.hasPermission("Main.time")) {
				World w = ((Player) sender).getWorld();
				player.sendMessage(serverpre + "Set time to " + ChatColor.GOLD + "Night " + ChatColor.GRAY
						+ ChatColor.ITALIC + "(13000 ticks)");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(13000);
			} else {
				player.sendMessage(serverpre + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("midnight") || CommandLabel.equalsIgnoreCase("SMDMain:midnight")) {
			if (player.isOp() || player.hasPermission("Main.time")) {
				World w = ((Player) sender).getWorld();
				player.sendMessage(serverpre + "Set time to " + ChatColor.GOLD + "Midnight " + ChatColor.GRAY
						+ ChatColor.ITALIC + "(18000 ticks)");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(18000);
			} else {
				player.sendMessage(serverpre + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("bc") || CommandLabel.equalsIgnoreCase("SMDMain:bc")) {
			if (player.isOp() || player.hasPermission("Main.bc")) {
				if (args.length == 0 || args[0].isEmpty()) {
					player.sendMessage(serverpre + type + "/bc [text].");
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
					player.sendMessage(serverpre + noperm);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("force") || CommandLabel.equalsIgnoreCase("SMDMain:force")) {
			if (player.isOp() || player.hasPermission("Main.force")) {
				if (args.length == 0 || args[0].isEmpty()) {
					player.sendMessage(serverpre + type + "/force [player] [message].");
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
						player.sendMessage(serverpre + "You forced " + ChatColor.YELLOW + "all online player"
								+ ChatColor.GRAY + ": " + ChatColor.AQUA + message);
					} else if (Bukkit.getServer().getPlayer(args[0]) != null) {
						Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
						String targetPlayerName = targetPlayer.getName();
						message = "";
						for (int i = 1; i != args.length; i++)
							message += args[i] + " ";
						message = message.replaceAll("&", "§");
						targetPlayer.chat(message);
						player.sendMessage(serverpre + "You forced " + ChatColor.YELLOW + targetPlayerName
								+ ChatColor.GRAY + ": " + ChatColor.AQUA + message);
					} else {
						player.sendMessage(serverpre + wrongplayer);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(serverpre + noperm);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("bedrock") || CommandLabel.equalsIgnoreCase("SMDMain:bedrock")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " bedrock");
			player.sendMessage(serverpre + "Here you are, Use " + ChatColor.YELLOW + "Bedrock" + ChatColor.GRAY
					+ " to check block-logging");
			player.sendMessage(
					serverpre + ChatColor.GREEN + "Right-Click" + ChatColor.YELLOW + " to check at placed location");
			player.sendMessage(
					serverpre + ChatColor.GREEN + "Left-Click" + ChatColor.YELLOW + " to check at clicked location");
		}

		if (CommandLabel.equalsIgnoreCase("ping") || CommandLabel.equalsIgnoreCase("SMDMain:ping")) {
			int ping = ((CraftPlayer) player).getHandle().ping;
			if (args.length == 0) {
				if (ping < 31) {
					ChatColor color = ChatColor.AQUA;
					player.sendMessage(serverpre + "Your ping is " + color + ping + ChatColor.GRAY + " ms.");
				}
				if (ping > 30 && ping < 81) {
					ChatColor color = ChatColor.GREEN;
					player.sendMessage(serverpre + "Your ping is " + color + ping + ChatColor.GRAY + " ms.");
				}
				if (ping > 80 && ping < 151) {
					ChatColor color = ChatColor.GOLD;
					player.sendMessage(serverpre + "Your ping is " + color + ping + ChatColor.GRAY + " ms.");
				}
				if (ping > 150 && ping < 501) {
					ChatColor color = ChatColor.RED;
					player.sendMessage(serverpre + "Your ping is " + color + ping + ChatColor.GRAY + " ms.");
				}
				if (ping > 500) {
					ChatColor color = ChatColor.DARK_RED;
					player.sendMessage(serverpre + "Your ping is " + color + ping + ChatColor.GRAY + " ms.");
				}
			} else if (args.length == 1) {
				if (player.getServer().getPlayer(args[0]) != null) {
					Player targetPlayer = player.getServer().getPlayer(args[0]);
					String targetPlayerName = targetPlayer.getName();
					int ping2 = ((CraftPlayer) targetPlayer).getHandle().ping;
					if (ping2 < 31) {
						ChatColor color = ChatColor.AQUA;
						player.sendMessage(serverpre + ChatColor.YELLOW + targetPlayerName + "'s ping" + ChatColor.GRAY
								+ " is " + color + ping2 + ChatColor.GRAY + " ms.");
					} else if (ping2 > 30 && ping < 81) {
						ChatColor color = ChatColor.GREEN;
						player.sendMessage(serverpre + ChatColor.YELLOW + targetPlayerName + "'s ping" + ChatColor.GRAY
								+ " is " + color + ping2 + ChatColor.GRAY + " ms.");
					} else if (ping2 > 80 && ping < 151) {
						ChatColor color = ChatColor.GOLD;
						player.sendMessage(serverpre + ChatColor.YELLOW + targetPlayerName + "'s ping" + ChatColor.GRAY
								+ " is " + color + ping2 + ChatColor.GRAY + " ms.");
					} else if (ping2 > 150 && ping < 501) {
						ChatColor color = ChatColor.RED;
						player.sendMessage(serverpre + ChatColor.YELLOW + targetPlayerName + "'s ping" + ChatColor.GRAY
								+ " is " + color + ping2 + ChatColor.GRAY + " ms.");
					} else if (ping2 > 500) {
						ChatColor color = ChatColor.DARK_RED;
						player.sendMessage(serverpre + ChatColor.YELLOW + targetPlayerName + "'s ping" + ChatColor.GRAY
								+ " is " + color + ping2 + ChatColor.GRAY + " ms.");
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "Server>" + ChatColor.GRAY + wrongplayer);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("world") || CommandLabel.equalsIgnoreCase("SMDMain:world")) {
			if (player.isOp() || player.hasPermission("Main.world")) {
				double x = player.getLocation().getX();
				double y = player.getLocation().getY();
				double z = player.getLocation().getZ();
				double pitch = player.getLocation().getPitch();
				double yaw = player.getLocation().getYaw();
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("world")) {
						World p = Bukkit.getWorld("world");
						Location loc = new Location(p, x, y, z);
						loc.setPitch((float) pitch);
						loc.setYaw((float) yaw);
						player.teleport(loc);
						player.sendMessage(serverpre + sendtoworld + ChatColor.GREEN + "world" + ChatColor.GRAY + ".");
						player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					} else if (args[0].equalsIgnoreCase("nether")) {
						World p = Bukkit.getWorld("world_nether");
						Location loc = new Location(p, x, y, z);
						loc.setPitch((float) pitch);
						loc.setYaw((float) yaw);
						player.teleport(loc);
						player.sendMessage(
								serverpre + sendtoworld + ChatColor.GREEN + "world_nether" + ChatColor.GRAY + ".");
						player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					} else if (args[0].equalsIgnoreCase("end")) {
						World p = Bukkit.getWorld("world_the_end");
						Location loc = new Location(p, x, y, z);
						loc.setPitch((float) pitch);
						loc.setYaw((float) yaw);
						player.teleport(loc);
						player.sendMessage(
								serverpre + sendtoworld + ChatColor.GREEN + "world_the_end" + ChatColor.GRAY + ".");
						player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					} else {
						player.sendMessage(serverpre + type + "/world [world|nether|end]");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else if (args.length == 2) {
					if (Bukkit.getServer().getPlayer(args[1]) != null) {
						Player targetPlayer = Bukkit.getServer().getPlayer(args[1]);
						String targetPlayerName = targetPlayer.getName();
						if (args[0].equalsIgnoreCase("world")) {
							World p = Bukkit.getWorld("world");
							Location loc = new Location(p, x, y, z);
							loc.setPitch((float) pitch);
							loc.setYaw((float) yaw);
							targetPlayer.teleport(loc);
							targetPlayer.sendMessage(
									serverpre + sendtoworld + ChatColor.GREEN + "world" + ChatColor.GRAY + ".");
							targetPlayer.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
							player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
							player.sendMessage(serverpre + "You sent player " + ChatColor.YELLOW + targetPlayerName
									+ ChatColor.GRAY + " to world " + ChatColor.GREEN + "world.");
						} else if (args[0].equalsIgnoreCase("nether")) {
							World p = Bukkit.getWorld("world_nether");
							Location loc = new Location(p, x, y, z);
							loc.setPitch((float) pitch);
							loc.setYaw((float) yaw);
							targetPlayer.teleport(loc);
							targetPlayer.sendMessage(
									serverpre + sendtoworld + ChatColor.GREEN + "nether" + ChatColor.GRAY + ".");
							targetPlayer.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
							player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
							player.sendMessage(serverpre + "You sent player " + ChatColor.YELLOW + targetPlayerName
									+ ChatColor.GRAY + " to world " + ChatColor.GREEN + "nether.");
						} else if (args[0].equalsIgnoreCase("end")) {
							World p = Bukkit.getWorld("world_the_end");
							Location loc = new Location(p, x, y, z);
							loc.setPitch((float) pitch);
							loc.setYaw((float) yaw);
							targetPlayer.teleport(loc);
							targetPlayer.sendMessage(
									serverpre + sendtoworld + ChatColor.GREEN + "the end" + ChatColor.GRAY + ".");
							targetPlayer.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
							player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
							player.sendMessage(serverpre + "You sent player " + ChatColor.YELLOW + targetPlayerName
									+ ChatColor.GRAY + " to world " + ChatColor.GREEN + "the end.");
						}
					}
				} else {
					player.sendMessage(serverpre + type + "/world [world|nether|end] [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(serverpre + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("climate") || CommandLabel.equalsIgnoreCase("SMDMain:climate")) {
			if (player.isOp() || player.hasPermission("Main.climate")) {
				World w = ((Player) sender).getWorld();
				if (args.length == 0) {
					if (w.hasStorm() == true) {
						w.setThundering(false);
						w.setStorm(false);
						player.sendMessage(serverpre + "Set weather to " + ChatColor.GOLD + "Sunny");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					} else if (w.hasStorm() == false) {
						w.setThundering(false);
						w.setStorm(true);
						player.sendMessage(serverpre + "Set weather to " + ChatColor.AQUA + "Rain");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					}
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("sun")) {
						w.setThundering(false);
						w.setStorm(false);
						player.sendMessage(serverpre + "Set weather to " + ChatColor.GOLD + "Sunny");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					} else if (args[0].equalsIgnoreCase("storm")) {
						w.setThundering(true);
						w.setStorm(true);
						player.sendMessage(serverpre + "Set weather to " + ChatColor.DARK_AQUA + "Storm");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					} else if (args[0].equalsIgnoreCase("rain")) {
						w.setThundering(false);
						w.setStorm(true);
						player.sendMessage(serverpre + "Set weather to " + ChatColor.AQUA + "Rain");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					} else {
						player.sendMessage(serverpre + type + "/climate [sun/storm/rain]");
						player.sendMessage(
								serverpre + "Or " + ChatColor.GREEN + "/climate (toggle between sun and rain)");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_SNARE, 1, 1);
					}
				}
			} else {
				player.sendMessage(serverpre + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);

			}
		}
		if (CommandLabel.equalsIgnoreCase("tpr") || CommandLabel.equalsIgnoreCase("SMDMain:tpr")
				|| CommandLabel.equalsIgnoreCase("tprequest") || CommandLabel.equalsIgnoreCase("SMDMain:tprequest")) {
			if (args.length == 1) {
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
						targetPlayer.sendMessage(portalpre + "Player " + ChatColor.YELLOW + playerName + ChatColor.GRAY
								+ " sent teleportion request to you");
						targetPlayer.sendMessage(portalpre + ChatColor.GREEN + "/tpaccept " + ChatColor.YELLOW
								+ playerName + ChatColor.GRAY + " to" + ChatColor.GREEN + " accept " + ChatColor.GRAY
								+ "teleportion request.");
						targetPlayer.sendMessage(
								portalpre + ChatColor.RED + "/tpdeny " + ChatColor.YELLOW + playerName + ChatColor.GRAY
										+ " to" + ChatColor.RED + " deny " + ChatColor.GRAY + "teleportion request.");
					}
				} else {
					player.sendMessage(portalpre + wrongplayer);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(portalpre + type + "/tpr [player] - Sent teleportion request to [player]");
			}
		}
		if (CommandLabel.equalsIgnoreCase("tpaccept") || CommandLabel.equalsIgnoreCase("SMDMain:tpaccept")) {
			if (args.length == 1) {
				if (Bukkit.getServer().getPlayer(args[0]) != null) {
					Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
					String targetPlayerName = targetPlayer.getName();
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
		if (CommandLabel.equalsIgnoreCase("mute")) {
			if (player.isOp() || player.hasPermission("main.mute")) {
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
							targetPlayer.sendMessage(serverpre + "You have been muted.");
							targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,
									1);
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
							player.sendMessage(serverpre + "You " + ChatColor.GREEN + "grant " + ChatColor.YELLOW
									+ targetPlayerName + "'s ability " + ChatColor.GRAY + "to chat. ");
							targetPlayer.sendMessage(serverpre + "You have been unmuted.");
							targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,
									1);
							try {
								playerData1.set("mute.is", "false");
								playerData1.set("mute.reason", "none");
								playerData1.save(f1);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} else {
						player.sendMessage(serverpre + wrongplayer);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(serverpre + type + "/mute [player] [reason]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(serverpre + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("warn")) {
			if (player.isOp() || player.hasPermission("Main.warn")) {
				if (args.length > 1) {
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
						for (int i = 1; i != args.length; i++) // i = argrement
																// if start
																// catch args[0]
																// type i = 0
							message += args[i] + " ";
						message = message.replaceAll("&", "§");
						int countnew = countwarn + 1;
						if (countnew == 4) {
							countnew = 3;
							Bukkit.broadcastMessage(serverpre + targetPlayerName + " has been banned");
							Bukkit.broadcastMessage(serverpre + "Reason: " + ChatColor.YELLOW + message);
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
									"ban " + targetPlayerName + " " + message);
						} else {
							Bukkit.broadcastMessage(
									serverpre + targetPlayerName + " has been warned (" + countnew + ")");
							Bukkit.broadcastMessage(serverpre + "Reason: " + ChatColor.YELLOW + message);
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
						player.sendMessage(serverpre + wrongplayer);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(serverpre + type + "/warn [player] [reason]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(serverpre + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("resetwarn")) {
			if (player.isOp() || player.hasPermission("Main.warn")) {
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
						Bukkit.broadcastMessage(serverpre + ChatColor.YELLOW + playerName + ChatColor.GRAY + " reset "
								+ targetPlayerName + "'s warned (0)");
						try {
							playerData1.set("warn", "0");
							playerData1.save(f1);
						} catch (IOException e) {
							e.printStackTrace();
						}
						for (Player p : Bukkit.getOnlinePlayers()) {
							p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
						}
					} else {
						player.sendMessage(serverpre + wrongplayer);
					}
				} else {
					player.sendMessage(serverpre + type + "/resetwarn [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(serverpre + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("rank")) {
			if (player.isOp() || player.hasPermission("Main.rank")) {
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
						player.sendMessage(serverpre + ChatColor.RED + wrongplayer);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(
							ChatColor.BLUE + "Rank> " + type + "/rank [default|vip|mvp|staff|admin|owner] [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(serverpre + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("status") || CommandLabel.equalsIgnoreCase("SMDMain:status")) {
			player.sendMessage(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "----[" + ChatColor.WHITE + "STATS"
					+ ChatColor.YELLOW + ChatColor.STRIKETHROUGH + "]----");
			player.sendMessage(ChatColor.WHITE + "Mute: ");
			String muteis = playerData.getString("mute.is");
			String mutere = playerData.getString("mute.reason");
			String freeze = playerData.getString("freeze");
			int countwarn = playerData.getInt("warn");
			if (muteis.equalsIgnoreCase("true")) {
				player.sendMessage(ChatColor.WHITE + "  Status: " + ChatColor.RED + "TRUE");
				player.sendMessage(ChatColor.WHITE + "  Reason: " + ChatColor.YELLOW + mutere);
			}
			if (muteis.equalsIgnoreCase("false")) {
				player.sendMessage(ChatColor.WHITE + "  Status: " + ChatColor.GREEN + "FALSE");
				player.sendMessage(ChatColor.WHITE + "  Reason: " + ChatColor.GRAY + "You didn't get mute.");
			}
			player.sendMessage(ChatColor.WHITE + "Warn: " + ChatColor.YELLOW + countwarn + "/3 Left.");
			player.sendMessage(ChatColor.WHITE + "Rank: " + ChatColor.YELLOW + rank.toUpperCase());
			if (freeze.equalsIgnoreCase("true")) {
				player.sendMessage(ChatColor.WHITE + "Freeze: " + ChatColor.RED + "TRUE");
			}
			if (freeze.equalsIgnoreCase("false")) {
				player.sendMessage(ChatColor.WHITE + "Freeze: " + ChatColor.GREEN + "FALSE");
			}
		}
		if (CommandLabel.equalsIgnoreCase("wiki") || CommandLabel.equalsIgnoreCase("SMDMain:wiki")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("")) {

				} else if (args[0].equalsIgnoreCase("")) {

				} else if (args[0].equalsIgnoreCase("")) {

				} else {
					player.sendMessage(ChatColor.BLUE + "Wiki> " + ChatColor.GRAY + "Topic " + ChatColor.YELLOW
							+ args[0] + ChatColor.GRAY + " not found!");
				}
			} else {
				player.sendMessage(ChatColor.BLUE + "Wiki> " + ChatColor.GRAY + "Welcome to " + ChatColor.GREEN
						+ ChatColor.BOLD + "WIKI - The Information center");
				player.sendMessage(ChatColor.BLUE + "Wiki> " + ChatColor.GREEN + "Available Topic: " + ChatColor.GRAY
						+ "No Topic");
				player.sendMessage(ChatColor.BLUE + "Wiki> " + ChatColor.GRAY + "Please choose your topic by type: "
						+ ChatColor.YELLOW + "/wiki [topic]");
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
					player.sendMessage("That account not found.");
				}
				if (f1.exists()) {
					player.sendMessage(serverpre + "Account found");
					if (UUID.equalsIgnoreCase(playerData1.getString("uuid"))) {
						try {
							String rank1 = playerData1.getString("rank");
							int warn1 = playerData1.getInt("warn");
							String muteis = playerData1.getString("mute.is");
							String mutere = playerData1.getString("mute.reason");
							String freeze = playerData1.getString("freeze");
							if (playerData1.getString("home") != null) {
								double x = playerData1.getDouble("home.x");
								double y = playerData1.getDouble("home.y");
								double z = playerData1.getDouble("home.z");
								double pitch = playerData1.getDouble("home.pitch");
								double yaw = playerData1.getDouble("home.yaw");
								String world = playerData1.getString("home.world");
								playerData1.createSection("rank");
								playerData1.set("rank", rank1);
								playerData1.createSection("warn");
								playerData1.set("warn", warn1);
								playerData1.createSection("mute");
								playerData1.set("mute.is", muteis);
								playerData1.set("mute.reason", mutere);
								playerData1.createSection("freeze");
								playerData1.set("freeze", freeze);
								playerData1.createSection("home");
								playerData1.set("home.x", x);
								playerData1.set("home.y", y);
								playerData1.set("home.z", z);
								playerData1.set("home.pitch", pitch);
								playerData1.set("home.yaw", yaw);
								playerData1.set("home.world", world);
							} else {
								playerData1.createSection("rank");
								playerData1.set("rank", rank1);
								playerData1.createSection("warn");
								playerData1.set("warn", warn1);
								playerData1.createSection("mute");
								playerData1.set("mute.is", muteis);
								playerData1.set("mute.reason", mutere);
								playerData1.createSection("freeze");
								playerData1.set("freeze", freeze);
							}
							// playerData1.set("home", null);
							playerData1.save(f1);
						} catch (IOException e) {
							e.printStackTrace();
						}
						player.sendMessage("same_uuid");
						player.sendMessage("print_copy_complete");
					} else {
						player.sendMessage("not_same_uuid");
					}
				}
			} else {
				player.sendMessage("you_need_to_type");
			}
		}
		if (CommandLabel.equalsIgnoreCase("freeze") || CommandLabel.equalsIgnoreCase("SMDMain:freeze")) {
			if (player.isOp() || player.hasPermission("main.freeze")) {
				if (args.length == 1) {
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
							player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 0);
							targetPlayer.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 0);
							player.sendMessage(serverpre + "You " + ChatColor.GREEN + "grant " + ChatColor.YELLOW
									+ targetPlayerName + "'s ability " + ChatColor.GRAY + "to move.");
							targetPlayer.setAllowFlight(false);
						}
						if (freeze.equalsIgnoreCase("false")) {
							if (args[0].equalsIgnoreCase("SMD_SSG_PJ")) {
								player.sendMessage("You can't freeze SMD_SSG_PJ because he is develoepr 5555555+");
							} else {
								try {
									playerData1.set("freeze", "true");
									playerData1.save(f1);
								} catch (IOException e) {
									e.printStackTrace();
								}
								player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 0);
								targetPlayer.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
								player.sendMessage(serverpre + "You " + ChatColor.RED + "revoke " + ChatColor.YELLOW
										+ targetPlayerName + "'s ability " + ChatColor.GRAY + "to move.");
								targetPlayer.setAllowFlight(true);
							}
						}
					}
				} else {
					player.sendMessage(serverpre + type + "/freeze [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(serverpre + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("resetredeem") || CommandLabel.equalsIgnoreCase("SMDMain:resetredeem")) {
			if (player.isOp() || player.hasPermission("main.resetredeem")) {
				for (Player p : Bukkit.getOnlinePlayers()) {
					String pl = p.getName();
					getConfig().set("redeem." + pl, "false");
					saveConfig();
				}
				player.sendMessage(serverpre + ChatColor.GREEN + "Reset redeem complete.");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
			} else {
				player.sendMessage(serverpre + noperm);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("redeem") || CommandLabel.equalsIgnoreCase("SMDMain:redeem")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("SURVEY_DIA")) {
					if (getConfig().getString("redeem." + playerName) == null
							|| getConfig().getString("redeem." + playerName).equalsIgnoreCase("false")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " diamond 10");
						player.sendMessage("");
						player.sendMessage(ChatColor.GREEN + "Thank you for anwser our survey");
						player.sendMessage(ChatColor.GREEN + "Here is your reward!");
						player.sendMessage(ChatColor.YELLOW + "10x " + ChatColor.AQUA + "DIAMOND");
						player.sendMessage("");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
						getConfig().set("redeem." + playerName, "true");
						saveConfig();
					} else {
						player.sendMessage("You already earn reward from this code.");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(serverpre + "Your redeem code is incorrect!");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(serverpre + type + "/redeem [code]");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("reconfig") || CommandLabel.equalsIgnoreCase("SMDMain:reconfig")) {
			if (player.isOp() || player.hasPermission("main.reconfig")) {
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
							playerData1.set("warn", "0");
							playerData1.createSection("mute");
							playerData1.set("mute.is", "false");
							playerData1.set("mute.reason", "none");
							playerData1.set("home", null);
							playerData1.createSection("uuid");
							playerData1.set("uuid", targetPlayerUUID);
							playerData1.save(f1);
						} catch (IOException e) {
							e.printStackTrace();
						}
						Bukkit.broadcastMessage(serverpre + "Player " + ChatColor.YELLOW + targetPlayerName
								+ "'s information " + ChatColor.GRAY + "has been " + ChatColor.RED + "reset "
								+ ChatColor.GRAY + "by " + ChatColor.AQUA + playerName + ".");
						for (Player p : Bukkit.getOnlinePlayers()) {
							p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 2, 2);
						}
					} else {
						player.sendMessage(serverpre + wrongplayer);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(serverpre + type + "/resetconfig [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(serverpre + noperm);
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
		player.sendMessage("§a§lNEWS§f: หากเปลี่ยนชื่อใหม่ ให้พิมพ์ §a/call [ชื่อเดิม] §fเพื่อดึงข้อมูลจากชื่อเดิม");
		player.sendMessage("");
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		if (!f.exists()) {
			try {
				playerData.createSection("rank");
				playerData.set("rank", "default");
				playerData.createSection("warn");
				playerData.set("warn", "0");
				playerData.createSection("mute");
				playerData.set("mute.is", "false");
				playerData.set("mute.reason", "none");
				playerData.createSection("freeze");
				playerData.set("freeze", "false");
				playerData.createSection("uuid");
				playerData.set("uuid", player.getUniqueId().toString());
				getConfig().set("redeem." + playerName, "false");
				saveConfig();
				playerData.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (player.isOp() || player.hasPermission("main.see")) {
			player.sendMessage("§aYou have permission 'main.see', You will see command that player using automatic.");
		}
		if (getConfig().getString("redeem." + playerName) == null) {
			getConfig().set("redeem." + playerName, "false");
			saveConfig();
		}
		if (f.exists()) {
			try {
				playerData.createSection("uuid");
				playerData.set("uuid", player.getUniqueId().toString());
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
		String message1 = ChatColor.WHITE + " " + event.getMessage();
		message1 = message1.replaceAll("&", "§");
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
	}

	public void playCircularEffect(Location location, Effect effect, boolean v) {
		for (int i = 0; i <= 8; i += ((!v && (i == 3)) ? 2 : 1))
			location.getWorld().playEffect(location, effect, i);
	}
}
