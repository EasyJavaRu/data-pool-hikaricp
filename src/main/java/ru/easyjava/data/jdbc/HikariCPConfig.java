package ru.easyjava.data.jdbc;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import static ru.easyjava.data.jdbc.PoolTest.runParallelQueries;

/**
 * Simple example of HikariCP instantiation via config.
 */
public final class HikariCPConfig {

    /**
     * Do not construct me.
     */
    private HikariCPConfig() {
    }

    /**
     * Entry point.
     *
     * @param args Command line args. Not used.
     */
    public static void main(final String[] args) {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.setUsername("test");
        config.setPassword("test");
        config.addDataSourceProperty("databaseName", "test");
        config.addDataSourceProperty("serverName", "127.0.0.1");

        HikariDataSource ds = new HikariDataSource(config);

        try {
            runParallelQueries(ds);
        } catch (InterruptedException ex) {
            System.out.println("Execution failure: "
                    + ex.getMessage());

        }

    }

}
