package com.example.a71;

public final class DbContract {
    private DbContract() {} // Private constructor to prevent instantiation

    public static final class AdvertEntry {
        public static final String TABLE_NAME = "adverts";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_POST_TYPE = "post_type";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_LOCATION = "location";
    }
}
