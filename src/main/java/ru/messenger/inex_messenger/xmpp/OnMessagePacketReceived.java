package ru.messenger.inex_messenger.xmpp;

import ru.messenger.inex_messenger.entities.Account;
import ru.messenger.inex_messenger.xmpp.stanzas.MessagePacket;

public interface OnMessagePacketReceived extends PacketReceived {
	public void onMessagePacketReceived(Account account, MessagePacket packet);
}
