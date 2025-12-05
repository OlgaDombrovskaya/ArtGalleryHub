DELETE FROM reviews;                -- отзывы зависят от artworks / users
DELETE FROM invitations;            -- приглашения зависят от exhibitions и users
DELETE FROM exhibition_artworks;    -- join-таблица выставка–работа

-- 2. Таблицы, зависящие от профиля художника / пользователя

DELETE FROM exhibitions;            -- ссылаются invitations, exhibition_artworks (уже очищены)
DELETE FROM artworks;               -- ссылаются reviews, exhibition_artworks (уже очищены)

-- 3. Таблицы, зависящие от users / roles

DELETE FROM artist_profiles;        -- ссылается на users
DELETE FROM user_roles;

----------
DELETE FROM roles;
DELETE FROM users;