package com.github.blackenwhite.costplanner.provider.database;

public class LimitDbSchema {
    public static final class LimitTable {
        public static final String NAME = "limits";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String YEAR = "year";
            public static final String MONTH = "month";
            public static final String LIMIT_VALUE = "limit_value";
        }
    }
}
