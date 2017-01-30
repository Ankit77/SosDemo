package app.sosdemo.model;

/**
 * Created by indianic on 30/01/17.
 */

public class ActionModel {
    private String Caption;
    private String Action;

    public ActionModel(String caption, String action) {
        Caption = caption;
        Action = action;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }
}
