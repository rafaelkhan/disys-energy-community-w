package at.uastw.energycommunity.restapi.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

// the rest api only reads this table, it is written by the usage service
@Entity(name = "hourly_usage")
public class HourlyUsageEntity {

    @Id
    private LocalDateTime hour;
    @Column(name = "community_produced")
    private double communityProduced;
    @Column(name = "community_used")
    private double communityUsed;
    @Column(name = "grid_used")
    private double gridUsed;

    public LocalDateTime getHour() {
        return hour;
    }

    public void setHour(LocalDateTime hour) {
        this.hour = hour;
    }

    public double getCommunityProduced() {
        return communityProduced;
    }

    public void setCommunityProduced(double communityProduced) {
        this.communityProduced = communityProduced;
    }

    public double getCommunityUsed() {
        return communityUsed;
    }

    public void setCommunityUsed(double communityUsed) {
        this.communityUsed = communityUsed;
    }

    public double getGridUsed() {
        return gridUsed;
    }

    public void setGridUsed(double gridUsed) {
        this.gridUsed = gridUsed;
    }
}
