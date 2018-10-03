package ru.messenger.inex_messenger.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.messenger.inex_messenger.entities.Account;
import ru.messenger.inex_messenger.entities.Blockable;
import ru.messenger.inex_messenger.entities.ListItem;
import ru.messenger.inex_messenger.xmpp.jid.InvalidJidException;
import ru.messenger.inex_messenger.xmpp.jid.Jid;

/**
 * Created by Алексей on 02.05.2017.
 */

public class InternalContact  implements ListItem, Blockable {

    private ArrayList<Integer> list_jabber_id = new ArrayList<>();
    private ArrayList<String> list_jabber_name = new ArrayList<>();
    private ArrayList<String> list_phones = new ArrayList<>();
    private String name = "";
    Bitmap avatar_btmp = null;
    private String contactId = null;
    private long   row_id = 0;

    public long getRow_id() {
        return row_id;
    }

    public void setRow_id(long row_id) {
        this.row_id = row_id;
    }


    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }


    public ArrayList<String> getList_jabber_name() {
        return list_jabber_name;
    }

    public Bitmap getAvatar_btmp() {
        return avatar_btmp;
    }

    public void setAvatar_btmp(Bitmap avatar_btmp) {
        this.avatar_btmp = avatar_btmp;
    }

    public ArrayList<String> getList_phones() {
        return list_phones;
    }

    public void setList_phones(ArrayList<String> list_phones) {
        this.list_phones = list_phones;
    }
    public void addPhone(String phone) {
        this.list_phones.add(phone);
    }

    public String getName() {
        return name;
    }


    public String getJabberid(Integer type) {
        for(int i = 0; i < list_jabber_id.size(); i++) {
            if(list_jabber_id.get(i) == type) {
                return list_jabber_name.get(i);
            }
        }
        return "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJabberid(int type, String jabberid) {
        this.list_jabber_id.add(type);
        this.list_jabber_name.add(jabberid);
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    @Override
    public String getDisplayJid() {
        return getJabberid(ContactsContract.CommonDataKinds.Im.PROTOCOL_JABBER);
    }

    @Override
    public boolean isBlocked() {
        return false;
    }

    @Override
    public boolean isDomainBlocked() {
        return false;
    }

    @Override
    public Jid getBlockedJid() {
        return null;
    }

    @Override
    public Jid getJid() {
        try {
            return Jid.fromString(getJabberid(ContactsContract.CommonDataKinds.Im.PROTOCOL_JABBER));
        } catch (InvalidJidException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Account getAccount() {
        return null;
    }

    @Override
    public List<Tag> getTags(Context context) {
        return null;
    }

    @Override
    public boolean match(Context context, String needle) {
        return false;
    }

    @Override
    public int compareTo(@NonNull ListItem o) {
        return 0;
    }
}
