package ba.unsa.etf.rma.samira.spirala3;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DohvatiKnjige extends AsyncTask<String, Integer, Void> {

    public interface IDohvatiKnjigeDone{
        public void onDohvatiDone(ArrayList<Knjiga> rez);
    }
    ArrayList<Knjiga> rez = new ArrayList<Knjiga>();
    private IDohvatiKnjigeDone pozivatelj;
    public DohvatiKnjige(IDohvatiKnjigeDone p){pozivatelj = p;}

    @Override
    protected Void doInBackground(String... knjige){

        for(int h=0;h<knjige.length;h++) {

            String query = null;
            try {
                query = URLEncoder.encode(knjige[h], "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String url1 = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + query + "&maxResults=5";
            try {
                URL url = new URL(url1);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String rezultat = convertStreamToString(in);
                JSONObject jo = new JSONObject(rezultat);

                if (jo.has("items")) {
                    JSONArray items = jo.getJSONArray("items");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject book = items.getJSONObject(i);
                        String id = book.getString("id");
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
                        if (zaSliku != null && zaSliku.has("thumbnail")) link = zaSliku.getString("thumbnail");
                        else link = "https://meddwl.org/wp-content/uploads/2016/10/llyfrau-300x198.png";
                        ArrayList<Autor> autoriL = new ArrayList<Autor>();
                        if (autori != null) for (int j = 0; j < autori.length(); j++) {
                            autoriL.add(new Autor(autori.get(j).toString(), id));
                        }
                        URL url2 = null;
                        if (link != null) url2 = new URL(link);
                        else url2 = null;

                        rez.add(new Knjiga(id, naziv, autoriL, opis, datum, url2, brojStranica));
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String convertStreamToString(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try{
            while((line=reader.readLine())!=null){
                sb.append(line+"\n");
            }
        }
        catch(IOException e){e.printStackTrace();}
        finally{
            try {
                is.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        pozivatelj.onDohvatiDone(rez);
    }

}