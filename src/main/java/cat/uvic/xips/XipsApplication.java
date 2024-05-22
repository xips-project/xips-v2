package cat.uvic.xips;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class XipsApplication  {

    public static void main(String[] args)  {
		SpringApplication.run(XipsApplication.class, args);
	}

}
