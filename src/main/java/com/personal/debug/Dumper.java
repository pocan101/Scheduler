package com.personal.debug;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Jose Cortes on 11/04/15.
 */
public final class Dumper {
    /**
     * Utility class for dumping objects with their json representation
     *
     * @param col the object to dump
     * @return the json representation
     */
    public static String dump(final Object col) {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();
        return gson.toJson(col);
    }
}
