package ru.easyjava.data.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import static ru.easyjava.data.jdbc.PoolTest.runParallelQueries;

/**
 * Simple example of HikariCP instantiation using external properties.
 */
public final class HikariCPProperties {

    /**
     * Do not construct me.
     */
    private HikariCPProperties() {
    }

    /**
     * Entry point.
     *
     * @param args Command line args. Not used.
     */
    public static void main(final String[] args) {
        HikariConfig config = new HikariConfig("/hikaricp.properties");
        HikariDataSource ds = new HikariDataSource(config);

        try {
            runParallelQueries(ds);
        } catch (InterruptedException ex) {
            System.out.println("Execution failure: "
                    + ex.getMessage());

        }

    }
}
