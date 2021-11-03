package Model;

public class Country {
  private int id;
  private String country;

  public Country(int id, String name) {
    this.id = id;
    this.country = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  @Override
  public String toString() {
    return (country);
  }
}
