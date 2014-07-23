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
