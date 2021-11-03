package Model;

/** Object model for contacts. */
public class Contact {
  private int id;
  private String contactName, email;

  public Contact(int id, String contactName, String email) {
    this.id = id;
    this.contactName = contactName;
    this.email = email;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return (contactName);
  }
}
