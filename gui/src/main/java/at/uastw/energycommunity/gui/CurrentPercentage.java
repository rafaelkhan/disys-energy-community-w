package at.uastw.energycommunity.gui;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentPercentage {
    private LocalDateTime hour;
    private double communityDepleted;
    private double gridPortion;

    public LocalDateTime getHour() { return hour; }
    public void setHour(LocalDateTime hour) { this.hour = hour; }

    public double getCommunityDepleted() { return communityDepleted; }
    public void setCommunityDepleted(double communityDepleted) { this.communityDepleted = communityDepleted; }

    public double getGridPortion() { return gridPortion; }
    public void setGridPortion(double gridPortion) { this.gridPortion = gridPortion; }
}
