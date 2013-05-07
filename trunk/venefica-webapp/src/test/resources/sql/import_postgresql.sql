
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
insert into memberuserdata ("id", "callsAllowed", "emailsAllowed", "smsAllowed") values (1, 'f', 'f', 'f');
insert into memberuserdata ("id", "callsAllowed", "emailsAllowed", "smsAllowed") values (2, 'f', 'f', 'f');
insert into memberuserdata ("id", "callsAllowed", "emailsAllowed", "smsAllowed") values (3, 'f', 'f', 'f');
insert into memberuserdata ("id", "callsAllowed", "emailsAllowed", "smsAllowed") values (4, 'f', 'f', 'f');

-- Users
delete from local_user;
insert into local_user ("id", "name", "password", "email") values (nextval('user_seq'), 'first', '12345', 'firstUser@gmail.com');
insert into local_user ("id", "name", "password", "email") values (nextval('user_seq'), 'second', '12345', 'secondUser@gmail.com');
insert into local_user ("id", "name", "password", "email") values (nextval('user_seq'), 'third', '12345', 'thirdUser@gmail.com');
insert into local_user ("id", "name", "password", "email") values (nextval('user_seq'), 'null', '12345', 'null@gmail.com');

-- Categories
delete from category;
insert into category ("id", "name", "hidden") values (nextval('cat_seq'), 'local places', 'f');
insert into category ("id", "name", "hidden") values (nextval('cat_seq'), 'buy/sell/trade', 'f');
insert into category ("id", "name", "hidden") values (nextval('cat_seq'), 'automotive', 'f');
insert into category ("id", "name", "hidden") values (nextval('cat_seq'), 'musician', 'f');
insert into category ("id", "name", "hidden") values (nextval('cat_seq'), 'rentals', 'f');
insert into category ("id", "name", "hidden") values (nextval('cat_seq'), 'real estate', 'f');
insert into category ("id", "name", "hidden") values (nextval('cat_seq'), 'jobs', 'f');
insert into category ("id", "name", "hidden", "parent_id") values (nextval('cat_seq'), 'events', 'f', '1');
insert into category ("id", "name", "hidden", "parent_id") values (nextval('cat_seq'), 'bars/clubs', 'f', '1');
insert into category ("id", "name", "hidden", "parent_id") values (nextval('cat_seq'), 'restaurants', 'f', '1');
insert into category ("id", "name", "hidden", "parent_id") values (nextval('cat_seq'), 'salons/nails/spas', 'f', '1');

-- Ads
delete from ad;
insert into ad ("id", "creator_id", "category_id", "title", "createdAt", "expired", "deleted", "sold", "numviews", "wanted", "reviewed", "spam", "numavailprolongations", "rating", "selected", "sent", "received") values (nextval('ad_seq'), 1, 1, 'test ad', now(), 'f', 'f', 'f', 0, 'f', 'f', 'f', 1, 0.0, 'f', 'f', 'f');

-- Bookmarks
delete from bookmark;
insert into bookmark ("id", "user_id", "ad_id") values (nextval('bookmark_seq'), 1, 1);

-- Messages
--delete from message;
--insert into message ("id", "to_id", "hiddenbyrecipient", "from_id", "hiddenbysender", "text", "readd", "createdat") values (nextval('message_seq'), 1, 'f', 2, 'f', 'Test message', 'f', now());
