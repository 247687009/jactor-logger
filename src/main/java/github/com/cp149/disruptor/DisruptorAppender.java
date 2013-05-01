package github.com.cp149.disruptor;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.spi.AppenderAttachable;
import ch.qos.logback.core.spi.AppenderAttachableImpl;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorAppender extends UnsynchronizedAppenderBase<ILoggingEvent> implements AppenderAttachable<ILoggingEvent> {
	private final class LogEventHandler implements EventHandler<ValueEvent> {
		// event will eventually be recycled by the Disruptor after it wraps
		public void onEvent(final ValueEvent event, final long sequence, final boolean endOfBatch) throws Exception {
			aai.appendLoopOnAppenders(event.getEvent());
			event.setEvent(null);
		}
	}

	AppenderAttachableImpl<ILoggingEvent> aai = new AppenderAttachableImpl<ILoggingEvent>();
	ExecutorService exec = Executors.newCachedThreadPool();
	// Preallocate RingBuffer with 1024 ILoggingEvents
	Disruptor<ValueEvent> disruptor = new Disruptor<ValueEvent>(DisruptorAppender.EVENT_FACTORY, 1024, exec);
	final EventHandler<ValueEvent> handler = new LogEventHandler();

	public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>() {
		public ValueEvent newInstance() {
			return new ValueEvent();
		}
	};
	private RingBuffer<ValueEvent> ringBuffer;
	 boolean includeCallerData = true;
	public void addAppender(Appender<ILoggingEvent> newAppender) {

		aai.addAppender(newAppender);

	}

	public Iterator<Appender<ILoggingEvent>> iteratorForAppenders() {
		return aai.iteratorForAppenders();
	}

	public Appender<ILoggingEvent> getAppender(String name) {
		return aai.getAppender(name);
	}

	public boolean isAttached(Appender<ILoggingEvent> appender) {
		return aai.isAttached(appender);
	}

	public void detachAndStopAllAppenders() {
		aai.detachAndStopAllAppenders();

	}

	public boolean detachAppender(Appender<ILoggingEvent> appender) {
		return aai.detachAppender(appender);
	}

	public boolean detachAppender(String name) {
		return aai.detachAppender(name);
	}

	@Override
	public void start() {

		super.start();
		// Build dependency graph
		disruptor.handleEventsWith(handler);
		ringBuffer = disruptor.start();

	}

	@Override
	public void stop() {
		disruptor.shutdown();
		exec.shutdown();
		detachAndStopAllAppenders();
		super.stop();
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		try {
			if(includeCallerData){
			 eventObject.prepareForDeferredProcessing();
			 eventObject.getCallerData();}
			long seq = ringBuffer.next();
			ValueEvent valueEvent = ringBuffer.get(seq);
			valueEvent.setEvent(eventObject);
			ringBuffer.publish(seq);
		} catch (Exception e) {
			addError(e.getMessage());
		}

	}

	public boolean isIncludeCallerData() {
		return includeCallerData;
	}

	public void setIncludeCallerData(boolean includeCallerData) {
		this.includeCallerData = includeCallerData;
	}

}
