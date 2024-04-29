package me.sathish.entities;

import fr.ybonnel.csvengine.annotation.CsvColumn;
import fr.ybonnel.csvengine.annotation.CsvFile;
import lombok.Data;
import lombok.extern.java.Log;

@CsvFile
@Data
@Log
public class RawActivities {
    @CsvColumn("Activity ID")
    private String activityID;
    // The "race" field is mapped to a column in CSV named "race"
    @CsvColumn("Activity Date")
    private String activityDate;

    @CsvColumn("Activity Name")
    private String activityName;

    @CsvColumn("Activity Description")
    private String activityDescription;

    @CsvColumn("Activity Type")
    private String activityType;

    @CsvColumn("Max Heart Rate")
    private String maxHeartRate;

    @CsvColumn("Relative Effort")
    private String relativeEffort;

    @CsvColumn("Commute")
    private String commute;

    @CsvColumn("Activity Private Note")
    private String activityPrivateNote;

    @CsvColumn("Activity Gear")
    private String activityGear;

    @CsvColumn("Filename")
    private String filename;

    @CsvColumn("Athlete Weight")
    private String athleteWeight;

    @CsvColumn("Bike Weight")
    private String bikeWeight;

    @CsvColumn("Elapsed Time")
    private String elapsedTime;

    @CsvColumn("Elapsed Time Exact")
    private String elapsedTimeExact;

    @CsvColumn("Moving Time")
    private String movingTime;

    @CsvColumn("Distance")
    private String distance;

    @CsvColumn("Distance Exact")
    private String distanceExact;

    @CsvColumn("Max Speed")
    private String maxSpeed;

    @CsvColumn("Average Speed")
    private String averageSpeed;

    @CsvColumn("Elevation Gain")
    private String elevationGain;

    @CsvColumn("Elevation Loss")
    private String elevationLoss;

    @CsvColumn("Elevation Low")
    private String elevationLow;

    @CsvColumn("Elevation High")
    private String elevationHigh;

    @CsvColumn("Max Grade")
    private String maxGrade;

    @CsvColumn("Average Grade")
    private String averageGrade;

    @CsvColumn("Average Positive Grade")
    private String averagePositiveGrade;

    @CsvColumn("Average Negative Grade")
    private String averageNegativeGrade;

    @CsvColumn("Max Cadence")
    private String maxCadence;

    @CsvColumn("Max Heart Rate Exact")
    private String maxHeartRateExact;

    @CsvColumn("Average Heart Rate")
    private String averageHeartRate;

    @CsvColumn("Max Watts")
    private String maxWatts;

    @CsvColumn("Average Watts")
    private String averageWatts;

    @CsvColumn("Calories")
    private String calories;

    @CsvColumn("Max Temperature")
    private String maxTemperature;

    @CsvColumn("Average Temperature")
    private String averageTemperature;

    @CsvColumn("Relative Effort Exact")
    private String relativeEffortExact;

    @CsvColumn("Total Work")
    private String totalWork;

    @CsvColumn("Number of Runs")
    private String numberOfRuns;

    @CsvColumn("Uphill Time")
    private String uphillTime;

    @CsvColumn("Downhill Time")
    private String downhillTime;

    @CsvColumn("Other Time")
    private String otherTime;

    @CsvColumn("Perceived Exertion")
    private String perceivedExertion;

    @CsvColumn("Type")
    private String type;

    @CsvColumn("Start Time")
    private String startTime;

    @CsvColumn("Weighted Average Power")
    private String weightedAveragePower;

    @CsvColumn("Power Count")
    private String powerCount;

    @CsvColumn("Prefer Perceived Exertion")
    private String preferPerceivedExertion;

    @CsvColumn("Perceived Relative Effort")
    private String perceivedRelativeEffort;

    @CsvColumn("Commute")
    private String commute1;

    @CsvColumn("Total Weight Lifted")
    private String totalWeightLifted;

    @CsvColumn("From Upload")
    private String fromUpload;

    @CsvColumn("Grade Adjusted Distance")
    private String gradeAdjustedDistance;

    @CsvColumn("Weather Observation")
    private String weatherObservation;

    @CsvColumn("Time")
    private String time;

    @CsvColumn("Weather Condition")
    private String weatherCondition;

    @CsvColumn("Weather Temperature")
    private String weatherTemperature;

    @CsvColumn("Apparent Temperature")
    private String apparentTemperature;

    @CsvColumn("Dewpoint")
    private String dewpoint;

    @CsvColumn("Humidity")
    private String humidity;

    @CsvColumn("Weather Pressure")
    private String weatherPressure;

    @CsvColumn("Wind Speed")
    private String windSpeed;

    @CsvColumn("Wind Gust")
    private String windGust;

    @CsvColumn("Wind Bearing")
    private String windBearing;

    @CsvColumn("Precipitation Intensity")
    private String precipitationIntensity;

    @CsvColumn("Sunrise Time")
    private String sunriseTime;

    @CsvColumn("Sunset Time")
    private String sunsetTime;

    @CsvColumn("Moon Phase")
    private String moonPhase;

    @CsvColumn("Bike")
    private String bike;

    @CsvColumn("Gear")
    private String gear;

    @CsvColumn("Precipitation Probability")
    private String precipitationProbability;

    @CsvColumn("Precipitation Type")
    private String precipitationType;

    @CsvColumn("Cloud Cover")
    private String cloudCover;

    @CsvColumn("Weather Visibility")
    private String weatherVisibility;

    @CsvColumn("UV Index")
    private String uvIndex;

    @CsvColumn("Weather Ozone")
    private String weatherOzone;

    @CsvColumn("Jump Count")
    private String jumpCount;

    @CsvColumn("Total Grit")
    private String totalGrit;

    @CsvColumn("Average Flow")
    private String averageFlow;

    @CsvColumn("Flagged")
    private String flagged;

    @CsvColumn("Average Elapsed Speed")
    private String averageElapsedSpeed;

    @CsvColumn("Dirt Distance")
    private String dirtDistance;

    @CsvColumn("Newly Explored Distance")
    private String newlyExploredDistance;

    @CsvColumn("Newly Explored Dirt Distance")
    private String newlyExploredDirtDistance;

    @CsvColumn("Activity Count")
    private String activityCount;

    @CsvColumn("Total Steps")
    private String totalSteps;

    @CsvColumn("Carbon Saved")
    private String carbonSaved;

    @CsvColumn("Pool Length")
    private String poolLength;

    @CsvColumn("Training Load")
    private String trainingLoad;

    @CsvColumn("Intensity")
    private String intensity;

    @CsvColumn("Average Grade Adjusted Pace")
    private String averageGradeAdjustedPace;

    @CsvColumn("Media")
    private String media;

    @CsvColumn("Average Cadence")
    private String averageCadence;
}
