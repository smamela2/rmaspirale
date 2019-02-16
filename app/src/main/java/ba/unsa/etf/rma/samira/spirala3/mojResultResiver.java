package ba.unsa.etf.rma.samira.spirala3;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class mojResultResiver  extends ResultReceiver{

    private Receiver mReceiver;

    public mojResultResiver(Handler handler){super(handler);}

    public void setReceiver(Receiver mReceiver) {this.mReceiver = mReceiver;}

    public interface Receiver{
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData){
        if(mReceiver!=null){
            mReceiver.onReceiveResult(resultCode,resultData );
        }
    }




}
