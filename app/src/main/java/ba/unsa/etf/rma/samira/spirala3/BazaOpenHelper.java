package ba.unsa.etf.rma.samira.spirala3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLInput;
import java.util.ArrayList;

public class BazaOpenHelper extends SQLiteOpenHelper{



    public static final String IME_BAZE = "mojaBaza.db";
    public static final int VERZIJA_BAZE = 1;
    //Tabele
    public static final String KATEGORIJA_TABELA = "Kategorija";
    public static final String KNJIGA_TABELA = "Knjiga";
    public static final String AUTOR_TABELA = "Autor";
    public static final String AUTORSTVO_TABELA = "Autorstvo";
    //Kolone tabele Kategorija
    public static final String KATEGORIJA_ID = "_id";
    public static final String KATEGORIJA_NAZIV = "naziv";
    //Kolone tabele Knjiga
    public static final String KNJIGA_ID = "_id";
    public static final String KNJIGA_NAZIV = "naziv";
    public static final String KNJIGA_OPIS = "opis";
    public static final String KNJIGA_DATUM = "datumObjavljivanja";
    public static final String KNJIGA_STRANICE = "brojStranica";
    public static final String KNJIGA_IDWEB = "idWebServis";
    public static final String KNJIGA_IDKATEGORIJE = "idkategorije";
    public static final String KNJIGA_SLIKA = "slika";
    public static final String KNJIGA_PREGLEDANA = "pregledana";
    //Kolone tabele Autor
    public static final String AUTOR_ID = "_id";
    public static final String AUTOR_IME = "ime";
    //Kolone tabele Autorstvo
    public static final String AUTORSTVO_ID = "_id";
    public static final String AUTORSTVO_IDAUTORA = "idautora";
    public static final String AUTORSTVO_IDKNJIGE = "idknjige";


    private static final String KATEGORIJA_KREIRANJE = "create table " + KATEGORIJA_TABELA +
            " (" + KATEGORIJA_ID + " integer primary key autoincrement,"+
            KATEGORIJA_NAZIV + " text not null);";

    private static final String KNJIGA_KREIRANJE = "create table " + KNJIGA_TABELA +
            "(" + KNJIGA_ID + " integer primary key autoincrement, "+
            KNJIGA_NAZIV + " text not null, " +
            KNJIGA_OPIS + " text, " +
            KNJIGA_DATUM + " text, " +
            KNJIGA_STRANICE + " integer, "+
            KNJIGA_IDWEB + " text, " +
            KNJIGA_IDKATEGORIJE + " integer not null, " +
            KNJIGA_SLIKA + " text, " +
            KNJIGA_PREGLEDANA + " integer not null);";

    private static final String AUTOR_KREIRANJE = "create table " + AUTOR_TABELA +
            "(" + AUTOR_ID + " integer primary key autoincrement, " +
            AUTOR_IME + " text not null);";

    private static final String AUTORSTVO_KREIRANJE = "create table " + AUTORSTVO_TABELA +
            "(" + AUTORSTVO_ID + " integer primary key autoincrement, " +
            AUTORSTVO_IDAUTORA + " integer," +
            AUTORSTVO_IDKNJIGE + " integer not null);";



    public BazaOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(KATEGORIJA_KREIRANJE);
        db.execSQL(KNJIGA_KREIRANJE);
        db.execSQL(AUTOR_KREIRANJE);
        db.execSQL(AUTORSTVO_KREIRANJE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + KNJIGA_TABELA);
        db.execSQL("DROP TABLE IF EXISTS " + AUTOR_TABELA);
        db.execSQL("DROP TABLE IF EXISTS " + AUTORSTVO_TABELA);
        db.execSQL("DROP TABLE IF EXISTS " + KATEGORIJA_TABELA);

        onCreate(db);
    }

    public void obrisiSveTabele(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + KNJIGA_TABELA);
        db.execSQL("DROP TABLE IF EXISTS " + AUTOR_TABELA);
        db.execSQL("DROP TABLE IF EXISTS " + AUTORSTVO_TABELA);
        db.execSQL("DROP TABLE IF EXISTS " + KATEGORIJA_TABELA);
    }

    public long dodajKategoriju(String naziv){

        String[] koloneRezultat = new String[]{KATEGORIJA_NAZIV};
        String where = KATEGORIJA_NAZIV + "='" +naziv+"'";
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(KATEGORIJA_TABELA,koloneRezultat,where,whereArgs,groupBy,having,order);
        if(cursor.getCount()==0){
            ContentValues noviRed = new ContentValues();
            noviRed.put(KATEGORIJA_NAZIV,naziv);
            db.insert(KATEGORIJA_TABELA,null,noviRed);
        }
        else{return -1;}
        cursor.close();
        String[] koloneRezultat2 = new String[]{KATEGORIJA_ID};
        Cursor cursor2 = db.query(KATEGORIJA_TABELA,koloneRezultat2,where,whereArgs,groupBy,having,order);
        int INDEX_ID = cursor2.getColumnIndex(KATEGORIJA_ID);
        cursor2.moveToFirst();
        return cursor2.getInt(INDEX_ID);
    }

    public long dodajKnjigu(Knjiga knjiga){

        SQLiteDatabase db = getWritableDatabase();

        String[] koloneRezultat = new String[]{KNJIGA_NAZIV};
        String where = KNJIGA_NAZIV + "='" + popraviString(knjiga.getNaziv())+"'";
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        Cursor cursor = db.query(KNJIGA_TABELA,koloneRezultat,where,whereArgs,groupBy,having,order);
        if(cursor.getCount()>0){
            return -1;
        }
        ContentValues novaKnjiga = new ContentValues();
        novaKnjiga.put(KNJIGA_NAZIV,knjiga.getNaziv());
        novaKnjiga.put(KNJIGA_DATUM,knjiga.getDatumObjavljivanja());
        novaKnjiga.put(KNJIGA_IDWEB,knjiga.getId());
        novaKnjiga.put(KNJIGA_STRANICE,knjiga.getBrojStranica());
        
        novaKnjiga.put(KNJIGA_IDKATEGORIJE,vratiIdKategorije(knjiga.getKategorija()));
        novaKnjiga.put(KNJIGA_OPIS,knjiga.getOpis());
        novaKnjiga.put(KNJIGA_PREGLEDANA,0);
        

        if(knjiga.getId().equals("")){
            novaKnjiga.put(KNJIGA_SLIKA, knjiga.getUri().toString());
        }
        else{
        novaKnjiga.put(KNJIGA_SLIKA,knjiga.getSlika().toString());}

        db.insert(KNJIGA_TABELA,null,novaKnjiga);

        ArrayList<Autor> autori = knjiga.getAutori();

        cursor.close();

        long idKnjige = vratiIdKnjige(knjiga.getNaziv());

        if(autori.size()==0){
            ContentValues novoAutorstvo = new ContentValues();
            novoAutorstvo.put(AUTORSTVO_IDKNJIGE,idKnjige);
            db.insert(AUTORSTVO_TABELA,null,novoAutorstvo);
        }
        else{
            for(int i=0;i<autori.size();i++){
                if(!imaLiAutor(autori.get(i).getImeiPrezime())){
                    ContentValues noviAutor = new ContentValues();
                    noviAutor.put(AUTOR_IME,autori.get(i).getImeiPrezime());
                    db.insert(AUTOR_TABELA,null,noviAutor);
                }
                long idAutora = vratiIdAutora(autori.get(i).getImeiPrezime());
                ContentValues novoAutorstvo = new ContentValues();
                novoAutorstvo.put(AUTORSTVO_IDKNJIGE,idKnjige);
                novoAutorstvo.put(AUTORSTVO_IDAUTORA,idAutora);
                db.insert(AUTORSTVO_TABELA,null,novoAutorstvo);
            }
        }

        return idKnjige;
    }

    public ArrayList<Knjiga> knjigeKategorije(long idKategorije){
        ArrayList<Knjiga> rez = new ArrayList<Knjiga>();

        String[] koloneRezultat = new String[]{KNJIGA_NAZIV,KNJIGA_OPIS,KNJIGA_DATUM,KNJIGA_STRANICE,KNJIGA_IDWEB,KNJIGA_IDKATEGORIJE,KNJIGA_SLIKA,KNJIGA_PREGLEDANA};
        String where = KNJIGA_IDKATEGORIJE + "=" +idKategorije;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(KNJIGA_TABELA,koloneRezultat,where,whereArgs,groupBy,having,order);
        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
                String naziv = cursor.getString(cursor.getColumnIndex(KNJIGA_NAZIV));
                String opis = cursor.getString(cursor.getColumnIndex(KNJIGA_OPIS));
                String datum = cursor.getString(cursor.getColumnIndex(KNJIGA_DATUM));
                String id = cursor.getString(cursor.getColumnIndex(KNJIGA_IDWEB));
                String url = cursor.getString(cursor.getColumnIndex(KNJIGA_SLIKA));
                String kategorija = vratiKategorijuZaId(cursor.getLong(cursor.getColumnIndex(KNJIGA_IDKATEGORIJE)));
                int brojStranica = cursor.getInt(cursor.getColumnIndex(KNJIGA_STRANICE));
                int pregledano = cursor.getInt(cursor.getColumnIndex(KNJIGA_PREGLEDANA));
                ArrayList<Autor> autori = vratiAutoreZaKnjigu(vratiIdKnjige(naziv));
                Boolean seen = false;
                if(pregledano==1) seen=true;
                URL url1=null;

                if(id.equals("")){
                    try{
                        Uri uri = Uri.parse(url);
                        Knjiga k = new Knjiga(naziv,autori,kategorija,uri);
                        k.setKliknuto(seen);
                        rez.add(k);}
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                else{try{
                    url1 = new URL(url);
                    Knjiga k = new Knjiga(new Knjiga(id,naziv,autori,opis,datum,url1,brojStranica),kategorija,seen);
                    rez.add(k);
                }
                catch(MalformedURLException e){
                    e.printStackTrace();
                }
                }
            }
        }
        return rez;
    }

    public ArrayList<Knjiga> knjigeAutora(long idAutora){
        ArrayList<Knjiga> rez = new ArrayList<Knjiga>();

        String[] koloneRezultat = new String[]{AUTORSTVO_IDKNJIGE};
        String where = AUTORSTVO_IDAUTORA + "=" +idAutora;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(AUTORSTVO_TABELA,koloneRezultat,where,whereArgs,groupBy,having,order);

        while(cursor.moveToNext()){
            long idKnjige = cursor.getLong(cursor.getColumnIndex(AUTORSTVO_IDKNJIGE));
            String[] koloneRezultat2 = new String[]{KNJIGA_NAZIV,KNJIGA_OPIS,KNJIGA_DATUM,KNJIGA_STRANICE,KNJIGA_IDWEB,KNJIGA_IDKATEGORIJE,KNJIGA_SLIKA,KNJIGA_PREGLEDANA};
            String where2 = KNJIGA_ID + "=" +idKnjige;
            Cursor cursor2 = db.query(KNJIGA_TABELA,koloneRezultat2,where2,whereArgs,groupBy,having,order);
            cursor2.moveToFirst();
            String naziv = cursor2.getString(cursor2.getColumnIndex(KNJIGA_NAZIV));
            String opis = cursor2.getString(cursor2.getColumnIndex(KNJIGA_OPIS));
            String datum = cursor2.getString(cursor2.getColumnIndex(KNJIGA_DATUM));
            String id = cursor2.getString(cursor2.getColumnIndex(KNJIGA_IDWEB));
            String url = cursor2.getString(cursor2.getColumnIndex(KNJIGA_SLIKA));
            String kategorija = vratiKategorijuZaId(cursor2.getLong(cursor2.getColumnIndex(KNJIGA_IDKATEGORIJE)));
            int brojStranica = cursor2.getInt(cursor2.getColumnIndex(KNJIGA_STRANICE));
            int pregledano = cursor2.getInt(cursor2.getColumnIndex(KNJIGA_PREGLEDANA));
            ArrayList<Autor> autori = vratiAutoreZaKnjigu(vratiIdKnjige(naziv));
            Boolean seen = false;
            if(pregledano==1) seen=true;
            URL url1=null;

            Log.e("url",url);


            if(id.equals("")){
                try{
                    Uri uri = Uri.parse(url);
                    Knjiga k = new Knjiga(naziv,autori,kategorija,uri);
                    k.setKliknuto(seen);
                    rez.add(k);}
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            else{try{
                url1 = new URL(url);

                Knjiga k = new Knjiga(new Knjiga(id,naziv,autori,opis,datum,url1,brojStranica),kategorija,seen);
                rez.add(k);
                }
            catch(MalformedURLException e){
                e.printStackTrace();
            }
            }
            cursor2.close();
        }

        return rez;
    }

    public ArrayList<String> vratiKategorije(){

        ArrayList<String> rez = new ArrayList<String>();

        String[] koloneRezultat = new String[]{KATEGORIJA_NAZIV};
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(KATEGORIJA_TABELA,koloneRezultat,where,whereArgs,groupBy,having,order);
        int INDEX_KOLONE_NAZIV = cursor.getColumnIndex(KATEGORIJA_NAZIV);
        while(cursor.moveToNext()){
            rez.add(cursor.getString(INDEX_KOLONE_NAZIV));
        }
        cursor.close();
        db.close();

        return rez;
    }

    public ArrayList<Autor> vratiAutore(){
        ArrayList<Autor> rez = new ArrayList<Autor>();

        String[] koloneRezultat = new String[]{AUTOR_ID};
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(AUTOR_TABELA,koloneRezultat,where,whereArgs,groupBy,having,order);

        while(cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndex(AUTOR_ID));
            String ime = vratiAutoraZaId(id);
            ArrayList<String> knjige = vratiListuIdovaZaAutora(id);
            rez.add(new Autor(ime,knjige));
        }

        return rez;
    }

    public long vratiIdKategorije(String kategorija){

        kategorija = popraviString(kategorija);

        String[] koloneRezultat = new String[]{KATEGORIJA_ID};
        String where = KATEGORIJA_NAZIV + "='" +kategorija+"'";
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(KATEGORIJA_TABELA,koloneRezultat,where,whereArgs,groupBy,having,order);

        cursor.moveToFirst();
        return cursor.getLong(cursor.getColumnIndex(KATEGORIJA_ID));
    }

    public long vratiIdAutora(String imeAutora){

        imeAutora = popraviString(imeAutora);
        String[] koloneRezultat = new String[]{AUTOR_ID};
        String where = AUTOR_IME + "='" +imeAutora+"'";
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(AUTOR_TABELA,koloneRezultat,where,whereArgs,groupBy,having,order);

        cursor.moveToFirst();
        return cursor.getLong(cursor.getColumnIndex(AUTOR_ID));
    }

    public long vratiIdKnjige(String naziv){

        naziv = popraviString(naziv);
        String[] koloneRezultat = new String[]{KNJIGA_ID};
        String where = KNJIGA_NAZIV + "='" +naziv+"'";
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(KNJIGA_TABELA,koloneRezultat,where,whereArgs,groupBy,having,order);

        cursor.moveToFirst();
        return cursor.getLong(cursor.getColumnIndex(KNJIGA_ID));

    }

    public Boolean imaLiAutor(String imeAutora){

        imeAutora = popraviString(imeAutora);

        String[] koloneRezultat = new String[]{AUTOR_ID};
        String where = AUTOR_IME + "='" +imeAutora+"'";
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(AUTOR_TABELA,koloneRezultat,where,whereArgs,groupBy,having,order);
        if(cursor.getCount()==0)
            return false;
        return true;

    }

    public void oznaciKnjiguKaoProcitanu(Knjiga knjiga){

        String[] koloneRezultat = new String[]{KNJIGA_ID};
        String where = KNJIGA_NAZIV + "='" +popraviString(knjiga.getNaziv())+"'";
        String whereArgs[] = null;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues novi = new ContentValues();
        novi.put(KNJIGA_PREGLEDANA,1);
        db.update(KNJIGA_TABELA,novi,where,whereArgs);

    }

    public String vratiKategorijuZaId(long id){

        String[] koloneRezultat = new String[]{KATEGORIJA_NAZIV};
        String where = KATEGORIJA_ID + "=" +id;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(KATEGORIJA_TABELA,koloneRezultat,where,whereArgs,groupBy,having,order);

        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(KATEGORIJA_NAZIV));

    }

    public ArrayList<Autor> vratiAutoreZaKnjigu(long id){

        ArrayList<Autor> rez = new ArrayList<Autor>();

        String[] koloneRezultat = new String[]{AUTORSTVO_IDAUTORA};
        String where = AUTORSTVO_IDKNJIGE+ "=" +id;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(AUTORSTVO_TABELA,koloneRezultat,where,whereArgs,groupBy,having,order);

        while(cursor.moveToNext()){
            long idAutora = cursor.getLong(cursor.getColumnIndex(AUTORSTVO_IDAUTORA));
            String imeAutora = vratiAutoraZaId(idAutora);
            if(!imeAutora.equals("")){
            ArrayList<String> knjige = vratiListuIdovaZaAutora(idAutora);
            rez.add(new Autor(imeAutora,knjige));}
        }
        return rez;
    }

    public String vratiWebIdZaKnjigu(long id){

        SQLiteDatabase db = getWritableDatabase();

        String[] koloneRezultat = new String[]{KNJIGA_IDWEB};
        String where = KNJIGA_ID + "=" +id;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        Cursor cursor = db.query(KNJIGA_TABELA,koloneRezultat,where,whereArgs,groupBy,having,order);
        cursor.moveToFirst();

        return cursor.getString(cursor.getColumnIndex(KNJIGA_IDWEB));
    }

    public ArrayList<String> vratiListuIdovaZaAutora(long id){
        ArrayList<String> rez = new ArrayList<String>();

        String[] koloneRezultat = new String[]{AUTORSTVO_IDKNJIGE};
        String where = AUTORSTVO_IDAUTORA+ "=" +id;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(AUTORSTVO_TABELA,koloneRezultat,where,whereArgs,groupBy,having,order);

        while(cursor.moveToNext()){
            Long idd = cursor.getLong(cursor.getColumnIndex(AUTORSTVO_IDKNJIGE));
            rez.add(vratiWebIdZaKnjigu(idd));
        }
        return rez;
    }

    public String vratiAutoraZaId(long id){

        String[] koloneRezultat = new String[]{AUTOR_IME};
        String where = AUTOR_ID + "=" +id;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(AUTOR_TABELA,koloneRezultat,where,whereArgs,groupBy,having,order);
        if(cursor.getCount()>0){
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(AUTOR_IME));}
        return "";
    }



    private String popraviString(String s){
        char a = '\'';
        for(int i=0;i<s.length();i++){
            if(a==s.charAt(i)){
                s = s.substring(0,i) + "'" + s.substring(i);
                i++;
            }
        }
        return s;
    }

}
