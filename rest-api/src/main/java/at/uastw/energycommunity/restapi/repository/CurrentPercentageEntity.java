package at.uastw.energycommunity.restapi.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

// the rest api only reads this table, it is written by the current percentage service
@Entity(name = "current_percentage")
public class CurrentPercentageEntity {

    @Id
    private LocalDateTime hour;
    @Column(name = "community_depleted")
    private double communityDepleted;
    @Column(name = "grid_portion")
    private double gridPortion;

    public LocalDateTime getHour() {
        return hour;
    }

    public void setHour(LocalDateTime hour) {
        this.hour = hour;
    }

    public double getCommunityDepleted() {
        return communityDepleted;
    }

    public void setCommunityDepleted(double communityDepleted) {
        this.communityDepleted = communityDepleted;
    }

    public double getGridPortion() {
        return gridPortion;
    }

    public void setGridPortion(double gridPortion) {
        this.gridPortion = gridPortion;
    }
}
