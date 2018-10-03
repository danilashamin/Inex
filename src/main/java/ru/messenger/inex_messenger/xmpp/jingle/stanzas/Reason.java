package ru.messenger.inex_messenger.xmpp.jingle.stanzas;

import ru.messenger.inex_messenger.xml.Element;

public class Reason extends Element {
	private Reason(String name) {
		super(name);
	}

	public Reason() {
		super("reason");
	}
}
