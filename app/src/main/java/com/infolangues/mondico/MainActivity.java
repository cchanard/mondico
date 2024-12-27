package com.infolangues.mondico;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Spinner;
//import android.widget.TabHost;
import com.google.android.material.tabs.TabLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

//Created by Bonnet, design  rebuilt and additionnal functions by Maël Franceschetti and Louise Bouteilleon 05/2018

public class MainActivity extends AppCompatActivity /*ActionBarActivity*/ {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     * Rebuild and additionnal design and functions by Maël Franceschetti on 05/2018
     */
    private String color = "#00008B";//couleur des entrées dans la page principale
    private String colorFR = "#000000";//couleur des entrées dans la page principale
    private String colorEN = "#000000";//couleur des entrées dans la page principale
    private final float tailleEntrees = 24.f;//taille du texte des entrées dans la page principale
    private Menu menuM;
    private ArrayAdapter<String> adapterS;
    private GridLayout complement_letter_grid;
    private static final int maximum_number_of_button = 16;
    private int complement_letter_page = 0;
    LinearLayout layoutBtns;
    private boolean RA_active = false;
    private LinearLayout panelAS;
    //private TabHost host;
    private TabLayout host;
    private SearchView searchView;

    private EditText searchView2;
    private CheckBox as_cb;
    private ArrayList<String> THchoisis;
    private ArrayList<CheckBox> THdispos;
    private ArrayList<String> CATchoisies;
    private ArrayList<CheckBox> CATdispos2;
    private Spinner dropdown;
    private SwitchCompat isAdvancedSearchActive;
    ConstraintLayout fenetreRecherche ;
    ConstraintLayout panelLoading;
    private String currentLetter; //lettre en cours
    private static ArrayList<pageLettre> letterViews;
    private String currentLanguage;

    private class pageLettre{
        public String lettre;
        public LinearLayout vue;
        public int nbCharges;
        public int nbTotal;

        public pageLettre(String l, LinearLayout v, int nbCharg, int nbTot){
            this.lettre = l;
            this.vue = v;
            this.nbCharges = nbCharg;
            this.nbTotal=nbTot;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Resources res = getResources();
        color = res.getString(R.string.textColor_Title);//on récupère les couleurs et ressources qui ne sont pas encore gérées par word_description (pas encore créé au démarage)
        colorFR = res.getString(R.string.textColor_FR);
        colorEN = res.getString(R.string.textColor_EN);
        // long debut = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupérer la version actuelle de l'application
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String currentVersion = packageInfo.versionName;
            // Appeler la méthode pour vérifier les mises à jour
            checkForUpdates(currentVersion);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    //progressBar = findViewById(R.id.progressBar);   // CC 021024
        //executorService = Executors.newSingleThreadExecutor();
        //loadDataFromDatabase();

        letterViews = new ArrayList<>();//initialisation de la liste des pages des lettres déjà chargées

        fenetreRecherche = findViewById(R.id.fenetreRecherche);//fenetre de la recherche (R. avancée etc...)

        /*final LinearLayout ongletFR= findViewById(R.id.tab2);//contenu des onglets de langues invisibles avant leur création, pour éviter les superpositions et les bugs.
        ongletFR.setVisibility(View.INVISIBLE);//contenu des onglets (rendu invisible pour ne pas se superposer etc...)
        final LinearLayout ongletEN= findViewById(R.id.tab3);
        ongletEN.setVisibility(View.INVISIBLE);*/

        /*panelAS = findViewById(R.id.advancedSearchPanel); //panel recherche avancée masqué au départ
        panelAS.setVisibility(View.GONE);
        isAdvancedSearchActive = findViewById(R.id.isAdvancedSearchActive);
        isAdvancedSearchActive.setOnCheckedChangeListener((buttonView, isChecked) -> setActiveAS(isChecked));

        final LinearLayout arrowRA = findViewById(R.id.ArrowAdvancedSearch);
        //on initialise l'action au clic sur le bouton qui fait déplier le panel de recherche avancée (le rend visible), todo : animation ?
        LinearLayout btnAffPanelAS = findViewById(R.id.btnOpenAdvancedSearch);
        btnAffPanelAS.setOnClickListener(v -> {
            if(panelAS.getVisibility()==View.GONE){
                panelAS.setVisibility(View.VISIBLE);
                arrowRA.setRotation(arrowRA.getRotation()+180.f);
            }
            else{
                panelAS.setVisibility(View.GONE);
                arrowRA.setRotation(arrowRA.getRotation()+180.f);
            }
        });

        //pareil pour le texte qui est sur le bouton : le texte est par-dessus btnAffPanelAS,
        // il faut aussi lui mettre le onClick qui déplie le panel de recherche avancée
        //sinon le panneau ne se déplie que si on clique en dehors du texte :
        TextView txtOpenAdvancedSearch = findViewById(R.id.txtOpenAdvancedSearch);
        txtOpenAdvancedSearch.setOnClickListener(v -> {
            if(panelAS.getVisibility()==View.GONE){
                panelAS.setVisibility(View.VISIBLE);
                arrowRA.setRotation(arrowRA.getRotation()+180.f);
            }
            else{
                panelAS.setVisibility(View.GONE);
                arrowRA.setRotation(arrowRA.getRotation()+180.f);}
        });
*/
        /* INITIALISATION ONGLETS */
        //on initialise le TabHost qui accueillera les onglets des langues
        /* host = findViewById(R.id.tabHost);
        host.setup();*/
        // remplacé par tabLayout
        //host = findViewById(R.id.tabHost);

        /* FIN INITIALISATION ONGLETS */

        //on masque tout car ce sera ouvert à l'activation de la SearchView du menu :
        fermerPanneauRecherche();

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/DoulosSILCompact-R.ttf");//on met la police

        /*   GRILLE LETTRES COMPLEMENTAIRES */
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        //complement_letter_grid = findViewById(R.id.complement_letter_grid);
        //}
/*       final ArrayList<String> complement_letter_list = dictData.getComplementLetters();
        complement_letter_list.remove("");
        complement_letter_list.remove(" ");
        complement_letter_list.remove("(");
        complement_letter_list.remove(")");
        complement_letter_list.remove("-");
        complement_letter_list.remove("_");
        complement_letter_list.remove("?");
        Button[] c_l_btn;
        boolean pagination;
        int number_of_button;
        if(pagination = complement_letter_list.size() > maximum_number_of_button) {
            number_of_button = maximum_number_of_button - 1;
        }
        else {
            number_of_button = complement_letter_list.size();
        }
        c_l_btn = new Button[number_of_button+ ((pagination)?1:0)];
        for (int letter_btn_cpt = 0; letter_btn_cpt < number_of_button; letter_btn_cpt++) {
            final String chosen_letter = complement_letter_list.get(letter_btn_cpt);
            // @TODO use a non hardcoded list
            if (!chosen_letter.isEmpty() && !chosen_letter.equals(" ") && !chosen_letter.equals("(") && !chosen_letter.equals(")") && !chosen_letter.equals("-") && !chosen_letter.equals("_")) {
                c_l_btn[letter_btn_cpt] = new Button(this);
                c_l_btn[letter_btn_cpt].setText(complement_letter_list.get(letter_btn_cpt));
                c_l_btn[letter_btn_cpt].setTextSize(tailleEntrees);
                c_l_btn[letter_btn_cpt].setTransformationMethod(null);
                c_l_btn[letter_btn_cpt].setTypeface(custom_font);
                c_l_btn[letter_btn_cpt].setIncludeFontPadding(false);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);// = c_l_btn[i].getLayoutParams();
                Button btn_example= findViewById(R.id.example_button);
                ViewGroup.LayoutParams params_ex = btn_example.getLayoutParams();
                params.height = params_ex.height;
                params.width = params_ex.width;
                c_l_btn[letter_btn_cpt].setLayoutParams(params);
                complement_letter_grid.addView(c_l_btn[letter_btn_cpt]);
                c_l_btn[letter_btn_cpt].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//on ajoute le caractère choisi à la suite de la saisie dans la searchView du menu
                        String saisie = searchView.getQuery().toString()+chosen_letter;
                        searchView.setQuery(saisie,false);
                    }
                });
            }
        }
        if (pagination) {
            c_l_btn[number_of_button] = new Button(this);
            c_l_btn[number_of_button].setText("▶");
            c_l_btn[number_of_button].setTransformationMethod(null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Button btn_example= findViewById(R.id.example_button);
            ViewGroup.LayoutParams params_ex = btn_example.getLayoutParams();
            params.height = params_ex.height;
            params.width = params_ex.width;
            c_l_btn[number_of_button].setLayoutParams(params);
            complement_letter_grid.addView(c_l_btn[number_of_button]);
            c_l_btn[number_of_button].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    complement_letter_grid.removeAllViews();
                    complement_letter_page++;
                    createGrid(complement_letter_list);
                }
            });
        }

        complement_letter_grid.setVisibility(View.VISIBLE);
        //LinearLayout complement_letter = findViewById(R.id.ll1);  // CC 200915 : zone cachée pour le Neenge
        //complement_letter.setVisibility(LinearLayout.GONE);
*/
        final String[] letter_list;
        //if (dictData.alphabets_list.size() == 0  || dictData.ascii_devanagari_correspondence.size()  == 0) {
        letter_list = dictData.getLocalLetters();
        /*} else {
            letter_list = dictData.getLocalLetterList();
        }*/
        int currentID = 732; // magic constant
        //Resources res = getResources();

        final String[] language_names = res.getStringArray(R.array.language_names);
        MyApplication.Log("languages : " + Arrays.toString(language_names));

        /* ONGLET LANGUE LOCALE*/
        String nomLocale = "Locale";
        String abrevNomLocale = dictData.languages_list.get(0); //on récupère l'abréviation de la langue en BDD
        Resources resources=getResources();
        String[] languagesCorrespondance = resources.getStringArray(R.array.language_names);
        for (String s : languagesCorrespondance) { //on récupère le nom complet de la langue
            String[] corresp = s.split(":");
            if (corresp[0].equals(abrevNomLocale)) {
                nomLocale = corresp[1];
                System.err.println("*********** LANGUE LOCALE : " + corresp[1]);
                break;
            }
        }
        /* TabHost.TabSpec spec = host.newTabSpec(nomLocale);//on créé l'onglet avec le nom complet de la langue locale
        spec.setContent(R.id.tab1);
        spec.setIndicator(nomLocale);
        host.addTab(spec); */
/*
        TabLayout.Tab tab = host.newTab();
        tab.setText(nomLocale);
        //tab.setIcon(R.drawable.icon_Nengee);  // Définir une icône
        //host.addTab(tab);

        final List<String> other_languages_list = dictData.languages_list.subList(1, dictData.languages_list.size());
        MyApplication.Log("other_languages_list : " + Arrays.toString(other_languages_list.toArray()));
        // select languages to hide :
        for (String current_language : language_names) {
            if (current_language.charAt(current_language.length() - 1) == ':')
                other_languages_list.remove(current_language.substring(0, current_language.length() - 1));
        }
        MyApplication.Log("other_languages_list after hiding : " + Arrays.toString(other_languages_list.toArray()));
        LinearLayout[] ll = new LinearLayout[other_languages_list.size()];
        LinearLayout.LayoutParams params4language_layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params4language_layout.gravity = Gravity.BOTTOM;
        params4language_layout.setMargins(5, 5, 0, 0);
        LinearLayout.LayoutParams lgg_textView_layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lgg_textView_layoutParam.gravity = Gravity.BOTTOM;
        lgg_textView_layoutParam.setMargins(5, 0, 2, 0);
        final LinearLayout.LayoutParams lgg_edit_test_layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lgg_edit_test_layoutParam.gravity = Gravity.BOTTOM;

        // Ajout des autres langues :
        for (int lgg_cpt = 0; lgg_cpt < ll.length; lgg_cpt++) {
            ll[lgg_cpt] = new LinearLayout(this);
            ll[lgg_cpt].setLayoutParams(params4language_layout);
            ll[lgg_cpt].setId(currentID++);
            String nomLangue ="";
            for (String language_name : language_names) {
                if (language_name.substring(0, other_languages_list.get(lgg_cpt).length() + 1).equals(other_languages_list.get(lgg_cpt) + ":")) {
                    nomLangue= language_name.substring(other_languages_list.get(lgg_cpt).length() + 1);
                    break;
                }
            }
            //on place les vues crées dans le bon onglet :
            LinearLayout fr = findViewById(R.id.other_languages);
            LinearLayout en = findViewById(R.id.other_languages_EN);
            //spec = host.newTabSpec(nomLangue);
            TabLayout.Tab tabx = host.newTab();
            tabx.setText(nomLangue);
            if(nomLangue.equals("Français")){
                //ongletFR.setVisibility(View.VISIBLE);
                fr.addView(ll[lgg_cpt]);
                //spec.setContent(R.id.tab2);
            }else if(nomLangue.equals("English")){
                //ongletEN.setVisibility(View.VISIBLE);
                en.addView(ll[lgg_cpt]);
                //spec.setContent(R.id.tab3);
            }
            else {
                en.addView(ll[lgg_cpt]);
                //spec.setContent(R.id.tab3);
            }
            /*spec.setIndicator(nomLangue);
            host.addTab(spec);
            //tab.setText(nomLangue);
            //host.addTab(tabx);
        }
*/
        /*  ON INITIALISE LA COULEUR DES ONGLETS QUAND ACTIFS*/
/*
        initialiserCouleurOnglets(host,language_names.length, Color.WHITE);//on met la couleur de fond sur tous les onglets de choix de langue
        String colorchoiceNG =  "#caf2df";
        /*if(colorchoiceNG == null){
            colorchoiceNG = "#000000";//noir si non défini
        }
        if(host.getTabAt(0) != null) { //si le premier onglet est le n°1
            host.getTabAt(0).view.setBackgroundColor(Color.parseColor(colorchoiceNG));//on place le marqueur sur le 1er onglet (qui est celui actif par défaut)
        }
        else{//si ce n'est pas le n°1 le premier, on récupère celui actuellement actif
            int idCurrent = getCurrentTab().getPosition();//l'indice de l'onglet courant
            host.getTabAt(idCurrent).view.setBackgroundColor(Color.parseColor(colorchoiceNG));//on place le marqueur sur le 1er onglet (qui est celui actif par défaut)
        }
*/
        /*  MAJ SPINNER RECHERCHE AVANCEE SELON LANGUE */
/*
        host.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab;
                int idCurrent = host.getSelectedTabPosition();
                initialiserCouleurOnglets(host,language_names.length, Color.WHITE);
                Resources res = getResources();
                String colorchoiceNG = res.getString(R.string.localDelayColor_Theme);
                String colorchoiceFR = res.getString(R.string.frenchDelayColor_Theme); //colorFR adoucie;
                String colorchoiceEN = res.getString(R.string.englishDelayColor_Theme); //colorEN adoucie;
                String finalColor = idCurrent==0?colorchoiceNG:(idCurrent==1?colorchoiceFR:(idCurrent==2?colorchoiceEN:"BLACK"));
                host.getTabAt(idCurrent).view.setBackgroundColor(Color.parseColor(finalColor));
                String langue = (String) tab.getText();  // Récupérer le tag (ici, le nom de la langue)
                upDateSpinner(langue);//on met à jour les choix du spinner en fonction de la langue choisie.
                if(langue.equals("fra")||langue.equals("eng")||langue.equals("Français")||langue.equals("English")){
                    final MenuItem searchItem = menuM.findItem(R.id.action_search);
                    //searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
                    searchView =  (SearchView) searchItem.getActionView();  // CC 200919
                    //final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
                    AutoCompleteTextView searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
                    ArrayAdapter<String> adapter = null;
                    searchAutoComplete.setAdapter(adapter);
                }
                else{
                    final MenuItem searchItem = menuM.findItem(R.id.action_search);
                    //searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
                    SearchView searchView = (SearchView) searchItem.getActionView();  // CC 200919
                    //final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
                    AutoCompleteTextView searchAutoComplete;
                    searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
                    searchAutoComplete.setAdapter(adapterS);
                }
            }
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
*/
        // Prise en compte de la langue de recherche choisi. Désactive l'autocomplete pour l'anglais et le français
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                String currentLanguage = selectedRadioButton.getText().toString();
                if(currentLanguage.equals("fra")||currentLanguage.equals("eng")||currentLanguage.equals("Français")||currentLanguage.equals("English")){
                    final MenuItem searchItem = menuM.findItem(R.id.action_search);
                    searchView =  (SearchView) searchItem.getActionView();  // CC 200919
                    AutoCompleteTextView searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
                    ArrayAdapter<String> adapter = null;
                    searchAutoComplete.setAdapter(adapter);
                }
                System.out.println("**********"+currentLanguage);
            }
        });

        /*   CAROUSSEL BTNS LETTRES  */
        final int nbButtons = letter_list.length;
        Button[] btn = new Button[nbButtons];
        layoutBtns= findViewById(R.id.layoutBtns);
        for (int i = 0; i < letter_list.length; i++) {
            final String chosen_letter = letter_list[i];
            // @TODO use a non hardcoded list
            if (!chosen_letter.isEmpty() && !chosen_letter.equals(" ") &&
                    !chosen_letter.equals("(") && !chosen_letter.equals(")") &&
                    !chosen_letter.equals("-") && !chosen_letter.equals("_")) {
                btn[i] = new Button(this);
                btn[i].setText(letter_list[i]);
                btn[i].setTransformationMethod(null);
                btn[i].setLayoutParams (new LinearLayout.LayoutParams(120, 120)); //on a redéfini la taille du bouton, ancienne height: LinearLayout.LayoutParams.WRAP_CONTENT
                final Drawable background_default=btn[i].getBackground();
                btn[i].setOnClickListener(new View.OnClickListener() { //action déclenchée au clic d'un bouton d'une lettre dans le carrousel
                    @Override
                    public void onClick(View v) {
                        long debut = System.currentTimeMillis();
                        changerOngletLettre(chosen_letter, nbButtons,  background_default, v); //on passe à la page de la lettre sélectionnée
                    }
                });

                /*if(i==0){//on active la 1ere lettre
                    getFirstPage(letter_list[i]);//on affiche sur la page principale les mots de la 1ère lettre
                    btn[i].setBackgroundColor(Color.parseColor("#FAFAFA"));
                }*/
                layoutBtns.addView(btn[i]); //on ajoute le bouton au carousel
            }
        }
        // charge la liste des mots commençant par une lettre choisie au hasard  CC 05/05/22
        double rand = Math.random() * (letter_list.length);
        getFirstPage(letter_list[(int)rand]);//on affiche sur la page principale les mots de la 1ère lettre


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }


        /*  RECHERCHE AVANCEE: */

        //upDateSpinner("initialiser");//on initialise le Spinner en passant dans le "default case" du Switch

        /* CHECKBOX 'contains' */
        /*as_cb = findViewById(R.id.contains_or_isCheckBox);
        as_cb.setChecked(true);
        as_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setActiveAS(true);
            }
        });*/
        /* CATEGORIES GRAMMATICALES */
        /*CATdispos2 = new ArrayList<CheckBox>();//liste qui contiendra les cat gramm dispos
        LinearLayout checkBoxesPanel = findViewById(R.id.CheckBoxesCat); //Vue contenant les checkBoxes de Cat gramm
        ArrayList<String> catExistantesBDD = dictData.getPartOfSpeechChoices();//on récupère la liste des cat existantes en BDD
        for(int i = 0; i < catExistantesBDD.size();i++){
            CheckBox chCat = new CheckBox(this);//on créé une nouvelle checkbox
            chCat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){setActiveAS(true);}
                }
            });
            chCat.setText(catExistantesBDD.get(i));//on l'associe à une cat gramm de la bdd
            CATdispos2.add(chCat);// on l'ajoute à la liste des checkboxes de cat dispos (pour ensuite pouvoir vérifier lesquelles sont checked)
            checkBoxesPanel.addView(chCat); // on l'ajoute à la vue du pannel des checkboxes (dans la recherche avancée)
        }*/
        CATchoisies = new ArrayList<>();//liste des cat checkées par l'utilisateur (final, elle sera actualisée à chaque clique du bouton Rech avancée)
        /* FIN CATEGORIES GRAMMATICALES */

        /* THESAURUS */
        /*THdispos = new ArrayList<>();//liste qui contiendra les  thesaurus dispos
        LinearLayout thesaurusPanel = findViewById(R.id.CheckBoxesTh); //Vue contenant les checkBoxes de Thesaurus
        ArrayList<String> thExistantsBDD = dictData.getThesaurusChoices();//on récupère la liste des thesaurus existantes en BDD
        for(int i = 0; i < thExistantsBDD.size();i++){
            CheckBox chTh = new CheckBox(this);//on créé une nouvelle checkbox
            chTh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){setActiveAS(true);}
                }
            });
            chTh.setText(thExistantsBDD.get(i));//on l'associe à un thesaurus de la bdd
            THdispos.add(chTh);// on l'ajoute à la liste des checkboxes de th dispos (pour ensuite pouvoir vérifier lesquelles sont checked)
            thesaurusPanel.addView(chTh); // on l'ajoute à la vue du pannel des thesaurus (dans la recherche avancée)
        }*/
        THchoisis = new ArrayList<>();//liste des th checkées par l'utilisateur (final, elle sera actualisée à chaque clique du bouton Rech avancée)
        /* FIN THESAURUS */

        /* BTN CLEAR : */
        /*Button clear_adv_search = findViewById(R.id.clear_adv_search);
        clear_adv_search.setOnClickListener(v -> {
            as_cb.setChecked(true);//on check la checkbox "contains"
            dropdown.setSelection(0);//on place la sélection du spinner sur le 1er élément ("définition")
            for(int i=0;i<CATdispos2.size();i++){//on dé-check les checkboxes des CAT grammaticales
                CATdispos2.get(i).setChecked(false);
            }
            for(int i=0;i<THdispos.size();i++){
                THdispos.get(i).setChecked(false);//on dé-check les checkboxes des Thésaurus
            }
            setActiveAS(false);
        });  */            /* FIN BTN CLEAR */

        /* ON MASQUE LE CONTENU DES ONGLETS VIDES (FR et EN) : */
        //fr:
        //findViewById(R.id.tab2).setBackgroundColor(Color.TRANSPARENT);
        //findViewById(R.id.tab2).setVisibility(View.GONE);
        //findViewById(R.id.other_languages_and_letter_grid).setVisibility(View.GONE);
        //en:
        //findViewById(R.id.tab3).setBackgroundColor(Color.TRANSPARENT);
        //findViewById(R.id.tab3).setVisibility(View.GONE);
        //findViewById(R.id.other_languages_and_letter_grid_EN).setVisibility(View.GONE);
    }

    public String getSelectedRadioButton() {
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        int selectedId = radioGroup.getCheckedRadioButtonId(); // Récupérer l'ID du RadioButton sélectionné
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            String selectedText = selectedRadioButton.getText().toString();
            Toast.makeText(this, "RadioButton sélectionné : " + selectedText, Toast.LENGTH_SHORT).show();
            return(selectedText);
        }
        return "";
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    private static ArrayList<String> deleteDuplicateString(ArrayList<String> al) {//supprime les doublons
        ArrayList<String> al2 = new ArrayList<>();
        for (int i = 0; i < al.size(); i++) {
            String o = al.get(i);
            if (!al2.contains(o))
                al2.add(o);
        }
        return al2;
    }

    private void addBackwardArrowButton(Button[] c_l_btn, final ArrayList<String> complement_letter_list)
    {
        c_l_btn[0] = new Button(this);
        c_l_btn[0].setText("◀");
        c_l_btn[0].setTransformationMethod(null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btn_example= findViewById(R.id.example_button);
        ViewGroup.LayoutParams params_ex = btn_example.getLayoutParams();
        params.height = params_ex.height;
        params.width = params_ex.width;
        c_l_btn[0].setLayoutParams(params);
        complement_letter_grid.addView(c_l_btn[0]);
        c_l_btn[0].setOnClickListener(v -> {
            complement_letter_grid.removeAllViews();
            complement_letter_page--;
            createGrid(complement_letter_list);
        });
    }

    private void addForwardArrowButton(Button[] c_l_btn, final ArrayList<String> complement_letter_list)
    {
        int number_of_button;
        if(complement_letter_list.size() > maximum_number_of_button) {
            number_of_button = maximum_number_of_button - 1;
        }
        else {
            number_of_button = complement_letter_list.size();
        }
        c_l_btn[number_of_button] = new Button(this);
        c_l_btn[number_of_button].setText("▶");
        c_l_btn[number_of_button].setTransformationMethod(null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button btn_example= findViewById(R.id.example_button);
        ViewGroup.LayoutParams params_ex = btn_example.getLayoutParams();
        params.height = params_ex.height;
        params.width = params_ex.width;
        c_l_btn[number_of_button].setLayoutParams(params);
        complement_letter_grid.addView(c_l_btn[number_of_button]);
        c_l_btn[number_of_button].setOnClickListener(v -> {
            complement_letter_grid.removeAllViews();
            complement_letter_page++;
            createGrid(complement_letter_list);
        });
    }

    private void createGrid(ArrayList<String> complement_letter_list)
    {
        int start;
        int forward_button = complement_letter_page;
        int back_button = (complement_letter_page > 1?complement_letter_page-1:0);
        start = complement_letter_page * maximum_number_of_button - forward_button - back_button;
        int stop;
        if(complement_letter_list.size() > (start + maximum_number_of_button - 1)) {
            stop = start + maximum_number_of_button - 1;
        }
        else {
            stop = complement_letter_list.size() + 1;
        }
        Button[] c_l_btn = new Button[stop - start + 2];
        int btn_num = 0;
        if (start > 0) {
            addBackwardArrowButton(c_l_btn, complement_letter_list);
            btn_num++;
            stop--;
        }
        for (int letter_btn_cpt = start; letter_btn_cpt < stop; letter_btn_cpt++) {
            final String chosen_letter = complement_letter_list.get(letter_btn_cpt);
            c_l_btn[btn_num] = new Button(this);
            c_l_btn[btn_num].setText(complement_letter_list.get(letter_btn_cpt));
            c_l_btn[btn_num].setTextSize(tailleEntrees);
            c_l_btn[btn_num].setTransformationMethod(null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Button btn_example= findViewById(R.id.example_button);
            ViewGroup.LayoutParams params_ex = btn_example.getLayoutParams();
            params.height = params_ex.height;
            params.width = params_ex.width;
            c_l_btn[btn_num].setLayoutParams(params);
            complement_letter_grid.addView(c_l_btn[btn_num]);
            c_l_btn[btn_num].setOnClickListener(v -> {//on ajoute au champ de saisie de la barre de recherche le caractère spécial choisi parmi les caractères complémentaires
                String saisie = searchView.getQuery().toString()+chosen_letter;
                searchView.setQuery(saisie,false);
            });
            btn_num++;
        }
        if(complement_letter_list.size() > stop)
            addForwardArrowButton(c_l_btn, complement_letter_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuM = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        /* AUTOCOMPLETE ON SEARCHVIEW WITH ADAPTER*/
        //final MenuItem searchItem = menu.findItem(R.id.action_search);
        final MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        if (searchView != null) {
            searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);          // CC 15-11-2019
        }
        if (searchView != null) {
            int actualOptions = searchView.getImeOptions();
        }
        //System.out.println("actionIdTeko="+ actualOptions);
        //Toast.makeText(MainActivity_new.this,"Search="+searchView.getImeOptions(),Toast.LENGTH_SHORT).show();
        if (searchView != null) {
            searchView.setQueryHint(getString(R.string.search_hint));        // CC
        }
        //final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx
        //        .appcompat.R.id.search_src_text);
        AutoCompleteTextView searchAutoComplete = null;
        if (searchView != null) {
            searchAutoComplete = searchView.findViewById(androidx
                    .appcompat.R.id.search_src_text);
        }
        if (searchAutoComplete != null) {
            searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);
        }

        ArrayAdapter<String> adapter;
        ArrayList<String> propositions = new ArrayList<>();
        // put words in 'local' alphabet and ascii propositions if needed
        propositions.addAll(dictData.getDistinctAsciiWordList());
        // Modif post duplication
        ArrayList<String> citationforms = dictData.getCitationForms();
        citationforms.removeAll(Collections.singleton(""));
        if (!citationforms.isEmpty()) {
            propositions.addAll(citationforms);
        }
        else {
            propositions.addAll(deleteDuplicateString(dictData.getLexemes()));
        }
        // ajouter les variantes dans l'auto-complétion CC 2205
        ArrayList<String> variantes;
        variantes = dictData.getVariantes();
        propositions.addAll(deleteDuplicateString(variantes));
        if (MyApplication.word_duplication_without_prefix){
            ArrayList<String> artificial_propositions = new ArrayList<>();
            for (String proposition : propositions){
                if(proposition.contains("-")){
                    artificial_propositions.add(proposition.substring(proposition.indexOf('-')+1) + " ❲" + proposition + '❳');
                }
            }
            propositions.addAll(artificial_propositions);
        }
        Collections.sort(propositions);
        adapter = new ArrayAdapter<>(this, R.layout.list_item, propositions);
        // Set the adapter for the AutoCompleteTextView
        adapterS = adapter;
        if (searchAutoComplete != null) {
            searchAutoComplete.setAdapter(adapter);
        }

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Display a Toast Message when the user clicks on an item in the AutoCompleteTextView
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                MyApplication.Log("++++++++++++++++++++++++++++++++++++++  arg0 = " + arg0 + " arg1 = " + arg1 + " arg2 = " + arg2 + " arg3 = " + arg3);
                Intent intent = new Intent(MainActivity.this, word_descriptionActivity.class);
                String clicked_word = arg0.getAdapter().getItem(arg2).toString();
                if(MyApplication.word_duplication_without_prefix)
                {
                    if(clicked_word.contains("-") && clicked_word.contains(" ❲") && clicked_word.contains("❳"))
                    {
                        clicked_word = clicked_word.substring(clicked_word.indexOf(" ❲")+2, clicked_word.indexOf("❳"));
                    }
                }
                if (!dictData.citationFormExists(clicked_word) && !dictData.alphabets_list.isEmpty()) {
                    if (dictData.oneWord4asciiString(clicked_word)) {
                        intent.putExtra(MyApplication.CHOSEN_WORD, dictData.getWord4AsciiVersion(clicked_word));
                    } else {
                        intent = new Intent(MainActivity.this, word_listActivity.class);
                        intent.putExtra(MyApplication.CHOSEN_WORD, clicked_word);
                        // @TODO replace hard coded line
                        intent.putExtra(MyApplication.CHOSEN_ALPHABET, "ascii");
                    }
                } else if (! dictData.getWord4Variante(clicked_word).isEmpty() ){ // CC 2205
                    System.out.println("**** Search in variante form : " + clicked_word);
                    //ArrayList wrds = dictData.getWord4Variante(clicked_word);
                    //String chosen_word = (String) wrds.get(0);
                    String chosen_word = dictData.getWord4Variante(clicked_word);
                    System.out.println("**** Search : " + chosen_word);
                    intent = new Intent(MainActivity.this, word_listActivity.class);
                    intent.putExtra(MyApplication.CHOSEN_WORD, chosen_word);
                    // @TODO replace hard coded line!!
                    intent.putExtra(MyApplication.CHOSEN_ALPHABET, "devanagari");
                }  else {
                    if (dictData.oneWord4citationForm(clicked_word)) {
                        System.out.println("**** Search in citation form : " + clicked_word);
                        String chosen_word = dictData.getWord4citationForm(clicked_word);
                        if(chosen_word.isEmpty()) chosen_word = dictData.getWord4lexeme(clicked_word);
                        intent.putExtra(MyApplication.CHOSEN_WORD, chosen_word);
                    } else {
                        System.out.println("**** Search not in variante  : " + clicked_word);
                        intent = new Intent(MainActivity.this, word_listActivity.class);
                        intent.putExtra(MyApplication.CHOSEN_WORD, clicked_word);
                        // @TODO replace hard coded line!!
                        intent.putExtra(MyApplication.CHOSEN_ALPHABET, "devanagari");
                        //}*/
                    }
                }
                startActivity(intent);
            }
        });

        //searchAutoCompleteS = searchAutoComplete;
        //AutoCompleteTextView searchAutoCompleteS2 = (AutoCompleteTextView) searchAutoComplete2;
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    /*  LANCEMENT DE LA RECHERCHE */
                    // fermerPanneauRecherche();
                    System.err.println("**************** search launched");
                    Resources resources=getResources();//on récupère toutes les ressources (du fichier String.xml, dans le répertoire 'values")
                    String[] languagesCorrespondance = resources.getStringArray(R.array.language_names); //on récupère la tableau contenant les associations langue/abréviation
                    String chosenLanguage="";
                    boolean advancedSearchActive = RA_active;//on récupère la valeur de l'activité de la recherche avancée (true = active, false= inactive. Valeur mise à jour quand un champ de la R.A est modifié).
                    System.err.println("advancedSearchActive : "+advancedSearchActive);
                    String search_string = query;//on récupère la chaîne saisie dans le SearchView (passée automatiquement en paramètre à cette méthode)
                    //String tabLanguage = getCurrentLanguage();//On récupère le nom de l'onglet qui a le focus (le nom = la langue, exemple : "Français", "Gbaya")
                    String tabLanguage = getSelectedRadioButton();
                    for (String s : languagesCorrespondance) {//on cherche l'abréviation qui correspond à la langue de la recherche(exemple : 'fra' pour 'Français')
                        String[] corresp = s.split(":");
                        System.err.println("*********** ressources split : " + corresp[0] + " - " + corresp[1]);
                        if (corresp[1].equals(tabLanguage)) {
                            chosenLanguage = corresp[0];
                            System.err.println("*********** VOULU : " + corresp[0] + " - " + corresp[1]);
                            break;
                        }
                    }
                    boolean[] hiddenLanguages = dictData.getHidden_languages4gloss();
                    if((chosenLanguage.equals("fra")&& hiddenLanguages[0])||(chosenLanguage.equals("eng")&& hiddenLanguages[1])){//si la langue de recherche est masquée, on ne lance pas la recherche
                        Context context = getApplicationContext();
                        Toast msg = Toast.makeText(context, context.getString(R.string.hidden_search_language), Toast.LENGTH_LONG);
                        msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                        msg.show();
                        return false;
                    }
                    CATchoisies.clear();//on vide l'ancienne liste des catégories choisies
                    //on récupère la liste des catégories grammaticales choisies :
                    if(advancedSearchActive) {
                        for (int i = 0; i < CATdispos2.size(); i++) {
                            if (CATdispos2.get(i).isChecked()) {
                                CATchoisies.add(CATdispos2.get(i).getText().toString());
                                //  advancedSearchActive = true;
                            }
                        }
                    }
                    THchoisis.clear();//on vide l'ancienne liste des thesaurus choisies
                    // on récupère la liste des thesaurus choisis :
                    if(advancedSearchActive) {
                        for (int i = 0; i < THdispos.size(); i++) {
                            if (THdispos.get(i).isChecked()) {
                                THchoisis.add(THdispos.get(i).getText().toString());
                                //advancedSearchActive = true;
                            }
                        }
                    }
                    if (search_string.isEmpty()) {//si rien n'a été tappé on affiche un Toast et on quitte
                        Context context = getApplicationContext();
                        Toast msg = Toast.makeText(context, context.getString(R.string.empty_search_message), Toast.LENGTH_LONG);
                        msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                        msg.show();
                        return false;
                    }
                    //sinon, on créé l'intent qui va servir par la suite à appeler la recherche :
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    if(advancedSearchActive){
                        intent.putExtra(MyApplication.CHOSEN_LETTER, dropdown.getSelectedItem().toString());//en réalité "search_type" , pas "CHOSEN_LETTER"
                    }
                    else {
                        if(chosenLanguage.equals("fra")||chosenLanguage.equals("eng")){
                            intent.putExtra(MyApplication.CHOSEN_LETTER, "definition"); //si c'est un mot FR ou EN, on recherche dans les définitions
                        }
                        else {
                            intent.putExtra(MyApplication.CHOSEN_LETTER, "word"); // si c'est la langue locale, on recherche dans les lexeme des entrées
                        }
                    }
                    //on passe des extras (des variables) contenant les paramètres de la recherche choisis :
                    intent.putExtra(MyApplication.SEARCHED_STRING, search_string);//on passe la chaîne de caractères recherchée
                    //intent.putExtra(MyApplication.CHECKBOX_STATE, as_cb.isChecked() + "");//on passe le "Constaint" de la checkbox
                    intent.putExtra(MyApplication.CHECKBOX_STATE, false + "");//on passe le "Constaint" de la checkbox
                    intent.putExtra(MyApplication.CHOSEN_LANGUAGE, chosenLanguage); //on passe la langue choisi (l'abréviation, pour pouvoir comparer en BDD avec les chamsp "lang")
                    intent.putExtra("chosenCAT", CATchoisies);//on passe les catégories grammaticales choisies
                    intent.putExtra("chosenTH", THchoisis);//on passe les domaine choisis
                    startActivity(intent);//On démarre l'activité de recherche avec l'intent créé précedemment
                    return false;//fin de la méthode
                }

                @Override
                public boolean onQueryTextChange(String s) {//quand on saisit dans la recherche

                    ouvrirPanneauRecherche();
                    return false;
                }
            });
        }

        if (searchView != null) {
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    fermerPanneauRecherche();
                    fermerPanneauRecherche();
                    return false;
                }
            });
        }

        if (searchView != null) {
            searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {//quand on met le focus sur le champ de recherche
                    if(hasFocus){
                        ouvrirPanneauRecherche();
                    }
                }
            });
        }
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//gère le choix d'une option dans le menu
        int id = item.getItemId();
        String pop_up_message;
        Context context = getApplicationContext();
        //switch (id) {
            /*case R.id.action_information:
                pop_up_message = context.getString(R.string.message_dico_information);
                Toast msgDico = Toast.makeText(context, pop_up_message, Toast.LENGTH_LONG);
                msgDico.setGravity(Gravity.CENTER, msgDico.getXOffset() / 2, msgDico.getYOffset() / 2);
                msgDico.show();
                break;*/
        if (id == R.id.action_application_information) {
            pop_up_message = context.getString(R.string.message_application_information);
            //for (int i=0; i < 3; i++) {
            Toast msg = Toast.makeText(context, pop_up_message, Toast.LENGTH_LONG);
            //View toastView = msg.getView();
            //toastView.setBackgroundColor(0xFF00FF00);
            msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
            msg.show();
            //}
        } else if (id == R.id.action_languages_settings) {
            Intent languages_settings_intent = new Intent(MainActivity.this, languagesSettingsActivity.class);
            startActivity(languages_settings_intent);
        } else if (id == R.id.action_mail) {
            String[] to = new String[]{context.getString(R.string.feedback_mail)};
            String subject = "[" + context.getString(R.string.app_name) + "]";
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.comment_edit_text_default_string));
            emailIntent.setType("message/rfc822");
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client"));
        } else if (id == R.id.action_helpNg) {
            Intent help_intentNg = new Intent(MainActivity.this, helpActivity.class);
            help_intentNg.putExtra("chosenLanguage", "ng");
            startActivity(help_intentNg);
        } else if (id == R.id.action_helpFr) {
            Intent help_intentFr = new Intent(MainActivity.this, helpActivity.class);
            help_intentFr.putExtra("chosenLanguage", "fr");
            startActivity(help_intentFr);
        } else if (id == R.id.action_helpEn) {
            Intent help_intentEn = new Intent(MainActivity.this, helpActivity.class);
            help_intentEn.putExtra("chosenLanguage", "en");
            startActivity(help_intentEn);
        } else if (id == R.id.action_infoNg) {
            Intent info_intentNg = new Intent(MainActivity.this, infoActivity.class);
            info_intentNg.putExtra("chosenLanguage", "ng");
            startActivity(info_intentNg);
        } else if (id == R.id.action_infoFr) {
            Intent info_intentFr = new Intent(MainActivity.this, infoActivity.class);
            info_intentFr.putExtra("chosenLanguage", "fr");
            startActivity(info_intentFr);
        } else if (id == R.id.action_infoEn) {
            Intent info_intentEn = new Intent(MainActivity.this, infoActivity.class);
            info_intentEn.putExtra("chosenLanguage", "en");
            startActivity(info_intentEn);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
   /* If you’re logging an action on an item that has already been added to the index,
   you don’t have to add the following update line. See
   https://firebase.google.com/docs/app-indexing/android/personal-content#update-the-index for
   adding content to the index */
       /* FirebaseAppIndex.getInstance().update(getIndexable());
        FirebaseUserActions.getInstance().start(getAction());*/
    }

    @Override
    protected void onStop() {
        /* FirebaseUserActions.getInstance().end(getAction());*/
        super.onStop();
    }

    private void setActiveAS(boolean active){//activer ou désactiver la recherche avancée manuellement
        if(active){
            RA_active = true;
            isAdvancedSearchActive.setChecked(true);
        }
        else{
            RA_active = false;
            isAdvancedSearchActive.setChecked(false);
        }
    }

    /**
     * function affiche le progressBar de chargement
     */
    public void afficherLoading(){
        panelLoading.setVisibility(View.VISIBLE);
        panelLoading.setZ(1000);
        LinearLayout firstPage = findViewById(R.id.firstPage);
        firstPage.setBackgroundColor(Color.DKGRAY);
    }


    /**
     * function affiche le pannel de recherche et le pannel de recherche avancée
     */
    public synchronized void ouvrirPanneauRecherche(){
        fenetreRecherche.setVisibility(View.VISIBLE);
        fenetreRecherche.clearFocus();
        fenetreRecherche.setZ(1000);
        fenetreRecherche.setBackgroundColor(Color.parseColor("#A9A9A9"));//fond grisé quand recherche ouverte
        fenetreRecherche.getBackground().setAlpha(200);//opacité
        LinearLayout pagePrincipale = findViewById(R.id.pagePrincipale);
        disable(pagePrincipale);
        ScrollViewModulable scrollDico = findViewById(R.id.scrollDico);
        scrollDico.setScrollable(false);
    }

    /**
     * function masque le pannel de recherche et le pannel de recherche avancée
     */
    public synchronized void fermerPanneauRecherche(){
        fenetreRecherche.clearFocus();
        fenetreRecherche.setZ(0);
        fenetreRecherche.setBackground(null);
        fenetreRecherche.setVisibility(View.GONE);
        LinearLayout pagePrincipale = findViewById(R.id.pagePrincipale);
        enable(pagePrincipale);
        ScrollViewModulable scrollDico = findViewById(R.id.scrollDico);
        scrollDico.setScrollable(true);
    }

    private static void disable(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                disable((ViewGroup) child);
            } else {
                child.setEnabled(false);
            }
        }
    }

    private static void enable(ViewGroup layout) {
        layout.setEnabled(true);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                enable((ViewGroup) child);
            } else {
                child.setEnabled(true);
            }
        }
    }

    /**
     * function charge dans la page principale les entrées commençant par la lettre voulue par tranche de 'nbEntreesParPage'
     *  Garde en mémoire les pages déjà créées pour gagner du temps lorsqu'on y retourne ou qu'on charge les entrées suivantes
     */
    public void getFirstPage(String letter){ //temps moyen d'éxécution : 262,2ms
        pageLettre pageLettre = new pageLettre(null, null, 0, 0);//on initialise une PageLettre vide pour le moment
        int nbEntreesParPage = 100;
        int indexMax = nbEntreesParPage;
        int indexActuel = 0;
        LinearLayout pageForLetter = new LinearLayout(this);

        //chargement et sauvegarde des vues déjà chargées :
        if(letter.equals(currentLetter)&&getPageAlreadyLoaded(letter)!=null){//si on charge la suite de la lettre actuelle
            pageLettre = getPageAlreadyLoaded(letter);//on récupère le PageLettre sauvegardé contenant la vue et les infos déjà crées pour cette lettre
            pageForLetter = pageLettre.vue;//on récupère la vue déjà crée pour cette lettre (pour ne pas recharger toutes les entrées depuis le début)
            indexActuel = pageLettre.nbCharges;//on récupère l'index où on étais, pour charger la suite à partir de là
            indexMax = indexActuel + nbEntreesParPage;//on défini le nombre d'éléments à charger (par tranche de 'nbEntreesParPage')
        }
        else{ //si on charge une nouvelle lettre (différente de l'ancienne)
            TextView TitreLettre = findViewById(R.id.TitreLettre);
            TitreLettre.setText(letter.toUpperCase());
            pageForLetter.setOrientation(LinearLayout.VERTICAL);//on définit l'orientation de la nouvelle vue
        }

        long debut = System.currentTimeMillis();
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/DoulosSILCompact-R.ttf");//on définit la police
        //TitreLettre.setTypeface(custom_font);
        LinearLayout firstPage = findViewById(R.id.firstPage);//la vue qui contiendra les entrées affichées
        //firstPage.removeAllViews();//on vide les résultats précédents
        Drawable background = firstPage.getBackground();

        if (background != null && background instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) background;
            int color = colorDrawable.getColor();
            // Utilisez la couleur comme nécessaire
        } else {
            // Le fond est soit null, soit un autre type de Drawable, gérez ce cas
        }
        if (background != null && background instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) background;
            int color = colorDrawable.getColor();
            // Utilisez la couleur comme nécessaire
        } else {
            // Le fond est soit null, soit un autre type de Drawable, gérez ce cas
        }
        String[][] world_list;
        String[][] lex_id_list = dictData.getLexemeAndIDList(letter);
        if(lex_id_list.length == 0) {
            world_list = dictData.getCitationFormAndIDList(letter);
        }
        else{
            world_list = lex_id_list;
        }
        boolean[] hidden_languages = dictData.getHidden_languages4gloss();//listes langues non voulus (à masquer)

        Button[] btn = new Button[world_list[0].length];
        int i;
        for (i = indexActuel; i < world_list[0].length; i++) {// on créé un bouton par entrée jusqu'a tous les charger ou atteindre le Nb Max à charger (cf if qui fait sortir de la boucle, un peu plus loin)
            final String chosen_word = world_list[1][i];
            btn[i] = new Button(this);// bouton d'aperçu contenant l'entrée et ses informations, et menant à la notice de cette entrée.
            if(hidden_languages[0]||world_list[2][i]==null){
                world_list[2][i] = "";//si français ne doit pas être affiché, on retire le texte français (glossFR dans word_list[2])
            }
            if((hidden_languages.length>1&&hidden_languages[1])||world_list[3][i]==null){
                world_list[3][i] = "";//si anglais ne doit pas être affiché, on retire le texte anglais (glossEN dans word_list[3])
            }
            int hmNumber = dictData.getHmNumber(world_list[1][i]);
            String subIndice="";
            char[] indices = {0x2080,0x2081, 0x2082,0x2083,0x2084,0x2085,0x2086,0x2087,0x2088,0x2089};//liste des caractères à utiliser comme indice d'homonyme number (de 0 à 9)
            if(hmNumber<10){
                char c = indices[hmNumber];
                subIndice = String.valueOf(c);
            }
            else{
                char cDizaines = indices[(int)Math.floor(hmNumber / 10)];
                char cUnites = indices[hmNumber-(int)(Math.floor(hmNumber / 10)*10)];
                subIndice = String.valueOf(cDizaines)+ cUnites;
            }
            String hm = hmNumber!=0? subIndice :"";

            //séparateur utilisé dans les aperçus des notices affichées
            String separateur = " • ";
            btn[i].setText(Html.fromHtml("<b><font color=\""+color+"\">" + world_list[0][i] + "</font>"+hm+"</b> <i>("+ world_list[4][i] + ")</i> "
                    + (world_list[2][i].equals("()") ? "" : "<font color=\""+colorFR+"\">" + world_list[2][i] + "</font>")
                    +  (!world_list[2][i].equals("()")&&!world_list[3][i].equals("()")&&!world_list[2][i].equals("")&&!world_list[3][i].equals("")? separateur :"")
                    + (world_list[3][i].equals("()") ? "" : "<font color=\""+colorEN+"\">"+world_list[3][i]+ "</font>")));
            btn[i].setTypeface(custom_font);

            btn[i].setTextSize(tailleEntrees);//taille police
            //btn[i].setBackgroundColor(back_color);
            btn[i].setTransformationMethod(null);
            btn[i].setGravity(Gravity.START);
            btn[i].setIncludeFontPadding(false);
            pageForLetter.addView(btn[i]);//on l'ajoute à la vue qui sera affichée
            btn[i].setOnClickListener(new View.OnClickListener() {//"bouton" menant à la notice de l'entrée (à l'affichage, c'est l'aperçu de l'entrée)
                @Override
                public void onClick(View v) {//quand on clique sur l'entrée, on lance l'affichage de la notice dans une nouvelle Activité (nouvelle page)
                    Intent intent = new Intent(MainActivity.this, word_descriptionActivity.class);
                    intent.putExtra(MyApplication.CHOSEN_WORD, chosen_word);
                    startActivity(intent);
                }
            });
            //View sep = findViewById(R.id.sep); pour mettre un séparateur entre les items: marche pas
            //pageForLetter.addView(sep);

            if(i >= indexMax-1){
                i++;
                Button btnSuite = new Button(this);
                btnSuite.setText("Voir plus ("+(world_list[0].length-i)+" restants)");
                btnSuite.setTextSize(tailleEntrees);//taille police
                btnSuite.setGravity(Gravity.START);
                pageForLetter.addView(btnSuite);//on ajoute le bouton "charger la suite" après les entrées si il reste des entrées à afficher
                final String lettre = letter;//final pour pouvoir être utilisés dans le onClick qui suit
                final int memoriserNbEntreesChargees = i;//final pour pouvoir être utilisés dans le onClick qui suit

                btnSuite.setOnClickListener(new View.OnClickListener() {//bouton pour afficher les mots suivants
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.GONE);//le bouton disparait une fois cliqué
                        getFirstPage(lettre);//charger les 'nbEntreesParPage' mots suivants commençant par cette lettre
                    }
                });
                break;//on ne charge pas plus de mots à la fois que le nombre indiqué dans 'nbEntreesParPage';
            }
        }
        indexActuel = i;

        ScrollViewModulable scrollDico = findViewById(R.id.scrollDico);
        firstPage.removeAllViews();//on retire toutes les vues de la page principale (on vide la page de la lettre précédente)
        firstPage.addView(pageForLetter);//on ajoute la page de la lettre actuelle

        if(currentLetter!=letter) {//si on ne vient pas de charger les éléments suivants du même onglet de lettre
            scrollDico.fullScroll(View.FOCUS_UP);
            pageLettre = new pageLettre(letter, pageForLetter,indexActuel,world_list[0].length);//on sauvegarde la vue et ses infos pour ne pas recharger toutes les dnnées plus tard
            letterViews.add(pageLettre);//
        }
        else{//sinon on actualise les infos dans la sauvegarde de la vue déjà existante
            pageLettre.nbCharges = indexActuel;
            pageLettre.nbTotal = world_list[0].length;
        }
        currentLetter = letter;//on actualise la lettre en cours de consultation
    }

    // recherche dans entrée (nengee) ou dans définition (français, anglais)
    /*public void upDateSpinner(String lang){
        dropdown = findViewById(R.id.search_type_spinner);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    setActiveAS(true);//si on change les paramètres de recherche d'origine, on active automatiquement la Recherche Avancée
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setActiveAS(false);
            }
        });

        String[] items;
        if(lang.equals("Français")||lang.equals("English")){//les choix de champs contenant les langues FR et EN, dans lesquels on peut chercher dans ces langues
            items = new String[]{"definition", "example"};
        }
        else{//pour la langue locale :
            items = new String[]{"word", "definition", "example"};
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter2);
    }*/

    public String getColorEntrees(){
        return color;
    }

    public float getTailleEntrees(){
        return tailleEntrees;
    }

    /*
     * @function : passe d'un onglet de lettre à un autre sur la page principale, et actualise le visuel des boutons du carrousel des lettres
     */
    public void changerOngletLettre(String chosen_letter,int nbButtons, Drawable background_default, View v){
        if(v==null){ return;  }
        for (int j = 0; j < nbButtons; j++) { //on réinitialise l'apparence des boutons du carrousel des lettres
            if(layoutBtns.getChildAt(j)!=null) {
                layoutBtns.getChildAt(j).setBackground(background_default);
            }
        }
        v.setBackgroundColor(Color.parseColor("#FAFAFA")); //on colore comme un onglet le bouton de la lettre choisie uniquement, comme ça on voit quelle page de lettre est en cours de consultation (blanc)
        //On affiche la page de la lettre voulue :
        ScrollViewModulable scrollDico = findViewById(R.id.scrollDico);
        //si déjà chargée auparavant, on récupère la vue déjà créee
        if(pageAlreadyLoaded(chosen_letter)){
            scrollDico.fullScroll(View.FOCUS_UP);//on scroll en haut de la page
            afficherPageLettre(chosen_letter);
            currentLetter=chosen_letter;
            scrollDico.fullScroll(View.FOCUS_UP);//on scroll en haut de la page
        }
        else {//sinon, on créé la vue
            scrollDico.fullScroll(View.FOCUS_UP);//on scroll en haut de la page
            getFirstPage(chosen_letter);//on charge les données et on créé la vue (le LinearLayout contenant les entrées)
            scrollDico.fullScroll(View.FOCUS_UP);//on scroll en haut de la page
        }
        scrollDico.fullScroll(View.FOCUS_UP);//on scroll en haut de la page (plusieurs fois car marche pas toujours si le scheduler garde la main trop longtemps dessus)
    }

    /*
     * @function : vérifie si une vue sauvegardée existe déjà pour la lettre voulue ou non
     * @return : boolean, true si une sauvegarde de la PageLettre cherchée existe, false sinon
     */
    public boolean pageAlreadyLoaded(String letter){
        for(int i =0; i < letterViews.size();i++){
            if(letterViews.get(i).lettre.equals(letter)){
                return true;
            }
        }
        return false;
    }

    /*
     * @function : récupère la PageLettre déjà créée pour une lettre
     * @return : la pageLettre sauvegardée
     */
    public pageLettre getPageAlreadyLoaded(String letter){
        for(int i =0; i < letterViews.size();i++){
            if(letterViews.get(i).lettre.equals(letter)){
                return letterViews.get(i);//on récupère l'élément PageLettre pour la lettre voulue
            }
        }
        return null;
    }

    /*
     * @function : retire la vue mise dans la page principale et y met celle sauvegardée de la lettre voulue
     */
    public void afficherPageLettre(String letter){
        LinearLayout firstPage = findViewById(R.id.firstPage);
        firstPage.removeAllViews();//on retire la vue actuelle
        TextView TitreLettre = findViewById(R.id.TitreLettre);
        TitreLettre.setText(letter.toUpperCase());//on met à jour le titre de la page (la lettre choisie en majuscule)
        for(int i =0; i < letterViews.size();i++){
            if(letterViews.get(i).lettre.equals(letter)){//on cherche la vue correspondant à la lettre voulue (entrées commençant par cette lettre)
                firstPage.addView(letterViews.get(i).vue);//on ajoute la nouvelle vu récupérée, qui avait été chargée précédemment
                return;
            }
        }
    }

    public static void réinitialiserPagesLettres(){
        letterViews = new ArrayList<pageLettre>();//initialisation de la liste des pages des lettres déjà chargées
    }

    private void initialiserCouleurOnglets(TabLayout host, int number, int color){
        for(int w = 0; w < number; w++){
            if(host.getTabCount() != 0) {
                host.getTabAt(w).view.setBackgroundColor(color);
                TextView tabText = (TextView) host.getTabAt(w).view.findViewById(android.R.id.title);
                tabText.setTextColor(Color.BLACK);
            }
        }
    }

    public void checkForUpdates(String currentVersion) {
        String urlString = "https://corporan.huma-num.fr/Lexiques/TMP/NengeeVersion.txt"; // URL du fichier texte
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Crée une connexion HTTP pour récupérer le fichier texte
                    URL url = new URL(urlString);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String result = convertStreamToString(inputStream);
                    urlConnection.disconnect();

                    // Compare la version actuelle de l'application avec la version la plus récente
                    compareVersions(result);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String convertStreamToString(InputStream is) {
        Scanner scanner = new Scanner(is).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    private void compareVersions(String latestVersion) {
        try {
            // la version actuelle de l'application
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String currentVersion = packageInfo.versionName;

            // Comparer les versions
            if (!currentVersion.equals(latestVersion)) {
                // L'application doit être mise à jour
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Affichez un message ou proposez une mise à jour
                        Toast.makeText(getApplicationContext(), "version actuelle : " + currentVersion + " Une nouvelle mise à jour est disponible", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mise à jour disponible")
                .setMessage("Une nouvelle version de l'application est disponible. Voulez-vous la mettre à jour ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lien vers le Play Store pour mettre à jour l'application
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Non", null)
                .show();
    }


}
