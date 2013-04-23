package github.com.cp149.netty.client;

import java.io.Serializable;

import org.agilewiki.jactor.lpc.JLPCActor;
import org.jboss.netty.channel.Channel;

import ch.qos.logback.classic.net.LoggingEventPreSerializationTransformer;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.PreSerializationTransformer;

public class NettyActor extends JLPCActor{
	private ILoggingEvent eventObject;
	private Channel channel;
	private PreSerializationTransformer<ILoggingEvent> pst=new LoggingEventPreSerializationTransformer();

	

	public NettyActor(ILoggingEvent event, Channel channel) {
		super();
		this.eventObject = event;
		this.channel=channel;
	}



	public void doLogger() throws Exception {
		Serializable serEvent = pst.transform(eventObject);
		channel.write(eventObject.getLoggerContextVO());
		
	}

}
