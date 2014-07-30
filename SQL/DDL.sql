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
