package net.cybermak.integration.api.model.requests;

/**
 * IncidentDetails DTO for SolarWinds to BMC Remedy integration
 * Represents incident data from SolarWinds monitoring system
 * 
 * Recreated from original compiled WAR file to maintain API compatibility
 * 
 * @author Reverse Engineered from WAR file
 * @version 1.0
 */
public class IncidentDetails {

    private String sourceOfCreation;
    private String alertName;
    private String alertDescription;
    private String severity;
    private String alertLimitCategory;
    private String add1;
    private String add2;
    private String key;

    /**
     * Default constructor
     */
    public IncidentDetails() {
    }

    /**
     * Full constructor with all fields
     * 
     * @param sourceOfCreation Source system that created the incident
     * @param alertName Name of the alert
     * @param alertDescription Description of the alert
     * @param severity Severity level of the incident
     * @param alertLimitCategory Category of the alert limit
     * @param add1 Additional field 1
     * @param add2 Additional field 2
     * @param key Authentication key for the request
     */
    public IncidentDetails(String sourceOfCreation, String alertName, String alertDescription, 
                          String severity, String alertLimitCategory, String add1, String add2, String key) {
        this.sourceOfCreation = sourceOfCreation;
        this.alertName = alertName;
        this.alertDescription = alertDescription;
        this.severity = severity;
        this.alertLimitCategory = alertLimitCategory;
        this.add1 = add1;
        this.add2 = add2;
        this.key = key;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String getAlertDescription() {
        return alertDescription;
    }

    public void setAlertDescription(String alertDescription) {
        this.alertDescription = alertDescription;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getAlertLimitCategory() {
        return alertLimitCategory;
    }

    public void setAlertLimitCategory(String alertLimitCategory) {
        this.alertLimitCategory = alertLimitCategory;
    }

    public String getAdd1() {
        return add1;
    }

    public void setAdd1(String add1) {
        this.add1 = add1;
    }

    public String getAdd2() {
        return add2;
    }

    public void setAdd2(String add2) {
        this.add2 = add2;
    }

    public String getSourceOfCreation() {
        return sourceOfCreation;
    }

    public void setSourceOfCreation(String sourceOfCreation) {
        this.sourceOfCreation = sourceOfCreation;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "IncidentDetails{" +
                "sourceOfCreation='" + sourceOfCreation + '\'' +
                ", alertName='" + alertName + '\'' +
                ", alertDescription='" + alertDescription + '\'' +
                ", severity='" + severity + '\'' +
                ", alertLimitCategory='" + alertLimitCategory + '\'' +
                ", add1='" + add1 + '\'' +
                ", add2='" + add2 + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}