create table business(
business_id VARCHAR2(22),
name varchar2(255),
full_address varchar2(255),
city varchar2(255),
state varchar2(255),
stars float,
review_count int,
open varchar2(20),
primary key (business_id));


create table hours(
business_id VARCHAR2(22),
day varchar2(20),
open varchar2(20),
close varchar2(20),
PRIMARY KEY(business_id,day),
FOREIGN KEY(business_id) REFERENCES business(business_id) ON DELETE CASCADE);

create index hrs_index on hours(business_id);

create table category(
business_id VARCHAR2(22),
category varchar2(255),
PRIMARY KEY(business_id,category),
FOREIGN KEY(business_id) REFERENCES business(business_id) ON DELETE CASCADE);

create index cat_index on category(business_id);

create table subcategory(
business_id VARCHAR2(22),
subcategory varchar2(255),
PRIMARY KEY(business_id,subcategory),
FOREIGN KEY(business_id) REFERENCES business(business_id) ON DELETE CASCADE);

create index subcat_index on subcategory(business_id);

create table attributes(
business_id VARCHAR2(22),
--att_name varchar2(255),
att_value varchar2(255),
primary key(business_id,att_value),
foreign key(business_id) references business(business_id) on delete cascade); 

create index att_index on attributes(business_id);

create table checkin(
business_id VARCHAR2(22),
from_time NUMBER,
to_time NUMBER,
day_checkin NUMBER,
count int,
primary key(business_id,from_time,to_time,day_checkin),
foreign key(business_id) references business(business_id)on delete cascade);

create index checkin_index on checkin(business_id);

create table yelp_user(
user_id varchar2(50),
name varchar2(50),
primary key(user_id));

create table review(
review_id varchar2(22),
stars int,
date1 varchar2(25),
text clob,
useful int,
business_id varchar2(22),
user_id varchar2(22),
primary key(review_id),
foreign key (business_id) references business(business_id)on delete cascade,
foreign key (user_id) references yelp_user(user_id)on delete cascade);

create index review_index on review(business_id);

--CREATING AUXILLARY TABLE FOR CAT, SUBCAT 
CREATE TABLE AUX_CAT_SUBCAT(business_id VARCHAR2(50), category VARCHAR2(100), subcategory VARCHAR2(100), 
                            PRIMARY KEY (business_id, category, subcategory));
--                            FOREIGN KEY(business_id)REFERENCES business(business_id));

--CREATING AUXILLARY TABLE FOR CAT, SUBCAT, ATTR 
CREATE TABLE AUX_CAT_SUBCAT_ATTR(business_id VARCHAR2(50), category VARCHAR2(100), subcategory VARCHAR2(100), att_value varchar2(255), 
                            PRIMARY KEY (business_id, category, subcategory, att_value));
--                            FOREIGN KEY(business_id)REFERENCES business(business_id));

--CREATING AUXILLARY TABLE FOR CAT, SUBCAT, ATTR, LOC 
CREATE TABLE AUX_CAT_SUBCAT_ATTR_LOC(business_id VARCHAR2(50), category VARCHAR2(100), subcategory VARCHAR2(100), 
att_value varchar2(255), loc varchar2(100), 
                            PRIMARY KEY (business_id, category, subcategory, att_value, loc));
--                           FOREIGN KEY(business_id)REFERENCES business(business_id));

--CREATING AUXILLARY TABLE FOR CAT, SUBCAT, ATTR, LOC, DAY 
CREATE TABLE AUX_CAT_SUBCAT_ATTR_LOC_DAY(business_id VARCHAR2(50), category VARCHAR2(100), subcategory VARCHAR2(100), 
att_value varchar2(255), loc varchar2(100), day varchar2(30), 
                            PRIMARY KEY (business_id, category, subcategory, att_value, loc, day));
--                            FOREIGN KEY(business_id)REFERENCES business(business_id));

--CREATING AUXILLARY TABLE FOR CAT, SUBCAT, ATTR, LOC, DAY, OPEN 
CREATE TABLE AUX_MC_SC_AT_LOC_DAY_FROM(business_id VARCHAR2(50), category VARCHAR2(100), subcategory VARCHAR2(100), 
att_value varchar2(255), loc varchar2(100), day varchar2(30), open varchar2(30), 
                            PRIMARY KEY (business_id, category, subcategory, att_value, loc, day, open));
--                            FOREIGN KEY(business_id)REFERENCES business(business_id));

--CREATING AUXILLARY TABLE FOR CAT, SUBCAT, ATTR, LOC, DAY, OPEN, CLOSE
CREATE TABLE AUX_MC_SC_AT_LOC_DAY_FROM_TO(business_id VARCHAR2(50), category VARCHAR2(100), subcategory VARCHAR2(100), 
att_value varchar2(255), loc varchar2(100), day varchar2(30), open varchar2(30), close varchar2(30), 
                            PRIMARY KEY (business_id, category, subcategory, att_value, loc, day, open, close));
--                            FOREIGN KEY(business_id)REFERENCES business(business_id));
                            
CREATE TABLE AUX_BUSINESS_RESULT(business_id VARCHAR2(50),
                             FOREIGN KEY(business_id)REFERENCES business(business_id));

CREATE TABLE AUX_BUS_USEDBY_SUBCAT(business_id VARCHAR2(50),
                             FOREIGN KEY(business_id)REFERENCES business(business_id));

CREATE TABLE AUX_BUS_USEDBY_ATTR(business_id VARCHAR2(50),
                             FOREIGN KEY(business_id)REFERENCES business(business_id));

CREATE TABLE AUX_BUS_USEDBY_LOC(business_id VARCHAR2(50),
                             FOREIGN KEY(business_id)REFERENCES business(business_id));

CREATE TABLE AUX_BUS_USEDBY_DAY(business_id VARCHAR2(50),
                             FOREIGN KEY(business_id)REFERENCES business(business_id));

CREATE TABLE AUX_BUS_USEDBY_FROM(business_id VARCHAR2(50),
                             FOREIGN KEY(business_id)REFERENCES business(business_id));

CREATE TABLE AUX_BUS_USEDBY_TO(business_id VARCHAR2(50),
                             FOREIGN KEY(business_id)REFERENCES business(business_id));
