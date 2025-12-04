--- 1. Insert ROLES (1.2.1)
INSERT INTO roles (id, name)
-- VALUES (1, 'ADMIN');
VALUES (1, 'ROLE_ADMIN');

INSERT INTO roles (id, name)
-- VALUES (2, 'CURATOR');
VALUES (2, 'ROLE_CURATOR');

INSERT INTO roles (id, name)
-- VALUES (3, 'ARTIST');
VALUES (3, 'ROLE_ARTIST');

INSERT INTO roles (id, name)
-- VALUES (4, 'VISITOR');
VALUES (4, 'ROLE_VISITOR');

--- 2. Insert USERS (1.2.2)
-- ID 1
INSERT INTO users (id, username, email, password, enabled)
VALUES (1, 'admin', 'admin@gallery.com',
        '$2a$10$U.SOxlQMmMOINghKcfZf..mPLYkMnQ/K/DeV6KxtyaeX3LkfdaI7.', TRUE);

-- ID 2
INSERT INTO users (id, username, email, password, enabled)
VALUES (2, 'curator', 'curator@gallery.com',
        '$2a$10$uFipVPZBodrZ7erPxwwZOugW0Pm7/5q2Sr1r0VY0ijkJ5.Ts.Nhve', TRUE);

-- ID 3
INSERT INTO users (id, username, email, password, enabled)
VALUES (3, 'artist', 'artist@gallery.com',
        '$2a$10$3o1X4iu2LhtTyHC4TfhPI.6gHUsRx7vOwS9uVZ.y8ni197Bi2sK0m', TRUE);

-- ID 4
INSERT INTO users (id, username, email, password, enabled)
VALUES (4, 'visitor', 'visitor@gallery.com',
        '$2a$10$vaOXRnDMK4aTD7TOBh8ld.VLY/8zqucC7htYtDOTq26Sfla5ffpT6', TRUE);

--- 3. Insert USER_ROLES (1.2.3)
-- admin (ID 1) -> ADMIN (ID 1)
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1);

-- curator (ID 2) -> CURATOR (ID 2)
INSERT INTO user_roles (user_id, role_id)
VALUES (2, 2);

-- artist (ID 3) -> ARTIST (ID 3)
INSERT INTO user_roles (user_id, role_id)
VALUES (3, 3);

-- artist (ID 3) -> VISITOR (ID 4)
INSERT INTO user_roles (user_id, role_id)
VALUES (3, 4);

-- visitor (ID 4) -> VISITOR (ID 4)
INSERT INTO user_roles (user_id, role_id)
VALUES (4, 4);

--- 4. Insert ARTIST_PROFILES (1.3.1)
-- ID 1, user_id 3
INSERT INTO artist_profiles (id, user_id, display_name, bio, website)
VALUES (1, 3, 'ArtStudio Artist',
        'An artist specializing in impressionism and landscapes.', 'https://artist.gallery.com');

--- 5. Insert ARTWORKS (1.3.2)
-- ID 1, artist_profile_id 1
INSERT INTO artworks (id, artist_profile_id, title, description, artwork_year, style, image_path, is_public, created_at)
VALUES (1, 1, 'Quiet pond', 'Oil on canvas',
        2024, 'LANDSCAPE', '/images/art/quiet_pond_1.jpg',
        TRUE, NOW());
-- ID 2, artist_profile_id 1
INSERT INTO artworks (id, artist_profile_id, title, description, artwork_year, style, image_path, is_public, created_at)
VALUES (2, 1, 'Urban sunset', 'Watercolour',
        2025, 'ABSTRACT', '/images/art/urban_sunset_1.jpg',
        TRUE, NOW());

--- 6. Insert REVIEWS (1.3.3)
-- ID 1, artwork 1, author_id 4 (visitor)
INSERT INTO reviews (id, artwork_id, author_id, rating, comment, created_at)
VALUES (1, 1, 4, 5,
        'Amazing work, very soothing', NOW());
-- ID 2, artwork 2, author_id 4 (visitor)
INSERT INTO reviews (id, artwork_id, author_id, rating, comment, created_at)
VALUES (2, 2, 4, 4,
        'Interesting use of color, but a bit dark', NOW());

--- 7. Insert EXHIBITIONS (1.3.4)
-- ID 1, curator_id 2
INSERT INTO exhibitions (id, title, description, start_date, end_date, curator_id, status)
VALUES (1, 'Modern Impressionism: The Echo of Light',
        'This exhibition is a journey into the world of fleeting moments and the play of light.',
        '2025-12-01', '2025-12-31', 2, 'PLANNED');

--- 8. Insert EXHIBITION_ARTWORKS (1.3.5)
-- exhibition 1, artwork 1
INSERT INTO exhibition_artworks (exhibition_id, artwork_id)
VALUES (1, 1);
-- exhibition 1, artwork 2
INSERT INTO exhibition_artworks (exhibition_id, artwork_id)
VALUES (1, 2);

--- 9. Insert INVITATIONS (1.3.6)
-- ID 1, exhibition 1, artist_profile_id 1
INSERT INTO invitations (id, exhibition_id, artist_profile_id, status, created_at)
VALUES (1, 1, 1, 'PENDING', NOW());