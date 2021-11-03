package Model;

/**
 * Object model for first-level divisions. For example, states in the United States, and provinces
 * in Canada.
 */
public class Division {
  private int id, countryId;
  private String divisionName, createDate, createdBy, lastUpdate, lastUpdatedBy;

  public Division(
      int id,
      int countryId,
      String divisionName,
      String createDate,
      String createdBy,
      String lastUpdate,
      String lastUpdatedBy) {
    this.id = id;
    this.countryId = countryId;
    this.divisionName = divisionName;
    this.createDate = createDate;
    this.createdBy = createdBy;
    this.lastUpdate = lastUpdate;
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getCountryId() {
    return countryId;
  }

  public void setCountryId(int countryId) {
    this.countryId = countryId;
  }

  public String getDivision() {
    return divisionName;
  }

  public void setDivision(String division) {
    this.divisionName = division;
  }

  public String getCreateDate() {
    return createDate;
  }

  public void setCreateDate(String createDate) {
    this.createDate = createDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(String lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  /** Overrides string conversion for use in JavaFX elements. */
  @Override
  public String toString() {
    return (divisionName);
  }
}
