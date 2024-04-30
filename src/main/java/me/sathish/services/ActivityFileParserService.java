package me.sathish.services;

import fr.ybonnel.csvengine.CsvEngine;
import fr.ybonnel.csvengine.exception.CsvErrorsExceededException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import me.sathish.entities.Activities;
import me.sathish.entities.RawActivities;
import me.sathish.mapper.RawActivitiesMapper;
import me.sathish.repositories.ActivitiesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ActivityFileParserService {
    final CsvEngine engine = new CsvEngine(RawActivities.class);
    final ActivitiesRepository activitiesRepository;
    final RawActivitiesMapper rawActivitiesMapper;

    private static InputStream getCsvFile() {
        return ActivityFileParserService.class.getResourceAsStream("/activities.csv");
    }

    @Transactional
    public void readFirstLines() throws CsvErrorsExceededException {
        System.out.println("**************");
        System.out.println("read the batch mode lines of activities.csv");
        System.out.println("**************");
        List<RawActivities> rawActivitiesList = engine.parseFirstLinesOfInputStream(
                        getCsvFile(), RawActivities.class, 1000)
                .getObjects();
        Iterable<Activities> activitiesIterable =
                rawActivitiesList.stream().map(rawActivitiesMapper::toEntity).collect(Collectors.toList());
        List<Activities> activitiesList = activitiesRepository.saveAll(activitiesIterable);
        System.out.println("**************");
        System.out.println("Saved the activities in the database" + activitiesList.size());
        System.out.println("**************");
    }
}
