package com.example.demo.constant;

public class PropertiesConst {

    // Cache
    public static final String CACHE_USER_CACHE_TIME_TO_LIVE = "memory.cache.user.cache.time-to-live.seconds";
    public static final String CACHE_BOOK_CACHE_TIME_TO_LIVE = "memory.cache.book.cache.time-to-live.seconds";

    public static final class PropertiesKeyEL {
        // JWT
        public static final String JWT_EXPIRATION_MIN = "${jwt.expiration.min}";

        // RSA
        public static final String RSA_PUBLIC_KEY = "${rsa.public.key}";
        public static final String RSA_PRIVATE_KEY = "${rsa.private.key}";

        // ECDSA
        public static final String ECDSA_PRIVATE_KEY = "${ecdsa.private.key}";
        public static final String ECDSA_PUBLIC_KEY = "${ecdsa.public.key}";

        // Schedule
        public static final String DB_SCHEDULER_CLEAN_UP_LOGOUT_TOKEN_CRON = "${db.scheduler.clean.logout.token.cron}";
    }
}
