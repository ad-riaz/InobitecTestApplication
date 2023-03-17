## Задача
В обозревателе (клиенте) нужно отображение некоего древовидного представления, элементы которого хранятся в базе данных. Есть возможности добавления и удаления элементов дерева.
Предусмотреть функционал для отображения больших древовидных данных, когда каждая ветка дерева подгружается с сервера по требованию (клик на значке "+").
## Описание проекта
Приложение представляет собой клиент-серверное решение, которое реализует отображение древовидной системы комментариев на странице в браузере.
Элементы дерева (комментарии) хранятся в локальной базе данных PostgreSQL. Для хранения древовидных данных в базе использовано расширение Ltree, которое позволяет записывать "путь" сохраняемых комментариев.
Клиентская часть выполнена с использованием Javascript и библиотек jQuery, jQuery.Treetable и Bootstrap. jQuery.Treetable реализует часть функционала по управлению деревом элементов на странице.
Серверная часть реализована с помощью Spring Boot.
## Использованный стек технологий
- Серверная часть – **Java**, **Spring Boot**;
- Веб-сервер – **Tomcat**;
- СУБД – **PostgreSQL**, **Ltree extension**;
- Клиентская часть – **JavaScript**, **jQuery**, **jQuery.Treetable**, **Bootstrap**
## Инструкция по развертыванию
1. Обеспечить доступ к локальной базе данных по адресу localhost:5432.
   Имя пользователя: *postgres*, пароль: *123*.
3. Создать базу данных PostgreSQL 'inobitec-test':
```SQL
CREATE DATABASE "inobitec-test";
```
2. Подключиться к вновь созданной БД:
```SQL
\c "inobitec-test"
```
3. Добавить расширение ltree:
```SQL
CREATE EXTENSION ltree;
```
4. Создать таблицу 'info':
```SQL
CREATE TABLE IF NOT EXISTS info
(
    "id"      SERIAL 	PRIMARY KEY	NOT NULL,
    "comment" TEXT		NOT NULL,
    "path"    ltree		NOT NULL
);
```
Можно предварительно заполнить таблицу дефолтными комментариями:
```SQL
INSERT INTO info (id, comment, path)  
VALUES (1, 'Первый комментарий. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris condimentum hendrerit diam, eget laoreet est mollis quis. Sed cursus cursus vestibulum. Aenean erat neque, gravida nec ullamcorper et, ornare suscipit magna. Duis eros est, lobortis non accumsan lacinia, blandit sagittis eros. Aenean sed mauris ut purus fringilla consequat. Praesent sit amet eros ut enim vulputate rhoncus at vestibulum ante.', '1');  


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
```
5. Запустить .jar архив командой 
```Bash
java -jar inobitecJavaTest-0.0.1-SNAPSHOT.jar
```
6. Открыть в браузере страницу [http://localhost:8085/](http://localhost:8085/).