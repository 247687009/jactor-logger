package github.com.cp149.jactor;

import java.util.ArrayDeque;
import java.util.Queue;

import org.agilewiki.jactor.JAEvent;
import org.agilewiki.jactor.api.Mailbox;
import org.agilewiki.jactor.api.Request;
import org.agilewiki.jactor.api.RequestBase;
import org.agilewiki.jactor.api.ResponseProcessor;
import org.agilewiki.jactor.api.Transport;
import org.agilewiki.jactor.lpc.JLPCActor;

public class JaSemaphore {
	   /* A mailbox is needed to handle requests.
	     */
	    private final Mailbox mailbox;

	    /**
	     * The number of available semaphores.
	     */
	    private int permits;

	    /**
	     * A queue of blocked requests.
	     */
	    private final Queue<ResponseProcessor<Void>> queue = new ArrayDeque<ResponseProcessor<Void>>();

	    /**
	     * The acquire request.
	     */
	    private  JAEvent acquire;

	    /**
	     * The release request.
	     */
	    private  JAEvent release;

	    /**
	     * Create a semaphore manager.
	     *
	     * @param mbox        The mailbox used to handle requests.
	     * @param permitCount The number of semaphores initially available.
	     */
	    public JaSemaphore(final Mailbox mbox, final int permitCount) {
	        this.mailbox = mbox;
	        this.permits = permitCount;
	        
//	        acquire = new JAEvent() {
//	           
//	            public void processRequest(
//	                    )
//	                    throws Exception {
//	                if (permits > 0) {
//	                    permits -= 1;
//	                    responseProcessor.processResponse(null);
//	                } else {
//	                    queue.offer(responseProcessor);
//	                }
//	            }
//	        };
//	        
//
//	        release = new RequestBase<Void>(mailbox) {
//	            @Override
//	            public void processRequest(
//	                    final Transport<Void> responseProcessor)
//	                    throws Exception {
//	                final ResponseProcessor<Void> rp = queue.poll();
//	                if (rp == null) {
//	                    permits += 1;
//	                } else {
//	                    rp.processResponse(null);
//	                }
//	                responseProcessor.processResponse(null);
//	            }
//	        };
	    }

	    /**
	     * A request to acquire a semaphore.
	     * The release pends until a semaphore is available.
	     *
	     * @return The request.
	     */
	    public JAEvent acquireReq() {
	        return acquire;
	    }

	    /**
	     * A request to release a request.
	     * If there is a pending request, a release will allow that request to complete.
	     *
	     * @return The request.
	     */
	    public JAEvent releaseReq() {
	        return release;
	    }

}
