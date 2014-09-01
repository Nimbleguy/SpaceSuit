package aj.Java.SpaceSuit.Listners;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import aj.Java.SpaceSuit.Main;

public class SpaceSuitListner implements Listener {
	public static HashMap<String, Integer> timer = new HashMap<String, Integer>(20);
	@EventHandler
	public void playerMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		for(int world = 0; world < Main.worlds.size(); world++){
			if(Main.debug){
				Main.l.info(Main.worlds.get(world));
			}
			if(p.getWorld().getName().equalsIgnoreCase(Main.worlds.get(world))){
			//if(p.getWorld().getName().equalsIgnoreCase("world")){
				if(Main.debug){
					Main.l.info(Main.bubble);
				}
				boolean notNeedTank = nearBubble(p);
				if(timer.get(p.getDisplayName()) != null){
					if(timer.get(p.getDisplayName()) == 200){
						notNeedTank = true;
					}
					else{
						timer.put(p.getDisplayName(), timer.get(p.getDisplayName()) + 1);
					}
				}
				if(Main.worldguard && !notNeedTank){
					try {
						Class<?> c = Class.forName("aj.Java.SpaceSuit.WorldGuard.WorldGuardHook");
						Method m = c.getMethod("getNotNeedTank", Player.class);
						notNeedTank = (Boolean) m.invoke(null, p);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				//Do nothing if tank not needed
				if(notNeedTank){
					if(Main.debug){
						Main.l.info("Oxy tank not needed");
					}
				}
				//Oxygen Tank
				else if((p.getInventory().getHelmet() != null && p.getInventory().getChestplate() != null && !Main.full) || (p.getInventory().getHelmet() != null && p.getInventory().getChestplate() != null && p.getInventory().getLeggings() != null && p.getInventory().getBoots() != null && Main.full)){
					if(Main.debug){
						Main.l.info(Material.matchMaterial(Main.tank).toString());
					}
					if(p.getInventory().getItem(Main.tankslot).getType().compareTo(Material.matchMaterial(Main.tank)) == 0){
						if(Main.debug){
							Main.l.info(Material.matchMaterial(Main.mask).toString());
						}
						if(p.getInventory().getItem(Main.helslot).isSimilar(new ItemStack(Material.matchMaterial(Main.mask)))){
							ItemStack c = p.getInventory().getChestplate();
							ItemMeta m = c.getItemMeta();
							if(m.hasLore()){
								List<String> lore = m.getLore();
								if(isInt(lore.get(0))){
									int i = Integer.valueOf(lore.get(0));
									if(i > 0){
										i--;
										lore.clear();
										lore.add(Integer.toString(i));
										m.setLore(lore);
										c.setItemMeta(m);
										p.getInventory().setChestplate(c);
									}
									else{
										p.damage(Main.damage);
									}
								}
							}
							else{
								List<String> lore = m.getLore();
								lore.add(Main.oxygen + "");
								m.setLore(lore);
							}
						}
						else{
							p.damage(Main.damage);
						}
					}
					else{
						p.damage(Main.damage);
					}
				}
				else{
					p.damage(Main.damage);
				}
			}
		}
	}
	@EventHandler
	public void onPlayerUse(PlayerInteractEvent event){
		if(event.hasItem() && event.hasBlock()){
			if(new ItemStack(event.getClickedBlock().getType()).isSimilar(new ItemStack(Material.matchMaterial(Main.refill)))){
				if(event.getItem().getType().compareTo(Material.matchMaterial(Main.tank)) == 0){
					if(event.getItem().getItemMeta().hasLore()){
						List<String> lore = event.getItem().getItemMeta().getLore();
						lore.clear();
						lore.add(Main.oxygen + "");
						ItemMeta m = event.getItem().getItemMeta();
						m.setLore(lore);
						event.getItem().setItemMeta(m);
						event.getPlayer().getInventory().setChestplate(event.getItem());
						event.getPlayer().getInventory().setItemInHand(null);
					}
					else{
						List<String> lore = new ArrayList<String>(1);
						lore.add(Main.oxygen + "");
						ItemMeta m = event.getItem().getItemMeta();
						m.setLore(lore);
						event.getItem().setItemMeta(m);
						event.getPlayer().getInventory().setChestplate(event.getItem());
						event.getPlayer().getInventory().setItemInHand(null);
					}
				}
				else if(event.getItem().isSimilar(new ItemStack(Material.matchMaterial(Main.mask)))){
					event.getPlayer().getInventory().setHelmet(event.getItem());
					event.getPlayer().getInventory().setItemInHand(null);
				}
			}
		}
	}
	private boolean isInt(String s){
		try{
			Integer.parseInt(s);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	private boolean nearBubble(Player p){
		int radius = Main.bubbleR;
		for(int i = -radius; i <= radius; i++) {
		    for(int j = -radius; j <= radius; j++) {
		        for(int k = -radius; k <= radius; k++) {
		            if(p.getLocation().getBlock().getRelative(i, j, k).getType().toString().equalsIgnoreCase(Main.bubble)){
		                return true;
		            }
		        }
		    }
		}
		return false;
	}
}
