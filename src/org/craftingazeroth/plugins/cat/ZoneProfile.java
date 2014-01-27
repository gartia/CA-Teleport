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

import java.util.LinkedList;
import java.util.List;

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