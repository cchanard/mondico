package com.infolangues.mondico;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

// @TODO >> import android.widget.ListView;
//import java.util.List;

//import java.util.Map;

/**
 * Created by bonnet on 16/08/2015, edited by Maël Franceschetti
 * display a list of words (search result)
 */
public class word_listActivity extends Activity {

    private final String separateur = " • " ;
    private String color = "#00008B";//couleur des entrées dans la page principale
    private final float tailleEntrees = 24.f;//taille du texte des entrées dans la page principale
    private static String colorDefFR = "#29577a";
    private static String colorDefEN = "#a37135";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.word_list);

        Intent intent = getIntent();

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/DoulosSILCompact-R.ttf");

        TextView textView = findViewById(R.id.textView);
        Resources res = getResources();
        color = res.getString(R.string.SubEntry_Color);

        String letter, word, alphabet, thesaurus;
        if (intent != null) {
            letter = intent.getStringExtra(MyApplication.CHOSEN_LETTER);
            word = intent.getStringExtra(MyApplication.CHOSEN_WORD);
            alphabet = intent.getStringExtra(MyApplication.CHOSEN_ALPHABET);
            thesaurus = intent.getStringExtra(MyApplication.CHOSEN_THESAURUS);

        }  else {
            letter = "letter?";
            word = "word?";
            alphabet = "alphabet?";
            thesaurus = "thesaurus?";
        }

        if(letter != null) {
            textView.setText(letter);
            textView.setTypeface(custom_font);
        }  else if (word != null) {
            textView.setText(word);
            textView.setTypeface(custom_font);
        }  else {
            textView.setText(thesaurus);
            textView.setTypeface(custom_font);
        }
        MyApplication.Log("onCreate word_listActivity : after letter = " + letter + ", word = " + word + ", alphabet = " + alphabet);

        LinearLayout linearLayout4wb = findViewById(R.id.ll4wb);
        int back_color = linearLayout4wb.getDrawingCacheBackgroundColor();

        // @TODO >> ListView list_view4wb = (ListView)findViewById(R.id.lv4wb);

        String[][] world_list;

        if(letter != null) {
            String[][] citation_form_list = dictData.getCitationFormAndIDList(letter);
            if(citation_form_list.length == 0)
                world_list = citation_form_list;
            else
                world_list = dictData.getLexemeAndIDList(letter);

        } else if (word != null) {
            ArrayList<String> possible_local_words;
            if (alphabet.equals("ascii")) {
                possible_local_words = dictData.getWords4asciiWord(word);
                for (int i = 0; i < possible_local_words.size(); i++)
                    MyApplication.Log("onCreate word_listActivity : possible_local_words.get("+i+") = " + possible_local_words.get(i));

            } else {
                possible_local_words = new ArrayList<>();
                possible_local_words.add(word);
            }

            world_list = dictData.getCitationFormAndIDList(possible_local_words);

            // using 'lex' and not 'citation_form'
            if (world_list[0].length == 0) {
                world_list = dictData.getLexAndIDList(possible_local_words);
            }

        } else {
            world_list = dictData.getCitationFormAndIDListForThesaurus(thesaurus);
        }

         // linguist_version not used anymore }
        boolean[] hidden_languages = dictData.getHidden_languages4gloss();
        String colorDef = "#000000";// codes couleurs des langues
            colorDefFR = res.getString(R.string.textColor_FR);
            colorDefEN= res.getString(R.string.textColor_EN);

        for (int word_cpt = 0; word_cpt < world_list[0].length; word_cpt++) {
            String def="";
            if(!hidden_languages[0]){   //français pas masqué
                def+= "<font color=\""+colorDefFR+"\">"+world_list[2][word_cpt]+"</font>";
            }
            if(world_list[2][word_cpt].length()>0&&!hidden_languages[0]&&(hidden_languages.length>1&&!hidden_languages[1]&&world_list[3][word_cpt].length()>0)){//si les 2 affichés on les sépare
                def+= separateur;
            }
            if(hidden_languages.length>1&&!hidden_languages[1]){    //anglais pas masqué
                def+= "<font color=\""+colorDefEN+"\">"+world_list[3][word_cpt]+"</font>";
            }

            world_list[2][word_cpt] = def;//on met tous ça dans l'ancien glossaire multilingue pour garder le même traitement ensuite (ça marche très bien)
        }

        MyApplication.Log("onCreate word_listActivity : world_list[0].length = " + world_list[0].length);
        MyApplication.Log("onCreate word_listActivity : world_list[1].length = " + world_list[1].length);
        MyApplication.Log("onCreate word_listActivity : world_list[2].length = " + world_list[2].length);

        // liste des mots
        Button[] btn = new Button[world_list[0].length];
        for (int i = 0; i < world_list[0].length; i++) {
            //int i = Integer.parseInt(Order[x]);
            final String chosen_word = world_list[1][i];
            //btn[i] = new Button(this, null, android.R.attr.buttonBarButtonStyle);  // CC 1910
            btn[i] = new Button(this);
            int hmNumber = dictData.getHmNumber(chosen_word);
            String subIndice="";
            char[] indices = {0x2080,0x2081, 0x2082,0x2083,0x2084,0x2085,0x2086,0x2087,0x2088,0x2089};//caractères pour indices d'homonyme number, de 0 à 9
            if(hmNumber<10){
                char c = indices[hmNumber];//unités seulement
                subIndice = String.valueOf(c);
            }
            else{
                char cDizaines = indices[(int) Math.floor(hmNumber / 10)];//on récupère l'indice des dizaines
                char cUnites = indices[hmNumber-(int)(Math.floor(hmNumber / 10)*10)];//puis celui des unités
                subIndice = String.valueOf(cDizaines)+ cUnites;// et on concatène pour former le nombre complet en indice
            }
            String hm = hmNumber!=0? subIndice :"";
            btn[i].setText(Html.fromHtml("<b><font color=\""+color+"\">" + world_list[0][i] + "</font>"+hm+"</b> " + (world_list[2][i].equals("()") ? "" : world_list[2][i])));
            btn[i].setTypeface(custom_font);
            btn[i].setTextSize(tailleEntrees);

            btn[i].setBackgroundColor(back_color);
            btn[i].setTransformationMethod(null);
            btn[i].setGravity(Gravity.START);
            linearLayout4wb.addView(btn[i]);

            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(word_listActivity.this, word_descriptionActivity.class);
                    intent.putExtra(MyApplication.CHOSEN_WORD, chosen_word);
                    startActivity(intent);
                }
            });
        }
    }
}
