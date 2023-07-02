INSERT INTO member (email, password, role, activated, created_at, updated_at)
SELECT 'admin@gmail.com', '$2a$10$0/MqSDYi5gA7HitE1ioK.OmMceX4un2Fv3awXEvqn613QIjz8ob7q', 'ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM dual
WHERE NOT EXISTS (SELECT * FROM member WHERE email = 'admin@gmail.com');