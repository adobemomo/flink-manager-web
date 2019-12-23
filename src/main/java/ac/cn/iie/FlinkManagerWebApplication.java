package ac.cn.iie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlinkManagerWebApplication {

  public static void main(String[] args) {
    SpringApplication.run(FlinkManagerWebApplication.class, args);
  }
}
