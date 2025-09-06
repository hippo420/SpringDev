# 요청 데이터 받는 방법
### 스프링
* [Official Gradle documentation]
* [Spring Boot Gradle Plugin Reference Guide]
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.0/gradle-plugin/packaging-oci-image.html)

## 1. @RequestParam
### 쿼리 파라미터 받기

- Client
```vue
axios.get('http://localhost:8080/search', {
  params: {
    keyword: 'vuejs'
  }
});
```


- Server
```java
@GetMapping("queryParam")
public void queryParam(@RequestParam String param, HttpServletRequest request) {
    printRequest(request);
    log.info("파리미터 : {}", param);
}
```


required = false를 통해 필수 여부 조정 가능
```java
@RequestParam(required = false) String param
```
---
## 2. @PathVariable
### URL 경로 변수 받기
+ Client
```vue
const userId = 123;
axios.get(`http://localhost:8080/users/${userId}`);
```
+ Server
```java
@GetMapping("/users/{id}")
public void getUser(@PathVariable(required = false) Long id, HttpServletRequest request) {
    printRequest(request);
    log.info("파리미터 : {}", id == null ? "파라미터가 없습니다." : id);
}
```

required = false를 통해 필수 여부 조정 가능
```java
@PathVariable(required = false) Long id
```
---
## 3. @RequestBody
### JSON (또는 XML) 데이터를 객체로 받기
+ Client
```vue
const user = {
  name: '홍길동',
  age: 30
};

axios.post('http://localhost:8080/users', user);
```

+ Server
```java
@PostMapping("/users")
public void createUser(@RequestBody UserDto userDto, HttpServletRequest request) {
    printRequest(request);
    log.info("요청 사용자 데이터");
    log.info("id: {}", userDto.getId());
    log.info("name: {}", userDto.getName());
    log.info("age: {}", userDto.getAge());
    log.info("email: {}", userDto.getEmail());
}
```
UserDto는 단순 Java 클래스 (DTO)
```java
@Data
public class UserDto {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
```
---
## 4. @ModelAttribute
### 폼 데이터나 쿼리 파라미터를 객체로 매핑
+ Client
```vue
const form = new URLSearchParams();
form.append('name', '김개발');
form.append('email', 'dev@example.com');

axios.post('http://localhost:8080/form', form);
```

+ Server
```java
@PostMapping("/register")
public void register(@ModelAttribute UserDto userDto, HttpServletRequest request) {
    printRequest(request);
    log.info("요청 사용자 데이터");
    log.info("id: {}", userDto.getId());
    log.info("name: {}", userDto.getName());
    log.info("age: {}", userDto.getAge());
    log.info("email: {}", userDto.getEmail());
}
```
주로 **HTML \<form\>으로 보낸 데이터**를 처리할 때 사용하며 **application/x-www-form-urlencoded** 방식에 적합

---
## 5. @RequestHeader
### 요청 헤더 받기
+ Client
```vue
axios.get('http://localhost:8080/auth', {
  headers: {
    Authorization: 'Bearer abc123'
  }
});
```

+ Server
```java
@GetMapping("/auth")
public void auth(@RequestHeader("Authorization") String token, HttpServletRequest request) {
    printRequest(request);
    log.info("토큰 : [{}]",token);
}
```
---
## 5. @RequestParam MultipartFile
### 파일 업로드
+ Client
```vue
const formData = new FormData();
formData.append('file', selectedFile);  // selectedFile: input에서 가져온 파일

axios.post('http://localhost:8080/upload', formData, {
headers: {
'Content-Type': 'multipart/form-data'
}
});
```

+ Server
```java
@PostMapping("/upload")
public String upload(@RequestParam MultipartFile file) {
    return "파일명: " + file.getOriginalFilename();
}
```
---


