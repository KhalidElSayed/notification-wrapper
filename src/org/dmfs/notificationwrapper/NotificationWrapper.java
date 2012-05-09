/*
 * dmfs - http://dmfs.org/
 *
 * Copyright (C) 2011 Marten Gajda <marten@dmfs.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package org.dmfs.notificationwrapper;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;


/**
 * Simple wrapper to create and manage {@link Notification}s conveniently.
 * 
 * The simplest use case is as follows (called from an {@link Activity}):
 * 
 * <pre>
 * new NotificationWrapper(this, &quot;some text&quot;).update();
 * </pre>
 * 
 */
public class NotificationWrapper
{
	/**
	 * Next notification id to use starting at some random value.
	 */
	private static int sNotificationId = 989130213;

	/**
	 * The {@link Context} of this notification.
	 */
	private final Context mContext;

	/**
	 * The notification id.
	 */
	private final int mNotificationId;

	/**
	 * The actual {@link Notification} instance.
	 */
	private final Notification mNotification;

	/**
	 * The notification title.
	 */
	private CharSequence mTitle;

	/**
	 * The notification content text.
	 */
	private CharSequence mText = "";

	/**
	 * The pending intent to launch on click.
	 */
	private PendingIntent mPendingIntent;


	/**
	 * Create new notification and initialize it with some default values.
	 * 
	 * @param context
	 *            A {@link Context}.
	 */
	public NotificationWrapper(Context context)
	{
		mContext = context;
		mNotification = new android.app.Notification();
		mNotificationId = newNotificationId();

		// show notification instantly by default
		mNotification.when = System.currentTimeMillis();

		// use application label as default title
		PackageManager packageManager = context.getPackageManager();
		mTitle = context.getApplicationInfo().loadLabel(packageManager);

		// set auto cancel by default
		setAutoCancel(true);
	}


	/**
	 * Create new notification and initialize it with some default values and the notification text {@code text}.
	 * 
	 * @param context
	 *            A {@link Context}.
	 * @param text
	 *            The notification text.
	 */
	public NotificationWrapper(Context context, CharSequence text)
	{
		this(context);
		setText(text);
	}


	/**
	 * Create new notification and initialize it with some default values and the notification text resource {@code text}.
	 * 
	 * @param context
	 *            A {@link Context}.
	 * @param textRes
	 *            The notification text resource id.
	 */
	public NotificationWrapper(Context context, int textRes)
	{
		this(context);
		setText(textRes);
	}


	/**
	 * Get a new notification id. This method is synchronized to ensure all ids are unique.
	 * 
	 * @return A new unique notification id.
	 */
	private static synchronized int newNotificationId()
	{
		return sNotificationId++;
	}


	/**
	 * Show the notification, replacing previous notifications if any.
	 */
	public void update()
	{
		if (mPendingIntent == null)
		{
			// create an empty intent
			mPendingIntent = PendingIntent.getActivity(mContext.getApplicationContext(), 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
		}

		// show notification
		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotification.setLatestEventInfo(mContext, mTitle, mText, mPendingIntent);
		notificationManager.notify(mNotificationId, mNotification);
	}


	/**
	 * Cancel a notification (i.e. hide it).
	 */
	public void cancel()
	{
		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(mNotificationId);
	}


	/**
	 * Cancel a notification (i.e. hide it) by id. This will only work if the notification has been created within the same application context.
	 * 
	 * @param context
	 *            A {@link Context}.
	 * @param id
	 *            The id of a notification to cancel. See {@code getNotificationId()}.
	 */
	public static void cancel(Context context, int id)
	{
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(id);
	}


	/**
	 * Set time when to show this notification.
	 * 
	 * @param when
	 *            Timestamp in milliseconds of the notification time.
	 * @return this instance.
	 */
	public NotificationWrapper setWhen(long when)
	{
		mNotification.when = when;
		return this;
	}


	/**
	 * Set an {@link Intent} for an {@Activity} to call when the notification is clicked.
	 * 
	 * @param intent
	 *            The {@link Intent}.
	 * @return this instance.
	 */
	public NotificationWrapper setIntent(Intent intent)
	{
		mPendingIntent = PendingIntent.getActivity(mContext.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		return this;
	}


	/**
	 * Set an {@link Intent} for an {@Activity} to call when the notification is clicked.
	 * 
	 * @param intent
	 *            The {@link Intent}.
	 * @param flags
	 *            Flags of the pending intent. See {@link PendingIntent}.
	 * @return this instance.
	 */
	public NotificationWrapper setIntent(Intent intent, int flags)
	{
		mPendingIntent = PendingIntent.getActivity(mContext.getApplicationContext(), 0, intent, flags);
		return this;
	}


	/**
	 * Set an {@link Intent} for an {@Activity} to call when the notification is clicked.
	 * 
	 * @param intent
	 *            The {@link Intent}.
	 * @param intentContext
	 *            The {@link Context} to use when calling the intent.
	 * @return this instance.
	 */
	public NotificationWrapper setIntent(Intent intent, Context intentContext)
	{
		mPendingIntent = PendingIntent.getActivity(intentContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		return this;
	}


	/**
	 * Set an {@link Intent} for an {@Activity} to call when the notification is clicked.
	 * 
	 * @param intent
	 *            The {@link Intent}.
	 * @param intentContext
	 *            The {@link Context} to use when calling the intent.
	 * @param flags
	 *            Flags of the pending intent. See {@link PendingIntent}.
	 * @return this instance.
	 */
	public NotificationWrapper setIntent(Intent intent, Context intentContext, int flags)
	{
		mPendingIntent = PendingIntent.getActivity(intentContext, 0, intent, flags);
		return this;
	}


	/**
	 * Set the notification title.
	 * 
	 * @param title
	 *            The new notification title.
	 * @return this Instance.
	 */
	public NotificationWrapper setTitle(CharSequence title)
	{
		mTitle = title;
		return this;
	}


	/**
	 * Set the notification title resource.
	 * 
	 * @param title
	 *            The new notification title resource.
	 * @return this Instance.
	 */
	public NotificationWrapper setTitle(int title)
	{
		mTitle = mContext.getString(title);
		return this;
	}


	/**
	 * Set the notification content text.
	 * 
	 * @param text
	 *            The new notification content text.
	 * @return this Instance.
	 */
	public NotificationWrapper setText(CharSequence text)
	{
		mText = text;
		return this;
	}


	/**
	 * Set the notification content text resource.
	 * 
	 * @param text
	 *            The new notification content text resource.
	 * @return this Instance.
	 */
	public NotificationWrapper setText(int text)
	{
		mText = mContext.getString(text);
		return this;
	}


	/**
	 * Set the notification ticker text.
	 * 
	 * @param ticker
	 *            The new notification ticker text.
	 * @return this Instance.
	 */
	public NotificationWrapper setTicker(CharSequence ticker)
	{
		mNotification.tickerText = ticker;
		return this;
	}


	/**
	 * Set the notification ticker text resource.
	 * 
	 * @param ticker
	 *            The new notification ticker text resource.
	 * @return this Instance.
	 */
	public NotificationWrapper setTicker(int ticker)
	{
		mNotification.tickerText = mContext.getString(ticker);
		return this;
	}


	/**
	 * Set the notification icon resource.
	 * 
	 * @param ticker
	 *            The new notification ticker text resource.
	 * @return this Instance.
	 */
	public NotificationWrapper setIcon(int iconRes)
	{
		mNotification.icon = iconRes;
		return this;
	}


	/**
	 * Set whether this event is ongoing.
	 * 
	 * @param ongoing
	 *            Set to {@code true} if this is an ongoing event.
	 * @return this Instance.
	 */
	public NotificationWrapper setOngoing(boolean ongoing)
	{
		changeNotificationFlag(Notification.FLAG_ONGOING_EVENT, ongoing);
		return this;
	}


	/**
	 * Set whether this event should be cleared by the "clear all" button or not.
	 * 
	 * @param noClear
	 *            Set to {@code true} if this event should not be cleared by the "clear all" button.
	 * @return this Instance.
	 */
	public NotificationWrapper setNoClear(boolean noClear)
	{
		changeNotificationFlag(Notification.FLAG_NO_CLEAR, noClear);
		return this;
	}


	/**
	 * Set whether this event should be removed automatically by clicking it.
	 * 
	 * @param noClear
	 *            Set to {@code true} to remove this event automatically when it's clicked.
	 * @return this Instance.
	 */
	public NotificationWrapper setAutoCancel(boolean autoCancel)
	{
		changeNotificationFlag(Notification.FLAG_AUTO_CANCEL, autoCancel);
		return this;
	}


	/**
	 * Set a notification flag to {@code value}.
	 * 
	 * @param flag
	 *            Bit mask of the flag to set.
	 * @param value
	 *            {@code true} to set the flag, {@code false} to clear it.
	 */
	private void changeNotificationFlag(int flag, boolean value)
	{
		mNotification.flags = value ? mNotification.flags | flag : mNotification.flags & ~flag;
	}


	/**
	 * Return the id of this notification.
	 * 
	 * @return The notification id.
	 */
	public int getNotificationId()
	{
		return mNotificationId;
	}
}