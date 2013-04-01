
-- Invitation
delete from `invitation`;
insert into `invitation` (`code`, `createdAt`, `email`, `expired`, `numAvailUse`) values ('12345', now(), 'firstUser@gmail.com', '0', '5');

-- User data
delete from `userdata`;
insert into `userdata` (`id`, `phoneNumber`) values (1, '123456789');
insert into `userdata` (`id`, `phoneNumber`) values (2, '123456789');
insert into `userdata` (`id`, `phoneNumber`) values (3, '123456789');
insert into `userdata` (`id`, `phoneNumber`) values (4, '000000');

-- Member user data
delete from `memberuserdata`;
insert into `memberuserdata` (`id`, `callsAllowed`, `emailsAllowed`, `smsAllowed`) values (1, 0, 0, 0);
insert into `memberuserdata` (`id`, `callsAllowed`, `emailsAllowed`, `smsAllowed`) values (2, 0, 0, 0);
insert into `memberuserdata` (`id`, `callsAllowed`, `emailsAllowed`, `smsAllowed`) values (3, 0, 0, 0);
insert into `memberuserdata` (`id`, `callsAllowed`, `emailsAllowed`, `smsAllowed`) values (4, 0, 0, 0);

-- Users
delete from `local_user`;
insert into `local_user` (`name`, `password`, `email`, `userData_id`) values ('first', '12345', 'firstUser@gmail.com', 1);
insert into `local_user` (`name`, `password`, `email`, `userData_id`) values ('second', '12345', 'secondUser@gmail.com', 2);
insert into `local_user` (`name`, `password`, `email`, `userData_id`) values ('third', '12345', 'thirdUser@gmail.com', 3);
insert into `local_user` (`name`, `password`, `email`, `userData_id`) values ('null', '12345', 'null@gmail.com', 4);

-- Categories
delete from `category`;
insert into `category` (`name`, `hidden`) values ('test category', '0');

-- Ads
delete from `ad`;
insert into `ad` (`creator_id`, `category_id`, `title`, `createdAt`, `expired`, `deleted`, `sold`, `numviews`, `wanted`, `reviewed`, `spam`, `numavailprolongations`, `rating`) values (1, 1, 'test ad', now(), '0', '0', '0', 0, '0', '0', '0', 1, 0.0);

-- Bookmarks
delete from `bookmark`;
insert into `bookmark` (`user_id`, `ad_id`) values (1, 1);

-- Messages
--delete from `message`;
--insert into `message` (`to_id`, `hiddenbyrecipient`, `from_id`, `hiddenbysender`, `text`, `readd`, `createdat`) values (1, '0', 2, '0', 'Test message', '0', now());
