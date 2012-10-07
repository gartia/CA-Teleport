package com.github.rakama.plugins.cat;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class CATeleport extends JavaPlugin implements Listener
{
    // TODO: change material type at floor of world (e.g. sand to sandstone)
    // naming: kalimdor, kalimdor+001, kalimdor+002, kalimdor-001, kalimdor-002

    static final int world_height = 256;
    static final int border = 32;
    static final double padding = 1.5;

    // TODO: turn into command /cat time or something
    static final boolean sync_time_and_weather = false;

    static final int time_sync_threshold = 5;
    static final long teleport_cooldown = 120;

    static final int max_altitude_level = 999;
    static final int min_altitude_level = -999;
    static final int suffix_digits = 3;

    static final ChatColor broadcast_color = ChatColor.YELLOW;
    static final ChatColor status_color = ChatColor.AQUA;

    static final int bound_above = world_height - border;
    static final int bound_below = border;
    static final int offset = world_height - border * 2;

    boolean teleport_player = true;
    boolean sync_blocks = true;

    Map<Player, PlayerProfile> profiles;

    String suffix_pattern;
    DecimalFormat format;
    Logger log;

    public CATeleport()
    {
        log = Logger.getLogger("Minecraft");
        suffix_pattern = "[-+]" + StringUtils.repeat("\\d", suffix_digits);
        format = new DecimalFormat(StringUtils.repeat("0", suffix_digits));
        format.setPositivePrefix("+");
        profiles = new HashMap<Player, PlayerProfile>();
    }

    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable()
    {
        profiles.clear();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        if(!cmd.getName().equalsIgnoreCase("cat") || args.length <= 0)
            return false;

        String option = args[0];

        if(args.length <= 1)
            return displayStatus(sender, option);

        String mode = args[1];

        if(mode.equalsIgnoreCase("status"))
            return displayStatus(sender, option);
        else if(mode.equalsIgnoreCase("enable"))
            return setOptionStatus(sender, option, true);
        else if(mode.equalsIgnoreCase("disable"))
            return setOptionStatus(sender, option, false);

        return false;
    }

    protected boolean setOptionStatus(CommandSender sender, String option, boolean enabled)
    {
        String message;

        if(option.equalsIgnoreCase("tp"))
        {
            teleport_player = enabled;
            message = broadcast_color + "AltitudePlugin: player teleportation";
        }
        else if(option.equalsIgnoreCase("sync"))
        {
            sync_blocks = enabled;
            message = broadcast_color + "AltitudePlugin: block synchronization";
        }
        else
            return false;

        if(enabled)
            message += " enabled by " + sender.getName();
        else
            message += " disabled by " + sender.getName();

        getServer().broadcastMessage(message);

        return true;
    }

    protected boolean displayStatus(CommandSender sender, String option)
    {
        String message;
        boolean mode;

        if(option.equalsIgnoreCase("tp"))
        {
            mode = teleport_player;
            message = status_color + "AltitudePlugin: player teleportation";
        }
        else if(option.equalsIgnoreCase("sync"))
        {
            mode = sync_blocks;
            message = status_color + "AltitudePlugin: block synchronization";
        }
        else
            return false;

        if(mode)
            message += " is currently enabled";
        else
            message += " is currently disabled";

        sender.sendMessage(message);

        return true;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if(!sync_blocks)
            return;

        editBlock(event.getBlock(), false);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        if(!sync_blocks)
            return;

        editBlock(event.getBlock(), true);
    }

    protected void editBlock(Block block, boolean delete)
    {
        double y = block.getLocation().getY();

        if(y < bound_below + border - 1)
            editBlock(block, -1, delete);
        else if(y > bound_above - border + 1)
            editBlock(block, 1, delete);
    }

    protected boolean editBlock(Block block, int delta, boolean delete)
    {
        Location locationDelta = getLocationDelta(block.getLocation(), delta);
        Block blockDelta = locationDelta.getWorld().getBlockAt(locationDelta);

        if(delete)
            blockDelta.setType(Material.AIR);
        else
            blockDelta.setType(block.getType());

        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        createProfile(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        removeProfile(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event)
    {
        removeProfile(event.getPlayer());
    }

    protected PlayerProfile fetchProfile(Player player)
    {
        PlayerProfile profile = profiles.get(player);
        if(profile == null)
            profile = createProfile(player);

        return profile;
    }

    protected PlayerProfile createProfile(Player player)
    {
        PlayerProfile profile = new PlayerProfile(player);
        profiles.put(player, profile);
        return profile;
    }

    protected PlayerProfile removeProfile(Player player)
    {
        PlayerProfile profile = profiles.remove(player);

        if(profile == null)
            return null;

        return profile;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if(!teleport_player)
            return;

        Player player = event.getPlayer();
        PlayerProfile profile = fetchProfile(player);

        if(profile.isTeleportOnCooldown())
            return;

        Location loc = player.getLocation();
        double y = loc.getY();
        Location tele = null;

        if(y < bound_below - padding)
            tele = getLocationDelta(loc, -1);
        else if(y > bound_above + padding)
            tele = getLocationDelta(loc, 1);

        if(tele != null)
        {
            teleportPlayer(player, tele);
            profile.startTeleportCooldown();
        }
    }

    protected void teleportPlayer(Player player, Location loc)
    {
        // get player velocity and fall distance
        Vector velocity = player.getVelocity();
        float fallDistance = player.getFallDistance();
        boolean flying = player.isFlying();

        // teleport player to destination world
        player.teleport(loc);

        // restore velocity and fall distance
        player.setFlying(flying);
        player.setVelocity(velocity);

        // repeat, in case the player drifted during load
        player.teleport(loc);
        player.setFlying(flying);
        player.setVelocity(velocity);
        player.setFallDistance(fallDistance);
    }

    protected Location getLocationDelta(Location to, int delta)
    {
        // return false if no change in altitude
        if(delta == 0)
            return null;

        // get current world and world at new altitude level
        World world = to.getWorld();
        World worldDelta = getWorldDelta(world, delta);

        // return false if the new altitude level has no associated world
        if(worldDelta == null)
            return null;

        // synchronize time and weather of destination with default world
        if(sync_time_and_weather)
            synchronizeTimeAndWeather(worldDelta);

        // get new player location in destination world
        Location locationDelta = to.clone();
        locationDelta.setWorld(worldDelta);
        locationDelta.subtract(0, offset * delta, 0);

        return locationDelta;
    }

    protected World getWorldDelta(World world, int delta)
    {
        String worldName = world.getName();

        // get integer values for old and new altitude levels
        int altitudeLevel = getAltitudeLevel(worldName);
        int altitudeLevelDelta = altitudeLevel + delta;

        // return false if new altitude is outside min/max altitude range
        if(altitudeLevelDelta > max_altitude_level 
        || altitudeLevelDelta < min_altitude_level)
            return null;

        // get world prefix by removing the altitude value from name
        String prefix;
        if(altitudeLevel == 0)
            prefix = worldName;
        else
            prefix = getPrefix(worldName);

        // find world name for the new altitude level by appending an
        // altitude value to the world prefix
        String worldNameDelta = prefix + createSuffix(altitudeLevelDelta);
        return getServer().getWorld(worldNameDelta);
    }

    protected boolean synchronizeTimeAndWeather(World world)
    {
        World defaultWorld = getDefaultWorld(world);

        // synchronize time and weather with default world, if it exists
        if(defaultWorld != null && defaultWorld != world)
            return synchronizeTimeAndWeather(defaultWorld, world);

        return false;
    }

    protected boolean synchronizeTimeAndWeather(World sourceWorld, World targetWorld)
    {
        // get source time and weather properties
        long time = sourceWorld.getTime();
        boolean hasStorm = sourceWorld.hasStorm();
        boolean isThundering = sourceWorld.isThundering();
        int weatherDuration = sourceWorld.getWeatherDuration();
        int thunderDuration = sourceWorld.getThunderDuration();

        // difference between source and target time
        long timeDiff = Math.abs(time - targetWorld.getTime());

        // only sync time if difference exceeds threshold
        if(timeDiff > time_sync_threshold)
            targetWorld.setTime(time);

        // assign target time and weather properties
        targetWorld.setStorm(hasStorm);
        targetWorld.setThundering(isThundering);
        targetWorld.setWeatherDuration(weatherDuration);
        targetWorld.setThunderDuration(thunderDuration);

        return true;
    }

    protected int getAltitudeLevel(String worldName)
    {
        // default world has no suffix
        if(!hasSuffix(worldName))
            return 0;

        String suffix = getSuffix(worldName);
        char sign = suffix.charAt(0);
        int val = Integer.parseInt(suffix.substring(1));

        if(sign == '+')
            return val;
        else if(sign == '-')
            return -val;

        return 0;
    }

    protected World getDefaultWorld(World world)
    {
        String worldName = world.getName();

        // world is default world
        if(!hasSuffix(worldName))
            return world;

        // return default world if it exists, or null
        return getServer().getWorld(getPrefix(worldName));
    }

    protected String getPrefix(String worldName)
    {
        return worldName.substring(0, worldName.length() - suffix_digits - 1);
    }

    protected String getSuffix(String worldName)
    {
        return worldName.substring(worldName.length() - suffix_digits - 1);
    }

    protected boolean hasSuffix(String worldName)
    {
        return worldName.matches(".*" + suffix_pattern);
    }

    protected String createSuffix(int altitudeLevel)
    {
        // default world has no suffix
        if(altitudeLevel == 0)
            return "";

        return format.format(altitudeLevel);
    }
}