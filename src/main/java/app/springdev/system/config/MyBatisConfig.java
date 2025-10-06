package app.springdev.system.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "app.springdev",annotationClass = Mapper.class)
public class MyBatisConfig {
}
