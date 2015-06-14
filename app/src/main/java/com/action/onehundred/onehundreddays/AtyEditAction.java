package com.action.onehundred.onehundreddays;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AtyEditAction extends ActionBarActivity {

    static TextView tv_remindingTime;
    private TextView tv_ActionName;
    private TextView tv_actionDescription;
    private EditText et_ActionName;
    static int reminding_time_hour;
    static int reminding_time_minute;
    private int  actionCategory = -1;
    private int actionCategoryIndex = -1;
    boolean isDefaultAction;
    private static final int SELECT_PICTURE = 1;
    private static final int CROP_PHOTO = 2;
    private ImageView iv_picture;
    private Uri imageUri; //picture's Uri

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         isDefaultAction = getIntent().getIntExtra(Config.ActionCategory,0)==-1;
        if (isDefaultAction){
            setContentView(R.layout.activity_aty_edit_action_default);
        } else {
            setContentView(R.layout.activity_aty_edit_action);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_with_back);
        TextView tv_title = (TextView) findViewById(R.id.actionbar_title);
        tv_title.setText(R.string.add_action);
        findViewById(R.id.actionbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // return to the parent activity defined in AndroidManifest.xml
                // by android:parentActivity label.
                //NavUtils.navigateUpFromSameTask(AtyEditAction.this);
                finish();
            }
        });


        // set the default reminding time 20:00
        reminding_time_hour = 20;
        reminding_time_minute =  0;

        iv_picture = (ImageView) findViewById(R.id.action_picture);
        if (isDefaultAction){
            et_ActionName = (EditText) findViewById(R.id.edit_action_action_name_default);
            iv_picture.setImageResource(R.drawable.running);
            iv_picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Determine Uri of camera image to save.
                    final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
                    root.mkdirs();
                    final String fname = "this_picture";
                    final File sdImageMainDirectory = new File(root, fname);
                    imageUri = Uri.fromFile(sdImageMainDirectory);

                    // Camera.
                    final List<Intent> cameraIntents = new ArrayList<Intent>();
                    final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    final PackageManager packageManager = getPackageManager();
                    final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
                    for(ResolveInfo res : listCam) {
                        final String packageName = res.activityInfo.packageName;
                        final Intent intent = new Intent(captureIntent);
                        intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                        intent.setPackage(packageName);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        cameraIntents.add(intent);
                    }

                    // Filesystem.
                    final Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                    // Chooser of filesystem options.
                    final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

                    // Add the camera options.
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
                    startActivityForResult(chooserIntent, SELECT_PICTURE);
                }
            });

        } else {
            tv_ActionName = (TextView) findViewById(R.id.edit_action_action_name);
            actionCategory = getIntent().getIntExtra(Config.ChosenActionCategory, -1);
            actionCategoryIndex = getIntent().getIntExtra(Config.ChosenActionCategoryIndex, -1);
            tv_ActionName.setText(Config.ActionsStringResource[actionCategory][actionCategoryIndex]);
            iv_picture.setImageResource(Config.ActionsPictureResource[actionCategory][actionCategoryIndex]);
            tv_actionDescription = (TextView) findViewById(R.id.edit_action_description);
            try {
                Resources res = getResources();
                InputStream in_s = res.openRawResource(Config.ActionsDescriptionResource[actionCategory][actionCategoryIndex]);
                byte[] b = new byte[in_s.available()];
                in_s.read(b);
                tv_actionDescription.setText(new String(b));
                in_s.close();
            } catch (Exception e) {
                // e.printStackTrace();
                tv_actionDescription.setText("");
            }

        }


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        iv_picture.getLayoutParams().height = size.x*5/8;

        tv_remindingTime = (TextView) findViewById(R.id.edit_action_reminding_time);
        tv_remindingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new FmtTimePicker();
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });
        Date d = new Date();
        TextView tv_startDate = (TextView) findViewById(R.id.edit_action_start_date);
        tv_startDate.setText(DateFormat.format("yyyy/MM/dd", d));
        TextView tv_endDate = (TextView) findViewById(R.id.edit_action_end_date);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DAY_OF_YEAR,100);
        tv_endDate.setText(DateFormat.format("yyyy/MM/dd", c.getTime()));


    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                //NavUtils.navigateUpFromSameTask(AtyEditAction.this);
                finish();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aty_edit_action, menu);
        menu.findItem(R.id.edit_action_action_add).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String actionName;
                if(isDefaultAction){
                    actionName = et_ActionName.getText().toString();
                } else {
                    actionName = tv_ActionName.getText().toString();
                }
                Action currentAction = new Action(actionName,reminding_time_hour,reminding_time_minute,actionCategory,actionCategoryIndex);
                SharedPreferences sp =getSharedPreferences(Config.CurrentAction, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                try {
                    editor.putString(Config.CurrentActionDetails, currentAction.toJSON().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.apply();
                sp = getSharedPreferences(Config.ActionConfig,Context.MODE_PRIVATE);
                sp.edit().putBoolean(Config.ActionConfigHasAction,true).apply();
                Intent i = new Intent(AtyEditAction.this,AtyCurrentAction.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                return true;
            }
        });
        return true;
    }


    public static class FmtTimePicker extends DialogFragment
                                implements TimePickerDialog.OnTimeSetListener{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            reminding_time_hour = hourOfDay;
            reminding_time_minute = minute;
            String time = (hourOfDay>9?"":"0")+hourOfDay + (minute>9?" : ":" : 0") + minute;
            AtyEditAction.tv_remindingTime.setText(time);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                }

                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = imageUri;
                } else {
                    selectedImageUri = data.getData();
                }

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(selectedImageUri, "image/*");
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CROP_PHOTO);
            }
            else if (requestCode == CROP_PHOTO){
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                            .openInputStream(imageUri));
                    iv_picture.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
