package app.sosdemo.model;

/**
 * Created by indianic on 30/01/17.
 */

public class ActionModel {
    private String Code;
    private String Caption;
    private String Action;

    public ActionModel(String code, String caption, String action) {
        Caption = caption;
        Action = action;
        Code = code;
    }

    public ActionModel() {
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        this.Code = code;
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
