package transaction;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabasePopulatorUtils {

    private static final Logger log = LoggerFactory.getLogger(DatabasePopulatorUtils.class);

    public static void execute(final DataSource dataSource) {
        Connection connection = null;
        Statement statement = null;
        try {
            final var url = DatabasePopulatorUtils.class.getClassLoader().getResource("schema.sql");
            URI uri = url.toURI();
            Path path = Path.of(uri);
            final var sql = Files.readString(path);
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (NullPointerException | IOException | SQLException | URISyntaxException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ignored) {
            }

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }

    private DatabasePopulatorUtils() {
    }
}
