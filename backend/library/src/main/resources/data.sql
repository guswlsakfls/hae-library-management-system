INSERT INTO member (email, password, role, activated, created_at, updated_at, lending_count)
SELECT 'admin@gmail.com', '$2a$10$pskoHbjo/UVLojkBI/qQ3ew452Lj.g292mvJRREP2qb04w8b9tbWq', 'ROLE_ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0
FROM dual
WHERE NOT EXISTS (SELECT * FROM member WHERE email = 'admin@gmail.com');