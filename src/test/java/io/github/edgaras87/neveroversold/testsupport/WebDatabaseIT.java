package io.github.edgaras87.neveroversold.testsupport;

import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Web-plus-database test base: the full application on a random real
 * port over the shared per-JVM PostgreSQL container. Evidence that must
 * travel the real door (HTTP → application → store) extends this. The
 * door's client needs the explicit auto-configure annotation on Boot 4 —
 * a random port alone no longer provides the bean.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
public abstract class WebDatabaseIT extends DatabaseIT {
}
