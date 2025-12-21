package project.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import project.model.HistoryEntry;

/**
 * DAO implementation for HistoryEntry entities.
 *
 * <p>This class demonstrates:
 * <ul>
 *   <li>Inheritance from BaseDAO&lt;HistoryEntry&gt;</li>
 *   <li>JDBC usage with PreparedStatement & ResultSet mapping</li>
 *   <li>Automatic table creation for persistent browser history</li>
 * </ul>
 */
public class HistoryDAO extends BaseDAO<HistoryEntry> {

    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS history (" +
            "id INT PRIMARY KEY AUTO_INCREMENT," +
            "url TEXT," +
            "visited_at TIMESTAMP" +
            ")";

    private static final String INSERT_SQL =
            "INSERT INTO history(url, visited_at) VALUES(?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT id, url, visited_at FROM history ORDER BY visited_at DESC";

    private static final String CLEAR_ALL_SQL =
            "DELETE FROM history";

    public HistoryDAO(Connection connection) {
        super(connection);
        createTableIfNeeded();
    }

    /**
     * Creates the history table if it does not already exist.
     */
    private void createTableIfNeeded() {
        try (Statement st = connection.createStatement()) {
            st.execute(CREATE_TABLE_SQL);
        } catch (SQLException ex) {
            handleSqlException(ex);
        }
    }

    /**
     * Saves a visited webpage entry along with timestamp.
     *
     * @param entry history row to persist
     */
    @Override
    public void save(HistoryEntry entry) {
        if (entry == null) {
            return;
        }
        try (PreparedStatement ps = connection.prepareStatement(INSERT_SQL)) {
            ps.setString(1, entry.getUrl());
            ps.setTimestamp(2, Timestamp.valueOf(entry.getVisitedAt()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            handleSqlException(ex);
        }
    }

    /**
     * Returns all history entries, newest first.
     *
     * @return list of HistoryEntry objects
     */
    @Override
    public List<HistoryEntry> findAll() {
        List<HistoryEntry> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HistoryEntry h = new HistoryEntry(
                        rs.getInt("id"),
                        rs.getString("url"),
                        rs.getTimestamp("visited_at").toLocalDateTime()
                );
                result.add(h);
            }
        } catch (SQLException ex) {
            handleSqlException(ex);
        }
        return result;
    }

    /**
     * Deletes all stored history entries.
     */
    @Override
    public void clearAll() {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate(CLEAR_ALL_SQL);
        } catch (SQLException ex) {
            handleSqlException(ex);
        }
    }
}
