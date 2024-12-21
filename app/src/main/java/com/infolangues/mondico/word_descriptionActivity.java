package com.infolangues.mondico;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.text.HtmlCompat;  // CC 241210

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import java.util.concurrent.ExecutorService;        // CC 031024
import java.util.concurrent.Executors;

//import android.media.AudioManager;
//import android.text.Layout;
//import java.io.IOException;
//import java.net.URL;
//import java.util.Arrays;
//import android.widget.Toast;

/**
 * Created by Bonnet on 14/08/2015, edited by Maël Franceschetti
 * display a word description
 */
public class word_descriptionActivity extends Activity {
    private ExecutorService executorService;        // CC031024
    //private SwipeGestureDetector gestureDetector;       // CC 10082018
    private static final ArrayList<String> precedentSearches = new ArrayList<>();
    private int indexActuelDerniersMots = 0;
    private static boolean isRetour = false;
    private Typeface custom_font;
    public static int ExamplesFontSize = 22;//valeur par défaut
    public static int textSize_Title = 32;//valeur par défaut
    public static int SubEntry_TextSize = 32;//valeur par defaut
    public static int Definition_TextSize = 22;//valeur par defaut
    public static int Entry_TextSize = 32;//valeur par defaut
    public static int Usage_TextSize = 32;//valeur par defaut
    public static String SubEntry_Color = "#000000" ;//couleur par défaut
    public static String textStyle_Title = "";
    public static String textColor_Title = "#000000" ;//couleur par défaut
    public static String Definition_Color = "#000000" ;//couleur par défaut
    private static final String CHOSEN_WORD = "chosen_word";
    private final String word="word";
    private TextView modele;//modèle pour les styles des exemples en langue locale
    private TextView modele2;//modèle pour les styles des exemples en autres langues
    private static String localExampleColor;
    private static String otherExampleColorFR = "#000000";
    private static String otherExampleColorEN = "#000000";
    private static String localExampleStyle;
    private static String otherExampleStyle;

    private static int marginLeft_Usage;
    private static int marginRight_Usage;
    private static int marginTop_Usage;
    private static int marginBottom_Usage;

    private static int marginLeft_txtView;
    private static int marginRight_txtView;
    private static int marginTop_txtView;
    private static int marginBottom_txtView;

    private static int marginLeft_Thesaurus;
    private static int marginRight_Thesaurus;
    private static int marginTop_Thesaurus;
    private static int marginBottom_Thesaurus;

    private static int marginLeft_Header;
    private static int marginRight_Header;
    private static int marginTop_Header;
    private static int marginBottom_Header;

    private static int marginLeft_SenseLink;
    private static int marginRight_SenseLink;
    private static int marginTop_SenseLink;
    private static int marginBottom_SenseLink;

    private static int marginLeft_Title;
    private static int marginRight_Title;
    private static int marginTop_Title;
    private static int marginBottom_Title;

    private static int marginLeft_AudioBtn;
    private static int marginRight_AudioBtn;
    private static int marginTop_AudioBtn;
    private static int marginBottom_AudioBtn;

    private static int marginLeft_Illustration;
    private static int marginRight_Illustration;
    private static int marginTop_Illustration;
    private static int marginBottom_Illustration;

    private TextView textView;
    private String Word;

    Resources res;

    public static String getLocalExampleColor(){
        System.out.println(" ///////////// "+localExampleColor);
        return localExampleColor;
    }

    public static String getOtherExampleColorFR(){
        System.out.println(" ///////////// "+otherExampleColorFR);
        return otherExampleColorFR;
    }

    public static String getOtherExampleColorEN(){
        System.out.println(" ///////////// "+otherExampleColorEN);
        return otherExampleColorEN;
    }

    public static String getLocalExampleStyle(){
        System.out.println(" ///////////// st l : "+localExampleStyle);
        return localExampleStyle;
    }
    public static String getOtherExampleStyle(){
        System.out.println(" ///////////// st o : "+otherExampleStyle);
        return otherExampleStyle;
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_desc); // CC 0910
        custom_font = Typeface.createFromAsset(getAssets(), "fonts/DoulosSILCompact-R.ttf");
        modele = new TextView(this);//on créé un textView de modèle pour les exemples en langue locale
        modele.setTextAppearance(R.style.notice_ExamplesLocal);//on lui applique le style défini pour les exemples en langue locale
        localExampleColor = ColorToHex(modele.getCurrentTextColor());//on récupère la couleur de texte définie dans le style.xml
        System.out.println(" ///////////// " + localExampleColor);
        int styleLocal = modele.getTypeface() != null ? modele.getTypeface().getStyle() : Typeface.NORMAL;//on récupère le style (italique, normal ou gras) pour les exemples en langue locale
        System.out.println(" ///////////// int st l : " + styleLocal);
        if (styleLocal == Typeface.BOLD) {
            localExampleStyle = "b";
        } else if (styleLocal == Typeface.ITALIC) {
            localExampleStyle = "i";
        } else {
            localExampleStyle = "";
        }

        modele2 = new TextView(this);//on créé un textView de modèle pour les exemples en français et anglais
        modele2.setTextAppearance(R.style.notice_ExamplesOther);//on lui applique le style défini pour les exemples en autres langues
        System.out.println(" ///////////// " + otherExampleColorFR);
        int styleOther = modele2.getTypeface().getStyle();//on récupère le style (italique, normal ou gras) pour les exemples dans d'autres langues
        System.out.println(" ///////////// int st o : " + styleOther);
        if (styleOther == Typeface.BOLD) {
            otherExampleStyle = "b";
        } else if (styleOther == Typeface.ITALIC) {
            otherExampleStyle = "i";
        } else {
            otherExampleStyle = "";
        }

        //on récupère les dimensions (margins) à appliquer aux blocs d'éléments textView de la notice (définition + examples):
        res = getResources();
        otherExampleColorFR = res.getString(R.string.textColor_FR);
        otherExampleColorEN = res.getString(R.string.textColor_EN);
        textSize_Title = res.getInteger(R.integer.textSize_Title);
        textColor_Title = res.getString(R.string.textColor_Title);
        textStyle_Title = res.getString(R.string.textStyle_Title);
        SubEntry_Color = res.getString(R.string.SubEntry_Color);
        Definition_Color = res.getString(R.string.Definition_Color);
        Definition_TextSize = res.getInteger(R.integer.Definition_TextSize);
        Entry_TextSize = res.getInteger(R.integer.Entry_TextSize);
        Usage_TextSize = res.getInteger(R.integer.Usage_TextSize);
        SubEntry_TextSize = res.getInteger(R.integer.SubEntry_TextSize);
        ExamplesFontSize  = res.getInteger(R.integer.text_size_examples);
        marginLeft_txtView = res.getInteger(R.integer.marginLeft_notice_TextView);
        marginRight_txtView = res.getInteger(R.integer.marginRight_notice_TextView);
        marginTop_txtView = res.getInteger(R.integer.marginTop_notice_TextView);
        marginBottom_txtView = res.getInteger(R.integer.marginBottom_notice_TextView);
        System.out.println(" %%%%%%%%%%%%%%%%% padding Left txtView = : " + marginLeft_txtView);
        // on récupère les dimentions à appliquer aux usages :
        marginLeft_Usage = res.getInteger(R.integer.marginLeft_notice_Usage);
        marginRight_Usage = res.getInteger(R.integer.marginRight_notice_Usage);
        marginTop_Usage = res.getInteger(R.integer.marginTop_notice_Usage);
        marginBottom_Usage = res.getInteger(R.integer.marginBottom_notice_Usage);
        //on récupère les dimensions (margins) à appliquer au Thésaurus :
        marginLeft_Thesaurus = res.getInteger(R.integer.marginLeft_notice_Thesaurus);
        marginRight_Thesaurus = res.getInteger(R.integer.marginRight_notice_Thesaurus);
        marginTop_Thesaurus = res.getInteger(R.integer.marginTop_notice_Thesaurus);
        marginBottom_Thesaurus = res.getInteger(R.integer.marginBottom_notice_Thesaurus);
        //on récupère les dimensions (margins) à appliquer au Header(PoS+Lemma):
        marginLeft_Header = res.getInteger(R.integer.marginLeft_notice_Header);
        marginRight_Header = res.getInteger(R.integer.marginRight_notice_Header);
        marginTop_Header = res.getInteger(R.integer.marginTop_notice_Header);
        marginBottom_Header = res.getInteger(R.integer.marginBottom_notice_Header);
        //on récupère les dimensions (margins) à appliquer au SenseLink :
        marginLeft_SenseLink = res.getInteger(R.integer.marginLeft_notice_SenseLink);
        marginRight_SenseLink = res.getInteger(R.integer.marginRight_notice_SenseLink);
        marginTop_SenseLink = res.getInteger(R.integer.marginTop_notice_SenseLink);
        marginBottom_SenseLink = res.getInteger(R.integer.marginBottom_notice_SenseLink);
        //pour le titre (l'entrée) :
        marginLeft_Title = res.getInteger(R.integer.marginLeft_notice_Title);
        marginRight_Title = res.getInteger(R.integer.marginRight_notice_Title);
        marginTop_Title = res.getInteger(R.integer.marginTop_notice_Title);
        marginBottom_Title = res.getInteger(R.integer.marginBottom_notice_Title);
        //pour le bouton audio :
        marginLeft_AudioBtn = res.getInteger(R.integer.marginLeft_notice_AudioBtn);
        marginRight_AudioBtn = res.getInteger(R.integer.marginRight_notice_AudioBtn);
        marginTop_AudioBtn = res.getInteger(R.integer.marginTop_notice_AudioBtn);
        marginBottom_AudioBtn = res.getInteger(R.integer.marginBottom_notice_AudioBtn);
        //pour les illustrations :
        marginLeft_Illustration = res.getInteger(R.integer.marginLeft_notice_Illustration);
        marginRight_Illustration = res.getInteger(R.integer.marginRight_notice_Illustration);
        marginTop_Illustration = res.getInteger(R.integer.marginTop_notice_Illustration);
        marginBottom_Illustration = res.getInteger(R.integer.marginBottom_notice_Illustration);

       // gestureDetector = new SwipeGestureDetector( this);  // CC 16082018
        if (savedInstanceState != null) {
            Word = savedInstanceState.getString(word);
        } else {
            Intent intent = getIntent();
            if (intent != null) {
                Word = intent.getStringExtra(CHOSEN_WORD);
            }
        }
        afficherNoticeMot(Word);
    }
    @Override  //MARCHE PAS
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(word, CHOSEN_WORD);  // Sauvegarder le mot actuel dans l'état
    }
    /*
     * @function affiche les champs d'une entrée et les éléments sur la page
     */
    private void afficherNoticeMot(String word){
        precedentSearches.add(word);//pour la fonction "retour au mot consulté précédemment"
        if (isRetour) {
            //indexActuelDerniersMots += 1;//si on a fait "retour" pour charger ce mot, on incrémente l'index
            isRetour = false;//et on considère ce mot comme la nouvelle page actuelle (on quitte le mode "retour")
        } else {
            indexActuelDerniersMots = (precedentSearches.size()) - 1;//sinon, on va sur le dernier mot qu'on vient d'enregistrer (l'actuel)
        }
        int prevWord = Integer.parseInt(word) - 1;   // CC
        int nxtWord = Integer.parseInt(word) + 1;    // CC

        System.err.println("word : " + word + " nextWord : " + nxtWord + " previousWord : " + prevWord );
        String word_nb = dictData.findMainEntry(word);  // numéro d'item/nb de target identiques
        // CC ---------- pour sauter les (nb) sous-items
        int iSlash = word_nb.indexOf("/");
        int saute = 0;
        if (iSlash > -1){       //
            word = word_nb.substring(0, iSlash);
            String nbSe = word_nb.substring(iSlash+1);
            saute = Integer.parseInt(nbSe) ;
            System.err.println("saute : " + saute);
        }

        final String nextWord = Integer.toString(nxtWord + saute);
        final String previousWord = Integer.toString(prevWord - saute);   // CC  ---------

        System.err.println("nextWord : " + nextWord);
        MyApplication.Log("word_descriptionActivity create >> word = " + word);

        // retour au mot précédemment affiché
        ImageButton button_prec = findViewById(R.id.precedent_button);
        button_prec.getBackground().setAlpha(1);
        button_prec.setVisibility(View.GONE);

        if(precedentSearches!=null && precedentSearches.size()>1&&indexActuelDerniersMots>0) {
            button_prec.setVisibility(View.VISIBLE);
            button_prec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retourDernierMot();
                }
            });
        }

        // affiche le mot suivant
        ImageButton button_next = findViewById(R.id.next_button);
        button_next.getBackground().setAlpha(1);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(word_descriptionActivity.this, word_descriptionActivity.class);
                intent.putExtra(MyApplication.CHOSEN_WORD, nextWord);
                startActivity(intent);
            }
        });

        // affiche le mot précédent
        ImageButton button_previous = findViewById(R.id.previous_button);
        button_previous.getBackground().setAlpha(1);
        button_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(word_descriptionActivity.this, word_descriptionActivity.class);
                intent.putExtra(MyApplication.CHOSEN_WORD, previousWord);
                startActivity(intent);
            }
        });

        // retour à la page liste des mots (page d'entrée)
        ImageButton button_home = findViewById(R.id.home_button);
        button_home.getBackground().setAlpha(1);
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(word_descriptionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        String title;  // le mot lui-même
        title = dictData.getLocalForm4word(word);
        // copie le mot dans l'entête
        TextView textView = findViewById(R.id.lexeme);
        //textView.setText(Html.fromHtml(title));
        textView.setText(HtmlCompat.fromHtml(title,HtmlCompat.FROM_HTML_MODE_LEGACY));
        String titleStyleBaliseOpen = "";
        String titleStyleBaliseClose = "";
        if(textStyle_Title.equals("b")||textStyle_Title.equals("i")){
            titleStyleBaliseOpen = "<"+textStyle_Title+">";
            titleStyleBaliseClose = "</"+textStyle_Title+">";
        }
        //
        textView = findViewById(R.id.textViewW);
        //textView.setText(Html.fromHtml(titleStyleBaliseOpen+title+titleStyleBaliseClose));
        textView.setTextSize(textSize_Title);
        textView.setTypeface(custom_font);
        textView.setTextColor(Color.parseColor(textColor_Title));

        ArrayList<ArrayList<AudioRepresentation>> ar_tab_list;
        ArrayList<ArrayList<Illustration>> illustration_tab_list;
        ar_tab_list = dictData.getAudioRepresentationList(word);
        illustration_tab_list = dictData.getIllustrationList(word);

        // @TODO use generateViewId() if the app support API level 17 and more
        String lexeme = dictData.getLexeme4word(word) + "<br/>";

        //final RelativeLayout rl= findViewById(R.id.descriptionLayout);
        final RelativeLayout rl= findViewById(R.id.word_relative_layout);        //CC

        //View recentView = findViewById(R.id.head_relative_layout);
        View recentView = findViewById(R.id.text_relative_layout);


        final TextView gramTextView = new TextView(this);
        RelativeLayout.LayoutParams gram_params=new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // part of speech
        /*
        String gramText="";
        //gramText = dictData.getGrammarFromSQL(word); MyApplication.Log("§§§§%%%%$$$$****~~~~~°°°° gramText = " + gramText);
        int initialID = 732; // magic constant...*
        gramTextView.setId(initialID + 1);
        gramTextView.setText(Html.fromHtml((dictData.getLexeme4word(word).equals(dictData.getLocalForm4word(word)) ? "" : lexeme) + gramText));
          gramTextView.setTypeface(custom_font);
        gram_params.addRule(RelativeLayout.BELOW, recentView.getId());
        gramTextView.setLayoutParams(gram_params);
        if(gramTextView.getText().toString().trim().equals("")) {gramTextView.setVisibility(View.GONE);}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            gramTextView.setTextAppearance(R.style.notice_Grammar);
        }
        rl.addView(gramTextView);
        recentView = gramTextView;
        */
        //View recentView = new TextView(this);

        // @TODO call getVariantsStringFromSQL and use it in gramTextView.setText

        Context context = MyApplication.getAppContext();

        int num_entry = 0;
        String sense_text;
        String example_text;

        //variantes CC
        String variantes = dictData.getVariantes(word);
        if (variantes != "") { variantes = " ~ " + variantes;}

        ArrayList<String> idEntrys =  dictData.getEntryId(word);
        Map<Integer, String> entry_list = dictData.getEntry4word(word);
        System.out.println("****************** Entrées ********************");
        for (Map.Entry<Integer, String> current_entry : entry_list.entrySet()) {
            int hmNumber = dictData.getHmNumber(idEntrys.get(num_entry));
            String subIndice="";
            char[] indices = {0x2080,0x2081, 0x2082,0x2083,0x2084,0x2085,0x2086,0x2087,0x2088,0x2089};//liste des caractères à utiliser comme indice d'homonyme number (de 0 à 9)
            if(hmNumber<10){
                char c = indices[hmNumber];
                subIndice = String.valueOf(c);
            }
            else{
                char cDizaines = indices[(int) Math.floor(hmNumber / 10)];//on récupère l'indice des dizaines
                char cUnites = indices[hmNumber-(int)(Math.floor(hmNumber / 10)*10)];//puis celui des unités
                subIndice = String.valueOf(cDizaines)+ cUnites;// et on concatène pour former le nombre complet en indice
            }
            String hm = hmNumber!=0? subIndice :"";
            textView.setTextSize(textSize_Title);
            //textView.setText(Html.fromHtml(titleStyleBaliseOpen+"&nbsp;"+title+titleStyleBaliseClose+hm+variantes));
            textView.setText(HtmlCompat.fromHtml(titleStyleBaliseOpen+"&nbsp;"+title+titleStyleBaliseClose+hm+variantes,HtmlCompat.FROM_HTML_MODE_LEGACY));
            System.out.println("****************** illustration, audio ********************");
            if (haveInternetConnection()){
                recentView = addIllustrations(rl, illustration_tab_list, num_entry,  context, recentView);
            }
            recentView = addAudioButtons(rl, ar_tab_list, num_entry, context, recentView);
            String lex_note_string = dictData.getLexNoteString(word);

            // renvoie la catégorie gram
            recentView = addHeader(rl, recentView, dictData.create_entry_header(word, current_entry)+ lex_note_string);

            System.out.println("****************** Sens ********************");
            ArrayList<String> sense_id_list = dictData.getSenseIdList4Entry(word, current_entry);
            for (int cpt_sense = 0; cpt_sense < sense_id_list.size(); cpt_sense++)
            {
                sense_text = dictData.create_sense(sense_id_list, cpt_sense);
                example_text = dictData.create_examples(sense_id_list, cpt_sense);
                example_text = example_text.replace("{","'");       // CC 2205
                example_text = example_text.replace("}","'");
                if (sense_text.equals("<h3></h3>")) continue;

                // examples
                System.out.println("****************** Exemples ********************");
                recentView = addSensTextView(rl, recentView, sense_text);
                recentView = addExampleTextView(rl, recentView, example_text.replaceAll("(<br/>)+", "<br/>"));

                // usages
                System.out.println("****************** Usages ********************");
                String usageFR = dictData.getUsage(sense_id_list.get(cpt_sense), "fra");//on ajoute les ue
                usageFR = usageFR.replaceAll("\\{(.*?)\\}", "<i>$1</i>");       // CC 200911
                String usageEN = dictData.getUsage(sense_id_list.get(cpt_sense), "eng");//on ajoute les un
                usageEN = usageEN.replaceAll("\\{(.*?)\\}", "<i>$1</i>");
                if (usageFR.length() > 1 || usageFR.length() > 1){
                    String symbol = "&#9654;";
                    String usagesFormated = (usageFR.length() > 1 ? symbol + " " + usageFR : "") + (usageFR.length() > 1 && usageEN.length() > 1 ? "<br/>" : "") + (usageEN.length() > 1 ? symbol + " " + usageEN : "");
                    recentView = addUsage(rl, recentView, usagesFormated);//(pour éviter que les 2 soient trop espacés), on ajoute les triangles (&#9658;)
                }
                // thesaurus
                System.out.println("****************** thesaurus ********************");
                ArrayList<String> thesaurus_list = dictData.getThesaurus(sense_id_list.get(cpt_sense));
                for (String thesaurus : thesaurus_list){
                    recentView = addSenseThesaurus(rl, recentView, thesaurus);
                }

                // links
                System.out.println("*********** senselinks ************");
                ArrayList<String[]> sense_links_list = dictData.getSenseLinks(sense_id_list.get(cpt_sense));
                for (String[] sense_link : sense_links_list){
                    recentView = addSenseLink(rl, recentView, sense_link);
                }
            }
            num_entry++;
        }
        System.out.println("*********** sous-entrées ************");
        // déplacé en dehors de la boucle précédente : subEntry sous Entry pas sous Sense CC 0602
        ArrayList<String> current_sub_entry_list = dictData.getSubEntryList(word);
        for (String sub_entry : current_sub_entry_list) {
            String[] formatted_entries4sub_entry = dictData.format_definition_in_splinter(sub_entry);
            String[] formatted_examples4sub_entry = dictData.format_examples_in_splinter(sub_entry);
            recentView = addSubEntryView(rl, recentView, "&nbsp;▪ " + dictData.getLocalForm4word(sub_entry) , dictData.getVariantes(sub_entry));  // CC 1810

            ArrayList<ArrayList<AudioRepresentation>> se_ar_tab_list = dictData.getAudioRepresentationList(sub_entry);
            ArrayList<ArrayList<Illustration>> se_illustration_tab_list = dictData.getIllustrationList(sub_entry);

            int num_description = 0;
            for (int i =0; i<formatted_entries4sub_entry.length; i++) {
                if (haveInternetConnection()) {
                    recentView = addIllustrations(rl, se_illustration_tab_list, 0, context, recentView);
                }
                recentView = addAudioButtons(rl, se_ar_tab_list, num_description++, context, recentView);
                recentView = addSensTextView(rl, recentView, formatted_entries4sub_entry[i]);
                recentView = addExampleTextView(rl, recentView,formatted_examples4sub_entry[i]);
            }
        }

        Button feedback_button =  new Button(this);
        //feedback_button.setText(Html.fromHtml(context.getString(R.string.user_feedback_message)));
        feedback_button.setText(HtmlCompat.fromHtml(context.getString(R.string.user_feedback_message),HtmlCompat.FROM_HTML_MODE_LEGACY));
        feedback_button.setTransformationMethod(null);
        final String feedback_word = word;
        feedback_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(word_descriptionActivity.this, EmbeddedRecorderActivity.class);
                intent.putExtra(CHOSEN_WORD, feedback_word);
                startActivity(intent);
            }
        });
        RelativeLayout.LayoutParams params4feedback_button = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params4feedback_button.addRule(RelativeLayout.BELOW, recentView.getId());
        feedback_button.setLayoutParams(params4feedback_button);
        int id = recentView.getId();
        feedback_button.setId(id + 1);
        rl.addView(feedback_button);

    }


    @SuppressLint("NewApi")
    private View addSensTextView(RelativeLayout current_relative_layout, View precedent_view, String text){
    //private View addSensTextView(LinearLayout current_relative_layout, View precedent_view, String text){
        TextView current_text_view = new TextView(this);
        //current_text_view.setText(Html.fromHtml("<b>"+text+"</b>"));
        current_text_view.setText(HtmlCompat.fromHtml("<b>"+text+"</b>",HtmlCompat.FROM_HTML_MODE_LEGACY));
        RelativeLayout.LayoutParams params4sub_entry=new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params4sub_entry.setMargins(marginLeft_txtView, marginTop_txtView, marginRight_txtView, marginBottom_txtView);
        params4sub_entry.addRule(RelativeLayout.BELOW, precedent_view.getId());
        current_text_view.setLayoutParams(params4sub_entry);
        int id = precedent_view.getId();
        current_text_view.setId( id+1);
        current_text_view.setTypeface(custom_font);
        current_text_view.setTextSize(Definition_TextSize);
        current_text_view.setTextColor(Color.parseColor(Definition_Color));

        if(text.length()>0) {
            current_relative_layout.addView(current_text_view);
            return current_text_view;
        }
        return precedent_view;
    }

    @SuppressLint("NewApi")
    private View addSubEntryView(RelativeLayout current_relative_layout, View precedent_view, String text, String variantes) {  // CC 1810
    //private View addSubEntryView(LinearLayout current_relative_layout, View precedent_view, String text) {
        if(variantes != "") { variantes = " ~ " + variantes;}
        TextView current_text_view = new TextView(this);
        //current_text_view.setText(Html.fromHtml("<b>" + text.replaceAll("(<br/>)+", "<br/>") + "</b>" + variantes));
        current_text_view.setText(HtmlCompat.fromHtml("<b>" + text.replaceAll("(<br/>)+", "<br/>") + "</b>" + variantes,HtmlCompat.FROM_HTML_MODE_LEGACY));
        RelativeLayout.LayoutParams params4sub_entry=new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params4sub_entry.setMargins(0/*marginLeft_txtView*/, marginTop_txtView, marginRight_txtView, 0);
        params4sub_entry.addRule(RelativeLayout.BELOW, precedent_view.getId());
        current_text_view.setLayoutParams(params4sub_entry);
        int id = precedent_view.getId();
        current_text_view.setId( id+1);
        current_text_view.setTypeface(custom_font);
        current_text_view.setTextColor(Color.parseColor(SubEntry_Color));
        current_text_view.setTextSize(SubEntry_TextSize);
        if(text.length()>0) {
            current_relative_layout.addView(current_text_view);
            return current_text_view;
        }
        return precedent_view;
    }

    @SuppressLint("NewApi")
    private View addExampleTextView(RelativeLayout current_relative_layout, View precedent_view, String text) {
    //private View addExampleTextView(LinearLayout current_relative_layout, View precedent_view, String text) {
        TextView current_text_view = new TextView(this);
        //current_text_view.setText(Html.fromHtml(text.replaceAll("(<br/>)+", "<br/>")));
        current_text_view.setText(HtmlCompat.fromHtml(text.replaceAll("(<br/>)+", "<br/>"),HtmlCompat.FROM_HTML_MODE_LEGACY));
        RelativeLayout.LayoutParams params4sub_entry=new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params4sub_entry.setMargins(marginLeft_txtView, marginTop_txtView, marginRight_txtView, marginBottom_txtView);
        params4sub_entry.addRule(RelativeLayout.BELOW, precedent_view.getId());
        current_text_view.setLayoutParams(params4sub_entry);
        int id = precedent_view.getId();
        current_text_view.setId( id+1);
        current_text_view.setTypeface(custom_font);
        current_text_view.setTextSize(ExamplesFontSize);

        if(text.length()>0) {
            current_relative_layout.addView(current_text_view);
            return current_text_view;
        }
        return precedent_view;
    }

    @SuppressLint("NewApi")
    private View addHeader(RelativeLayout current_relative_layout, View precedent_view, String text)//now, only used to put the PartOfSpeech under the title of the notice{
    //private View addHeader(LinearLayout current_relative_layout, View precedent_view, String text)//now, only used to put the PartOfSpeech under the title of the notice
        {
        TextView current_text_view = new TextView(this);
        //current_text_view.setText(Html.fromHtml(text.replaceAll("(<br/>)+", "<br/>")));
        current_text_view.setText(HtmlCompat.fromHtml(text.replaceAll("(<br/>)+", "<br/>"),HtmlCompat.FROM_HTML_MODE_LEGACY));
        RelativeLayout.LayoutParams params4sub_entry=new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params4sub_entry.setMargins(marginLeft_Header, marginTop_Header, marginRight_Header, marginBottom_Header);
        params4sub_entry.addRule(RelativeLayout.BELOW, precedent_view.getId());
        current_text_view.setLayoutParams(params4sub_entry);
        int id = precedent_view.getId();
        current_text_view.setId(id+1);
        current_text_view.setTypeface(custom_font);//
        current_text_view.setTextSize(Definition_TextSize);
        current_text_view.setTextColor(Color.parseColor(Definition_Color));
        if(text.length()>0) {
            current_relative_layout.addView(current_text_view);
            return current_text_view;
        }
        return precedent_view;
    }


    @SuppressLint("NewApi")
    private View addUsage(RelativeLayout current_relative_layout, View precedent_view, String text){ //now, only used to put the PartOfSpeech under the title of the notice
    //private View addUsage(LinearLayout current_relative_layout, View precedent_view, String text){ //now, only used to put the PartOfSpeech under the title of the notice
        TextView current_text_view = new TextView(this);
        //current_text_view.setText(Html.fromHtml(text));
        current_text_view.setText(HtmlCompat.fromHtml(text,HtmlCompat.FROM_HTML_MODE_LEGACY));
        RelativeLayout.LayoutParams params4sub_entry=new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params4sub_entry.setMargins(marginLeft_Usage, marginTop_Usage, marginRight_Usage, marginBottom_Usage);
        params4sub_entry.addRule(RelativeLayout.BELOW, precedent_view.getId());
        current_text_view.setLayoutParams(params4sub_entry);
        int id = precedent_view.getId();
        current_text_view.setId( id+1);
        current_text_view.setTypeface(custom_font);//
        current_text_view.setTextSize(Usage_TextSize);
        if(text.length()>"<br/>".length()) {
            current_relative_layout.addView(current_text_view);
            return current_text_view;
        }
        return precedent_view;
    }

    // CC à revoir
    private View addDivider(RelativeLayout current_relative_layout, View precedent_view){
        TextView current_text_view = new TextView(this);
        RelativeLayout.LayoutParams params4sub_entry = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params4sub_entry.addRule(RelativeLayout.BELOW, precedent_view.getId());
        params4sub_entry.height = 1;
        current_text_view.setLayoutParams(params4sub_entry);
        current_relative_layout.addView(current_text_view);
        return current_relative_layout;
    }

    @SuppressLint("NewApi")
    private View addSenseLink(RelativeLayout current_relative_layout, View precedent_view, String[] link)  {
    //private View addSenseLink(LinearLayout current_relative_layout, View precedent_view, String[] link)  {
        final String chosen_word = link[2];
        String text = dictData.formatLink(link);
        Button current_button_link =new Button(this);
        //current_button_link.setText(Html.fromHtml("<b>"+text+"</b>"));
        current_button_link.setText(HtmlCompat.fromHtml(text,HtmlCompat.FROM_HTML_MODE_LEGACY));
        RelativeLayout.LayoutParams params4sub_entry=new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params4sub_entry.setMargins( marginLeft_SenseLink,  marginTop_SenseLink,  marginRight_SenseLink,  marginBottom_SenseLink);
        params4sub_entry.addRule(RelativeLayout.BELOW, precedent_view.getId());
        current_button_link.setLayoutParams(params4sub_entry);
        MyApplication.Log("addSenseLink ►►►► text = " + text + " precedent_view.getId() = " + precedent_view.getId());

        current_button_link.setBackgroundColor(current_relative_layout.getDrawingCacheBackgroundColor());
        current_button_link.setTransformationMethod(null);

        current_button_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(word_descriptionActivity.this, word_descriptionActivity.class);
                intent.putExtra(MyApplication.CHOSEN_WORD, chosen_word);
                startActivity(intent);
            }
        });
        int id = precedent_view.getId();
        current_button_link.setId( id+1);
        current_button_link.setTextAppearance(R.style.notice_SenseLink);//on lie au style définir dans style.xml
        current_button_link.setTypeface(custom_font);
        /* ***** */
        if(text.length()>0&&link[0].length()>0&&link[1].length()>0&&link[2].length()>0) {//on n'affiche pas le cf si il n'est pas fonctionnel ou vide!
            current_relative_layout.addView(current_button_link);
            return current_button_link;
        }
        return precedent_view;
    }



@SuppressLint("NewApi")
private View addSenseThesaurus(RelativeLayout current_relative_layout, View precedent_view, final String thesaurus){
//private View addSenseThesaurus(LinearLayout current_relative_layout, View precedent_view, final String thesaurus){
    Button current_button_link =new Button(this);
    //current_button_link.setText(Html.fromHtml("<b>→ th : "+thesaurus.replaceAll("(<br/>)+", "<br/>")+"</b>"));
    current_button_link.setText(HtmlCompat.fromHtml("<b>→ th : "+thesaurus.replaceAll("(<br/>)+", "<br/>"),HtmlCompat.FROM_HTML_MODE_LEGACY));
    RelativeLayout.LayoutParams params4sub_entry=new RelativeLayout.LayoutParams
            (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    params4sub_entry.setMargins(marginLeft_Thesaurus, marginTop_Thesaurus, marginRight_Thesaurus, marginBottom_Thesaurus);
    params4sub_entry.addRule(RelativeLayout.BELOW, precedent_view.getId());
    current_button_link.setLayoutParams(params4sub_entry);

    current_button_link.setBackgroundColor(current_relative_layout.getDrawingCacheBackgroundColor());
    current_button_link.setTransformationMethod(null);

    current_button_link.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(word_descriptionActivity.this, word_listActivity.class);
            intent.putExtra(MyApplication.CHOSEN_THESAURUS, thesaurus);
            startActivity(intent);
        }
    });
    int id = precedent_view.getId();
    current_button_link.setId(id +1);
    current_button_link.setTextAppearance(R.style.notice_Thesaurus);//on lie au style définir dans style.xml
    current_button_link.setTypeface(custom_font);
    if(thesaurus.length()>0) {
        current_relative_layout.addView(current_button_link);
        return current_button_link;
    }
    return precedent_view;
}

    private View addAudioButtons(RelativeLayout current_relative_layout, ArrayList<ArrayList<AudioRepresentation>> ar_tab_list, int num_entry, final Context context, View recentView) {
    //private View addAudioButtons(LinearLayout current_relative_layout, ArrayList<ArrayList<AudioRepresentation>> ar_tab_list, int num_entry, final Context context, View recentView) {
        if (ar_tab_list.size() > 0 && ar_tab_list.get(num_entry) != null) {
            for (int ar_count = 0; ar_count < ar_tab_list.get(num_entry).size(); ar_count++) {
                RelativeLayout.LayoutParams params4button = new RelativeLayout.LayoutParams
                        (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                final Button audio_btn = new Button(this);
                final String raw_sound_file = ar_tab_list.get(num_entry).get(ar_count).fileName;
                String sound_file = ar_tab_list.get(num_entry).get(ar_count).fileName.substring(0, ar_tab_list.get(num_entry).get(ar_count).fileName.length() - 4);
                sound_file = sound_file.replace('-', '_');
                sound_file = sound_file.toLowerCase();

                sound_file = dictData.getSoundFileNormalizedName(ar_tab_list.get(num_entry).get(ar_count).fileName);

                //audio_btn.setText(Html.fromHtml("<b>&#x1f50a;</b>"));
                audio_btn.setText(HtmlCompat.fromHtml("<b>&#x1f50a;</b>",HtmlCompat.FROM_HTML_MODE_LEGACY));
                // @TODO use(?) >>>> audio_btn.setText(Html.fromHtml("<b>&#x23f9;</b>"));
                audio_btn.setTransformationMethod(null);

                Resources res = context.getResources();

                int sound_id = res.getIdentifier(sound_file, "raw", context.getPackageName());

                if (sound_id == 0) {
                    sound_id = res.getIdentifier(dictData.getSoundFileNormalizedName(raw_sound_file), "raw", context.getPackageName());
                    if (sound_id == 0) {
                        sound_id = res.getIdentifier("gong", "raw", context.getPackageName());
                    }
                }

                final MediaPlayer mp;
                final boolean sound_id_not_found; // = MyApplication.offline_dictionary && (sound_id == res.getIdentifier("gong", "raw", context.getPackageName()));

                if(MyApplication.offline_dictionary)
                {
                    sound_id_not_found = (sound_id == res.getIdentifier("gong", "raw", context.getPackageName()));
                    mp = MediaPlayer.create(this, sound_id);
                }
                else
                {
                    sound_id_not_found = false;
                    final Uri sound_file_uri = Uri.parse(context.getString(R.string.audio_files_location) + ar_tab_list.get(num_entry).get(ar_count).fileName + "." + ar_tab_list.get(num_entry).get(ar_count).audioFileFormat);
                    mp = new MediaPlayer();

                    try {
                        MyApplication.Log(">>>> fct addAudioButtons a" + sound_file_uri + " " + sound_file_uri.toString());
                        try {
                        mp.setDataSource(context, sound_file_uri);
                        }
                        catch (Error error) {
                            System.err.println("$$ kk -> " + error);
                        }
                        MyApplication.Log(">>>> fct addAudioButtons b");
                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        MyApplication.Log(">>>> fct addAudioButtons c");
                        try {
                            //mp.prepare(); //don't use prepareAsync for mp3 playback
                            mp.prepareAsync();
                        }
                        catch (Error error) {
                            System.err.println("$$ -> " + error);
                        }
                        MyApplication.Log(">>>> fct addAudioButtons d");
                    }
                    catch (IOException e) {
                    e.printStackTrace();
                    }

                }

                // for test
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        //audio_btn.setText(Html.fromHtml("<b>&#x1f50a;</b>"));
                        audio_btn.setText(HtmlCompat.fromHtml("<b>&#x1f50a;</b>",HtmlCompat.FROM_HTML_MODE_LEGACY));
                    }
                });
                audio_btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Playing sound...
                        // @TODO use this fct for positive values in startPosition
                        //mp.seekTo(4000);
                        // @TODO right now the gong is mute
                        //mp.start();
                        if(sound_id_not_found) {
                            Toast msg = Toast.makeText(context, raw_sound_file + context.getString(R.string.missing_audio_file_message), Toast.LENGTH_LONG);
                            msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                            msg.show();
                        }
                        else {
                            if (mp.isPlaying() || mp.getCurrentPosition() > 1) {
                                mp.pause();


                                mp.seekTo(0);
                                //audio_btn.setText(Html.fromHtml("<b>&#x1f50a;</b>"));
                                audio_btn.setText(HtmlCompat.fromHtml("<b>&#x1f50a;</b>",HtmlCompat.FROM_HTML_MODE_LEGACY));
                            } else {
                                try {
                                    mp.start();
                                } catch (IllegalArgumentException | IllegalStateException e) {
                                    e.printStackTrace();
                                } catch (Error e) {
                                    System.err.println("€€ " + e);
                                }
                                //audio_btn.setText(Html.fromHtml("<b>&#x25a0;</b>"));
                                audio_btn.setText(HtmlCompat.fromHtml("<b>&#x25a0;</b>",HtmlCompat.FROM_HTML_MODE_LEGACY));
                            }
                        }
                    }
                });

                params4button.addRule(RelativeLayout.BELOW, recentView.getId());
                params4button.setMargins(marginLeft_AudioBtn, marginTop_AudioBtn, marginRight_AudioBtn, marginBottom_AudioBtn);
                audio_btn.setLayoutParams(params4button);
                int id = recentView.getId();
                audio_btn.setId( id + 1);
                current_relative_layout.addView(audio_btn);

                recentView = audio_btn;
            }
        }
        return recentView;
    }

    private View addIllustrations(RelativeLayout current_relative_layout, ArrayList<ArrayList<Illustration>> illustration_tab_list, int num_entry, Context context, View recentView) {
    //private View addIllustrations(LinearLayout current_relative_layout, ArrayList<ArrayList<Illustration>> illustration_tab_list, int num_entry, Context context, View recentView) {
        if (illustration_tab_list.size() > 0) {
            for (int illustration_count = 0; illustration_count < illustration_tab_list.get(num_entry).size(); illustration_count++) {
                // CC 200911 : modif pour charger des illustrations sur le web
                String illustration_file = illustration_tab_list.get(num_entry).get(illustration_count).fileName;
                /*String illustration_file = illustration_tab_list.get(num_entry).get(illustration_count).fileName.substring(0, illustration_tab_list.get(num_entry).get(illustration_count).fileName.length() - 4);
                illustration_file = illustration_file.replace('-', '_');
                illustration_file = illustration_file.toLowerCase();

                android.content.res.Resources res = context.getResources();
                int illustration_id = res.getIdentifier(illustration_file, "drawable", context.getPackageName());

                if (illustration_id == 0) {
                    System.err.println(illustration_file + " not found");
                    return recentView;
                }*/
                ImageView illustration_picture = new ImageView( this );
                //illustration_picture.setImageResource(illustration_id);

                //new DownloadImageTask(illustration_picture)  // obsolete : AsynkTask
                //        .execute(illustration_file);

                // CC 021024 En remplacement de DownloadImageTask
                executorService = Executors.newSingleThreadExecutor();
                downloadImage(illustration_picture, illustration_file); // Remplacez par l'URL de votre image

                int id = recentView.getId();
                illustration_picture.setId(id + 1);
                RelativeLayout.LayoutParams params4image = new RelativeLayout.LayoutParams
                        (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params4image.addRule(RelativeLayout.BELOW, recentView.getId());
                params4image.addRule(RelativeLayout.CENTER_HORIZONTAL);
                params4image.setMargins(marginLeft_Illustration, marginTop_Illustration, marginRight_Illustration, marginBottom_Illustration);
                params4image.height=800; params4image.width=800;
                illustration_picture.setLayoutParams(params4image);
                current_relative_layout.addView(illustration_picture);
                recentView = illustration_picture;
            }
        }
        return recentView;
    }

    // CC 021024 En remplacement de DownloadImageTask extends AsyncTask
    private void downloadImage(ImageView bmImage, String url) { //
        executorService.execute(() -> {
            Bitmap bitmap = null;
            try {
                // Télécharger l'image
                InputStream in = new URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Mettre à jour l'UI sur le thread principal
            if (bitmap != null) {
                Bitmap finalBitmap = bitmap;
                runOnUiThread(() -> bmImage.setImageBitmap(finalBitmap));
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown(); // Fermer l'ExecutorService
    }
    ///

    // pour charger les images depuis le Web  Obsolete CC 021024
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //int newHeight = (int) Math.floor((double) currentBitmapHeight *( (double) newWidth / (double) currentBitmapWidth));
            //mIcon11 = Bitmap.createScaledBitmap(mIcon11, newWidth, newHeight, true);*/
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public static String ColorToHex(int color) {
        String hex = String.format("#%06X", (0xFFFFFF & color));
        return hex;
    }


    /*
     * @function retourne au mot précédemment affiché (permet de remonter le fil de l'historique des mots consultés)
     */
    public void retourDernierMot(){
        if(indexActuelDerniersMots >= 1){
            isRetour = true;
            indexActuelDerniersMots -= 1;//on retourne à l'élément précédent
            //affcher la notice du mot :
            String mot= precedentSearches.get(indexActuelDerniersMots);
            setContentView(R.layout.word_desc); // CC
            afficherNoticeMot(mot);
        }
    }

    public void motSuivant(){
        if(indexActuelDerniersMots >= 1){
            isRetour = true;
            indexActuelDerniersMots -= 1;//on retourne à l'élément précédent
            //affcher la notice du mot :
            String mot= precedentSearches.get(indexActuelDerniersMots);
            setContentView(R.layout.word_desc); // CC
            afficherNoticeMot(mot);
        }
    }


    private boolean haveInternetConnection(){
        // Fonction haveInternetConnection : return true si connecté, return false dans le cas contraire
        //@SuppressLint("MissingPermission")  // CC 190924
        NetworkInfo network = ((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return network != null && network.isConnected();
        /*if (network.isRoaming()){
            // Si tu as besoin d’exécuter une tache spéciale si le périphérique est connecté à Internet en roaming (pour afficher un message prévenant des surcoûts opérateurs par exemple)
            // Si inutile, supprime la condition
        }*/
    }

    // intercepte les swipe events
    /*public boolean dispatchTouchEvent (MotionEvent event ){
        return gestureDetector.onTouchEvent( event );
    }*/

    /*public void onSwipe(SwipeGestureDetector.SwipeDirection direction){  // CC 16082018
        String message = ""; String mot;
        switch (direction){
            case LEFT_TO_RIGHT:
                //message = "affiche precedent" + Word;
                mot = dictData.getPreviousWord(Word);
                setContentView(R.layout.word_description);
                Word = mot;
                afficherNoticeMot(mot);
                break;
            case RIGHT_TO_LEFT:
                mot = dictData.getNextWord(Word);
                setContentView(R.layout.word_description);
                Word = mot;
                afficherNoticeMot(mot);
                break;

        }
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }*/
}

