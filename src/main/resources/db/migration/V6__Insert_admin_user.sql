INSERT INTO users (
    first_name,
    last_name,
    email,
    password,
    phone,
    profile_image_url,
    active,
    role_id
)
VALUES (
           'Admin',
           'User',
           'admin@example.com',
           '$2a$10$5MBbHaBqwunY.fc09Qef5etcV/fdP1J2Y6u73A9Aaz4ISSpKCKiwS',
           '9999999999',
           NULL,
           true,
           1
       );