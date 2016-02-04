package ru.easyjava.data.jdbc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Simple example of HikariCP and Spring.
 */
public final class HikariCPSpringXml {

    /**
     * Do not construct me.
     */
    private HikariCPSpringXml() {
    }

    /**
     * Application entry point.
     * @param args Array of command line arguments.
     */
    public static void main(final String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("/applicationContext.xml");
        SpringPoolTest poolTest = context.getBean(SpringPoolTest.class);
        try {
            poolTest.runParallelQueries();
        } catch (InterruptedException ex) {
            System.out.println("Execution failure: "
                    + ex.getMessage());
        }
    }

}
