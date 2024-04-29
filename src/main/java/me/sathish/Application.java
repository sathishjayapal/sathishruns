package me.sathish;

import me.sathish.config.ApplicationProperties;
import me.sathish.services.ActivityFileParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
class RawActivitesCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ActivityFileParserService activityFileParserService;

    public static void main(String[] args) {
        SpringApplication.run(RawActivitesCommandLineRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        activityFileParserService.readFirstLines();
    }
}
