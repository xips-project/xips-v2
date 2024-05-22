package cat.uvic.xips;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class XipsApplication implements CommandLineRunner {

	//private final DataInitializer dataInitializer;

   /* public XipsApplication(DataInitializer dataInitializer) {
        this.dataInitializer = dataInitializer;
    }*/

    public static void main(String[] args)  {
		SpringApplication.run(XipsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//dataInitializer.initializeUsers();
		//dataInitializer.initializeProducts();
		//dataInitializer.initializeRatings();
	}
}
