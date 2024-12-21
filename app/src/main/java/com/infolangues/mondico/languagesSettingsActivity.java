package com.infolangues.mondico;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by bonnet on 14/09/2017.
 * display and modify languages options
 */
public class languagesSettingsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages_settings);

        Resources res = getResources();
        String[] language_names = res.getStringArray(R.array.language_names);

        final ArrayList<String> other_languages_list = new ArrayList<>(dictData.languages_list);
        other_languages_list.remove(0);

        for (String current_language : language_names) {
            if (current_language.charAt(current_language.length() - 1) == ':')
                other_languages_list.remove(current_language.substring(0, current_language.length() - 1));
        }

        final RelativeLayout rl= findViewById(R.id.head_relative_layout);
        View recentView = findViewById(R.id.textView4);
        boolean[] hidden_languages = dictData.getHidden_languages4gloss();
        addLanguageCheckBoxes(rl, other_languages_list, language_names, hidden_languages, recentView, 0);
        ImageButton button_home =findViewById(R.id.home_button);        // CC 021024
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(languagesSettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private View addLanguageCheckBoxes(RelativeLayout current_relative_layout, ArrayList<String> languages, String[] language_names, boolean[] hidden_languages, View recentView, int left_margin) {
        if (languages.size() == 0) return recentView;

        for (String current_lgg : languages) {
            System.err.println("hidden language possible : "+current_lgg);
            RelativeLayout.LayoutParams params4button = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            final int num_lgg = languages.indexOf(current_lgg);

            CheckBox lgg_check_box = new CheckBox(this);

            for (String language_name : language_names) {

                if (language_name.substring(0, languages.get(num_lgg).length() + 1).equals(languages.get(num_lgg) + ":")) {
                    lgg_check_box.setText(language_name.substring(language_name.indexOf(":") + 1));
                    break;
                }
            }

            if(lgg_check_box.getText().equals("")) lgg_check_box.setText(current_lgg);
            lgg_check_box.setTransformationMethod(null);

            lgg_check_box.setChecked(!hidden_languages[num_lgg]);

            lgg_check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    MainActivity.réinitialiserPagesLettres();//on réinitialise les pages sauvegardées, elles devront être rechargées avec les nouveaux paramètres de langue
                    if (isChecked) {    //on veut afficher cette langue
                        dictData.setHidden_languages4gloss(num_lgg, false);
                    } else {//on ne veut pas afficher cette langue
                        System.err.println("hidden language : "+num_lgg);
                        dictData.setHidden_languages4gloss(num_lgg, true);
                        if(areAllTrue(dictData.getHidden_languages4gloss()))
                        {
                            Context context = getApplicationContext();
                            Toast msg = Toast.makeText(context, context.getString(R.string.all_languages_hidden_message), Toast.LENGTH_LONG);
                            msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                            msg.show();
                            buttonView.setChecked(true);
                            dictData.setHidden_languages4gloss(num_lgg, false);
                        }
                    }
                }
            });

            params4button.addRule(RelativeLayout.BELOW, recentView.getId());
            params4button.setMargins(left_margin, 0, 0, 0);
            lgg_check_box.setLayoutParams(params4button);
            int id= recentView.getId();
            lgg_check_box.setId(id + 1);
            current_relative_layout.addView(lgg_check_box);

            recentView = lgg_check_box;
        }

        return recentView;
    }


    private static boolean areAllTrue(boolean[] array) {
        for(boolean b : array) if(!b) return false;
        return true;
    }

}
