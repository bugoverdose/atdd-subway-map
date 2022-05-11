TRUNCATE TABLE station;
TRUNCATE TABLE line;
TRUNCATE TABLE section;

ALTER TABLE station ALTER COLUMN id RESTART WITH 1;
ALTER TABLE line ALTER COLUMN id RESTART WITH 1;
ALTER TABLE section ALTER COLUMN id RESTART WITH 1;

INSERT INTO station(id, name)
VALUES (1, '강남역'),
       (2, '선릉역'),
       (3, '잠실역'),
       (4, '청계산입구역'),
       (5, '한티역');

INSERT INTO line(id, name, color)
VALUES (1, '1호선', '노란색'),
       (2, '신분당선', '빨간색'),
       (3, '2호선', '초록색');

INSERT INTO section(id, line_id, up_station_id, down_station_id, distance)
VALUES (1, 1, 3, 1, 5),
       (2, 2, 2, 3, 5),
       (3, 2, 1, 2, 5),
       (4, 3, 1, 3, 7);