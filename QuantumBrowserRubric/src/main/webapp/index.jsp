<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Quantum Browser Web Panel</title>
    <style>
        body {
            margin: 0;
            font-family: Segoe UI, sans-serif;
            background: linear-gradient(135deg, #0a1f66, #000428);
            color: white;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }

        .glass {
            background: rgba(255,255,255,0.12);
            border-radius: 25px;
            padding: 40px 60px;
            backdrop-filter: blur(14px);
            border: 1.5px solid rgba(255,255,255,0.4);
            box-shadow: 0 0 30px rgba(0,140,255,0.5);
            text-align: center;
        }

        h1 {
            margin-bottom: 14px;
            font-size: 38px;
        }

        a.btn {
            text-decoration: none;
            display: block;
            margin: 12px 0;
            background: #0b6bff;
            padding: 14px 20px;
            border-radius: 14px;
            color: white;
            font-size: 18px;
            transition: 0.3s;
        }

        a.btn:hover {
            background: #1b78ff;
            box-shadow: 0 0 20px rgba(0,140,255,0.9);
        }

    </style>
</head>
<body>

<div class="glass">
    <h1>Quantum Browser Control Panel</h1>
    <p>Web-based management for Bookmarks & History</p>

    <a href="bookmarks" class="btn">📌 View & Manage Bookmarks</a>
    <a href="history" class="btn">🕑 View & Clear History</a>
</div>

</body>
</html>
