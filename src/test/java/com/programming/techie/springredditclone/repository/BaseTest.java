package com.programming.techie.springredditclone.repository;

import org.testcontainers.containers.MySQLContainer;

public class BaseTest {

    static MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:latest")
            .withDatabaseName("redditDB")
            .withUsername("root")
            .withPassword("1234").withReuse(true);

    //we are manually starting the container inside the static block.
    static {
        mySQLContainer.start();
    }
}
