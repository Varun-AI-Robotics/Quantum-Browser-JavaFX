package project.model;

/**
 * Bookmark is a persistent entity that represents a saved webpage entry.
 * It extends BaseEntity to inherit the common primary key field (id),
 * demonstrating an inheritance hierarchy for all database-backed models.
 */
public class Bookmark extends BaseEntity {

    /** Display text for the saved webpage title. */
    private String title;

    /** Full URL of the webpage being saved. */
    private String url;

    /**
     * Default constructor required for DAO hydration and frameworks.
     */
    public Bookmark() {
    }

    /**
     * Constructs a bookmark instance retrieved from the database.
     *
     * @param id    database-generated unique identifier
     * @param title title of the bookmarked webpage
     * @param url   full URL path
     */
    public Bookmark(int id, String title, String url) {
        super(id);
        this.title = title;
        this.url = url;
    }

    /**
     * Constructs a bookmark that has not yet been persisted.
     *
     * @param title title of the webpage
     * @param url   full URL path
     */
    public Bookmark(String title, String url) {
        this.title = title;
        this.url = url;
    }

    /** @return the human-readable bookmark label */
    public String getTitle() {
        return title;
    }

    /** @param title updates the title value */
    public void setTitle(String title) {
        this.title = title;
    }

    /** @return the full URL stored for this bookmark */
    public String getUrl() {
        return url;
    }

    /** @param url updates the URL value */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * UI-friendly representation for quick display in lists.
     */
    @Override
    public String toString() {
        return title + " - " + url;
    }
}
