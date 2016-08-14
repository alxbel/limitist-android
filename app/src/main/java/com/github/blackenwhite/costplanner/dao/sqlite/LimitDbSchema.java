package com.github.blackenwhite.costplanner.dao.sqlite;

public class LimitDbSchema {
    public static final class LimitMonthlyTable {
        public static final String NAME = "limit_monthly";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String YEAR = "year";
            public static final String MONTH = "month";
            public static final String LIMIT_VALUE = "limit_value";
        }
    }

    public static final class LimitDailyTable {
        public static final String NAME = "limit_daily";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String LIMIT_MONTHLY_ID = "limit_monthly_id";
            public static final String DAY = "day";
            public static final String LIMIT_VALUE = "limit_value";
            public static final String SPENT = "spent";
        }
    }

    public static final class ExpenseTable {
        public static final String NAME = "expense";

        public static final class Cols {
            public static final String ID = "_id";
            public static final String LIMIT_DAILY_ID = "limit_daily_id";
            public static final String EXPENSE_VALUE = "expense_value";
            public static final String EXPENSE_TITLE = "expense_title";
            public static final String EXPENSE_CATEGORY = "expense_category";
        }
    }
}
