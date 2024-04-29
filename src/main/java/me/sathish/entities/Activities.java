package me.sathish.entities;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Activities {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String activityID;

    @Column(nullable = false)
    private String activityDate;

    @Column(nullable = false)
    private String activityType;

    private String activityDescription;

    @Column(nullable = false)
    private String elapsedTime;

    @Column(nullable = false)
    private String distance;

    @Column(nullable = false)
    private String calories;

    private String maxHeartRate;

    @Column(nullable = false)
    private String activityName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Activities activities = (Activities) o;
        return id != null && Objects.equals(id, activities.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
