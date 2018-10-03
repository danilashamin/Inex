package ru.messenger.inex_messenger.xmpp;

import ru.messenger.inex_messenger.entities.Account;

public interface OnMessageAcknowledged {
	public void onMessageAcknowledged(Account account, String id);
}
