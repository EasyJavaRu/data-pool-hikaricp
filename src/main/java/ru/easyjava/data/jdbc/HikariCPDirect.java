package ru.easyjava.data.jdbc;

import com.zaxxer.hikari.HikariDataSource;

/**
 * Simple example of HikariCP direct instantiation.
 */
public final class HikariCPDirect {

    /**
     * Do not construct me.
     */
    private HikariCPDirect() {
    }

    /**
     * Entry point.
     *
     * @param args Command line args. Not used.
     */
    public static void main(final String[] args) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://192.168.75.6/test");
        ds.setUsername("test");
        ds.setPassword("test");

        try {
            PoolTest.runParallelQueries(ds);
        } catch (InterruptedException ex) {
            System.out.println("Execution failure: "
                    + ex.getMessage());
        }

    }

}
