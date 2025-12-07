# ArtGalleryHub — Backend Application

## Описание проекта

ArtGalleryHub это backend платформа для цифровой галереи, онлайн выставок и взаимодействия между художниками, посетителями и кураторами. Приложение предоставляет функционал для загрузки работ, участия в выставках, модерации, уведомлений и управления ролями.

Сервис написан на Spring Boot и построен с учетом стандартов корпоративной архитектуры.

### 🚀 Основные возможности

#### 👩‍🎨 Для художников/ ARTIST
* Регистрация и управление профилем
* Загрузка изображений своих работ
* Управление своими арт работами
* Участие в онлайн-выставках
* Получение приглашений и уведомлений

#### 🖼 Для посетителей / VISITOR
* Просмотр картин
* Добавление работ в избранное
* Оставление отзывов
* Подписка на события
* Просмотр информации о художнике

#### 🧑‍💼 Для кураторов / CURATOR
* Создание и управление выставками
* Модерация загруженных работ
* Приглашение художников
* Управление статусами выставок

#### 👮 Для администраторов / ADMIN
* Управление пользователями и правами
* Управление ролями
* Просмотр всех пользователей

#### ✉️ Email уведомления
* Отправка приглашений художнику
* Уведомление куратора об участии в выставке

#### 📁 Работа с файлами
* Загрузка изображений
* Хранение и доступ через file-storage

#### 🔐 Безопасность и роли
* JWT-аутентификация
* Роли: USER, ARTIST, CURATOR, ADMIN
* Granular role-based access (Spring Security)

### 🛠 Используемые технологии
* Java 17+
* Spring Boot 3+
  - Spring Web
  - Spring Data JPA
  - Spring Security 
  - Spring Validation
  - Spring mail
* Maven
* H2 (для dev и test)
* PostgreSQL/MySQL для prod
* Spring Boot Test + Spring Security Test + MockMvc / TestRestTemplate
* Liquibase
* Lombok
* OpenAPI / Swagger
* Docker (опционально)
* Thymeleaf (email шаблоны)
* JWT (аутентификация)

### 🗂️ Архитектура проекта
```
src/main/java/com/art_gallery_hub
│
├── controller            (REST controllers)
│
├── service               (business logic)
│
├── security              (JWT + filters + access checkers)
│
├── repository            (Spring Data JPA interfaces)
│
├── model                 (JPA entities)
│
├── dto                   (request/response DTO objects)
│
├── mapper                (MapStruct или ручные мапперы)
│
└── advice                (Global exception handler)

src/main/resources
│
├── db/changelog          (Liquibase миграции)
│
├── templates/email       (email HTML шаблоны)
│
├── application-dev.properties
├── application-prod.properties
└── application-test.properties
```

### 🧪 Архитектура тестов
```
src/test/java/com/art_gallery_hub
│
├── config              (тестовые конфигурации безопасности и контекста)
│
├── controller          (интеграционные тесты REST контроллеров через MockMvc)
│
├── repository          (интеграционные тесты JPA и базы данных)
│
└── ArtGalleryHubApplicationTests.java   (общий контекстный тест)

src/test/resources
│
├── sql
│   ├── clear.sql                  (очистка таблиц перед тестами)
│   ├── seed_users.sql             (инициализация тестовых пользователей)
│   └── seed_public_artworks.sql   (инициализация публичных картин)
│
└── application-test.properties    (отдельный профиль тестирования)
```
### 🔑 Основные эндпоинты

#### 🔐 Аутентификация

* POST /api/auth/register-artist
* POST /api/auth/register-visitor
* POST /api/auth/login

#### 🎨 Художник

* GET    /api/artist/profile
* PUT    /api/artist/profile
* POST   /api/artist/artworks
* PUT    /api/artist/artworks/{id}
* DELETE /api/artist/artworks/{id}
* GET    /api/artist/invitations
* POST   /api/artist/invitations/{id}/accept
* POST   /api/artist/invitations/{id}/decline

#### 🖼️ Публичная зона

* GET /api/public/artworks
* GET /api/public/artworks/{id}
* GET /api/public/exhibitions/open

#### 🧑‍💼 Куратор

* POST /api/curator/exhibitions
* PUT  /api/curator/exhibitions/{id}
* POST /api/curator/exhibitions/{id}/invite/{artistId}

#### 🧑‍⚖️ Администратор

* GET   /api/admin/users
* POST  /api/admin/users
* PUT   /api/admin/users/{id}/roles

### 🧪 Тестирование
* JUnit 
* Mockito
* Интеграционные тесты (@SpringBootTest)

### 👤 Тестовые пользователи

| Роль    | Логин   | Электронная почта                                            | Пароль     |
| ------- | ------- | ------------------------------------------------- | ---------- |
| ADMIN   | admin   | [admin@gallery.com](mailto:admin@gallery.com)     | admin123   |
| CURATOR | curator | [curator@gallery.com](mailto:curator@gallery.com) | curator123 |
| ARTIST  | artist  | [artist@gallery.com](mailto:artist@gallery.com)   | artist123  |
| VISITOR | visitor | [visitor@gallery.com](mailto:visitor@gallery.com) | visitor123 |

## 💻 Полный набор команд запуска и диагностики проекта

### 1. Запуск в режиме разработки dev

### В dev режиме используются:

* H2 база (in memory)
* Liquibase отключен
* schema.sql и data.sql загружаются автоматически
* включены SQL логи
* доступна H2 консоль по адресу [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

### В prod режиме используются:

* используется PostgreSQL
* Liquibase включен
* почтовая отправка работает на реальном SMTP

#### Перед запуском нужно задать переменные среды:
```
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=galleryhub
export DB_USERNAME=postgres
export DB_PASSWORD=your_pass

export MAIL_HOST=smtp.gmail.com
export MAIL_PORT=587
export MAIL_USERNAME=your_email
export MAIL_PASSWORD=your_app_password
export MAIL_ADDRESS=no-reply@gallery.com
```
### 2. Запуск через IntelliJ IDEA

### Профиль dev

* Открыть Run Configurations
* Добавить VM options:
```
-Dspring.profiles.active=dev
```
### Профиль prod
```
-Dspring.profiles.active=prod
```
### 3. Команды для H2 консоли (dev)

Для входа:
```
JDBC URL: jdbc:h2:mem:art_gallery_hub_db
Username: sa
Password: (пустой)
```

### 4. Проверка API после запуска

#### Проверить доступность сервера

curl http://localhost:8080/api/public/info


#### Получить публичные работы

curl http://localhost:8080/api/public/artworks
