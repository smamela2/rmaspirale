package ba.unsa.etf.rma.samira.spirala3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterSpinner extends BaseAdapter {

    private Context ctx;
    private ArrayList<Knjiga> knjige;


    public CustomAdapterSpinner(Context ctx, ArrayList<Knjiga> knjige) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(ctx,R.layout.knjige_spinner_layout, null);

        TextView ime = (TextView) v.findViewById(R.id.lImeKnjige);
        TextView autori = (TextView) v.findViewById(R.id.lAutori);

        ime.setText(knjige.get(i).getNaziv());
        String autoriS="";
        if(knjige.get(i).getAutori().size()==0){
            autoriS="Nema autora";
        }
        else{
            for(int j=0;j<knjige.get(i).getAutori().size();j++){
                if(j==knjige.get(i).getAutori().size()-1){
                    autoriS+=knjige.get(i).getAutori().get(j).getImeiPrezime();
                }
                else autoriS=autoriS+knjige.get(i).getAutori().get(j).getImeiPrezime()+",";

            }}

        autori.setText(autoriS);
        return v;

    }
}