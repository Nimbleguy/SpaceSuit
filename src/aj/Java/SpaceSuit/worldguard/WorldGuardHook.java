package aj.Java.SpaceSuit.WorldGuard;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import aj.Java.SpaceSuit.Main;

public class WorldGuardHook {
	public static boolean getNotNeedTank(Player p){
		if(Main.blacklist){
			for(String regionName : Main.regions){
				ProtectedRegion reg = WGBukkit.getRegionManager(p.getWorld()).getRegion(regionName);
				if(reg != null){
					Location l = p.getLocation();
					if(reg.contains(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ())){
						return true;
					}
				}
			}
		}
		else{
			boolean check = true;
			for(String regionName : Main.regions){
				ProtectedRegion reg = WGBukkit.getRegionManager(p.getWorld()).getRegion(regionName);
				if(reg != null){
					Location l = p.getLocation();
					if(reg.contains(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ())){
						check = false;
					}
				}
			}
			return check;
		}
		return false;
	}
}
