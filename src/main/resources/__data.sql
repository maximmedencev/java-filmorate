DELETE FROM mpa;
INSERT INTO mpa (name)
    VALUES
        ('G'),
        ('PG'),
        ('PG-13'),
        ('R'),
        ('NC-17');

DELETE FROM genres;
INSERT INTO genres (name)
    VALUES
        ('Комедия'),
        ('Драма'),
        ('Мультфильм'),
        ('Триллер'),
        ('Документальный'),
        ('Боевик');

DELETE FROM users;
INSERT INTO users(email, login, name, birthday)
    VALUES('user1@company.com','login1','name1','1991-12-12'),
          ('user2@company.com','login2','name2','1992-12-12'),
          ('user3@company.com','login3','name3','1993-12-12'),
          ('user4@company.com','login4','name4','1994-12-12'),
          ('user5@company.com','login5','name5','1995-12-12');

DELETE FROM friendships;
INSERT INTO friendships(user1_id, user2_id)
    VALUES(1, 2),
          (1, 3),
          (1, 5),
          (2, 5),
          (2, 3),
          (2, 4);

DELETE FROM films;
INSERT INTO films(name, description, release_date, duration, mpa_id)
    VALUES('name1', 'description1', '2000-10-11', 100, 1),
          ('name2', 'description2', '2000-10-12', 100, 2),
          ('name3', 'description3', '2000-10-13', 100, 3),
          ('name4', 'description4', '2000-10-14', 100, 4),
          ('name5', 'description5', '2000-10-15', 100, 5);

DELETE FROM genres_film;
INSERT INTO genres_film(film_id, genre_id)
    VALUES(1,1),
          (1,2),
          (2,3),
          (3,5),
          (4,4),
          (5,2),
          (5,3);

DELETE FROM likes;
INSERT INTO likes(film_id, user_id)
    VALUES(1,1),
          (1,2),
          (2,3),
          (3,4),
          (3,1),
          (4,5),
          (5,2),
          (5,3);
