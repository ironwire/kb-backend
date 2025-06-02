-- Insert roles if they don't exist
INSERT INTO roles (name, description) 
VALUES ('ROLE_ADMIN', 'Administrator role with full access') 
ON CONFLICT (name) DO NOTHING;

INSERT INTO roles (name, description) 
VALUES ('ROLE_USER', 'Regular user with limited access') 
ON CONFLICT (name) DO NOTHING;

-- Insert test admin user with password 'admin123'
-- The password is BCrypt encoded - this is 'admin123'
INSERT INTO users (username, password, email, display_name, created_at, is_active) 
VALUES ('admin', '$2a$10$yfIHMg4KLQtUJDQG.hDQXeRuQKnV.HtJHDZt5jBvFaBHJWJqvhcWC', 'admin@example.com', 'Admin User', CURRENT_TIMESTAMP, true)
ON CONFLICT (username) DO NOTHING;

-- Link admin user to ADMIN role
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur 
    JOIN users u2 ON ur.user_id = u2.id 
    JOIN roles r2 ON ur.role_id = r2.id
    WHERE u2.username = 'admin' AND r2.name = 'ROLE_ADMIN'
);