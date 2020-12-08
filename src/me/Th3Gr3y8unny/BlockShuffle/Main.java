package me.Th3Gr3y8unny.BlockShuffle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener{
	
	@Override
	public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length > 0) {
			
			ArrayList<Material> playerBlocks = new ArrayList<Material>();
			ArrayList<Integer> foundBlock = new ArrayList<Integer>();
			
			for (Player p : Bukkit.getOnlinePlayers()){
				
				ArrayList<Material> blocks = new ArrayList<Material>();
				for (Material block : Material.values()) {
					if (block.isBlock() && block.isItem() && block.isSolid()) {
						blocks.add(block);
					}
				}
				Material randomBlock = blocks.get(new Random().nextInt(blocks.size()));
				playerBlocks.add(randomBlock);
				foundBlock.add(0);
				p.sendMessage(ChatColor.GREEN + "Your block is " + randomBlock.name());
			}
			new BukkitRunnable() {
				
				int timer = Integer.parseInt(args[0]);
				
				@Override
	            public void run() {
	            	
	            	if (timer > 0) {
		            	//When there is 30 seconds left
		            	if (timer == 30) {
		            		Bukkit.broadcastMessage(ChatColor.RED + "30 seconds left!");
		            	}
		            	//10 second countdown
		            	if (timer <= 10 && timer != 0) {
		            		if (timer == 1) {
		            			Bukkit.broadcastMessage(ChatColor.RED + "" + timer + " second left!");
		            		}
		            		else {
		            		Bukkit.broadcastMessage(ChatColor.RED + "" + timer + " seconds left!");
		            		}
		            	}
		            	
		            	int index = 0;
		            	for (Player p : Bukkit.getOnlinePlayers()){
		            		
		            		Location loc = p.getLocation().clone().subtract(0, 1, 0); //Get block player is on
		            		Block b = loc.getBlock();
		            	
		            		//Check if player is standing on the block
		            		if(b.getType() == playerBlocks.get(index)){
		            			if (foundBlock.get(index) == 0) {
		            				Bukkit.broadcastMessage(ChatColor.AQUA + p.getDisplayName() + " has found their block!");
		            				foundBlock.set(index, 1);
		            			}
		            		}
		            		
		            		index++;
		            	}
	            	}
	            	//When time is up
	            	else if (timer <= 0) {
		            	int index1 = 0;
		                for (Player p : Bukkit.getOnlinePlayers()){
		                	if (foundBlock.get(index1) == 0) {
		                		Bukkit.broadcastMessage(ChatColor.RED + p.getDisplayName() + " lost!");
		                	}
		                	else {
		                		Bukkit.broadcastMessage(ChatColor.GREEN + p.getDisplayName() + " won!");
		                	}
		                	index1++;
		                }
		                this.cancel();
	            	}
	            	//Check if everyone found their block
	            	int amountFound = 0;
	            	for (int x : foundBlock) {
	            		amountFound += x;
	            	}
	            	//If everyone found their block quit the task
	            	if (amountFound == foundBlock.size()) {
	            		timer = 0;
	            		Bukkit.broadcastMessage(ChatColor.GREEN + "Everyone won!");
	            		this.cancel();
	            	}
	            	timer--;
	            }
			}.runTaskTimer(this, 0, 20L);
		}
		return false;
	}
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (command.getName().equalsIgnoreCase("blockshuffle")) {
            return Arrays.asList("60");
        }

        return null;
    }
	
}
