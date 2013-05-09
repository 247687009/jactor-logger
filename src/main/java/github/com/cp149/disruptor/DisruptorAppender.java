package github.com.cp149.disruptor;

import github.com.cp149.BaseAppender;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ch.qos.logback.classic.spi.ILoggingEvent;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorAppender extends BaseAppender {
	private final class LogEventHandler implements EventHandler<ValueEvent> {
		// event will eventually be recycled by the Disruptor after it wraps
		public void onEvent(final ValueEvent event, final long sequence, final boolean endOfBatch) throws Exception {
			aai.appendLoopOnAppenders(event.getEvent());
			event.setEvent(null);
		}
	}

	
	ExecutorService exec = Executors.newFixedThreadPool(1);
	// Preallocate RingBuffer with 1024 ILoggingEvents
	Disruptor<ValueEvent> disruptor = new Disruptor<ValueEvent>(DisruptorAppender.EVENT_FACTORY, 1024, exec);

	final EventHandler<ValueEvent> handler = new LogEventHandler();

	public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>() {
		public ValueEvent newInstance() {
			return new ValueEvent();
		}
	};
	private RingBuffer<ValueEvent> ringBuffer;

	@Override
	public void start() {

		super.start();
		// Build dependency graph
		disruptor.handleEventsWith(handler);
		ringBuffer = disruptor.start();

	}

	@Override
	public void stop() {
		if (!isStarted())
			return;

		this.started = false;
		disruptor.shutdown();
		exec.shutdown();
		detachAndStopAllAppenders();
		super.stop();
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		try {
			if (includeCallerData) {
				eventObject.prepareForDeferredProcessing();
				eventObject.getCallerData();
			}
			long seq = ringBuffer.next();
			ValueEvent valueEvent = ringBuffer.get(seq);
			valueEvent.setEvent(eventObject);
			ringBuffer.publish(seq);
		} catch (Exception e) {
			addError(e.getMessage());
		}

	}

}
