package ba.unsa.etf.rma.samira.spirala3;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.facebook.stetho.Stetho;

public class Pocetna extends AppCompatActivity {

    static public Boolean siri = false;
    static public Boolean knjigeFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_pocetna);
        FragmentManager fm = getFragmentManager();


        FrameLayout kff = (FrameLayout) findViewById(R.id.mjesto1);
        if (kff != null) {
            siri = true;
        } else siri = false;
        if (siri == true) {
            KnjigeFragment kf = (KnjigeFragment) fm.findFragmentByTag("knjigefragment");
            if (kf == null) {
                kf = new KnjigeFragment();
                fm.beginTransaction().replace(R.id.mjesto1, kf, "knjigefragment").commit();
            }

        }

        fm.beginTransaction().remove(fm.findFragmentById(R.id.mjesto0));
        ListeFragment lf = (ListeFragment) fm.findFragmentByTag("listefragment");
        if (lf == null) {
            lf = new ListeFragment();
            fm.beginTransaction().replace(R.id.mjesto0, lf, "listefragment").commit();
        } else {
            if (Pocetna.knjigeFragment == true) {
                lf = new ListeFragment();
                fm.beginTransaction().replace(R.id.mjesto0, lf, "listefragment").addToBackStack(null).commit();
            }

        }

    }

}

