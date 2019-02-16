package ba.unsa.etf.rma.samira.spirala3;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class ListeFragment extends Fragment {

    CustomAdapterAutor caa;
    ArrayAdapter<String> adapter;
    ArrayList<String> kat1;
    ArrayList<Autor> autori;
    Boolean autoriB = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.liste_fragment_layout, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);
        Pocetna.knjigeFragment=false;
        final Button dugmePretraga = (Button) getActivity().findViewById(R.id.dPretraga);
        Button dugmeDodajKnjigu = (Button) getActivity().findViewById(R.id.dDodajKnjigu);
        Button dugmeAutori = (Button) getActivity().findViewById(R.id.dAutori);
        Button dugmeKategorije = (Button) getActivity().findViewById(R.id.dKategorije);
        Button dugmeDodajOnline = (Button) getActivity().findViewById(R.id.dDodajOnline);
        final Button dugmeDodajKategoriju = (Button) getActivity().findViewById(R.id.dDodajKategoriju);
        final EditText pretragaKategorija = (EditText) getActivity().findViewById(R.id.tekstPretraga);
        final ListView kategorijaLista = (ListView) getActivity().findViewById(R.id.listaKategorija);
        dugmeDodajKategoriju.setEnabled(false);
        caa = new CustomAdapterAutor(getActivity(), Liste.autori);
        BazaOpenHelper boh = new BazaOpenHelper(getActivity(),BazaOpenHelper.IME_BAZE,null,BazaOpenHelper.VERZIJA_BAZE);
        kat1 = boh.vratiKategorije();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, kat1);
        kategorijaLista.setAdapter(adapter);
        autoriB=false;

        if(Pocetna.siri==true){
            FrameLayout m = (FrameLayout)getActivity().findViewById(R.id.mjesto1);
            if(m!=null){
                m.setVisibility(View.VISIBLE);
            }
        }

        dugmeDodajKnjigu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                DodavanjeKnjigeFragment dkf = (DodavanjeKnjigeFragment)getFragmentManager().findFragmentByTag("dodavanjeknjigefragment");
                if(dkf==null){
                    dkf=new DodavanjeKnjigeFragment();
                }
                getFragmentManager().beginTransaction().replace(R.id.mjesto0, dkf,"dodavanjeknjigefragment").addToBackStack(null).commit();

            }
        });

        dugmeDodajOnline.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                FragmentOnline dkf = new FragmentOnline();
                if(dkf==null){
                    dkf=new FragmentOnline();
                }
                getFragmentManager().beginTransaction().replace(R.id.mjesto0, dkf,"fragmentonline").addToBackStack(null).commit();
            }
        });

        dugmePretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BazaOpenHelper boh = new BazaOpenHelper(getActivity(),BazaOpenHelper.IME_BAZE,null,BazaOpenHelper.VERZIJA_BAZE);
                kat1 = boh.vratiKategorije();

                String kat = pretragaKategorija.getText().toString();
                if (!kat.equals("")) {
                    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, kat1);
                    adapter.getFilter().filter(kat);
                    kategorijaLista.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    boolean ima = false;
                    for (int i = 0; i < kat1.size(); i++) {
                        if (kat1.get(i).length() >= kat.length() && kat1.get(i).substring(0, kat.length()).equalsIgnoreCase(kat))
                            ima = true;
                    }
                    if (ima == true) {
                        dugmeDodajKategoriju.setEnabled(false);
                    } else {
                        dugmeDodajKategoriju.setEnabled(true);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    dugmeDodajKategoriju.setEnabled(false);
                }

            }
        });


        dugmeDodajKategoriju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BazaOpenHelper boh = new BazaOpenHelper(getActivity(),BazaOpenHelper.IME_BAZE,null,BazaOpenHelper.VERZIJA_BAZE);
                boh.dodajKategoriju(pretragaKategorija.getText().toString());
                kat1 = boh.vratiKategorije();
                dugmeDodajKategoriju.setEnabled(false);
                ArrayAdapter<String> adapter2;
                adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, kat1);
                kategorijaLista.setAdapter(adapter2);
                adapter.notifyDataSetChanged();
                pretragaKategorija.setText("");
            }
        });

        kategorijaLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> aV, View view, int position, long id) {

                if (autoriB == false) {

                    String kat = (String) aV.getItemAtPosition(position);
                    KnjigeFragment kf = new KnjigeFragment();
                    Bundle arg = new Bundle();
                    arg.putString("mode", "k");
                    arg.putString("kategorija", kat);
                    kf.setArguments(arg);
                    FragmentManager fm = getFragmentManager();
                    if (Pocetna.siri == true) {
                        fm.beginTransaction().replace(R.id.mjesto1, kf).addToBackStack(null).commit();
                    } else {
                        fm.beginTransaction().replace(R.id.mjesto0, kf).addToBackStack(null).commit();
                    }
                } else {
                    Autor aut = (Autor) aV.getItemAtPosition(position);
                    KnjigeFragment kf = new KnjigeFragment();
                    Bundle arg = new Bundle();
                    arg.putString("mode", "a");
                    arg.putString("autor", aut.getImeiPrezime());
                    kf.setArguments(arg);
                    FragmentManager fm = getFragmentManager();
                    if (Pocetna.siri == true) {
                        fm.beginTransaction().replace(R.id.mjesto1, kf).addToBackStack(null).commit();
                    } else {
                        fm.beginTransaction().replace(R.id.mjesto0, kf).addToBackStack(null).commit();
                    }

                }

            }
        });
        dugmeAutori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dugmeDodajKategoriju.setVisibility(View.GONE);
                dugmePretraga.setVisibility(View.GONE);
                pretragaKategorija.setVisibility(View.GONE);

                BazaOpenHelper boh = new BazaOpenHelper(getActivity(),BazaOpenHelper.IME_BAZE,null,BazaOpenHelper.VERZIJA_BAZE);
                autori = boh.vratiAutore();

                caa = new CustomAdapterAutor(getActivity(),autori);
                kategorijaLista.setAdapter(caa);

                autoriB = true;

            }
        });

        dugmeKategorije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dugmeDodajKategoriju.setVisibility(View.VISIBLE);
                dugmePretraga.setVisibility(View.VISIBLE);
                pretragaKategorija.setVisibility(View.VISIBLE);

                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, kat1);
                kategorijaLista.setAdapter(adapter);

                autoriB = false;

            }
        });





    }
}
