drop table Actors_Role_In;
drop table Ratings;
drop table Reviews;
drop table Quotes;
drop table Awards;
drop table Trivia;
drop table News;

drop table Users;
drop table Actors;
drop table Titles;

create table Users (
	userID varchar(24) primary key,
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
	TID number primary key,
	name varchar(128),
	genre varchar(128),
	year number,
	synopsis varchar(600),
	title_type varchar(50)
);

create table Actors_Role_In(
	AID number,
	TID number,
	role varchar(128),
	primary key(AID, TID),
	foreign key (TID) references Titles(TID),
	foreign key (AID) references Actors(AID)
);

create table Ratings(
	AID number,
	TID number,
	userID varchar(24),
	score number,
	primary key (AID, TID, userID),
	foreign key (TID) references Titles(TID),
	foreign key (AID) references Actors(AID),
	foreign key (userID) references Users(userID)
);

create table Reviews(
	RevID number primary key,
	rating number,
	TID number,
	rs varchar(512), --review source
	rt varchar(1024), --review text
	foreign key (TID) references Titles(TID)
);

create table Quotes(
	QID number primary key,
	qt varchar(500), --quote text
	AID number,
	foreign key (AID) references Actors(AID)
);

create table Trivia(
	TrvID number primary key,
	AID number,
	trivia_text varchar(1024),
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
	news_url varchar(512),
	foreign key (AID) references Actors(AID)
);
