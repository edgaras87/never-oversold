package io.github.edgaras87.neveroversold.testsupport;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * The scene for interrupting a consume — SL-4's tests start from it, and
 * it lives here, not in one test class, because several share it and its
 * numbers must stay the record's. Set through a forked instance's real
 * door, since an interrupted consume needs an instance that can be killed:
 * an item with 10 on hand; reservation R holding 3, S holding 5 — 8 held,
 * 2 free (SL-4's record, §4). A consume of R should leave 7 on hand and 5
 * held.
 *
 * <p>Setting the scene asserts, since a scene that did not set is no
 * scene. The door's helpers it sets it with are its own, not a general
 * client for forked instances.
 */
public record InterruptedConsumeScene(String item, UUID r, UUID s) {

    private static final HttpClient HTTP = HttpClient.newHttpClient();

    public static InterruptedConsumeScene set(ForkedLedger ledger) throws IOException, InterruptedException {
        String item = "sl4-" + UUID.randomUUID();
        HttpResponse<String> adjusted = adjust(ledger, item, 10);
        if (adjusted.statusCode() != 200) {
            throw new IllegalStateException("the scene's item was not set: " + adjusted.body());
        }
        return new InterruptedConsumeScene(item, reserved(ledger, item, 3), reserved(ledger, item, 5));
    }

    private static UUID reserved(ForkedLedger ledger, String item, int units)
            throws IOException, InterruptedException {
        HttpResponse<String> answer = reserve(ledger, item, units);
        if (answer.statusCode() != 201) {
            throw new IllegalStateException("the scene's hold of " + units + " was not admitted: " + answer.body());
        }
        return Body.of(answer.body()).uuidAt("$.id");
    }

    /** Sends the consume of R and returns at once; the answer arrives when the consume ends, if it does. */
    public CompletableFuture<HttpResponse<String>> consumeR(ForkedLedger ledger) {
        return HTTP.sendAsync(post(ledger.baseUrl() + "/reservations/" + r + "/consume", ""),
                HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Sends a reserve on the scene's item and returns at once — it may wait
     * behind a held consume. Asserts nothing: a refusal is an answer.
     */
    public CompletableFuture<HttpResponse<String>> reserveOnItem(ForkedLedger ledger, int units) {
        return HTTP.sendAsync(post(ledger.baseUrl() + "/items/" + item + "/reservations",
                "{\"quantity\":" + units + ",\"hold\":\"PT10M\"}"), HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> adjust(ForkedLedger ledger, String item, int onHandCount)
            throws IOException, InterruptedException {
        return HTTP.send(post(ledger.baseUrl() + "/items/" + item + "/adjustments",
                "{\"onHandCount\":" + onHandCount + "}"), HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> reserve(ForkedLedger ledger, String item, int units)
            throws IOException, InterruptedException {
        return HTTP.send(post(ledger.baseUrl() + "/items/" + item + "/reservations",
                "{\"quantity\":" + units + ",\"hold\":\"PT10M\"}"), HttpResponse.BodyHandlers.ofString());
    }

    private static HttpRequest post(String url, String json) {
        return HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(json.isEmpty() ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(json))
                .build();
    }
}
