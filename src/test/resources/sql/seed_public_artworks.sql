INSERT INTO artist_profiles (id, user_id, display_name, bio, website)
VALUES (1, 2, 'Artist One', 'Bio of artist1', 'https://artist1.example.com');

INSERT INTO artworks (id, title, description, artwork_year,
    style, image_path, is_public, artist_profile_id, created_at)
VALUES
    (1, 'Sunset',  'Beautiful sunset', 2020, 'ABSTRACT', '/images/sunset.jpg',  TRUE, 1, CURRENT_TIMESTAMP),
    (2, 'Portrait','Nice portrait',    2021, 'PORTRAIT', '/images/portrait.jpg', TRUE, 1, CURRENT_TIMESTAMP);