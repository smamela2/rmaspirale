package ba.unsa.etf.rma.samira.spirala3;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class KnjigeFragment extends Fragment {

    CustomAdapterKnjiga ca;
    ArrayList<Knjiga> knjige;

    Boolean aut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.knjige_fragment_layout, container, false);
        Button dugmePovratak = (Button) v.findViewById(R.id.dPovratak);
        if(Pocetna.siri==true) dugmePovratak.setVisibility(View.GONE);
        if(getArguments()!=null){
            String mode = getArguments().getString("mode");
            if(mode.equals("k")){
                String kat = getArguments().getString("kategorija");
                final ListView lista = (ListView) v.findViewById(R.id.listaKnjiga);
                if(Pocetna.siri==true) dugmePovratak.setVisibility(View.GONE);
                else dugmePovratak.setVisibility(View.VISIBLE);
                TextView nazivKategorije = (TextView) v.findViewById(R.id.nazivKategorija);
                nazivKategorije.setText(kat);
                TextView lab = (TextView) v.findViewById(R.id.Kategorijaa);
                lab.setText(R.string.labelakategorija);
                BazaOpenHelper boh = new BazaOpenHelper(getActivity(),BazaOpenHelper.IME_BAZE,null,BazaOpenHelper.VERZIJA_BAZE);
                ArrayList<Knjiga> tmp = boh.knjigeKategorije(boh.vratiIdKategorije(kat));
                ca = new CustomAdapterKnjiga(getActivity(), tmp);
                lista.setAdapter(ca);}
            else{
                String auto = getArguments().getString("autor");
                final ListView lista = (ListView) v.findViewById(R.id.listaKnjiga);
                TextView nazivKategorije = (TextView) v.findViewById(R.id.nazivKategorija);
                TextView lab = (TextView) v.findViewById(R.id.Kategorijaa);
                lab.setText(auto);
                if(Pocetna.siri==true) dugmePovratak.setVisibility(View.GONE);
                else dugmePovratak.setVisibility(View.VISIBLE);
                BazaOpenHelper boh = new BazaOpenHelper(getActivity(),BazaOpenHelper.IME_BAZE,null,BazaOpenHelper.VERZIJA_BAZE);
                ArrayList<Knjiga> tmp = boh.knjigeAutora(boh.vratiIdAutora(auto));
                Log.e("vraceneKnjige",String.valueOf(tmp.size()));
                ca = new CustomAdapterKnjiga(getActivity(),tmp);
                lista.setAdapter(ca);
            }}
        else {
            TextView nazivKategorije = (TextView) v.findViewById(R.id.nazivKategorija);
            nazivKategorije.setText("");
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);

        if(Pocetna.siri==false){
            Pocetna.knjigeFragment=true;
        }
        else Pocetna.knjigeFragment=false;

        if(getArguments()!=null) {

            String kat = getArguments().getString("kategorija");
            final ListView lista = (ListView) getView().findViewById(R.id.listaKnjiga);
            Button dugmePovratak = (Button) getView().findViewById(R.id.dPovratak);
            TextView nazivKategorije = (TextView) getView().findViewById(R.id.nazivKategorija);
            nazivKategorije.setText(kat);

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> aV, View view, int position, long id) {

                    lista.getChildAt(position-lista.getFirstVisiblePosition()).setBackgroundResource(R.color.elementListe);

                    Knjiga k = (Knjiga) ca.getItem(position);

                    BazaOpenHelper boh = new BazaOpenHelper(getActivity(),BazaOpenHelper.IME_BAZE,null,BazaOpenHelper.VERZIJA_BAZE);
                    boh.oznaciKnjiguKaoProcitanu(k);

                }
            });

            dugmePovratak.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    ListeFragment lf = new ListeFragment();
                    getFragmentManager().beginTransaction().replace(R.id.mjesto0, lf).addToBackStack(null).commit();
                }
            });


        }


    }





}
