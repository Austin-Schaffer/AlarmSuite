package aschaffer.alarmsuite;

import java.util.Vector;

public enum Ref {
    _id(0),
    enabled(1),
    title(2),
    message(3),
    timeInMillis(4);

    private final int key;


    private Ref(final int i) {
        key = i;
    }

    public int number(){
        return key;
    }

    public static Vector<String> getAttNames(){
        Vector<String> ret = new Vector<String>();
        ret.add(_id.name());
        ret.add(enabled.name());
        ret.add(title.name());
        ret.add(message.name());
        ret.add(timeInMillis.name());

        return ret;
    }
    public int numberOfAtts(){
        return Ref.getAttNames().size();
    }
}
