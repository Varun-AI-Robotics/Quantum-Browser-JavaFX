<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, project.model.Bookmark" %>

<!DOCTYPE html>
<html>
<head>
    <title>Bookmarks | Quantum Browser</title>
    <style>
        body {
            margin: 0;
            font-family: Segoe UI, sans-serif;
            background: linear-gradient(135deg, #020617, #000428);
            color: white;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .glass {
            width: 800px;
            background: rgba(255,255,255,0.12);
            border-radius: 26px;
            padding: 35px;
            backdrop-filter: blur(14px);
            border: 1.5px solid rgba(255,255,255,0.35);
            box-shadow: 0 0 30px rgba(0,140,255,0.45);
        }

        h1 {
            text-align: center;
            margin-bottom: 25px;
        }

        .bookmark {
            padding: 14px 18px;
            background: rgba(255,255,255,0.15);
            border-radius: 14px;
            margin-bottom: 12px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .bookmark a {
            color: #60a5fa;
            text-decoration: none;
            font-size: 16px;
        }

        .bookmark a:hover {
            text-decoration: underline;
        }

        form {
            margin-top: 25px;
        }

        input {
            width: 100%;
            padding: 10px;
            margin: 6px 0;
            border-radius: 12px;
            border: none;
            outline: none;
        }

        button {
            margin-top: 10px;
            width: 100%;
            padding: 12px;
            background: linear-gradient(135deg, #3b82f6, #06b6d4);
            border: none;
            border-radius: 16px;
            color: white;
            font-size: 16px;
            cursor: pointer;
        }

        button:hover {
            opacity: 0.9;
        }

        .back {
            text-align: center;
            margin-top: 20px;
        }

        .back a {
            color: #cbd5f5;
            text-decoration: none;
        }
    </style>
</head>

<body>

<div class="glass">
    <h1>📌 Bookmarks</h1>

    <%
        List<Bookmark> bookmarks =
                (List<Bookmark>) request.getAttribute("bookmarks");
        if (bookmarks != null && !bookmarks.isEmpty()) {
            for (Bookmark b : bookmarks) {
    %>
        <div class="bookmark">
            <span><%= b.getTitle() %></span>
            <a href="<%= b.getUrl() %>" target="_blank">Open</a>
        </div>
    <%
            }
        } else {
    %>
        <p>No bookmarks saved.</p>
    <%
        }
    %>

    <form method="post" action="bookmarks">
        <h3>Add Bookmark</h3>
        <input name="title" placeholder="Title" />
        <input name="url" placeholder="URL (https://...)" required />
        <button>Add Bookmark</button>
    </form>

    <div class="back">
        <a href="index.jsp">⬅ Back to Control Panel</a>
    </div>
</div>

</body>
</html>
