
drop table if exists divisions;
drop table if exists ranks;
drop table if exists votestat;
drop table if exists tenure;
drop table if exists template;
drop table if exists vote_cast;
drop table if exists token;
drop table if exists ballotOption;
drop table if exists ballot;
drop table if exists faculty;
drop table if exists AcUser;
drop table if exists properties;
 

create table divisions (
       code varchar2(20) primary key,
       label varchar2(20) not null
 );
insert into divisions (code,label) values ('HU','Humanities');
insert into divisions (code,label) values ('SC','Sciences');
insert into divisions (code,label) values ('SS','Social Sciences');
insert into divisions (code,label) values ('CW','Campus Wide');


create table ranks (
       code varchar2(20) primary key,
       label varchar2(20) not null
 );
insert into ranks (code,label) values ('SUST','Staff');
insert into ranks (code,label) values ('ADJU','Adjunct');
insert into ranks (code,label) values ('VSTG','Visiting');
insert into ranks (code,label) values ('COAC','Coach');
insert into ranks (code,label) values ('EMER','Emeritus');
insert into ranks (code,label) values ('INST','Instructor');
insert into ranks (code,label) values ('ADST','Administration Staff');
insert into ranks (code,label) values ('ASIP','Assistant Prof');
insert into ranks (code,label) values ('ASOP','Associate Prof');
insert into ranks (code,label) values ('PROF','Full Prof');
insert into ranks (code,label) values ('OTFA','OTFA?');
insert into ranks (code,label) values ('NA','Not Applicable');


create table votestat (
       code varchar2(20) primary key,
       label varchar2(20) not null
 );

insert into votestat (code,label) values ('VT','Voting');
insert into votestat (code,label) values ('NV','Non-Voting');


create table tenure (
       code varchar2(20) primary key,
       label varchar2(20) not null
 );

insert into tenure (code,label) values ('TT','Tenure Track');
insert into tenure (code,label) values ('T','Tenured');
insert into tenure (code,label) values ('NA','NA');

 

create table template (
       tid bigint generated always as identity not null,
       template_title varchar2(50),
       ballot_title varchar2(50),
       instructions varchar2(512),
       description varchar2(512),
       vote_type varchar2(5),
       outcomes numeric(2,0),
       faculty boolean default TRUE,
       primary key (tid)
);


create table ballot (
       bid bigint generated always as identity not null,
       title varchar2(50) not null,
       instructions varchar2(512),
       description varchar2(512),
       outcomes numeric(2,0),
       open_time timestamp,
       close_time timestamp,
       vote_expected int,
       vote_type varchar2(5),
       voters varchar2(2),
       active boolean default FALSE,
       locked boolean default FALSE,
       faculty boolean default TRUE,
       primary key (bid)
);


create table ballotOption (
       bid bigint not null,
       oid varchar2(10) not null,
       label varchar2(30),
       enabled boolean default TRUE,
       foreign key (bid) references ballot(bid) on delete cascade,
       primary key (bid, oid)
);


create table faculty (
       acid varchar2(10) not null,
       lname varchar2(20),
       fname varchar2(20),
       dept varchar2(5) not null,
       div varchar2(5) not null,
       rank varchar2(20) default 'NA' not null,
       tenure varchar2(5) default 'NA',
       voting boolean default FALSE,
       email varchar2(50),
       active boolean default TRUE,
       primary key (acid)
);


create table token (
       tid bigint generated always as identity,
       keyval varchar2(32) UNIQUE,
       bid bigint,
       acid varchar2(10),
       primary key (tid),
       foreign key (bid) references ballot(bid) on delete cascade,
       foreign key (acid) references faculty(acid) on delete cascade
);


create table vote_cast (
       bid BIGINT,
       oid varchar2(10),
       token varchar2(32),
       rank int,
       constraint vote_cast_pk primary key (bid, oid, token),
       constraint vote_cast_fk_bid foreign key (bid, oid) references ballotOption on delete cascade
);

create table AcUser (
       uid varchar2(32) primary key,
       role varchar2(16) not null
);

create table properties(
       propkey varchar2(256) primary key,
       propval varchar2(256) not null
);

insert into template (template_title, ballot_title, instructions, description, vote_type, outcomes, faculty)
values('CC',
'Curriculum Committee',
'Please rank the following candidates by transfering to your ballot and ordering them as needed according your preference. Click SUBMIT to cast your ballot.', 
'<b>Committee Charge: </b>3-year term.  The Executive Committee of the Faculty advises the full Faculty on any matters of organization and represents that body, as appropriate, before other campus constituencies',
'irv',1,TRUE);

insert into template (template_title, ballot_title, instructions, description, vote_type, outcomes, faculty)
values('JC',
'Johnson Center Committee',
'Please rank the following candidates by transfering to your ballot and ordering them as needed according your preference. Click SUBMIT to cast your ballot.', 
'',
'irv',1,TRUE);

insert into template (template_title, ballot_title, instructions, description, vote_type, outcomes, faculty)
values('JC',
'Johnson Center Committee',
'Please rank the following candidates by transfering to your ballot and ordering them as needed according your preference. Click SUBMIT to cast your ballot.', 
'<b>Committee Charge:</b>  3 year term to support faculty professional development, teaching, and scholarship.',
'irv',1,TRUE);

insert into template (template_title, ballot_title, instructions, description, vote_type, outcomes, faculty)
values('FEC',
'Faculty Executive Committee (FEC)',
'Please rank the following candidates by transfering to your ballot and ordering them as needed according your preference. Click SUBMIT to cast your ballot.', 
'<b>Committee Charge: </b>3-year term.  The Executive Committee of the Faculty advises the full Faculty on any matters of organization and represents that body, as appropriate, before other campus constituencies',
'irv',1,TRUE);

insert into template (template_title, ballot_title, instructions, description, vote_type, outcomes, faculty)
values('FRPC',
'Faculty Review and Promotion (FRPC) Committee',
'Please rank the following candidates by transfering to your ballot and ordering them as needed according your preference. Click SUBMIT to cast your ballot.', 
'<p><b>Committee Charge:</b>  Reviews tenure and promotion cases and makes a recommendation to the President. Advises the President for named chairs and professorships.',
'irv',1,TRUE);

insert into template (template_title, ballot_title, instructions, description, vote_type, outcomes, faculty)
values('Grievance',
'Faculty Grievance Committee',
'Please rank the following candidates by transfering to your ballot and ordering them as needed according your preference. Click SUBMIT to cast your ballot.', 
'<p><b>Committee Charge:</b> 3 members elected annually for one year of service. To attempt to mediate grievances brought by faculty in accordance with the procedures outlined in the <em>Judicial Guidelines and Procedures for the Faculty, JP 4</em>.',
'irv',3,TRUE);

insert into template (template_title, ballot_title, instructions, description, vote_type, outcomes, faculty)
values('Hearing',
'Faculty Hearing Committee',
'Please rank the following candidates by transfering to your ballot and ordering them as needed according your preference. Click SUBMIT to cast your ballot.',
'<b>Committee Charge:</b>5 members elected annually (all tenured excluding FRPC).',
'irv',5,TRUE);


insert into ballot (title, instructions, description, outcomes, open_time, close_time, vote_expected, vote_type, voters, active, locked, faculty) values ('Coolest CS Professor', 'Please drag and drop these candidates to the right column to vote for them. Ranking does matter, so make sure they are in your desired order.', 'This is a description for ballot 1!', 1, parsedatetime('2023-06-08 16:00:00', 'yyyy-MM-dd HH:mm:ss'), parsedatetime('2023-07-08 16:00:00', 'yyyy-MM-dd HH:mm:ss'), 103, 'IRV', 'CW', FALSE, FALSE, TRUE);
insert into ballot (title, instructions, description, outcomes, open_time, close_time, vote_expected, vote_type, voters, active, locked, faculty) values ('These are definitely some CS professors', 'Please drag and drop these candidates to the right column to vote for them. Ranking does matter, so make sure they are in your desired order. This is for ballot 2', 'This description describes ballot 2.', 1, parsedatetime('2025-01-01 24:00:00', 'yyyy-MM-dd HH:mm:ss'), parsedatetime('2025-12-31 24:00:00', 'yyyy-MM-dd HH:mm:ss'), 27, 'IRV', 'CW', TRUE, TRUE, TRUE);
insert into ballot (title, instructions, description, outcomes, open_time, close_time, vote_expected, vote_type, voters, active, locked, faculty) values ('Favorite Foods', 'Rank all of the foods listed.', 'Vote for you favorite food.', 2, parsedatetime('2025-01-01 24:00:00', 'yyyy-MM-dd HH:mm:ss'), parsedatetime('2025-12-31 24:00:00', 'yyyy-MM-dd HH:mm:ss'), 100, 'IRV', 'SC', TRUE, TRUE, FALSE);
insert into ballot (title, instructions, description, outcomes, open_time, close_time, vote_expected, vote_type, voters, active, locked, faculty) values ('More Favorite Foods', 'Rank all of the foods listed. Or else...', 'Vote for you favorite food. Or else...', 3, parsedatetime('2025-01-01 24:00:00', 'yyyy-MM-dd HH:mm:ss'), parsedatetime('2025-12-31 24:00:00', 'yyyy-MM-dd HH:mm:ss'), 100, 'IRV2', 'SC', TRUE, TRUE, FALSE);

insert into ballotOption (bid, oid, label) values (1, '5854836', 'Cathy Richardson');
insert into ballotOption (bid, oid, label) values (1, '4138778', 'Ed Richardson');

insert into ballotOption (bid, oid, label) values (2, '5993138', 'David Schones');

insert into ballotOption (bid, oid, label) values (3, 'ip', 'Pizza');
insert into ballotOption (bid, oid, label) values (3, 'js', 'Sushi');
insert into ballotOption (bid, oid, label) values (3, 'mt', 'Tacos');
insert into ballotOption (bid, oid, label) values (3, 'th', 'Thai');

insert into ballotOption (bid, oid, label) values (4, 'ip', 'Pizza');
insert into ballotOption (bid, oid, label) values (4, 'js', 'Sushi');
insert into ballotOption (bid, oid, label) values (4, 'mt', 'Tacos');
insert into ballotOption (bid, oid, label) values (4, 'th', 'Thai');
insert into ballotOption (bid, oid, label) values (4, 'ab', 'Burger');
insert into ballotOption (bid, oid, label) values (4, 'sp', 'Paella');
insert into ballotOption (bid, oid, label) values (4, 'va', 'Arepa');
insert into ballotOption (bid, oid, label) values (4, 'sh', 'Haggis');
insert into ballotOption (bid, oid, label) values (4, 'kr', 'Ramen');
insert into ballotOption (bid, oid, label) values (4, 'ce', 'Egg Roll');
insert into ballotOption (bid, oid, label) values (4, 'ic', 'Curry');
insert into ballotOption (bid, oid, label) values (4, 'as', 'Steak');
insert into ballotOption (bid, oid, label) values (4, 'ms', 'Salad');
insert into ballotOption (bid, oid, label) values (4, 'aq', 'BBQ');



insert into faculty (acid, lname, fname, rank, dept, div, email, tenure, voting, active) values ('5854836','Richardson','Cathy','ADJU','MUS','HU','crichardson@austincollege.edu',null,FALSE,TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, email, tenure, voting, active) values ('4138778','Richardson','Ed','SUST','CW','CW','edrichardson@austincollege.edu',null,FALSE,TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('5488165','Rivers','Sylvia','ADJU','MUS','HU',null,FALSE,'srivers@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('7095095','Roth','Miles','NA','MUS','HU',null,FALSE,'mroth@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('2323136','Schable','Mandy','SUST','BIOL','SC',null,FALSE,'nschable@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('5993138','Schones','David','ADJU','REL','HU',null,FALSE,'dschones@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('8684581','Snell','Steve','ADJU','PSCI','SS',null,FALSE,'ssnell@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('2146198','Stone','Theresa','VSTG','EDUC','SS',null,FALSE,'tstone@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('1967882','Sullivan','Zachary','ADJU','KINES','SS',null,FALSE,'zsullivan@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('5791845','Vallejo-Nieto','Alejandra','ADJU','CW','CW',null,FALSE,'anieto@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('9425799','Wecker','Rodney','COAC','KINES','SS',null,FALSE,'rwecker@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('8419090','Whelan','Leslie','NA','MUS','HU',null,FALSE,'lwhelan@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('6425057','Aiello','David','ASOP','BIOL','SC','T',TRUE,'daiello@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('3853609','Akuoko','Mathias','ASIP','PUBH','SC','TT',TRUE,'makuoko@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('4332623','Baker','David','PROF','PHY','SC','T',TRUE,'dbaker@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('9011615','Bangara','Saritha','ASOP','PUBH','SC','T',TRUE,'sbangara@austincollege.edu',FALSE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('6407820','Banks','Liz','ASOP','CMT','HU','T',TRUE,'banks@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('8090379','Barton','Lance','PROF','BIOL','SC','T',TRUE,'lbarton@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('4580739','Bigelow','Nate','PROF','PSCI','SS','T',TRUE,'nbigelow@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('8299895','Blake','Tom','ASOP','ENG','HU','T',TRUE,'tblake@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('3594249','Block','Aaron','ASOP','MACS','SC','T',TRUE,'ablock@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('4896352','Boessen','Brett','ASOP','CMT','HU','T',TRUE,'bboessen@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('2169323','Bowman','Cate','ASIP','SOCAN','SS','TT',TRUE,'cbowman@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('9783086','Brandl','Meg','ASIP','ENG','HU','TT',TRUE,'mbrandl@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('4381605','Brown','Lisa','PROF','PSY','SS','T',TRUE,'lbrown@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('1299808','Bumpus','J''Lee','PROF','MACS','SC','T',TRUE,'jbumpus@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('0468076','Cape','Bob','PROF','CLML','HU','T',TRUE,'rcape@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('2728007','Cape','Ruth','ASOP','CLML','HU','T',TRUE,'ricape@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('3931528','Carr','Andy','PROF','CHEM','SC','T',TRUE,'acarr@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('4879105','Carroll','Kelli','ASIP','BIOL','SC','TT',TRUE,'kcarroll@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('0298799','Cornelison-Brown','Shannon','ASIP','ECBA','SS','TT',TRUE,'sbrown@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('4443407','Countryman','Renee','PROF','PSY','SS','T',TRUE,'rcountryman@austincollege.edu',FALSE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('9459939','Crannell','Wayne','ASOP','MUS','HU','T',TRUE,'wcrannell@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('9855943','Dominick','Dan','PROF','MUS','HU','T',TRUE,'ddominick@austincollege.edu',TRUE);
insert into faculty (acid, lname, fname, rank, dept, div, tenure, voting, email, active) values ('3372158','Dryburgh','Martinella','ASOP','LEAD','SS',null,TRUE,'mdryburgh@austincollege.edu',TRUE);


insert into vote_cast ( bid,oid,token,rank) values (3,'ip','40353fbd9b3aad310fe2bc5b5a8e133c',1);
insert into vote_cast ( bid,oid,token,rank) values (3,'th','40353fbd9b3aad310fe2bc5b5a8e133c',2);
insert into vote_cast ( bid,oid,token,rank) values (3,'mt','40353fbd9b3aad310fe2bc5b5a8e133c',3);
insert into vote_cast ( bid,oid,token,rank) values (3,'js','40353fbd9b3aad310fe2bc5b5a8e133c',4);
insert into vote_cast ( bid,oid,token,rank) values (3,'th','644a782c5d78c4b8754f47338608f9cc',1);
insert into vote_cast ( bid,oid,token,rank) values (3,'js','644a782c5d78c4b8754f47338608f9cc',2);
insert into vote_cast ( bid,oid,token,rank) values (3,'ip','644a782c5d78c4b8754f47338608f9cc',3);
insert into vote_cast ( bid,oid,token,rank) values (3,'mt','644a782c5d78c4b8754f47338608f9cc',4);
insert into vote_cast ( bid,oid,token,rank) values (3,'mt','b32820dfa6347f9bbc828eafb776712f',1);
insert into vote_cast ( bid,oid,token,rank) values (3,'th','b32820dfa6347f9bbc828eafb776712f',2);
insert into vote_cast ( bid,oid,token,rank) values (3,'ip','b32820dfa6347f9bbc828eafb776712f',3);
insert into vote_cast ( bid,oid,token,rank) values (3,'js','b32820dfa6347f9bbc828eafb776712f',4);
insert into vote_cast ( bid,oid,token,rank) values (3,'mt','28c8bcf85d8e69873b96938c1a23a77f',1);
insert into vote_cast ( bid,oid,token,rank) values (3,'ip','28c8bcf85d8e69873b96938c1a23a77f',2);
insert into vote_cast ( bid,oid,token,rank) values (3,'js','28c8bcf85d8e69873b96938c1a23a77f',3);
insert into vote_cast ( bid,oid,token,rank) values (3,'th','28c8bcf85d8e69873b96938c1a23a77f',4);
insert into vote_cast ( bid,oid,token,rank) values (3,'js','c782bcd5fef0667e54d52803faba656b',1);
insert into vote_cast ( bid,oid,token,rank) values (3,'ip','c782bcd5fef0667e54d52803faba656b',2);
insert into vote_cast ( bid,oid,token,rank) values (3,'th','c782bcd5fef0667e54d52803faba656b',3);
insert into vote_cast ( bid,oid,token,rank) values (3,'mt','c782bcd5fef0667e54d52803faba656b',4);
insert into vote_cast ( bid,oid,token,rank) values (3,'ip','47a68cc7986244f78efce26a86d5779d',1);
insert into vote_cast ( bid,oid,token,rank) values (3,'th','47a68cc7986244f78efce26a86d5779d',2);
insert into vote_cast ( bid,oid,token,rank) values (3,'mt','47a68cc7986244f78efce26a86d5779d',3);
insert into vote_cast ( bid,oid,token,rank) values (3,'js','47a68cc7986244f78efce26a86d5779d',4);

insert into token (keyval,bid,acid) values ('0d354582ca957a881ac85b7dfdbe8163',3,'2323136'); 
insert into token (keyval,bid,acid) values ('7969c2f99032a73348bfca4f2c80ba00',3,'6425057'); 
insert into token (keyval,bid,acid) values ('db858663c59dc97addf3e349880cb590',3,'3853609'); 
insert into token (keyval,bid,acid) values ('6018522f38570996801cf2a22ba743fb',3,'4332623'); 
insert into token (keyval,bid,acid) values ('fb37dff111c2f0f0c4928dce248bf307',3,'9011615'); 
insert into token (keyval,bid,acid) values ('d86479351fd357e16dd68e6f15af58a3',3,'8090379'); 
insert into token (keyval,bid,acid) values ('60c0c1362aafefb214666233c156bc1f',3,'3594249'); 
insert into token (keyval,bid,acid) values ('234ae868e24e202982d9389e9cbb55a7',3,'1299808'); 
insert into token (keyval,bid,acid) values ('180aeaf626bc1148a6fc034622886010',3,'3931528'); 
insert into token (keyval,bid,acid) values ('0cfd26b73161d6d2d1b76832a2a72a31',3,'4879105');
insert into token (keyval,bid,acid) values ('0cfd26b73161d6d2d1b76832a2a72a32',4,'4879105');


insert into AcUser (uid, role) values ('arosenberg20', 'ADMIN');
insert into AcUser (uid, role) values ('gvengalasetti19', 'ADMIN');
insert into AcUser (uid, role) values ('bhill20', 'ADMIN');
insert into AcUser (uid, role) values ('kleahy20', 'ADMIN');
insert into AcUser (uid, role) values ('mhiggs', 'VOTER');
insert into AcUser (uid, role) values ('ablock', 'VIEWER');
insert into AcUser (uid, role) values ('jedge', 'EDITOR');

insert into properties (propkey, propval) values ('faculty.last.import', '2023-06-08T13:23:05');
insert into properties (propkey, propval) values ('faculty.import.june', '2023-06-08T13:23:05');
insert into properties (propkey, propval) values ('faculty.last.may', '2023-06-08T13:23:05');
