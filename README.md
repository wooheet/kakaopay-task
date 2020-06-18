# Rest API 기반 쿠폰시스템

## Contents
* [Specifications](#chapter-1)
* [How to run](#chapter-2)
* [Requirement](#chapter-3) 
* [Strategy](#chapter-4)
* [Domain](#chapter-5)
* [Entity](#chapter-6)
* [Explanation of REST](#chapter-7)
* [Api Feature list](#chapter-8)
* [Api Endpoint](#chapter-9)
* [Performance Test](#chapter-10)

### <a name="chapter-1"></a>Specifications 
````
 OpenJDK11
 Spring Boot 2.3.1.RELEASE
 Spring Data Jpa
 Spring Data Jdbc(Batch Update)
 Swagger2
 Domain Driven Design
 CQRS Pettern
````

### <a name="chapter-2"></a>How to Run
```
1. 실행
./gradlew bootrun

# Using IntelliJ
1. Sync gradle
2. Run Application

2. Test 
./gradlew test
```

### <a name="chapter-3"></a>Requirement 
````
- 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관
- 생성된 쿠폰중 하나를 사용자에게 지급
- 사용자에게 지급된 쿠폰을 조회
- 지급된 쿠폰중 하나를 사용 (쿠폰 재사용은 불가)
- 지급된 쿠폰중 하나를 사용 취소 (취소된 쿠폰 재사용 가능)
- 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회
- 만료 3일전 쿠폰을 조회하여 알림 
- JWT 웹 토큰을 이용한 회원가입, 로그인, API 인증 구현
- 회원가입 구현 ( 패스워드 안전한 방법으로 저장 )
- signup, signin 구현   
- API 호출 시 발급 받은 Token을 이용해 인증처리 
- 트래픽(성능)을 고려한 설계  
- 10만개 이상 벌크 Insert 구현
- 성능 테스트 결과서 만들기 
````

### <a name="chapter-4"></a>Strategy 
```` 
- DDD(Domain Driven Design) 적용
- Coupon, CouponIssue 도메인으로 분리
- User, User Role 도메인으로 분리 / JPA fetch join 적용
- API 기능명세에서 제시한 기능 구현
- 만료 3일전 쿠폰 조회

- 회원가입 구현 / Spring security ( 패스워드 안전한 방법으로 저장 ) 
- 로그인 구현 ( 로그인 후 JWT 토큰 제공 ) 
- Spring security Jwt Authentication 적용 / API 호출 시 token 값 유효성 여부 검증 

- 쿠폰 생성 시 대용량 Insert를 위해 jdbc batch update를 구현
- 10만개 이상 벌크 Insert 구현하기
- 10만개 csv 파일을 읽고 DB 저장(jdbc batch update)구현
- 성능 테스트 결과서 : nGrinder를 이용해 성능 테스트 및 리포트 출력 
````

### <a name="chapter-5"></a>Domain 
```
쿠폰(Coupon) 
   쿠폰번호
   쿠폰상태
   만료일
   생성일
   업데이트일

쿠폰발급(CouponIssue)
   유저아이디
   사용일
   발급일

유저(User)
   유저아이디
   패스워드

유저롤(User_Role)
   롤아이디
   유저아이디
   롤
```

## <a name="chapter-6"></a>Entity
```
쿠폰(Coupon) 
   쿠폰번호
   쿠폰상태
   유저아이디
   만료일
   생성일   
   사용일
   발급일

유저(User)
   유저아이디
   패스워드

유저롤(User_Role)
   롤아이디
   유저아이디
   롤   
```

### <a name="chapter-7"></a>Explanation of REST 
|HTTP Method|Usage|
|:---|:---|
|GET   |Receive a read-only data      |
|PUT   |Overwrite an existing resource|
|POST  |Creates a new resource        |
|DELETE|Deletes the given resource    |

### <a name="chapter-8"></a>Api Feature list 
```
- 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관
- 회원가입
- 로그인
- 쿠폰 코드를 이용해 쿠폰 정보를 조회
- 생성된 쿠폰중 하나를 사용자에게 지급
- 사용자에게 지급된 쿠폰을 조회
- 지급된 쿠폰중 하나를 사용  (쿠폰 재사용은 불가) 
- 지급된 쿠폰중 하나를 사용 취소 (취소된 쿠폰 재사용 가능)
- 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회
- 만료 N일전 쿠폰을 조회하여 알림
- CSV 파일을 읽고 10만개 쿠폰 정보 Insert
- 토큰 인증하여 User정보 조
``` 

### <a name="chapter-9"></a>Api Endpoint
```
API 실행 && 테스트 절차
1. 회원가입을 합니다 
2. 로그인 후 인증 토큰을 받습니다
3. Header userId, Authorization Header Bearer Token 값을 넣고 각 Coupon API를 호출합니다

EndPoint : /v1/signup
Method : POST 
Description : 회원가입
Return value:
{
    "success": true,
    "code": 0,
    "msg": "성공하였습니다.",
    "data": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZWR5QHIydi5pbyIsInJvbGVzIjpbeyJpZCI6MSwicm9sZSI6IlVTRVIifV0sImlhdCI6MTU5MjUxMDMzOSwiZXhwIjoxNTkyNTk2NzM5fQ.11Dnk3US5ZHuCmbae4qsWIJJyTF81_kFrgkGW3AcO8o"
}

Payload Example (required parameters)
- http://{CouponApiServer}/v1/signup?email=tedy@r2v.io&password=1

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| email     | @QueryParam  |                                                   |
|-----------|--------------|---------------------------------------------------|
| password  | @QueryParam  |                                                   |
|-----------|--------------|---------------------------------------------------|

----------------------------------------------------------------------------------------------------

EndPoint : /v1/signin
Method : POST
Description : 로그인
{
    "success": true,
    "code": 0,
    "msg": "성공하였습니다.",
    "data": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZWR5QHIydi5pbyIsInJvbGVzIjpbeyJpZCI6MSwicm9sZSI6IlVTRVIifV0sImlhdCI6MTU5MjUxMDMzOSwiZXhwIjoxNTkyNTk2NzM5fQ.11Dnk3US5ZHuCmbae4qsWIJJyTF81_kFrgkGW3AcO8o"
}

Payload Example (required parameters)
- http://{CouponApiServer}/v1/signin?email=tedy@r2v.io&password=1

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| email     | @QueryParam  |                                                   |
|-----------|--------------|---------------------------------------------------|
| password  | @QueryParam  |                                                   |
|-----------|--------------|---------------------------------------------------|


----------------------------------------------------------------------------------------------------

EndPoint : /v1/coupons
Method : POST 
Description : 쿠폰 N개 생성
Return value: HTTP status 201 (Created) 
{
    "success": true,
    "code": 0,
    "msg": "성공하였습니다.",
    "list": [
        "e1u0a0W0Q0b0cG3H"
    ]
}
Payload Example (required parameters)
- http://{CouponApiServer}/v1/coupons?size=1

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| size      | @QueryParam  | coupon size                                       |
|-----------|--------------|---------------------------------------------------|


----------------------------------------------------------------------------------------------------

EndPoint : /v1/coupons/generate
Method : POST 
Description : 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관
              (쿠폰 코드 1씩 자동증가 : 초기값 1) 
              만료일을 테스트 하기 위해 10000단위 레코드 별로 만료일 +1 day 증가
Return value: HTTP status 201 (Created) 
Payload Example (required parameters)
- http://{CouponApiServer}/v1/coupons?size=100000

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| size      | Long         | coupon size                                       |
|-----------|--------------|---------------------------------------------------|

----------------------------------------------------------------------------------------------------

EndPoint : /v1/coupons/generate/csv
Method : POST 
Description : CSV 파일을 읽고 10만개 쿠폰 정보 Insert
              (쿠폰 코드 1씩 자동증가 : 초기값 1) 
Return value: HTTP status 201 (Created) 

----------------------------------------------------------------------------------------------------

EndPoint : /v1/coupons/use/{useId}
Method : PUT 
Description : 생성된 쿠폰중 하나를 사용자에게 지급 
              사용자에게 지급한 쿠폰을 사용
              사용된 쿠폰을 사용 취소
Return value: HTTP status 200 (OK), 404 (NOT_FOUND)

Payload Example (required parameters)
- http://{CouponApiServer}/v1/coupons/use/1?couponNum=g071v010m0L0tX3q

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| id        | @PathParam   | Coupon id                                         |
|-----------|--------------|---------------------------------------------------|

----------------------------------------------------------------------------------------------------

EndPoint : /v1/coupons
Method : GET
Description : 사용자에게 지급된 쿠폰을 조회
Return value: 
{
    "couponNum": "g071v010m0L0tX3q",
    "status": "USED",
    "expirationAt": "2020-06-19T01:50:48"
}

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| userId    | @QueryParam  |                          |
|-----------|--------------|---------------------------------------------------|
| pageable  | @Pageable    | Pageable Object(size, page)                       |
|-----------|--------------|---------------------------------------------------|

----------------------------------------------------------------------------------------------------

EndPoint : /v1/coupons/duedate-today
Method : GET
Description : 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회
Return value: HTTP status 200 (OK) 

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| pageable  | @Pageable    | Pageable Object(size, page)                       |
|-----------|--------------|---------------------------------------------------|

----------------------------------------------------------------------------------------------------

EndPoint : /v1/coupons/notify-expire
Method : GET
Description : 만료 day일전 쿠폰을 조회하여 logger info 알림
Return value: HTTP status 200 (OK) 

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| day       | @QueryParam  | search expired plus day                           |
|-----------|--------------|---------------------------------------------------|
