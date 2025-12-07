INSERT INTO roles (id, name)
VALUES
    (1, 'ADMIN'),
    (2, 'ARTIST'),
    (3, 'CURATOR'),
    (4, 'VISITOR');

INSERT INTO users (id, username, email, password, enabled)
VALUES
    (1, 'admin1',   'admin1@example.com',   '$2a$10$/20aztPgjHOyYX3h5WL3SehAYXtkVd2x.e9RnLtXcMmgin.mtJP4y',   TRUE),
    (2, 'artist1',  'artist1@example.com',  '$2a$10$arnbHuE4lTnyF/AJigGCUOxlhVI.y0pWmST0DRUc7Lz39FX/7OP.u',  TRUE),
    (3, 'curator1', 'curator1@example.com', '$2a$10$ltmK3YtK54oMVCELFryiQekJEl2lXDWkSpg7mFrvLJcqG.CJ20an.', TRUE),
    (4, 'visitor1', 'visitor1@example.com', '$2a$10$KA7EIBSzCFykQ13BtlfQkO0sKb82GN/BsNlNsHN9EAOeduVCy8LFG', TRUE);

-- Связи пользователь–роль

INSERT INTO user_roles (user_id, role_id)
VALUES
    (1, 1),  -- admin1 → ADMIN
    (2, 2),  -- artist1 → ARTIST
    (3, 3),  -- curator1 → CURATOR
    (4, 4);  -- visitor1 → VISITOR