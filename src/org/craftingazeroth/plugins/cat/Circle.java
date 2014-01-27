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

public class Circle
{
    public final double x0, z0, r;
    public final double xmin, zmin, xmax, zmax;
    public final double xmin_int, zmin_int, xmax_int, zmax_int;
    
    public Circle(double x0, double z0, double r)
    {
        this.x0 = x0;
        this.z0 = z0;
        this.r = r;

        double interior = Math.sqrt(r*r / 2);
        
        xmin = x0 - r;
        xmax = x0 + r;
        zmin = z0 - r;
        zmax = z0 + r;
        
        xmin_int = x0 - interior;
        xmax_int = x0 + interior;
        zmin_int = z0 - interior;
        zmax_int = z0 + interior;
    }
    
    public boolean contains(double x, double z)
    {
        if(x < xmin || x > xmax || z < zmin || z > zmax)
            return false;

        if(x > xmin_int && x < xmax_int && z > zmin_int && z < zmax_int)
            return true;

        double dx = x - x0;
        double dz = z - z0;
        
        return Math.sqrt(dx*dx + dz*dz) < r;
    }
}