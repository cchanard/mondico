package com.infolangues.mondico;

// update_database not used anymore >> import android.content.ContentValues;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

//import android.util.Log;
/* 14/12/2017
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
*/

/**
 * Created by Bonnet on 14/09/2016 , edited by Maël Franceschetti
 * SQL data access Class
 */
class DictManager {

    private final static String lex_id_cf_gloss_TABLE_NAME = "lex_id_cf_gloss";
    private final static String correspondence_TABLE_NAME = "letter_correspondence";
    private final static String languages_TABLE_NAME = "languages";
    private final static String alphabets_TABLE_NAME = "alphabets";
    private final static String variants_TABLE_NAME = "variant";  // CC
    // update_database not used anymore >> private final static String word_description_TABLE_NAME = "word_description";
    private final static String word_grammar_TABLE_NAME = "word_grammar";
    private final static String audio_description_TABLE_NAME = "audio_description";
    private final static String illustration_TABLE_NAME = "illustration";
    private final static String word_definition_TABLE_NAME = "word_definition";
    private final static String word_example_TABLE_NAME = "word_example";
    private final static String dictionary_information_TABLE_NAME = "dictionary_informations";
    private final static String letters_TABLE_NAME = "letters";
    private final static String homonym_number_TABLE_NAME = "homonym_number";
    private final static String usage_TABLE_NAME = "usage";

    private final String separateur = " • " ;
    private final SQLiteWrapper maBaseSQLite;
    private SQLiteDatabase db;

    // Constructor
    public DictManager(Context context)
    {
        maBaseSQLite = SQLiteWrapper.getInstance(context);
    }

    public void open()
    {
        //on ouvre la table en lecture/écriture
        db = maBaseSQLite.getWritableDatabase();
    }

    private String getFullWordCondition(String searched_string)
    {
        return " WHERE ( definition LIKE '" + searched_string + "' or definition like '% " + searched_string + "' or definition like '% " + searched_string + " %' or definition like '% " + searched_string + ",%' or definition like '% " + searched_string + ".%' or definition like '% " + searched_string + "-%' or definition like '% " + searched_string + ")%' or definition like '" + searched_string + " %' or definition like '" + searched_string + ",%' or definition like '" + searched_string + ".%' or definition like '" + searched_string + "-%' or definition like '" + searched_string + ")%')";
    }

    // 02/12/2017
    @SuppressLint("Range")
    public String[][] getLexAndID4Thesaurus(String thesaurus)
    {
        String[][] res_tab;
        Cursor c;
        c = db.rawQuery("select distinct l.id as word_id, lex, glossFR, glossEN from " + thesaurus_TABLE_NAME + " t, " + sense_TABLE_NAME + " s, " + lex_id_cf_gloss_TABLE_NAME + " l where semantic_domain like \"" + thesaurus +"\" and t.sense_id = s.sense_id and s.id = l.id", null);
        res_tab = new String[4][c.getCount()];
        int i = 0;
        if (c.moveToFirst()) {
            do {
                res_tab[0][i] = c.getString(c.getColumnIndex("lex"));
                res_tab[1][i] = c.getString(c.getColumnIndex("word_id"));
                res_tab[2][i] = c.getString(c.getColumnIndex("glossFR"));
                res_tab[3][i] = c.getString(c.getColumnIndex("glossEN"));
                i++;
            }
            while (c.moveToNext());
            c.close();
        }

        return res_tab;
    }
    @SuppressLint("Range")
    public String[][] getMatchingWords(String searched_string, String search_type, Boolean contains, String language, ArrayList<String> chosenCAT , ArrayList<String> chosenTH) {
        String[][] def_tab;
        if(searched_string.equals("*")) {
            if (chosenCAT.size() > 0 || chosenTH.size() > 0) {
                searched_string = "";
            }  // CC 200915 * pour rechercher tous les mots d'une/plusieurs cat ou th choisi(s)
            else { searched_string = "#*";}
        }

        String language_clause = "";
        String condition;

        if(contains) {
            searched_string = "%" + searched_string + "%";
        }
        condition = getFullWordCondition(searched_string);
        String otherConditions="";
        //traitement de la catégorie grammaticale (part of speech) :
        if(chosenCAT!=null) {
            boolean CAT = chosenCAT.size() > 0;//on veut une recherche dans les filtres CAT si on lui en a passé
            if (CAT) {//si tableau vide, pas de recherche dans les CAT grammaticales.
                String conditionCat = " AND ( ";
                for (int indexCAT = 0; indexCAT < chosenCAT.size(); indexCAT++) {
                    if (indexCAT > 0) {
                        conditionCat += " OR ";
                    }
                    conditionCat += " " + entry_TABLE_NAME + ".partofspeech = '" + chosenCAT.get(indexCAT) + "' ";
                }
                otherConditions += conditionCat + " ) ";
            }//fin CAT GRAM
        }

        if(chosenTH!=null) {
            boolean TH = chosenTH.size() > 0;//on veut une recherche dans les filtres TH si on lui en a passé
            if (TH) {//si tableau vide, pas de recherche dans les TH.
                String conditionTh = " AND ( ";
                for (int indexTH = 0; indexTH < chosenTH.size(); indexTH++) {
                    if (indexTH > 0) {
                        conditionTh += " OR ";
                    }
                    conditionTh += " " + thesaurus_TABLE_NAME + ".semantic_domain = '" + chosenTH.get(indexTH) + "' ";
                }
                otherConditions += conditionTh + " ) ";
            }//fin TH
        }


        if(language != null&&!language.equals("any")) language_clause = " AND lang = '" + language + "'";
        System.err.println("%%%%%%%%%%%%%%%%%%%% langue voulue = " + language);
        System.err.println("%%%%%%%%%%%%%%%%%%%% search type = " + search_type);
        System.err.println("%%%%%%%%%%%%%%%%%%%% conditions = " + condition);
        System.err.println("%%%%%%%%%%%%%%%%%%%% other conditions = " + otherConditions);
        System.err.println("%%%%%%%%%%%%%%%%%%%% REQUEST '"+ search_type+"' = " +condition+ otherConditions + language_clause);
       // System.out.println(">> getMatchingWords query = SELECT DISTINCT * FROM " + word_definition_TABLE_NAME + condition + language_clause);
        Cursor c;
        String langueDef = "";
        boolean[] hidden_languages = dictData.getHidden_languages4gloss();
        String colorDef = "<font color=\"#000000\">";//on récupère le code couleur correspondant à chaque langue pour l'affichage
        String colorFR = "<font color=\""+(SearchResultActivity.getColorFR()!=null?SearchResultActivity.getColorFR():"#29577a")+"\">";
        String colorEN = "<font color=\""+(SearchResultActivity.getColorEN()!=null?SearchResultActivity.getColorEN():"#a37135")+"\">";
        if(hidden_languages[0]&&(hidden_languages.length <= 1 || !hidden_languages[1])) {//si FR à ne pas afficher uniquement
            langueDef = "eng";
            colorDef = colorEN;
        }
        if((hidden_languages.length <= 1 || hidden_languages[1])&&!hidden_languages[0]) {//si EN à ne pas afficher uniquement
            langueDef = "fra";
            colorDef = colorFR;
        }
        String clFnt = "</font>";
        switch(search_type)// dans le SQL, ajout d'inner join et left join avec les autres tables (grammar, def, example...) pour pouvoir ajouter des conditions sur ces champs !
        {
            case "word": //pour la recherche locale (langue du dico),on recherche dans le lexème ! Donc jointure avec la table des définitions pour avoir les autres champs à afficher en résultat de recherche
                String conditionlangueDef="";
                if(langueDef.equals("fra")||langueDef.equals("eng")){
                    conditionlangueDef = " AND " + word_definition_TABLE_NAME + ".lang = '" +langueDef+ "' ";
                }
                String req = "SELECT DISTINCT lex_id_cf_gloss.lex, lex_id_cf_gloss.glossFR, lex_id_cf_gloss.glossEN, word_definition.id, word_definition.definition FROM \"" + lex_id_cf_gloss_TABLE_NAME + "\" LEFT JOIN \"" + word_definition_TABLE_NAME + "\" ON \"" + word_definition_TABLE_NAME + "\".id =  \"" + lex_id_cf_gloss_TABLE_NAME + "\".id LEFT JOIN \"" + entry_TABLE_NAME + "\" ON \"" + word_definition_TABLE_NAME + "\".id =  \"" + entry_TABLE_NAME  + "\".id  INNER JOIN \"" + sense_TABLE_NAME + "\" ON \"" + word_definition_TABLE_NAME  + "\".id =  \"" + sense_TABLE_NAME + "\".id LEFT JOIN \"" + thesaurus_TABLE_NAME + "\" ON \"" + sense_TABLE_NAME + "\".sense_id = \"" + thesaurus_TABLE_NAME  + "\".sense_id WHERE \"" + lex_id_cf_gloss_TABLE_NAME + "\".lex LIKE '" + searched_string + "'" + conditionlangueDef + otherConditions;
                System.err.println(req);
                c = db.rawQuery(req, null);
                //c = db.rawQuery("SELECT DISTINCT * FROM " + lex_id_cf_gloss_TABLE_NAME + " LEFT JOIN " + word_definition_TABLE_NAME + " ON "+word_definition_TABLE_NAME + ".id =  " + lex_id_cf_gloss_TABLE_NAME + ".id LEFT JOIN " + entry_TABLE_NAME+" ON " + word_definition_TABLE_NAME+".id =  " + entry_TABLE_NAME  + ".id  INNER JOIN "+sense_TABLE_NAME + " ON " + word_definition_TABLE_NAME  + ".id =  " + sense_TABLE_NAME + ".id LEFT JOIN " + thesaurus_TABLE_NAME + " ON " + sense_TABLE_NAME + ".sense_id = " + thesaurus_TABLE_NAME  + ".sense_id WHERE " + lex_id_cf_gloss_TABLE_NAME + ".lex LIKE \'" + searched_string + "\' "+ conditionlangueDef + otherConditions, null);
                //c = db.rawQuery("SELECT DISTINCT * FROM " + lex_id_cf_gloss_TABLE_NAME + " LEFT JOIN "+word_definition_TABLE_NAME+" ON "+word_definition_TABLE_NAME+".id =  " + lex_id_cf_gloss_TABLE_NAME + ".id LEFT JOIN "+entry_TABLE_NAME+" ON "+word_definition_TABLE_NAME+".id =  " + entry_TABLE_NAME  +".id  INNER JOIN "+sense_TABLE_NAME+" ON "+word_definition_TABLE_NAME+".id =  " + sense_TABLE_NAME + ".id LEFT JOIN "+thesaurus_TABLE_NAME+" ON "+sense_TABLE_NAME+".sense_id =  " + thesaurus_TABLE_NAME + ".sense_id WHERE " + lex_id_cf_gloss_TABLE_NAME + ".lex LIKE \'"+searched_string + "\' "+ conditionlangueDef + otherConditions, null);
                break;
            case "definition":
                c = db.rawQuery("SELECT DISTINCT * FROM " + lex_id_cf_gloss_TABLE_NAME + " LEFT JOIN " + word_definition_TABLE_NAME + " ON "+word_definition_TABLE_NAME + ".id =  " + lex_id_cf_gloss_TABLE_NAME + ".id  INNER JOIN " + entry_TABLE_NAME+" ON " + word_definition_TABLE_NAME+".id =  " + entry_TABLE_NAME  +".id   LEFT JOIN "+sense_TABLE_NAME+" ON "+word_definition_TABLE_NAME+".id =  " + sense_TABLE_NAME + ".id LEFT JOIN "+thesaurus_TABLE_NAME+" ON "+sense_TABLE_NAME+".sense_id =  " + thesaurus_TABLE_NAME + ".sense_id "+ condition + language_clause + otherConditions, null);
                break;
            case "example"://maintenant on cherche quelle que soit la langue, et on joint avec les tables grammar et example pour la recherche avancée
                c = db.rawQuery("SELECT DISTINCT * FROM " + lex_id_cf_gloss_TABLE_NAME + " LEFT JOIN " + word_definition_TABLE_NAME + " ON "+word_definition_TABLE_NAME + ".id =  " + lex_id_cf_gloss_TABLE_NAME + ".id INNER JOIN " + word_example_TABLE_NAME  + " ON " + word_definition_TABLE_NAME+".id =  " + word_example_TABLE_NAME  + ".id  INNER JOIN "+entry_TABLE_NAME+" ON "+word_definition_TABLE_NAME+".id =  " + entry_TABLE_NAME  + ".id  LEFT JOIN "+sense_TABLE_NAME+" ON "+word_definition_TABLE_NAME+".id =  " + sense_TABLE_NAME + ".id LEFT JOIN "+thesaurus_TABLE_NAME+" ON "+sense_TABLE_NAME+".sense_id =  " + thesaurus_TABLE_NAME + ".sense_id  WHERE " + word_example_TABLE_NAME  + ".example LIKE '" +searched_string + "'" + " AND "+word_example_TABLE_NAME+ ".lang = '" +language+ "' " + otherConditions, null);

                break;
        // @TODO regarder si ca a un sens de faire des recherches "citation form"
            case "citation form"://TODO ajouter le inner join GRAMMAR
                c = db.rawQuery("SELECT DISTINCT * FROM " + word_definition_TABLE_NAME + " WHERE definition LIKE '" + searched_string + "'" + language_clause, null);
                break;
            // @ TODO modifier le else (peut etre une erreur ou un warning ou je ne sais pas..)
            default:
                String condlangueDef = "";
                if(langueDef.equals("fra")||langueDef.equals("eng")){
                    condlangueDef = " AND " +word_definition_TABLE_NAME + ".lang = '" +langueDef+ "' ";
                }
                c = db.rawQuery("SELECT DISTINCT * FROM " + word_definition_TABLE_NAME + " WHERE definition LIKE '" + searched_string + "'" + condlangueDef + language_clause, null);
                break;
        }
        def_tab = new String[2][c.getCount()];
        int i = 0;

        if (c.moveToFirst()) {
            do {
                if(notAlreadyIn(def_tab[0],c.getString(c.getColumnIndex("id")))) {
                    def_tab[0][i] = c.getString(c.getColumnIndex("id"));
                    System.err.println(">>> search: /"+def_tab[0][i] + "/");
                    //on concacène avec les balises pour mettre les couleurs définies plus haut. Si ni FR ni EN masqué, alors on met les 2 langues avec chacune leur code couleur, avec un séparateur entre les 2
                    if (search_type.equals("example")) {
                        String definitionsApercu =  colorDef+c.getString(c.getColumnIndex("example"))+clFnt;
                        if(!hidden_languages[0]&&(hidden_languages.length>1&&!hidden_languages[1])) {//si on veut afficher FR et EN alors on affiche les glossFR et glossEN à la place de la definition dans la langue par défaut
                            definitionsApercu = (c.getString(c.getColumnIndex("glossFR"))!=null?colorFR+c.getString(c.getColumnIndex("glossFR"))+clFnt:"")+ ((c.getString(c.getColumnIndex("glossEN"))!=null&&(c.getString(c.getColumnIndex("glossEN")).length()>0))? separateur +colorEN+c.getString(c.getColumnIndex("glossEN"))+clFnt:"");
                        }
                        def_tab[1][i] = definitionsApercu;
                    } else {
                        if (search_type.equals("word")) {
                            String definitionsApercu = colorDef+c.getString(c.getColumnIndex("definition"))+clFnt;
                            if(!hidden_languages[0]&&(hidden_languages.length>1&&!hidden_languages[1])) {//si on veut afficher FR et EN alors on affiche les glossFR et glossEN à la place de la definition dans la langue par défaut
                                definitionsApercu = (c.getString(c.getColumnIndex("glossFR"))!=null?colorFR+c.getString(c.getColumnIndex("glossFR"))+clFnt:"")+ ((c.getString(c.getColumnIndex("glossEN"))!=null&&(c.getString(c.getColumnIndex("glossEN")).length()>0))? separateur +colorEN+c.getString(c.getColumnIndex("glossEN"))+clFnt:"");
                            }
                            def_tab[1][i] = definitionsApercu;
                        }
                        else {
                            if (search_type.equals("definition")) {
                                String definitionsApercu =  colorDef+c.getString(c.getColumnIndex("definition"))+clFnt;
                                if(!hidden_languages[0]&&(hidden_languages.length>1&&!hidden_languages[1])) {//si on veut afficher FR et EN alors on affiche les glossFR et glossEN à la place de la definition dans la langue par défaut
                                    definitionsApercu = (c.getString(c.getColumnIndex("glossFR"))!=null?colorFR+c.getString(c.getColumnIndex("glossFR"))+clFnt:"")+ ((c.getString(c.getColumnIndex("glossEN"))!=null&&(c.getString(c.getColumnIndex("glossEN")).length()>0))? separateur +colorEN+c.getString(c.getColumnIndex("glossEN"))+clFnt:"");
                                }
                                def_tab[1][i] = definitionsApercu;
                            }
                            else{
                                String definitionsApercu =  colorDef+c.getString(c.getColumnIndex("definition"))+clFnt;
                                if(!hidden_languages[0]&&(hidden_languages.length>1&&!hidden_languages[1])) {//si on veut afficher FR et EN alors on affiche les glossFR et glossEN à la place de la definition dans la langue par défaut
                                    definitionsApercu = (c.getString(c.getColumnIndex("glossFR"))!=null?colorFR+c.getString(c.getColumnIndex("glossFR"))+clFnt:"")+ ((c.getString(c.getColumnIndex("glossEN"))!=null&&(c.getString(c.getColumnIndex("glossEN")).length()>0))? separateur +colorEN+c.getString(c.getColumnIndex("glossEN"))+clFnt:"");
                                }
                                def_tab[1][i] = definitionsApercu;
                            }
                        }
                    }
                    i++;
                }
            }
            while (c.moveToNext());
            c.close();
        }
        return def_tab;
    }

    @SuppressLint("Range")
    public ArrayList<String> getDictionaryInformation() {
        ArrayList<String> result;
        result = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT DISTINCT * FROM " + dictionary_information_TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                result.add(c.getString(c.getColumnIndex("line")));
            }
            while (c.moveToNext());
            c.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public Integer getSoundFileRowId(String file_name) {
        int result = 0;
        Cursor c = db.rawQuery("SELECT MIN (ROWID) AS line FROM " + audio_description_TABLE_NAME + " WHERE fileName='" +file_name.replaceAll("'", "''")+ "'", null);
        if (c.moveToFirst()) {
            result = c.getInt(c.getColumnIndex("line"));
            c.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public ArrayList<ArrayList<AudioRepresentation>> getAudioRepresentationList(String id) {
        ArrayList<ArrayList<AudioRepresentation>> result;
        result = new ArrayList<>();
        int current_cpt_entry = 0;
        ArrayList<AudioRepresentation> sub_result;
        sub_result = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT DISTINCT id, cpt_entry, startPosition, audioFileFormat, mediaType, fileName, quality FROM "+audio_description_TABLE_NAME+ " WHERE id='" +id.replaceAll("'", "''")+ "'", null);
        if (c.moveToFirst()) {
            do {
                AudioRepresentation current_ar = new AudioRepresentation();
                current_ar.quality = c.getString(c.getColumnIndex("quality"));
                current_ar.fileName = c.getString(c.getColumnIndex("fileName"));
                current_ar.mediaType = c.getString(c.getColumnIndex("mediaType"));
                current_ar.audioFileFormat = c.getString(c.getColumnIndex("audioFileFormat"));
                current_ar.startPosition = c.getString(c.getColumnIndex("startPosition"));
                if(c.getInt(c.getColumnIndex("cpt_entry")) == current_cpt_entry)
                {
                    sub_result.add(current_ar);
                }
                else
                {
                    result.add(sub_result);
                    sub_result = new ArrayList<>();
                    sub_result.add(current_ar);
                }
            }
            while (c.moveToNext());
            c.close();
        }
        if(sub_result.size() != 0)
        {
            result.add(sub_result);
        }
        return result;
    }

    // added for picture
    @SuppressLint("Range")
    public ArrayList<ArrayList<Illustration>> getIllustrationList(String id) {
       System.out.println("****************  Pictures **************");
       ArrayList<ArrayList<Illustration>> result;
        result = new ArrayList<>();
        int current_cpt_entry = 0;
        ArrayList<Illustration> sub_result;
        sub_result = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT DISTINCT id, cpt_entry, format, mediaType, fileName FROM "+ illustration_TABLE_NAME + " WHERE id='" +id.replaceAll("'", "''") + "'", null);
        System.out.println("SELECT DISTINCT id, cpt_entry, format, mediaType, fileName FROM " + illustration_TABLE_NAME + " WHERE id = '" + id.replaceAll("'", "''")+ "'");
        if (c.moveToFirst()) {
            System.out.println("URL trouvé");
            do {
                Illustration illustration = new Illustration();
                illustration.fileName = c.getString(c.getColumnIndex("fileName"));
                illustration.mediaType = c.getString(c.getColumnIndex("mediaType"));
                illustration.format = c.getString(c.getColumnIndex("format"));
                if(c.getInt(c.getColumnIndex("cpt_entry")) == current_cpt_entry)
                {
                    sub_result.add(illustration);
                    System.out.println(illustration.fileName);
                }
                else
                {
                    result.add(sub_result);
                    sub_result = new ArrayList<>();
                    sub_result.add(illustration);
                }
            }
            while (c.moveToNext());
            c.close();
        }
        if(sub_result.size() != 0)
        {
            result.add(sub_result);
        }
        return result;
    }

    @SuppressLint("Range")
    public String getGrammar(String id) {
        String result = "";
        Cursor c = db.rawQuery("SELECT DISTINCT * FROM "+word_grammar_TABLE_NAME+ " WHERE id='" +id.replaceAll("'", "''")+ "'", null);
        if (c.moveToFirst()) {
            result = c.getString(c.getColumnIndex("grammar"));
            c.close();
        }
        return result;
    }

    private Cursor getLex_id_cf_gloss() {  // CC Order by lexie (colonne copie de lex avec traitement de l'espace et tiret)
        //return db.rawQuery("SELECT * FROM " + lex_id_cf_gloss_TABLE_NAME + " ORDER BY lexie COLLATE NOCASE", null);
        return db.rawQuery("SELECT * FROM " + lex_id_cf_gloss_TABLE_NAME, null);
    }

    private Cursor getCorrespondence() {
        return db.rawQuery("SELECT * FROM "+correspondence_TABLE_NAME, null);
    }

    private Cursor getSortedLetters(boolean initials_only) {
        return db.rawQuery("SELECT str, rank FROM "+letters_TABLE_NAME+ (initials_only? " WHERE is_initial='true' " :"") + " ORDER BY rank", null);
    }

    private Cursor getLanguages() {
        return db.rawQuery("SELECT * FROM "+languages_TABLE_NAME, null);
    }

    private Cursor getAlphabets() {
        return db.rawQuery("SELECT * FROM "+alphabets_TABLE_NAME, null);
    }

    @SuppressLint("Range")
    public String[][] getLex_id_cf_glossTab3(){//seul algo gardé car le plus performant (389ms)
      //  long debut = System.currentTimeMillis(); //utilisé pour mesurer la durée d'exécution de l'algo
        class Lex_id_cf_gloss
        {
            final String lex;
            final String id;
            final String cf;
            final String glossFR;
            final String glossEN;
            final String pos;
            final String prev;    // CC
            final String next;    // CC

            public Lex_id_cf_gloss(String lex, String id, String cf, String glossFR, String glossEN, String pos, String pr, String nx) {
                this.lex=lex;
                this.id=id;
                this.cf=cf;
                this.glossFR=glossFR;
                this.glossEN=glossEN;
                this.pos = pos;
                this.next = nx;  // CC
                this.prev = pr;  // CC
            }

            public String getWord() {
                return lex;
            }
        }
        Cursor c = this.getLex_id_cf_gloss();
        ArrayList<Lex_id_cf_gloss> res_array = new ArrayList<>();
        int item = 0;
        String Word="", Id="", Cf="", Fr="", En="", Pos="";
        String Prev = "", Next="";
       /* while (c.moveToNext()){
            if (item == 0) {
                Word =  c.getString(c.getColumnIndex("lex"));
                Id = c.getString(c.getColumnIndex("id"));
                Cf = c.getString(c.getColumnIndex("cf"));
                Pos = c.getString(c.getColumnIndex("partofspeech"));
                Fr =  c.getString(c.getColumnIndex("glossFR"));
                En =  c.getString(c.getColumnIndex("glossEN"));
                item++;
            } else {
                res_array.add(new Lex_id_cf_gloss(Word, Id, Cf, Fr, En, Pos, Prev, c.getString(c.getColumnIndex("id"))));
                Prev = Id;
                Word =  c.getString(c.getColumnIndex("lex"));
                Id = c.getString(c.getColumnIndex("id"));
                Cf = c.getString(c.getColumnIndex("cf"));
                Pos = c.getString(c.getColumnIndex("partofspeech"));
                Fr =  c.getString(c.getColumnIndex("glossFR"));
                En =  c.getString(c.getColumnIndex("glossEN"));
                item++;
            }
        }
        res_array.add(new Lex_id_cf_gloss(Word, Id, Cf, Fr, En, Pos, Prev, ""));
        c.close();*/
        if (c.moveToNext()) { //CC 05/05/22
            do {
                if (item == 0) {
                    Word = c.getString(c.getColumnIndex("lex"));
                    Id = c.getString(c.getColumnIndex("id"));
                    Cf = c.getString(c.getColumnIndex("cf"));
                    Pos = c.getString(c.getColumnIndex("partofspeech"));
                    Fr = c.getString(c.getColumnIndex("glossFR"));
                    En = c.getString(c.getColumnIndex("glossEN"));
                    item++;
                } else {
                    res_array.add(new Lex_id_cf_gloss(Word, Id, Cf, Fr, En, Pos, Prev, c.getString(c.getColumnIndex("id"))));
                    Prev = Id;
                    Word = c.getString(c.getColumnIndex("lex"));
                    Id = c.getString(c.getColumnIndex("id"));
                    Cf = c.getString(c.getColumnIndex("cf"));
                    Pos = c.getString(c.getColumnIndex("partofspeech"));
                    Fr = c.getString(c.getColumnIndex("glossFR"));
                    En = c.getString(c.getColumnIndex("glossEN"));
                    item++;
                }
            }
            while (c.moveToNext());
             c.close();
        }
        res_array.add(new Lex_id_cf_gloss(Word, Id, Cf, Fr, En, Pos, Prev, Id));  // CC 220523 dernier item n'était pas traité
        // CC 241226 tri par rapport au mot pour la liste des mots commençant par une lettre
        Collections.sort(res_array, new Comparator<Lex_id_cf_gloss>() {
            @Override
            public int compare(Lex_id_cf_gloss o1, Lex_id_cf_gloss o2) {
                return o1.getWord().compareTo(o2.getWord()); // Comparaison basée sur le champ 'Word'
            }
        });
        String[][] res_tab = new String[8][res_array.size()];       // CC
        int tab_iterator = 0;
        for (Lex_id_cf_gloss line : res_array)
        {
            res_tab[0][tab_iterator] = line.lex;
            res_tab[1][tab_iterator] = line.id;
            res_tab[2][tab_iterator] = line.cf;
            res_tab[3][tab_iterator] = line.glossFR;
            res_tab[4][tab_iterator] = line.glossEN;
            res_tab[5][tab_iterator] = line.pos;
            res_tab[6][tab_iterator] = line.prev;
            res_tab[7][tab_iterator] = line.next;
            tab_iterator++;
        }

      //  System.err.println(" ------ getLex_id_cf_glossTab3 execution time : " + (System.currentTimeMillis() - debut));
        return res_tab;
    }
    @SuppressLint("Range")
    public Map<String,String> getCorrespondenceTab(){
        Map<String,String> ascii_devanagari_list = new LinkedHashMap<>();
        //int tab_iterator = 0;
        Cursor c = this.getCorrespondence();
        String local_letter, ascii_correspondence;
        if (c.moveToFirst())
        {
            do {
                local_letter = c.getString(c.getColumnIndex("local_letter"));
                ascii_correspondence = c.getString(c.getColumnIndex("ascii_correspondence"));
                ascii_devanagari_list.put(local_letter, ascii_correspondence);
            }
            while (c.moveToNext());
        }
        c.close();
        return ascii_devanagari_list;
    }

    @SuppressLint("Range")
    public String[] getSortedInitialLettersTab(){
        int tab_iterator = 0;
        boolean is_sorted = false;
        Cursor c = this.getSortedLetters(true);
        String[] res_tab = new String[c.getCount()];
        System.err.println(" >>>>>>>> >>>>>>>> getSortedInitialLettersTab : c.getCount() = " + c.getCount());
        if (c.moveToFirst()) {
            do {
                res_tab[tab_iterator] = c.getString(c.getColumnIndex("str"));
                if (c.getFloat(c.getColumnIndex("rank")) > 0.0) is_sorted = true;
                System.err.println(" >>>>>>>> >>>>>>>> getSortedInitialLettersTab : res_tab["+ tab_iterator + "] = " + res_tab[tab_iterator]);
                tab_iterator++;
            }
            while (c.moveToNext());
        }
        c.close();
        if(!is_sorted)
            Arrays.sort(res_tab);
        return res_tab;
    }

    @SuppressLint("Range")
    public ArrayList<String> getComplementLettersList(){
        System.err.println("rorororororororor");
        Cursor c = this.getSortedLetters(false);
        ArrayList<String> res_list = new ArrayList<>();
        if (c.moveToFirst())
        {
            do {
                res_list.add(c.getString(c.getColumnIndex("str")));
            }
            while (c.moveToNext());
        }
        c.close();
        return res_list;
    }

    @SuppressLint("Range")
    public String[] getLanguagesTab(){
        int tab_iterator = 0;
        Cursor c = this.getLanguages();
        String[] res_tab = new String[c.getCount()];

        if (c.moveToFirst())
        {
            do {
                res_tab[tab_iterator] = c.getString(c.getColumnIndex("language"));
                tab_iterator++;
            }
            while (c.moveToNext());
        }
        c.close();
        return res_tab;
    }

    @SuppressLint("Range")
    public String[] getAlphabetsTab(){
        int tab_iterator = 0;
        Cursor c = this.getAlphabets();
        String[] res_tab = new String[c.getCount()];

        if (c.moveToFirst())
        {
            do {
                res_tab[tab_iterator] = c.getString(c.getColumnIndex("alphabet"));
                tab_iterator++;
            }
            while (c.moveToNext());
        }

        c.close();
        return res_tab;
    }

    private final static String entry_TABLE_NAME = "entry";
    private final static String definition_TABLE_NAME = "definition";
    private final static String sense_TABLE_NAME = "sense";
    private final static String context_TABLE_NAME = "context";
    private final static String lemma_html_block_TABLE_NAME = "lemma_html_block";
    private final static String sense_link_TABLE_NAME = "sense_link";

    private final static String thesaurus_TABLE_NAME = "thesaurus";

    private final static String pdl_pdv_TABLE_NAME = "pdl_pdv";
    //public Map<String, String> getPdlPdv(String sense_id) {

    @SuppressLint("Range")
    public ArrayList<String[]> getPdlPdv(String sense_id) {
        //Map<String, String> result;
        ArrayList<String[]> pdl_pdv_list = new ArrayList<>();
        //result = new HashMap<>();

        Cursor c = db.rawQuery("SELECT DISTINCT * FROM "+ pdl_pdv_TABLE_NAME + " WHERE sense_id='" +sense_id.replaceAll("'", "''")+ "'", null);
        if (c.moveToFirst()) {
            do {
                System.err.println("pdl_pdv_TABLE_NAME : pour sense_id = " + sense_id + " pdl = " + c.getString(c.getColumnIndex("pdl")) + " pdv = " + c.getString(c.getColumnIndex("pdv")));
                //result.put(c.getString(c.getColumnIndex("pdl")), c.getString(c.getColumnIndex("pdv")));
                String[] pdl_pdv = new String[2];
                pdl_pdv[0] = c.getString(c.getColumnIndex("pdl"));
                pdl_pdv[1] = c.getString(c.getColumnIndex("pdv"));
                pdl_pdv_list.add(pdl_pdv);
            }
            while (c.moveToNext());
            c.close();
        }

        //return result;
        return pdl_pdv_list;
    }

    private Cursor getEntryCursor(String word) {
        return db.rawQuery("SELECT * FROM " + entry_TABLE_NAME + " WHERE id LIKE \"" + word + "\"", null);
    }

    @SuppressLint("Range")
    public Map<Integer, String> getEntry(String word){
        Map<Integer,String> entry = new HashMap<>();
        System.err.println("SELECT * FROM " + entry_TABLE_NAME + " WHERE id LIKE \"" + word + "\"");
        Cursor c = this.getEntryCursor(word);
        if (c.moveToFirst())
        {
            do {
                entry.put(c.getInt(c.getColumnIndex("cpt_entry")), c.getString(c.getColumnIndex("partofspeech")));
            }
            while (c.moveToNext());
        }
        c.close();
        return entry;
    }

    @SuppressLint("Range")
    public String getUsage(String sense_id, String lang){
        Cursor c = db.rawQuery("SELECT DISTINCT * FROM "+usage_TABLE_NAME + " WHERE sense_id='" +sense_id.replaceAll("'", "''")+ "' AND language = '" +lang+ "'", null);
        String usage = "";
        if (c.moveToFirst())
        {
            do {
               usage += c.getString(c.getColumnIndex("usage"));
            }
            while (c.moveToNext());
        }
        c.close();
        return usage;
    }

    @SuppressLint("Range")
    public ArrayList<String> getEntryId(String word){
        ArrayList<String> entry = new ArrayList<String>();
        System.err.println("SELECT * FROM " + entry_TABLE_NAME + " WHERE id LIKE \"" + word + "\"");
        Cursor c = this.getEntryCursor(word);
        if (c.moveToFirst())
        {
            do {
                entry.add(c.getString(c.getColumnIndex("id")));
            }
            while (c.moveToNext());
        }
        c.close();
        return entry;
    }

    private Cursor getDefinitionCursor(String sense_id) {
        String[] languages = getLanguagesTab();
        boolean[] hiddenLanguages = dictData.getHidden_languages4gloss();
        String conditions_hidden_languages = "";
        for(int i = 0; i<hiddenLanguages.length;++i){//on ajoute les conditions SQL pour ne aps récupérer les champs qui sont dans des langues non voulues
            if(hiddenLanguages[i]) {
                conditions_hidden_languages += " AND  language not like \"" + languages[i+1] + "\" ";
            }
        }
        System.err.println("HIDDEN FINAL CONDITIONS (getDefinitionCursor) = " + conditions_hidden_languages);//on ne tient pas compte des champs dans les langues non voulues
        return db.rawQuery("SELECT * FROM " + definition_TABLE_NAME + " WHERE sense_id = \"" + sense_id + "\" AND definition not like \"null\" " + conditions_hidden_languages,null);
    }


    @SuppressLint("Range")
    public Map<String, String> getDefinition(String sense_id){
        Map<String,String> entry = new HashMap<>();
        Cursor c = this.getDefinitionCursor(sense_id);
        if (c.moveToFirst())
        {
            do {
                System.err.println(">>>> >>>> >>>>> >>>>> getDefinition : lgg = " + c.getColumnIndex("language") + ", def = " + c.getString(c.getColumnIndex("definition")));
                if(entry.containsKey(c.getString(c.getColumnIndex("language")))) {
                    entry.put(c.getString(c.getColumnIndex("language")), entry.get(c.getString(c.getColumnIndex("language"))) + ", " + c.getString(c.getColumnIndex("definition")));
                }
                else {
                    entry.put(c.getString(c.getColumnIndex("language")), c.getString(c.getColumnIndex("definition")));
                }
            }
            while (c.moveToNext());
        }
        /*
        if (c.moveToFirst()){
            do {
                entry.put(c.getString(c.getColumnIndex("language")), c.getString(c.getColumnIndex("definition")));
                System.out.println(c.getString(c.getColumnIndex("definition")));
            }
            while (c.moveToNext());
        }
        else {
            return null;
        }*/
        c.close();
        return entry;
    }

    @SuppressLint("Range")
    public ArrayList<String> getSenseId(String id, int cpt_entry) {
        ArrayList<String> result;
        result = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT sense_id FROM " + sense_TABLE_NAME + " WHERE id = \"" + id + "\" AND cpt_entry = " + cpt_entry, null);
        if (c.moveToFirst()) {
            do {
                result.add(c.getString(c.getColumnIndex("sense_id")));
            }
            while (c.moveToNext());
            c.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public int getHmNumber(String id) {
        int result = 0;
        Cursor c = db.rawQuery("SELECT num_homonym FROM " +homonym_number_TABLE_NAME+ " WHERE id = \"" + id + "\"", null);
        if (c.moveToFirst()) {
            result = c.getInt(c.getColumnIndex("num_homonym"));
            c.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public Map<Integer, ArrayList<String[]>> getContext_cpt__sn_wf_l(String sense_id) {
        String[] languages = getLanguagesTab();
        boolean[] hiddenLanguages = dictData.getHidden_languages4gloss();
        String conditions_hidden_languages = "";
        for(int i = 0; i<hiddenLanguages.length;++i){//on ajoute les conditions SQL pour ne aps récupérer les champs qui sont dans des langues non voulues
            if(hiddenLanguages[i]) {
                System.err.println("HIDDEN LANGUAGE FOUND : "+hiddenLanguages[i]+" for : "+languages[i+1]+" (index = "+i+")");
                conditions_hidden_languages += " AND  language not like \"" + languages[i+1] + "\" ";
            }
        }
        Map<Integer, ArrayList<String[]>> result;
        result = new LinkedHashMap<>();
        Cursor c = db.rawQuery("SELECT * FROM " + context_TABLE_NAME + " WHERE sense_id = \"" + sense_id + "\" "+conditions_hidden_languages+" order by num_context", null);
        if (c.moveToFirst())
        {
            do {
                String[] sn_wf_l_tmp = new String[3];
                sn_wf_l_tmp[0] = c.getString(c.getColumnIndex("scriptName"));
                sn_wf_l_tmp[1] = c.getString(c.getColumnIndex("writtenForm"));
                sn_wf_l_tmp[2] = c.getString(c.getColumnIndex("language"));

                if(result.containsKey(c.getInt(c.getColumnIndex("num_context"))))
                {
                    result.get(c.getInt(c.getColumnIndex("num_context"))).add(sn_wf_l_tmp);
                }
                else
                {
                    ArrayList<String[]> val_tab = new ArrayList<>();
                    val_tab.add(sn_wf_l_tmp);
                    result.put(c.getInt(c.getColumnIndex("num_context")), val_tab);
                }
            }
            while (c.moveToNext());
        }
        c.close();
        return result;
    }

    @SuppressLint("Range")
    public String getLemma_html_block(String id, int cpt_entry) {
        String result = "";
        Cursor c = db.rawQuery("SELECT DISTINCT * FROM "+lemma_html_block_TABLE_NAME+ " WHERE id= \"" + id +"\" AND cpt_entry = " + cpt_entry, null);
        if (c.moveToFirst()) {
            result = c.getString(c.getColumnIndex("lemma_html_block"));
            c.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public ArrayList<String[]> getSenseLinks(String sense_id) {
        ArrayList<String[]> result;

        result = new ArrayList<>();
        String sql = "SELECT * FROM " + sense_link_TABLE_NAME + " WHERE id = \"" + sense_id+ "\"";
        //System.out.println(sql);
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                String[] target_type_id = new String[3];
                target_type_id[0] = c.getString(c.getColumnIndex("target"));
                target_type_id[1] = c.getString(c.getColumnIndex("type"));
                target_type_id[2] = c.getString(c.getColumnIndex("target_id"));
                result.add(target_type_id);
            }
            while (c.moveToNext());
            c.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public ArrayList<String> getSenseThesaurus(String sense_id) {
        ArrayList<String> result;

        result = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + thesaurus_TABLE_NAME + " WHERE sense_id = \"" + sense_id+ "\"", null);
        if (c.moveToFirst()) {
            do {
                result.add(c.getString(c.getColumnIndex("semantic_domain")));
            }
            while (c.moveToNext());
            c.close();
        }
        Collections.sort(result);  // CC 241226
        return result;
    }

    @SuppressLint("Range")
    public String getMainEntry(String id) {
        // retourne le premier target (lexeme) correspondant au id (lex et se)
        if(id == null){ return null; }
        String link_TABLE_NAME = "link";
        String result = "";
        Cursor c = db.rawQuery("SELECT DISTINCT target FROM "+ link_TABLE_NAME + " WHERE id='" + id.replaceAll("'", "''")+ "' AND type = \"mainEntry\"", null);

        if (c.moveToFirst()) {
            result = c.getString(c.getColumnIndex("target"));
            c.close();
        }
        if(result == ""){result = id;}
        // CC : nb = nombre de sous entrées, pour nextWord et previousWord
        c = db.rawQuery("SELECT DISTINCT id FROM "+ link_TABLE_NAME + " WHERE target='" + result .replaceAll("'", "''")+ "' AND type = \"mainEntry\"", null);
        int nb = c.getCount();
        if (c.moveToFirst()) {
            result += "/" + nb;
            c.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public ArrayList<String> getSubEntry(String main_entry_id){
        String link_TABLE_NAME = "link";
        Cursor c = db.rawQuery("SELECT id FROM " + link_TABLE_NAME + " WHERE target='" + main_entry_id.replaceAll("'", "''") + "' AND type = \"mainEntry\"", null);
        ArrayList<String> sub_entry_list = new ArrayList<>();
        if (c.moveToFirst()){
            do {
                sub_entry_list.add(c.getString(c.getColumnIndex("id")));
            }
            while (c.moveToNext());
        }
        c.close();
        return sub_entry_list;
    }

    @SuppressLint("Range")
    public ArrayList<String[]> getLexNote(String id){
        String lex_note_TABLE_NAME = "lex_note";
        Cursor c = db.rawQuery("SELECT * FROM " + lex_note_TABLE_NAME + " WHERE id='" + id /*.replaceAll("\'", "\'\'")*/ + "'", null);
        ArrayList<String[]> lex_note_list = new ArrayList<>();
        if (c.moveToFirst())
        {
            do {
                String[] lex_note = new String[3];
                lex_note[0] = c.getString(c.getColumnIndex("type"));
                lex_note[1] = c.getString(c.getColumnIndex("note"));
                lex_note[2] = c.getString(c.getColumnIndex("language"));

                lex_note_list.add(lex_note);
            }
            while (c.moveToNext());
        }
        c.close();
        return lex_note_list;
    }

    /*
     * @ function : permet de vérifier si un objet existe déjà dans un tableau, utilisé pour dédoublonner les résultats de recherche
     * @ return : boolean, false si déjà dans le tableau, true sinon.
     */
    public boolean notAlreadyIn(Object[] tableau, Object o){
        for(int i=0; i<tableau.length;i++){
            if(tableau[i] != null && tableau[i].equals(o)){
                return false;
            }
        }
        return true;
    }



    /*
     * @ function : permet de créer la liste des "Part of speech" (CAT grammaticales) existantes en BDD
     * @ return : ArrayList<String>, la liste contenant tous les part of speech existants dédoublonnés)
     */
    @SuppressLint("Range")
    public ArrayList<String> getPartOfSpeech() {
        ArrayList<String> partOfSpeech = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT DISTINCT trim(partofspeech) as partofspeech FROM " + entry_TABLE_NAME  + " ORDER BY partofspeech", null);
        if (c.moveToFirst()) {
            do {
                String nextPartOfSpeech = c.getString(c.getColumnIndex("partofspeech")).trim();
                partOfSpeech.add(nextPartOfSpeech);
            }
            while (c.moveToNext());
            c.close();
        }
        //Collections.sort(partOfSpeech);
        return partOfSpeech;
    }

    /*
     * @ function : permet de créer la liste des Thesaurus (champ semantic_domain de la table Thesaurus) existants en BDD
     * @ return : ArrayList<String>, la liste contenant tous les thesaurus existants (dédoublonnés)
     */
    @SuppressLint("Range")
    public ArrayList<String> getThesaurus() {
        ArrayList<String> thesaurus = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT DISTINCT trim(semantic_domain) as semantic_domain  FROM " + thesaurus_TABLE_NAME , null);
        if (c.moveToFirst()) {
            do {
                String nextThesaurus = c.getString(c.getColumnIndex("semantic_domain"));//.trim()
                    thesaurus.add(nextThesaurus);
            }
            while (c.moveToNext());
            c.close();
        }
        Collections.sort(thesaurus);
        return thesaurus;
    }

    @SuppressLint("Range")
    public String getVariantes(String idWord){  // CC 1810
        //liste des variantes d'un lexème (s'ajoute à la suite du lexème après ~)
        String variante = "";
        Cursor c = db.rawQuery("SELECT variant_form FROM " + variants_TABLE_NAME  + " WHERE id = '" + idWord + "'", null);
        if (c.moveToFirst()) {
            do {
                String nextVariante = c.getString(c.getColumnIndex("variant_form"));//.trim()
                variante += nextVariante + ", ";
            }
            while (c.moveToNext());
            variante = variante.substring(0,variante.length()-2);
            c.close();
        }
        return variante;
    }

    @SuppressLint("Range")
    public String getWord4Variante(String variante){  // CC 2205
        //renvoie seulement le premier mot correspondant à une variante, si plusieurs (?)
        String word = "";
        String req = "SELECT DISTINCT lex FROM " + variants_TABLE_NAME  + ", " + lex_id_cf_gloss_TABLE_NAME + " WHERE variant_form = '" + variante + "' and variant.id= lex_id_cf_gloss.id";
        //System.out.println(req);
        Cursor c = db.rawQuery(req, null);
        if (c.moveToFirst()) {
            word = c.getString(c.getColumnIndex("lex"));//.trim()
            c.close();
        }
        return word;
    }

    @SuppressLint("Range")
    public ArrayList<String> getVariantes(){  // CC 2205
        // liste de toutes les variantes pour affichage dans autocompletion
        ArrayList<String> variantes = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT variant_form FROM " + variants_TABLE_NAME , null);
        if (c.moveToFirst()) {
            do {
                String nextVariante = c.getString(c.getColumnIndex("variant_form"));//.trim()
                variantes.add(nextVariante);
            }
            while (c.moveToNext());
            c.close();
        }
        return variantes;
    }
}