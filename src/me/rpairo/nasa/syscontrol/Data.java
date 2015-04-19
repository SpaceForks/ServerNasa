package me.rpairo.nasa.syscontrol;

import java.io.Serializable;

public class Data implements Serializable{
	private static final long serialVersionUID = 9178463713495654837L;

    public int Action;
    public String texto;
    public boolean last_msg=false;
    public boolean encender = false;
    public boolean callback = false;
}