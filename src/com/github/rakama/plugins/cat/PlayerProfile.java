package com.github.rakama.plugins.cat;

import org.bukkit.entity.Player;

public class PlayerProfile
{
    final Player player;
    long lastTeleport;

    public PlayerProfile(Player player)
    {
        this.player = player;
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