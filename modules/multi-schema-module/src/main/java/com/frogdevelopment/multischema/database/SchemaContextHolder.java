package com.frogdevelopment.multischema.database;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SchemaContextHolder {

    private static final ThreadLocal<String> CONTEXT = new InheritableThreadLocal<>();

    public static void set(String schema) {
        Assert.notNull(schema, "schema cannot be null");
        CONTEXT.set(schema);
        log.debug("Set context schema=\"{}\"", schema);
    }

    public static String get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
        log.debug("Cleared schema from context");
    }
}
