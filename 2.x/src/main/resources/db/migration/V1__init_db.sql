drop table if exists users;
drop table if exists vendor_request_histories;

CREATE TABLE `users`
(
    `id`           bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id`      varchar(20)  NOT NULL COMMENT '로그인 ID',
    `password`     varchar(255) NOT NULL COMMENT '패스워드',
    `role_code`    varchar(10) DEFAULT 'general' COMMENT '유저 권한',
    `nick_name`    varchar(20) COMMENT '닉네임',
    `phone_number` varchar(20) COMMENT '휴대폰 번호',
    `email`        varchar(255) COMMENT '이메일',
    `login_count`  int         DEFAULT '0' COMMENT '로그인 횟수',
    `created_at`   datetime,
    `updated_at`   datetime,
    `is_deleted`   varchar(1)  DEFAULT 'N' COMMENT '삭제여부 Y/N',
    `deleted_at`   datetime
) ENGINE = InnoDB, COMMENT = '유저 정보';

CREATE TABLE `vendor_request_histories`
(
    `id`                   bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `request_id`           varchar(40),
    `end_point`            varchar(1024),
    `request_header`       longtext,
    `request_method`       varchar(10),
    `request_body`         longtext,
    `response_header`      longtext,
    `response_status_code` varchar(10),
    `response_body`        longtext,
    `created_at`           datetime,
    `updated_at`           datetime
) ENGINE = InnoDB, COMMENT = '외부 API 호출 내역';


