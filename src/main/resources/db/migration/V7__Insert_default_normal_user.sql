INSERT INTO users (first_name,
                   last_name,
                   email,
                   password,
                   phone,
                   active,
                   role_id)
SELECT 'Normal',
       'User',
       'user@example.com',
       '$2a$10$CjkdQTwPPkkQR1.q0t15W.4IbQwzw1QH1Cwt6Eqk3wA6LQxJOgfpi',
       '9876543210',
       true,
       r.id
FROM roles r
WHERE r.name = 'CUSTOMER';