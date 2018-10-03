package ru.messenger.inex_messenger.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import ru.messenger.inex_messenger.R;

public class StartActivity extends Activity {

    private final int REQUEST_READ_CONTACTS = 0x3b28df;
    private final int REQUEST_WRITE_CONTACTS = 0x3b28ef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void onStart() {
        super.onStart();

        askForContactsPermissions();
    }

    private void askForContactsPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.sync_with_contacts);
                    builder.setMessage(R.string.sync_with_contacts_long);
                    builder.setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
                            }
                        }
                    });
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
                                }
                            }
                        });
                    }
                    builder.create().show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
                }
            } else {
                askForContactsPermissionsWrite();
            }
        } else {
            startActivity(new Intent(this, ConversationActivity.class));
            finish();
        }
    }

    private void askForContactsPermissionsWrite() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.sync_with_contacts);
                    builder.setMessage(R.string.sync_with_contacts_long);
                    builder.setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, REQUEST_WRITE_CONTACTS);
                            }
                        }
                    });
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, REQUEST_WRITE_CONTACTS);
                                }
                            }
                        });
                    }
                    builder.create().show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, REQUEST_WRITE_CONTACTS);
                }
            } else {
                startActivity(new Intent(this, ConversationActivity.class));
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == REQUEST_READ_CONTACTS) {
                    askForContactsPermissionsWrite();
                } else if (requestCode == REQUEST_WRITE_CONTACTS) {
                    startActivity(new Intent(this, ConversationActivity.class));
                    finish();
                }
            }
    }
}
