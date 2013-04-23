package championshang.wordpress.com;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jasocket.cluster.AgentChannelManager;
import org.agilewiki.jasocket.console.ConsoleIO;
import org.agilewiki.jasocket.console.Interpret;
import org.agilewiki.jasocket.console.Interpreter;
import org.agilewiki.jasocket.console.Interrupter;
import org.agilewiki.jasocket.node.Node;

public class ConsoleApp implements ConsoleIO {

	private Node node;
	private Interpreter interpreter;
	private BufferedReader inbr;
	private String operatorName;
	private Console cons;

	public boolean hasInput() {
		try {
			return inbr.ready();
		} catch (Exception ex) {
			return false;
		}
	}

	public void create(Node node, Interrupter interrupter) throws Exception {
		AgentChannelManager agentChannelManager = node.agentChannelManager();
		System.out.println("\n*** ConsoleApp " + agentChannelManager.agentChannelManagerAddress() + " ***\n");
		cons = System.console();
		boolean authenticated = true;
		while (!authenticated) {
			operatorName = cons.readLine("name: ");
			authenticated = node.passwordAuthenticator().authenticate(operatorName, new String(cons.readPassword("password: ")), null);
			if (!authenticated)
				cons.printf("invalid\n\n");
		}
		interpreter = new Interpreter();
		interpreter.initialize(node.mailboxFactory().createAsyncMailbox());
		interpreter.configure(operatorName, "c", node, this, System.out);
		if (interrupter != null) {
			node.mailboxFactory().addClosable(interrupter);
			interrupter.activate(interpreter);
		}
		this.node = node;
		agentChannelManager.interpreters.put("c", interpreter);
		inbr = new BufferedReader(new InputStreamReader(System.in));
		JAFuture future = new JAFuture();
		boolean first = true;
		while (true) {
			String commandLine = null;
			while (commandLine == null) {
				if (!first)
					System.out.println();
				else
					first = false;
				interpreter.prompt();
				commandLine = readLine();
			}
			(new Interpret(commandLine)).send(future, interpreter);
		}
	}

	public static void main(String[] args) throws Exception {
		Node node = new Node(args, 100);
		try {
			node.process();
			(new ConsoleApp()).create(node, null);
		} catch (Exception ex) {
			node.mailboxFactory().close();
			throw ex;
		}
	}

	@Override
	public void close() {
	}

	@Override
	public void print(String s) {
		System.out.print(s);
		System.out.flush();
	}

	@Override
	public void println(String s) {
		System.out.println(s);
		System.out.flush();
	}

	@Override
	public String readLine() throws IOException {
		return inbr.readLine();
	}

	@Override
	public String readPassword() throws Exception {
		char[] p = cons.readPassword();
		if (p == null) {
			/*
			 * System.out.println(); System.out.flush();
			 */
			return null;
		}
		return new String(cons.readPassword());
	}

}
