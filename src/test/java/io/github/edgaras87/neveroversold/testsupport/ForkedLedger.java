package io.github.edgaras87.neveroversold.testsupport;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * One instance of the ledger as its own operating-system process
 * (ADR-0009): the build's own output — {@code target/classes} plus exactly
 * the runtime classpath the build wrote before the tests — started with
 * the three environment facts a real instance gets and nothing else from
 * the harness: the store's port, the runtime password, its listen port.
 * No test-scope code runs inside it.
 *
 * <p>The harness owns the process: started here, confirmed up through its
 * own door (health UP with the store's component UP), stopped in
 * {@link #close()} — destroyed, then forcibly if it lingers — so no
 * instance outlives its test. Its output goes to
 * {@code target/instances/ledger-<port>.log}.
 */
public final class ForkedLedger implements AutoCloseable {

    private static final String MAIN_CLASS =
            "io.github.edgaras87.neveroversold.NeverOversoldApplication";
    private static final Path RUNTIME_CLASSPATH = Path.of("target", "runtime-classpath.txt");
    private static final Duration START_TIMEOUT = Duration.ofSeconds(90);

    private final Process process;
    private final int port;
    private final Path log;

    private ForkedLedger(Process process, int port, Path log) {
        this.process = process;
        this.port = port;
        this.log = log;
    }

    /** Starts an instance on a free port against the store, and waits until its door answers UP. */
    public static ForkedLedger start(int storePort) throws IOException, InterruptedException {
        int port = freePort();
        Path log = Path.of("target", "instances", "ledger-" + port + ".log");
        Files.createDirectories(log.getParent());

        String java = ProcessHandle.current().info().command().orElseThrow();
        String classpath = "target/classes" + File.pathSeparator
                + Files.readString(RUNTIME_CLASSPATH).trim();

        ProcessBuilder builder = new ProcessBuilder(java, "-cp", classpath, MAIN_CLASS)
                .redirectErrorStream(true)
                .redirectOutput(log.toFile());
        builder.environment().put("POSTGRES_PORT", Integer.toString(storePort));
        builder.environment().put("NEVER_OVERSOLD_RUNTIME_PASSWORD", ThrowawayStore.RUNTIME_PASSWORD);
        builder.environment().put("SERVER_PORT", Integer.toString(port));

        ForkedLedger ledger = new ForkedLedger(builder.start(), port, log);
        ledger.awaitUp();
        return ledger;
    }

    public long pid() {
        return process.pid();
    }

    public String baseUrl() {
        return "http://localhost:" + port;
    }

    private void awaitUp() throws IOException, InterruptedException {
        HttpClient http = HttpClient.newHttpClient();
        HttpRequest health = HttpRequest.newBuilder(URI.create(baseUrl() + "/actuator/health"))
                .timeout(Duration.ofSeconds(2)).build();
        Instant deadline = Instant.now().plus(START_TIMEOUT);
        while (Instant.now().isBefore(deadline)) {
            if (!process.isAlive()) {
                throw new IllegalStateException("instance on port " + port + " died while starting; its log: "
                        + log + "\n" + Files.readString(log));
            }
            try {
                HttpResponse<String> response = http.send(health, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200
                        && response.body().contains("\"status\":\"UP\"")
                        && response.body().contains("\"db\":{\"status\":\"UP\"}")) {
                    return;
                }
            } catch (IOException notYetListening) {
                // the door is not open yet; keep waiting
            }
            Thread.sleep(250);
        }
        close();
        throw new IllegalStateException("instance on port " + port + " not UP within " + START_TIMEOUT
                + "; its log: " + log);
    }

    @Override
    public void close() {
        process.destroy();
        try {
            if (!process.waitFor(10, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                process.waitFor(10, TimeUnit.SECONDS);
            }
        } catch (InterruptedException interrupted) {
            process.destroyForcibly();
            Thread.currentThread().interrupt();
        }
    }

    private static int freePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }
}
