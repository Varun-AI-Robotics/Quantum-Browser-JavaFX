package project.dao;

import java.util.List;
import project.model.BaseEntity;

/**
 * GenericDAO is a generic data-access contract for all entities.
 *
 * <p>This interface demonstrates:
 * <ul>
 *   <li>Use of interfaces for abstraction</li>
 *   <li>Java generics with an upper bound (T extends BaseEntity)</li>
 *   <li>Collection usage via List&lt;T&gt; return type</li>
 * </ul>
 *
 * @param <T> entity type that extends BaseEntity
 */
public interface GenericDAO<T extends BaseEntity> {

    /**
     * Persists the given entity.
     *
     * @param entity entity to save
     */
    void save(T entity);

    /**
     * Returns all entities of type T.
     *
     * @return list of entities (never null, may be empty)
     */
    List<T> findAll();

    /**
     * Deletes all entities of type T from the underlying storage.
     */
    void clearAll();
}
