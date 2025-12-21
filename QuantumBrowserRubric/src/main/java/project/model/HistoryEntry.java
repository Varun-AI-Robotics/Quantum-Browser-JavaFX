package project.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * HistoryEntry represents a single webpage visit event.
 * It extends BaseEntity to inherit the common primary key field (id),
 * demonstrating practical use of inheritance in a persistent model hierarchy.
 */
public class HistoryEntry extends BaseEntity {

    /** Full URL of the visited webpage. */
    private String url;

    /** Timestamp when the webpage was accessed. */
    private LocalDateTime visitedAt;

    /**
     * Default constructor required for DAO hydration and serialization.
     */
    public HistoryEntry() {
    }

    /**
     * Constructs a new history record before database insertion.
     *
     * @param url       visited webpage URL
     * @param visitedAt time of visit
     */
    public HistoryEntry(String url, LocalDateTime visitedAt) {
        this.url = url;
        this.visitedAt = visitedAt;
    }

    /**
     * Constructs a history entry retrieved from the database.
     *
     * @param id        database identifier
     * @param url       visited webpage URL
     * @param visitedAt time of visit
     */
    public HistoryEntry(int id, String url, LocalDateTime visitedAt) {
        super(id);
        this.url = url;
        this.visitedAt = visitedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getVisitedAt() {
        return visitedAt;
    }

    public void setVisitedAt(LocalDateTime visitedAt) {
        this.visitedAt = visitedAt;
    }

    /**
     * Formats timestamp for human-readable UI display.
     */
    @Override
    public String toString() {
        return (visitedAt != null
                ? visitedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : "") + " - " + url;
    }
}
