insert into `user`(is_deleted, email, first_name, is_admin, last_login, last_name, password, username, display_name, description, role)
values (false, 'pera@mail.com', 'Pera', true, null, 'Peric', '$2a$12$6LRoZ4kDywW7WnK9bg16A.XXVHgKXxpi6YZ5JYptFnwW3y97DZGju', 'pera', "-pera-", "I am admin of this app", 'ADMIN');
insert into `user`( is_deleted, email, first_name, is_admin, last_login, last_name, password, username, display_name, description, role)
values (false, 'mika@mail.com', 'Mika', false, null, 'Mikic', '$2a$12$15ymkpdnVT1DGRfGjjqIY.SnwcaMTyiIUb71f3r3Be8i3zHuNRM.i', 'mika', '-mika-', "I am user of this app", 'USER');
insert into `user`(is_deleted, email, first_name, is_admin, last_login, last_name, password, username, display_name, description, role)
values (false, 'ana@mail.com', 'Ana', false, null, 'Anic', '$2a$12$uVuGNCVu62e8v7YtlF9yZurtYkvWgOj9N5UEdb51eB1EM959We.v.', 'ana','-ana-', "I am student of ftn", 'USER');
insert into `user`(is_deleted, email, first_name, is_admin, last_login, last_name, password, username, display_name, description, role)
values (false, 'natasa@mail.com', 'Natasa', false, null, 'Kotaranin', '$2a$12$bwkBqUjK8dS9fU1HzCGEd.mekeVQYgU5tZ0jH9gz5IMXMEbFIcbxu', 'natasa','-natasa-', "I am student of ftn", 'USER');
insert into `user`(is_deleted, email, first_name, is_admin, last_login, last_name, password, username, display_name, description, role)
values (false, 'zika@mail.com', 'Zika', false, null, 'Zikic', '$2a$12$TeQF.oCNjgTsl9rFWA9Tb.zA3716nzJZ5wwxONeu1tTzHoqBkk7FK', 'zika', '-zika-', "I love nature and animals", 'USER');
insert into `user`(is_deleted, email, first_name, is_admin, last_login, last_name, password, username, display_name, description, role)
values (false, 'djura@mail.com', 'Djura', false, null, 'Djuric', '$2a$12$n8eEY1H5vFbQyiHDmw3CG.ih35ZaWN4pjoY27e1rxrksdNwPDjWZW', 'djura', '-djurica-', "I am fan of cars", 'USER');

insert into user_friends(user_id, friend_id) values (2, 3);
insert into user_friends(user_id, friend_id) values (1, 2);
insert into user_friends(user_id, friend_id) values (1, 3);
insert into user_friends(user_id, friend_id) values (1, 4);
insert into user_friends(user_id, friend_id) values (1, 5);
insert into user_friends(user_id, friend_id) values (1, 6);

/*insert into friend_request (approved, at, created_at, is_deleted, from_user_id, to_user_id)
values (null, null, '2023-08-20', false, 2, 5);
insert into friend_request (approved, at, created_at, is_deleted, from_user_id, to_user_id)
values (null, null, '2023-08-22', false, 2, 4);*/

insert into post (content, creation_date, is_deleted, posted_by_user_id)
values ('This is first post by me. I am glad if you can see it.',
        '2023-08-12 12:00:00', false, 1);
insert into post (content, creation_date, is_deleted, posted_by_user_id)
values ('This is second post by me. I am glad if you can see it.',
        '2023-08-20 12:00:00', false, 1);
insert into post (content, creation_date, is_deleted, posted_by_user_id)
values ('This is new post for today, happy to be here.',
        '2023-08-08 18:20:30', false, 2);
insert into post (content, creation_date, is_deleted, posted_by_user_id)
values ('This is a post in a group. If you see it, you are inside a group',
        '2023-08-14 15:23:35', false, 1);
insert into post (content, creation_date, is_deleted, posted_by_user_id)
values ('This is another post for a group. Say hi to everyone in this group',
        '2023-08-15 14:56:55', false, 3);
insert into post (content, creation_date, is_deleted, posted_by_user_id)
values ('This is test post for second group. Say hi to everyone in this group',
        '2023-08-13 12:12:12', false, 3);
insert into post (content, creation_date, is_deleted, posted_by_user_id)
values ('This is Anas global post. Friends can see it. Sending good vibes to everyone.',
        '2023-08-26 05:22:12', false, 3);
insert into post (content, creation_date, is_deleted, posted_by_user_id)
    values ('This is Mikas global post. Friends can see it. Sending good vibes to everyone.',
        '2023-08-28 01:12:39', false, 2);

insert into comment (is_deleted, text, timestamp, belongs_to_post_id, belongs_to_user_id, replies_to_comment_id)
values (false, 'Nice picture!', '2023-08-11', 1, 2, null);
insert into comment (is_deleted, text, timestamp, belongs_to_post_id, belongs_to_user_id, replies_to_comment_id)
values (false, 'It really is!', '2023-08-11', 1, 1, 1);
insert into comment (is_deleted, text, timestamp, belongs_to_post_id, belongs_to_user_id, replies_to_comment_id)
values (false, 'I think this is very beautiful.', '2023-08-22', 1, 4, null);
insert into comment (is_deleted, text, timestamp, belongs_to_post_id, belongs_to_user_id, replies_to_comment_id)
values (false, 'This is my picture. Who gave you the right to post it?', '2023-08-25', 1, 5, null);
insert into comment (is_deleted, text, timestamp, belongs_to_post_id, belongs_to_user_id, replies_to_comment_id)
values (false, 'Where is this?', '2023-08-27', 1, 5, null);
insert into comment (is_deleted, text, timestamp, belongs_to_post_id, belongs_to_user_id, replies_to_comment_id)
values (false, 'In some place in Serbia.', '2023-08-28', 1, 1, 5);
insert into comment (is_deleted, text, timestamp, belongs_to_post_id, belongs_to_user_id, replies_to_comment_id)
values (false, 'Nature looks stunning!', '2023-08-28', 3, 5, null);

insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-11', 'HEART', 2, 1, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-11', 'HEART', 3, 1, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-11', 'HEART', 4, 1, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-22', 'HEART', 2, 3, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-23', 'HEART', 3, 3, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-22', 'HEART', 4, 2, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-12', 'LIKE', 3, 1, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-13', 'LIKE', 2, 1, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-22', 'LIKE', 2, 3, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-27', 'LIKE', 2, 5, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-27', 'LIKE', 3, 5, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-28', 'LIKE', 4, 5, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-28', 'LIKE', 5, 5, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-27', 'LIKE', 6, 5, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-28', 'LIKE', 3, 7, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-29', 'LIKE', 4, 7, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-28', 'LIKE', 4, null, 1);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-24', 'LIKE', 5, null, 1);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-25', 'DISLIKE', 2, 4, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-25', 'DISLIKE', 3, 4, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-26', 'DISLIKE', 4, 4, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-22', 'DISLIKE', 3, 3, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-23', 'DISLIKE', 4, 3, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-25', 'DISLIKE', 5, 4, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-27', 'DISLIKE', 5, 1, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-28', 'DISLIKE', 3, 7, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-29', 'DISLIKE', 2, 7, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-16', 'HEART', 5, null, 1);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-12', 'HEART', 4, null, 1);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-14', 'HEART', 3, null, 1);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-24', 'LIKE', 3, null, 2);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-08-27', 'DISLIKE', 4, null, 2);

/*
insert into report (accepted, is_deleted, reason, timestamp, by_user_id, on_comment_id, on_post_id, on_user_id)
values (true, false, 'HARASSMENT', '2023-05-12', 3, null, 1, null);
insert into report (accepted, is_deleted, reason, timestamp, by_user_id, on_comment_id, on_post_id, on_user_id)
values (false, false, 'HARASSMENT', '2023-05-12', 3, 1, null, null);
insert into report (accepted, is_deleted, reason, timestamp, by_user_id, on_comment_id, on_post_id, on_user_id)
values (false, false, 'HARASSMENT', '2023-05-12', 1, null, 2, null);
insert into report (accepted, is_deleted, reason, timestamp, by_user_id, on_comment_id, on_post_id, on_user_id)
values (true, true, 'HARASSMENT', '2023-05-12', 1, null, 2, null);*/

insert into `group` (creation_date, is_deleted, description, is_suspended, name, suspended_reason)
values ('2023-08-31', false, 'Test group for testing purposes', false, 'Group of Pera', null);
insert into `group` (creation_date, is_deleted, description, is_suspended, name, suspended_reason)
values ('2023-09-01', false, 'Another test group for testing', false, 'Group of Ana', null);
insert into `group` (creation_date, is_deleted, description, is_suspended, name, suspended_reason)
values ('2023-08-12', true, 'Deleted test group for testing', true, 'Test Group 3', 'Not usable');

insert into group_admins (group_id, admin_id)
values (1, 1);
insert into group_admins (group_id, admin_id)
values (2, 3);

insert into group_members (group_id, member_id)
values (1, 1);
insert into group_members (group_id, member_id)
values (1, 2);
insert into group_members (group_id, member_id)
values (2, 3);

insert into group_posts (group_id, post_id)
values (1, 3);
insert into group_posts (group_id, post_id)
values (1, 4);
insert into group_posts (group_id, post_id)
values (2, 5);

/*insert into group_request (approved, at, created_at, is_deleted, created_by_user_id, for_group_id)
values (null, null, '2023-05-13', false, 4, 2);*/


insert into image (is_deleted, path, belongs_to_post_id, belongs_to_user_id)
values (false, '../../assets/images/rsz_nature.jpg', 1, null);
insert into image (is_deleted, path, belongs_to_post_id, belongs_to_user_id)
values (false, '../../assets/images/rsz_1male-avatar-1_2.jpg', null, 1);
insert into image (is_deleted, path, belongs_to_post_id, belongs_to_user_id)
values (false, '../../assets/images/rsz_male-avatar-2_2.jpg', null, 2);
insert into image (is_deleted, path, belongs_to_post_id, belongs_to_user_id)
values (false, '../../assets/images/rsz_woman-avatar-1_2.jpg', null, 3);
insert into image (is_deleted, path, belongs_to_post_id, belongs_to_user_id)
values (false, '../../assets/images/rsz_1women-avatar_2.jpg', null, 4);
insert into image (is_deleted, path, belongs_to_post_id, belongs_to_user_id)
values (false, '../../assets/images/rsz_male-avatar-3_2.jpg', null, 5);
insert into image (is_deleted, path, belongs_to_post_id, belongs_to_user_id)
values (false, '../../assets/images/rsz_male-avatar.jpg', null, 6);
