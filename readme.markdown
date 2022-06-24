# Triple 테스트

시작 2022. 6. 18

종료 2022. 6. 25 (자정)

## 🦐 Tools

<center>

![](https://img.shields.io/badge/JAVA-FF7800?style=for-the-badge&logo=JAVA&logoColor=white)
![](https://img.shields.io/badge/SPRINGBOOT-6DB33F?style=for-the-badge&logo=SPRINGBOOT&logoColor=white)
![](https://img.shields.io/badge/JPA-34E27A?style=for-the-badge&logo=JPA&logoColor=white)

![](https://img.shields.io/badge/Mysql-4479A1?style=for-the-badge&logo=Mysql&logoColor=white)
![](https://img.shields.io/badge/GRADLE-02303A?style=for-the-badge&logo=GRADLE&logoColor=white)
![](https://img.shields.io/badge/JUNIT5-25A162?style=for-the-badge&logo=JUNIT5&logoColor=white)

</center>

## 🦐 API Request / Response

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


## 🦐 application.properties

```properties

spring.datasource.url=jdbc:mysql://localhost:3306/triple?useUnicode=true&characterEncoding=utf8
spring.datasource.username= <username>
spring.datasource.password= <password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.format_sql=true

// JPA 자동 생성
spring.jpa.hibernate.ddl-auto=update
```

## Table

![image](https://user-images.githubusercontent.com/65659478/175563609-1ef29a15-eac3-4f77-b5fb-787044e08019.png)
