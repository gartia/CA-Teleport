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