package ru.messenger.inex_messenger.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.messenger.inex_messenger.Config;
import ru.messenger.inex_messenger.R;
import ru.messenger.inex_messenger.entities.Account;
import ru.messenger.inex_messenger.ui.ManageAccountActivity;
import ru.messenger.inex_messenger.ui.XmppActivity;
import ru.messenger.inex_messenger.ui.widget.Switch;

public class AccountAdapter extends ArrayAdapter<Account> {

	private XmppActivity activity;

	public AccountAdapter(XmppActivity activity, List<Account> objects) {
		super(activity, 0, objects);
		this.activity = activity;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final Account account = getItem(position);
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.account_row, parent, false);
		}
		TextView jid = (TextView) view.findViewById(R.id.account_jid);
		if (Config.DOMAIN_LOCK != null) {
			jid.setText(account.getJid().getLocalpart());
		} else {
			//KAA jid.setText(account.getJid().toBareJid().toString());
			jid.setText(account.getJid().getLocalpart());
		}
		TextView statusView = (TextView) view.findViewById(R.id.account_status);
		ImageView imageView = (ImageView) view.findViewById(R.id.account_image);
		imageView.setImageBitmap(activity.avatarService().get(account, activity.getPixel(48)));
		statusView.setText(getContext().getString(account.getStatus().getReadableId()));
		switch (account.getStatus()) {
			case ONLINE:
				statusView.setTextColor(activity.getGreenColor());
				break;
			case DISABLED:
				statusView.setTextColor(activity.getSecondaryTextColor());
				break;
			case CONNECTING:
				statusView.setTextColor(activity.getLightGreenColor());
				break;
			default:
				statusView.setTextColor(activity.getWarningTextColor());
				break;
		}
		final Switch tglAccountState = (Switch) view.findViewById(R.id.tgl_account_status);
		final boolean isDisabled = (account.getStatus() == Account.State.DISABLED);
		tglAccountState.setChecked(!isDisabled,false);
		tglAccountState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b == isDisabled && activity instanceof ManageAccountActivity) {
					((ManageAccountActivity) activity).onClickTglAccountState(account,b);
				}
			}
		});
		return view;
	}
}
