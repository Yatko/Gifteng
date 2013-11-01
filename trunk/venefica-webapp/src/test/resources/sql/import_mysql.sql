
-- Invitation
delete from `invitation`;
insert into `invitation` (`code`, `createdAt`, `email`, `expired`, `numAvailUse`) values ('12345', now(), 'firstUser@gmail.com', '0', '5');
insert into `invitation` (`code`, `createdAt`, `email`, `expired`, `numAvailUse`) values ('11111', now(), 'null@gmail.com', '0', '2');

-- User data
delete from `userdata`;
insert into `userdata` (`id`, `phoneNumber`) values (1, '123456789');
insert into `userdata` (`id`, `phoneNumber`) values (2, '123456789');
insert into `userdata` (`id`, `phoneNumber`) values (3, '123456789');
insert into `userdata` (`id`, `phoneNumber`) values (4, '000000');

-- Member user data
delete from `memberuserdata`;
insert into `memberuserdata` (`id`, `firstName`, `lastName`) values (1, 'First', 'User');
insert into `memberuserdata` (`id`, `firstName`, `lastName`) values (2, 'Second', 'User');
insert into `memberuserdata` (`id`, `firstName`, `lastName`) values (3, 'Third', 'User');
insert into `memberuserdata` (`id`, `firstName`, `lastName`) values (4, 'Null', 'User');

-- Users
delete from `local_user`;
insert into `local_user` (`name`, `password`, `email`, `adminn`, `verified`, `userData_id`, `deleted`) values ('first', '12345', 'firstUser@gmail.com', '1', '0', 1, '0');
insert into `local_user` (`name`, `password`, `email`, `adminn`, `verified`, `userData_id`, `deleted`) values ('second', '12345', 'secondUser@gmail.com', '0', '0', 2, '0');
insert into `local_user` (`name`, `password`, `email`, `adminn`, `verified`, `userData_id`, `deleted`) values ('third', '12345', 'thirdUser@gmail.com', '0', '0', 3, '0');
insert into `local_user` (`name`, `password`, `email`, `adminn`, `verified`, `userData_id`, `deleted`) values ('null', '12345', 'null@gmail.com', '0', '0', 4, '0');

-- User point
delete from `user_point`;
insert into `user_point` (`requestLimit`, `givingNumber`, `receivingNumber`) values ('5', '0', '0');
insert into `user_point` (`requestLimit`, `givingNumber`, `receivingNumber`) values ('5', '0', '0');
insert into `user_point` (`requestLimit`, `givingNumber`, `receivingNumber`) values ('5', '0', '0');
insert into `user_point` (`requestLimit`, `givingNumber`, `receivingNumber`) values ('5', '0', '0');

update `local_user` set `userPoint_id`=1 where `id`=1;
update `local_user` set `userPoint_id`=2 where `id`=2;
update `local_user` set `userPoint_id`=3 where `id`=3;
update `local_user` set `userPoint_id`=4 where `id`=4;

-- Business categories
delete from `businesscategory`;
insert into `businesscategory` (`name`, `hidden`) values ('Electronics', '0');
insert into `businesscategory` (`name`, `hidden`) values ('Furniture', '0');

-- Categories
delete from `category`;
insert into `category` (`name`, `hidden`) values ('local places', '0');
insert into `category` (`name`, `hidden`) values ('buy/sell/trade', '0');
insert into `category` (`name`, `hidden`) values ('automotive', '0');
insert into `category` (`name`, `hidden`) values ('musician', '0');
insert into `category` (`name`, `hidden`) values ('rentals', '0');
insert into `category` (`name`, `hidden`) values ('real estate', '0');
insert into `category` (`name`, `hidden`) values ('jobs', '0');
insert into `category` (`name`, `hidden`, `parent_id`) values ('events', '0', '1');
insert into `category` (`name`, `hidden`, `parent_id`) values ('bars/clubs', '0', '1');
insert into `category` (`name`, `hidden`, `parent_id`) values ('restaurants', '0', '1');
insert into `category` (`name`, `hidden`, `parent_id`) values ('salons/nails/spas', '0', '1');

-- Ad data
delete from `addata`;
insert into `addata` (`id`, `type`, `category_id`, `title`, `quantity`, `price`) values (1, 'MEMBER', 1, 'test ad', '1', '12.50');

-- Member ad data
delete from `memberaddata`;
insert into `memberaddata` (`id`) values (1);

-- Ads
delete from `ad`;
insert into `ad` (`adData_id`, `creator_id`, `createdAt`, `expired`, `deleted`, `sold`, `numviews`, `reviewed`, `spam`, `numavailprolongations`, `rating`, `numexpire`, `status`, `expires`, `approved`, `online`) values (1, 1, now(), '0', '0', '0', 0, '0', '0', 1, 0.0, '0', 'ACTIVE', '1', '1', '1');

delete from `request`;
insert into `request` (`ad_id`, `user_id`, `status`, `deleted`, `selected`, `hidden`, `sent`, `received`, `messagesHiddenByCreator`, `messagesHiddenByRequestor`) values (1, 4, 'PENDING', '0', '0', '0', '0', '0', '0', '0');

delete from `user_transaction`;
insert into `user_transaction` (`finalized`, `pendingGivingNumber`, `pendingReceivingNumber`, `ad_id`, `user_id`, `userPoint_id`, `status`) values ('0', '2.2625', '0', 1, 1, 1, 'NONE');

-- Bookmarks
delete from `bookmark`;
insert into `bookmark` (`user_id`, `ad_id`) values (3, 1);

-- Messages
--delete from `message`;
--insert into `message` (`to_id`, `hiddenbyrecipient`, `from_id`, `hiddenbysender`, `text`, `readd`, `createdat`) values (1, '0', 2, '0', 'Test message', '0', now());
