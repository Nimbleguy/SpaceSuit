package aj.Java.SpaceSuit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import aj.Java.SpaceSuit.Listners.SpaceSuitListner;

public class Main extends JavaPlugin {
	public static Logger l;
	public static boolean debug = true;
	public static List<String> worlds = new ArrayList<String>();
	public static String mask = "GLASS";
	public static String tank = "CACTUS";
	public static String refill = "SPONGE";
	public static String bubble = "SPONGE";
	public static int oxygen = 9999;
	public static int bubbleR = 5;
	public static boolean full = true;
	public static int damage = 4;
	public static int helslot = 103;
	public static int tankslot = 102;
	public static boolean blacklist = true;
	public static List<String> regions = null;
	public static List<Location> generators = null;
	public static boolean worldguard = false;
	@Override
	public void onEnable(){
		this.saveDefaultConfig();
		debug = this.getConfig().getBoolean("debug");
		worlds = this.getConfig().getStringList("worlds");
		mask = this.getConfig().getString("mask");
		tank = this.getConfig().getString("tank");
		refill = this.getConfig().getString("refill");
		full = this.getConfig().getBoolean("full");
		bubble = this.getConfig().getString("bubble");
		oxygen = this.getConfig().getInt("oxygen");
		bubbleR = this.getConfig().getInt("bradius");
		damage = this.getConfig().getInt("damage");
		helslot = this.getConfig().getInt("helslot");
		tankslot = this.getConfig().getInt("tankslot");
		blacklist = this.getConfig().getBoolean("worldguard.blacklist");
		regions = this.getConfig().getStringList("worldguard.regions");
		this.getServer().getPluginManager().registerEvents(new SpaceSuitListner(), this);
		if(this.getConfig().getBoolean("crafts")){
			addRecipes();
		}
		if(debug){
			for(int i = 0; i < worlds.size(); i++){
				getLogger().info(worlds.get(i));
			}
		}
		l = getLogger();
		if(getServer().getPluginManager().getPlugin("WorldGuard") != null){
			l.info("WorldGuard compat loaded!");
			worldguard = true;
		}
		l.info("SpaceSuit is ready to breath!");
	}
	@Override
	public void onDisable(){
		getLogger().info("*choke*need air*choke*");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("ssreload")){
			if(sender.hasPermission("spacesuit.reload")){
				this.reloadConfig();
				debug = this.getConfig().getBoolean("debug");
				worlds = this.getConfig().getStringList("worlds");
				mask = this.getConfig().getString("mask");
				tank = this.getConfig().getString("tank");
				refill = this.getConfig().getString("refill");
				full = this.getConfig().getBoolean("full");
				bubble = this.getConfig().getString("bubble");
				oxygen = this.getConfig().getInt("oxygen");
				bubbleR = this.getConfig().getInt("bradius");
				damage = this.getConfig().getInt("damage");
				helslot = this.getConfig().getInt("helslot");
				tankslot = this.getConfig().getInt("tankslot");
				blacklist = this.getConfig().getBoolean("worldguard.blacklist");
				regions = this.getConfig().getStringList("worldguard.regions");
				
				sender.sendMessage("SpaceSuit reloaded!");
			}
		}
		return true; 
	}
	private void addRecipes(){
		ShapedRecipe sponge = new ShapedRecipe(new ItemStack(Material.SPONGE, 1));
		sponge.shape("CWC", "GIG", "RPR");
		sponge.setIngredient('W', Material.WOOL);
		sponge.setIngredient('C', Material.CACTUS);
		sponge.setIngredient('G', Material.GOLD_BLOCK);
		sponge.setIngredient('I', Material.IRON_BLOCK);
		sponge.setIngredient('R', Material.REDSTONE);
		sponge.setIngredient('P', Material.REDSTONE_BLOCK);
		this.getServer().addRecipe(sponge);
	}
	public static <T extends Object> void save(T obj, String path) throws Exception
	{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
		oos.writeObject(obj);
		oos.flush();
		oos.close();
	}
	public static <T extends Object> T load(String path) throws Exception
	{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
		@SuppressWarnings("unchecked")
		T result = (T)ois.readObject();
		ois.close();
		
		return result;
	}
}
