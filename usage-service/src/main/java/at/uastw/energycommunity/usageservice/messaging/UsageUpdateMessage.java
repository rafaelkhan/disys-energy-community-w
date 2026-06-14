package at.uastw.energycommunity.usageservice.messaging;

import java.time.LocalDateTime;

// the message the usage service puts on the energy_update queue.
// it carries everything the percentage service needs, so that service
// never has to read the hourly_usage table itself.
public class UsageUpdateMessage {

    private LocalDateTime hour;
    private double communityProduced;
    private double communityUsed;
    private double gridUsed;

    public UsageUpdateMessage() {
    }

    public UsageUpdateMessage(LocalDateTime hour, double communityProduced,
                              double communityUsed, double gridUsed) {
        this.hour = hour;
        this.communityProduced = communityProduced;
        this.communityUsed = communityUsed;
        this.gridUsed = gridUsed;
    }

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
