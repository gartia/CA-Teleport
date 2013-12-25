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

import org.bukkit.entity.Player;

public class PlayerProfile
{
    final Player player;
    boolean teleportEnabled;
    boolean syncEnabled;
    long lastTeleport;
    
    public PlayerProfile(Player player)
    {
        this.player = player;
        this.teleportEnabled = true;
        this.syncEnabled = true;
    }
    
    public void setSyncEnabled(boolean enabled)
    {
        syncEnabled = enabled;
    }
    
    public boolean isSyncEnabled()
    {
        return syncEnabled;
    }

    public void setTeleportEnabled(boolean enabled)
    {
        teleportEnabled = enabled;
    }
    
    public boolean isTeleportEnabled()
    {
        return teleportEnabled;
    }
    
    public void startTeleportCooldown()
    {
        lastTeleport = player.getTicksLived();
    }

    public boolean isTeleportOnCooldown()
    {
        // restart cooldown if ticksLived was reset
        if(player.getTicksLived() < lastTeleport)
            lastTeleport = player.getTicksLived();

        return (player.getTicksLived() - lastTeleport) < CATeleport.teleport_cooldown;
    }

    public Player getPlayer()
    {
        return player;
    }
}