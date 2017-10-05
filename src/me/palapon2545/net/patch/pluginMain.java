package me.palapon2545.net.patch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;

public class pluginMain extends JavaPlugin implements Listener {

	public pluginMain plugin;
	public final Logger logger = Logger.getLogger("Minecraft");

	public void onEnable() {
		Bukkit.broadcastMessage("SMDUpdate System: " + ChatColor.GREEN + ChatColor.BOLD + "Enable");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
	}

	public void onDisable() {
		Bukkit.broadcastMessage("SMDUpdate System: " + ChatColor.RED + ChatColor.BOLD + "Disable");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		if (CommandLabel.equalsIgnoreCase("patch")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.isOp()) {
					update();
				} else {
					player.sendMessage(ChatColor.RED + "You don't have op to do that!");
				}
			}
			if (sender instanceof ConsoleCommandSender) {
				update();
			}
		}
		return true;
	}

	public void update() {
		Plugin SMDMain = Bukkit.getPluginManager().getPlugin("SMDMain");
		File file = new File("plugins/SMDMain.jar");
		Bukkit.getServer().getPluginManager().disablePlugin(SMDMain);
		try {
			URL url = new URL("http://pic.palapon2545.net/SMDMain.jar");
			URLConnection connection = url.openConnection();
			connection.connect();
			InputStream in = connection.getInputStream();
			FileOutputStream out = new FileOutputStream(new File("plugins/" + File.separator + "SMDMain.jar"));
			byte[] b = new byte[128];
			for (int len = in.read(b); len != -1; len = in.read(b)) {
				out.write(b, 0, len);
			}
			in.close();
			out.flush();
			out.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnknownDependencyException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().reload();
	}
}
