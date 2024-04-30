package me.sathish;

import lombok.RequiredArgsConstructor;
import me.sathish.services.ActivityFileParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RawActivitiesInitializer implements CommandLineRunner {
    @Autowired
    private ActivityFileParserService activityFileParserService;

    @Override
    public void run(String... args) throws Exception {
        activityFileParserService.readFirstLines();
    }
}
