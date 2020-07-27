package pl.konradmaksymilian.persistence;

public final class RankRow {

    private final String name;
    private final int level;
    private final String prefix;
    private final int server;
    private final String permissions;

    public RankRow(String name, int level, String prefix, int server, String permissions) {
        this.name = name;
        this.level = level;
        this.prefix = prefix;
        this.server = server;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getServer() {
        return server;
    }

    public String getPermissions() {
        return permissions;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String name;
        private Integer level;
        private String prefix;
        private Integer server;
        private String permissions;

        private Builder() {}

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder level(Integer level) {
            this.level = level;
            return this;
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder server(Integer server) {
            this.server = server;
            return this;
        }

        public Builder permissions(String permissions) {
            this.permissions = permissions;
            return this;
        }

        public RankRow build() {
            if (name == null || level == null || prefix == null || server == null || permissions == null) {
                throw new RuntimeException("Cannot build Rank object - one of its properties is null");
            }
            return new RankRow(name, level, prefix, server, permissions);
        }
    }
}
