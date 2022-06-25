# 🐋 Triple 테스트

- 시작 2022. 6. 18
- 종료 2022. 6. 25 (자정)

## 🦐 Tools

<center>

![](https://img.shields.io/badge/JAVA-FF7800?style=for-the-badge&logo=JAVA&logoColor=white)
![](https://img.shields.io/badge/SPRINGBOOT-6DB33F?style=for-the-badge&logo=SPRINGBOOT&logoColor=white)
![](https://img.shields.io/badge/JPA-34E27A?style=for-the-badge&logo=JPA&logoColor=white)

![](https://img.shields.io/badge/Mysql-4479A1?style=for-the-badge&logo=Mysql&logoColor=white)
![](https://img.shields.io/badge/GRADLE-02303A?style=for-the-badge&logo=GRADLE&logoColor=white)
![](https://img.shields.io/badge/JUNIT5-25A162?style=for-the-badge&logo=JUNIT5&logoColor=white)

</center>


# 실행 방법

## 🦐 1. 테이블을 별도 입력할 필요가 없습니다.

`TripleApplication.java`를 실행하면 JPA DDL CREATE 가 작동합니다.

## 🦐 2. application.properties 작성해 주세요.

```properties

spring.datasource.url=jdbc:mysql://localhost:3306/triple?useUnicode=true&characterEncoding=utf8
spring.datasource.username= <username>
spring.datasource.password= <password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.format_sql=true

// JPA 자동 생성
spring.jpa.hibernate.ddl-auto=update
```

## 🦐 3. Test Code로 결과를 확인하세요.

`API Tester` 없이도 테스트 코드로 DB(mysql)결과를 직접 확인할 수 있습니다.


# 🐋 2. API Request / Response

<table>
<tr>
<td> API </td> <td> Request </td> <td> Response </td>
</tr>
<tr>
<td> /evnets  </td>
<td>

```json
{
"type": "REVIEW",
"action": "ADD", /* "MOD", "DELETE" */
"reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
"content": "좋아요!",
"attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
"userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
"placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}
```
</td>

<td>
 Boolean
</td>
</tr>

<tr>

<td> /user  </td>
<td>

```
http://localhost:8080/user?user={user_UUID}

    ex) http://localhost:8080/user?name=3ede0ef2-92b7-4817-a5f3-0c575361f745
```
</td>
<td>

```json
{
  "point": 1
}
```
</td>

</tr>
</table>



# 🐋 Table

![image](https://user-images.githubusercontent.com/65659478/175563609-1ef29a15-eac3-4f77-b5fb-787044e08019.png)


# MARKS

- 포인트 증감이 있을 때마다 이력이 남아야 합니다. ⭕
  - PointLog Table에 증/감 이력이 기록됩니다.
  

- 사용자마다 현재 시점의 포인트 총점을 조회하거나 계산할 수 있어야 합니다. ⭕
  - `/user?user=<user_UUID>`를 통해 확인할 수 있습니다.
  

- 포인트 부여 API 구현에 필요한 SQL 수행 시, 전체 테이블 스캔이 일어나지 않는 인덱스가 필요합니다. ⭕
    - `@Index` 어노테이션을 사용해 `full-scan` 빈도를 줄였습니다.


- 리뷰를 작성했다가 삭제하면 해당 리뷰로 부여한 내용 점수와 보너스 점수는 회수합니다. ⭕
- 리뷰를 수정하면 수정한 내용에 맞는 내용 점수를 계산하여 점수를 부여하거나 회수합니다. ⭕


- 사용자 입장에서 본 '첫 리뷰'일 때 보너스 점수를 부여합니다. ⭕
