CREATE DATABASE quantum_browser;
USE quantum_browser;

CREATE TABLE bookmarks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    url TEXT
);

CREATE TABLE history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    url TEXT,
    timestamp DATETIME
);
