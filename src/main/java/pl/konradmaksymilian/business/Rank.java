package pl.konradmaksymilian.business;

import java.util.List;

public class Rank {

    private final String prefix;
    private final List<String> permissions;

    public Rank(String prefix, List<String> permissions) {
        this.prefix = prefix;
        this.permissions = permissions;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}
