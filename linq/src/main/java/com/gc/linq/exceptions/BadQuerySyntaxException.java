package com.gc.linq.exceptions;

/**
 * Created by Navas on 23/5/17.
 */

public class BadQuerySyntaxException extends RuntimeException {

    public BadQuerySyntaxException(String s) {
        super(String.format("Bad syntax in '%s'",s));
    }
}
