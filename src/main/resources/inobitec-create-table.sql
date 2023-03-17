CREATE EXTENSION ltree;

CREATE TABLE IF NOT EXISTS info
(
    "id"      SERIAL    PRIMARY KEY NOT NULL,
    "comment" TEXT      NOT NULL,
    "path"    ltree     NOT NULL
);

INSERT INTO info (id, comment, path)
VALUES (1, 'Первый комментарий. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris condimentum hendrerit diam, eget laoreet est mollis quis. Sed cursus cursus vestibulum. Aenean erat neque, gravida nec ullamcorper et, ornare suscipit magna. Duis eros est, lobortis non accumsan lacinia, blandit sagittis eros. Aenean sed mauris ut purus fringilla consequat. Praesent sit amet eros ut enim vulputate rhoncus at vestibulum ante. Praesent lacus arcu, dictum scelerisque euismod ut, rutrum a ex. Nunc porta sem ut tellus placerat, non eleifend nibh sodales. Proin eget turpis eu magna consectetur volutpat.', '1');

INSERT INTO info (id, comment, path)
VALUES (2, 'Второй комментарий.', '2');

INSERT INTO info (id, comment, path)
VALUES (3, 'Третий комментарий.', '3');

INSERT INTO info (id, comment, path)
VALUES (4, 'Четвертый комментарий. Ответ на комментарий 1.', '1.4');

INSERT INTO info (id, comment, path)
VALUES (5, 'Пятый комментарий. Ответ на комментарий 1.', '1.5');

INSERT INTO info (id, comment, path)
VALUES (6, 'Шестой комментарий. Ответ на комментарий 2.', '2.6');

INSERT INTO info (id, comment, path)
VALUES (7, 'Седьмой комментарий. Ответ на комментарий 3.', '3.7');

INSERT INTO info (id, comment, path)
VALUES (8, 'Восьмой комментарий. Ответ на комментарий 7.', '3.7.8');

INSERT INTO info (id, comment, path)
VALUES (9, 'Девятый комментарий. Ответ на комментарий 1.', '1.9');

INSERT INTO info (id, comment, path)
VALUES (10, 'Десятый комментарий. Ответ на комментарий 4.', '1.4.10');