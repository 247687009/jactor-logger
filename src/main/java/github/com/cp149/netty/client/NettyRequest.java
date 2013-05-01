package github.com.cp149.netty.client;

import github.com.cp149.LoggerActor;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;


public class NettyRequest extends Request<String, NettyActor> {

	public static final NettyRequest req = new NettyRequest();

	@Override
	public boolean isTargetType(Actor targetActor) {
		return targetActor instanceof LoggerActor;
	}

	@Override
	public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
		NettyActor a = (NettyActor) targetActor;
		a.doLogger();
		rp.processResponse(null);

	}
}
