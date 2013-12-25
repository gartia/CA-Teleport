package org.craftingazeroth.plugins.cat;

import org.bukkit.Location;

public class BoxTeleport implements ZoneTeleport
{
    static double RAD2DEG = 180 / Math.PI;
    
    BoundingBox box;
    double x1, y1, z1;
    
    public BoxTeleport(BoundingBox box, double x1, double y1, double z1)
    {
        this.box = box;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
    }
    
    public Location getTeleportLocation(Location current)
    {
        if(!box.contains(current))
            return null;
        
        Location delta = current.clone();
        delta.setX(x1);
        delta.setY(y1);
        delta.setZ(z1);
        return delta;
    }
}