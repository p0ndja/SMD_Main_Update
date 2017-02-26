package me.palapon2545;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.print.attribute.standard.Finishings;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.*;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import me.palapon2545.pluginMain;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class pluginMain extends JavaPlugin implements Listener {

	public HashMap<String, Long> cooldowns = new HashMap<String, Long>();
	private static int zeit = 60;
	private static boolean sound = false;
	private int countdown;
	private final Material Material = null;
	public final Logger logger = Logger.getLogger("Minecraft");
	public pluginMain plugin;
	ArrayList<UUID> register = new ArrayList<UUID>();
	ArrayList<UUID> login = new ArrayList<UUID>();
	ArrayList<UUID> finish = new ArrayList<UUID>();
	List<String> bannedFromChatlogin = new ArrayList<String>();
	List<String> bannedFromChatregister = new ArrayList<String>();
	List<String> frozenlogin = new ArrayList<String>();
	List<String> frozenregister = new ArrayList<String>();
	List<String> blocklogin = new ArrayList<String>();
	List<String> blockregister = new ArrayList<String>();
	List<String> blockplogin = new ArrayList<String>();
	List<String> blockpregister = new ArrayList<String>();

	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Has Been Disable! ");
		saveConfig();
		Bukkit.broadcastMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Main System: " + ChatColor.RED
				+ ChatColor.BOLD + "Disable");
		for (Player player1 : Bukkit.getOnlinePlayers()) {
			player1.playSound(player1.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 0);

		}
	}

	public void onEnable() {
		Bukkit.broadcastMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Main System: " + ChatColor.GREEN
				+ ChatColor.BOLD + "Enable");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		getConfig().options().copyDefaults(true);
		saveConfig();
		for (Player player1 : Bukkit.getOnlinePlayers()) {
			player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
		}

	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		Player player = (Player) sender;
		String message = "";
		String m[] = message.split("\\s+");
		// UserData
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("Main").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		// Setspawn
		if (CommandLabel.equalsIgnoreCase("setspawn") || CommandLabel.equalsIgnoreCase("ss")
				|| CommandLabel.equalsIgnoreCase("Main:ss") || CommandLabel.equalsIgnoreCase("Main:setspawn")) {
			if (player.isOp() || player.hasPermission("main.setspawn")) {
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
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		// Spawn
		if (CommandLabel.equalsIgnoreCase("spawn") || CommandLabel.equalsIgnoreCase("ts")
				|| CommandLabel.equalsIgnoreCase("Main:spawn") || CommandLabel.equalsIgnoreCase("Main:ts")) {
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
				player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Teleported to " + ChatColor.YELLOW
						+ "Spawn" + ChatColor.GRAY + ".");
				player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
			} else {
				player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Spawn location not found!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);

			}
		}
		// sethome

		if (CommandLabel.equalsIgnoreCase("sethome") || CommandLabel.equalsIgnoreCase("sh")
				|| CommandLabel.equalsIgnoreCase("Main:sh") || CommandLabel.equalsIgnoreCase("Main:sethome")) {
			if (f.exists()) {
				if (playerData.getString("home") != null) {
					// SethomeäÁèä´é #Setä»áÅéÇ
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.RED + "Reach maximum sethome.");
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Allow Sethome:"
							+ ChatColor.YELLOW + " 1 home(s)");
					player.sendMessage(
							ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "You need to remove your old house first");
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Type: " + ChatColor.YELLOW
							+ "/removehome , /rh");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
				}
				else {
					// Sethomeä´é
					if (args.length == 0) {
						Location pl = player.getLocation();
						double plx = pl.getX();
						double ply = pl.getY();
						double plz = pl.getZ();
						double plpitch = pl.getPitch();
						double plyaw = pl.getYaw();
						double x = Math.floor(plx);
						double y = Math.floor(ply);
						double z = Math.floor(plz);
						String plw = pl.getWorld().getName();
						try {
							playerData.createSection("home");
							playerData.set("home.world", plw);
							playerData.set("home.x", plx);
							playerData.set("home.y", ply);
							playerData.set("home.z", plz);
							playerData.set("home.pitch", plpitch);
							playerData.set("home.yaw", plyaw);
							playerData.save(f);
						} catch (IOException exception) {
							exception.printStackTrace();
						}
						player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Sethome complete.");
						player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "At location "
								+ ChatColor.GREEN + "X: " + x + ChatColor.AQUA + " Y:" + y + ChatColor.YELLOW + " Z:"
								+ z + ChatColor.LIGHT_PURPLE + " at World " + ChatColor.GOLD + plw);
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					} else if (args.length == 1) {
						Location pl = player.getLocation();
						double plx = pl.getX();
						double ply = pl.getY();
						double plz = pl.getZ();
						double plpitch = pl.getPitch();
						double plyaw = pl.getYaw();
						String plw = pl.getWorld().getName();
						try {
							playerData.createSection("home");
							playerData.set("home.world", plw);
							playerData.set("home.x", plx);
							playerData.set("home.y", ply);
							playerData.set("home.z", plz);
							playerData.set("home.pitch", plpitch);
							playerData.set("home.yaw", plyaw);
							playerData.save(f);
						} catch (IOException exception) {
							exception.printStackTrace();
						}
						player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Sethome complete.");
						player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "At location "
								+ ChatColor.GREEN + "X: " + plx + ChatColor.AQUA + " Y:" + ply + ChatColor.YELLOW
								+ " Z:" + plz + ChatColor.LIGHT_PURPLE + " at World" + ChatColor.GOLD + plw);
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					}
				}
			}
		}
		// home
		if (CommandLabel.equalsIgnoreCase("home") || CommandLabel.equalsIgnoreCase("h")
				|| CommandLabel.equalsIgnoreCase("Main:home") || CommandLabel.equalsIgnoreCase("Main:h")) {
			if (f.exists()) {
				if (playerData.getString("home") != null) {
					// Homeä´é
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
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Teleported to "
							+ ChatColor.YELLOW + "Home" + ChatColor.GRAY + ".");
					player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
				}
				else {
					// HomeäÁèä´é
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "You didn't sethome yet.");
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Type: /sethome , /sh first.");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		// list location of home
		if (CommandLabel.equalsIgnoreCase("locationhome") || CommandLabel.equalsIgnoreCase("lh")
				|| CommandLabel.equalsIgnoreCase("Main:locationhome") || CommandLabel.equalsIgnoreCase("Main:lh")) {
			if (f.exists()) {
				if (playerData.getString("home") != null) {
					// Homeä´é
					int x = playerData.getInt("home.x");
					int y = playerData.getInt("home.y");
					int z = playerData.getInt("home.z");
					int pitch = playerData.getInt("home.pitch");
					int yaw = playerData.getInt("home.yaw");
					String world = playerData.getString("home.world");
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.AQUA + "Home info");
					player.sendMessage(
							ChatColor.BLUE + "Portal> " + ChatColor.YELLOW + "World: " + ChatColor.GREEN + world);
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.YELLOW + "Location: ");
					player.sendMessage(
							ChatColor.BLUE + "Portal> " + ChatColor.YELLOW + "  - X: " + ChatColor.GREEN + x);
					player.sendMessage(
							ChatColor.BLUE + "Portal> " + ChatColor.YELLOW + "  - Y: " + ChatColor.GREEN + y);
					player.sendMessage(
							ChatColor.BLUE + "Portal> " + ChatColor.YELLOW + "  - Z: " + ChatColor.GREEN + z);
					player.sendMessage(
							ChatColor.BLUE + "Portal> " + ChatColor.YELLOW + "Yaw: " + ChatColor.GREEN + yaw);
					player.sendMessage(
							ChatColor.BLUE + "Portal> " + ChatColor.YELLOW + "Pitch: " + ChatColor.GREEN + pitch);
				}
				else {
					// HomeäÁèä´é
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "You didn't sethome yet.");
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Type: /sethome , /sh first.");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		// removehome
		if (CommandLabel.equalsIgnoreCase("removehome") || CommandLabel.equalsIgnoreCase("rh")
				|| CommandLabel.equalsIgnoreCase("Main:rh") || CommandLabel.equalsIgnoreCase("Main:removehome")) {
			if (f.exists()) {
				if (playerData.getString("home") != null) {
					try {
						playerData.set("home", null);
						playerData.save(f);
					} catch (IOException exception) {
						exception.printStackTrace();
					}
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Remove home complete.");
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 10, 1);
				} else {
					player.sendMessage(
							ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Couldn't find you home information.");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("warp") || CommandLabel.equalsIgnoreCase("Main:warp")) {
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
				player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Teleported to " + ChatColor.YELLOW
						+ "Server Warp" + ChatColor.GRAY + ".");
				player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
			} else if (warp == null) {
				player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.YELLOW + "Server Warp " + ChatColor.GRAY
						+ "isn't open");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
			}
		}
		if (CommandLabel.equalsIgnoreCase("setwarp") || CommandLabel.equalsIgnoreCase("Main:setwarp")) {
			if (player.isOp() || player.hasPermission("main.warp")) {
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
						player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Set server warp complete.");
						for (Player player1 : Bukkit.getOnlinePlayers()) {
							player1.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.YELLOW + pls + ChatColor.GREEN
									+ " open" + ChatColor.LIGHT_PURPLE + " server warp!");
							player1.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Type: /warp to warp.");
							player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 0);
						}
					}
				}
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);

			}
		}
		if (CommandLabel.equalsIgnoreCase("removewarp") || CommandLabel.equalsIgnoreCase("Main:removewarp")) {
			if (player.isOp() || player.hasPermission("main.warp")) {
				if (f.exists()) {
					if (getConfig().getConfigurationSection("warp") != null) {
						getConfig().set("warp", null);
						player.sendMessage(
								ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Remove server warp complete.");
						for (Player player1 : Bukkit.getOnlinePlayers()) {
							player1.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.RED + "Closed"
									+ ChatColor.LIGHT_PURPLE + " Server Warp.");
							player1.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 10, 1);

						}

					} else if (getConfig().getConfigurationSection("warp") == null) {
						player.sendMessage(
								ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "You didn't set server warp yet.");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);

					}
				}

			}
		}
		if (CommandLabel.equalsIgnoreCase("gamemode") || CommandLabel.equalsIgnoreCase("Main:gamemode")
				|| CommandLabel.equalsIgnoreCase("gm") || CommandLabel.equalsIgnoreCase("Main:gm")) {
			if (player.isOp() || player.hasPermission("main.gamemode")) {
				if (args.length != 1) {
					player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.YELLOW + "Type: " + ChatColor.GREEN
							+ "/gamemode [mode] -or- /gm [mode]");
					player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.YELLOW + "Mode: ");
					player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.WHITE + "- " + ChatColor.GREEN
							+ "Survival , S , 0");
					player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.WHITE + "- " + ChatColor.GREEN
							+ "Creative , C , 1");
					player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.WHITE + "- " + ChatColor.GREEN
							+ "Adventure , A , 2");
					player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.WHITE + "- " + ChatColor.GREEN
							+ "Spectator , SP , 3");
				}
				if (args.length == 1) {
					if ((args[0].equalsIgnoreCase("1")) || (args[0].equalsIgnoreCase("c"))
							|| (args[0].equalsIgnoreCase("creative"))) {
						player.setGameMode(GameMode.CREATIVE);
						player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.GRAY
								+ "Your gamemode has been updated to " + ChatColor.GREEN + "Creative.");
						player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
					} else if ((args[0].equalsIgnoreCase("0")) || (args[0].equalsIgnoreCase("s"))
							|| (args[0].equalsIgnoreCase("survival"))) {
						player.setGameMode(GameMode.SURVIVAL);
						player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.GRAY
								+ "Your gamemode has been updated to " + ChatColor.YELLOW + "Survival.");
						player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
					} else if ((args[0].equalsIgnoreCase("2")) || (args[0].equalsIgnoreCase("a"))
							|| (args[0].equalsIgnoreCase("adventure"))) {
						player.setGameMode(GameMode.ADVENTURE);
						player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.GRAY
								+ "Your gamemode has been updated to " + ChatColor.LIGHT_PURPLE + "Adventure.");
						player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
					} else if ((args[0].equalsIgnoreCase("3")) || (args[0].equalsIgnoreCase("sp"))
							|| (args[0].equalsIgnoreCase("spectator"))) {
						player.setGameMode(GameMode.SPECTATOR);
						player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.GRAY
								+ "Your gamemode has been updated to " + ChatColor.AQUA + "Spectator.");
						player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
					}
				} else if (!player.isOp()) {
					player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.YELLOW + "You don't have "
							+ ChatColor.RED + "OP!");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("heal") || CommandLabel.equalsIgnoreCase("Main:heal")) {
			if (player.isOp() || player.hasPermission("main.heal")) {
				if (args.length == 0) {
					player.setFoodLevel(40);
					for (PotionEffect Effect : player.getActivePotionEffects()) {
						player.removePotionEffect(Effect.getType());
					}
					player.setHealth(20);
					player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.LIGHT_PURPLE + "You have been healed!");
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
				}
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);

			}
		}
		if (CommandLabel.equalsIgnoreCase("fly") || CommandLabel.equalsIgnoreCase("Main:fly")) {
			if (player.isOp() || player.hasPermission("main.fly")) {
				if (player.getAllowFlight() == false) {
					player.setAllowFlight(true);
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
					player.sendMessage(
							ChatColor.BLUE + "Fly> " + ChatColor.YELLOW + "Now you're " + ChatColor.RED + "flyable");
				} else if (player.getAllowFlight() == true) {
					player.setAllowFlight(false);
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 0);
					player.sendMessage(ChatColor.BLUE + "Fly> " + ChatColor.YELLOW + "Now you're " + ChatColor.RED
							+ "no longer flyable");
				}
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);

			}
		}
		if (CommandLabel.equalsIgnoreCase("stuck") || CommandLabel.equalsIgnoreCase("Main:stuck")) {
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
			player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.YELLOW + "You have been resend your location.");
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
		}
		if (CommandLabel.equalsIgnoreCase("day") || CommandLabel.equalsIgnoreCase("Main:day")) {
			if (player.isOp() || player.hasPermission("main.time")) {
				World w = ((Player) sender).getWorld();
				player.sendMessage(ChatColor.BLUE + "Time> " + ChatColor.GRAY + "Set time to " + ChatColor.GOLD + "Day "
						+ ChatColor.GRAY + ChatColor.ITALIC + "(1000 ticks");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(1000);
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("midday") || CommandLabel.equalsIgnoreCase("Main:midday")) {
			if (player.isOp() || player.hasPermission("main.time")) {
				player.sendMessage(ChatColor.BLUE + "Time> " + ChatColor.GRAY + "Set time to " + ChatColor.GOLD
						+ "Midday " + ChatColor.GRAY + ChatColor.ITALIC + "(6000 ticks");
				World w = ((Player) sender).getWorld();
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(6000);
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("night") || CommandLabel.equalsIgnoreCase("Main:night")) {
			if (player.isOp() || player.hasPermission("main.time")) {
				World w = ((Player) sender).getWorld();
				player.sendMessage(ChatColor.BLUE + "Time> " + ChatColor.GRAY + "Set time to " + ChatColor.GOLD
						+ "Night " + ChatColor.GRAY + ChatColor.ITALIC + "(13000 ticks");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(13000);
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("midnight") || CommandLabel.equalsIgnoreCase("Main:midnight")) {
			if (player.isOp() || player.hasPermission("main.time")) {
				World w = ((Player) sender).getWorld();
				player.sendMessage(ChatColor.BLUE + "Time> " + ChatColor.GRAY + "Set time to " + ChatColor.GOLD
						+ "Midnight " + ChatColor.GRAY + ChatColor.ITALIC + "(18000 ticks");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(18000);
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("bc") || CommandLabel.equalsIgnoreCase("Main:skev")) {
			String text = args[0].replaceAll("_", " ");
			String text1 = text.replaceAll("&", "§");
			if (args.length == 0) {
				player.sendMessage("/bc <text> (Using _ for spacing)");
			} else if (args.length != 0) {
				if (player.isOp() || player.hasPermission("main.admin")) {
					Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Broadcast> " + ChatColor.WHITE + text1);
					for (Player player1 : Bukkit.getOnlinePlayers()) {
						player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					}
				}
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		/*
		 * if (CommandLabel.equalsIgnoreCase("tpr")) { if (args.length == 0) {
		 * player.sendMessage(ChatColor.BLUE + "Teleport> " + ChatColor.GREEN +
		 * "Type: /tpr [player]"); player.sendMessage(ChatColor.BLUE +
		 * "Teleport> " + ChatColor.GREEN + "อัตราราคา: " + ChatColor.YELLOW +
		 * "1,000 บล๊อกแรกฟรี! " + ChatColor.AQUA +
		 * "หากเกินมา ทุก ๆ 1,000 บล๊อก = 1 Compass");
		 * player.sendMessage(ChatColor.BLUE + "Teleport> " + ChatColor.WHITE +
		 * ChatColor.BOLD + "BETA! " + ChatColor.GREEN +
		 * "ตอนนี้เปิดให้ใช้งานฟรี~!"); } if (args.length == 1) { Player target
		 * = Bukkit.getPlayer(args[0]); String targetName =
		 * Bukkit.getPlayer(args[0]).getName(); if (target.isOnline()) { if
		 * (player.getWorld() == target.getWorld()) {
		 * getConfig().createSection("Player"); getConfig().set("Teleport." +
		 * target, player); saveConfig(); } if (player.getWorld() !=
		 * target.getWorld()) { player.sendMessage(ChatColor.BLUE + "Teleport> "
		 * + ChatColor.RED + "Player " + targetName +
		 * " isn't stay in same world with you!");
		 * player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
		 * } } else if (target.isOnline() == false || target == null ||
		 * target.isEmpty()) { player.sendMessage( ChatColor.BLUE + "Teleport> "
		 * + ChatColor.RED + "Player " + targetName + " not found!");
		 * player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
		 * } } } if (CommandLabel.equalsIgnoreCase("tpaccept")) { String
		 * IsRequest = getConfig().getString("Teleport." + player); if
		 * (IsRequest == args[0]) { Player target = Bukkit.getPlayer(args[0]);
		 * String targetName = Bukkit.getPlayer(args[0]).getName(); Location
		 * Before = player.getLocation(); Location After = target.getLocation();
		 * Double Distance = player.getLocation().distance(After); Double
		 * Distanced = Math.floor(Distance); Location pl = target.getLocation();
		 * double x = pl.getX(); double y = (pl.getY()); double z = pl.getZ();
		 * double pitch = pl.getPitch(); double yaw = pl.getYaw(); World p =
		 * pl.getWorld(); Location loc = new Location(p, x, y, z);
		 * loc.setPitch((float) pitch); loc.setYaw((float) yaw);
		 * target.teleport(loc); target.sendMessage(ChatColor.BLUE +
		 * "Teleport> " + ChatColor.GREEN + "You teleported to " +
		 * ChatColor.YELLOW + targetName); target.sendMessage(ChatColor.BLUE +
		 * "Teleport> " + ChatColor.GREEN + "Distance: " + ChatColor.YELLOW +
		 * Distanced); Double Price = (Distanced / 1000); Double Priced =
		 * Math.floor(Price); if (Priced <= 0) {
		 * target.sendMessage(ChatColor.BLUE + "Teleport> " + ChatColor.GREEN +
		 * "Price: " + ChatColor.YELLOW + " FREE (0 Compass(s))"); } if (Priced
		 * > 0) { target.sendMessage(ChatColor.BLUE + "Teleport> " +
		 * ChatColor.GREEN + "Price: " + ChatColor.YELLOW + Priced +
		 * " Compass(s)"); } target.playSound(player.getLocation(),
		 * Sound.BLOCK_NOTE_BASS, 1, 0); getConfig().set("Teleport." + player,
		 * ""); saveConfig(); } else { player.sendMessage("Else"); } } if
		 * (CommandLabel.equalsIgnoreCase("tpdeny")) {
		 * 
		 * }
		 */
		if (CommandLabel.equalsIgnoreCase("climate") || CommandLabel.equalsIgnoreCase("Main:climate")) {
			if (player.isOp() || player.hasPermission("main.time")) {
				World w = ((Player) sender).getWorld();
				if (args.length == 0) {
					if (w.hasStorm() == true) {
						w.setThundering(false);
						w.setStorm(false);
						player.sendMessage(ChatColor.BLUE + "Weather> " + ChatColor.GRAY + "Set weather to "
								+ ChatColor.GOLD + "Sunny");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					} else if (w.hasStorm() == false) {
						w.setThundering(false);
						w.setStorm(true);
						player.sendMessage(ChatColor.BLUE + "Weather> " + ChatColor.GRAY + "Set weather to "
								+ ChatColor.AQUA + "Rain");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					}
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("sun")) {
						w.setThundering(false);
						w.setStorm(false);
						player.sendMessage(ChatColor.BLUE + "Weather> " + ChatColor.GRAY + "Set weather to "
								+ ChatColor.GOLD + "Sunny");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					} else if (args[0].equalsIgnoreCase("storm")) {
						w.setThundering(true);
						w.setStorm(true);
						player.sendMessage(ChatColor.BLUE + "Weather> " + ChatColor.GRAY + "Set weather to "
								+ ChatColor.DARK_AQUA + "Storm");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					} else if (args[0].equalsIgnoreCase("rain")) {
						w.setThundering(false);
						w.setStorm(true);
						player.sendMessage(ChatColor.BLUE + "Weather> " + ChatColor.GRAY + "Set weather to "
								+ ChatColor.AQUA + "Rain");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					} else {
						player.sendMessage(ChatColor.BLUE + "Weather> " + ChatColor.GRAY + "Type: " + ChatColor.YELLOW
								+ "/climate [sun/storm/rain]");
						player.sendMessage(ChatColor.BLUE + "Weather> " + ChatColor.GRAY + "Or " + ChatColor.YELLOW
								+ "/climate (toggle between sun and rain)");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_SNARE, 1, 1);
					}
				}
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);

			}
		}
		return true;
	}

	@EventHandler
	public void JoinServer(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		// UserData
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("Main").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		if (!f.exists()) {
			try {
				/*
				 * playerData.createSection("group");
				 * playerData.set("group.group", "default");
				 */
				playerData.save(f);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		event.setJoinMessage(playerName + ChatColor.YELLOW + " joined the game!");
	}

	@EventHandler
	public void QuitServer(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		finish.remove(player.getUniqueId());
		event.setQuitMessage(player.getName() + ChatColor.YELLOW + " left the game!");
	}

	// warp
	@EventHandler
	public void chackbeforewarp(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		loc.setY(loc.getY());
		Block block = loc.getBlock();
		if (block.getType() == Material.GOLD_PLATE) {
			Location loc2 = player.getLocation();
			loc2.setY(loc.getY() - 2);
			Block block2 = loc2.getBlock();
			if ((block2.getType() == Material.SIGN_POST) || (block2.getType() == Material.WALL_SIGN)) {
				Sign sign = (Sign) block2.getState();
				if (sign.getLine(0).equalsIgnoreCase("[tp]")) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 10));
					ActionBar action = new ActionBar(ChatColor.YELLOW + "" + ChatColor.BOLD + "Hold" + ChatColor.GREEN
							+ "" + ChatColor.BOLD + " >>Shift<< " + ChatColor.AQUA + "To Teleport");
					action.sendToPlayer(player);
					// player.sendMessage(ChatColor.YELLOW+""+ChatColor.BOLD+"Hold"+ChatColor.GREEN+""+ChatColor.BOLD+"
					// >>Shift<< "+ChatColor.AQUA+"To Teleport");
				}
			}
		}
		if (block.getType() == Material.GOLD_PLATE) {
			Location loc2 = player.getLocation();
			loc2.setY(loc.getY() - 2);
			Block block2 = loc2.getBlock();
			if ((block2.getType() == Material.SIGN_POST) || (block2.getType() == Material.WALL_SIGN)) {
				Sign sign = (Sign) block2.getState();
				if (sign.getLine(0).equalsIgnoreCase("[cmd]")) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 10));
					ActionBar action = new ActionBar(ChatColor.YELLOW + "" + ChatColor.BOLD + "Hold" + ChatColor.GREEN
							+ "" + ChatColor.BOLD + " >>Shift<< " + ChatColor.AQUA + "To Teleport");
					action.sendToPlayer(player);
					// player.sendMessage(ChatColor.YELLOW+""+ChatColor.BOLD+"Hold"+ChatColor.GREEN+""+ChatColor.BOLD+"
					// >>Shift<< "+ChatColor.AQUA+"To Teleport");
				}
			}
		}
	}

	@EventHandler
	public void warping(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		loc.setY(loc.getY());
		Block block = loc.getBlock();
		if (block.getType() == Material.GOLD_PLATE) {
			Location loc2 = player.getLocation();
			loc2.setY(loc.getY() - 2);
			Block block2 = loc2.getBlock();
			if ((block2.getType() == Material.SIGN_POST) || (block2.getType() == Material.WALL_SIGN)) {
				Sign sign = (Sign) block2.getState();
				if (sign.getLine(0).equalsIgnoreCase("[tp]")) {

					int xh = Integer.parseInt(sign.getLine(1));
					int yh = Integer.parseInt(sign.getLine(2));
					int zh = Integer.parseInt(sign.getLine(3));

					Location loca = player.getLocation();
					loca.setX(xh);
					loca.setY(yh);
					loca.setZ(zh);

					player.teleport(loca);
				}
			}
			if ((block2.getType() == Material.SIGN_POST) || (block2.getType() == Material.WALL_SIGN)) {
				Sign sign = (Sign) block2.getState();
				if (sign.getLine(0).equalsIgnoreCase("[cmd]")) {
					String cmd = sign.getLine(1);
					player.performCommand(cmd);
				}
			}
		}
	}

	public void playCircularEffect(Location location, Effect effect, boolean v) {
		for (int i = 0; i <= 8; i += ((!v && (i == 3)) ? 2 : 1))
			location.getWorld().playEffect(location, effect, i);
	}
}
