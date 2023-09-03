insert into boards(id, title, content, created_at, parent_id) values
        (10, 'title1', 'content1', '2019-01-01 00:00:00', null),
        (20, 'title2', 'content2', '2019-01-01 00:00:00', 10),
        (30, 'title3', 'content3', '2019-01-01 00:00:00', 10),
        (40, 'title4', 'content4', '2019-01-01 00:00:00', 20),
        (50, 'title5', 'content5', '2019-01-01 00:00:00', 20),
        (60, 'title6', 'content6', '2019-01-01 00:00:00', 20),
        (70, 'title2', 'content2', '2019-01-01 00:00:00', 30),
        (80, 'title2', 'content2', '2019-01-01 00:00:00', 30);