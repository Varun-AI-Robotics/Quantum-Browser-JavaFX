package project;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import project.dao.BookmarkDAO;
import project.dao.DBUtil;
import project.dao.HistoryDAO;
import project.model.Bookmark;
import project.model.HistoryEntry;

/**
 * PrimaryController is the main controller for the Quantum Browser UI.
 *
 * It coordinates:
 * - JavaFX WebView navigation
 * - Bookmarks and History (ListView + MySQL via DAOs)
 * - Background DB operations using ExecutorService
 * - Animated slide-in side panel
 * - Custom neon glass home page
 */
public class PrimaryController {

    // -------------------------------------------------------------------
    // FXML references
    // -------------------------------------------------------------------

    @FXML private WebView webView;
    @FXML private TextField addressBar;
    @FXML private ListView<Bookmark> bookmarksList;
    @FXML private ListView<HistoryEntry> historyList;
    @FXML private Label statusLabel;
    @FXML private StackPane sidePanel;

    // -------------------------------------------------------------------
    // Engine, DAO & Collections
    // -------------------------------------------------------------------

    private WebEngine engine;

    private BookmarkDAO bookmarkDAO;
    private HistoryDAO historyDAO;

    private final ObservableList<Bookmark> bookmarks = FXCollections.observableArrayList();
    private final ObservableList<HistoryEntry> history = FXCollections.observableArrayList();

    // Single-threaded executor for DB tasks (keeps UI thread responsive)
    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();

    // Side panel state
    private boolean sideVisible = false;

    // -------------------------------------------------------------------
    // Initialization
    // -------------------------------------------------------------------

    @FXML
    public void initialize() {
        // Setup WebView engine
        engine = webView.getEngine();
        loadHomePage();  // load custom neon home page

        // Make sure side panel starts hidden (off-screen)
        if (sidePanel != null) {
            sidePanel.setTranslateX(1600); // off-screen to the right
            sideVisible = false;
        }

        // Setup DAOs and connect to DB
        bookmarkDAO = new BookmarkDAO(DBUtil.getConnection());
        historyDAO = new HistoryDAO(DBUtil.getConnection());

        // Bind observable lists to ListViews
        bookmarksList.setItems(bookmarks);
        historyList.setItems(history);

        // Load existing bookmarks & history from DB on background thread
        dbExecutor.submit(() -> {
            List<Bookmark> bk = bookmarkDAO.findAll();
            List<HistoryEntry> hs = historyDAO.findAll();

            Platform.runLater(() -> {
                bookmarks.setAll(bk);
                history.setAll(hs);
                statusLabel.setText("Loaded bookmarks & history");
            });
        });

        // When location changes, add to history and update address bar
        engine.locationProperty().addListener((obs, oldLoc, newLoc) -> {
            if (newLoc != null && !newLoc.isEmpty()) {
                HistoryEntry entry = new HistoryEntry(newLoc, LocalDateTime.now());
                addHistoryEntry(entry);
                addressBar.setText(newLoc);
            }
        });

        statusLabel.setText("Ready");
    }

    // -------------------------------------------------------------------
    // Navigation Buttons
    // -------------------------------------------------------------------

    @FXML
    private void onBack() {
        WebHistory h = engine.getHistory();
        if (h.getCurrentIndex() > 0) {
            h.go(-1);
        }
    }

    @FXML
    private void onForward() {
        WebHistory h = engine.getHistory();
        if (h.getCurrentIndex() + 1 < h.getEntries().size()) {
            h.go(1);
        }
    }

    @FXML
    private void onReload() {
        engine.reload();
    }

    @FXML
    private void onHome() {
        loadHomePage();
    }

    @FXML
    private void onGo() {
        loadPage(addressBar.getText());
    }

    @FXML
    private void onSearch() {
        String q = addressBar.getText();
        if (q == null || q.isBlank()) {
            return;
        }
        engine.load("https://www.google.com/search?q=" + q.replace(" ", "+"));
    }

    // -------------------------------------------------------------------
    // Bookmarks
    // -------------------------------------------------------------------

    @FXML
    private void onAddBookmark() {
        String currentUrl = engine.getLocation();
        if (currentUrl == null || currentUrl.isBlank()) {
            return;
        }

        TextInputDialog dialog = new TextInputDialog(currentUrl);
        dialog.setHeaderText("Add Bookmark");
        dialog.setContentText("Title:");

        dialog.showAndWait().ifPresent(title -> {
            Bookmark b = new Bookmark(title, currentUrl);
            bookmarks.add(0, b);                     // update UI
            dbExecutor.submit(() -> bookmarkDAO.save(b)); // persist in DB
            statusLabel.setText("Bookmark added");
        });
    }

    /**
     * Called from FXML on double-click (set onMouseClicked in FXML with a handler).
     */
    @FXML
    private void onBookmarkDoubleClick() {
        Bookmark b = bookmarksList.getSelectionModel().getSelectedItem();
        if (b != null) {
            loadPage(b.getUrl());
        }
    }

    @FXML
    private void onClearHistory() {
        history.clear();
        dbExecutor.submit(historyDAO::clearAll);
        statusLabel.setText("History cleared");
    }

    // -------------------------------------------------------------------
    // Slide Side Panel
    // -------------------------------------------------------------------

    @FXML
    private void onToggleSidePanel() {
        toggleSidePanel();
    }

    /**
     * Slide side panel from 1600 → 1200 (show) and 1200 → 1600 (hide).
     */
    private void toggleSidePanel() {
        if (sidePanel == null) return;

        double fromX = sidePanel.getTranslateX();
        double toX = sideVisible ? 1600 : 1200;

        TranslateTransition slide = new TranslateTransition(Duration.millis(220), sidePanel);
        slide.setFromX(fromX);
        slide.setToX(toX);
        slide.play();

        sideVisible = !sideVisible;
    }

    // -------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------

    /**
     * Thread-safe history insertion: updates UI list and persists to DB.
     */
    private synchronized void addHistoryEntry(HistoryEntry entry) {
        history.add(0, entry);
        dbExecutor.submit(() -> historyDAO.save(entry));
    }

    /**
     * Normalizes and loads a given URL into the WebView.
     */
    private void loadPage(String url) {
        if (url == null || url.isBlank()) {
            return;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        engine.load(url);
        addressBar.setText(url);
    }

    // -------------------------------------------------------------------
    // Neon Home Page Loader
    // -------------------------------------------------------------------

    private void loadHomePage() {
        String bgUrl = null;
        try {
            bgUrl = Objects.requireNonNull(
                    getClass().getResource("/project/wallpaper.jpg")
            ).toExternalForm();
        } catch (Exception ignored) {
            // fallback background will be used
        }

        String css = (bgUrl != null)
                ? "background: url('" + bgUrl + "') no-repeat center center fixed; " +
                  "background-size: cover;"
                : "background: radial-gradient(circle at top, #2f6bff, #000428);";

        String html = HOME_TEMPLATE.replace("%BACKGROUND%", css);

        engine.loadContent(html);
        addressBar.setText("");
    }

    // Full HTML home template with glass UI and JS search
    private static final String HOME_TEMPLATE = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Quantum Home</title>
                <style>
                    html, body { margin:0; height:100%; }
                    body {
                        %BACKGROUND%;
                        display:flex;
                        justify-content:center;
                        align-items:center;
                        font-family: Segoe UI, sans-serif;
                        color:white;
                    }
                    .glass-panel {
                        padding:60px 80px;
                        border-radius:50px;
                        background:rgba(255,255,255,0.16);
                        border:1.5px solid rgba(173,216,255,0.8);
                        backdrop-filter:blur(15px);
                        box-shadow:0 0 50px rgba(0,150,255,0.6);
                        text-align:center;
                        min-width:600px;
                        max-width:780px;
                    }
                    .title { font-size:42px; font-weight:600; margin-bottom:14px; }
                    .subtitle { opacity:0.9; margin-bottom:22px; }
                    .search-box {
                        display:flex;
                        padding:10px 18px;
                        border-radius:40px;
                        background:rgba(255,255,255,0.20);
                        border:1px solid rgba(255,255,255,0.4);
                        min-width:520px;
                    }
                    .search-box input {
                        flex:1;
                        background:transparent;
                        border:none;
                        outline:none;
                        color:white;
                        font-size:17px;
                        margin-right:10px;
                    }
                    button {
                        background:#0b6bff;
                        border:none;
                        padding:8px 20px;
                        border-radius:20px;
                        color:white;
                        cursor:pointer;
                    }
                </style>
            </head>
            <body>
            <div class="glass-panel">
                <div class="title">Quantum Browser</div>
                <div class="subtitle">Type and press Enter to search</div>

                <div class="search-box">
                    <input id="q" type="text" placeholder="Search here..." />
                    <button onclick="doSearch()">Search</button>
                </div>
            </div>

            <script>
                function doSearch(){
                    const q=document.getElementById('q').value;
                    if(!q) return;
                    window.location.href='https://www.google.com/search?q='+encodeURIComponent(q);
                }
                document.addEventListener('keydown',e=>{
                    if(e.key==='Enter') doSearch();
                });
            </script>

            </body>
            </html>
            """;

    // -------------------------------------------------------------------
    // Shutdown hook (called from App)
    // -------------------------------------------------------------------

    public void shutdown() {
        dbExecutor.shutdownNow();
    }
}
