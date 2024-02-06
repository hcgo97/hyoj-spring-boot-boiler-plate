# hyoj-spring-boot-boiler-plate-3.x
HyoJ's 스프링부트 템플릿 3.x 버전

## 2. 서버 구성
## 2-1. 개발 환경
- **OS**: Alpine Linux 3.16
- **Language**: openjdk 17
- **Framework**: SpringBoot 3.2.2
- **build & lib management**: Gradle 8.5
- **DB**: MySQL 8.0.34, H2 In-memory(Test), Redis 7.0.5
- **ORM**: Spring Boot JPA
- **Etc**: lombok, flyway, okhttp3, apache commons

## 2-2. Host
- http://localhost:8080

## 3. 애플리케이션 실행 방법
- **실행 전 `gradle`, `openjdk-17`, `docker`, `docker-compose` 가 설치되어 있어야 합니다.**

### 3-1. 프로젝트 루트 경로에 `.env` 생성
```env
# 활성화 할 프로파일
SPRING_PROFILE=local

# MYSQL 환경변수
MYSQL_PASSWORD=yourpassword

# REDIS 환경변수
REDIS_PASSWORD=yourpassword
```

### 3-2. build 하기
```shell
make build
```

### 3-3. 서버 띄우기
```shell
make run
```

### 3-4. 서버 종료하기
```shell
make down
```

### 3-5. Health Check URL
```bash
curl -X GET http://localhost:8080/actuator/health
```
<br></br>

## 4. [데이터베이스 테이블 구조]

```sql
# 유저 정보 테이블
CREATE TABLE users
(
    id         bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email      varchar(50) NOT NULL UNIQUE COMMENT '이메일(로그인 ID)',
    password   varchar(50) NOT NULL COMMENT '비밀번호(bcrypt)',
    created_at datetime COMMENT '생성일자',
    updated_at datetime COMMENT '수정일자',
    is_deleted tinyint(1) DEFAULT false COMMENT '삭제여부 true/false',
    deleted_at datetime   DEFAULT NULL COMMENT '삭제일자'
) COMMENT = '유저 정보';

# 게시글 정보 테이블
CREATE TABLE posts
(
    id         bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id    bigint      NOT NULL COMMENT 'users PK',
    title      varchar(20) NOT NULL COMMENT '게시글 제목',
    contents   text        NOT NULL COMMENT '게시글 내용',
    created_at datetime COMMENT '생성일자',
    updated_at datetime COMMENT '수정일자',
    is_deleted tinyint(1) DEFAULT false COMMENT '삭제여부 true/false',
    deleted_at datetime   DEFAULT NULL COMMENT '삭제일자',
    FOREIGN KEY (user_id) REFERENCES users(id)
) COMMENT = '게시글 정보';
```
<br></br>

## 5. 구현 방법
### 5-1. Health Check URL 구현
- Spring Actuator 를 활용하여 `/actuator/health` 엔드포인트를 통해 서버 Health Check 기능을 구현하였습니다.
### 5-2. 통일된 API 응답 구조 활용
- 각 API 마다 일관된 응답을 제공하기 위해 기본 응답 구조를 정의하였습니다. 
- 각 요청마다 트랜잭션 ID를 할당하여 추후 로그 추적을 용이하게 하였습니다.
### 5-3. 파라미터 유효성 검사
- Spring Boot Validation 을 활용하여 파라미터의 유효성을 검사합니다. 
- 유효성 검사 실패 시 Exception Handler 에서 MethodArgumentNotValidException 을 catch 하여 적절한 에러 메시지를 응답에 포함시킵니다.
### 5-4. 리소스 삭제 시 Soft Delete 처리
DB 상의 데이터를 영구적으로 삭제하는 Hard Delete 대신 Soft Delete 를 적용하여 추후 데이터 복구가 가능하도록 설계하였습니다.
리소스 삭제 시 해당 리소스 테이블의 is_deleted 컬럼을 true로 변경하고, deleted_at 컬럼을 삭제 시각으로 업데이트하여 삭제 여부를 구분할 수 있도록 하였습니다.
### 5-5. 삭제 API 성공 시의 HTTP 상태 코드를 204 가 아닌 200 으로 처리
- 204 NO_CONTENT 상태 코드는 응답 본문이 없어 출력되지 않기 때문에, 모든 API의 일관성 있는 응답 구조를 위해 200 OK로 처리하였습니다.

<br></br>

## 6. API 명세
- [Postman Collection 다운로드](https://drive.google.com/file/d/1CvQqLTsTyZfUx_cS1Loe7IF9dOIuITK2/view?usp=drive_link)
## 6-1. 회원가입 API
### URL
- `/api/v1/users/join`

### Method
- `POST`

### Request Header
- `Content-Type`: `application/json`

### Request Body
- `email`: 이메일, string
- `password`: 패스워드, string

### Response Body
- `txid`: 요청 트랜잭션 id, string
- `status`: http 상태 코드, integer
- `message`: 응답 메시지, string
- `data`: 응답 데이터 객체, object
    - `id`: 생성된 유저 PK, long
    - `email`: 생성된 유저 이메일, string

### Example
- **Request Body**
```json
{
    "email": "test@abc.com",
    "password": "test1234"
}
```
- **Response Body**
```json
{
    "txid": "558ca027-1248-4a63-935f-b3523153812a",
    "status": 201,
    "message": "정상적으로 처리되었습니다.",
    "data": {
        "id": 1,
        "email": "test@abc.com"
    }
}
```
- **curl**
```bash
curl --location 'https://api-wanted-internship.hyoj.me/api/v1/users/join' \
--header 'Content-Type: application/json' \
--data '{
    "email": "test@abc.com",
    "password": "test1234"
}'
```
<br></br>

## 6-2. 로그인 API
### URL
- `/api/v1/users/login`

### Method
- `POST`

### Request Header
- `Content-Type`: `application/json`

### Request Body
- `email`: 이메일, string
- `password`: 패스워드, string

### Response Body
- `txid`: 요청 트랜잭션 id, string
- `status`: http 상태 코드, integer
- `message`: 응답 메시지, string
- `data`: 응답 데이터 객체, object
    - `accessToken`: 유저 액세스 토큰, string
    - `userInfo`: 유저 정보 객체, object
        - `id`: 로그인한 유저 PK, long
        - `email`: 로그인한 유저 이메일, string

### Example
- **Request Body**
```json
{
    "email": "test@abc.com",
    "password": "test1234"
}
```
- **Response Body**
```json
{
    "txid": "46fac5d6-7279-4bf2-9140-8d35dc160e24",
    "status": 200,
    "message": "정상적으로 처리되었습니다.",
    "data": {
        "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwiZW1haWwiOiJ0ZXN0QGFiYy5jb20iLCJpYXQiOjE2OTE5Mjg5MDQsImV4cCI6MTY5MTkyOTUwNH0.gKkxmJkx_ExqzsYYhrCEm7be36W9ZeDqWPIoKTrGVwJTBcHZ60KoshGe5HaY6InuuMivUMPg5KkbdjVGeUTruw",
        "userInfo": {
            "id": 1,
            "email": "test@abc.com"
        }
    }
}
```
- **curl**
```bash
curl --location 'https://api-wanted-internship.hyoj.me/api/v1/users/login' \
--header 'Content-Type: application/json' \
--data '{
    "email": "test@abc.com",
    "password": "test1234"
}'
```
<br></br>

## 6-3. 게시글 작성 API
### URL
- `/api/v1/posts`

### Method
- `POST`

### Request Header
- `Content-Type`: `application/json`
- `Authorization`: Bearer Token, string

### Request Body
- `title`: 게시글 제목, string
- `contents`: 게시글 내용, string

### Response Body
- `txid`: 요청 트랜잭션 id, string
- `status`: http 상태 코드, integer
- `message`: 응답 메시지, string
- `data`: 응답 데이터 객체, object
    - `id`: 생성된 게시글 PK, long
    - `author`: 게시글 작성자, string
    - `createdAt`: 생성 시각(yyyy-MM-dd HH-mm-ss), string
    - `updatedAt`: 수정 시각(yyyy-MM-dd HH-mm-ss), string
    - `isUpdated`: 수정 여부, boolean
    - `title`: 생성된 게시글 제목, string
    - `contents`: 생성된 게시글 내용, string

### Example
- **Request Body**
```json
{
    "title": "test title",
    "contents": "test contents~~~"
}
```
- **Response Body**
```json
{
    "txid": "bd8ba5a6-bb13-4329-9edf-f45a15da1554",
    "status": 201,
    "message": "정상적으로 처리되었습니다.",
    "data": {
        "id": 72,
        "author": "test@abc.com",
        "createdAt": "2023-08-14 17:22:48",
        "updatedAt": "2023-08-14 17:22:48",
        "isUpdated": false,
        "title": "test title",
        "contents": "test contents~~~"
    }
}
```
- **curl**
```bash
curl --location 'https://api-wanted-internship.hyoj.me/api/v1/posts' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer xxxx' \
--data '{
    "title": "test title",
    "contents": "test contents~~~"
}'
```
<br></br>

## 6-4. 게시글 목록 조회 API
### URL
- `/api/v1/posts`

### Method
- `GET`

### Request Header
- `Content-Type`: `application/json`
- `Authorization`: Bearer Token, string

### Request Parameters
- `page`: 페이지 번호 (1부터 시작), integer
- `size`: 페이지 당 표시할 게시글 수, integer

### Response Body
- `txid`: 요청 트랜잭션 id, string
- `status`: http 상태 코드, integer
- `message`: 응답 메시지, string
- `data`: 응답 데이터 객체, object
    - `content`: 조회된 게시글 목록, array
        - `id`: 게시글 PK, long
        - `author`: 게시글 작성자, string
        - `createdAt`: 생성 시각(yyyy-MM-dd HH-mm-ss), string
        - `isUpdated`: 수정 여부, boolean
        - `title`: 게시글 제목, string
    - `pageable`: 페이징 객체, object

### Example
- **Response Body**
```json
{
    "txid": "dbf43fd4-1b36-4f2c-ac5a-e1f19546d318",
    "status": 200,
    "message": "정상적으로 처리되었습니다.",
    "data": {
        "content": [
            {
                "id": 72,
                "author": "test@abc.com",
                "createdAt": "2023-08-14 17:22:48",
                "isUpdated": false,
                "title": "test title"
            }
        ],
        "pageable": {
            "sort": {
                "empty": true,
                "sorted": false,
                "unsorted": true
            },
            "offset": 0,
            "pageNumber": 0,
            "pageSize": 1,
            "paged": true,
            "unpaged": false
        },
        "last": false,
        "totalPages": 10,
        "totalElements": 10,
        "size": 1,
        "number": 0,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "first": true,
        "numberOfElements": 1,
        "empty": false
    }
}
```
- **curl**
```bash
curl --location 'https://api-wanted-internship.hyoj.me/api/v1/posts?page=1&size=1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer xxxx'
```
<br></br>

## 6-5. 게시글 상세 조회 API
### URL
- `/api/v1/posts/{postId}`

### Method
- `GET`

### Path Variables
- `postId`: 조회할 게시글 PK, long

### Request Header
- `Content-Type`: `application/json`
- `Authorization`: Bearer Token, string

### Response Body
- `txid`: 요청 트랜잭션 id, string
- `status`: http 상태 코드, integer
- `message`: 응답 메시지, string
- `data`: 응답 데이터 객체, object
    - `id`: 게시글 PK, long
    - `author`: 작성자, string
    - `createdAt`: 생성 시각(yyyy-MM-dd HH-mm-ss), string
    - `updatedAt`: 수정 시각(yyyy-MM-dd HH-mm-ss), string
    - `isUpdated`: 수정 여부, boolean
    - `title`: 게시글 제목, string
    - `contents`: 게시글 내용, string

### Example
- **Response Body**
```json
{
    "txid": "269c279e-1822-4733-ba68-5e26c2150ba9",
    "status": 200,
    "message": "정상적으로 처리되었습니다.",
    "data": {
        "id": 72,
        "author": "test@abc.com",
        "createdAt": "2023-08-14 17:22:48",
        "updatedAt": "2023-08-14 17:22:48",
        "isUpdated": false,
        "title": "test title",
        "contents": "test contents~~~"
    }
}
```
- **curl**
```bash
curl --location 'https://api-wanted-internship.hyoj.me/api/v1/posts/72' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer xxxx'
```
<br></br>

## 6-6. 게시글 수정 API
### URL
- `/api/v1/posts/{postId}`

### Method
- `PUT`

### Path Variables
- `postId`: 수정할 게시글 PK, long

### Request Header
- `Content-Type`: `application/json`
- `Authorization`: Bearer Token, string

### Request Body
- `title`: 게시글 제목, string
- `contents`: 게시글 내용, string

### Response Body
- `txid`: 요청 트랜잭션 id, string
- `status`: http 상태 코드, integer
- `message`: 응답 메시지, string
- `data`: 응답 데이터 객체, object
    - `id`: 수정된 게시글 PK, long
    - `author`: 작성자, string
    - `createdAt`: 생성 시각(yyyy-MM-dd HH-mm-ss), string
    - `updatedAt`: 수정 시각(yyyy-MM-dd HH-mm-ss), string
    - `isUpdated`: 수정 여부, boolean
    - `title`: 수정된 게시글 제목, string
    - `contents`: 수정된 게시글 내용, string

### Example
- **Request Body**
```json
{
    "title": "edited title",
    "contents": "edited contents~~~"
}
```
- **Response Body**
```json
{
    "txid": "bd8ba5a6-bb13-4329-9edf-f45a15da1554",
    "status": 200,
    "message": "정상적으로 처리되었습니다.",
    "data": {
        "id": 72,
        "author": "test@abc.com",
        "createdAt": "2023-08-14 17:22:48",
        "updatedAt": "2023-08-15 02:28:51",
        "isUpdated": true,
        "title": "edited title",
        "contents": "edited contents~~~"
    }
}
```
- **curl**
```bash
curl --location --request PUT 'https://api-wanted-internship.hyoj.me/api/v1/posts/72' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer xxxx' \
--data '{
    "title": "edited title",
    "contents": "edited contents~~~"
}'
```
<br></br>

## 6-7. 게시글 삭제 API
### URL
- `/api/v1/posts/{postId}`

### Method
- `DELETE`

### Path Variables
- `postId`: 삭제할 게시글 PK, long

### Request Header
- `Content-Type`: `application/json`
- `Authorization`: Bearer Token, string

### Response Body
- `txid`: 요청 트랜잭션 id, string
- `status`: http 상태 코드, integer
- `message`: 응답 메시지, string
- `data`: 응답 데이터 객체, object
    - `id`: 삭제된 게시글 PK, long
    - `author`: 작성자, string
    - `createdAt`: 생성 시각(yyyy-MM-dd HH-mm-ss), string
    - `updatedAt`: 수정 시각(yyyy-MM-dd HH-mm-ss), string
    - `isUpdated`: 수정 여부, boolean
    - `title`: 삭제된 게시글 제목, string
    - `contents`: 삭제된 게시글 내용, string

### Example
- **Response Body**
```json
{
    "txid": "bd8ba5a6-bb13-4329-9edf-f45a15da1554",
    "status": 200,
    "message": "정상적으로 처리되었습니다.",
    "data": {
        "id": 72,
        "author": "test@abc.com",
        "createdAt": "2023-08-14 17:22:48",
        "updatedAt": "2023-08-15 02:28:51",
        "isUpdated": true,
        "title": "edited title",
        "contents": "edited contents~~~"
    }
}
```
- **curl**
```bash
curl --location --request DELETE 'https://api-wanted-internship.hyoj.me/api/v1/posts/72' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer xxxx'
```
<br></br>

## 6-8. 에러 정의
|에러 코드|에러 이름|상태 코드|에러 설명|
|----|----|----|----|
|E4000|CLIENT_ERROR|400|잘못된 요청 파라미터|
|**E4010**|**UNAUTHORIZED**|**401**|**로그인 정보가 없음**|
|E4011|TOKEN_NOT_FOUND|401|토큰을 찾을 수 없음|
|E4012|TOKEN_INVALID|401|유효하지 않은 토큰|
|E4013|TOKEN_DATE_EXPIRED|401|토큰이 만료됨|
|**E4014**|**LOGIN_FAILED**|**401**|**로그인 정보가 잘못됨**|
|E4030|FORBIDDEN|403|권한 없음|
|**E4031**|**NOT_MY_POST**|**403**|**해당 게시글에 대한 작업 권한 없음**|
|E4040|NOT_FOUND|404|존재하지 않는 데이터 또는 경로|
|**E4041**|**USER_NOT_FOUND**|**404**|**존재하지 않는 유저**|
|**E4042**|**POST_NOT_FOUND**|**404**|**존재하지 않는 게시글**|
|**E4090**|**ALREADY_SIGNED_UP**|**409**|**이미 가입된 이메일**|
|**E4220**|**INVALID_FORMAT**|**422**|**파라미터 유효성 검사 실패**|
|E9000|INTERNAL_SERVER_ERROR|500|서버 에러|
|E9100|DATABASE_ACCESS_ERROR|500|DB 에러|
|E9200|TOKEN_CREATED_FAILED|500|토큰 생성 중 에러|

### Response Body
- `txid`: 요청 트랜잭션 id, string
- `status`: http 상태 코드, integer
- `message`: 응답 메시지, string
- `error`: 에러 데이터 객체, object
  - `code`: 에러 코드, string
  - `description`: 에러 이름(설명), string

### Example
- **Response Body**
```json
{
    "txid": "054871bd-32a1-4b4a-916e-fac786077d8a",
    "status": 401,
    "message": "로그인 정보가 일치하지 않습니다.",
    "error": {
        "code": "E4014",
        "description": "LOGIN_FAILED"
    }
}
```
<br></br>
