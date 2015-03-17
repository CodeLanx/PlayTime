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
package main.java.com.codelanx.playtime.data.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;

import main.java.com.codelanx.playtime.Playtime;

/**
 * Instantiable MySQL connector
 *
 * @since 1.1
 * @author 1Rogue
 * @version 1.5.0
 */
public class MySQL {

    protected static byte connections = 0;
    protected static String HOST = "";
    protected static String USER = "";
    protected static String PASS = "";
    protected static String DATABASE = "";
    protected static String PORT = "";
    protected Playtime plugin;
    protected Connection con = null;

    /**
     * Sets the static variables to use in future MySQL connections
     *
     * @since 1.4.1
     * @version 1.4.1
     *
     * @param host The hostname to use
     * @param user The username to use
     * @param pass The password to use
     * @param database The database to use
     * @param port The port number to use
     * @param plugins The main plugin instance, does not need to be set
     */
    public MySQL(String host, String user, String pass, String database, String port, Playtime... plugins) {
        HOST = host;
        USER = user;
        PASS = pass;
        DATABASE = database;
        PORT = port;
        this.setMain(plugins);
    }

    /**
     * Allows calling a new MySQL instance with previous connection options
     *
     * @since 1.4.1
     * @version 1.4.1
     *
     * @param plugins The main plugin instance, does not need to be set
     */
    public MySQL(Playtime... plugins) {
        this.setMain(plugins);
    }
    
    /**
     * Sets the main {@link Playtime} instance
     * 
     * @since 1.4.3
     * @version 1.4.3
     * 
     * @param plugins An array containing the main instance, only the first is relevant
     */
    private void setMain(Playtime... plugins) {
        if (this.plugin == null) {
            if (plugins.length > 0) {
                this.plugin = plugins[0];
            } else {
                this.plugin = Playtime.getPlugin();
            }
        }
    }

    /**
     * Opens a connection to the MySQL database. Make sure to call MySQL.close()
     * after you are finished working with the database for your segment of your
     * code.
     *
     * @since 1.1
     * @version 1.4.0
     *
     * @return The Connection object
     * @throws SQLException
     */
    public Connection open() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", USER);
        connectionProps.put("password", PASS);

        this.con = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE, connectionProps);
        if (this.plugin.getDebug() >= 2) {
            this.plugin.getLogger().log(Level.INFO, this.plugin.getCipher().getString("data.mysql.instance.open", ++connections));
        }
        return this.con;
    }

    /**
     * Checks if a table exists within the set database
     *
     * @since 1.1
     * @version 1.5.0
     *
     * @param tablename Name of the table to check for
     * @return true if exists, false otherwise
     * @throws SQLException
     */
    public boolean checkTable(String tablename) throws SQLException {
        ResultSet count = query("SELECT count(*) FROM information_schema.TABLES WHERE (TABLE_SCHEMA = '" + DATABASE + "') AND (TABLE_NAME = '" + tablename + "')");
        byte i = 0;
        if (count.next()) {
            i = count.getByte(1);
        }
        count.close();
        return i == 1;
    }

    /**
     * Executes a query, but does not update any information
     *
     * @since 1.1
     * @version 1.1
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
     * Executes a query that can change values
     *
     * @since 1.1
     * @version 1.1
     *
     * @param query The string query to execute
     * @return 0 for no returned results, or the number of returned rows
     * @throws SQLException
     */
    public int update(String query) throws SQLException {
        Statement stmt = this.con.createStatement();
        return stmt.executeUpdate(query);
    }

    /**
     * Closes the MySQL connection. Must be open first.
     *
     * @since 1.1
     * @version 1.4.2
     */
    public void close() {
        try {
            this.con.close();
            if (this.plugin.getDebug() >= 2) {
                this.plugin.getLogger().log(Level.INFO, this.plugin.getCipher().getString("data.mysql.instance.open", --connections));
            }
        } catch (SQLException e) {
            this.plugin.getLogger().log(Level.WARNING, this.plugin.getCipher().getString("data.mysql.instance.close-error"), this.plugin.getDebug() >= 3 ? e : "");
        }
    }

    /**
     * Checks to make sure the connection is active to the MySQL server
     *
     * @since 1.1
     * @version 1.1
     *
     * @return true if connected, false otherwise
     * @throws SQLException
     */
    public boolean checkConnection() throws SQLException {
        ResultSet count = query("SELECT count(*) FROM information_schema.SCHEMATA");
        boolean give = count.first();
        count.close();
        return give;
    }

    public Connection getConnection() {
        return this.con;
    }

}
