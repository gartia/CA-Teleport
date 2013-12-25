package org.craftingazeroth.plugins.cat;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ZoneProfile
{
    String name;
    ZoneCoordinate coordinate;
    List<ZoneTeleport> teleports;
    
    public ZoneProfile(String name, int x, int z)
    {
        this.name = name;
        teleports = new LinkedList<ZoneTeleport>();
        coordinate = new ZoneCoordinate(x, z);
    }
    
    public void addTeleport(ZoneTeleport teleport)
    {
        teleports.add(teleport);
    }
    
    public boolean hasTeleports()
    {
        return !teleports.isEmpty();
    }
    
    public Location getTeleportLocation(Location current)
    { 
        for(ZoneTeleport teleport : teleports)
        {
            Location loc = teleport.getTeleportLocation(current);
            if(loc != null)
                return loc;
        }
        
        return null;
    }
    
    public String getName()
    {
        return name;
    }
    
    public ZoneCoordinate getCoordinate()
    {
        return coordinate;
    }
    
    public String toString()
    {
        return name;
    }
}