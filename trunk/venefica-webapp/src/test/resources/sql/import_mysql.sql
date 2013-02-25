
-- Users
delete from `local_user`;
insert into `local_user` (`name`, `password`, `email`, `phonenumber`, `businessacc`, `callsallowed`, `smsallowed`, `emailsallowed`) values ('first', '12345', 'firstUser@gmail.com', '123456789', '0', '0', '0', '0');
insert into `local_user` (`name`, `password`, `email`, `phonenumber`, `businessacc`, `callsallowed`, `smsallowed`, `emailsallowed`) values ('second', '12345', 'secondUser@gmail.com', '123456789', '0', '0', '0', '0');
insert into `local_user` (`name`, `password`, `email`, `phonenumber`, `businessacc`, `callsallowed`, `smsallowed`, `emailsallowed`) values ('third', '12345', 'thirdUser@gmail.com', '123456789', '0', '0', '0', '0');
insert into `local_user` (`name`, `password`, `email`, `phonenumber`, `businessacc`, `callsallowed`, `smsallowed`, `emailsallowed`) values (null, '12345', 'null@gmail.com', '000000', '0', '0', '0', '0');

-- Categories
delete from `category`;
insert into `category` (`name`, `hidden`) values ('test category', '0');

-- Ads
delete from `ad`;
insert into `ad` (`creator_id`, `category_id`, `title`, `createdat`, `expired`, `deleted`, `sold`, `numviews`, `wanted`, `reviewed`, `spam`, `numavailprolongations`, `rating`) values (1, 1, 'test ad', now(), '0', '0', '0', 0, '0', '0', '0', 1, 0.0);

-- Bookmarks
delete from `bookmark`;
insert into `bookmark` (`user_id`, `ad_id`) values (1, 1);

-- Messages
--delete from `message`;
--insert into `message` (`to_id`, `hiddenbyrecipient`, `from_id`, `hiddenbysender`, `text`, `readd`, `createdat`) values (1, '0', 2, '0', 'Test message', '0', now());
