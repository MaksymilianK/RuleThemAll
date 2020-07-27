package pl.konradmaksymilian.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class PersistenceManager {

    private final HikariDataSource pool;
    private final int server;

    private PersistenceManager(HikariDataSource pool, int server) {
        this.pool = pool;
        this.server = server;
    }

    public static Builder builder() {
        return new Builder();
    }

    public List<RankRow> getRanks() {
        try (
                Connection connection = pool.getConnection();
                Statement stmt = connection.createStatement();
        ) {
            ResultSet result = stmt.executeQuery("SELECT * FROM ranks WHERE server=0 OR server=" + server);
            List<RankRow> ranks = new ArrayList<>();
            while (result.next()) {
                ranks.add(RankRow.builder()
                        .name(result.getString(1))
                        .level(result.getInt(2))
                        .prefix(result.getString(3))
                        .server(result.getInt(4))
                        .permissions(result.getString(5))
                        .build());
            }
            return ranks;
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    public static final class Builder {

        private String user;
        private String password;
        private String host;
        private Integer port;
        private String name;
        private Integer server;

        private Builder() {}

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder server(int server) {
            this.server = server;
            return this;
        }

        public PersistenceManager build() {
            if (user == null || password == null || host == null || port == null || name == null || server == null) {
                throw new RuntimeException("Persistence manager cannot be created: one of its properties is null");
            }
            return new PersistenceManager(createPool(), server);
        }

        private HikariDataSource createPool() {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://" + host + ":" + port + "/" + name);
            config.setUsername(user);
            config.setPassword(password);

            HikariDataSource pool = new HikariDataSource(config);
            prepareDb(pool);
            return pool;
        }

        private void prepareDb(HikariDataSource pool) {
            try (
                    Connection connection = pool.getConnection();
                    Statement stmt = connection.createStatement();
            ) {
                prepareTables(stmt);
                prepareDefaultRank(stmt, server);
            } catch (Exception e) {
                throw new PersistenceException(e);
            }
        }

        private void prepareTables(Statement stmt) throws SQLException {
            stmt.execute("CREATE TABLE IF NOT EXISTS players" +
                    "(id SERIAL PRIMARY KEY," +
                    "nick VARCHAR(17) NOT NULL UNIQUE," +
                    "password VARCHAR NOT NULL," +
                    "rank SMALLINT NOT NULL," +
                    "created BIGINT NOT NULL," +
                    "last_online BIGINT NOT NULL," +
                    "email VARCHAR(50) UNIQUE)"
            );

            stmt.execute("CREATE TABLE IF NOT EXISTS ranks" +
                    "(name VARCHAR(17) NOT NULL," +
                    "level SMALLINT NOT NULL," +
                    "prefix VARCHAR(50)," +
                    "server INT NOT NULL," +
                    "permissions TEXT NOT NULL)"
            );
        }

        private void prepareDefaultRank(Statement stmt, int server) throws SQLException {
            ResultSet result = stmt.executeQuery("SELECT COUNT(*) FROM ranks WHERE level=10 AND server=" + server);
            if (!result.next()) {
                stmt.execute("INSERT INTO ranks VALUES('Player',10,NULL," + server + ",'')");
            }
        }
    }
}
