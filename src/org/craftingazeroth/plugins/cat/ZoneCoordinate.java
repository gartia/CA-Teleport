package org.craftingazeroth.plugins.cat;

public class ZoneCoordinate
{
    public final int x, z;
    
    public ZoneCoordinate(int x, int z)
    {
        this.x = x;
        this.z = z;
    }
    
    public int hashCode()
    {
        return x ^ z;
    }
    
    public boolean equals(ZoneCoordinate c)
    {
        return z == c.z && x == c.x;
    }
    
    public String toString()
    {
        return x + ", " + z;
    }
}