
-- Users
delete from local_user;
insert into local_user("id", "name", "password", "email", "phonenumber", "businessacc", "callsallowed", "smsallowed", "emailsallowed") values (nextval('user_seq'), 'first', '12345', 'firstUser@gmail.com', '123456789', 'f', 'f', 'f', 'f');
insert into local_user("id", "name", "password", "email", "phonenumber", "businessacc", "callsallowed", "smsallowed", "emailsallowed") values (nextval('user_seq'), 'second', '12345', 'secondUser@gmail.com', '123456789', 'f', 'f', 'f', 'f');
insert into local_user("id", "name", "password", "email", "phonenumber", "businessacc", "callsallowed", "smsallowed", "emailsallowed") values (nextval('user_seq'), 'third', '12345', 'thirdUser@gmail.com', '123456789', 'f', 'f', 'f', 'f');
insert into local_user("id", "name", "password", "email", "phonenumber", "businessacc", "callsallowed", "smsallowed", "emailsallowed") values (nextval('user_seq'), null, '12345', 'null@gmail.com', '000000', 'f', 'f', 'f', 'f');

-- Categories
delete from category;
insert into category("id", "name", "hidden") values (nextval('cat_seq'), 'test category', 'f');

-- Ads
delete from ad;
insert into ad("id", "creator_id", "category_id", "title", "createdat", "expired", "deleted", "sold", "numviews", "wanted", "reviewed", "spam", "numavailprolongations", "rating") values (nextval('ad_seq'), 1, 1, 'test ad', now(), 'f', 'f', 'f', 0, 'f', 'f', 'f', 1, 0.0);

-- Bookmarks
delete from bookmark;
insert into bookmark("id", "user_id", "ad_id") values (nextval('bookmark_seq'), 1, 1);

-- Messages
-- delete from message;
-- insert into message("id", "to_id", "hiddenbyrecipient", "from_id", "hiddenbysender", "text", "read", "createdat") values (nextval('message_seq'), 1, 'f', 2, 'f', 'Test message', 'f', now());


