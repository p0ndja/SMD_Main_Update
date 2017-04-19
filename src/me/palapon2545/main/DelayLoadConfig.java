package me.palapon2545.main;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import me.palapon2545.main.pluginMain;

public class DelayLoadConfig implements Runnable {

	pluginMain pl;

	private boolean isRunning = true;

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	@Override
	public void run() {

		while (isRunning == true) {
			try {
				Thread.sleep(600000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int player = Bukkit.getServer().getOnlinePlayers().size();
			if (player > 0) {
				System.out.println("Player count: " + player);
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage(ChatColor.BLUE + "World> " + ChatColor.GRAY + "Starting Auto-Save " + ChatColor.YELLOW
							+ "World and Player data.");
					p.sendMessage(ChatColor.BLUE + "World> " + ChatColor.GRAY + "This system will auto-run every "
							+ ChatColor.AQUA + "10 minutes.");
					p.sendMessage(
							ChatColor.BLUE + "World> " + ChatColor.GREEN + "Saving World and Player Data Complete.");
				}
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");

			}

		}

	}

}
