package ru.easyjava.data.jdbc;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * Here lies code that makes parallel queries.
 */
public final class PoolTest {
    /**
     * Number of parallel database readers.
     */
    private static final Integer NO_THREADS = 16;
    /**
     * Query that create table.
     */
    private static final String CREATE_QUERY =
            "CREATE TABLE EXAMPLE (GREETING VARCHAR(6), TARGET VARCHAR(6))";
    /**
     * Query that populates table with data.
     */
    private static final String DATA_QUERY =
            "INSERT INTO EXAMPLE VALUES('Hello','World')";

    /**
     * Do not construct me.
     */
    private PoolTest() {
    }

    /**
     *
     * Runs several parallel queries to check database pooling.
     *
     * @param ds Database connection pool.
     * @throws InterruptedException when synchronization fails.
     */
    protected static void runParallelQueries(final HikariDataSource ds)
            throws InterruptedException {
        try (Connection db = ds.getConnection()) {
            try (Statement dataQuery = db.createStatement()) {
                dataQuery.execute(CREATE_QUERY);
                dataQuery.execute(DATA_QUERY);
            }

        } catch (SQLException ex) {
            System.out.println("Database connection failure: "
                    + ex.getMessage());
            return;
        }

        CountDownLatch startLatch = new CountDownLatch(NO_THREADS);
        CountDownLatch finishLatch = new CountDownLatch(NO_THREADS);

        Runnable readingThread = () -> {
            startLatch.countDown();
            try {
                startLatch.await();
            } catch (InterruptedException ex) {
                System.out.println("Synchronization failure: "
                        + ex.getMessage());
                return;
            }
            try (Connection db = ds.getConnection()) {
                try (PreparedStatement query =
                             db.prepareStatement("SELECT * FROM EXAMPLE")) {
                    ResultSet rs = query.executeQuery();
                    while (rs.next()) {
                        System.out.println(String.format("%s, %s!",
                                rs.getString(1),
                                rs.getString("TARGET")));
                    }
                    rs.close();
                }
            } catch (SQLException ex) {
                System.out.println("Database connection failure: "
                        + ex.getMessage());
            }
            finishLatch.countDown();
        };

        IntStream.range(0, NO_THREADS).forEach(
                (index) -> new Thread(readingThread).start()
        );

        finishLatch.await();
        System.out.println("All reading thread complete.");
    }
}
