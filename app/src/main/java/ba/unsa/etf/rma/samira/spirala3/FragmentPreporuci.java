package ba.unsa.etf.rma.samira.spirala3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObservable;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FragmentPreporuci extends Fragment{

    static ArrayList<Kontakt> kontakti = new ArrayList<Kontakt>();

    Knjiga knjiga;

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_preporuci_layout,container,false);

        Spinner kontaktiS = (Spinner)v.findViewById(R.id.sKontakti);
        knjiga=getArguments().getParcelable("knjiga");

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
           requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},1);
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED)

        {
            ContentResolver cr = getActivity().getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        kontakti.add(new Kontakt(name, email));
                    }
                    emailCur.close();
                }

            }
        }

        TextView naslov = (TextView)v.findViewById(R.id.podaciNaslov);
        TextView stranice = (TextView)v.findViewById(R.id.podaciStrane);
        TextView opis = (TextView)v.findViewById(R.id.podaciOpis);
        TextView autori = (TextView)v.findViewById(R.id.podaciAutori);

        TextView datum = (TextView)v.findViewById(R.id.podaciDatum);
        ImageView sl = (ImageView)v.findViewById(R.id.podaciSlika);
        sl.setImageBitmap(knjiga.getSlika1());

        Picasso.get().load(knjiga.getSlika().toString()).into(sl);

        String autoriTekst = "";
        for(int i=0;i<knjiga.getAutori().size();i++){
            autoriTekst = autoriTekst + knjiga.getAutori().get(i).getImeiPrezime()+"\n";
        }

        if(autoriTekst.equals("")){
            autoriTekst = v.getResources().getString(R.string.nemaAutora);
        }

        String opiscic = knjiga.getOpis();
        if(opiscic.equals("")){
            opiscic=v.getResources().getString(R.string.nepoznat);
        }

        String datumcic = knjiga.getDatumObjavljivanja();
        if(datumcic.equals("")){
            datumcic=v.getResources().getString(R.string.nepoznat);
        }

        String str = String.valueOf(knjiga.getBrojStranica());
        if(str.equals("0")){
            str=v.getResources().getString(R.string.nepoznat);
        }
        naslov.setText(knjiga.getNaziv());
        autori.setText(autoriTekst);
        opis.setText(opiscic);
        stranice.setText(str);
        datum.setText(datumcic);


        CustomAdapterKontakti cak = new CustomAdapterKontakti(getActivity(),kontakti);
        kontaktiS.setAdapter(cak);
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ContentResolver cr = getActivity().getContentResolver();
                    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
                    if (cur.getCount() > 0) {
                        while (cur.moveToNext()) {
                            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                            String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                            Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id},null);
                            while (emailCur.moveToNext()) {
                                String email = emailCur.getString( emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                                kontakti.add(new Kontakt(name,email));
                            }
                            emailCur.close();
                        }

                    }
                    Spinner kontaktiS = (Spinner)getActivity().findViewById(R.id.sKontakti);
                    CustomAdapterKontakti cak = new CustomAdapterKontakti(getActivity(),kontakti);
                    kontaktiS.setAdapter(cak);


                } else {
                }
                return;
            }

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);
        final Spinner kontaktiS = (Spinner)getActivity().findViewById(R.id.sKontakti);

        Button dugmePosalji = (Button)getActivity().findViewById(R.id.dPosalji);
        dugmePosalji.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(kontakti.size()>0) {

                    Kontakt k = (Kontakt) kontaktiS.getSelectedItem();
                    String email = k.getEmail();
                    String aut = "";
                    for (int i = 0; i < knjiga.getAutori().size(); i++) {
                        if (i == knjiga.getAutori().size() - 1) {
                            aut += knjiga.getAutori().get(i).getImeiPrezime();
                        } else {
                            aut = aut + knjiga.getAutori().get(i).getImeiPrezime() + ",";
                        }
                    }
                    String[] TO = {email};
                    String[] CC = {""};
                    String tekst = "Zdravo " + k.getIme() + ",\n" + "Pročitaj knjigu " + knjiga.getNaziv() + " od " + aut + "!";
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Knjiga");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, tekst);
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        getActivity().finish();
                    } catch (android.content.ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast msg = Toast.makeText(getActivity(), R.string.nemaKontakata, Toast.LENGTH_SHORT);
                    msg.show();
                }

            }
        });



    }
}
