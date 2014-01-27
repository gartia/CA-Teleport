/*
 * Copyright (c) 2012, RamsesA <ramsesakama@gmail.com>
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
 * REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
 * LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
 * OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */

package org.craftingazeroth.plugins.cat;

import org.bukkit.Location;

public class CircTeleport implements ZoneTeleport
{
    static double RAD2DEG = 180 / Math.PI;
    
    Circle area, mask;
    double x1, z1, psi;
    boolean inverted;
    
    public CircTeleport(Circle area, Circle mask, double x1, double z1, double psi, boolean inverted)
    {
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

        double dx = x - area.x0;
        double dz = z - area.z0;        
        double hyp = Math.sqrt(dx*dx + dz*dz);
        
        Location delta = current.clone();

        double theta = Math.atan2(dz, dx) + psi;        
        double xnew = x1 + Math.cos(theta) * hyp;
        double znew = z1 + Math.sin(theta) * hyp;
        
        delta.subtract(x - xnew, 0, z - znew);
        delta.setYaw(current.getYaw() + (float)(psi*RAD2DEG));

        return delta;
    }
}