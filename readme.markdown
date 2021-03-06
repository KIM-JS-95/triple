# π Triple νμ€νΈ

- μμ 2022. 6. 18
- μ’λ£ 2022. 6. 25 (μμ )

## π¦ Tools

<center>

![](https://img.shields.io/badge/JAVA-FF7800?style=for-the-badge&logo=JAVA&logoColor=white)
![](https://img.shields.io/badge/SPRINGBOOT-6DB33F?style=for-the-badge&logo=SPRINGBOOT&logoColor=white)
![](https://img.shields.io/badge/JPA-34E27A?style=for-the-badge&logo=JPA&logoColor=white)

![](https://img.shields.io/badge/Mysql-4479A1?style=for-the-badge&logo=Mysql&logoColor=white)
![](https://img.shields.io/badge/GRADLE-02303A?style=for-the-badge&logo=GRADLE&logoColor=white)
![](https://img.shields.io/badge/JUNIT5-25A162?style=for-the-badge&logo=JUNIT5&logoColor=white)

</center>


# μ€ν λ°©λ²

## π¦ 1. νμ΄λΈμ λ³λ μλ ₯ν  νμκ° μμ΅λλ€.

`TripleApplication.java`λ₯Ό μ€ννλ©΄ JPA DDL CREATE κ° μλν©λλ€.

## π¦ 2. application.properties μμ±ν΄ μ£ΌμΈμ.

```properties

spring.datasource.url=jdbc:mysql://localhost:3306/triple?useUnicode=true&characterEncoding=utf8
spring.datasource.username= <username>
spring.datasource.password= <password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.format_sql=true

// JPA μλ μμ±
spring.jpa.hibernate.ddl-auto=update
```

## π¦ 3. Test Codeλ‘ κ²°κ³Όλ₯Ό νμΈνμΈμ.

`API Tester` μμ΄λ νμ€νΈ μ½λλ‘ DB(mysql)κ²°κ³Όλ₯Ό μ§μ  νμΈν  μ μμ΅λλ€.


# π 2. API Request / Response

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
"content": "μ’μμ!",
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



# π Table

![image](https://user-images.githubusercontent.com/65659478/175563609-1ef29a15-eac3-4f77-b5fb-787044e08019.png)


# MARKS

- ν¬μΈνΈ μ¦κ°μ΄ μμ λλ§λ€ μ΄λ ₯μ΄ λ¨μμΌ ν©λλ€. β­
  - PointLog Tableμ μ¦/κ° μ΄λ ₯μ΄ κΈ°λ‘λ©λλ€.
  

- μ¬μ©μλ§λ€ νμ¬ μμ μ ν¬μΈνΈ μ΄μ μ μ‘°ννκ±°λ κ³μ°ν  μ μμ΄μΌ ν©λλ€. β­
  - `/user?user=<user_UUID>`λ₯Ό ν΅ν΄ νμΈν  μ μμ΅λλ€.
  

- ν¬μΈνΈ λΆμ¬ API κ΅¬νμ νμν SQL μν μ, μ μ²΄ νμ΄λΈ μ€μΊμ΄ μΌμ΄λμ§ μλ μΈλ±μ€κ° νμν©λλ€. β­
    - `@Index` μ΄λΈνμ΄μμ μ¬μ©ν΄ `full-scan` λΉλλ₯Ό μ€μμ΅λλ€.


- λ¦¬λ·°λ₯Ό μμ±νλ€κ° μ­μ νλ©΄ ν΄λΉ λ¦¬λ·°λ‘ λΆμ¬ν λ΄μ© μ μμ λ³΄λμ€ μ μλ νμν©λλ€. β­
- λ¦¬λ·°λ₯Ό μμ νλ©΄ μμ ν λ΄μ©μ λ§λ λ΄μ© μ μλ₯Ό κ³μ°νμ¬ μ μλ₯Ό λΆμ¬νκ±°λ νμν©λλ€. β­


- μ¬μ©μ μμ₯μμ λ³Έ 'μ²« λ¦¬λ·°'μΌ λ λ³΄λμ€ μ μλ₯Ό λΆμ¬ν©λλ€. β­
