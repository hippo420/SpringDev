package app.springdev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
//Hook 처리시 주석
//@EnableScheduling
public class SpringDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDevApplication.class, args);
    }

}
