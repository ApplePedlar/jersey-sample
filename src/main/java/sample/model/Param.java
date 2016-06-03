package sample.model;

import javax.ws.rs.FormParam;

/**
 * Created by yoan on 2016/06/03.
 */
public class Param {
    @FormParam("btnName")
    private String btnName;
    public void setBtnName(String btnName) {
        this.btnName = btnName;
    }

    public String getBtnName() {
        return btnName;
    }
}

