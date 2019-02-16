package ba.unsa.etf.rma.samira.spirala3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomAdapterKontakti extends BaseAdapter {

    private Context ctx;
    private ArrayList<Kontakt> kontakti;


    public CustomAdapterKontakti(Context ctx, ArrayList<Kontakt> kontakti) {
        this.ctx=ctx;
        this.kontakti=kontakti;
    }

    @Override
    public int getCount() {
        return kontakti.size();
    }

    @Override
    public Object getItem(int i) {
        return kontakti.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = View.inflate(ctx,R.layout.liste_element_layout, null);
        TextView ime = (TextView)v.findViewById(R.id.imeKontakta);
        TextView email = (TextView)v.findViewById(R.id.emailKontakta);

        ime.setText(kontakti.get(i).getIme());
        email.setText(kontakti.get(i).getEmail());

        return v;
    }
}