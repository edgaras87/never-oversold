package io.github.edgaras87.neveroversold.testsupport;

import java.util.UUID;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

/**
 * A response body read by JSON path. The evidence's one convention for
 * bodies: a field is asserted at its path ({@code $.quantity}), never by
 * substring — a substring cannot tell 3 from 30 or say a field exists.
 * Parsed once; the test keeps no knowledge of the application's Java
 * types, only of what a caller sees on the wire.
 */
public final class Body {

    private final DocumentContext json;

    private Body(DocumentContext json) {
        this.json = json;
    }

    public static Body of(String raw) {
        return new Body(JsonPath.parse(raw));
    }

    /** The value at the path, as the type the wire carries it (Integer, String, Boolean…). */
    public <T> T at(String path, Class<T> type) {
        return json.read(path, type);
    }

    public int intAt(String path) {
        return at(path, Integer.class);
    }

    public String stringAt(String path) {
        return at(path, String.class);
    }

    public UUID uuidAt(String path) {
        return UUID.fromString(stringAt(path));
    }

    public boolean has(String path) {
        try {
            return json.read(path) != null;
        } catch (PathNotFoundException absent) {
            return false;
        }
    }
}
