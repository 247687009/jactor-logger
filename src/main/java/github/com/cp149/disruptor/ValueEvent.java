package github.com.cp149.disruptor;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class  ValueEvent{
	
	private ILoggingEvent event;

	public ILoggingEvent getEvent() {
		return event;
	}

	public void setEvent(ILoggingEvent event) {
		this.event = event;
	}

	
	
	
}