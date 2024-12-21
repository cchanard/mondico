package com.infolangues.mondico;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by bonnet on 14/09/2015.
 * may be to merge with word_listActivity
 */
public class SearchResultActivity extends Activity {

    private static String colorDefFR = "#29577a";
    private static String colorDefEN = "#a37135";

    public static String getColorEN(){
        return colorDefEN;
    }

    public static String getColorFR(){
        return colorDefFR;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        setContentView(R.layout.word_list);
        //setContentView(com.llacan.cnrs.labex.dict_univ.R.layout.word_list);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/DoulosSILCompact-R.ttf");
        Intent intent = getIntent();

        colorDefFR = res.getString(R.string.textColor_FR);//codes couleurs du texte de chaque langue
        colorDefEN = res.getString(R.string.textColor_EN);
        String color = res.getString(R.string.SubEntry_Color);

        TextView textView = findViewById(R.id.textView);

        String search_type;
        String searched_string;
        String cb_state;
        String language;
        ArrayList<String> chosenCAT = new ArrayList<String>();
        ArrayList<String> chosenTH = new ArrayList<String>();

        if (intent != null) {
            search_type = intent.getStringExtra(MyApplication.CHOSEN_LETTER);
            searched_string = intent.getStringExtra(MyApplication.SEARCHED_STRING);
            cb_state = intent.getStringExtra(MyApplication.CHECKBOX_STATE);
            language = intent.getStringExtra(MyApplication.CHOSEN_LANGUAGE);
            chosenCAT = intent.getStringArrayListExtra("chosenCAT");
            chosenTH = intent.getStringArrayListExtra("chosenTH");
        } else {
            // @TODO improve error and exception handling
            search_type = "search_type?";
            searched_string = "searched_string?";
            cb_state = "false";
            language = "";
        }
        System.out.println(">>> search" + searched_string);
        textView.setText(Html.fromHtml("<b>"+searched_string+"</b>", Html.FROM_HTML_MODE_LEGACY));
        textView.setTypeface(custom_font);

        LinearLayout linearLayout4wb = findViewById(R.id.ll4wb);
        int back_color = linearLayout4wb.getDrawingCacheBackgroundColor();

        String[][] word_and_def_list;
        String[] world_list;

        word_and_def_list = dictData.getWordList4searchFromSQL(searched_string, search_type, cb_state, language, chosenCAT, chosenTH);
        world_list = word_and_def_list[0];

        final String[] def_list = (language == null) ? null : word_and_def_list[1];

        Button[] btn = new Button[world_list.length];
        int i = 0;
        int cpt = 0;
        for (i = 0; i < world_list.length; i++) {
            if(world_list[i]!=null){
                cpt++;          // CC 2205
                final String chosen_word = world_list[i];
                System.out.println(">>> searchResult : " + chosen_word);
                String subIndice="";
                int hmNumber = dictData.getHmNumber(chosen_word);
                char[] indices = {0x2080,0x2081, 0x2082,0x2083,0x2084,0x2085,0x2086,0x2087,0x2088,0x2089};//liste des caractères spéciaux à utiliser pour les indices d'homonyme number (~=sub), de 0 à 9
                if(hmNumber<10){
                    char c = indices[hmNumber];
                    subIndice = String.valueOf(c);
                }
                else{
                    char cDizaines = indices[(int) Math.floor((double) hmNumber / 10)];//récupérer les dizaines
                    char cUnites = indices[hmNumber-(int)(Math.floor((double) hmNumber / 10)*10)];//les unités
                    subIndice = String.valueOf(cDizaines)+ cUnites;
                }
                String hm = hmNumber!=0? subIndice :"";
                btn[i] = new Button(this);
                float tailleEntrees = 24.f;
                btn[i].setTextSize(tailleEntrees);//taille police
                btn[i].setTypeface(custom_font);
                //on colore les textes selon le code couleur correspondant à chaque langue :
                if (def_list == null) {
                    btn[i].setText(Html.fromHtml("<b><font color=\""+ color +"\">"  + dictData.getLocalForm4word(world_list[i]) + "</font>"+hm+"</b>"));
                } else {
                    if (dictData.getLocalForm4word(world_list[i]).equals(dictData.getLexeme4word(chosen_word)))
                        btn[i].setText(Html.fromHtml("<b><font color=\""+ color +"\">" + dictData.getLocalForm4word(world_list[i]) + "</font>"+hm+"</b> : " + def_list[i]));
                    else
                        btn[i].setText(Html.fromHtml("<b><font color=\""+ color +"\">" + dictData.getLocalForm4word(world_list[i]) + "</font>"+hm+"</b> [" + dictData.getLexeme4word(chosen_word) + "]: " +  def_list[i]));

                    btn[i].setGravity(Gravity.START);
                }


                btn[i].setBackgroundColor(back_color);
                btn[i].setTransformationMethod(null);
                linearLayout4wb.addView(btn[i]);
                btn[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchResultActivity.this, word_descriptionActivity.class);
                        intent.putExtra(MyApplication.CHOSEN_WORD, chosen_word);
                        startActivity(intent);
                    }
                });
            }
        }
        textView.setText(Html.fromHtml("<b>"+searched_string+" : "+ cpt +" results</b> "));//on change le titre pour afficher le nombre de résultats dedans.
    }
}
