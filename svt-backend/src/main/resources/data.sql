insert into `user`(is_deleted, email, first_name, is_admin, last_login, last_name, password, username, display_name, description, role)
values (false, 'pera@mail.com', 'Pera', true, null, 'Peric', '$2a$12$6LRoZ4kDywW7WnK9bg16A.XXVHgKXxpi6YZ5JYptFnwW3y97DZGju', 'pera',
        "pera", "I am admin of this app", 'ADMIN');
insert into `user`( is_deleted, email, first_name, is_admin, last_login, last_name, password, username, display_name, role)
values (false, 'mika@mail.com', 'Mika', false, null, 'Mikic', '$2a$12$15ymkpdnVT1DGRfGjjqIY.SnwcaMTyiIUb71f3r3Be8i3zHuNRM.i', 'mika',
        'mika', 'USER');
insert into `user`(is_deleted, email, first_name, is_admin, last_login, last_name, password, username, role)
values (false, 'ana@mail.com', 'Ana', false, null, 'Anic', '$2a$12$uVuGNCVu62e8v7YtlF9yZurtYkvWgOj9N5UEdb51eB1EM959We.v.', 'ana', 'USER');
insert into `user`(is_deleted, email, first_name, is_admin, last_login, last_name, password, username, role)
values (false, 'zika@mail.com', 'Zika', false, null, 'Zikic', '$2a$12$TeQF.oCNjgTsl9rFWA9Tb.zA3716nzJZ5wwxONeu1tTzHoqBkk7FK', 'zika', 'USER');
insert into `user`(is_deleted, email, first_name, is_admin, last_login, last_name, password, username, role)
values (true, 'djura@mail.com', 'Djura', false, null, 'Djuric', '$2a$12$TeQF.oCNjgTsl9rFWA9Tb.zA3716nzJZ5wwxONeu1tTzHoqBkk7FK', 'djurica', 'USER');

insert into user_friends(user_id, friend_id) values (2, 3);
insert into user_friends(user_id, friend_id) values (1, 2);

insert into friend_request (approved, at, created_at, is_deleted, from_user_id, to_user_id)
values (null, null, '2023-05-13', false, 1, 3);
insert into friend_request (approved, at, created_at, is_deleted, from_user_id, to_user_id)
values (null, null, '2023-05-13', false, 2, 4);

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
        '2023-05-14 15:23:35', false, 1);
insert into post (content, creation_date, is_deleted, posted_by_user_id)
values ('This is another post for a group. Say hi to everyone in this group',
        '2023-05-15 14:56:55', false, 2);
insert into post (content, creation_date, is_deleted, posted_by_user_id)
values ('This is test post for second group. Say hi to everyone in this group',
        '2023-06-13 12:12:12', false, 3);
insert into post (content, creation_date, is_deleted, posted_by_user_id)
values ('This is Anas global post. Friends can see it. Sending good vibes to everyone.',
        '2023-07-20 05:22:12', false, 3);
insert into post (content, creation_date, is_deleted, posted_by_user_id)
    values ('This is Mikas global post. Friends can see it. Sending good vibes to everyone.',
        '2023-07-27 01:12:39', false, 2);

insert into comment (is_deleted, text, timestamp, belongs_to_post_id, belongs_to_user_id, replies_to_comment_id)
values (false, 'Good thoughts', '2023-08-11', 1, 2, null);
insert into comment (is_deleted, text, timestamp, belongs_to_post_id, belongs_to_user_id, replies_to_comment_id)
values (false, 'Fine answer to message', '2023-08-04', 1, 1, 1);
insert into comment (is_deleted, text, timestamp, belongs_to_post_id, belongs_to_user_id, replies_to_comment_id)
values (false, 'Another comment', '2023-06-22', 1, 3, null);

insert into report (accepted, is_deleted, reason, timestamp, by_user_id, on_comment_id, on_post_id, on_user_id)
values (true, false, 'HARASSMENT', '2023-05-12', 3, null, null, 4);
insert into report (accepted, is_deleted, reason, timestamp, by_user_id, on_comment_id, on_post_id, on_user_id)
values (true, false, 'HARASSMENT', '2023-05-12', 2, 1, null, null);
insert into report (accepted, is_deleted, reason, timestamp, by_user_id, on_comment_id, on_post_id, on_user_id)
values (true, false, 'HARASSMENT', '2023-05-12', 1, null, 2, null);

insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-05-12', 'HEART', 3, null, 1);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-05-13', 'LIKE', 3, 1, null);
insert into reaction (is_deleted, timestamp, type, made_by_user_id, on_comment_id, on_post_id)
values (false, '2023-05-13', 'DISLIKE', 3, 1, null);

insert into `group` (creation_date, is_deleted, description, is_suspended, name, suspended_reason)
values ('2023-05-11', false, 'Test group for testing purposes', false, 'Test Group 1', null);
insert into `group` (creation_date, is_deleted, description, is_suspended, name, suspended_reason)
values ('2023-05-11', false, 'Another test group for testing', false, 'Test Group 2', null);
insert into `group` (creation_date, is_deleted, description, is_suspended, name, suspended_reason)
values ('2023-05-12', true, 'Deleted test group for testing', true, 'Test Group 3', 'Un-moderated');

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

insert into group_request (approved, at, created_at, is_deleted, created_by_user_id, for_group_id)
values (null, null, '2023-05-13', false, 3, 1);

insert into banned (is_deleted, timestamp, by_group_admin_id, towards_user_id, for_group_id)
values (false, '2023-05-13', null, 1, null);

insert into image (is_deleted, path, belongs_to_post_id, belongs_to_user_id)
values (false, '../../assets/images/rsz_nature.jpg', 1, null);
insert into image (is_deleted, path, belongs_to_post_id, belongs_to_user_id)
values (false, '../../assets/images/rsz_1male-avatar-1_2.jpg', null, 1);
insert into image (is_deleted, path, belongs_to_post_id, belongs_to_user_id)
values (false, '../../assets/images/rsz_male-avatar-2_2.jpg', null, 2);
