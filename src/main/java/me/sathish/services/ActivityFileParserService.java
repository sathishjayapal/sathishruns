package me.sathish.services;

import fr.ybonnel.csvengine.CsvEngine;
import fr.ybonnel.csvengine.exception.CsvErrorsExceededException;
import lombok.AllArgsConstructor;
import me.sathish.entities.Activities;
import me.sathish.entities.RawActivities;
import me.sathish.mapper.RawActivitiesMapper;
import me.sathish.repositories.ActivitiesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ActivityFileParserService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityFileParserService.class);
    final CsvEngine engine = new CsvEngine(RawActivities.class);
    final ActivitiesRepository activitiesRepository;
    final RawActivitiesMapper rawActivitiesMapper;

    private static InputStream getCsvFile() {
        return ActivityFileParserService.class.getResourceAsStream("/activities.csv");
    }

    @Transactional
    public void readFirstLines() throws CsvErrorsExceededException {
        if (activitiesRepository.count() > 0) {
            logger.info("**************");
            logger.info("Activities already present in the database");
            logger.info("**************");
            return;
        } else {
            logger.info("**************");
            logger.info("read the batch mode lines of activities.csv");
            logger.info("**************");
            List<RawActivities> rawActivitiesList = engine.parseFirstLinesOfInputStream(
                            getCsvFile(), RawActivities.class, 1000)
                    .getObjects();
            Iterable<Activities> activitiesIterable = rawActivitiesList.stream()
                    .map(rawActivitiesMapper::toEntity)
                    .collect(Collectors.toList());
            List<Activities> activitiesList = activitiesRepository.saveAll(activitiesIterable);
            System.out.println("**************");
            System.out.println("Saved the activities in the database" + activitiesList.size());
            System.out.println("**************");
        }
    }
}
