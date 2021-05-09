package assignment2;


public class SolitaireCipher {
    public Deck key;

    public SolitaireCipher (Deck key) {
        this.key = new Deck(key); // deep copy of the deck
    }

    /*
     * TODO: Generates a keystream of the given size
     */
    public int[] getKeystream(int size) {

        int[] keys = new int[size];
        for(int i =0;i<size;i++){
            keys[i]= this.key.generateNextKeystreamValue();
        }
        if(size<0) {
            return null;
        }
        return  keys;
    }

    /*
     * TODO: Encodes the input message using the algorithm described in the pdf.
     */
    public String encode(String msg) {

        String adder="";
        String letterOnly="";
        String allCaps="";



        for(int i=0;i<msg.length();i++) {
            if (Character.isLetter(msg.charAt(i))) {
                letterOnly += msg.charAt(i); //extracting the letters only
            }
        }
        for(int i=0;i<letterOnly.length();i++){
            allCaps += letterOnly.toUpperCase().charAt(i); //all capitals
        }

        int[]codes = getKeystream(allCaps.length());

        for(int i =0;i<allCaps.length();i++) {
            int pos =  allCaps.charAt(i)-'A';
            int newPos = (pos + codes[i])%26;
            adder+= (char)(newPos  + 'A');
        }

        if(msg==null){
            return null;
        }
        return adder;
    }



    /*
     * TODO: Decodes the input message using the algorithm described in the pdf.
     */
    public String decode(String msg) {

        String adder="";
        int[] codes = getKeystream(msg.length());

        for(int i =0; i<msg.length();i++){
            int pos = msg.charAt(i)-'A';
            codes[i]= (26 - ((codes[i] ) % 26));
            int newPos = (pos + codes[i])%26;
            adder+= (char)(newPos  + 'A');
        }

        if(msg==null){
            return null;
        }

        return adder;

    }

}



