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

import org.bukkit.Location;

/**
 *
 * @since 1.2.0
 * @author 1Rogue
 * @version 1.2.0
 */
public class PlaytimePlayer {

    private String name = "";
    private int time = 0;
    private Location location = null;
    private boolean afk = false;

    public PlaytimePlayer(String name, int time, Location location) {
        this.name = name;
        this.time = time;
        this.location = location;
    }

    /**
     * Sets the player's starting AFK time.
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @param time The supplied time
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Sets the player's location at the time of going AFK.
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @param location The saved location
     */
    public void setSavedLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets the player's starting AFK time.
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @return Starting AFK time
     */
    public int getTime() {
        return this.time;
    }

    /**
     * Gets the player's location at the time of going AFK.
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @return The saved location
     */
    public Location getSavedLocation() {
        return this.location;
    }

    /**
     * Sets whether or not the player is AFK.
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @param value Value to set AFK for
     */
    public void setAFK(boolean value) {
        this.afk = value;
    }

    /**
     * Returns whether or not the player is AFK.
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @return AFK status
     */
    public boolean isAFK() {
        return this.afk;
    }

    /**
     * Gets the name value for the player.
     *
     * @since 1.2.0
     * @version 1.2.0
     *
     * @return The name of the player
     */
    public String getName() {
        return this.name;
    }
}