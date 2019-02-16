package ba.unsa.etf.rma.samira.spirala3;

import java.util.ArrayList;

    public class Autor {

        private String imeiPrezime;
        private ArrayList<String> knjige = new ArrayList<String>();

        public Autor(String imeiPrezime, String id){
            this.imeiPrezime = imeiPrezime;
            knjige.add(id);
        }

        public Autor(String imeiPrezime, ArrayList<String> knjige){
            this.imeiPrezime = imeiPrezime;
            this.knjige = knjige;
        }

        public String getImeiPrezime() {
            return imeiPrezime;
        }

        public void setImeiPrezime(String imeiPrezime) {
            this.imeiPrezime = imeiPrezime;
        }

        public void setKnjige(ArrayList<String> knjige) {
            this.knjige = knjige;
        }

        public ArrayList<String> getKnjige(){
            return knjige;
        }

        public void dodajKnjigu(String id){

            for(int i=0;i<knjige.size();i++){
                if(knjige.get(i).equalsIgnoreCase(id))
                    return;
            }
            knjige.add(id);
        }
}
