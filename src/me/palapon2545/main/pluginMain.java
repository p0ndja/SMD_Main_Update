package me.palapon2545.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;

import me.palapon2545.main.pluginMain;
import me.palapon2545.main.DelayLoadConfig;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;

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
	public static DelayLoadConfig delayLoadConfig = null;
	public static Thread delayLoadConnfig_Thread = null;
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
	LinkedList<String> badWord = new LinkedList<String>();

	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Has Been Disable! ");
		saveConfig();
		Bukkit.broadcastMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "SMDMain System: " + ChatColor.RED
				+ ChatColor.BOLD + "Disable");
		for (Player player1 : Bukkit.getOnlinePlayers()) {
			player1.playSound(player1.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 0);
		}

		delayLoadConfig.setRunning(false);

	}

	public void onEnable() {
		PluginDescriptionFile file = this.getDescription();
		String version = file.getVersion();
		Bukkit.broadcastMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "SMDMain System: " + ChatColor.GREEN
				+ ChatColor.BOLD + "Enable");
		Bukkit.broadcastMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Server patch has been updated to "
				+ ChatColor.YELLOW + ChatColor.BOLD + version + ".");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		getConfig().options().copyDefaults(true);
		saveConfig();
		for (Player player1 : Bukkit.getOnlinePlayers()) {
			player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
		}

		delayLoadConfig = new DelayLoadConfig();
		delayLoadConnfig_Thread = new Thread(delayLoadConfig);
		delayLoadConnfig_Thread.start();

	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		PluginDescriptionFile file = this.getDescription();
		String version = file.getVersion();
		Player player = (Player) sender;
		String message = "";
		String m[] = message.split("\\s+");
		// UserData
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String rank = playerData.getString("rank");
		// Setspawn
		if (CommandLabel.equalsIgnoreCase("patch") || CommandLabel.equalsIgnoreCase("SMDMain:patch")) {
			player.sendMessage(
					ChatColor.BLUE + "Patch> " + ChatColor.GRAY + "Currently patch is " + ChatColor.YELLOW + version);
		}
		if (CommandLabel.equalsIgnoreCase("setspawn") || CommandLabel.equalsIgnoreCase("ss")
				|| CommandLabel.equalsIgnoreCase("SMDMain:ss") || CommandLabel.equalsIgnoreCase("SMDMain:setspawn")) {
			if (player.isOp() || player.hasPermission("SMDMain.setspawn")) {
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
				|| CommandLabel.equalsIgnoreCase("SMDMain:sh") || CommandLabel.equalsIgnoreCase("SMDMain:sethome")) {
			if (f.exists()) {
				if (playerData.getString("home") != null) {
					// Can't Set home //Already set
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.RED + "Reach maximum sethome.");
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Allow Sethome:"
							+ ChatColor.YELLOW + " 1 home(s)");
					player.sendMessage(
							ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "You need to remove your old house first");
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
							+ "/removehome , /rh");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
				} else {
					// Can Set home
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
					} catch (IOException exception) {
						exception.printStackTrace();
					}
					Location loc = new Location(w, plx, ply, plz);
					player.setBedSpawnLocation(loc);
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Sethome complete.");
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "At location " + ChatColor.GREEN
							+ "X:" + x + ChatColor.AQUA + " Y:" + y + ChatColor.YELLOW + " Z:" + z
							+ ChatColor.LIGHT_PURPLE + " at World " + ChatColor.GOLD + plw);
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				}
			}
		}
		// home
		if (CommandLabel.equalsIgnoreCase("home") || CommandLabel.equalsIgnoreCase("h")
				|| CommandLabel.equalsIgnoreCase("SMDMain:home") || CommandLabel.equalsIgnoreCase("SMDMain:h")) {
			if (f.exists()) {
				if (playerData.getString("home") != null) {
					// Have Home
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
				} else {
					// Not set home yet
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "You didn't sethome yet.");
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
							+ "/sethome , /sh first.");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		// location of home
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
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.AQUA + "===Home info===");
					player.sendMessage(
							ChatColor.BLUE + "Portal> " + ChatColor.YELLOW + "World: " + ChatColor.GREEN + world);
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.YELLOW + "Location: " + ChatColor.GREEN
							+ "X:" + x + ChatColor.AQUA + " Y:" + y + ChatColor.YELLOW + " Z:" + z);
					player.sendMessage(
							ChatColor.BLUE + "Portal> " + ChatColor.YELLOW + "Yaw: " + ChatColor.GREEN + yaw);
					player.sendMessage(
							ChatColor.BLUE + "Portal> " + ChatColor.YELLOW + "Pitch: " + ChatColor.GREEN + pitch);
				} else {
					// Not set home yet
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "You didn't sethome yet.");
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
							+ "/sethome , /sh first.");
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
					} catch (IOException exception) {
						exception.printStackTrace();
					}
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Remove home complete. [" + x
							+ ", " + y + ", " + z + ", " + world + "]");
					player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 10, 1);
				} else {
					// Not set home yet
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "You didn't sethome yet.");
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
							+ "/sethome , /sh first.");
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
				player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Teleported to " + ChatColor.YELLOW
						+ "Server Warp" + ChatColor.GRAY + ".");
				player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
			} else if (warp == null) {
				player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.YELLOW + "Server Warp " + ChatColor.GRAY
						+ "isn't open");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
			}
		}
		if (CommandLabel.equalsIgnoreCase("setwarp") || CommandLabel.equalsIgnoreCase("SMDMain:setwarp")) {
			if (player.isOp() || player.hasPermission("SMDMain.warp")) {
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
						player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY
								+ "Set server warp at your location completed.");
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
		if (CommandLabel.equalsIgnoreCase("removewarp") || CommandLabel.equalsIgnoreCase("SMDMain:removewarp")) {
			if (player.isOp() || player.hasPermission("SMDMain.warp")) {
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
		if (CommandLabel.equalsIgnoreCase("gamemode") || CommandLabel.equalsIgnoreCase("SMDMain:gamemode")
				|| CommandLabel.equalsIgnoreCase("gm") || CommandLabel.equalsIgnoreCase("SMDMain:gm")) {
			if (player.isOp() || player.hasPermission("SMDMain.gamemode")) {
				if (args.length == 0) {
					player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.YELLOW + "Type: " + ChatColor.GREEN
							+ "/gamemode [mode] [player] (/gm)");
					player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.YELLOW
							+ "If [player] empty = change your gamemode.");
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
				}
				if (args.length == 2) {
					if (Bukkit.getServer().getPlayer(args[1]) != null) {
						Player targetPlayer = player.getServer().getPlayer(args[1]);
						String targetPlayerName = targetPlayer.getName();
						if ((args[0].equalsIgnoreCase("1")) || (args[0].equalsIgnoreCase("c"))
								|| (args[0].equalsIgnoreCase("creative"))) {
							targetPlayer.setGameMode(GameMode.CREATIVE);
							targetPlayer.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.GRAY
									+ "Your gamemode has been updated to " + ChatColor.GREEN + "Creative.");
							player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.YELLOW + targetPlayerName
									+ "'s " + ChatColor.GRAY + "gamemode has been updated to " + ChatColor.GREEN
									+ "Creative.");
							player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
						} else if ((args[0].equalsIgnoreCase("0")) || (args[0].equalsIgnoreCase("s"))
								|| (args[0].equalsIgnoreCase("survival"))) {
							targetPlayer.setGameMode(GameMode.SURVIVAL);
							targetPlayer.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.GRAY
									+ "Your gamemode has been updated to " + ChatColor.YELLOW + "Survival.");
							player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.YELLOW + targetPlayerName
									+ "'s " + ChatColor.GRAY + "gamemode has been updated to " + ChatColor.YELLOW
									+ "Survival.");
							player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
						} else if ((args[0].equalsIgnoreCase("2")) || (args[0].equalsIgnoreCase("a"))
								|| (args[0].equalsIgnoreCase("adventure"))) {
							targetPlayer.setGameMode(GameMode.ADVENTURE);
							targetPlayer.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.GRAY
									+ "Your gamemode has been updated to " + ChatColor.LIGHT_PURPLE + "Adventure.");
							player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.YELLOW + targetPlayerName
									+ "'s " + ChatColor.GRAY + "gamemode has been updated to " + ChatColor.LIGHT_PURPLE
									+ "Adventure.");
							player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
						} else if ((args[0].equalsIgnoreCase("3")) || (args[0].equalsIgnoreCase("sp"))
								|| (args[0].equalsIgnoreCase("spectator"))) {
							targetPlayer.setGameMode(GameMode.SPECTATOR);
							targetPlayer.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.GRAY
									+ "Your gamemode has been updated to " + ChatColor.AQUA + "Spectator.");
							player.sendMessage(ChatColor.BLUE + "Gamemode> " + ChatColor.YELLOW + targetPlayerName
									+ "'s " + ChatColor.GRAY + "gamemode has been updated to " + ChatColor.AQUA
									+ "Spectator.");
							player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
						}
					}
				}
			} else {
				player.sendMessage(
						ChatColor.BLUE + "Gamemode> " + ChatColor.YELLOW + "You don't have " + ChatColor.RED + "OP!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
			}
		}
		if (CommandLabel.equalsIgnoreCase("heal") || CommandLabel.equalsIgnoreCase("SMDMain:heal")) {
			if (player.isOp() || player.hasPermission("SMDMain.heal")) {
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
		if (CommandLabel.equalsIgnoreCase("fly") || CommandLabel.equalsIgnoreCase("SMDMain:fly")) {
			if (player.isOp() || player.hasPermission("SMDMain.fly")) {
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
			player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.YELLOW + "You have been resend your location.");
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
		}
		if (CommandLabel.equalsIgnoreCase("day") || CommandLabel.equalsIgnoreCase("SMDMain:day")) {
			if (player.isOp() || player.hasPermission("SMDMain.time")) {
				World w = ((Player) sender).getWorld();
				player.sendMessage(ChatColor.BLUE + "Time> " + ChatColor.GRAY + "Set time to " + ChatColor.GOLD + "Day "
						+ ChatColor.GRAY + ChatColor.ITALIC + "(1000 ticks)");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(1000);
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("midday") || CommandLabel.equalsIgnoreCase("SMDMain:midday")) {
			if (player.isOp() || player.hasPermission("SMDMain.time")) {
				player.sendMessage(ChatColor.BLUE + "Time> " + ChatColor.GRAY + "Set time to " + ChatColor.GOLD
						+ "Midday " + ChatColor.GRAY + ChatColor.ITALIC + "(6000 ticks)");
				World w = ((Player) sender).getWorld();
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(6000);
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("night") || CommandLabel.equalsIgnoreCase("SMDMain:night")) {
			if (player.isOp() || player.hasPermission("SMDMain.time")) {
				World w = ((Player) sender).getWorld();
				player.sendMessage(ChatColor.BLUE + "Time> " + ChatColor.GRAY + "Set time to " + ChatColor.GOLD
						+ "Night " + ChatColor.GRAY + ChatColor.ITALIC + "(13000 ticks)");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(13000);
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("midnight") || CommandLabel.equalsIgnoreCase("SMDMain:midnight")) {
			if (player.isOp() || player.hasPermission("SMDMain.time")) {
				World w = ((Player) sender).getWorld();
				player.sendMessage(ChatColor.BLUE + "Time> " + ChatColor.GRAY + "Set time to " + ChatColor.GOLD
						+ "Midnight " + ChatColor.GRAY + ChatColor.ITALIC + "(18000 ticks)");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				w.setTime(18000);
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("bc") || CommandLabel.equalsIgnoreCase("SMDMain:bc")) {
			if (player.isOp() || player.hasPermission("SMDMain.admin")) {
				if (args.length == 0 || args[0].isEmpty()) {
					player.sendMessage(
							ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN + "/bc [text].");
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
					player.sendMessage(
							ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("force") || CommandLabel.equalsIgnoreCase("SMDMain:force")) {
			if (player.isOp() || player.hasPermission("SMDMain.admin")) {
				if (args.length == 0 || args[0].isEmpty()) {
					player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
							+ "/force [player] [message].");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				} else if (args.length != 0) {
					if (Bukkit.getServer().getPlayer(args[0]) != null) {
						Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
						String targetPlayerName = targetPlayer.getName();
						message = "";
						for (int i = 1; i != args.length; i++)
							message += args[i] + " ";
						message = message.replaceAll("&", "§");
						targetPlayer.chat(message);
						player.sendMessage(
								ChatColor.BLUE + "Force> " + ChatColor.GRAY + "You forced " + ChatColor.YELLOW
										+ targetPlayerName + ChatColor.GRAY + ": " + ChatColor.AQUA + message);
					} else {
						player.sendMessage(ChatColor.BLUE + "Force> " + ChatColor.RED + "Player not found.");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(
							ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("bedrock") || CommandLabel.equalsIgnoreCase("SMDMain:bedrock")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + player.getName() + " bedrock");
			player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Here you are, Use " + ChatColor.YELLOW
					+ "Bedrock" + ChatColor.GRAY + " to check block-logging");
			player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GREEN + "Right-Click" + ChatColor.YELLOW
					+ " to check at placed location");
			player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GREEN + "Left-Click" + ChatColor.YELLOW
					+ " to check at clicked location");
		}

		if (CommandLabel.equalsIgnoreCase("ping") || CommandLabel.equalsIgnoreCase("SMDMain:ping")) {
			int ping = ((CraftPlayer) player).getHandle().ping;
			if (args.length == 0) {
				if (ping < 31) {
					ChatColor color = ChatColor.AQUA;
					player.sendMessage(ChatColor.BLUE + "Ping> " + ChatColor.GRAY + "Your ping is " + color + ping
							+ ChatColor.GRAY + " ms.");
				}
				if (ping > 30 && ping < 81) {
					ChatColor color = ChatColor.GREEN;
					player.sendMessage(ChatColor.BLUE + "Ping> " + ChatColor.GRAY + "Your ping is " + color + ping
							+ ChatColor.GRAY + " ms.");
				}
				if (ping > 80 && ping < 151) {
					ChatColor color = ChatColor.GOLD;
					player.sendMessage(ChatColor.BLUE + "Ping> " + ChatColor.GRAY + "Your ping is " + color + ping
							+ ChatColor.GRAY + " ms.");
				}
				if (ping > 150 && ping < 501) {
					ChatColor color = ChatColor.RED;
					player.sendMessage(ChatColor.BLUE + "Ping> " + ChatColor.GRAY + "Your ping is " + color + ping
							+ ChatColor.GRAY + " ms.");
				}
				if (ping > 500) {
					ChatColor color = ChatColor.DARK_RED;
					player.sendMessage(ChatColor.BLUE + "Ping> " + ChatColor.GRAY + "Your ping is " + color + ping
							+ ChatColor.GRAY + " ms.");
				}
			} else if (args.length == 1) {
				if (player.getServer().getPlayer(args[0]) != null) {
					Player targetPlayer = player.getServer().getPlayer(args[0]);
					int ping2 = ((CraftPlayer) targetPlayer).getHandle().ping;
					if (ping2 < 31) {
						ChatColor color = ChatColor.AQUA;
						player.sendMessage(ChatColor.BLUE + "Ping> " + ChatColor.YELLOW + args[0] + "'s ping"
								+ ChatColor.GRAY + " is " + color + ping2 + ChatColor.GRAY + " ms.");
					}
					if (ping2 > 30 && ping < 81) {
						ChatColor color = ChatColor.GREEN;
						player.sendMessage(ChatColor.BLUE + "Ping> " + ChatColor.YELLOW + args[0] + "'s ping"
								+ ChatColor.GRAY + " is " + color + ping2 + ChatColor.GRAY + " ms.");
					}
					if (ping2 > 80 && ping < 151) {
						ChatColor color = ChatColor.GOLD;
						player.sendMessage(ChatColor.BLUE + "Ping> " + ChatColor.YELLOW + args[0] + "'s ping"
								+ ChatColor.GRAY + " is " + color + ping2 + ChatColor.GRAY + " ms.");
					}
					if (ping2 > 150 && ping < 501) {
						ChatColor color = ChatColor.RED;
						player.sendMessage(ChatColor.BLUE + "Ping> " + ChatColor.YELLOW + args[0] + "'s ping"
								+ ChatColor.GRAY + " is " + color + ping2 + ChatColor.GRAY + " ms.");
					}
					if (ping2 > 500) {
						ChatColor color = ChatColor.DARK_RED;
						player.sendMessage(ChatColor.BLUE + "Ping> " + ChatColor.YELLOW + args[0] + "'s ping"
								+ ChatColor.GRAY + " is " + color + ping2 + ChatColor.GRAY + " ms.");
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "Server>" + ChatColor.GRAY + "Player not found!");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("world") || CommandLabel.equalsIgnoreCase("SMDMain:world")) {
			if (player.isOp() || player.hasPermission("SMDMain.admin")) {
				double x = player.getLocation().getX();
				double y = player.getLocation().getY();
				double z = player.getLocation().getZ();
				double pitch = player.getLocation().getPitch();
				double yaw = player.getLocation().getYaw();
				if (args.length == 1 && !args[0].isEmpty()) {
					if (args[0].equalsIgnoreCase("world")) {
						World p = Bukkit.getWorld("world");
						Location loc = new Location(p, x, y, z);
						loc.setPitch((float) pitch);
						loc.setYaw((float) yaw);
						player.teleport(loc);
						player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Sent you to world "
								+ ChatColor.GREEN + "world" + ChatColor.GRAY + ".");
						for (Player player1 : Bukkit.getOnlinePlayers()) {
							player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
						}
					} else if (args[0].equalsIgnoreCase("nether")) {
						World p = Bukkit.getWorld("world_nether");
						Location loc = new Location(p, x, y, z);
						loc.setPitch((float) pitch);
						loc.setYaw((float) yaw);
						player.teleport(loc);
						player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Sent you to world "
								+ ChatColor.GREEN + "world_nether" + ChatColor.GRAY + ".");
						for (Player player1 : Bukkit.getOnlinePlayers()) {
							player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
						}
					} else if (args[0].equalsIgnoreCase("end")) {
						World p = Bukkit.getWorld("world_the_end");
						Location loc = new Location(p, x, y, z);
						loc.setPitch((float) pitch);
						loc.setYaw((float) yaw);
						player.teleport(loc);
						player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Sent you to world "
								+ ChatColor.GREEN + "world_the_end" + ChatColor.GRAY + ".");
						for (Player player1 : Bukkit.getOnlinePlayers()) {
							player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
						}
					} else {
						player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
								+ "/world [world|nether|end]");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else if (args.length != 1) {
					player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
							+ "/world [world|nether|end]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
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
		if (CommandLabel.equalsIgnoreCase("climate") || CommandLabel.equalsIgnoreCase("SMDMain:climate")) {
			if (player.isOp() || player.hasPermission("SMDMain.time")) {
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
						player.sendMessage(ChatColor.BLUE + "Weather> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
								+ "/climate [sun/storm/rain]");
						player.sendMessage(ChatColor.BLUE + "Weather> " + ChatColor.GRAY + "Or " + ChatColor.GREEN
								+ "/climate (toggle between sun and rain)");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_SNARE, 1, 1);
					}
				}
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
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
						player.sendMessage(
								ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "You can't teleport to yourself!");
					} else if (targetPlayerName != playerName) {
						getConfig().set("Teleport." + targetPlayerName, playerName);
						saveConfig();
						player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GREEN
								+ "You sent teleportion request to " + ChatColor.YELLOW + targetPlayerName);
						targetPlayer.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Player "
								+ ChatColor.YELLOW + playerName + ChatColor.GRAY + " sent teleportion request to you");
						targetPlayer.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GREEN + "/tpaccept "
								+ ChatColor.YELLOW + playerName + ChatColor.GRAY + " to" + ChatColor.GREEN + " accept "
								+ ChatColor.GRAY + "teleportion request.");
						targetPlayer.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.RED + "/tpdeny "
								+ ChatColor.YELLOW + playerName + ChatColor.GRAY + " to" + ChatColor.RED + " deny "
								+ ChatColor.GRAY + "teleportion request.");
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Player not found!");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
						+ "/tpr [player] - Sent teleportion request to [player]");
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
						player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GREEN
								+ "You accept teleportion request from " + ChatColor.YELLOW + targetPlayerName + ".");
						targetPlayer.sendMessage(
								ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Player " + ChatColor.YELLOW + playerName
										+ ChatColor.GREEN + " accept " + ChatColor.GRAY + "your teleportion request.");
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
						player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY
								+ "You didn't have any request from anyone");
					} else {
						player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY
								+ "You don't have any teleportion request from " + ChatColor.YELLOW + targetPlayerName
								+ ".");
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Player not found!");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
						+ "/tpaccept [player] - Accept teleportion request");
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
						player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.RED
								+ "You deny teleportion request from " + ChatColor.YELLOW + targetPlayerName + ".");
						targetPlayer.sendMessage(
								ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Player " + ChatColor.YELLOW + playerName
										+ ChatColor.RED + " deny " + ChatColor.GRAY + "your teleportion request.");
					} else if (getConfig().getString("Teleport." + targetPlayerName).equalsIgnoreCase("None")) {
						player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY
								+ "You didn't have any request from anyone");
					} else {
						player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY
								+ "You don't have any teleportion request from " + ChatColor.YELLOW + targetPlayerName
								+ ".");
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Player not found!");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(ChatColor.BLUE + "Portal> " + ChatColor.GRAY + "Type: " + ChatColor.RED
						+ "/tpdeny [player] - Deny teleportion request");
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
								message += args[i] + "";
							message = message.replaceAll("&", "§");
							Bukkit.broadcastMessage(ChatColor.BLUE + "Chat> " + ChatColor.GRAY + "Player "
									+ ChatColor.YELLOW + playerName + ChatColor.RED + " revoke " + ChatColor.YELLOW
									+ targetPlayerName + "'s ability " + ChatColor.GRAY + "to chat. ");
							Bukkit.broadcastMessage(ChatColor.BLUE + "Chat> " + ChatColor.GRAY + "Reason: "
									+ ChatColor.YELLOW + message);
							targetPlayer
									.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "You have been muted.");
							targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,
									1);
							try {
								playerData1.set("mute.is", "true");
								playerData1.set("mute.reason", message);
								playerData1.save(f1);
							} catch (IOException exception) {
								exception.printStackTrace();
							}
						}
						if (muteis.equalsIgnoreCase("true")) {
							Bukkit.broadcastMessage(ChatColor.BLUE + "Chat> " + ChatColor.GRAY + "Player "
									+ ChatColor.YELLOW + playerName + ChatColor.RED + " revoke " + ChatColor.YELLOW
									+ targetPlayerName + "'s ability " + ChatColor.GRAY + "to chat. ");
							player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "You " + ChatColor.GREEN
									+ "grant " + ChatColor.YELLOW + targetPlayerName + "'s ability " + ChatColor.GRAY
									+ "to chat. ");
							targetPlayer.sendMessage(
									ChatColor.BLUE + "Server> " + ChatColor.GRAY + "You have been unmuted.");
							targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,
									1);
							try {
								playerData1.set("mute.is", "false");
								playerData1.set("mute.reason", "none");
								playerData1.save(f1);
							} catch (IOException exception) {
								exception.printStackTrace();
							}
						}
					} else {
						player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Player not found!");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
							+ "/mute [player] [reason]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("warn")) {
			if (player.isOp() || player.hasPermission("SMDMain.warn")) {
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
						Bukkit.broadcastMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + targetPlayerName
								+ " has been warned (" + countnew + ")");
						Bukkit.broadcastMessage(
								ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Reason: " + ChatColor.YELLOW + message);
						try {
							playerData1.set("warn", countnew);
							playerData1.save(f1);
						} catch (IOException exception) {
							exception.printStackTrace();
						}
						for (Player p : Bukkit.getOnlinePlayers()) {
							p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
						}
					} else {
						player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Player not found!");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
							+ "/warn [player] [reason]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("resetwarn")) {
			if (player.isOp() || player.hasPermission("SMDMain.warn")) {
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
						Bukkit.broadcastMessage(ChatColor.BLUE + "Server> " + ChatColor.YELLOW + playerName
								+ ChatColor.GRAY + " reset " + targetPlayerName + "'s warned (0)");
						try {
							playerData1.set("warn", "0");
							playerData1.save(f1);
						} catch (IOException exception) {
							exception.printStackTrace();
						}
						for (Player p : Bukkit.getOnlinePlayers()) {
							p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
						}
					} else {
						player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Player not found!");
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
							+ "/resetwarn [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("rank")) {
			if (player.isOp() || player.hasPermission("SMDMain.rank")) {
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
							Bukkit.broadcastMessage(
									ChatColor.BLUE + "Rank> " + ChatColor.GRAY + "Player " + ChatColor.YELLOW
											+ targetPlayerName + ChatColor.GRAY + "'s rank has been updated to "
											+ ChatColor.DARK_PURPLE + ChatColor.BOLD + "Staff");
							targetPlayer.setPlayerListName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Staff"
									+ ChatColor.LIGHT_PURPLE + targetPlayerName);
							try {
								playerData1.set("rank", "staff");
								playerData1.save(f1);
							} catch (IOException exception) {
								exception.printStackTrace();
							}
						} else if (args[0].equalsIgnoreCase("default")) {
							Bukkit.broadcastMessage(ChatColor.BLUE + "Rank> " + ChatColor.GRAY + "Player "
									+ ChatColor.YELLOW + targetPlayerName + ChatColor.GRAY
									+ "'s rank has been updated to " + ChatColor.BLUE + ChatColor.BOLD + "Default");
							targetPlayer.setPlayerListName(ChatColor.BLUE + targetPlayerName);
							try {
								playerData1.set("rank", "default");
								playerData1.save(f1);
							} catch (IOException exception) {
								exception.printStackTrace();
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
							try {
								playerData1.set("rank", "vip");
								playerData1.save(f1);
							} catch (IOException exception) {
								exception.printStackTrace();
							}
							for (Player p : Bukkit.getOnlinePlayers()) {
								p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
							}
						} else if (args[0].equalsIgnoreCase("mvp")) {
							targetPlayer.setPlayerListName(ChatColor.AQUA + "" + ChatColor.BOLD + "MVP"
									+ ChatColor.DARK_AQUA + targetPlayerName);
							Bukkit.broadcastMessage(ChatColor.BLUE + "Rank> " + ChatColor.GRAY + "Player "
									+ ChatColor.YELLOW + targetPlayerName + ChatColor.GRAY
									+ "'s rank has been updated to " + ChatColor.AQUA + ChatColor.BOLD + "MVP");
							try {
								playerData1.set("rank", "mvp");
								playerData1.save(f1);
							} catch (IOException exception) {
								exception.printStackTrace();
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

							try {
								playerData1.set("rank", "admin");
								playerData1.save(f1);
							} catch (IOException exception) {
								exception.printStackTrace();
							}
							for (Player p : Bukkit.getOnlinePlayers()) {
								p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
							}

						} else {
							player.sendMessage(ChatColor.BLUE + "Rank> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
									+ "/rank [default|vip|mvp|staff|admin] [player]");
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
						}
					} else {
						player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "Player not found!");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "Rank> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
							+ "/rank [default|vip|mvp|staff|admin] [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.RED + "You don't have permission or op!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("status") || CommandLabel.equalsIgnoreCase("SMDMain:status")) {
			player.sendMessage(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "----[" + ChatColor.WHITE + "STATS"
					+ ChatColor.YELLOW + ChatColor.STRIKETHROUGH + "]----");
			player.sendMessage(ChatColor.WHITE + "Mute: ");
			String muteis = playerData.getString("mute.is");
			String mutere = playerData.getString("mute.reason");
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
		if (CommandLabel.equalsIgnoreCase("reconfig") || CommandLabel.equalsIgnoreCase("SMDMain:reconfig")) {
			if (player.isOp() || player.hasPermission("main.reconfig")) {
				if (args.length == 1) {
					if (Bukkit.getServer().getPlayer(args[0]) != null) {
						Player targetPlayer = player.getServer().getPlayer(args[0]);
						String targetPlayerName = targetPlayer.getName();
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
							playerData1.save(f1);
						} catch (IOException exception) {
							exception.printStackTrace();
						}
						Bukkit.broadcastMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Player "
								+ ChatColor.YELLOW + targetPlayerName + "'s information " + ChatColor.GRAY + "has been "
								+ ChatColor.RED + "reset " + ChatColor.GRAY + "by " + ChatColor.AQUA + playerName
								+ ".");
						for (Player p : Bukkit.getOnlinePlayers()) {
							p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 2, 2);
						}
					} else {
						player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Player not found!");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(ChatColor.BLUE + "Server> " + ChatColor.GRAY + "Type: " + ChatColor.GREEN
							+ "/resetconfig [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
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
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		PluginDescriptionFile file = this.getDescription();
		String version = file.getVersion();
		Map<String, Map<String, Object>> test = file.getCommands();
		player.sendMessage(ChatColor.GRAY + "");
		player.sendMessage(ChatColor.GRAY + "Welcome! " + ChatColor.YELLOW + playerName + ChatColor.GRAY
				+ " to §6§lE§e§lx§f§lcalibur §4§lTh§f§la§1§lil§f§la§4§lnd §a§lSurvival.");
		player.sendMessage(ChatColor.GRAY + "Now we're " + ChatColor.YELLOW + ChatColor.BOLD + "BETA" + ChatColor.GRAY
				+ "." + ChatColor.GREEN + " Please friendly and respect to other player.");
		player.sendMessage(ChatColor.GRAY + "Report " + ChatColor.RED + ChatColor.BOLD + "BUG/PROBLEM/GLITCH"
				+ ChatColor.GRAY + " in " + ChatColor.AQUA + "Facebook Group");
		player.sendMessage(ChatColor.GRAY + "");
		player.sendMessage(ChatColor.GRAY + "Server Patch: " + ChatColor.YELLOW + ChatColor.BOLD + version);
		player.sendMessage(ChatColor.GRAY + "");
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
				playerData.save(f);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		if (f.exists()) {
			String rank = playerData.getString("rank");
			int countwarn = playerData.getInt("warn");
			if (countwarn > 0) {
				player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "ALERT!" + ChatColor.RED
						+ " You have been warned " + ChatColor.YELLOW + countwarn + " time(s).");
				player.sendMessage(ChatColor.RED + "If you get warned 3 time, You will be " + ChatColor.DARK_RED
						+ ChatColor.BOLD + "BANNED.");
				player.sendMessage("");
			}
			if (rank.equalsIgnoreCase("default")) {
				player.setPlayerListName(ChatColor.BLUE + player.getName());
				event.setJoinMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " joined the game!");
			} else if (rank.equalsIgnoreCase("staff")) {
				player.setPlayerListName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Staff" + ChatColor.LIGHT_PURPLE
						+ player.getName());
				event.setJoinMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Staff" + ChatColor.LIGHT_PURPLE
						+ player.getName() + ChatColor.WHITE + " joined the game!");
			} else if (rank.equalsIgnoreCase("vip")) {
				player.setPlayerListName(
						ChatColor.GREEN + "" + ChatColor.BOLD + "VIP" + ChatColor.DARK_GREEN + player.getName());
				event.setJoinMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "VIP" + ChatColor.DARK_GREEN
						+ player.getName() + ChatColor.WHITE + " joined the game!");
			} else if (rank.equalsIgnoreCase("mvp")) {
				player.setPlayerListName(
						ChatColor.AQUA + "" + ChatColor.BOLD + "MVP" + ChatColor.DARK_AQUA + player.getName());
				event.setJoinMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "MVP" + ChatColor.DARK_AQUA
						+ player.getName() + ChatColor.WHITE + " joined the game!");
			} else if (rank.equalsIgnoreCase("admin")) {
				player.setPlayerListName(
						ChatColor.DARK_RED + "" + ChatColor.BOLD + "Admin" + ChatColor.RED + player.getName());
				event.setJoinMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Admin" + ChatColor.RED
						+ player.getName() + ChatColor.WHITE + " joined the game!");
			}
		}
	}

	@EventHandler
	public void playerChat(AsyncPlayerChatEvent event) {
		String message1 = ChatColor.GRAY + ": " + ChatColor.WHITE + event.getMessage();
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
			player.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GRAY + "You have been muted." + ChatColor.YELLOW
					+ " (Reason: " + mutere + ")");
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			event.setCancelled(true);
		} else {

			if (rank.equalsIgnoreCase("default")) {
				event.setFormat(ChatColor.BLUE + player.getName() + ChatColor.GRAY + message1);
			} else if (rank.equalsIgnoreCase("staff")) {
				event.setFormat(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Staff" + ChatColor.LIGHT_PURPLE
						+ player.getName() + ChatColor.WHITE + message1);
			} else if (rank.equalsIgnoreCase("vip")) {
				event.setFormat(ChatColor.GREEN + "" + ChatColor.BOLD + "VIP" + ChatColor.DARK_GREEN + player.getName()
						+ ChatColor.WHITE + message1);
			} else if (rank.equalsIgnoreCase("mvp")) {
				event.setFormat(ChatColor.AQUA + "" + ChatColor.BOLD + "MVP" + ChatColor.DARK_AQUA + player.getName()
						+ ChatColor.WHITE + message1);
			} else if (rank.equalsIgnoreCase("admin")) {
				event.setFormat(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Admin" + ChatColor.RED + player.getName()
						+ ChatColor.WHITE + message1);
			} else {
				event.setFormat(ChatColor.BLUE + player.getName() + ChatColor.GRAY + message1);
			}
		}
	}

	@EventHandler
	public void QuitServer(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		finish.remove(player.getUniqueId());
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase");
		File f = new File(userdata, File.separator + playerName + ".yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String rank = playerData.getString("rank");
		if (rank.equalsIgnoreCase("default")) {
			event.setQuitMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " left the game!");
		} else if (rank.equalsIgnoreCase("staff")) {
			event.setQuitMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Staff" + ChatColor.LIGHT_PURPLE
					+ player.getName() + ChatColor.WHITE + " left the game!");
		} else if (rank.equalsIgnoreCase("vip")) {
			event.setQuitMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "VIP" + ChatColor.DARK_GREEN + player.getName()
					+ ChatColor.WHITE + " left the game!");
		} else if (rank.equalsIgnoreCase("mvp")) {
			event.setQuitMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "MVP" + ChatColor.DARK_AQUA + player.getName()
					+ ChatColor.WHITE + " left the game!");
		} else if (rank.equalsIgnoreCase("admin")) {
			event.setQuitMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Admin" + ChatColor.RED + player.getName()
					+ ChatColor.WHITE + " left the game!");
		}
	}

	public void playCircularEffect(Location location, Effect effect, boolean v) {
		for (int i = 0; i <= 8; i += ((!v && (i == 3)) ? 2 : 1))
			location.getWorld().playEffect(location, effect, i);
	}
}
