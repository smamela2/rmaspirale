package ba.unsa.etf.rma.samira.spirala3;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

public class DodavanjeKnjigeFragment extends Fragment {

    static public Bitmap slikica;
    static public Uri slikaZaBazu;
    static public ArrayList<String> kat1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.dodavanje_knjige_fragment_layout,container,false);

        ImageView s = (ImageView) v.findViewById(R.id.naslovnaStr);
        if(slikica!=null)
            s.setImageBitmap(slikica);
        else
                s.setImageResource(R.drawable.ic_launcher_background);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);
        Pocetna.knjigeFragment=false;
        FrameLayout m = (FrameLayout)getActivity().findViewById(R.id.mjesto1);
        if(Pocetna.siri==true && m!=null){
            m.setVisibility(View.GONE);
        }

        Button dugmePonisti = (Button) getView().findViewById(R.id.dPonisti);
        Button dugmeUpisiKnjigu = (Button) getView().findViewById(R.id.dUpisiKnjigu);
        Button dugmeNadjiSliku = (Button) getView().findViewById(R.id.dNadjiSliku);
        final ImageView slikaKnjige = (ImageView) getView().findViewById(R.id.naslovnaStr);
        final EditText imePisca = (EditText) getView().findViewById(R.id.imeAutora);
        final EditText imeKnjige = (EditText) getView().findViewById(R.id.nazivKnjige);
        final Spinner kategorijaKnjige = (Spinner) getView().findViewById(R.id.sKategorijaKnjige);

        BazaOpenHelper boh = new BazaOpenHelper(getActivity(),BazaOpenHelper.IME_BAZE,null,BazaOpenHelper.VERZIJA_BAZE);
        kat1 = boh.vratiKategorije();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,kat1);
        kategorijaKnjige.setAdapter(adapter);

        dugmeNadjiSliku.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent ucitajSliku = new Intent();
                ucitajSliku.setAction(Intent.ACTION_GET_CONTENT);
                ucitajSliku.setType("image/*");
                if(ucitajSliku.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivityForResult(ucitajSliku,0);}
            }
        });

        dugmeUpisiKnjigu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(kat1.size()==0){
                    Toast msg3 = Toast.makeText(getActivity(), R.string.nemaKategorija, Toast.LENGTH_LONG);
                    msg3.show();
                }
                else {
                    String kategorija = kategorijaKnjige.getSelectedItem().toString();
                    String autorIme = imePisca.getText().toString();
                    String knjigaNaziv = imeKnjige.getText().toString();

                    if (kategorija.equals("") || autorIme.equals("") || knjigaNaziv.equals("") || slikica == null) {
                        Toast msg = Toast.makeText(getActivity(), R.string.falePodaci, Toast.LENGTH_SHORT);
                        msg.show();
                    } else {
                        ArrayList<Autor> aut = new ArrayList<Autor>();
                        aut.add(new Autor(autorIme,""));
                        BazaOpenHelper boh = new BazaOpenHelper(getActivity(),BazaOpenHelper.IME_BAZE,null,BazaOpenHelper.VERZIJA_BAZE);
                        boh.dodajKnjigu(new Knjiga(knjigaNaziv,aut,kategorija,slikaZaBazu));
                        imePisca.setText("");
                        imeKnjige.setText("");
                        slikaKnjige.setImageResource(R.drawable.ic_launcher_background);
                        slikica = null;
                        slikaZaBazu=null;
                        Toast msg = Toast.makeText(getActivity(), R.string.uspjesnoDodana, Toast.LENGTH_SHORT);
                        msg.show();
                    }
                }
            }

        });

        dugmePonisti.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                slikica=null;
                ListeFragment kf = new ListeFragment();
                getFragmentManager().beginTransaction().replace(R.id.mjesto0,kf).commit();
            }
        });



    }






    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        try{
            switch(requestCode) {
                case 0:
                    if (resultCode == Activity.RESULT_OK) {
                        EditText imeKnjigee = (EditText) getActivity().findViewById(R.id.nazivKnjige);
                        ImageView slikaKnjigee = (ImageView) getActivity().findViewById(R.id.naslovnaStr);
                        slikaZaBazu = data.getData();
                        slikica = getBitmapFromUri(data.getData());
                        slikaKnjigee.setImageBitmap(slikica);
                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                    }
                    break;
            }
        }
        catch(Exception e){
            Log.e("Izuzetak:",e.getMessage());
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

}
