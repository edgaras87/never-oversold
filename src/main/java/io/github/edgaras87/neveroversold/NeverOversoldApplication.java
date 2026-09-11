package io.github.edgaras87.neveroversold;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The ledger's entry point. One image, run as as many instances as the
 * evidence needs; every instance is this class started once.
 */
@SpringBootApplication
public class NeverOversoldApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeverOversoldApplication.class, args);
    }
}
