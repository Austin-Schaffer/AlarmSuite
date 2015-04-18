package aschaffer.alarmsuite;

/**
 * Created by Me on 4/18/2015.
 */
public class AlarmListItem {

    private String itemTitle;

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public AlarmListItem(String title){
        this.itemTitle = title;
    }
}
