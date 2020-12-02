package com.frogdevelopment.multischema.database.exception;

public class UnknownSchemaDataSourceException extends RuntimeException {

    public UnknownSchemaDataSourceException(String schema) {
        super("Cannot determine target DataSource schema=" + schema);
    }
}
