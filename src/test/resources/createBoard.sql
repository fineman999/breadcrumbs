CREATE TABLE boards (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        content TEXT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        parent_id INT,
                        FOREIGN KEY (parent_id) REFERENCES boards(id)
);