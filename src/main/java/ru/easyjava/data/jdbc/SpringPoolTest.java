package ru.easyjava.data.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * Here lies code that makes parallel queries with Spring support.
 */
@Service
public final class SpringPoolTest {
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
     * Spring's JDBC template, backed by HikariCP pool.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     *
     * Runs several parallel queries to check database pooling.
     *
     * @throws InterruptedException when synchronization fails.
     */
    public void runParallelQueries()
            throws InterruptedException {

        jdbcTemplate.execute(CREATE_QUERY);
        jdbcTemplate.execute(DATA_QUERY);

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
            jdbcTemplate
                    .queryForList("SELECT * FROM EXAMPLE")
                    .forEach(e -> System.out.println(
                            String.format("%s, %s!",
                                    e.get("GREETING"),
                                    e.get("TARGET"))));
            finishLatch.countDown();
        };

        IntStream.range(0, NO_THREADS).forEach(
                (index) -> new Thread(readingThread).start()
        );

        finishLatch.await();
        System.out.println("All reading thread complete.");
    }
}
