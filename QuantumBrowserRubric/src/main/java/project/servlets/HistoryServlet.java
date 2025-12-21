package project.servlets;

import project.dao.DBUtil;
import project.dao.HistoryDAO;
import project.model.HistoryEntry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for viewing and clearing browser history.
 * URL: /history
 */
@WebServlet("/history")
public class HistoryServlet extends HttpServlet {

    private HistoryDAO historyDAO;
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DBUtil.getConnection();
            historyDAO = new HistoryDAO(conn);
        } catch (Exception e) {
            throw new ServletException("Failed to init HistoryDAO", e);
        }
    }

    /**
     * GET /history
     * → forward history data to history.jsp
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<HistoryEntry> entries = historyDAO.findAll();
        List<String> history = new ArrayList<>();

        for (HistoryEntry h : entries) {
            String time = h.getVisitedAt() != null
                    ? formatter.format(h.getVisitedAt())
                    : "";
            history.add(time + " — " + h.getUrl());
        }

        req.setAttribute("history", history);
        req.getRequestDispatcher("/history.jsp").forward(req, resp);
    }

    /**
     * POST /history → clear history
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        historyDAO.clearAll();
        resp.sendRedirect(req.getContextPath() + "/history");
    }
}
