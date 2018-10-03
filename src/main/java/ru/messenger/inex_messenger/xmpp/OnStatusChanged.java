package ru.messenger.inex_messenger.xmpp;

import ru.messenger.inex_messenger.entities.Account;

public interface OnStatusChanged {
	public void onStatusChanged(Account account);
}
