package in.darshana.mobigicassignment;

import android.provider.BaseColumns;

public final class UserContract {

    public UserContract() {
    }

    public static abstract class UserEntry implements BaseColumns{
        public static final String TABLE_NAME = "UserDetails";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_NAME_NULLABLE = null;
    }
}
