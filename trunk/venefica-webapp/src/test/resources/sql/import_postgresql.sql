
-- Invitation
delete from invitation;
insert into invitation ("id", "code", "createdAt", "email", "expired", "numAvailUse") values (nextval('invitation_seq'), '12345', now(), 'firstUser@gmail.com', 'f', '5');
insert into invitation ("id", "code", "createdAt", "email", "expired", "numAvailUse") values (nextval('invitation_seq'), '11111', now(), 'null@gmail.com', 'f', '2');

-- User data
delete from userdata;
insert into userdata ("id", "phoneNumber") values (nextval('userdata_seq'), '123456789');
insert into userdata ("id", "phoneNumber") values (nextval('userdata_seq'), '123456789');
insert into userdata ("id", "phoneNumber") values (nextval('userdata_seq'), '123456789');
insert into userdata ("id", "phoneNumber") values (nextval('userdata_seq'), '000000');

-- Member user data
delete from memberuserdata;
insert into memberuserdata ("id", "firstName", "lastName") values (1, 'First', 'User');
insert into memberuserdata ("id", "firstName", "lastName") values (2, 'Second', 'User');
insert into memberuserdata ("id", "firstName", "lastName") values (3, 'Third', 'User');
insert into memberuserdata ("id", "firstName", "lastName") values (4, 'Null', 'User');

-- Users
delete from local_user;
insert into local_user ("id", "name", "password", "email", "adminn", "verified", "userData_id") values (nextval('user_seq'), 'first', '12345', 'firstUser@gmail.com', 't', 'f', 1);
insert into local_user ("id", "name", "password", "email", "adminn", "verified", "userData_id") values (nextval('user_seq'), 'second', '12345', 'secondUser@gmail.com', 'f', 'f', 2);
insert into local_user ("id", "name", "password", "email", "adminn", "verified", "userData_id") values (nextval('user_seq'), 'third', '12345', 'thirdUser@gmail.com', 'f', 'f', 3);
insert into local_user ("id", "name", "password", "email", "adminn", "verified", "userData_id") values (nextval('user_seq'), 'null', '12345', 'null@gmail.com', 'f', 'f', 4);

-- User point
delete from user_point;
insert into user_point ("id", "givingNumber", "receivingNumber") values (1, '0', '0');
insert into user_point ("id", "givingNumber", "receivingNumber") values (2, '0', '0');
insert into user_point ("id", "givingNumber", "receivingNumber") values (3, '0', '0');
insert into user_point ("id", "givingNumber", "receivingNumber") values (4, '0', '0');

update local_user set "userPoint_id"=1 where "id"=1;
update local_user set "userPoint_id"=2 where "id"=2;
update local_user set "userPoint_id"=3 where "id"=3;
update local_user set "userPoint_id"=4 where "id"=4;

-- Business categories
delete from businesscategory;
insert into businesscategory ("id", "name", "hidden") values (nextval('business_cat_seq'), 'Electronics', 'f');
insert into businesscategory ("id", "name", "hidden") values (nextval('business_cat_seq'), 'Furniture', 'f');

-- Categories
delete from category;
insert into category ("id", "name", "hidden") values (nextval('cat_seq'), 'local places', 'f');
insert into category ("id", "name", "hidden") values (nextval('cat_seq'), 'buy/sell/trade', 'f');
insert into category ("id", "name", "hidden") values (nextval('cat_seq'), 'automotive', 'f');
insert into category ("id", "name", "hidden") values (nextval('cat_seq'), 'musician', 'f');
insert into category ("id", "name", "hidden") values (nextval('cat_seq'), 'rentals', 'f');
insert into category ("id", "name", "hidden") values (nextval('cat_seq'), 'real estate', 'f');
insert into category ("id", "name", "hidden") values (nextval('cat_seq'), 'jobs', 'f');
insert into category ("id", "name", "hidden", "parent_id") values (nextval('cat_seq'), 'events', 'f', 1);
insert into category ("id", "name", "hidden", "parent_id") values (nextval('cat_seq'), 'bars/clubs', 'f', 1);
insert into category ("id", "name", "hidden", "parent_id") values (nextval('cat_seq'), 'restaurants', 'f', 1);
insert into category ("id", "name", "hidden", "parent_id") values (nextval('cat_seq'), 'salons/nails/spas', 'f', 1);

-- Ad data
delete from addata;
insert into addata ("id", "type", "category_id", "title", "quantity", "price") values (nextval('addata_seq'), 'MEMBER', 1, 'test ad', '1', '12.50');

-- Member ad data
delete from memberaddata;
insert into memberaddata ("id") values (1);

-- Ads
delete from ad;
insert into ad ("id", "adData_id", "creator_id", "createdAt", "expired", "deleted", "sold", "numviews", "reviewed", "spam", "numavailprolongations", "rating", "numexpire", "status", "expires", "approved", "online") values (nextval('ad_seq'), 1, 1, now(), 'f', 'f', 'f', 0, 'f', 'f', 1, 0.0, 0, 'ACTIVE', 't', 't', 't');

delete from user_transaction;
insert into user_transaction ("finalized", "pendingGivingNumber", "pendingReceivingNumber", "ad_id", "user_id", "userPoint_id") values ('f', '2.2625', '0', 1, 1, 1);

-- Bookmarks
delete from bookmark;
insert into bookmark ("id", "user_id", "ad_id") values (nextval('bookmark_seq'), 1, 1);

-- Messages
--delete from message;
--insert into message ("id", "to_id", "hiddenbyrecipient", "from_id", "hiddenbysender", "text", "readd", "createdat") values (nextval('message_seq'), 1, 'f', 2, 'f', 'Test message', 'f', now());
