/*
 * Copyright (C) 2013 Spencer Alderman
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package main.java.com.codelanx.playtime.player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import main.java.com.codelanx.playtime.Playtime;

import org.bukkit.Location;

/**
 *
 * @since 1.2.0
 * @author 1Rogue
 * @version 1.5.0
 */
public class PlayerHandler {

    private final Playtime plugin;
    private final int interval;
    private final int timeout;
    private final ConcurrentHashMap<String, PlaytimePlayer> players = new ConcurrentHashMap<String, PlaytimePlayer>();

    public PlayerHandler(Playtime plugin, int interval, int timeout) {
        this.plugin = plugin;
        this.interval = interval;
        this.timeout = timeout;
    }

    /**
     * Updates the AFK-start time for the provided player.
     *
     * @since 1.2.0
     * @version 1.4.1
     *
     * @param name The relevant player's name
     * @param time The time being AFK started
     */
    public void updatePlayer(String name, int time) {
        PlaytimePlayer temp = this.getPlayer(name);
        if (temp != null) {
            temp.setTime(time);
            this.putPlayer(name, temp);
        }
    }

    /**
     * Updates the AFK-start location for the provided player.
     *
     * @since 1.2.0
     * @version 1.4.1
     *
     * @param name The relevant player's name
     * @param place The place the player was at the start of being AFK
     */
    public void updatePlayer(String name, Location place) {
        PlaytimePlayer temp = this.getPlayer(name.toLowerCase());
        if (temp != null) {
            temp.setSavedLocation(place);
            this.putPlayer(name.toLowerCase(), temp);
        }
    }

    /**
     * Updates the AFK-status for the provided player.
     *
     * @since 1.2.0
     * @version 1.4.1
     *
     * @param name The relevant player's name
     * @param afk Whether the player is AFK or not
     */
    public void updatePlayer(String name, boolean afk) {
        PlaytimePlayer temp = this.getPlayer(name.toLowerCase());
        if (temp != null) {
            temp.setAFK(afk);
            this.putPlayer(name.toLowerCase(), temp);
        }
    }

    /**
     * Adds a player to the Plugin's tracked list of players.
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @param name The player name
     * @param time The AFK-start time
     * @param location The location when a player went AFK
     */
    public void putPlayer(String name, int time, Location location) {
        this.putPlayer(name.toLowerCase(), new PlaytimePlayer(name.toLowerCase(), time, location));
    }

    /**
     * Adds an PlaytimePlayer to the plugin's tracked list of players.
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @param name The player name
     * @param player The PlaytimePlayer object of the player
     */
    public void putPlayer(String name, PlaytimePlayer player) {
        players.put(name.toLowerCase(), player);
    }

    /**
     * Removes a player from the plugin's tracked list of players
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @param name The player name
     */
    public void remPlayer(String name) {
        players.remove(name.toLowerCase());
    }

    /**
     * Gets the plugin's instance of the player
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @param name The player name
     * @return The player instance
     */
    public PlaytimePlayer getPlayer(String name) {
        return players.get(name.toLowerCase());
    }

    /**
     * Returns whether or not the player is AFK.
     *
     * @since 1.2.0
     * @version 1.4.1
     *
     * @return AFK status
     */
    public boolean isAFK(String name) {
        return this.getPlayer(name.toLowerCase()).isAFK();
    }

    /**
     * Gets the time the player went AFK. 0 if they are not AFK.
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @param name The player name
     * @return AFK Starting time for the player
     */
    public int checkTime(String name) {
        return this.getPlayer(name.toLowerCase()).getTime();
    }

    /**
     * Gets the location where the player went AFK. Returns null if they have
     * never gone AFK.
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @param name The player name
     * @return Location where a player went AFK
     */
    public Location checkLocation(String name) {
        return this.getPlayer(name.toLowerCase()).getSavedLocation();
    }

    /**
     * Increments the value for a player's AFK time.
     *
     * @since 1.2.0
     * @version 1.4.1
     *
     * @param name The username to increment time for
     */
    public void incrementTime(String name) {
        PlaytimePlayer temp = this.getPlayer(name.toLowerCase());
        if (temp != null) {
            temp.setTime(this.getPlayer(name.toLowerCase()).getTime() + this.interval);
            if (this.checkTime(name) >= this.timeout) {
                if (this.plugin.getDebug() >= 2) {
                    this.plugin.getLogger().log(Level.INFO, this.plugin.getCipher().getString("player.set-afk", name));
                }
                this.plugin.getPlayerHandler().updatePlayer(name, true);
            }
        }
    }

    /**
     * Gets the timeout value for AFK management in seconds
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @return The int value of the AFK timeout
     */
    public int getAFKTimeout() {
        return this.timeout;
    }

    /**
     * Gets the interval at which the plugin checks for AFK people in seconds
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @return The int value of the AFK checking interval
     */
    public int getAFKCheckInterval() {
        return this.interval;
    }

    /**
     * Returns the map of all PlaytimePlayers.
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @return Map of PlaytimePlayers players
     */
    public ConcurrentHashMap<String, PlaytimePlayer> getPlayers() {
        return this.players;
    }
}
