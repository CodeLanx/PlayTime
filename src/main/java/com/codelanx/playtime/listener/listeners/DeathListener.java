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
package main.java.com.codelanx.playtime.listener.listeners;

import main.java.com.codelanx.playtime.Playtime;
import main.java.com.codelanx.playtime.runnable.ResetRunnable;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Actions to take if death checking is enabled
 *
 * @since 1.4.1
 * @author 1Rogue
 * @version 1.5.0
 */
public class DeathListener implements Listener {

    private final Playtime plugin;

    public DeathListener(Playtime plugin) {
        this.plugin = plugin;
    }

    /**
     * Resets a player's death timer on death.
     *
     * @since 1.2.0
     * @version 1.5.0
     *
     * @param e The death event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent e) {
        this.plugin.getExecutiveManager().runAsyncTask(new ResetRunnable(this.plugin, e.getEntity().getUniqueId(), "deathtime"), 0L);
    }
}
