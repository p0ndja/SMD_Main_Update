package me.palapon2545.main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
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
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import me.palapon2545.main.pluginMain;
import me.palapon2545.main.ActionBarAPI;
import me.palapon2545.main.ActionBarMessageEvent;

public class pluginMain extends JavaPlugin implements Listener {

	public HashMap<String, Long> cooldowns = new HashMap<String, Long>();
	public final Logger logger = Logger.getLogger("Minecraft");
	public pluginMain plugin;
	LinkedList<String> badWord = new LinkedList<String>();

	String cl = "§";
	String sv = ChatColor.BLUE + "Server> " + ChatColor.GRAY;
	String pp = ChatColor.BLUE + "Portal> " + ChatColor.GRAY;
	String np = ChatColor.RED + "You don't have permission or op!";
	String wp = ChatColor.RED + "Player not found.";
	String type = ChatColor.GRAY + "Type: " + ChatColor.GREEN;
	String j = ChatColor.GREEN + "Join> ";
	String l = ChatColor.RED + "Left> ";
	String noi = "You don't have enough item.";
	String nom = "You don't have enough money";
	String lc = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Lucky" + ChatColor.YELLOW + ChatColor.BOLD + "Click> "
			+ ChatColor.WHITE;
	String non = ChatColor.GRAY + " is not number";
	String tc = ChatColor.BLUE + "" + ChatColor.BOLD + "Teleport Charger: ";
	String ct = tc + ChatColor.RED + "Cancelled!";
	String cd = ChatColor.AQUA + "" + ChatColor.BOLD + "[Countdown]: " + ChatColor.WHITE;

	public void onDisable() {
		Bukkit.broadcastMessage(sv + "SMDMain System: " + ChatColor.RED + ChatColor.BOLD + "Disable");
		for (Player player1 : Bukkit.getOnlinePlayers()) {
			player1.playSound(player1.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 0);
		}
		saveConfig();
	}

	public void onEnable() {
		Bukkit.broadcastMessage(sv + "SMDMain System: " + ChatColor.GREEN + ChatColor.BOLD + "Enable");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		ActionBarAPI.run();
		getConfig().options().copyDefaults(true);
		getConfig().set("warp", null);
		getConfig().set("count", -1);
		getConfig().set("countdown_msg", "Undefined");
		getConfig().set("countdown_msg_toggle", "u");
		getConfig().set("event.warpstatus", "false");
		getConfig().set("event.name", "none");
		getConfig().set("event.join", "false");
		getConfig().set("event.queuelist", null);
		saveConfig();
		for (Player player1 : Bukkit.getOnlinePlayers()) {
			player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
		}
		BukkitScheduler s = getServer().getScheduler();
		s.scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				Countdown();
			}
		}, 0L, 20L);
		s.scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				int player = Bukkit.getServer().getOnlinePlayers().size();
				if (player > 0) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.sendMessage(
								ChatColor.GREEN + "World> " + ChatColor.AQUA + "World and Player data has been saved.");
						p.saveData();
					}
					for (World w : Bukkit.getWorlds()) {
						w.save();
					}
				} else {

				}
			}

		}, 0L, 6000L);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		String message = "";
		Player player = (Player) sender;
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase/" + playerName);
		File f = new File(userdata, File.separator + "config.yml");
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
				player.sendMessage(sv + np);
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
				player.sendMessage(pp + "Teleported to " + ChatColor.YELLOW + "Spawn" + ChatColor.GRAY + ".");
				player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
			} else {
				player.sendMessage(pp + "Spawn location not found!");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);

			}
		}
		if (CommandLabel.equalsIgnoreCase("sethome") || CommandLabel.equalsIgnoreCase("sh")
				|| CommandLabel.equalsIgnoreCase("SMDMain:sh") || CommandLabel.equalsIgnoreCase("SMDMain:sethome")) {
			String name = "";
			if (args.length == 0) {
				name = player.getWorld().getName();
			}
			if (args.length == 1) {
				name = args[0];
			}
			int homeq = playerData.getInt("Quota.Home");
			File path = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
					File.separator + "PlayerDatabase/" + playerName + "/HomeDatabase");
			File[] files = path.listFiles();
			if (files.length >= homeq) {
				player.sendMessage(pp + "Your sethome reach limit " + ChatColor.RED + "(" + homeq + ")");
				player.sendMessage(pp + "Try to remove your home first.");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			} else {
				File userdata2 = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
						File.separator + "PlayerDatabase/" + playerName + "/HomeDatabase");
				File f2 = new File(userdata2, File.separator + name + ".yml");
				FileConfiguration playerData2 = YamlConfiguration.loadConfiguration(f2);
				if (!f2.exists()) {
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
						playerData2.createSection("home");
						playerData2.set("home.name", name);
						playerData2.set("home.world", plw);
						playerData2.set("home.x", plx);
						playerData2.set("home.y", ply);
						playerData2.set("home.z", plz);
						playerData2.set("home.pitch", plpitch);
						playerData2.set("home.yaw", plyaw);
						playerData2.save(f2);
					} catch (IOException e) {
						e.printStackTrace();
					}
					Location loc = new Location(w, plx, ply, plz);
					player.setBedSpawnLocation(loc);
					player.sendMessage(pp + "Set home " + ChatColor.YELLOW + name + ChatColor.YELLOW + " complete.");
					player.sendMessage(pp + "At location " + ChatColor.YELLOW + x + ", " + y + ", " + z
							+ ChatColor.LIGHT_PURPLE + " at World " + ChatColor.GOLD + plw);
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
				} else {
					player.sendMessage(pp + "Home " + name + " is already using");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("home") || CommandLabel.equalsIgnoreCase("h")
				|| CommandLabel.equalsIgnoreCase("SMDMain:home") || CommandLabel.equalsIgnoreCase("SMDMain:h")) {
			String name = "";
			if (args.length == 0) {
				name = player.getWorld().getName();
			}
			if (args.length == 1) {
				name = args[0];
			}
			File userdata2 = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
					File.separator + "PlayerDatabase/" + playerName + "/HomeDatabase");
			File f2 = new File(userdata2, File.separator + name + ".yml");
			FileConfiguration playerData2 = YamlConfiguration.loadConfiguration(f2);
			if (f2.exists()) {
				double x = playerData2.getDouble("home.x");
				double y = playerData2.getDouble("home.y");
				double z = playerData2.getDouble("home.z");
				double pitch = playerData2.getDouble("home.pitch");
				double yaw = playerData2.getDouble("home.yaw");
				String world = playerData2.getString("home.world");
				World p = Bukkit.getWorld(world);
				Location loc = new Location(p, x, y, z);
				loc.setPitch((float) pitch);
				loc.setYaw((float) yaw);
				player.teleport(loc);
				player.sendMessage(pp + "Teleported to home " + ChatColor.YELLOW + name + ChatColor.GRAY + ".");
				player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
			} else {
				player.sendMessage(pp + "Home " + ChatColor.RED + name + ChatColor.GRAY + " not found.");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("listhome") || CommandLabel.equalsIgnoreCase("lh")
				|| CommandLabel.equalsIgnoreCase("SMDMain:listhome") || CommandLabel.equalsIgnoreCase("SMDMain:lh")) {
			File path = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
					File.separator + "PlayerDatabase/" + playerName + "/HomeDatabase");
			File[] files = path.listFiles();
			player.sendMessage(pp + "List of your home " + ChatColor.YELLOW + "(" + files.length + ")" + ChatColor.GRAY + " :");
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					String l = files[i].getName().replaceAll(".yml", "");
					player.sendMessage("- " + ChatColor.YELLOW + l);
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("removehome") || CommandLabel.equalsIgnoreCase("rh")
				|| CommandLabel.equalsIgnoreCase("SMDMain:rh") || CommandLabel.equalsIgnoreCase("SMDMain:removehome")) {
			String name = "";
			if (args.length == 0) {
				name = player.getWorld().getName();
			}
			if (args.length == 1) {
				name = args[0];
			}
			File userdata2 = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
					File.separator + "PlayerDatabase/" + playerName + "/HomeDatabase");
			File f2 = new File(userdata2, File.separator + name + ".yml");
			if (f2.exists()) {
				f2.delete();
				player.sendMessage(pp + "Remove home " + ChatColor.YELLOW + name + ChatColor.GRAY + " complete!");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
			} else {
				player.sendMessage(pp + "Home " + ChatColor.RED + name + ChatColor.GRAY + "not found.");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("event") || CommandLabel.equalsIgnoreCase("SMDMain:event")) {
			String evn = getConfig().getString("event.name");
			String evj = getConfig().getString("event.join");
			String evs = getConfig().getString("event.queuelist." + playerName);
			String re = "";
			String status = "";
			if (evj.equalsIgnoreCase("true")) {
				status = ChatColor.GREEN + "Yes";
			}
			if (evj.equalsIgnoreCase("false")) {
				status = ChatColor.RED + "No";
			}
			if (evs.equalsIgnoreCase(null) || evs.equalsIgnoreCase("false")) {
				re = ChatColor.GRAY + "" + ChatColor.ITALIC + "Not Reserve";
			}
			if (evs.equalsIgnoreCase("true")) {
				re = ChatColor.LIGHT_PURPLE + "Reserved";
			}
			if (args.length == 0) {
				player.sendMessage("---------" + ChatColor.LIGHT_PURPLE + "[Event]" + ChatColor.WHITE + "---------");
				player.sendMessage("Name: " + ChatColor.AQUA + evn);
				player.sendMessage("Reservation: " + status);
				player.sendMessage("Status: " + re);
				player.sendMessage("");
				player.sendMessage(
						"'/event warp' " + ChatColor.GOLD + "-" + ChatColor.YELLOW + " Warp to event location");
				player.sendMessage("'/event reserve'  " + ChatColor.GOLD + "-" + ChatColor.YELLOW
						+ " Add/Cancel your reservation");
				player.sendMessage("");
			} else {
				if (args[0].equalsIgnoreCase("warp")) {
					String warp = getConfig().getString("event.warp");
					String warpstatus = getConfig().getString("event.warpstatus");
					if (warp != null && warpstatus.equalsIgnoreCase("true")) {
						double x = getConfig().getDouble("event.warp.x");
						double y = getConfig().getDouble("event.warp.y");
						double z = getConfig().getDouble("event.warp.z");
						double yaw = getConfig().getDouble("event.warp.yaw");
						double pitch = getConfig().getDouble("event.warp.pitch");
						String world = getConfig().getString("event.warp.world");
						World p = Bukkit.getWorld(world);
						Location loc = new Location(p, x, y, z);
						loc.setPitch((float) pitch);
						loc.setYaw((float) yaw);
						player.teleport(loc);
						player.sendMessage(pp + "Teleported to " + ChatColor.YELLOW + "Event's Spectate Location"
								+ ChatColor.GRAY + ".");
						player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
					} else {
						player.sendMessage(pp + ChatColor.YELLOW + "Event's Warp Location isn't available yet");
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
					}
				}
				if (args[0].equalsIgnoreCase("reserve")) {
					String evl = getConfig().getString("event.queuelist." + playerName);
					if (evj.equalsIgnoreCase("true")) {
						if (evl.equalsIgnoreCase("false")
								|| getConfig().getString("event.queuelist." + playerName) == null
								|| evl.equalsIgnoreCase(null)) {
							getConfig().set("event.queuelist." + playerName, "true");
							saveConfig();
							player.sendMessage(pp + "You reserved event's reserve slot");
						} else {
							getConfig().set("event.queuelist." + playerName, "false");
							saveConfig();
							player.sendMessage(pp + "You canceled your event's reserve slot");
						}
					} else {
						player.sendMessage(pp + "You can't do it at this time (Reservation has been locked)");
					}
				}
			}
		}
		if (CommandLabel.equalsIgnoreCase("eventadmin") || CommandLabel.equalsIgnoreCase("SMDMain:eventadmin")) {
			if (player.isOp() || player.hasPermission("main.eventadmin") || player.hasPermission("main.*")) {
				if (args.length == 0) {
					player.sendMessage("------------------");
					player.sendMessage(
							"'/eventadmin setname' " + ChatColor.GOLD + "-" + ChatColor.YELLOW + " Set event's name");
					player.sendMessage("'/eventadmin setwarp' " + ChatColor.GOLD + "-" + ChatColor.YELLOW
							+ " Set event's warp location");
					player.sendMessage("'/eventadmin reserve' " + ChatColor.GOLD + "-" + ChatColor.YELLOW
							+ " Open/Close reservation system");
					player.sendMessage(
							"'/eventadmin close' " + ChatColor.GOLD + "-" + ChatColor.YELLOW + " Close event");
					player.sendMessage("'/eventadmin warpplayer' " + ChatColor.GOLD + "-" + ChatColor.YELLOW
							+ " Warp Reserved Player to your location");
					player.sendMessage("------------------");
				} else {
					if (args[0].equalsIgnoreCase("setwarp")) {
						Location pl = player.getLocation();
						double plx = pl.getX();
						double ply = pl.getY();
						double plz = pl.getZ();
						double plpitch = pl.getPitch();
						double plyaw = pl.getYaw();
						String plw = pl.getWorld().getName();
						getConfig().set("event.warp.world", plw);
						getConfig().set("event.warp.x", plx);
						getConfig().set("event.warp.y", ply);
						getConfig().set("event.warp.z", plz);
						getConfig().set("event.warp.pitch", plpitch);
						getConfig().set("event.warp.yaw", plyaw);
						getConfig().set("event.warpstatus", "true");
						saveConfig();
						player.sendMessage(pp + ChatColor.GREEN + "Set new event's warp location");
					}
					if (args[0].equalsIgnoreCase("reserve")) {
						if (getConfig().getString("event.join").equalsIgnoreCase("false")) {
							getConfig().set("event.join", "true");
							saveConfig();
							player.sendMessage(pp + "Event Reserve: " + ChatColor.GREEN + "Enable");
						} else {
							getConfig().set("event.join", "false");
							saveConfig();
							player.sendMessage(pp + "Event Reserve: " + ChatColor.RED + "Disable");
						}
					}
					if (args[0].equalsIgnoreCase("warpplayer")) {
						Location pl = player.getLocation();
						double plx = pl.getX();
						double ply = pl.getY();
						double plz = pl.getZ();
						double plpitch = pl.getPitch();
						double plyaw = pl.getYaw();
						String plw = pl.getWorld().getName();
						World p = Bukkit.getWorld(plw);
						Location loc = new Location(p, plx, ply, plz);
						loc.setPitch((float) plpitch);
						loc.setYaw((float) plyaw);
						for (Player o : Bukkit.getOnlinePlayers()) {
							String join = getConfig().getString("event.queuelist." + o.getName());
							if (join.equalsIgnoreCase("true")) {
								o.teleport(loc);
								player.sendMessage(
										pp + "Admin teleport you to " + ChatColor.YELLOW + "Event's Location");
							} else {

							}
						}
					}
					if (args[0].equalsIgnoreCase("setname")) {
						message = "";
						for (int i = 1; i != args.length; i++)
							message += args[i] + " ";
						message = message.replaceAll("&", cl);
						getConfig().set("event.name", message);
						saveConfig();
						player.sendMessage(pp + "Set event's name to " + ChatColor.YELLOW + "' " + message + "'");
					}
					if (args[0].equalsIgnoreCase("close")) {
						String evn = getConfig().getString("event.name");
						Bukkit.broadcastMessage(pp + "Event " + ChatColor.YELLOW + evn + ChatColor.GRAY + "has been "
								+ ChatColor.RED + "closed");
						getConfig().set("event.warpstatus", "false");
						getConfig().set("event.name", "none");
						getConfig().set("event.join", "false");
						for (Player p : Bukkit.getOnlinePlayers()) {
							getConfig().set("event.queuelist." + p.getName(), "false");
						}
						saveConfig();
					}
				}
			} else {
				player.sendMessage(sv + np);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
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
					if (args[0].equalsIgnoreCase("1") || args[0].endsWith("c")) {
						player.setGameMode(GameMode.CREATIVE);
						player.sendMessage(sv + "Your gamemode has been updated to " + ChatColor.GREEN + "Creative.");
					} else if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")
							|| args[0].endsWith("su")) {
						player.setGameMode(GameMode.SURVIVAL);
						player.sendMessage(sv + "Your gamemode has been updated to " + ChatColor.YELLOW + "Survival.");
					} else if (args[0].equalsIgnoreCase("2") || args[0].endsWith("a")) {
						player.setGameMode(GameMode.ADVENTURE);
						player.sendMessage(
								sv + "Your gamemode has been updated to " + ChatColor.LIGHT_PURPLE + "Adventure.");
					} else if (args[0].equalsIgnoreCase("3") || args[0].endsWith("sp")) {
						player.setGameMode(GameMode.SPECTATOR);
						player.sendMessage(sv + "Your gamemode has been updated to " + ChatColor.AQUA + "Spectator.");
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
				player.sendMessage(sv + np);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
			}
		}
		if (CommandLabel.equalsIgnoreCase("register") || CommandLabel.equalsIgnoreCase("SMDMain:register")) {
			String p = playerData.getString("Security.password");
			String e = playerData.getString("Security.email");
			String l = getConfig().getString("login_freeze." + playerName);
			if (p.equalsIgnoreCase("none") && e.equalsIgnoreCase("none")) {
				if (l.equalsIgnoreCase("false")) {
					player.sendMessage(sv + "You're already sign-in!");
				} else {
					try {
						playerData.set("Security.password", args[0]);
						playerData.set("Security.email", args[1]);
						getConfig().set("login_freeze." + playerName, "false");
						playerData.save(f);
						saveConfig();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					player.sendMessage(sv + "Your password is " + ChatColor.YELLOW + args[0]);
					player.sendMessage(sv + "If you forgot password, Use " + ChatColor.YELLOW + "/recover " + args[1]);
				}
			} else {
				player.sendMessage(sv + "You're already register! Use /login [password] to login instead!");
			}
		}
		if (CommandLabel.equalsIgnoreCase("login") || CommandLabel.equalsIgnoreCase("SMDMain:login")) {
			String p = playerData.getString("Security.password");
			String e = playerData.getString("Security.email");
			String l = getConfig().getString("login_freeze." + playerName);
			if (!p.equalsIgnoreCase("none") && !e.equalsIgnoreCase("none")) {
				if (l.equalsIgnoreCase("false")) {
					player.sendMessage(sv + "You're already sign-in!");
				} else {
					if (args[0].equalsIgnoreCase(p)) {
						player.sendMessage(sv + "Sign-in Complete!");
						getConfig().set("login_freeze." + playerName, "false");
						saveConfig();
					} else {
						player.sendMessage(sv + "Incorrect! (Forget password? Type /recover [email])");
					}
				}
			} else {
				player.sendMessage(sv + "You're not register yet! Type /register [password] [email]");
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
						player.sendMessage(sv + wp);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + type + "/heal [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + np);
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
						player.sendMessage(sv + wp);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + type + "/fly [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + np);
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
				player.sendMessage(sv + np);
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
				player.sendMessage(sv + np);
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
				player.sendMessage(sv + np);
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
				player.sendMessage(sv + np);
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
					message = message.replaceAll("&", cl);
					Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Broadcast> " + ChatColor.WHITE + message);
					for (Player player1 : Bukkit.getOnlinePlayers()) {
						player1.playSound(player1.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					}
				} else {
					player.sendMessage(sv + np);
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
						message = message.replaceAll("&", cl);
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
						message = message.replaceAll("&", cl);
						targetPlayer.chat(message);
						player.sendMessage(sv + "You forced " + ChatColor.YELLOW + targetPlayerName + ChatColor.GRAY
								+ ": " + ChatColor.AQUA + message);
					} else {
						player.sendMessage(sv + wp);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + np);
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
			int ping = getPing(player);
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
					int ping2 = getPing(targetPlayer);
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
					player.sendMessage(ChatColor.BLUE + "Server>" + ChatColor.GRAY + wp);
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
								player.sendMessage(sv + wp);
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
				player.sendMessage(sv + np);
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
				player.sendMessage(sv + np);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);

			}
		}
		if (CommandLabel.equalsIgnoreCase("tpr") || CommandLabel.equalsIgnoreCase("SMDMain:tpr")
				|| CommandLabel.equalsIgnoreCase("tprequest") || CommandLabel.equalsIgnoreCase("SMDMain:tprequest")) {
			int tprq = playerData.getInt("Quota.TPR");
			if (args.length == 1) {
				if (tprq < 1) {
					player.sendMessage(pp + "You don't have enough " + ChatColor.YELLOW + "TPR Quota!");
					player.sendMessage(
							pp + "Use " + ChatColor.AQUA + "/buyquota TPR" + ChatColor.GRAY + " to buy more quota.");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				} else {
					if (Bukkit.getServer().getPlayer(args[0]) != null) {
						Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
						String targetPlayerName = targetPlayer.getName();
						if (targetPlayerName == playerName) {
							player.sendMessage(pp + "You can't teleport to yourself!");
						} else if (targetPlayerName != playerName) {
							getConfig().set("Teleport." + targetPlayerName, playerName);
							saveConfig();
							player.sendMessage(pp + ChatColor.GREEN + "You sent teleportion request to "
									+ ChatColor.YELLOW + targetPlayerName);
							targetPlayer.sendMessage(pp + "Player " + ChatColor.YELLOW + playerName + ChatColor.GRAY
									+ " sent teleportion request to you");
							targetPlayer.sendMessage(pp + ChatColor.GREEN + "/tpaccept " + ChatColor.YELLOW + playerName
									+ ChatColor.GRAY + " to" + ChatColor.GREEN + " accept " + ChatColor.GRAY
									+ "teleportion request.");
							targetPlayer.sendMessage(pp + ChatColor.RED + "/tpdeny " + ChatColor.YELLOW + playerName
									+ ChatColor.GRAY + " to" + ChatColor.RED + " deny " + ChatColor.GRAY
									+ "teleportion request.");
						}
					} else {
						player.sendMessage(pp + wp);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				}
			} else {
				player.sendMessage(pp + type + "/tpr [player]");
			}
		}
		if (CommandLabel.equalsIgnoreCase("tpaccept") || CommandLabel.equalsIgnoreCase("SMDMain:tpaccept")) {
			if (args.length == 1) {
				if (Bukkit.getServer().getPlayer(args[0]) != null) {
					Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
					String targetPlayerName = targetPlayer.getName();
					File userdata1 = new File(
							Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
							File.separator + "PlayerDatabase/" + targetPlayerName);
					File f1 = new File(userdata1, File.separator + "config.yml");
					FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
					int tprq = playerData1.getInt("Quota.TPR");
					if (getConfig().getString("Teleport." + playerName) == (targetPlayerName)) {
						getConfig().set("Teleport." + playerName, "None");
						saveConfig();
						player.sendMessage(pp + ChatColor.GREEN + "You accept teleportion request from "
								+ ChatColor.YELLOW + targetPlayerName + ".");
						targetPlayer.sendMessage(pp + "Player " + ChatColor.YELLOW + playerName + ChatColor.GREEN
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
						targetPlayer.sendMessage(pp + "You have " + ChatColor.YELLOW + tprq2 + " TPR Quota left.");
					} else if (getConfig().getString("Teleport." + targetPlayerName) == ("None")) {
						player.sendMessage(pp + "You didn't have any request from anyone");
					} else {
						player.sendMessage(pp + "You don't have any teleportion request from " + ChatColor.YELLOW
								+ targetPlayerName + ".");
					}
				} else {
					player.sendMessage(pp + wp);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(pp + type + "/tpaccept [player]");
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
						player.sendMessage(pp + ChatColor.RED + "You deny teleportion request from " + ChatColor.YELLOW
								+ targetPlayerName + ".");
						targetPlayer.sendMessage(pp + "Player " + ChatColor.YELLOW + playerName + ChatColor.RED
								+ " deny " + ChatColor.GRAY + "your teleportion request.");
					} else if (getConfig().getString("Teleport." + targetPlayerName).equalsIgnoreCase("None")) {
						player.sendMessage(pp + "You didn't have any request from anyone");
					} else {
						player.sendMessage(pp + "You don't have any teleportion request from " + ChatColor.YELLOW
								+ targetPlayerName + ".");
					}
				} else {
					player.sendMessage(pp + wp);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(pp + "Type: " + ChatColor.GREEN + "/tpdeny [player]");
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("qwertyuiop")) {
			ActionBarAPI.send(player, "test");
			ActionBarAPI.sendToAll("test");
		}
		if (CommandLabel.equalsIgnoreCase("platewarp")) {
			Location loc = player.getLocation();
			loc.setY(loc.getY());
			Location locs = player.getLocation();
			locs.setY(loc.getY() - 2);
			Block block = loc.getBlock();
			Block blocks = locs.getBlock();
			String w = getConfig().getString("WarpState." + playerName);
			if (block.getType() == Material.GOLD_PLATE || block.getType() == Material.IRON_PLATE) {
				if (blocks.getType() == Material.SIGN_POST || blocks.getType() == Material.WALL_SIGN) {
					Sign sign = (Sign) blocks.getState();
					if (sign.getLine(0).equalsIgnoreCase("[tp]")) {
						if (w.equalsIgnoreCase("1")) {
							ActionBarAPI.send(player, tc + ChatColor.GOLD + "▃ " + ChatColor.GRAY + "▄ ▅ ▆ ▇");
							player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, (float) 0.3);
							player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
							player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
							player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
							getConfig().set("WarpState." + playerName, "2");
							saveConfig();
							getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
								@Override
								public void run() {
									player.performCommand("platewarp");
								}
							}, 10);
						} else if (w.equalsIgnoreCase("2")) {
							if (player.isSneaking() == false) {
								getConfig().set("WarpState." + playerName, "false");
								saveConfig();
								ActionBarAPI.send(player, ct);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
							} else {
								ActionBarAPI.send(player, tc + ChatColor.GOLD + "▃ ▄ " + ChatColor.GRAY + "▅ ▆ ▇");
								player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, (float) 0.5);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								getConfig().set("WarpState." + playerName, "3");
								saveConfig();
								getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

									@Override
									public void run() {
										player.performCommand("platewarp");
									}
								}, 10);
							}
						} else if (w.equalsIgnoreCase("3")) {
							if (player.isSneaking() == false) {
								getConfig().set("WarpState." + playerName, "false");
								saveConfig();
								ActionBarAPI.send(player, ct);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
							} else {
								ActionBarAPI.send(player, tc + ChatColor.GOLD + "▃ ▄ ▅ " + ChatColor.GRAY + "▆ ▇");
								player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, (float) 0.7);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);

								getConfig().set("WarpState." + playerName, "4");
								saveConfig();
								getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
									@Override
									public void run() {
										player.performCommand("platewarp");
									}
								}, 10);
							}
						} else if (w.equalsIgnoreCase("4")) {
							if (player.isSneaking() == false) {
								getConfig().set("WarpState." + playerName, "false");
								saveConfig();
								ActionBarAPI.send(player, ct);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
							} else {
								ActionBarAPI.send(player, tc + ChatColor.GOLD + "▃ ▄ ▅ ▆ " + ChatColor.GRAY + "▇");
								player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, (float) 0.9);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);

								getConfig().set("WarpState." + playerName, "5");
								saveConfig();
								getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
									@Override
									public void run() {
										player.performCommand("platewarp");
									}
								}, 10);
							}
						} else if (w.equalsIgnoreCase("5")) {
							if (player.isSneaking() == false) {
								getConfig().set("WarpState." + playerName, "false");
								saveConfig();
								ActionBarAPI.send(player, ct);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
							} else {
								ActionBarAPI.send(player, tc + ChatColor.GOLD + "▃ ▄ ▅ ▆ ▇");
								player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, (float) 1.2);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);
								player.playEffect(player.getLocation(), Effect.LAVA_POP, 10);

								getConfig().set("WarpState." + playerName, "6");
								saveConfig();
								getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
									@Override
									public void run() {
										player.performCommand("platewarp");
									}
								}, 15);
							}
						} else if (w.equalsIgnoreCase("6")) {
							if (block.getType() == Material.GOLD_PLATE || block.getType() == Material.IRON_PLATE) {
								Location loc2 = player.getLocation();
								loc2.setY(loc.getY() - 2);
								Block block2 = loc2.getBlock();
								if ((block2.getType() == Material.SIGN_POST
										|| block2.getType() == Material.WALL_SIGN)) {
									Location loc3 = player.getLocation();
									loc3.setY(loc.getY() - 3);
									Block block3 = loc3.getBlock();
									Sign s1 = (Sign) block2.getState();
									Sign s2 = (Sign) block3.getState();
									if (s1.getLine(0).equalsIgnoreCase("[tp]")
											&& s2.getLine(0).equalsIgnoreCase("[world]")) {
										if (block3.getType() == Material.SIGN_POST
												|| block3.getType() == Material.WALL_SIGN) {
											World world = Bukkit
													.getWorld(s2.getLine(1) + s2.getLine(2) + s2.getLine(3));
											if (world != null) {
												Location pl = player.getLocation();
												double xh = Integer.parseInt(s1.getLine(1));
												double yh = Integer.parseInt(s1.getLine(2));
												double zh = Integer.parseInt(s1.getLine(3));
												double x = xh + 0.5;
												double y = yh;
												double z = zh + 0.5;
												double yaw = pl.getYaw();
												double pitch = pl.getPitch();
												Location loca = new Location(world, x, y, z);
												loca.setPitch((float) pitch);
												loca.setYaw((float) yaw);
												player.teleport(loca);
												ActionBarAPI.send(player,
														ChatColor.GREEN + "" + ChatColor.BOLD + "Teleport!");
												player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10,
														2);
											} else {
												ActionBarAPI.send(player,
														ChatColor.RED + "World " + ChatColor.WHITE + s2.getLine(1)
																+ s2.getLine(2) + s2.getLine(3) + ChatColor.RED
																+ " not found");
												player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
											}
										} else {
										}
									} else {
									}
								} else {
								}
							} else {
							}
							getConfig().set("WarpState." + playerName, "false");
							saveConfig();
						}
					}
				}
			} else {
				getConfig().set("WarpState." + playerName, "false");
				saveConfig();
				ActionBarAPI.send(player, ct);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("countdown") || CommandLabel.equalsIgnoreCase("SMDMain:countdown")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.countdown")) {
				if (args.length != 0) {
					if (args[0].equalsIgnoreCase("start")) {
						if (args.length == 2) {
							if (isInt(args[1])) {
								int i = Integer.parseInt(args[1]);
								player.sendMessage(sv + "Set timer to " + ChatColor.YELLOW + args[1] + " seconds");
								getConfig().set("count", i);
								saveConfig();
							} else {
								player.sendMessage(
										sv + ChatColor.YELLOW + args[1] + ChatColor.GRAY + " is not number.");
							}
						} else if (args.length > 2) {
							if (isInt(args[1])) {
								int l = Integer.parseInt(args[1]);
								for (int i = 2; i != args.length; i++)
									message += args[i] + " ";
								message = message.replaceAll("&", cl);
								getConfig().set("countdown_msg", message);
								saveConfig();
								getConfig().set("count", l);
								player.sendMessage(sv + "Set timer to " + ChatColor.YELLOW + args[1]
										+ " seconds with message " + ChatColor.GREEN + message);
							} else {
								player.sendMessage(
										sv + ChatColor.YELLOW + args[1] + ChatColor.GRAY + " is not number.");
							}

						} else {
							player.sendMessage(sv + type + "/countdown start [second] [message]");
						}
					}
					if (args[0].equalsIgnoreCase("stop")) {
						player.sendMessage(sv + "Stopped Countdown");
						ActionBarAPI.sendToAll(cd + "Countdown has been cancelled");
						getConfig().set("countdown_msg", "Undefined");
						getConfig().set("count", -1);
						saveConfig();
					}
				} else {
					player.sendMessage(sv + type + "/countdown [start/stop] [second]");
				}
			} else {
				player.sendMessage(sv + np);
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
								File.separator + "PlayerDatabase/" + targetPlayerName);
						File f1 = new File(userdata1, File.separator + "config.yml");
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
								message = message.replaceAll("&", cl);
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
						player.sendMessage(sv + wp);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + type + "/mute [player] [reason]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + np);
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
									File.separator + "PlayerDatabase/" + targetPlayerName);
							File f1 = new File(userdata1, File.separator + "config.yml");
							FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
							int countwarn = playerData1.getInt("warn");
							message = "";
							for (int i = 1; i != args.length; i++)
								message += args[i] + " ";
							message = message.replaceAll("&", cl);
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
							player.sendMessage(sv + wp);
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
						}
					}
				} else {
					player.sendMessage(sv + type + "/warn [player] [reason]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + np);
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
								File.separator + "PlayerDatabase/" + targetPlayerName);
						File f1 = new File(userdata1, File.separator + "config.yml");
						FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
						message = "";
						for (int i = 1; i != args.length; i++) // catch args[0]
																// -> i = 0
							message += args[i] + " ";
						message = message.replaceAll("&", cl);
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
						player.sendMessage(sv + wp);
					}
				} else {
					player.sendMessage(sv + type + "/resetwarn [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + np);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("buyquota") || CommandLabel.equalsIgnoreCase("SMDMain:buyquota")) {
			double money = playerData.getDouble("money");
			int tprq = playerData.getInt("Quota.TPR");
			int lcq = playerData.getInt("Quota.LuckyClick");
			int homeq = playerData.getInt("Quota.Home");
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
				} else if (args[0].equalsIgnoreCase("home")) {
					if (money > 5000) {
						try {
							playerData.set("Quota.Home", lcq + 1);
							playerData.set("money", money - 5000);
							playerData.save(f);
						} catch (IOException e) {
							e.printStackTrace();
						}
						player.sendMessage(sv + "You " + ChatColor.YELLOW + "paid 5000 Coins" + ChatColor.GRAY
								+ " to brought " + ChatColor.LIGHT_PURPLE + "1x Extend Sethome Limit");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
					} else {
						player.sendMessage(sv + nom);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + type + "/buyquota [tpr|luckyclick|home]");
					player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 10, 0);
				}
			} else {
				player.sendMessage(sv + "Welcome to " + ChatColor.YELLOW + ChatColor.BOLD + "Quota's Shop");
				player.sendMessage(ChatColor.GREEN + "Pricing List " + ChatColor.WHITE + ":");
				player.sendMessage("- " + ChatColor.GREEN + "15x TPR Quota" + ChatColor.YELLOW + " 3000 Coin");
				player.sendMessage(
						"- " + ChatColor.LIGHT_PURPLE + "3x Lucky Click Quota" + ChatColor.YELLOW + " 1500 Coin");
				player.sendMessage("- " + ChatColor.AQUA + "1x Extend Sethome Limit" + ChatColor.YELLOW + " 5000 Coin");
				player.sendMessage(type + "/buyquota [tpr|luckyclick|home]");
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
								File.separator + "PlayerDatabase/" + targetPlayerName);
						File f1 = new File(userdata1, File.separator + "config.yml");
						FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
						int homeq = playerData1.getInt("Quota.Home");
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
								playerData1.set("Quota.Home", 20);
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
								playerData1.set("Quota.Home", 3);
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
								playerData1.set("Quota.Home", 7);
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
								playerData1.set("Quota.Home", 10);
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
								playerData1.set("Quota.Home", 100);
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
								playerData1.set("Quota.Home", 100);
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
						player.sendMessage(sv + ChatColor.RED + wp);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(
							ChatColor.BLUE + "Rank> " + type + "/rank [default|vip|mvp|staff|admin|owner] [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + np);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("status") || CommandLabel.equalsIgnoreCase("SMDMain:status")) {
			String nnn = "";
			if (args.length == 0) {
				nnn = playerName + "";
			} else {
				if (Bukkit.getServer().getPlayer(args[0]) != null) {
					nnn = Bukkit.getServer().getPlayer(args[0]).getName();
				} else {
					nnn = playerName + "";
				}
			}
			Player targetPlayer = player.getServer().getPlayer(nnn);
			String targetPlayerName = targetPlayer.getName();
			File userdata1 = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
					File.separator + "PlayerDatabase/" + targetPlayerName);
			File f1 = new File(userdata1, File.separator + "config.yml");
			FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
			player.sendMessage(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "----[" + ChatColor.WHITE + "STATS"
					+ ChatColor.YELLOW + ChatColor.STRIKETHROUGH + "]----");
			player.sendMessage("Name: " + ChatColor.AQUA + nnn);
			player.sendMessage("UUID: " + ChatColor.LIGHT_PURPLE + targetPlayer.getUniqueId().toString());
			player.sendMessage("Mute: ");
			String muteis = playerData1.getString("mute.is");
			String mutere = playerData1.getString("mute.reason");
			String freeze = playerData1.getString("freeze");
			int countwarn = playerData1.getInt("warn");
			int tprq = playerData1.getInt("Quota.TPR");
			int lcq = playerData1.getInt("Quota.LuckyClick");
			int homeq = playerData1.getInt("Quota.Home");
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
			player.sendMessage("  - Home: " + ChatColor.GREEN + homeq);
		}
		if (CommandLabel.equalsIgnoreCase("wiki") || CommandLabel.equalsIgnoreCase("SMDMain:wiki")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("rule")) {
					player.sendMessage(ChatColor.GREEN + "Topic: " + ChatColor.YELLOW + "RULE");
					player.sendMessage(
							"1) เธซเน�เธฒเธกเน�เธ�เน�เธ�เธฑเธ�เธ�เธญเธ�เน�เธ” เน� เน�เธซเน�เน€เธ�เน�เธ�เธ�เธฃเธฐเน�เธขเธ�เธ�เน�เธชเน�เธงเธ�เธ�เธธเธ�เธ�เธฅ (เน€เธ�เน�เธ� เธ•เน�เธญเธ•เธฑเธง, เน�เธซเธ� Private เธฏเธฅเธฏ)");
					player.sendMessage(
							"  1.1) เธ�เธฃเธ“เธตเน€เธ�เน�เธ�เธ�เธฑเธ�เธ�เธญเธ� Minecraft เธญเธขเธนเน�เน�เธฅเน�เธง เน�เธกเน�เธ�เธฑเธ�เน€เธ�เน�เธ�เธ�เธงเธฒเธกเธ�เธดเธ”");
					player.sendMessage(
							"  1.2) เธ�เธฃเธ“เธตเธ—เธตเน�เธ—เธธเธ�เธ�เธ� เน�เธ”เน�เธ�เธฃเธฐเน�เธขเธ�เธ�เน�เธฃเน�เธงเธกเธ�เธฑเธ� (เธชเธฒเธ�เธฒเธฃเธ“เธฐ) เธ�เธฐเน�เธกเน�เธ�เธฑเธ�เน€เธ�เน�เธ�เธ�เธงเธฒเธกเธ�เธดเธ”");
					player.sendMessage(
							"  1.3) เธซเธฒเธ�เธ�เธ�เน€เธซเน�เธ�เธ�เธฑเธ�เธ�เธญเธ� 'Plugin' เน�เธซเน�เน�เธ�เน�เธ�เธ—เธตเน� SMD_SSG_PJ เธ�เธฐเธกเธตเธฃเธฒเธ�เธงเธฑเธฅเน�เธซเน�เธ”เน�เธงเธข");
					player.sendMessage(
							"2) เน�เธ�เธ�เธฒเธฃเน€เธ•เธทเธญเธ�เธ�เธงเธฒเธกเธ�เธดเธ”เน�เธ” เน� เน�เธ�เน�เธ�เธนเน�เน€เธฅเน�เธ� เธ�เธงเธฃเธชเธญเธ�เธ–เธฒเธกเธ�เธงเธฒเธกเน€เธ�เน�เธ�เธกเธฒเธ�เน�เธญเธ� เธงเน�เธฒเน€เธ�เธดเธ”เน€เธซเธ•เธธเธ�เธฒเธฃเธ“เน�เธญเธฐเน�เธฃเธ�เธถเน�เธ� เธ—เธณเน�เธกเธ–เธถเธ�เธ—เธณเน�เธ�เธ�เธ�เธฑเน�เธ� เธฏเธฅเธฏ เธ�เน�เธญเธ�เธ—เธตเน�เธ�เธฐ Warn เธ—เธธเธ�เธ�เธฃเธฑเน�เธ� เธซเธฒเธ�เธกเธตเน€เธซเธ•เธธเธ�เธฅเธ—เธตเน�เน�เธกเน�เน€เธซเธกเธฒเธฐเธชเธกเธ�เธฐเธกเธตเธ�เธฒเธฃเธ�เธดเธ�เธฒเธฃเธ“เธฒ");
					player.sendMessage(
							"3) เธซเน�เธฒเธก Mod เธ—เธตเน�เธ—เธณเน�เธซเน�เธกเธตเธ�เธงเธฒเธกเธชเธฒเธกเธฒเธฃเธ–เธ�เธดเน€เธจเธฉเน€เธซเธ�เธทเธญเธ�เธงเน�เธฒเธ�เธนเน�เธญเธทเน�เธ� เน€เธ�เน�เธ� Kill Aura, XRay เธชเน�เธงเธ�เธ�เธงเธ� LightMap เธ–เธทเธญเธงเน�เธฒเธญเธ�เธธเน�เธฅเธกเน€เธ�เธทเน�เธญเธ�เธ�เธฒเธ�เน�เธกเน�เธ—เธณเน�เธซเน�เธ�เธนเน�เน€เธฅเน�เธ�เธญเธทเน�เธ�เน€เธชเธตเธขเน€เธ�เธฃเธตเธขเธ�เธกเธฒเธ�");
					player.sendMessage(
							"4) เธซเน�เธฒเธกเธ�เน�เธกเธขเธ�เธญเธ� - เธ—เธณเธฅเธฒเธขเธชเธดเน�เธ�เธ�เน�เธญเธชเธฃเน�เธฒเธ�เธ�เธญเธ�เธ�เธนเน�เธญเธทเน�เธ�เธ�เน�เธญเธ�เน�เธ”เน�เธฃเธฑเธ�เธญเธ�เธธเธ�เธฒเธ•เธ�เธฒเธ�เน€เธ�เน�เธฒเธ�เธญเธ� 'เธขเธ�เน€เธงเน�เธ�' เธ�เธฃเธ“เธตเธ�เธญเธ�เธ�เธฅเน�เธญเธ�เธชเธฒเธ�เธฒเธฃเธ“เธฐ เธซเธฒเธ�เธกเธตเธ�เธฒเธฃเธฃเธฐเธ�เธธเน�เธงเน� 'เธ�เธฑเธ”เน€เธ�เธ�' เน€เธ�เน�เธฒเธ�เธญเธ�เธ�เธฐเน�เธกเน�เธชเธฒเธกเธฒเธฃเธ–เน�เธ�เน�เธ�เธ�เธงเธฒเธกเธ�เธดเธ”เน�เธ” เน� เน�เธ”เน�");

				} else if (args[0].equalsIgnoreCase("warn")) {
					player.sendMessage(ChatColor.GREEN + "Topic: " + ChatColor.YELLOW + "WARN");
					player.sendMessage("Q: เธ�เธฒเธฃ Warn เธ�เธทเธญเธญเธฐเน�เธฃ?");
					player.sendMessage(
							"A: เธ�เธฒเธฃ Warn เธ�เธทเธญเธ�เธฒเธฃเน€เธ•เธทเธญเธ�เธ�เธนเน�เน€เธฅเน�เธ�เน€เธกเธทเน�เธญเธ�เธนเน�เน€เธฅเน�เธ�เธ�เธฃเธฐเธ—เธณเธ�เธดเธ”เธ�เธฒเธ�เธ�เธ�เธ—เธตเน�เน�เธ”เน�เธ�เธณเธซเธ�เธ”เน�เธงเน� (/wiki rule)");
					player.sendMessage("");
					player.sendMessage(
							"Q: เธ–เน�เธฒเน�เธ”เธ� Warn เน€เธขเธญเธฐ เน� เน�เธฅเน�เธงเธ�เธฐเน€เธ�เธดเธ”เธญเธฐเน�เธฃเธ�เธถเน�เธ�?");
					player.sendMessage(
							"A: เธ—เธฒเธ�เน€เธฃเธฒเธ�เธฐเน�เธซเน�เน�เธญเธ�เธฒเธชเธ—เธฑเน�เธ�เธซเธกเธ” 3 เธ�เธฃเธฑเน�เธ� เธซเธฒเธ�เธกเธตเธ�เธฃเธฑเน�เธ�เธ—เธตเน� 4 เธ�เธฐเธ–เธทเธญเน€เธ�เน�เธ�เธ�เธฒเธฃเน�เธ”เธ�เน�เธ�เธ� เน�เธ”เธขเธ�เธนเน�เน€เธฅเน�เธ�เธชเธฒเธกเธฒเธฃเธ–เธ�เธฅเธ” Ban เน�เธ”เน�เน�เธ�เธฃเธฒเธ�เธฒ 50 TrueWallet (เธ•เธดเธ”เธ•เน�เธญ SMD_SSG_PJ) เน�เธ”เน�เน�เธกเน�เน€เธ�เธดเธ� 2 เธ�เธฃเธฑเน�เธ� เธซเธฒเธ�เธ–เธนเธ� Ban เธ�เธฃเธ� 3 เธ�เธฃเธฑเน�เธ�เธ�เธฐเน�เธกเน�เธกเธตเธชเธดเธ—เธ�เธดเน�เน�เธ�เธ�เธฒเธฃเธ�เธฅเธ”เน�เธ�เธ�เธญเธตเธ�เธ•เน�เธญเน�เธ�");
					player.sendMessage("");
					player.sendMessage(
							"Q: เธ�เธกเธ–เธนเธ� Warn เน�เธ”เธขเธ—เธตเน�เน�เธกเน�เน�เธ”เน�เธ�เธฃเธฐเธ—เธณเธ�เธดเธ” เธ•เน�เธญเธ�เธ—เธณเน�เธ�เธญเธฐเธ�เธฃเธฑเธ�?");
					player.sendMessage(
							"A: เน�เธซเน�เธ�เธนเน�เธ—เธตเน�เธ–เธนเธ� Warn เน�เธ�เธ�เน€เธซเธ•เธธเธ�เธฅเธ—เธตเน�เน�เธ”เธ� Warn (Reason) เน�เธฅเน�เธงเธ•เธดเธ”เธ•เน�เธญเธกเธฒเธขเธฑเธ� SMD_SSG_PJ เธ�เธฃเธฑเธ� เน�เธ”เธขเธ—เธฒเธ�เน€เธฃเธฒเธ�เธฐเธ•เธฃเธงเธ�เธชเธญเธ�เธ�เธงเธฒเธกเน€เธ�เน�เธ�เธกเธฒเธญเธตเธ�เธ�เธฃเธฑเน�เธ�เน�เธฅเธฐเธ�เธดเธ�เธฒเธฃเธ“เธฒเน�เธซเธกเน�เธญเธตเธ�เธ�เธฃเธฑเน�เธ�เธ�เธฃเธฑเธ�");
				} else if (args[0].equalsIgnoreCase("luckyclick")) {
					player.sendMessage(ChatColor.GREEN + "Topic: " + ChatColor.YELLOW + "LUCKYCLICK");
					player.sendMessage("LuckyClick เธ�เธทเธญเธญเธฐเน�เธฃ");
					player.sendMessage(
							"LuckyClick เน€เธ�เน�เธ�เธฃเธฐเธ�เธ�เธชเธธเน�เธกเธ�เธญเธ�เธ�เธฒเธ�เธ�เน�เธฒเธข เน�เธ”เธขเธกเธตเธ—เธฑเน�เธ�เธ�เธญเธ�เธ”เธตเน�เธฅเธฐเธ�เธญเธ�เน�เธกเน�เธ”เธต เธชเธฒเธกเธฒเธฃเธ–เธชเธธเน�เธกเน�เธ”เน�เธ—เธตเน�เธ�เน�เธฒเธข [LuckyClick] เธ—เธตเน�เธชเธ�เธฒเธง (เธ•เน�เธญเธ�เธกเธต Quota เธ�เน�เธญเธ� เธชเธฒเธกเธฒเธฃเธ–เธ�เธทเน�เธญเน�เธ”เน�เน�เธ”เธขเน�เธ�เน�เธ�เธณเธชเธฑเน�เธ� /buyquota luckyclick เน�เธ�เธฃเธฒเธ�เธฒ 1500 Coin(s) เธ•เน�เธญ 3 LuckyClick Quota");
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
						File.separator + "PlayerDatabase." + targetPlayer);
				File f1 = new File(userdata1, File.separator + "config.yml");
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
							long money = playerData1.getLong("money");
							int tprq = playerData1.getInt("Quota.TPR");
							int lcq = playerData1.getInt("Quota.LuckyClick");
							int homeq = playerData1.getInt("Quota.Home");
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
								playerData.set("Quota.Home", homeq);
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
								playerData.set("Quota.Home", homeq);
							}

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
		if (CommandLabel.equalsIgnoreCase("invisible")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.invisible")) {
				String invi = playerData.getString("Invisible");
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.hasPermission("main.seeinvisible") || p.isOp() || p.hasPermission("main.*")) {
						if (invi.equalsIgnoreCase("true")) {
							player.hidePlayer(player);
							player.sendMessage(sv + "You're now " + ChatColor.AQUA + "invisible.");
						} else {
							player.showPlayer(player);
							player.sendMessage(sv + "You're now " + ChatColor.LIGHT_PURPLE + "visible.");
						}
					} else {
					}
				}
			} else {
				player.sendMessage(sv + np);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("givequota")) {
			if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.givequota")) {
				if (args.length == 3) {
					Player targetPlayer = player.getServer().getPlayer(args[0]);
					String targetPlayerName = targetPlayer.getName();
					File userdata1 = new File(
							Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
							File.separator + "PlayerDatabase/" + targetPlayerName);
					File f1 = new File(userdata1, File.separator + "config.yml");
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
								player.sendMessage(sv + ChatColor.YELLOW + args[2] + non);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						} else if (args[1].equalsIgnoreCase("luckyclick")) {
							if (isInt(args[2])) {
								int lcqn = lcq + Integer.parseInt(args[2]);
								try {
									playerData1.set("Quota.LuckyClick", lcqn);
									playerData1.save(f1);
								} catch (IOException e) {
									e.printStackTrace();
								}
								player.sendMessage(sv + "You gave " + ChatColor.LIGHT_PURPLE + args[2]
										+ "x LuckyClick Quota to " + ChatColor.YELLOW + targetPlayerName);
							} else {
								player.sendMessage(sv + ChatColor.YELLOW + args[2] + non);
								player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
							}
						} else

						{
							player.sendMessage(sv + type + "/givequota [player] [type] [amount]");
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
						}
					} else {
						player.sendMessage(sv + wp);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + type + "/givequota [player] [type] [amount]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + np);
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
					message = message.replaceAll("&", cl);
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
				player.sendMessage(sv + np);
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
									File.separator + "PlayerDatabase/" + targetPlayerName);
							File f1 = new File(userdata1, File.separator + "config.yml");
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
				player.sendMessage(sv + np);
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
				player.sendMessage(sv + np);
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
								File.separator + "PlayerDatabase/" + targetPlayerName);
						File f1 = new File(userdata1, File.separator + "config.yml");
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
							playerData1.set("Quota.Home", 3);
							playerData1.createSection("Security");
							playerData1.set("Security.password", "none");
							playerData1.set("Security.email", "none");
							playerData1.set("Security.freeze", "true");

							getConfig().set("redeem." + playerName, "false");
							getConfig().set("event.queuelist." + playerName, "false");
							saveConfig();
							playerData1.save(f1);
						} catch (

						IOException e) {
							e.printStackTrace();
						}
						Bukkit.broadcastMessage(sv + "Player " + ChatColor.YELLOW + targetPlayerName + "'s information "
								+ ChatColor.GRAY + "has been " + ChatColor.RED + "reset " + ChatColor.GRAY + "by "
								+ ChatColor.AQUA + playerName + ".");
						for (Player p : Bukkit.getOnlinePlayers()) {
							p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 2, 2);
						}
					} else {
						player.sendMessage(sv + wp);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + type + "/resetconfig [player]");
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
				}
			} else {
				player.sendMessage(sv + np);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			}
		}
		if (CommandLabel.equalsIgnoreCase("money") || CommandLabel.equalsIgnoreCase("SMDMain:money")) {
			long money = playerData.getLong("money");
			player.sendMessage(sv + "Your balance is " + ChatColor.YELLOW + money + " Coin(s)");
		}
		if (CommandLabel.equalsIgnoreCase("paymoney") || CommandLabel.equalsIgnoreCase("SMDMain:paymoney")) {
			long money = playerData.getLong("money");
			if (args.length == 2) {
				if (Bukkit.getServer().getPlayer(args[0]) != null) {
					Player targetPlayer = player.getServer().getPlayer(args[0]);
					String targetPlayerName = targetPlayer.getName();
					File userdata1 = new File(
							Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
							File.separator + "PlayerDatabase/" + targetPlayerName);
					File f1 = new File(userdata1, File.separator + "config.yml");
					FileConfiguration playerData1 = YamlConfiguration.loadConfiguration(f1);
					long targetPlayerMoney = playerData1.getLong("money");
					if (isInt(args[1])) {
						long paymoney = Integer.parseInt(args[1]);
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
						player.sendMessage(sv + ChatColor.YELLOW + args[1] + non);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
					}
				} else {
					player.sendMessage(sv + wp);
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
				File.separator + "PlayerDatabase/" + playerName);
		File f = new File(userdata, File.separator + "config.yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, (float) 0.5, 1);
		}
		if (!f.exists()) {
			File userfiles;
			try {
				userfiles = new File(
						getDataFolder() + File.separator + "/PlayerDatabase/" + playerName + "/HomeDatabase");
				if (!userfiles.exists()) {
					userfiles.mkdirs();
				}
			} catch (SecurityException e) {
				return;
			}
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
				playerData.set("Quota.Home", 3);
				playerData.createSection("Invisible");
				playerData.set("Invisible", "false");
				playerData.createSection("Security");
				playerData.set("Security.password", "none");
				playerData.set("Security.email", "none");
				getConfig().set("redeem." + playerName, "false");
				getConfig().set("event.queuelist." + playerName, "false");
				saveConfig();
				playerData.save(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String evs = getConfig().getString("event.queuelist." + playerName);
		if (evs == null || evs.isEmpty()) {
			getConfig().set("event.queuelist." + playerName, "false");
			saveConfig();
		} else {
			return;
		}
		if (player.isOp() || player.hasPermission("main.*") || player.hasPermission("main.see")) {
			player.sendMessage(ChatColor.GREEN + "You have perm. 'main.see', You will see command that player using");
		}
		getConfig().set("login_freeze." + playerName, "false");
		getConfig().set("WarpState." + playerName, "false");
		saveConfig();
		if (f.exists()) {
			try {
				playerData.createSection("uuid");
				playerData.set("uuid", player.getUniqueId().toString());
				playerData.set("Security.freeze", "true");
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
				event.setJoinMessage(j + ChatColor.BLUE + playerName);
			} else if (rank.equalsIgnoreCase("staff")) {
				player.setPlayerListName(
						ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Staff" + ChatColor.BLUE + playerName);
				player.setDisplayName(
						ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Staff" + ChatColor.BLUE + playerName);
				event.setJoinMessage(
						j + ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Staff" + ChatColor.BLUE + playerName);
			} else if (rank.equalsIgnoreCase("vip")) {
				player.setPlayerListName(
						ChatColor.GREEN + "" + ChatColor.BOLD + "VIP" + ChatColor.DARK_GREEN + playerName);
				player.setDisplayName(
						ChatColor.GREEN + "" + ChatColor.BOLD + "VIP" + ChatColor.DARK_GREEN + playerName);
				event.setJoinMessage(j + ChatColor.GREEN + ChatColor.BOLD + "VIP" + ChatColor.DARK_GREEN + playerName);
			} else if (rank.equalsIgnoreCase("mvp")) {
				player.setPlayerListName(
						ChatColor.AQUA + "" + ChatColor.BOLD + "MVP" + ChatColor.DARK_AQUA + player.getName());
				player.setDisplayName(
						ChatColor.AQUA + "" + ChatColor.BOLD + "MVP" + ChatColor.DARK_AQUA + player.getName());
				event.setJoinMessage(j + ChatColor.AQUA + ChatColor.BOLD + "MVP" + ChatColor.DARK_AQUA + playerName);
			} else if (rank.equalsIgnoreCase("admin")) {
				player.setPlayerListName(
						ChatColor.DARK_RED + "" + ChatColor.BOLD + "Admin" + ChatColor.RED + player.getName());
				player.setDisplayName(
						ChatColor.DARK_RED + "" + ChatColor.BOLD + "Admin" + ChatColor.RED + player.getName());
				event.setJoinMessage(
						j + ChatColor.DARK_RED + "" + ChatColor.BOLD + "Admin" + ChatColor.RED + playerName);
			} else if (rank.equalsIgnoreCase("owner")) {
				player.setPlayerListName(
						ChatColor.GOLD + "" + ChatColor.BOLD + "Owner" + ChatColor.YELLOW + player.getName());
				player.setDisplayName(
						ChatColor.GOLD + "" + ChatColor.BOLD + "Owner" + ChatColor.YELLOW + player.getName());
				event.setJoinMessage(
						j + ChatColor.GOLD + "" + ChatColor.BOLD + "Owner" + ChatColor.YELLOW + playerName);
			}
		}
	}

	@EventHandler
	public void playerPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase/" + playerName);
		File f = new File(userdata, File.separator + "config.yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String freeze = playerData.getString("freeze");
		String l = getConfig().getString("login_freeze." + playerName);
		if (freeze.equalsIgnoreCase("true")) {
			event.setCancelled(true);
			ActionBarAPI.send(player, ChatColor.AQUA + "You're " + ChatColor.BOLD + "FREEZING");
		}
		if (l.equalsIgnoreCase("true")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void playerBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase/" + playerName);
		File f = new File(userdata, File.separator + "config.yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String freeze = playerData.getString("freeze");
		String l = getConfig().getString("login_freeze." + playerName);
		if (freeze.equalsIgnoreCase("true")) {
			event.setCancelled(true);
			ActionBarAPI.send(player, ChatColor.AQUA + "You're " + ChatColor.BOLD + "FREEZING");
		}
		if (l.equalsIgnoreCase("true")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void playerChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		String message2 = message.replaceAll("%", "%%");
		String messagem = message2.replaceAll("&", cl);
		String message1 = ChatColor.WHITE + ": " + messagem;
		Player player = event.getPlayer();
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase/" + playerName);
		File f = new File(userdata, File.separator + "config.yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String rank = playerData.getString("rank");
		String muteis = playerData.getString("mute.is");
		String mutere = playerData.getString("mute.reason");
		String l = getConfig().getString("login_freeze." + playerName);
		if (muteis.equalsIgnoreCase("true")) {
			player.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.GRAY + "You have been muted.");
			player.sendMessage(ChatColor.BLUE + "Chat> " + ChatColor.YELLOW + "Reason: " + ChatColor.GRAY + mutere);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 0);
			event.setCancelled(true);
		} else if (l.equalsIgnoreCase("true")) {
			event.setCancelled(true);
		} else {
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, (float) 0.5, 1);
			}
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
				File.separator + "PlayerDatabase/" + playerName);
		File f = new File(userdata, File.separator + "config.yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String freeze = playerData.getString("freeze");
		String l = getConfig().getString("login_freeze." + playerName);
		if (freeze.equalsIgnoreCase("true")) {
			event.setCancelled(true);
			ActionBarAPI.send(player, ChatColor.AQUA + "You're " + ChatColor.BOLD + "FREEZING");
			player.setAllowFlight(true);
		}
		if (l.equalsIgnoreCase("true")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void PlayerCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		String playerDisplay = player.getDisplayName();
		String command = event.getMessage().replaceAll("&", cl);
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase/" + playerName);
		File f = new File(userdata, File.separator + "config.yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String freeze = playerData.getString("freeze");
		if (freeze.equalsIgnoreCase("true")) {
			event.setCancelled(true);
			ActionBarAPI.send(player, ChatColor.AQUA + "You're " + ChatColor.BOLD + "FREEZING");
			Bukkit.broadcast(ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "CMD"
					+ ChatColor.DARK_PURPLE + "] " + ChatColor.RED + "(BLOCKED) " + playerDisplay + ChatColor.DARK_GRAY
					+ ": " + ChatColor.GREEN + command, "main.seecmd");
		}
		if (freeze.equalsIgnoreCase("false")) {
			Bukkit.broadcast(ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "CMD"
					+ ChatColor.DARK_PURPLE + "] " + ChatColor.RED + playerDisplay + ChatColor.GRAY + ": "
					+ ChatColor.GREEN + command, "main.seecmd");
		}
		if (l.equalsIgnoreCase("true")) {
			if (event.getMessage().endsWith("login") || event.getMessage().endsWith("register")
					|| event.getMessage().endsWith("recover")) {
			} else {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void QuitServer(PlayerQuitEvent event) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, (float) 0.5, 0);
		}
		Player player = event.getPlayer();
		String playerName = player.getName();
		File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("SMDMain").getDataFolder(),
				File.separator + "PlayerDatabase/" + playerName);
		File f = new File(userdata, File.separator + "config.yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		String rank = playerData.getString("rank");
		if (rank.equalsIgnoreCase("default")) {
			event.setQuitMessage(l + ChatColor.BLUE + player.getName());
		} else if (rank.equalsIgnoreCase("staff")) {
			event.setQuitMessage(l + ChatColor.DARK_BLUE + ChatColor.BOLD + "Staff" + ChatColor.BLUE + playerName);
		} else if (rank.equalsIgnoreCase("vip")) {
			event.setQuitMessage(l + ChatColor.GREEN + ChatColor.BOLD + "VIP" + ChatColor.DARK_GREEN + playerName);
		} else if (rank.equalsIgnoreCase("mvp")) {
			event.setQuitMessage(l + ChatColor.AQUA + ChatColor.BOLD + "MVP" + ChatColor.DARK_AQUA + playerName);
		} else if (rank.equalsIgnoreCase("admin")) {
			event.setQuitMessage(l + ChatColor.DARK_RED + ChatColor.BOLD + "Admin" + ChatColor.RED + playerName);
		} else if (rank.equalsIgnoreCase("owner")) {
			event.setQuitMessage(l + ChatColor.GOLD + ChatColor.BOLD + "Owner" + ChatColor.YELLOW + playerName);
		}
		try {
			playerData.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		getConfig().set("Teleport." + playerName, "None");
		getConfig().set("event.queuelist." + playerName, "false");
		saveConfig();
		int n = Bukkit.getServer().getOnlinePlayers().size();
		if (n == 0 || n < 0) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
		} else {
			return;
		}
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
				File.separator + "PlayerDatabase/" + playerName);
		File f = new File(userdata, File.separator + "config.yml");
		FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
		long money = playerData.getLong("money");
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
				} else {
					return;
				}
			}
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
						player.chat("I'm Gay~!");
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
				event.setLine(0, "ยง4ยงlSorryยงr, but");
				event.setLine(1, "You ยงlneed ยงrperm.");
				event.setLine(2, "or op to create sign with");
				event.setLine(3, "'" + line0 + "'" + " prefix!");
				player.sendMessage(sv + np);
				Bukkit.broadcastMessage(sv + "Player " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY
						+ " try to create sign " + ChatColor.RED + ChatColor.BOLD + line0);
				return;
			}
		}
	}

	@EventHandler
	public void PlayerStandOnPlate(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		Location loc = player.getLocation();
		loc.setY(loc.getY());
		String w = getConfig().getString("WarpState." + playerName);
		Block block = loc.getBlock();
		if (block.getType() == Material.GOLD_PLATE || block.getType() == Material.IRON_PLATE) {
			Location loc2 = player.getLocation();
			loc2.setY(loc.getY() - 2);
			Block block2 = loc2.getBlock();
			if ((block2.getType() == Material.SIGN_POST) || (block2.getType() == Material.WALL_SIGN)) {
				Sign sign = (Sign) block2.getState();
				if (sign.getLine(0).equalsIgnoreCase("[tp]")) {
					if (!w.equalsIgnoreCase("false")) {
						// Mean player currently stand on plate, No sending
						// holding shift message
					} else {
						player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 10));
						ActionBarAPI.send(player, ChatColor.YELLOW + "" + ChatColor.BOLD + "Hold " + ChatColor.GREEN
								+ ChatColor.BOLD + ChatColor.UNDERLINE + "Shift" + ChatColor.AQUA + " to teleport.");
					}
				}
			}
		}
	}

	@EventHandler
	public void PlayerUsePlate(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		String w = getConfig().getString("WarpState." + playerName);
		Location loc = player.getLocation();
		Location loc2 = player.getLocation();
		Location loc3 = player.getLocation();
		loc.setY(loc.getY());
		loc2.setY(loc.getY() - 2);
		loc3.setY(loc.getY() - 3);
		Block block = loc.getBlock();
		Block block2 = loc2.getBlock();
		Block block3 = loc3.getBlock();
		if (event.isSneaking() == true) {
			if ((block.getType() == Material.GOLD_PLATE || block.getType() == Material.IRON_PLATE)
					&& (block2.getType() == Material.SIGN_POST || block2.getType() == Material.WALL_SIGN)
					&& (block3.getType() == Material.SIGN_POST || block3.getType() == Material.WALL_SIGN)) {
				Sign s1 = (Sign) block2.getState();
				Sign s2 = (Sign) block3.getState();
				if (s1.getLine(0).equalsIgnoreCase("[tp]") && s2.getLine(0).equalsIgnoreCase("[world]")) {
					if (w.equalsIgnoreCase("false")) {
						getConfig().set("WarpState." + playerName, "1");
						saveConfig();
						player.performCommand("platewarp");
					} else {
						return;
					}
				} else {
					return;
				}
			} else {
				return;
			}
		} else

		{
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

	public void checkHour() {
		int c = getConfig().getInt("count");
		if (c > 3599) {
			int value = c;
			int h = value / 3600;
			int m = value % 3600;
			int s = m % 60;
			if (h == 1 && m == 1 && s == 1) {
				ActionBarAPI.sendToAll(cd + h + " hour " + m + " minute " + s + " second");
			} else if (h == 1 && m == 1 && s == 0) {
				ActionBarAPI.sendToAll(cd + h + " hour " + m + " minute");
			} else if (h == 1 && m == 0 && s == 1) {
				ActionBarAPI.sendToAll(cd + h + " hour " + m + " minute " + s + " second");
			} else if (h == 1 && m == 0 && s == 0) {
				ActionBarAPI.sendToAll(cd + h + " hour");
			} else if (h > 1 && m == 1 && s == 0) {
				ActionBarAPI.sendToAll(cd + h + " hour " + m + " minute");
			} else if (h > 1 && m == 1 && s == 1) {
				ActionBarAPI.sendToAll(cd + h + " hours " + m + " minute " + s + " second");
			} else if (h > 1 && m == 0 && s == 1) {
				ActionBarAPI.sendToAll(cd + h + " hours " + m + " minute " + s + " second");
			} else if (h > 1 && m == 0 && s == 0) {
				ActionBarAPI.sendToAll(cd + h + " hours");
			} else {
				ActionBarAPI.sendToAll(cd + h + " hours " + m + " minutes " + s + " seconds");
			}
		} else {
			checkMin();
		}
	}

	public void checkMin() {
		int c = getConfig().getInt("count");
		int value = c;
		int m = value / 60;
		int s = value % 60;
		if (c > 59 && c < 3600) {
			if (s == 0 && m != 1) {
				ActionBarAPI.sendToAll(cd + m + " minutes");
			} else if (s == 0 && m == 1) {
				ActionBarAPI.sendToAll(cd + m + " minute");
			} else if (s == 1 && m != 1) {
				ActionBarAPI.sendToAll(cd + m + " minutes " + s + " second");
			} else if (s == 1 && m == 1) {
				ActionBarAPI.sendToAll(cd + m + " minute " + s + " second");
			} else if (s == 0 && m == 1) {
				ActionBarAPI.sendToAll(cd + m + " minute ");
			} else if (s > 1 && m == 1) {
				ActionBarAPI.sendToAll(cd + m + " minute " + s + " seconds");
			} else {
				ActionBarAPI.sendToAll(cd + m + " minutes " + s + " seconds");
			}
		} else {
			checkSec();
		}

	}

	public void checkSec() {
		int c = getConfig().getInt("count");
		if (c > 5 && c < 60) {
			ActionBarAPI.sendToAll(cd + c + " seconds");
		} else if (c == 5) {
			ActionBarAPI.sendToAll(cd + ChatColor.AQUA + c + " seconds");
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, (float) 1.5);
			}
		} else if (c == 4) {
			ActionBarAPI.sendToAll(cd + ChatColor.GREEN + c + " seconds");
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, (float) 1);
			}
		} else if (c == 3) {
			ActionBarAPI.sendToAll(cd + ChatColor.YELLOW + c + " seconds");
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, (float) 0.8);
			}
		} else if (c == 2) {
			ActionBarAPI.sendToAll(cd + ChatColor.GOLD + c + " seconds");
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, (float) 0.6);
			}
		} else if (c == 1) {
			ActionBarAPI.sendToAll(cd + ChatColor.RED + c + " second");
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, (float) 0.4);
			}
		} else if (c == 0) {
			ActionBarAPI.sendToAll(cd + ChatColor.LIGHT_PURPLE + "TIME UP!");
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 1, 1);
			}
		} else if (c < 0) {
			// Do nothing
		}

	}

	public void Countdown() {
		int c = getConfig().getInt("count");
		int n = c - 1;
		int value = c;
		int h = value / 3600;
		int m = value % 3600;
		int s = m % 60;
		String st = getConfig().getString("countdown_msg_toggle");
		String ms = getConfig().getString("countdown_msg").replaceAll("&", cl);
		if (ms.equalsIgnoreCase("Undefined")) {
			checkHour();
		} else {
			if (s % 4 == 0) {
				if (c < 11) {
					getConfig().set("countdown_msg_toggle", "u");
					saveConfig();
				} else {
					if (st.equalsIgnoreCase("d")) {
						getConfig().set("countdown_msg_toggle", "u");
						saveConfig();
					}
					if (st.equalsIgnoreCase("u")) {
						getConfig().set("countdown_msg_toggle", "d");
						saveConfig();
					}
				}
			}
			if (st.equalsIgnoreCase("d")) {
				ActionBarAPI.sendToAll(cd + ms);
			} else {
				checkHour();
			}
		}
		if (c == -1) {
			getConfig().set("count", -1);
			saveConfig();
		} else {
			getConfig().set("count", n);
			saveConfig();
		}
	}

	public static int getPing(Player p) {
		Class<?> CPClass;
		String bpName = Bukkit.getServer().getClass().getPackage().getName(),
				version = bpName.substring(bpName.lastIndexOf(".") + 1, bpName.length());
		try {
			CPClass = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
			Object CraftPlayer = CPClass.cast(p);
			Method getHandle = CraftPlayer.getClass().getMethod("getHandle", new Class[0]);
			Object EntityPlayer = getHandle.invoke(CraftPlayer, new Object[0]);
			Field ping = EntityPlayer.getClass().getDeclaredField("ping");
			return ping.getInt(EntityPlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
}