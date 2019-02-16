package ba.unsa.etf.rma.samira.spirala3;

public class Kontakt {

    private String ime;
    private String email;


    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Kontakt(String ime, String email){
        this.ime = ime;
        this.email = email;
    }
}
