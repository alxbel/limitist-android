package com.github.blackenwhite.costplanner.dao.sqlite;

public class LimitDbSchema {
    public static final class LimitMonthlyTable {
        public static final String NAME = "limit_monthly";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String YEAR = "year";
            public static final String MONTH = "month";
            public static final String LIMIT_MONTHLY = "limit_monthly";
        }
    }

    public static final class LimitDailyTable {
        public static final String NAME = "limit_daily";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String MONTH_ID = "month_id";
            public static final String DAY = "day";
            public static final String LIMIT_DAILY = "limit_daily";
        }
    }
}
