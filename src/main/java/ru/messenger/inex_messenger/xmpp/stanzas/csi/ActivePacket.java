package ru.messenger.inex_messenger.xmpp.stanzas.csi;

import ru.messenger.inex_messenger.xmpp.stanzas.AbstractStanza;

public class ActivePacket extends AbstractStanza {
	public ActivePacket() {
		super("active");
		setAttribute("xmlns", "urn:xmpp:csi:0");
	}
}
