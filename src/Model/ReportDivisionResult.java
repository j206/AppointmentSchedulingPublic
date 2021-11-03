package Model;

/**
 * Object model for results of a DB query for appointments of type, per month. Used in
 * ReportDivisionController.
 */
public class ReportDivisionResult {
  private String division;
  private int count;

  public ReportDivisionResult(String division, int count) {
    this.division = division;
    this.count = count;
  }

  public String getDivision() {
    return division;
  }

  public void setDivision(String division) {
    this.division = division;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
