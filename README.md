# hyoj-spring-boot-boiler-plate
## HyoJ's 스프링부트 템플릿

## 환경
* language: openJDK 11.0.2
* Framework: SpringBoot 2.7.3
* build & lib management: Gradle 7.x
* DB: MySQL 8.x
* Cache: Redis 5.x
* ORM: JPA, QueryDSL
* Etc: Lombok, JWT

## 테스트
### build
```bash
./gradlew bootJar
```

### run
```bash
# local 환경
java -Dspring.profiles.active=local -jar ./build/libs/my-spring-boot-boiler-plate-*.jar
```

## 서버 정보
### port
- local listen port: 8081

### API prefix
* ${endpoint}/api/v1

### Health Check URL
* /ping

### 디렉토리, 파일구성
* server_dir: /app/hyoj-server
* jar: ${admin_home}/lib/my-spring-boot-boiler-plate-*.jar
* log: ${admin_home}/logs/app.log
