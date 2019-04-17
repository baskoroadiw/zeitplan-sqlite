package org.d3ifcool.zeitplannew;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class FragmentSetting extends Fragment {

    public FragmentSetting() {}

    Context appContext = MainActivity.getContextOfApplication();

    //Start of Feedback Card
    ConstraintLayout CardFeedback;
    ExpandableRelativeLayout exFeedback;
    ImageView arrowFeedback;
    Boolean rotateArrowFeedback;
    //End of Feedback Card

    //Start of Contact Card
    ConstraintLayout CardContact;
    ExpandableRelativeLayout exContact;
    ImageView arrowContact;
    Boolean rotateArrowContact;
    //End of Contact Card

    TextView toasts;
    Switch switchNotification;
    TextView contactEmail, contactWhatsapp, contactWhatsapp2, contactWhatsapp3;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH_NOTIFICATION = "switchNotification";

    private boolean switchOnOff;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting,container,false);

        //Start of Feedback Card
        exFeedback = (ExpandableRelativeLayout) view.findViewById(R.id.expand_feedback); //mencari expandnya
        exFeedback.collapse(); //untuk men-set agar awal dijalankan expandnya tertutup
        CardFeedback = (ConstraintLayout) view.findViewById(R.id.card_feedback); //mencari card yanag akan dijadikan trigger onClick
        arrowFeedback = (ImageView) view.findViewById(R.id.arrow_feedback); //mencari arrow
        rotateArrowFeedback = false;
        CardFeedback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { //set onClick pada Card nya
                exFeedback.toggle(); //fungsi toggle agar otomastis expand terbuka & tertutup bergantian
                if (rotateArrowFeedback == false){
                    arrowFeedback.setRotation(180); //men-rotate arrow 180 derajat
                    rotateArrowFeedback = true;
                }else{
                    arrowFeedback.setRotation(0); //men-rotate arrow jadi normal
                    rotateArrowFeedback = false;
                }
            }
        });
        //End of Feedback Card

        //Start of Contact Card
        exContact = (ExpandableRelativeLayout) view.findViewById(R.id.expand_contact); //mencari expandnya
        exContact.collapse(); //untuk men-set agar awal dijalankan expandnya tertutup
        CardContact = (ConstraintLayout) view.findViewById(R.id.card_contact); //mencari card yanag akan dijadikan trigger onClick
        arrowContact = (ImageView) view.findViewById(R.id.arrow_contact); //mencari arrow
        rotateArrowContact = false;
        CardContact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { //set onClick pada Card nya
                exContact.toggle(); //fungsi toggle agar otomastis expand terbuka & tertutup bergantian
                if (rotateArrowContact == false){
                    arrowContact.setRotation(180); //men-rotate arrow 180 derajat
                    rotateArrowContact = true;
                }else{
                    arrowContact.setRotation(0); //men-rotate arrow jadi normal
                    rotateArrowContact = false;
                }
            }
        });
        //End of Contact Card

        toasts = view.findViewById(R.id.feedback_email);
        toasts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { //set onClick pada Card nya
                composeEmail(new String[]{"zeitplan.dev@gmail.com"},"[Feedback for Zeitplan Team]");
            }
        });

        switchNotification = (Switch) view.findViewById(R.id.switch_notification);
        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    onPreferences();
                    loadPrefereces();
                } else {
                    offPreferences();
                    loadPrefereces();
                }
            }
        });


        contactEmail = view.findViewById(R.id.contact_email);
        contactEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { //set onClick pada Card nya
                contactEmail(new String[]{"zeitplan.dev@gmail.com"});
            }
        });

        contactWhatsapp = view.findViewById(R.id.contact_whatsapp);
        contactWhatsapp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { //set onClick pada Card nya
                intentWhatsapp();
            }
        });

        contactWhatsapp2 = view.findViewById(R.id.contact_whatsapp2);
        contactWhatsapp2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { //set onClick pada Card nya
                intentWhatsapp2();
            }
        });

        contactWhatsapp3 = view.findViewById(R.id.contact_whatsapp3);
        contactWhatsapp3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { //set onClick pada Card nya
                intentWhatsapp3();
            }
        });

        loadPrefereces();
        updatePreferences();

        return view;

    }

    private void onPreferences(){
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SWITCH_NOTIFICATION,switchNotification.isChecked());
        editor.apply();
    }

    private void offPreferences(){
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SWITCH_NOTIFICATION,false);
        editor.apply();
    }

    private void loadPrefereces(){
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        switchOnOff = sharedPreferences.getBoolean(SWITCH_NOTIFICATION,false);
//        Log.d("LoadPreferences",String.valueOf(switchOnOff));
    }

    private void updatePreferences(){
        switchNotification.setChecked(switchOnOff);
    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(appContext.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void contactEmail(String[] addresses) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        if (intent.resolveActivity(appContext.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void intentWhatsapp(){
        try{
            String toNumber = "+62 85325633101"; // contains spaces.
            toNumber = toNumber.replace("+", "").replace(" ", "");
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            // sendIntent.setComponent(new ComponentName(“com.whatsapp”, “com.whatsapp.Conversation”));
            sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,"Type your feedback");
            sendIntent.setPackage("com.whatsapp");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }catch (Exception e){
            return;
        }
    }

    private void intentWhatsapp2(){
        try{
            String toNumber = "+62 85867755150"; // contains spaces.
            toNumber = toNumber.replace("+", "").replace(" ", "");
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            // sendIntent.setComponent(new ComponentName(“com.whatsapp”, “com.whatsapp.Conversation”));
            sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,"Type your feedback");
            sendIntent.setPackage("com.whatsapp");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }catch (Exception e){
            return;
        }
    }

    private void intentWhatsapp3(){
        try{
            String toNumber = "+62 89655463831"; // contains spaces.
            toNumber = toNumber.replace("+", "").replace(" ", "");
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            // sendIntent.setComponent(new ComponentName(“com.whatsapp”, “com.whatsapp.Conversation”));
            sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,"Type your feedback");
            sendIntent.setPackage("com.whatsapp");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }catch (Exception e){
            return;
        }
    }
}
