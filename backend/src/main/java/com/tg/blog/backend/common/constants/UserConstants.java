package com.tg.blog.backend.common.constants;

public final class UserConstants {
    private UserConstants() {}

    public enum Status {
        ACTIVE(0),
        LOCKED(1),
        BANNED(2),
        DELETED(3);

        private final int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public static final class Role {
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";

        private Role() {}
    }
}
