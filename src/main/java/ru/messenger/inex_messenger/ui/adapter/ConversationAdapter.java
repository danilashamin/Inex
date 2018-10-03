package ru.messenger.inex_messenger.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import ru.messenger.inex_messenger.R;
import ru.messenger.inex_messenger.entities.Account;
import ru.messenger.inex_messenger.entities.Conversation;
import ru.messenger.inex_messenger.entities.ListItem;
import ru.messenger.inex_messenger.entities.Message;
import ru.messenger.inex_messenger.entities.Presence;
import ru.messenger.inex_messenger.entities.Transferable;
import ru.messenger.inex_messenger.ui.ConversationActivity;
import ru.messenger.inex_messenger.ui.XmppActivity;
import ru.messenger.inex_messenger.utils.UIHelper;
import ru.messenger.inex_messenger.circleimage.CircleImageView;
import ru.messenger.inex_messenger.utils.UIHelper;
import android.widget.RelativeLayout;

import org.bouncycastle.jcajce.provider.symmetric.AES;

public class ConversationAdapter extends ArrayAdapter<Conversation> {

	private XmppActivity activity;
	private static final int SENT = 0;
	private static final int RECEIVED = 1;
	private static final int STATUS = 2;
	private static final int ISUNLOCK = 0;
	private static final int ISNOLOCK = 1;
	private static final int ISLOCK   = 2;

	public ConversationAdapter(XmppActivity activity,
			List<Conversation> conversations) {
		super(activity, 0, conversations);
		this.activity = activity;
	}

	private	@DrawableRes int getDrawableStatusIc(Presence.Status status) {
		// TODO KAA возвращаем иконку
		switch (status) {
			case CHAT:
				return R.drawable.ic__indicator_terra_chat;
			case AWAY:
				return R.drawable.ic__indicator_terra_away;
			case XA:
				return R.drawable.ic__indicator_terra_xa;
			case DND:
				return R.drawable.ic__indicator_terra_dnd;
			case ONLINE:
				return R.drawable.ic__indicator_terra;
			case OFFLINE:
				return R.drawable.ic__indicator_terra_offline;
			default: return R.drawable.ic__indicator_terra_offline;
		}
	}

	private	@DrawableRes int getTextStatus(Presence.Status status) {
		// TODO KAA возвращаем иконку
		switch (status) {
			case CHAT:
				return R.drawable.ic__indicator_terra_chat;
			case AWAY:
				return R.drawable.ic__indicator_terra_away;
			case XA:
				return R.drawable.ic__indicator_terra_xa;
			case DND:
				return R.drawable.ic__indicator_terra_dnd;
			case ONLINE:
				return R.drawable.ic__indicator_terra;
			case OFFLINE:
				return R.drawable.ic__indicator_terra_offline;
			default: return R.drawable.ic__indicator_terra_offline;
		}
	}

	private Presence.Status getStatusTitle(Conversation conversation){
		Presence.Status st;
		if (conversation != null
				&& conversation.getAccount().getStatus() == Account.State.ONLINE) {
			if (conversation.getMode() == Conversation.MODE_SINGLE) {
				st = conversation.getContact().getShownStatus();
			} else {
				st = conversation.getMucOptions().online() ? Presence.Status.ONLINE : Presence.Status.OFFLINE;
			}
		} else {
			st = Presence.Status.OFFLINE;
		}
		return st;
	}

	public int getLock(Conversation conversation) {
		switch (conversation.getNextEncryption()) {
			case Message.ENCRYPTION_NONE:
				if (conversation.getAccount().getAxolotlService().isConversationAxolotlCapable(conversation)) {
					return ISUNLOCK;
				} else {
					return ISNOLOCK;
				}
			case Message.ENCRYPTION_AXOLOTL:
				return ISLOCK;
			default:
				return ISNOLOCK;
		}
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.conversation_list_row,parent, false);
		}
		Conversation conversation = getItem(position);
		if (this.activity instanceof ConversationActivity) {
			View swipeableItem = view.findViewById(R.id.swipeable_item);
			ConversationActivity a = (ConversationActivity) this.activity;
			int c = a.highlightSelectedConversations() && conversation == a.getSelectedConversation() ? a.getSecondaryBackgroundColor() : a.getPrimaryBackgroundColor();
			swipeableItem.setBackgroundColor(c);
		}
		TextView convName = (TextView) view.findViewById(R.id.conversation_name);
		Boolean isConference = false;
		if (conversation.getMode() == Conversation.MODE_SINGLE || activity.useSubjectToIdentifyConference()) {
			convName.setText(conversation.getName());
			if (activity.useSubjectToIdentifyConference() && conversation.getMode() != Conversation.MODE_SINGLE){
			  isConference = true;
			}
		} else {
			convName.setText(conversation.getJid().toBareJid().toString());
		}
		TextView mLastMessage = (TextView) view.findViewById(R.id.conversation_lastmsg);
		TextView mTimestamp = (TextView) view.findViewById(R.id.conversation_lastupdate);
		ImageView imagePreview = (ImageView) view.findViewById(R.id.conversation_lastimage);
		RelativeLayout notificationStatus = (RelativeLayout) view.findViewById(R.id.notification_status);
		CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.CircleImageView);
		TextView notificationStatus0 = (TextView) view.findViewById(R.id.notification_status0);
		// TODO KAA Пока не поддерживается
		TextView nSenderName = (TextView) view.findViewById(R.id.ls_sendername);
		nSenderName.setText("");
		nSenderName.setVisibility(View.GONE);
		ImageView indicatorReceivedGrey = (ImageView) view.findViewById(R.id.ls_indicator_received_grey);
		ImageView indicatorReceived = (ImageView) view.findViewById(R.id.ls_indicator_received);
        ImageView indicatorReceivedW = (ImageView) view.findViewById(R.id.ls_indicator_receivedW);
		CircleImageView indicator_line = (CircleImageView) view.findViewById(R.id.indicator_line);
		TextView textstatus = (TextView) view.findViewById(R.id.textstatus);
		ImageView imageLock = (ImageView) view.findViewById(R.id.imageLock);
		indicatorReceivedGrey.setVisibility(View.GONE);
        indicatorReceived.setVisibility(View.GONE);
        indicatorReceivedW.setVisibility(View.GONE);
		indicator_line.setVisibility(View.GONE);
		imageLock.setVisibility(View.GONE);
		textstatus.setText("");
		textstatus.setVisibility(View.VISIBLE);

		Message message = conversation.getLatestMessage();
		// TODO KAA
		int cnt_no_read_messages = conversation.getNoReadMessage();
		if (!conversation.isRead()) {
			convName.setTypeface(null, Typeface.BOLD);
		} else {
			convName.setTypeface(null, Typeface.BOLD);
		}
        /* KAA TODO 20170622 1106 */
		if (message.getFileParams().width > 0
				&& (message.getTransferable() == null
				|| message.getTransferable().getStatus() != Transferable.STATUS_DELETED)) {

			mLastMessage.setVisibility(View.VISIBLE);
			indicatorReceivedGrey.setVisibility(View.GONE);
			indicatorReceived.setVisibility(View.GONE);
			indicatorReceivedW.setVisibility(View.GONE);
			imagePreview.setVisibility(View.VISIBLE);
			notificationStatus.setVisibility(View.GONE);
			activity.loadBitmap(message, imagePreview);
			Pair<String, Boolean> preview = UIHelper.getMessagePreview(activity, message);

///////////////////////
			indicatorReceivedGrey.setVisibility(View.GONE);
			indicatorReceived.setVisibility(View.GONE);
			indicatorReceivedW.setVisibility(View.GONE);
			String text_mess = preview.first;
			switch (message.getMergedStatus()) {
				case Message.STATUS_SEND_RECEIVED:
					indicatorReceivedGrey.setVisibility(View.GONE);
					indicatorReceived.setVisibility(View.VISIBLE);
					indicatorReceivedW.setVisibility(View.GONE);
					break;
				case Message.STATUS_SEND_DISPLAYED:
					indicatorReceivedGrey.setVisibility(View.GONE);
					indicatorReceived.setVisibility(View.GONE);
					indicatorReceivedW.setVisibility(View.VISIBLE);
					break;
				case Message.STATUS_SEND:
					indicatorReceivedGrey.setVisibility(View.VISIBLE);
					indicatorReceived.setVisibility(View.GONE);
					indicatorReceivedW.setVisibility(View.GONE);
					break;
				case Message.STATUS_SEND_FAILED:
					//		text_mess = "failed" + ": " + preview.first;
					break;
				case Message.STATUS_WAITING:
					//		text_mess = "wait" + ": " + preview.first;
					break;
				case Message.STATUS_UNSEND:
					//text_mess = "unsend" + ": " + preview.first;
					break;
				case Message.STATUS_OFFERED:
					//		text_mess = "offered" + ": " + preview.first;
					break;
				default:
					if (isConference) {
						nSenderName.setVisibility(View.VISIBLE);
						nSenderName.setText(UIHelper.getMessageDisplayName(message) + ": ");
					}
					break;
			}
			if (isConference){
			 	nSenderName.setVisibility(View.VISIBLE);
		    }
		    if (text_mess.contains(activity.getString(R.string.image))){
				mLastMessage.setText(" " + activity.getString(R.string.foto));
			}
			else if (text_mess.contains(activity.getString(R.string.video))){
				mLastMessage.setText(" " + activity.getString(R.string.videofile));
			}
			else{
				mLastMessage.setText(" " + activity.getString(R.string.file));
			}


			//int cnt_new, cnt_old;
			// TODO KAA
			if (conversation.isRead()) {
				mLastMessage.setTypeface(null,Typeface.NORMAL);
				mLastMessage.setTextColor(activity.getSecondaryTextColor());
				nSenderName.setTypeface(null,Typeface.NORMAL);
				nSenderName.setTextColor(activity.getSecondaryTextColor());
				mTimestamp.setTextColor(activity.getSecondaryTextColor());
				mTimestamp.setTypeface(null, Typeface.NORMAL);
				notificationStatus.setVisibility(View.GONE);
				notificationStatus0.setText(""+0);
			} else {
				if (cnt_no_read_messages > 0) {
					notificationStatus.setVisibility(View.VISIBLE);
					mLastMessage.setTypeface(null, Typeface.BOLD);
					mLastMessage.setTextColor(activity.getSecondaryTextColor());
					nSenderName.setTypeface(null,Typeface.BOLD);
					nSenderName.setTextColor(activity.getSecondaryTextColor());
					mTimestamp.setTypeface(null, Typeface.BOLD);
					mTimestamp.setTextColor(activity.getLightGreenColor());
					circleImageView.setImageResource(R.color.green200);
					//cnt_old = Integer.parseInt(notificationStatus0.getText().toString());
					//cnt_new = cnt_old+1;
					notificationStatus0.setText("" + cnt_no_read_messages);
				}
			}
///////////////////////





		} else {
		    Pair<String,Boolean> preview = UIHelper.getMessagePreview(activity,message);
		    mLastMessage.setVisibility(View.VISIBLE);
			imagePreview.setVisibility(View.GONE);
			if (preview.second) {
				/*if (conversation.isRead()) {
                    notificationStatus.setVisibility(View.GONE);
                    mLastMessage.setTypeface(null, Typeface.ITALIC);
					mLastMessage.setTextColor(activity.getSecondaryTextColor());
				} else {
                    notificationStatus.setVisibility(View.GONE);
                    mLastMessage.setTypeface(null,Typeface.BOLD_ITALIC);
					mLastMessage.setTextColor(activity.getSecondaryTextColor());
				}*/
				indicatorReceivedGrey.setVisibility(View.GONE);
				indicatorReceived.setVisibility(View.GONE);
				indicatorReceivedW.setVisibility(View.GONE);
				String text_mess = preview.first;
				switch (message.getMergedStatus()) {
					case Message.STATUS_SEND_RECEIVED:
						indicatorReceivedGrey.setVisibility(View.GONE);
						indicatorReceived.setVisibility(View.VISIBLE);
						indicatorReceivedW.setVisibility(View.GONE);
						break;
					case Message.STATUS_SEND_DISPLAYED:
						indicatorReceivedGrey.setVisibility(View.GONE);
						indicatorReceived.setVisibility(View.GONE);
						indicatorReceivedW.setVisibility(View.VISIBLE);
						break;
					case Message.STATUS_SEND:
						indicatorReceivedGrey.setVisibility(View.VISIBLE);
						indicatorReceived.setVisibility(View.GONE);
						indicatorReceivedW.setVisibility(View.GONE);
						break;
					case Message.STATUS_SEND_FAILED:
						text_mess = "failed" + ": " + preview.first;
						break;
					case Message.STATUS_WAITING:
						text_mess = "wait" + ": " + preview.first;
						break;
					case Message.STATUS_UNSEND:
						//text_mess = "unsend" + ": " + preview.first;
						break;
					case Message.STATUS_OFFERED:
						text_mess = "offered" + ": " + preview.first;
						break;
					default:
						if (isConference) {
							text_mess = UIHelper.getMessageDisplayName(message) + ": " + text_mess;
						}
						//sendername.setVisibility(View.VISIBLE);
						break;
				}
				mLastMessage.setText(text_mess);

				/*if (isConference){
					nSenderName.setVisibility(View.VISIBLE);
				}
				if (text_mess.contains(activity.getString(R.string.image))){
					mLastMessage.setText(" " + activity.getString(R.string.foto));
				}
				else if (text_mess.contains(activity.getString(R.string.video))){
					mLastMessage.setText(" " + activity.getString(R.string.videofile));
				}
				else{
					mLastMessage.setText(" " + activity.getString(R.string.file));
				}*/
				//int cnt_new, cnt_old;
				// TODO KAA
				if (conversation.isRead()) {
					mLastMessage.setTypeface(null,Typeface.NORMAL);
					mLastMessage.setTextColor(activity.getSecondaryTextColor());
					mTimestamp.setTextColor(activity.getSecondaryTextColor());
					mTimestamp.setTypeface(null, Typeface.NORMAL);
					notificationStatus.setVisibility(View.GONE);
					notificationStatus0.setText(""+0);
				} else {
					if (cnt_no_read_messages > 0) {
						notificationStatus.setVisibility(View.VISIBLE);
						mLastMessage.setTypeface(null, Typeface.BOLD);
						mLastMessage.setTextColor(activity.getSecondaryTextColor());
						mTimestamp.setTypeface(null, Typeface.BOLD);
						mTimestamp.setTextColor(activity.getLightGreenColor());
						circleImageView.setImageResource(R.color.green200);
						//cnt_old = Integer.parseInt(notificationStatus0.getText().toString());
						//cnt_new = cnt_old+1;
						notificationStatus0.setText("" + cnt_no_read_messages);

					}
				}

			} else {
				// KAA TODO
				indicatorReceivedGrey.setVisibility(View.GONE);
				indicatorReceived.setVisibility(View.GONE);
				indicatorReceivedW.setVisibility(View.GONE);
				String text_mess = preview.first;
				switch (message.getMergedStatus()) {
					case Message.STATUS_SEND_RECEIVED:
						indicatorReceivedGrey.setVisibility(View.GONE);
						indicatorReceived.setVisibility(View.VISIBLE);
						indicatorReceivedW.setVisibility(View.GONE);
						break;
					case Message.STATUS_SEND_DISPLAYED:
						indicatorReceivedGrey.setVisibility(View.GONE);
						indicatorReceived.setVisibility(View.GONE);
						indicatorReceivedW.setVisibility(View.VISIBLE);
						break;
					case Message.STATUS_SEND:
						indicatorReceivedGrey.setVisibility(View.VISIBLE);
						indicatorReceived.setVisibility(View.GONE);
						indicatorReceivedW.setVisibility(View.GONE);
						break;
					case Message.STATUS_SEND_FAILED:
						text_mess = "failed" + ": " + preview.first;
						break;
					case Message.STATUS_WAITING:
						text_mess = "wait" + ": " + preview.first;
						break;
					case Message.STATUS_UNSEND:
						//text_mess = "unsend" + ": " + preview.first;
						break;
					case Message.STATUS_OFFERED:
						text_mess = "offered" + ": " + preview.first;
						break;
					default:
						if (isConference) {
							text_mess = UIHelper.getMessageDisplayName(message) + ": " + text_mess;
						}
						//sendername.setVisibility(View.VISIBLE);
						break;
				}
				mLastMessage.setText(text_mess);
				//int cnt_new, cnt_old;
				// TODO KAA
				if (conversation.isRead()) {
					mLastMessage.setTypeface(null,Typeface.NORMAL);
					mLastMessage.setTextColor(activity.getSecondaryTextColor());
					mTimestamp.setTextColor(activity.getSecondaryTextColor());
					mTimestamp.setTypeface(null, Typeface.NORMAL);
					notificationStatus.setVisibility(View.GONE);
                    notificationStatus0.setText(""+0);
                } else {
					if (cnt_no_read_messages > 0) {
						notificationStatus.setVisibility(View.VISIBLE);
						mLastMessage.setTypeface(null, Typeface.BOLD);
						mLastMessage.setTextColor(activity.getSecondaryTextColor());
						mTimestamp.setTypeface(null, Typeface.BOLD);
						mTimestamp.setTextColor(activity.getLightGreenColor());
						circleImageView.setImageResource(R.color.green200);
						//cnt_old = Integer.parseInt(notificationStatus0.getText().toString());
						//cnt_new = cnt_old+1;
						notificationStatus0.setText("" + cnt_no_read_messages);

					}
				}
			}
		}

		long muted_till = conversation.getLongAttribute(Conversation.ATTRIBUTE_MUTED_TILL,0);
		if (muted_till == Long.MAX_VALUE) {
			//notificationStatus.setVisibility(View.VISIBLE);
			int ic_notifications_off = 	  activity.getThemeResource(R.attr.icon_notifications_off, R.drawable.ic_notifications_off_black54_24dp);
			//notificationStatus.setImageResource(ic_notifications_off);
		} else if (muted_till >= System.currentTimeMillis()) {
			//notificationStatus.setVisibility(View.VISIBLE);
			int ic_notifications_paused = activity.getThemeResource(R.attr.icon_notifications_paused, R.drawable.ic_notifications_paused_black54_24dp);
			//notificationStatus.setImageResource(ic_notifications_paused);
		} else if (conversation.alwaysNotify()) {
			//notificationStatus.setVisibility(View.GONE);
		} else {
			//notificationStatus.setVisibility(View.VISIBLE);
			int ic_notifications_none =	  activity.getThemeResource(R.attr.icon_notifications_none, R.drawable.ic_notifications_none_black54_24dp);
			//notificationStatus.setImageResource(ic_notifications_none);
		}

		mTimestamp.setText(UIHelper.readableTimeDifference(activity,conversation.getLatestMessage().getTimeSent()));
        CircleImageView profilePicture = (CircleImageView) view.findViewById(R.id.conversation_image);
		loadAvatar(conversation,profilePicture);
		// TODO KAA
//		String strStatus = "";
		Presence.Status st = getStatusTitle(conversation);
//		strStatus = conversationActivity.getStringStatus(st);
		indicator_line.setImageResource(getDrawableStatusIc(st));
		if (st != Presence.Status.OFFLINE) {
			ListItem.Tag tag = UIHelper.getTagForStatus(activity, st);
			textstatus.setText(tag.getName());
			textstatus.setTextColor(tag.getColor());
		}
        if (getLock(conversation) == ISLOCK){
			imageLock.setVisibility(View.VISIBLE);
			//imageLock.setImageResource(R.drawable.ic_security_lock);
			imageLock.setImageResource(R.drawable.ic_lock_black_18dp);
			imageLock.setAlpha(0.57f); //0.57f //0.7f

		}
        return view;
	}

	class BitmapWorkerTask extends AsyncTask<Conversation, Void, Bitmap> {
		// TODO KAA
        private final WeakReference<CircleImageView> imageViewReference;
		private Conversation conversation = null;

		public BitmapWorkerTask(CircleImageView imageView) { // TODO KAA
			imageViewReference = new WeakReference<>(imageView);
		}

		@Override
		protected Bitmap doInBackground(Conversation... params) {
			return activity.avatarService().get(params[0], activity.getPixel(56), isCancelled());
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap != null && !isCancelled()) {
				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
					imageView.setBackgroundColor(0x00000000);
				}
			}
		}
	}

	public void loadAvatar(Conversation conversation, CircleImageView imageView) {
		if (cancelPotentialWork(conversation, imageView)) {
			final Bitmap bm = activity.avatarService().get(conversation, activity.getPixel(56), true);
			if (bm != null) {
				cancelPotentialWork(conversation, imageView);
				imageView.setImageBitmap(bm);

				// TODO KAA Color
				//imageView.setBackgroundColor(0x00000000);
			} else {
				// TODO KAA Color
				//imageView.setBackgroundColor(UIHelper.getColorForName(conversation.getName()));
				imageView.setImageDrawable(null);
				final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
				final AsyncDrawable asyncDrawable = new AsyncDrawable(activity.getResources(), null, task);
				imageView.setImageDrawable(asyncDrawable);
				try {
					task.execute(conversation);
				} catch (final RejectedExecutionException ignored) {
				}
			}
		}
	}

	public static boolean cancelPotentialWork(Conversation conversation, CircleImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final Conversation oldConversation = bitmapWorkerTask.conversation;
			if (oldConversation == null || conversation != oldConversation) {
				bitmapWorkerTask.cancel(true);
			} else {
				return false;
			}
		}
		return true;
	}

	private static BitmapWorkerTask getBitmapWorkerTask(CircleImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<>(bitmapWorkerTask);
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}
}