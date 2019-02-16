package ba.unsa.etf.rma.samira.spirala3;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomAdapterKnjiga extends BaseAdapter {

    private Context ctx;
    private ArrayList<Knjiga> knjige;


    public CustomAdapterKnjiga(Context ctx, ArrayList<Knjiga> knjige) {
        this.ctx=ctx;
        this.knjige=knjige;
    }



    @Override
    public int getCount() {
        return knjige.size();
    }

    @Override
    public Object getItem(int i) {
        return knjige.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(ctx,R.layout.lista_knjiga_layout, null);
        TextView imeKnjige = (TextView) v.findViewById(R.id.eNaziv);
        TextView imeAutora = (TextView) v.findViewById(R.id.eAutor);
        ImageView slikaa = (ImageView) v.findViewById(R.id.eNaslovna);
        TextView opis = (TextView)v.findViewById(R.id.eOpis);
        TextView datum = (TextView)v.findViewById(R.id.eDatumObjavljivanja);
        TextView brojStr = (TextView)v.findViewById(R.id.eBrojStranica);
        Button preporuci = (Button)v.findViewById(R.id.dPreporuci);



        String opisT = knjige.get(i).getOpis();
        if(opisT.equals("")){
            opisT = v.getResources().getString(R.string.nepoznat);
        }

        String datumT = knjige.get(i).getDatumObjavljivanja();
        if(datumT.equals("")){
            datumT = v.getResources().getString(R.string.nepoznat);
        }

        String brojStrT = String.valueOf(knjige.get(i).getBrojStranica());
        if(brojStrT.equals("0")){
            brojStrT = v.getResources().getString(R.string.nepoznat);
        }
        
        opis.setText(v.getResources().getString(R.string.opis) +": "+ opisT);
        datum.setText(v.getResources().getString(R.string.datumObjavljivanja) +": "+datumT);
        brojStr.setText(v.getResources().getString(R.string.brojStranica) +": "+ brojStrT);


        imeKnjige.setText(knjige.get(i).getNaziv());

        Picasso.get().load(knjige.get(i).getSlika().toString()).into(slikaa);



        String autoriS="";
        if(knjige.get(i).getAutori().size()==0){
            autoriS="";
        }
        else{
            for(int j=0;j<knjige.get(i).getAutori().size();j++){
                if(j==knjige.get(i).getAutori().size()-1){
                    autoriS+=knjige.get(i).getAutori().get(j).getImeiPrezime();
                }
                else autoriS=autoriS+knjige.get(i).getAutori().get(j).getImeiPrezime()+",";

            }}
            if(autoriS.equals("")){
            imeAutora.setText(R.string.nemaAutora);
            }
            else{
        imeAutora.setText(autoriS);}
        if(knjige.get(i) != null && knjige.get(i).getKliknuto()==true){
            v.setBackgroundResource(R.color.elementListe);
        }



        preporuci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentPreporuci fp = new FragmentPreporuci();
                FragmentManager fm = ((Pocetna)ctx).getFragmentManager();
                Bundle knjiga = new Bundle();
                knjiga.putParcelable("knjiga",knjige.get(i));
                fp.setArguments(knjiga);

                if(Pocetna.siri == true){fm.beginTransaction().replace(R.id.mjesto1,fp).addToBackStack(null).commit();}
                else {
                fm.beginTransaction().replace(R.id.mjesto0,fp).addToBackStack(null).commit();}

            }
        });


        return v;

    }
}