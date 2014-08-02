-- Example SQL DML Queries
--------------------------
-- Users

INSERT INTO users VALUES ('psuping', 'test1', to_date('1982/04/15', 'YYYY/MM/DD'), to_date('2013/07/01', 'YYYY/MM/DD'));
INSERT INTO users VALUES ('mrbucci', 'test2', to_date('1986/07/30', 'YYYY/MM/DD'), to_date('2013/07/05', 'YYYY/MM/DD'));

UPDATE users SET pwd = 'testing' WHERE userid = 'psuping';

SELECT userid, pwd, dob, date_joined FROM users;
SELECT userid, pwd, dob, date_joined FROM users WHERE uid = 2;

-- Actors

INSERT INTO actors VALUES (1, 'Mark Wahlberg', to_date('1971/06/05', 'YYYY/MM/DD'), 'American Actor');
INSERT INTO actors VALUES (2, 'Gregg Bruger', to_date('1965/06/05', 'YYYY/MM/DD'), 'American Actor');
INSERT INTO actors VALUES (3, 'Norman Reedus', to_date('1975/06/05', 'YYYY/MM/DD'), 'American Actor');

UPDATE actors SET bio = 'Star of Transformers 4' WHERE aid = 1;
UPDATE actors SET name = 'Mark Wahlberg', bio = 'Star of Transformers 4' WHERE aid = 1;

SELECT aid, name, dob, bio FROM actors;
SELECT aid, name, dob, bio FROM actors WHERE aid = 2;

-- Titles

INSERT INTO titles VALUES (1, 'Transformers 4', 'Action', 2014, 'Robots fighting', 'Movie');
INSERT INTO titles VALUES (2, 'The Walking Dead', 'Horror', 2008, 'Zombie apocolypse', 'TV');
INSERT INTO titles VALUES (3, 'The Boondock Saints', 'Action', 1999, 'Vigilante Justice', 'Movie');

UPDATE titles SET year = 2010 WHERE tid = 2;

SELECT tid, name, genre, year, synopsis, title_type FROM titles;
SELECT tid, name, genre, year, synopsis, title_type FROM titles WHERE tid = 1;

-- Actors_Role_In

INSERT INTO Actors_Role_In VALUES (1, 1, 'Cade Yeager');
INSERT INTO Actors_Role_In VALUES (3, 2, 'Dale');
INSERT INTO Actors_Role_In VALUES (3, 3, 'Murphy MacManus');

UPDATE Actors_Role_In SET role = 'Dale Dixon' WHERE aid = 3 AND tid = 2;

SELECT aid, tid, role FROM Actors_Role_In;
SELECT aid, tid, role FROM Actors_Role_In WHERE aid = 3 AND tid = 2;

-- Ratings

INSERT INTO ratings VALUES (3, 3, 'psuping', 4);
INSERT INTO ratings VALUES (3, 3, 'mrbucci', 5);
INSERT INTO ratings VALUES (1, 1, 'psuping', 3);
INSERT INTO ratings VALUES (3, 2, 'mrbucci', 4);

UPDATE ratings SET score = 4 WHERE aid = 1 AND tid = 1 AND userid = 'psuping';

SELECT aid, tid, userid, score FROM ratings;
SELECT aid, tid, userid, score FROM ratings WHERE aid = 3;
SELECT aid, tid, userid, score FROM ratings WHERE tid = 3;
SELECT aid, tid, userid, score FROM ratings WHERE userid = 'mrbucci';

-- Reviews

INSERT INTO reviews VALUES (1, 8, 3, 'http://www.imdb.com/', 'Good show');
INSERT INTO reviews VALUES (2, 9, 3, 'http://www.rottentomatoes.com/', 'Revolutionary');

UPDATE reviews SET rs = 'http://www.imdb.com/BoondockSaints', rt = 'Really good show' WHERE revid = 1;

SELECT revid, rating, tid, rs, rt FROM reviews;
SELECT revid, rating, tid, rs, rt FROM reviews WHERE revid = 2;
SELECT revid, rating, tid, rs, rt FROM reviews WHERE tid = 3;

-- Trivia 

INSERT INTO trivia VALUES (1, 1, 'Owns burger chain');
INSERT INTO trivia VALUES (2, 1, 'Has show about burger chain');

UPDATE trivia SET trivia_text = 'His brother has a show about a burger chain' WHERE trvid = 2;

SELECT trvid, aid, trivia_text FROM trivia;
SELECT trvid, aid, trivia_text FROM trivia WHERE aid = 1;

-- Awards

INSERT INTO awards VALUES (1, 3, to_date('2011/07/01', 'YYYY/MM/DD'), to_date('2011/08/15', 'YYYY/MM/DD'));
INSERT INTO awards VALUES (2, 3, to_date('2011/07/01', 'YYYY/MM/DD'), NULL);
INSERT INTO awards VALUES (3, 3, to_date('2011/12/15', 'YYYY/MM/DD'), NULL);

UPDATE awards SET award_date = to_date('2011/09/01', 'YYYY/MM/DD') WHERE awid = 2;

SELECT awid, aid, nomination_date, award_date FROM awards;
SELECT awid, aid, nomination_date, award_date FROM awards WHERE aid = 3;

-- News

INSERT INTO news VALUES (1, 1, 'Hollywood Reporter', 'http://www.hollywoodreporter.com');
INSERT INTO news VALUES (2, 1, 'Hollywood Reporter', 'http://www.hollywoodreporter.com/TF5');

UPDATE news SET news_source = 'Transformers 5 Rumors' WHERE nid = 2;

SELECT nid, aid, news_source, news_url FROM news;
SELECT nid, aid, news_source, news_url FROM news WHERE aid = 1;
