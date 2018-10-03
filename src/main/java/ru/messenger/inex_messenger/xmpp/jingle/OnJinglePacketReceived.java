package ru.messenger.inex_messenger.xmpp.jingle;

import ru.messenger.inex_messenger.entities.Account;
import ru.messenger.inex_messenger.xmpp.PacketReceived;
import ru.messenger.inex_messenger.xmpp.jingle.stanzas.JinglePacket;

public interface OnJinglePacketReceived extends PacketReceived {
	void onJinglePacketReceived(Account account, JinglePacket packet);
}
