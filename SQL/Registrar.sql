drop table Enrollment;
drop table Courses;
drop table Departments;
drop table Students;

create table Students (
  sid number primary key,
  name varchar(128) not null,
  gpa number
);

create table Departments (
  did number primary key,
  name varchar(128) not null
);

create table Courses (
  cid number,
  did number,
  name varchar(128) unique,
  credits number default 3,
  primary key (cid, did),
  foreign key (did) references Departments(did)
);

create table Enrollment (
  did number,
  cid number,
  sid number,
  term varchar(32),
  grade number,
  primary key (cid, did, sid),
  foreign key (cid, did) references Courses(cid, did),
  foreign key (sid) references Students(sid)
 );

insert into Students (sid, name, gpa) values (1, 'Jane', 4);
insert into Students (sid, name, gpa) values (2, 'Mike', 3.75);
insert into Students (sid, name, gpa) values (3, 'Ann', 3.3);
insert into Students (sid, name, gpa) values (4, 'Mike', 2.8);

insert into Departments values (100, 'Math');
insert into Departments values (200, 'CS');
insert into Departments values (300, 'Italian');

insert into Courses (cid, did, name) values (101, 100, 'Calculus 1');
insert into Courses (cid, did, name) values (101, 200, 'Introduction to CS');
insert into Courses (cid, did, name) values (500, 200, 'Database Theory');

insert into Enrollment(cid, did, sid, term) values (101, 100, 1, 'Fall 2013');
insert into Enrollment(cid, did, sid, term) values (101, 100, 2, 'Fall 2013');
insert into Enrollment(cid, did, sid, term, grade) values (210, 200, 1, 'Spring 2014', 2);
insert into Enrollment(cid, did, sid, term, grade) values (210, 200, 2, 'Spring 2014', 3);
insert into Enrollment(cid, did, sid, term, grade) values (210, 200, 3, 'Spring 2014', 4);
insert into Enrollment(cid, did, sid, term) values (210, 200, 4, 'Spring 2014');

create table Users(
	UID number primary key,
	pwd varchar(128),
	dob date,
	date_joined date
);

create table Actors(
	AID number primary key,
	name varchar(128),
	dob date,
	bio varchar(600)
);

create table Titles(
	MID number primary key,
	name varchar(128),
	genre varchar(128),
	year number,
	synopsis varchar(600),
	title_type varchar(50)
);

create table Actors_Role_In(
	AID number,
	MID number,
	role varchar(128),
	primary key(AID, MID),
	foreign key (MID) references Titles(MID),
	foreign key (AID) references Actors(AID)
);

create table Ratings(
	RatID number primary key,
	AID number,
	MID number,
	UID number,
	score number,
	foreign key (MID) references Titles(MID),
	foreign key (AID) references Actors(AID)
	foreign key (UID) references Users(UID)
);

create table Reviews(
	RevID number primary key,
	rating number,
	MID number,
	rs varchar(128), --review source
	rt varchar(500), --review text
	foreign key (MID) references Titles(MID)
);

create table Quotes(
	QID number primary key,
	qt varchar(500), --quote text
	AID number,
	foreign key (AID) references Actors(AID)
);

create table Trivia(
	TID number primary key,
	AID number,
	trivia_text varchar(600),
	foreign key (AID) references Actors(AID)
);

create table Awards(
	AwID number primary key,
	AID number,
	nomination_date date,
	award_date date,
	foreign key (AID) references Actors(AID)
);

create table News(
	NID number primary key,
	AID number,
	news_source varchar(128),
	news_url varchar(50),
	foreign key (AID) references Actors(AID)
);
