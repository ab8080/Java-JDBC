package edu.phystech.jdbcdemo.service.db;

import edu.phystech.jdbcdemo.Hello;
import edu.phystech.jdbcdemo.SimpleJdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataBaseInit {
    private final SimpleJdbcTemplate source;

    public DataBaseInit(SimpleJdbcTemplate source) {
        this.source = source;
    }

    private String getSQL(String name) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Hello.class.getResourceAsStream(name)),
                        StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    public final void create() throws SQLException, IOException {
        String sql = getSQL("dbcreate.sql");
        source.statement(stmt -> {
            stmt.execute(sql);
        });
    }
}
