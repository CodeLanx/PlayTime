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
package main.java.com.codelanx.playtime.data.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.codelanx.playtime.Playtime;

import org.bukkit.Bukkit;

/**
 * Instantiable SQLite connector
 *
 * @since 1.3.0
 * @author 1Rogue
 * @version 1.5.0
 */
public class SQLite {

    protected static byte connections = 0;
    protected Connection con = null;
    protected Playtime plugin;
    
    public SQLite(Playtime plugin) {
        this.plugin = plugin;
    }
    
    public SQLite() {
        this.plugin = Playtime.getPlugin();
    }

    /**
     * Opens a connection to the SQLite database. Make sure to call
     * SQLite.close() after you are finished working with the database for your
     * segment of your code.
     *
     * @since 1.3.0
     * @version 1.4.0
     *
     * @return The Connection object
     * @throws SQLException
     */
    public Connection open() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLite.class.getName()).log(Level.SEVERE, this.plugin.getCipher().getString("data.sqlite.instance.error"), ex);
            Bukkit.getServer().getPluginManager().disablePlugin(this.plugin);
        }
        this.con = DriverManager.getConnection("jdbc:sqlite:" + this.plugin.getDataFolder() + File.separator + "users.db");
        if (this.plugin.getDebug() >= 2) {
            this.plugin.getLogger().log(Level.INFO, this.plugin.getCipher().getString("data.sqlite.instance.open", ++connections));
        }
        return this.con;
    }

    /**
     * Checks if a table exists within the set database
     *
     * @since 1.3.0
     * @version 1.4.0
     *
     * @param tablename Name of the table to check for
     * @return true if exists, false otherwise
     * @throws SQLException
     */
    public boolean checkTable(String tablename) throws SQLException {
        ResultSet count = query("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='" + tablename + "'");
        byte i = 0;
        if (count.next()) {
            i = count.getByte(1);
        }
        count.close();
        return (i == 1) ? true : false;
    }

    /**
     * Executes a query, but does not update any information nor lock the
     * database
     *
     * @since 1.3.0
     * @version 1.3.0
     *
     * @param query The string query to execute
     * @return A ResultSet from the query
     * @throws SQLException
     */
    public ResultSet query(String query) throws SQLException {
        Statement stmt = this.con.createStatement();
        return stmt.executeQuery(query);
    }

    /**
     * Executes a query that can change values, and will lock the database for
     * the duration of the query
     *
     * @since 1.3.0
     * @version 1.3.0
     *
     * @param query The string query to execute
     * @return 0 for no returned results, or the number of returned rows
     * @throws SQLException
     */
    public synchronized int update(String query) throws SQLException {
        Statement stmt = this.con.createStatement();
        return stmt.executeUpdate(query);
    }

    /**
     * Closes the SQLite connection. Must be open first.
     *
     * @since 1.3.0
     * @version 1.4.2
     */
    public void close() {
        try {
            this.con.close();
            if (this.plugin.getDebug() >= 2) {
                this.plugin.getLogger().log(Level.INFO, this.plugin.getCipher().getString("data.sqlite.instance.open", --connections));
            }
        } catch (SQLException e) {
            this.plugin.getLogger().log(Level.WARNING, this.plugin.getCipher().getString("data.sqlite.instance.close-error"), this.plugin.getDebug() >= 3 ? e : "");
        }
    }

    public Connection getConnection() {
        return this.con;
    }

}
