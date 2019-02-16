package ba.unsa.etf.rma.samira.spirala3;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentOnline extends Fragment implements DohvatiKnjige.IDohvatiKnjigeDone, DohvatiNajnovije.IDohvatiNajnovijeDone,mojResultResiver.Receiver{

    ArrayAdapter<String> adapter;
    CustomAdapterSpinner cas;
    static ArrayList<Knjiga> lista = new ArrayList<Knjiga>();
    ArrayList<String> kat1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_online_layout,container,false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        BazaOpenHelper boh = new BazaOpenHelper(getActivity(),BazaOpenHelper.IME_BAZE,null,BazaOpenHelper.VERZIJA_BAZE);
        kat1 = boh.vratiKategorije();
        Spinner kat = (Spinner) v.findViewById(R.id.sKategorije);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,kat1);
        Spinner rez = (Spinner) v.findViewById(R.id.sRezultat);
        cas = new CustomAdapterSpinner(getActivity(),lista);
        rez.setAdapter(cas);
        kat.setAdapter(adapter);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Pocetna.knjigeFragment=false;
        Button povratakD = (Button) getActivity().findViewById(R.id.dPovratak);
        Button dug = (Button) getActivity().findViewById(R.id.dRun);
        Button dodajDugme = (Button) getActivity().findViewById(R.id.dAdd);

        if(Pocetna.siri==true){
            FrameLayout m = (FrameLayout)getActivity().findViewById(R.id.mjesto1);
            if(m!=null)
            m.setVisibility(View.GONE);
        }

        povratakD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lista.clear();
                ListeFragment lf = new ListeFragment();
                getFragmentManager().beginTransaction().replace(R.id.mjesto0,lf).addToBackStack(null).commit();

            }
        });

        dodajDugme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Spinner knj = (Spinner) getActivity().findViewById(R.id.sRezultat);
                Spinner ka = (Spinner) getActivity().findViewById(R.id.sKategorije);
                if (kat1.size() == 0) {
                    Toast msg = Toast.makeText(getActivity(), R.string.nemaKategorija, Toast.LENGTH_SHORT);
                    msg.show();
                }
                else if (lista.size()==0){
                    Toast msg = Toast.makeText(getActivity(), R.string.nemaKnjiga, Toast.LENGTH_SHORT);
                    msg.show();
                }
                else {
                    for (int i = 0; i < lista.size(); i++) {
                        if (lista.get(i).equals(knj.getSelectedItem())) {
                            Knjiga knjigica = new Knjiga(lista.get(i), ka.getSelectedItem().toString());
                            Liste.knjige.add(knjigica);
                            BazaOpenHelper boh = new BazaOpenHelper(getActivity(),BazaOpenHelper.IME_BAZE,null,BazaOpenHelper.VERZIJA_BAZE);
                            boh.dodajKnjigu(knjigica);
                            Toast msg = Toast.makeText(getActivity(), R.string.uspjesnoDodana, Toast.LENGTH_SHORT);
                            msg.show();
                            for (int j = 0; j < knjigica.getAutori().size(); j++) {

                                Boolean ima = false;
                                for (int k = 0; k < Liste.autori.size(); k++) {
                                    if (knjigica.getAutori().get(j).getImeiPrezime().equals(Liste.autori.get(k).getImeiPrezime())) {
                                        ima = true;
                                        Liste.autori.get(k).dodajKnjigu(knjigica.getNaziv());
                                    }
                                }
                                if (ima == false) {
                                    Liste.autori.add(new Autor(knjigica.getAutori().get(j).getImeiPrezime(), knjigica.getNaziv()));
                                }
                            }
                        }
                    }

                }
            }
        });



        dug.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                lista.clear();
                EditText e = (EditText)getActivity().findViewById(R.id.tekstUpit);
                String upit = e.getText().toString();
                if(upit.length()>=6 && upit.substring(0,6).equals("autor:")){
                    String upit2 = upit.substring(6);
                    new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone)FragmentOnline.this).execute(upit2);
                }
                else if(upit.contains(";")){
                    String[] upiti = upit.split(";");

                    new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone)FragmentOnline.this).execute(upiti);

                }
                else if(upit.length()>=9 && upit.substring(0,9).equals("korisnik:")){
                    String upit2 = upit.substring(9);
                    Intent i = new Intent(Intent.ACTION_SYNC,null,getActivity(),KnjigePoznanika.class);
                    mojResultResiver mr = new mojResultResiver(new Handler());
                    mr.setReceiver(FragmentOnline.this);
                    i.putExtra("id",upit2);
                    i.putExtra("receiver",mr);
                    mr.send(KnjigePoznanika.STATUS_START, Bundle.EMPTY);
                    getActivity().startService(i);
                }
                else new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone)FragmentOnline.this).execute(upit);
            }
        });



    }

    public Boolean imaAutor(String autor){
        for(int i=0;i<Liste.autori.size();i++){
            if(Liste.autori.get(i).getImeiPrezime().equals(autor))
                return true;
        }
        return false;
    }

    @Override
    public void onDohvatiDone(ArrayList<Knjiga> rez){
        Spinner rezultat = (Spinner)getActivity().findViewById(R.id.sRezultat);
        lista=rez;
        cas = new CustomAdapterSpinner(getActivity(),lista);
        if(rezultat!=null){
        rezultat.setAdapter(cas);}
        else {
            lista.clear();
            cas=new CustomAdapterSpinner(getActivity(),lista);

            rezultat.setAdapter(cas);
            Toast msg = Toast.makeText(getActivity(),R.string.nemarezultata, Toast.LENGTH_SHORT);
            msg.show();
        }
    }

    @Override
    public void onNajnovijeDone(ArrayList<Knjiga> rez) {
        lista=rez;
        Spinner rezultat = (Spinner)getActivity().findViewById(R.id.sRezultat);
        if(rez.size()!=0){
        cas = new CustomAdapterSpinner(getActivity(),rez);
        if(rezultat!=null)
        rezultat.setAdapter(cas);}
        else {
            lista.clear();
            cas=new CustomAdapterSpinner(getActivity(),lista);
            rezultat.setAdapter(cas);
            Toast msg = Toast.makeText(getActivity(),R.string.nemarezultata, Toast.LENGTH_SHORT);
            msg.show();
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData){

        if(resultCode==KnjigePoznanika.STATUS_FINISH){
            lista = resultData.getParcelableArrayList("knjige");
            Spinner rezultat = (Spinner)getActivity().findViewById(R.id.sRezultat);
            cas = new CustomAdapterSpinner(getActivity(),lista);
            if(rezultat!=null)
            rezultat.setAdapter(cas);
            if(lista.size()==0){
                lista.clear();
                cas=new CustomAdapterSpinner(getActivity(),lista);
                rezultat.setAdapter(cas);
                Toast msg = Toast.makeText(getActivity(),R.string.nemarezultata, Toast.LENGTH_SHORT);
                msg.show();
            }
        }
    }
}
