package me.sathish.services;

import fr.ybonnel.csvengine.CsvEngine;
import fr.ybonnel.csvengine.exception.CsvErrorsExceededException;
import java.io.InputStream;
import java.util.List;
import lombok.AllArgsConstructor;
import me.sathish.entities.RawActivities;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActivityFileParserService {
    /**
     * Create the CsvEngine for the class Dog.
     */
    final CsvEngine engine = new CsvEngine(RawActivities.class);

    public void readFirstLines() throws CsvErrorsExceededException {
        System.out.println("**************");
        System.out.println("read the first two lines of activities.csv");
        System.out.println("**************");
        List<RawActivities> rawActivitiesList = engine.parseFirstLinesOfInputStream(
                        getCsvFile(), RawActivities.class, 1000)
                .getObjects();
        for (RawActivities rawActivities : rawActivitiesList) {
            System.out.println(rawActivities.toString());
        }
    }

    private static InputStream getCsvFile() {
        return ActivityFileParserService.class.getResourceAsStream("/activities.csv");
    }
}
