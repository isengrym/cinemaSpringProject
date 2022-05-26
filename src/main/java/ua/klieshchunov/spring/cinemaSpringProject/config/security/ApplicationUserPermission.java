package ua.klieshchunov.spring.cinemaSpringProject.config.security;

public enum ApplicationUserPermission {
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    MOVIE_READ("movie:read"),
    MOVIE_WRITE("movie:write"),
    SHOWTIME_READ("showtime:read"),
    SHOWTIME_WRITE("showtime:write"),
    TICKET_READ("ticket:read"),
    TICKET_WRITE("ticket:write");

    private final String permission;

    ApplicationUserPermission(String string) {
        this.permission = string;
    }

    public String getPermission() {
        return permission;
    }
}
