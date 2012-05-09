# notification-wrapper

__A convenient wrapper for android.app.Notification__

This is a convenient helper to show notifications in Android.

## Requirements

This package depends on the Android SDK.

## Examples

To show just a simple notification text call (from an Activity):

		new NotificationWrapper(this, "some text").update();

You can set an Intent to launch when the notification is opened:

		Intent someIntent = new Intent(...);
		...
		new NotificationWrapper(this, "some text").setIntent(someIntent).update();

## TODO

* Add all missing Notification features (i.e. LEDs, sounds, vibrate ...)

## License

Copyright (c) Marten Gajda 2012, licensed under GPL version 2 or newer (see `LICENSE`).

