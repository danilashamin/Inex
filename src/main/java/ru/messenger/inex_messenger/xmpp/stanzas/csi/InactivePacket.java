package ru.messenger.inex_messenger.xmpp.stanzas.csi;

import ru.messenger.inex_messenger.xmpp.stanzas.AbstractStanza;

public class InactivePacket extends AbstractStanza {
	public InactivePacket() {
		super("inactive");
		setAttribute("xmlns", "urn:xmpp:csi:0");
	}
}
