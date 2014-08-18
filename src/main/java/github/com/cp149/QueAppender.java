package github.com.cp149;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class QueAppender extends BaseAppender {
	private LinkedBlockingQueue<ILoggingEvent> storeCache = new LinkedBlockingQueue<ILoggingEvent>( );
	ExecutorService exec = Executors.newSingleThreadExecutor();

	@Override
	protected void append(ILoggingEvent eventObject) {
		if (includeCallerData) {
			eventObject.prepareForDeferredProcessing();
			eventObject.getCallerData();
		}
		storeCache.add(eventObject);

	}

	@Override
	public void start() {
		exec.execute(new Runnable() {

			@Override
			public void run() {
				while (isStarted()) {
					try {
						aai.appendLoopOnAppenders(storeCache.take());
					} catch (InterruptedException e) {

					}
				}

			}
		});
		super.start();
	}

	@Override
	public void stop() {
		while (!storeCache.isEmpty()) {
			ILoggingEvent event = storeCache.poll();
			if (event != null)
				aai.appendLoopOnAppenders(event);

		}
		super.stop();
	}

}
