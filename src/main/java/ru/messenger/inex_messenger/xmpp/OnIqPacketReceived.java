package ru.messenger.inex_messenger.xmpp;

import ru.messenger.inex_messenger.entities.Account;
import ru.messenger.inex_messenger.xmpp.stanzas.IqPacket;

public interface OnIqPacketReceived extends PacketReceived {
	public void onIqPacketReceived(Account account, IqPacket packet);
}
