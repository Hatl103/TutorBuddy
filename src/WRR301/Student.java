package WRR301;

public class Student {

    private int studentNr;
    private String firstName;
    private String lastName;
    private String email;
    private int contactNumber;
    private Boolean tutoringStatus;

    public Student(int studentNr, String firstName, String lastName,String email, int contactNumber, Boolean tutoringStatus)
    {
        this.studentNr=studentNr;
        this.firstName =firstName;
        this.lastName= lastName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.tutoringStatus =tutoringStatus;
    }

    public int getStudentNr() {
        return studentNr;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(int contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Boolean getTutoringStatus() {
        return tutoringStatus;
    }

    public void setTutoringStatus(Boolean tutoringStatus) {
        this.tutoringStatus = tutoringStatus;
    }
}
