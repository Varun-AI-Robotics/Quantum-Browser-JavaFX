package project.servlets;

import project.dao.BookmarkDAO;
import project.dao.DBUtil;
import project.model.Bookmark;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Servlet for listing, adding and deleting bookmarks.
 * URL: /bookmarks
 */
@WebServlet("/bookmarks")
public class BookmarkServlet extends HttpServlet {

    private BookmarkDAO bookmarkDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DBUtil.getConnection();
            bookmarkDAO = new BookmarkDAO(conn);
        } catch (Exception e) {
            throw new ServletException("Failed to init BookmarkDAO", e);
        }
    }

    /**
     * GET /bookmarks
     * → forward bookmark list to bookmarks.jsp
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Bookmark> list = bookmarkDAO.findAll();
        req.setAttribute("bookmarks", list);
        req.getRequestDispatcher("/bookmarks.jsp").forward(req, resp);
    }

    /**
     * POST /bookmarks
     * → add a bookmark
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String title = req.getParameter("title");
        String url   = req.getParameter("url");

        if (url != null && !url.isBlank()) {
            if (title == null || title.isBlank()) {
                title = url;
            }
            bookmarkDAO.save(new Bookmark(title, url));
        }

        resp.sendRedirect(req.getContextPath() + "/bookmarks");
    }
}
