package Model;

/**
 * Object model for results of a DB query for appointments of type, per month. Used in
 * ReportCountsController.
 */
public class ReportCountsResult {
  private String type;
  private int count;

  public ReportCountsResult(String type, int count) {
    this.type = type;
    this.count = count;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
