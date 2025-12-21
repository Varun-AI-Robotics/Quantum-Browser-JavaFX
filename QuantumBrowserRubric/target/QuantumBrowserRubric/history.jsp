<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html>
<head>
    <title>History | Quantum Browser</title>
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
            width: 700px;
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

        .entry {
            padding: 12px 16px;
            background: rgba(255,255,255,0.15);
            border-radius: 14px;
            margin-bottom: 10px;
            font-size: 15px;
        }

        .danger {
            width: 100%;
            padding: 12px;
            margin-top: 18px;
            background: #ef4444;
            border: none;
            border-radius: 16px;
            color: white;
            font-size: 16px;
            cursor: pointer;
        }

        .danger:hover {
            background: #dc2626;
        }

        .back {
            text-align: center;
            margin-top: 18px;
        }

        .back a {
            color: #cbd5f5;
            text-decoration: none;
        }
    </style>
</head>

<body>

<div class="glass">
    <h1>🕑 Browsing History</h1>

    <%
        List<String> history =
                (List<String>) request.getAttribute("history");
        if (history != null && !history.isEmpty()) {
            for (String h : history) {
    %>
        <div class="entry"><%= h %></div>
    <%
            }
        } else {
    %>
        <p>No browsing history.</p>
    <%
        }
    %>

    <form method="post" action="history">
        <button class="danger">Clear History</button>
    </form>

    <div class="back">
        <a href="index.jsp">⬅ Back to Control Panel</a>
    </div>
</div>

</body>
</html>
