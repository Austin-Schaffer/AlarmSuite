package aschaffer.alarmsuite;

public class Attribute {
    //TODO: figure out id system
    public int id;
    public String title = "Default";
    public Object value = -1;

    public Attribute(Ref ref){
        title = ref.name();
    }

    public Attribute(Ref ref, Object val){
        title = new String(ref.name());
        value = new Object();
        value = val;
    }
}
