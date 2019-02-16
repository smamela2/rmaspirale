package ba.unsa.etf.rma.samira.spirala3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterAutor extends BaseAdapter {

    private Context ctx;
    private ArrayList<Autor> autori;


    public CustomAdapterAutor(Context ctx, ArrayList<Autor> autori) {
        this.ctx=ctx;
        this.autori=autori;
    }

    @Override
    public int getCount() {
        return autori.size();
    }

    @Override
    public Object getItem(int i) {
        return autori.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(ctx,R.layout.lista_autor_layout, null);
        TextView imeAutora = (TextView) v.findViewById(R.id.autorIme);
        TextView brojKnjiga = (TextView) v.findViewById(R.id.brojKnjiga);

        int broj=0;

        imeAutora.setText(autori.get(i).getImeiPrezime());

        brojKnjiga.setText(String.valueOf(autori.get(i).getKnjige().size()));

        return v;

    }
}