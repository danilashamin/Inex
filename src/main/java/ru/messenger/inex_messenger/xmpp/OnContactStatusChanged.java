package ru.messenger.inex_messenger.xmpp;

import ru.messenger.inex_messenger.entities.Contact;

public interface OnContactStatusChanged {
	public void onContactStatusChanged(final Contact contact, final boolean online);
}
