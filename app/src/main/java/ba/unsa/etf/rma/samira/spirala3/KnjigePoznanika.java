package ba.unsa.etf.rma.samira.spirala3;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class KnjigePoznanika extends IntentService {

    public static int STATUS_START = 0;
    public static int STATUS_FINISH = 1;
    public static int STATUS_ERROR = 2;

    ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();

    public KnjigePoznanika() {
        super("");
    }

    public KnjigePoznanika(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String id = intent.getStringExtra("id");

        String url1 = "https://www.googleapis.com/books/v1/users/" + id + "/bookshelves";
        ArrayList<String> idovi = new ArrayList<String>();

        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);
            JSONObject jo = new JSONObject(rezultat);


            JSONArray items = jo.getJSONArray("items");
            for (int j = 0; j < items.length(); j++) {
                JSONObject bs = items.getJSONObject(j);
                String idbs;
                if (bs.has("id")) idbs = bs.getString("id");
                else idbs = "";
                Log.e("id",idbs);
                if(!idbs.equals(""))
                idovi.add(idbs);
            }
        } catch (MalformedURLException e) {
            receiver.send(KnjigePoznanika.STATUS_ERROR, Bundle.EMPTY);
        } catch (IOException e) {
            receiver.send(KnjigePoznanika.STATUS_ERROR, Bundle.EMPTY);
        } catch (JSONException e) {
            receiver.send(KnjigePoznanika.STATUS_ERROR, Bundle.EMPTY);
        }

        for (int k = 0; k < idovi.size(); k++) {

            String url3 = "https://www.googleapis.com/books/v1/users/" + id + "/bookshelves/" + idovi.get(k) + "/volumes";
            try {
                URL url = new URL(url3);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String rezultat = convertStreamToString(in);
                JSONObject jo = new JSONObject(rezultat);
                JSONArray items = jo.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject book = items.getJSONObject(i);
                    String idd = book.getString("id");
                    JSONObject vI = book.getJSONObject("volumeInfo");

                    String naziv = vI.getString("title");
                    JSONArray autori;
                    if (vI.has("authors")) {
                        autori = vI.getJSONArray("authors");
                    } else autori = null;
                    String opis;
                    if (vI.has("description")) opis = vI.getString("description");
                    else opis = "";

                    String datum;
                    if (vI.has("publishedDate")) datum = vI.getString("publishedDate");
                    else datum = "";

                    int brojStranica;
                    if (vI.has("pageCount")) brojStranica = vI.getInt("pageCount");
                    else brojStranica = 0;

                    JSONObject zaSliku;
                    if (vI.has("imageLinks")) zaSliku = vI.getJSONObject("imageLinks");
                    else zaSliku = null;
                    String link;
                    if (zaSliku != null && zaSliku.has("thumbnail"))
                        link = zaSliku.getString("thumbnail");
                    else link = "https://meddwl.org/wp-content/uploads/2016/10/llyfrau-300x198.png";
                    ArrayList<Autor> autoriL = new ArrayList<Autor>();
                    if (autori != null) for (int j = 0; j < autori.length(); j++) {
                        autoriL.add(new Autor(autori.get(j).toString(), id));
                    }
                    URL url2 = null;
                    if (link != null) url2 = new URL(link);
                    else url2 = null;

                    knjige.add(new Knjiga(idd, naziv, autoriL, opis, datum, url2, brojStranica));
                }
            }
            catch (MalformedURLException e) {
                receiver.send(KnjigePoznanika.STATUS_ERROR, Bundle.EMPTY);
            } catch (IOException e) {
                receiver.send(KnjigePoznanika.STATUS_ERROR, Bundle.EMPTY);
            } catch (JSONException e) {
                receiver.send(KnjigePoznanika.STATUS_ERROR, Bundle.EMPTY);
                e.printStackTrace();
            }

        }

        Bundle b = new Bundle();
        b.putParcelableArrayList("knjige",knjige);
        receiver.send(KnjigePoznanika.STATUS_FINISH,b);
    }

        public String convertStreamToString (InputStream is){
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

}