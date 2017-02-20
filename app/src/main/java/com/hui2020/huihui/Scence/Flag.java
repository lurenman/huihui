package com.hui2020.huihui.Scence;

/**
 * Created by FD-GHOST on 2016/12/27.
 * This is to make sure the GetScence will only be called once at each time, because the
 * scrollview listener will keep detect whether it has been scroll to bot
 */

public class Flag {
    private Boolean flag;

    public Flag(){
        flag = true;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

}
