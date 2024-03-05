package webproject.factoryvision.domain.user.entity;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN(Authority.ADMIN),
    USER(Authority.USER);

//    USER,
//    ADMIN;

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public static class Authority{
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String USER = "ROLE_MEMBER";

    }
}
