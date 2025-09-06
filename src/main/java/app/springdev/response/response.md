# 응답 데이터 주는 방법
### 스프링
* [Official Gradle documentation]
* [Spring Boot Gradle Plugin Reference Guide]
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.0/gradle-plugin/packaging-oci-image.html)

## 1. 뷰(View) 반환 (전통적인 MVC 방식)
+ Spring MVC에서 JSP, Thymeleaf 같은 템플릿 엔진을 사용해 HTML 페이지를 응답
+ 컨트롤러 메서드가 문자열을 반환하면, ViewResolver가 해당 뷰 파일을 찾아서 렌더링

```java
@GetMapping("/view")
public String view(Model model) {
    UserDto userDto = new UserDto();
    userDto.setId(1L);
    userDto.setName("Jack");
    userDto.setEmail("jack@gmail.com");
    userDto.setAge(25);

    model.addAttribute("user", userDto);
    return "/response/sample";
}
```

---


## 2. @ResponseBody 활용 (상태 코드 + 헤더 제어 가능) → 가장 많이 사용

+ 리턴값을 HTTP Response Body에 그대로 넣어줌
+ 뷰를 찾지 않고, HttpMessageConverter가 **JSON, XML, String** 등으로 변환해서 응답
+ 상태 코드(HTTP Status)와 헤더는 기본값(200 OK, 기본 헤더)으로 동작

**@RestController**인 경우에는 **@ResponseBody** 생략가능
```java

//@ResponseBody
@GetMapping("/body")
public UserDto body() {
    UserDto userDto = new UserDto();
    userDto.setId(1L);
    userDto.setName("JackBody");
    userDto.setEmail("jackBody@naver.com");
    userDto.setAge(25);

    return userDto;
}
```

---


## 4. ResponseEntity<T> 활용 (상태 코드 + 헤더 제어 가능)

+ 단순 JSON 반환보다 상태 코드, 헤더, 바디를 세밀하게 제어할 수 있음
+ **응답 본문(body) + 상태 코드(status) + 헤더(headers)**를 모두 직접 제어 가능
+ **@ResponseBody**가 내부적으로 적용된 형태라고 볼 수도 있음

```java
@GetMapping("/entity")
public ResponseEntity<UserDto> entity() {
    UserDto userDto = new UserDto();
    userDto.setId(2L);
    userDto.setName("JackEntity");
    userDto.setEmail("JackEntity@gmail.com");
    userDto.setAge(50);

    return ResponseEntity
            .status(HttpStatus.OK)
            .header("Token", "MyToken")
            .body(userDto);
}
```

## 5. 파일 응답
+ 단순 JSON 반환보다 상태 코드, 헤더, 바디를 세밀하게 제어할 수 있음

```java
@PostMapping("/download")
public ResponseEntity<Resource>  download() {
    ClassPathResource resource = new ClassPathResource("data/test.pdf");

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(resource);
}
```

### 📌@ResponseBody vs ResponseEntity<T>
 🔹@ResponseBody = "본문만 보내줄게"

 🔹ResponseEntity<T> = "본문 + 상태코드 + 헤더까지 내가 컨트롤할게"

---


## 5. 파일 응답 (다운로드)
+ 파일 크기나 전송 방식 주의
+ Content-Length 명시
Resource를 반환하면 Spring이 InputStream을 열어 버퍼링 하면서 클라이언트로 흘려줌

### 5-1. 파일 응답 (다운로드)
```java
@GetMapping("/download")
public ResponseEntity<Resource> downloadFile() throws IOException {
    Path path = Paths.get("C:/files/test.pdf");
    Resource resource = new FileSystemResource(path);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(resource);
}
```

### 5-2. 파일 응답 (다운로드-스트리밍)
```java
@PostMapping("/downloadstream")
public ResponseEntity<StreamingResponseBody> downloadstream() throws IOException {
    ClassPathResource file = new ClassPathResource("data/test.pdf");
    long length = file.contentLength();

    InputStream inputStream = new FileInputStream(String.valueOf(file));

    StreamingResponseBody body = outputStream -> {
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
    };

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test.pdf")
            .contentLength(length)
            .contentType(MediaType.APPLICATION_PDF)
            .body(body);
}
```

---
## 6. 비동기 응답 (SSE, WebSocket)
+ SSE(Server-Sent Events) → 서버에서 클라이언트로 실시간 푸시 (단방향)
+ WebSocket → 양방향 통신

```java

```
---



