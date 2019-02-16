package ba.unsa.etf.rma.samira.spirala3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class Knjiga implements Parcelable {

    private String id;
    private String naziv;
    private ArrayList<Autor> autori = new ArrayList<Autor>();
    private String opis;
    private String datumObjavljivanja;
    private URL slika;
    private int brojStranica;
    private Boolean kliknuto;
    private Bitmap slika1;
    private String kategorija;
    private Uri uri;

    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStranica) {
        this.id = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStranica = brojStranica;
        this.kliknuto = false;

    }

    public Knjiga(Knjiga k, String kategorija) {
        this.id = k.getId();
        this.naziv = k.getNaziv();
        this.autori = k.getAutori();
        this.opis = k.getOpis();
        this.datumObjavljivanja = k.getDatumObjavljivanja();
        this.slika = k.getSlika();
        this.slika1 = k.getSlika1();
        this.brojStranica = k.getBrojStranica();
        this.kliknuto = false;
        this.kategorija = kategorija;
    }

    public Knjiga(Knjiga k, String kategorija, Boolean kliknuto) {
        this.id = k.getId();
        this.naziv = k.getNaziv();
        this.autori = k.getAutori();
        this.opis = k.getOpis();
        this.datumObjavljivanja = k.getDatumObjavljivanja();
        this.slika = k.getSlika();
        this.slika1 = k.getSlika1();
        this.brojStranica = k.getBrojStranica();
        this.kliknuto = kliknuto;
        this.kategorija = kategorija;
        try {
            slika1 = BitmapFactory.decodeStream(slika.openConnection().getInputStream());
        } catch (Exception e) {
            slika1 = null;
        }
    }

    public Knjiga(String ime, ArrayList<Autor> autor, String kategorija, Bitmap slika) {
        this.naziv = ime;
        this.autori = autor;
        this.kategorija = kategorija;
        this.kliknuto = false;
        this.slika1 = slika;
        this.opis = "";
        this.datumObjavljivanja = "";
        this.brojStranica = 0;
        this.slika = null;
        this.id = "";
        try{
        this.slika= new URL("https://meddwl.org/wp-content/uploads/2016/10/llyfrau-300x198.png");}
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public Knjiga(String ime, ArrayList<Autor> autor, String kategorija, Uri slika) {
        this.naziv = ime;
        this.autori = autor;
        this.kategorija = kategorija;
        this.kliknuto = false;
        this.uri = slika;
        this.opis = "";
        this.datumObjavljivanja = "";
        this.brojStranica = 0;
        this.slika = null;
        this.id = "";
        try{
            this.slika= new URL("https://meddwl.org/wp-content/uploads/2016/10/llyfrau-300x198.png");}
        catch(Exception e){
            e.printStackTrace();
        }
    }

    protected Knjiga(Parcel in) {
        id = in.readString();
        naziv = in.readString();
        opis = in.readString();
        datumObjavljivanja = in.readString();
        brojStranica = in.readInt();
        byte tmpKliknuto = in.readByte();
        kliknuto = tmpKliknuto == 0 ? null : tmpKliknuto == 1;
        slika1 = in.readParcelable(Bitmap.class.getClassLoader());
        kategorija = in.readString();
    }

    public static final Creator<Knjiga> CREATOR = new Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel in) {
            return new Knjiga(in);
        }

        @Override
        public Knjiga[] newArray(int size) {
            return new Knjiga[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public URL getSlika() {
        return slika;
    }

    public void setSlika(URL slika) {
        this.slika = slika;
    }

    public int getBrojStranica() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }

    public Bitmap getSlika1() {
        return slika1;
    }

    public void setSlika1(Bitmap slika1) {
        this.slika1 = slika1;
    }

    public Boolean getKliknuto() {
        return kliknuto;
    }

    public void setKliknuto(Boolean kliknuto) {
        this.kliknuto = kliknuto;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(naziv);
        dest.writeString(opis);
        dest.writeString(datumObjavljivanja);
        dest.writeInt(brojStranica);
        dest.writeByte((byte) (kliknuto == null ? 0 : kliknuto ? 1 : 2));
        dest.writeParcelable(slika1, flags);
        dest.writeString(kategorija);
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
