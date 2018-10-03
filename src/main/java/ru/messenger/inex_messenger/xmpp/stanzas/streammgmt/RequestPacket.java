package ru.messenger.inex_messenger.xmpp.stanzas.streammgmt;

import ru.messenger.inex_messenger.xmpp.stanzas.AbstractStanza;

public class RequestPacket extends AbstractStanza {

	public RequestPacket(int smVersion) {
		super("r");
		this.setAttribute("xmlns", "urn:xmpp:sm:" + smVersion);
	}

}
