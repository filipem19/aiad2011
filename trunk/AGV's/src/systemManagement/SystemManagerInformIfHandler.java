package systemManagement;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class SystemManagerInformIfHandler extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6104164352894958372L;

	private SystemManager mySysManager;

	public SystemManagerInformIfHandler(SystemManager mySysManager) {
		super(mySysManager);
		this.mySysManager = mySysManager;
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM_IF));
		if (msg != null) {
			try {
				if (msg.getContentObject().getClass() == Location.class) {
					Location content = null;
					content = (Location) msg.getContentObject();
					mySysManager.sendAgvLocation(msg.getSender(), content);
				}
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		block();
	}

}
