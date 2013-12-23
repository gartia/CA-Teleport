package org.craftingazeroth.plugins.cat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ZoneTeleport
{
    static double RAD2DEG = 180 / Math.PI;
    
    ZoneProfile target;
    Circle area, mask;
    double x1, z1, psi;
    boolean inverted;
    
    public ZoneTeleport(ZoneProfile target, Circle area, Circle mask, 
            double x1, double z1, double psi, boolean inverted)
    {
        this.target = target;
        this.area = area;
        this.mask = mask;
        this.x1 = x1;
        this.z1 = z1;
        this.psi = psi;
        this.inverted = inverted;
    }
    
    public Location getTeleportLocation(Location current)
    {
        double x = current.getX();
        double z = current.getZ();
        
        boolean in = area.contains(x, z) && (mask == null || !mask.contains(x, z));
        
        if(inverted && in)
            return null;
        else if(!inverted && !in)
            return null;

//        if(mask != null)
//        {
//            double dx = mask.x0 - area.x0;
//            double dz = mask.z0 - area.z0;
//            double hyp = Math.sqrt(dx*dx + dz*dz);
//            double theta = Math.atan2(dz, dx) + psi;        
//            double xnew = x1 + Math.cos(theta) * hyp;
//            double znew = z1 + Math.sin(theta) * hyp;
//            Bukkit.getServer().broadcastMessage("MASK " + xnew + " " + znew);
//        }
        
        double dx = x - area.x0;
        double dz = z - area.z0;        
        double hyp = Math.sqrt(dx*dx + dz*dz);

        World world = Bukkit.getServer().getWorld(target.getName());
        
        if(world == null)
            return null;
        
        Location delta = current.clone();

        double theta = Math.atan2(dz, dx) + psi;        
        double xnew = x1 + Math.cos(theta) * hyp;
        double znew = z1 + Math.sin(theta) * hyp;
        
        delta.setWorld(world);
        delta.subtract(x - xnew, 0, z - znew);
        delta.setYaw(current.getYaw() + (float)(psi*RAD2DEG));

        return delta;
    }
    
    public String toString()
    {
        return "to " + target;
    }
}