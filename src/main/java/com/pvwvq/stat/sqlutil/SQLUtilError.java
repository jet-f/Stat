package com.pvwvq.stat.sqlutil;

public class SQLUtilError extends RuntimeException {

    public SQLUtilError() {

        super();

    }

    public SQLUtilError(String error_message) {

        super(error_message);

    }

}
