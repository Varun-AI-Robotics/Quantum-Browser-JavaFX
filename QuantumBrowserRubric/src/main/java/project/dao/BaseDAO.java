package project.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import project.exception.DataAccessException;
import project.model.BaseEntity;

/**
 * BaseDAO is an abstract, reusable superclass for all DAOs.
 *
 * <p>It demonstrates:
 * <ul>
 *   <li>Inheritance (all concrete DAOs extend this class)</li>
 *   <li>Generics (type parameter T extends BaseEntity)</li>
 *   <li>Exception handling (wraps SQLException into DataAccessException)</li>
 * </ul>
 *
 * @param <T> entity type that extends BaseEntity
 */
public abstract class BaseDAO<T extends BaseEntity> implements GenericDAO<T> {

    /**
     * Shared JDBC connection used by DAO implementations.
     * The connection is managed externally and injected via constructor,
     * which makes this class easier to test and reuse.
     */
    protected final Connection connection;

    /**
     * Creates a new BaseDAO with the given JDBC connection.
     *
     * @param connection active JDBC connection (must not be null)
     */
    protected BaseDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Persists the given entity to the database.
     *
     * @param entity entity to save
     * @throws DataAccessException if a database error occurs
     */
    @Override
    public abstract void save(T entity) throws DataAccessException;

    /**
     * Returns all entities of type T.
     *
     * @return list of entities (never null, may be empty)
     * @throws DataAccessException if a database error occurs
     */
    @Override
    public abstract List<T> findAll() throws DataAccessException;

    /**
     * Deletes all entities of type T from the underlying table.
     *
     * @throws DataAccessException if a database error occurs
     */
    @Override
    public abstract void clearAll() throws DataAccessException;

    /**
     * Centralized handler for SQLExceptions.
     * Wraps low-level SQL errors into a custom unchecked DataAccessException,
     * so upper layers do not depend on java.sql.* directly.
     *
     * @param ex underlying SQLException
     * @throws DataAccessException always thrown to signal a DB error
     */
    protected final void handleSqlException(SQLException ex) throws DataAccessException {
        throw new DataAccessException("Database error: " + ex.getMessage(), ex);
    }
}
