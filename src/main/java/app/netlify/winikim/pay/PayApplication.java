package app.netlify.winikim.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class PayApplication {

  public static void main(String[] args) {
    SpringApplication.run(PayApplication.class, args);
  }

}
