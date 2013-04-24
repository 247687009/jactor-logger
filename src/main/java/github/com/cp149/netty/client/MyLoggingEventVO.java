package github.com.cp149.netty.client;

import java.io.Serializable;


public class MyLoggingEventVO implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1003788922558709787L;

	public MyLoggingEventVO() {
		super();
		
	}
	
	
   public MyLoggingEventVO(String msg,int level) {
		super();
		this.msg = msg;
		this.level=level;
	}


public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}

private String msg;
private int level;
}
