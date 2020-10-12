# pay-service
## 필요사항
1. jdk8 이상 필요.
2. maven 필요. (Apache Maven 3.6.3 에서 진행함)


## 빌드 및 실행
```
1. git clone https://github.com/winikim/pay-service.git
2. cd pay-service
3. mvn install
4. mvn package
6. cd target
7. java -jar pay-0.0.1-SNAPSHOT.jar
8. h2 console 경로            
-> http://127.0.0.1:8080/h2
-> jdbcURL:jdbc:h2:mem:pay
-> id: sa, password: 없음
```
-----------------------------------------

## 빌드 된 파일 다운로드
URL: https://drive.google.com/file/d/1bOJ2t5fhg7_3h68X46bSlPqy8L9wpT_I/view?usp=sharing                     
다운로드 후 해당 파일을 실행
실행명령어: java -jar pay-0.0.1-SNAPSHOT.jar

-----------------------------------------
## 핵심 문제해결 전략
```
1. Spring boot 사용
- 빠르게 구성하고 실행할 수 있도록 Spring boot starter 사용

2. jpa 사용
- 객체지향적으로 요구사항을 해결하기 위하여 사용

3. 테스트 용이성을 위하여 H2 Database 사용

4. Layer 별로 단위 테스트 케이스 작성 (infrastructure layer 제외)
- presentation, application, domain에 대한 단위 테스트 코드 작성    
- presentation(@Webmvc사용), application(mock사용), domain(none)
- infrastructure는 custom한 기능 없이 jpa 기능을 사용 했으므로 제외 시킴.
     
5. Lombok    
- 반복 되는 코드 사용을 줄이기 위하여 사용
    
6. JPA PESSIMISTIC_WRITE Lock 사용      
- 다량 트래픽으로 인하여, 뿌린 금액 받기 기능 동시에 수행 될 때 금액 차감이 중복으로 발생하지 않도록 함.
- 한명의 사용자가 동시에 연속 된 요청을 통하여 뿌린 금액 받기 기능을 사용할 때 중복으로 금액 받기가 되지 않도록함.

7. API 요청 시 식별 값들의 일관성을 유지하기 위하여 토큰 값을 Header로 전달 받도록 함.

```
------------------------

## 요구사항에 대한 API
#### 1.뿌리기 API
```
[REQUEST]

POST /amount-to-be-distributed HTTP/1.1
Host: 127.0.0.1:8080
X-ROOM-ID: room
X-USER-ID: 1
Content-Type: application/json

{
    "amount": 20000,
    "numberOfPeople": 5
}

-------------------------------------

[RESPONSE]
{
    "token": "oJa"
}

```
#### 2. 받기 API
```
[REQUEST]

GET /distributed-amount HTTP/1.1
Host: 127.0.0.1:8080
X-ROOM-ID: room
X-USER-ID: 1123
X-TOKEN-ID: oJa

---------------------------------------------------------------

[RESPONSE]
{
    "amount": 4000
}
```

#### 3. 조회 API
```
[REQUEST]

GET /amount-to-be-distributed HTTP/1.1
Host: 127.0.0.1:8080
X-ROOM-ID: room
X-USER-ID: 1
X-TOKEN-ID: oJa

----------------------------------------------------------------

[RESPONSE]

{
    "dateTimeOfDistributedAmount": "2020-10-12T14:37:58",
    "amountToBeDistributed": 20000,
    "completedToDistributedAmount": 4000,
    "completedToReceives": [
        {
            "amount": 4000,
            "userId": 1123
        }
    ]
}
```
