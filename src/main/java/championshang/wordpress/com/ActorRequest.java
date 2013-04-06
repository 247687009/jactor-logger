package championshang.wordpress.com;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

public class ActorRequest extends Request<String, LoggerActor> {

	public static final ActorRequest req = new ActorRequest();

	@Override
	public boolean isTargetType(Actor targetActor) {
		return targetActor instanceof LoggerActor;
	}

	@Override
	public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
		LoggerActor a = (LoggerActor) targetActor;
		a.doLogger();
		rp.processResponse(null);

	}

}
