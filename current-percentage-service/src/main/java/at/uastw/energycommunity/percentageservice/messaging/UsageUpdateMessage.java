package at.uastw.energycommunity.percentageservice.messaging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

// the message received from the energy_update queue. it already contains the
// full state of the hour, so this service does not read the hourly_usage table.
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsageUpdateMessage {

    private LocalDateTime hour;
    private double communityProduced;
    private double communityUsed;
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
