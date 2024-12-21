package com.infolangues.mondico;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import java.util.Iterator;
//import java.util.Scanner;

/**
 * Created by Remy Bonnet on 17/08/2015
 * Rebuild and additionnal design and functions by Maël Franceschetti on 05/2018
 * Maintained by Christian Chanard
 * Logic tier
 */
class dictData {
    @SuppressLint("StaticFieldLeak")
    private static final Context context = MyApplication.getAppContext();
    private static final String regex4note = context.getString(R.string.regex4phonetic_notes);
    private static DictManager dict_manager;
    private static String[][] lex_id_cf_gloss;
    static Map<String,String> ascii_devanagari_correspondence;
    private static ArrayList<String> words_ascii_version;
    private static ArrayList<Integer> id_index4ascii_words;
    static ArrayList<String> alphabets_list;
    static ArrayList<String> languages_list;
    private static boolean[] hidden_languages4gloss;

    private dictData() {}

    public static boolean[]getHidden_languages4gloss() {
        System.err.println("hidden languages list DictData : " + hidden_languages4gloss);
        return hidden_languages4gloss;
    }

    public static void setHidden_languages4gloss(int index, boolean value) {
        hidden_languages4gloss[index] = value;
        System.err.println("hidden language DictData : "+hidden_languages4gloss[index]);
    }

    private static void createAsciiVersionOfLocalWords()
    {
        words_ascii_version = new ArrayList<>();
        id_index4ascii_words = new ArrayList<>();
        if (!ascii_devanagari_correspondence.isEmpty()) {
            int word_count = 0;
            for (String original_word : lex_id_cf_gloss[2]) {
                String[] current_ascii_word = localString2ascii(original_word);
                for (String current_ascii_version : current_ascii_word) {
                    words_ascii_version.add(current_ascii_version);
                    id_index4ascii_words.add(word_count);
                }
                word_count++;
            }
            System.out.println(">>>>>>>>>>>>>>>>>> word_count = " + word_count);
        }
    }


    public static void initData4usersFromSQL(Context context) {
        System.err.println("****************************************************** go");
        dict_manager = new DictManager(context);
        dict_manager.open();
        long debut = System.currentTimeMillis();
        System.out.println("initData4usersFromSQL 0 : " + (System.currentTimeMillis() - debut));
        lex_id_cf_gloss = dict_manager.getLex_id_cf_glossTab3();
       // System.out.println("initData4usersFromSQL 1 : " + (System.currentTimeMillis() - debut));
        ascii_devanagari_correspondence = dict_manager.getCorrespondenceTab();
       // System.out.println("initData4usersFromSQL 2 : " + (System.currentTimeMillis() - debut));
        String[] languages_tab = dict_manager.getLanguagesTab();
        hidden_languages4gloss = new boolean[languages_tab.length - 1]; // the first language is the dictionary language
       // System.out.println("initData4usersFromSQL 3 : " + (System.currentTimeMillis() - debut));
        String[] alphabets_tab = dict_manager.getAlphabetsTab();
       // System.out.println("initData4usersFromSQL 4 : " + (System.currentTimeMillis() - debut));
        languages_list = new ArrayList<>(Arrays.asList(languages_tab));
        alphabets_list = new ArrayList<>(Arrays.asList(alphabets_tab));
       // System.out.println("initData4usersFromSQL 5 : " + (System.currentTimeMillis() - debut));
        dictData.createAsciiVersionOfLocalWords();
    }

    // @TODO temporary function
    private static boolean isConsonant(char in_letter)
    {
        for (String letter : ascii_devanagari_correspondence.keySet())
        {
            if (letter.equals(""+in_letter))
                return true;
            if (letter.equals(" "))
                return false;
        }
        // @TODO send a error (if we keep the function)
        return false;
    }


    // new
    private static String[] localString2ascii(String local_string)
    {
        String draft;
        draft = "";
        char cur_char;
        boolean last_letter_is_consonant = false;
        for(int i = 0; i < local_string.length(); i++) {
            cur_char = local_string.charAt(i);
            // @TODO modifier cette partie (ecriture du 'a' par defaut) afin qu'elle soit deduite d'une regle de ascii_correspondance
            if (isConsonant(cur_char))
            {
                if (last_letter_is_consonant) {
                    draft += 'a';
                }
                last_letter_is_consonant = true;
            }
            else
            {
                if(cur_char != ':')  // on ignore le ':'
                    last_letter_is_consonant = false;
            }
            draft += localLetter2ascii("" + local_string.charAt(i));
        }
        if (local_string.charAt(local_string.length()-1) != '्') {
            draft += 'a';
        }
        int current_index = draft.indexOf('/');
        int number_of_slash = 0;
        String tmp_substring = draft;
        while(current_index != -1)
        {
            //System.out.println("current_index F! = " + current_index);
            tmp_substring = tmp_substring.substring(current_index + 1);
            current_index = tmp_substring.indexOf('/');
            //current_index = draft.substring(current_index+1).indexOf('/');
            number_of_slash++;
        }
        //System.out.println("A number_of_possibilities = (int)Math.pow(2," + number_of_slash + ") " + (int)Math.pow(2,number_of_slash));
        int number_of_possibilities = (int) Math.pow(2,number_of_slash);
        String[] res = new String[number_of_possibilities];
        String line;

        for(int i = 0; i < number_of_possibilities; i++)
        {
            line = stringFromDraft(draft, i);
            res[i] = line;
        }
        return res;
    }

    private static String stringFromDraft(String draft, int count)
    {
        int start = draft.indexOf('[');
        int end, delimiter, number_of_slash = 0;

        while(start != -1)
        {
            start = draft.indexOf('[');
            end = draft.indexOf(']');
            delimiter = draft.indexOf('/');
            number_of_slash++;

            draft = draft.substring(0, start) + (count%(int) Math.pow(2,number_of_slash)<(int) Math.pow(2,number_of_slash)/2?draft.substring(start+1, delimiter):draft.substring(delimiter + 1, end)) + draft.substring(end+1);
            start = draft.indexOf('[');
        }

        return draft;
    }

    // new
    private static String localLetter2ascii(String local_letter)
    {
        String res = "";

        if(ascii_devanagari_correspondence.containsKey(local_letter))
            res = ascii_devanagari_correspondence.get(local_letter);
        if(ascii_devanagari_correspondence.containsKey("+"+local_letter)) {
            res = ascii_devanagari_correspondence.get("+"+local_letter);
        }
        String s = res.contains("/")? "[" + res + "]" : res;
        return (s);
    }

    private static String format_sense(String sens)
    {
        if(sens.equals("null")) return "";
        MyApplication.Log("format_sense >> SenseString = " + sens);
        MyApplication.Log("format_sense >> sense = " + sens);
        Pattern p= Pattern.compile("fl\\{(.*?)\\}");
        Matcher m=p.matcher(sens);
        while(m.find())
        {
            String d = m.group(1);
            if(d != null) {
                System.err.println(d);
            }
            else
                System.err.println("c est quand meme bizarre la");
        }
        MyApplication.Log("format_sense >> SenseString remplace = " + sens.replaceAll("\\|fl\\{(.*?)\\}", "<RT>$1<TR>"));
        MyApplication.Log("format_sense >> SenseString replace vl = " + sens.replaceAll("\\|fv\\{(.*?)\\}", "<font color=\"\\#999966\">$1<\\/font>"));
        MyApplication.Log("format_sense >> SenseString after replace vl = " + sens);
        sens = sens.replaceAll("\\|fl\\{(.*?)\\}", "<i>$1<\\/i>");
        // ax est sense etre comme fl j'ai donc mis
        sens = sens.replaceAll("\\|ax\\{(.*?)\\}", "<i>$1<\\/i>");
        // la par contre c'etait plus pour mettre quelque chose
        sens = sens.replaceAll("\\|fs\\{(.*?)\\}", "<i>$1<\\/i>");
        // ca ca devrait etre ca
        sens = sens.replaceAll("\\|fv\\{(.*?)\\}", "<font color=\"\\#999966\">$1<\\/font>");//OLD : #999966
        // vl est sense etre comme fv j'ai donc mis
        sens = sens.replaceAll("\\|vl\\{(.*?)\\}", "<font color=\"\\#999966\">$1<\\/font>");
        sens = sens.replaceAll(" fl:(.*?)([ \\).$])", " <i>$1<\\/i>$2");
        sens = sens.replaceAll(" ax:(.*?)([ \\).$])", " <i>$1<\\/i>$2");
        sens = sens.replaceAll("([ \\(])fv:(.*?)([ \\).$])", "$1<font color=\"\\#999966\">$2<\\/font>$3");
        sens = sens.replaceAll(" vl:(.*?)([ \\).$])", " <font color=\"\\#999966\">$1<\\/font>$2");
        return sens;
    }

    // @TODO supprimer
    public static Map<Integer, String> getEntry4word(String word)
    {
        return dict_manager.getEntry(word);
    }

    public static ArrayList<String> getEntryId(String word){
        return dict_manager. getEntryId(word);
    }

    public static ArrayList<String> getSenseIdList4Entry(String word, Map.Entry<Integer, String> current_entry)
    {
        return dict_manager.getSenseId(word, current_entry.getKey());
    }
    public static ArrayList<String[]> getSenseLinks(String sense_id)
    {
        return dict_manager.getSenseLinks(sense_id);
    }

    public static String getLexNoteString(String id)
    {
        String lex_note_string = "";
        ArrayList<String[]> lex_note_list = dict_manager.getLexNote(id);
        for (String[] lex_note : lex_note_list)
        {
            lex_note_string += "<br/>[" + lex_note[0] + " : " + lex_note[1] + "]";
        }

        return lex_note_string;
    }

    public static ArrayList<String> getThesaurus(String sense_id)
    {
        return dict_manager.getSenseThesaurus(sense_id);
    }

    public static String formatLink(String[] link)
    {
        return "→ " + link[1] + " : " + link[0];
    }

    public static String create_entry_header(String word, Map.Entry<Integer, String> current_entry)    {
        String toReturn = current_entry.getValue();
        return toReturn.replace("<h2>","").replace("</h2>","").replace("<h3>","").replace("</h3>","");
    }

    public static String create_sense(ArrayList<String> sense_id_list, int cpt_sense_id)
    {
        String res_sense = "";
        String sense_string;
        Map<String, String> def_lgg = dict_manager.getDefinition(sense_id_list.get(cpt_sense_id));
        sense_string = create_def_string(def_lgg, sense_id_list, cpt_sense_id);
        String pdl_pdvText = dictData.getPdlPdvText(sense_id_list.get(cpt_sense_id));
        if(!pdl_pdvText.isEmpty()){
            pdl_pdvText = "<font color=\"#00008B\">" + pdl_pdvText + "</font>";
        }
        if(!(format_sense(sense_string) + pdl_pdvText).isEmpty()) {
            res_sense += format_sense(sense_string) + pdl_pdvText ; //j'ai retiré les h3 car le padding bottom était trop important !
        }
        return res_sense;
    }

    public static String create_examples(ArrayList<String> sense_id_list, int cpt_sense_id){
        String res_examples = "";
        Map<Integer, ArrayList<String[]>> cpt__sn_wf_l = dict_manager.getContext_cpt__sn_wf_l(sense_id_list.get(cpt_sense_id));
        if(cpt__sn_wf_l != null && !cpt__sn_wf_l.isEmpty()) { //peut être nul si ne contient que des langues non voulues
            String baliseCouleurOp = "";
            String baliseCouleurCl = "";
            res_examples += baliseCouleurOp+create_context_string(cpt__sn_wf_l)+ baliseCouleurCl;
        }
        return res_examples;
    }

    public static String[] format_definition_in_splinter(String word)
    {
        String res_def;
        String[] def_tab;
        // @TODO essayer de supprimer cpt_entry
        int cpt_entry = 0;
        int cpt_dispayed_sense = 0;
        Map<Integer, String> numentry_pos = dict_manager.getEntry(word);
        if (numentry_pos.size() == 0) {numentry_pos.put(0, "unknown part of speech");}
        def_tab = new String[numentry_pos.size()];
        for (Map.Entry<Integer, String> current_entry : numentry_pos.entrySet())
        {
            res_def = current_entry.getValue();
            res_def += dict_manager.getLemma_html_block(word, current_entry.getKey());
            ArrayList<String> senseid_list = dict_manager.getSenseId(word, current_entry.getKey());
            for (int cpt_senseid = 0; cpt_senseid < senseid_list.size(); cpt_senseid++)
            {
                res_def += "<br/>";
                String sense_string;
                Map<String, String> def_lgg = dict_manager.getDefinition(senseid_list.get(cpt_senseid));
                if(def_lgg!=null && !def_lgg.isEmpty()) {//peut être nul si ne contient que des langues non voulues
                    System.err.println("HIDDEN : def_lgg !=null && !Empty");
                    sense_string = create_def_string(def_lgg, senseid_list, cpt_dispayed_sense++);
                    res_def += format_sense(sense_string);
                }
            }
            def_tab[cpt_entry] = res_def + (cpt_entry == numentry_pos.size() - 1?"":"<br/>");
            cpt_entry++;
        }
        return def_tab;
    }


    public static String[] format_examples_in_splinter(String word)//fait en speed pour récupérer les exemples à part des sens, cf format_definition_in_splinter
    {
        String ex_def;
        String[] ex_tab;
        // @TODO essayer de supprimer cpt_entry
        int cpt_entry = 0;
        Map<Integer, String> numentry_pos = dict_manager.getEntry(word);
        if (numentry_pos.isEmpty()) {numentry_pos.put(0, "unknown part of speech");}
        ex_tab = new String[numentry_pos.size()];
        for (Map.Entry<Integer, String> current_entry : numentry_pos.entrySet())
        {
            ex_def = "";
            ArrayList<String> senseid_list = dict_manager.getSenseId(word, current_entry.getKey());
            for (int cpt_senseid = 0; cpt_senseid < senseid_list.size(); cpt_senseid++)
            {
                Map<String, String> def_lgg = dict_manager.getDefinition(senseid_list.get(cpt_senseid));
                if(def_lgg!=null && !def_lgg.isEmpty()) {//peut être nul si ne contient que des langues non voulues
                    System.err.println("HIDDEN : def_lgg !=null && !Empty");
                    //display contexts...
                    Map<Integer, ArrayList<String[]>> cpt__sn_wf_l = dict_manager.getContext_cpt__sn_wf_l(senseid_list.get(cpt_senseid));
                    ex_def += create_context_string(cpt__sn_wf_l);
                    if(!create_context_string(cpt__sn_wf_l).isEmpty()){ex_def += "<br/>";}
                }
            }
            ex_tab[cpt_entry] = ex_def ;
            cpt_entry++;
        }
        return ex_tab;//TODO : on retourne que les exemples, donc le reste est a retirer
    }

    private static String create_def_string(Map<String, String> def_lgg, ArrayList<String> sense_id_list, int cpt_displayed_sense) {
        String sense_string;
        String sense_stringFR = ""; // CC 13/07
        String sense_stringEN = "";
        if(def_lgg == null || def_lgg.isEmpty()){ //peut être nul si ne contient que des langues non voulues
            return "";
        }

        if(sense_id_list.size() == 1){
            sense_string =  " •&nbsp;"; } else {
            sense_string =  " ("+(cpt_displayed_sense)+ ") ";
        }
        for (Map.Entry<String, String> entry : def_lgg.entrySet()){
            if(entry.getKey() == null || entry.getValue() == null){
                System.err.println("pb avec " + (entry.getKey()!=null?entry.getKey():"unknown entry") + " : sens (ou langue) du sens nul");
                continue;
            }
            String colorBaliseOp = "<font color =\"black\">";
            String colorBaliseCl = "</font>";
            if(entry.getKey().equals("fra")){
                colorBaliseOp = "<font color = \"" + word_descriptionActivity.getOtherExampleColorFR() + "\">";
                sense_stringFR+= colorBaliseOp + entry.getValue() + colorBaliseCl;
            }
            else if(entry.getKey().equals("eng")){
                colorBaliseOp = "<font color = \""+word_descriptionActivity.getOtherExampleColorEN()+"\">";
                sense_stringEN+= colorBaliseOp+entry.getValue()+colorBaliseCl;
            }
        }
        sense_string+= sense_stringFR + " " + sense_stringEN;
        return sense_string;
    }

    private static String create_context_string(Map<Integer, ArrayList<String[]>> cpt__sn_wf_l) {
        String res_context = "";
        String font_color = "#555555";
        String style = "";
        String end_style="";
        for (Map.Entry<Integer, ArrayList<String[]>> context_entry : cpt__sn_wf_l.entrySet()) {
            for (int cpt_tr = 0; cpt_tr < context_entry.getValue().size(); cpt_tr++) {
                // @TODO faire un vrai truc pour "comment" et co.
                if (context_entry.getValue().get(cpt_tr)[0] == null){
                    continue;
                }
                MyApplication.Log(">>-->>-- langue : " + context_entry.getValue().get(cpt_tr)[2] + " . languages_list.get(0) = " + languages_list.get(0) + " . languages_list.get(1) = " + languages_list.get(1));
                // @TODO : faire autrement
                if (context_entry.getValue().get(cpt_tr)[2] != null) {
                    if (context_entry.getValue().get(cpt_tr)[2].equals(languages_list.get(0))) {
                        String colorHex =  word_descriptionActivity.getLocalExampleColor();
                        font_color = colorHex;
                        if(!word_descriptionActivity.getLocalExampleStyle().isEmpty()) {//on ajoute les balises de style définies dans le fichier de style (pour l'affichage des notices)
                            style = "<" + word_descriptionActivity.getLocalExampleStyle() + ">";
                            end_style = "</" + word_descriptionActivity.getLocalExampleStyle() + ">";
                        }
                        else{
                            style =""; end_style ="";
                        }
                    } else if (context_entry.getValue().get(cpt_tr)[2].equals(languages_list.get(1))) {
                        String colorHex =  word_descriptionActivity.getOtherExampleColorFR();
                        font_color = colorHex;
                        if(!word_descriptionActivity.getOtherExampleStyle().isEmpty()) {
                            style = "<" + word_descriptionActivity.getOtherExampleStyle() + ">";
                            end_style = "</" + word_descriptionActivity.getOtherExampleStyle() + ">";
                        }
                        else{
                            style =""; end_style ="";
                        }
                    } else if (languages_list.size() > 2 && context_entry.getValue().get(cpt_tr)[2].equals(languages_list.get(2))) {
                        String colorHex = word_descriptionActivity.getOtherExampleColorEN();
                        font_color = colorHex;
                        if(!word_descriptionActivity.getOtherExampleStyle().isEmpty()) {
                            style = "<" + word_descriptionActivity.getOtherExampleStyle() + ">";
                            end_style = "</" + word_descriptionActivity.getOtherExampleStyle() + ">";
                        }
                        else{
                            style =""; end_style ="";
                        }
                    } else {
                        font_color = "#555555";
                    }
                }
                if (regex4note.length() == 0 && format_sense(context_entry.getValue().get(cpt_tr)[0]).length()>0) {//on concatène les balises de style créées avec les résultats à retourner
                    res_context += style+"<font color=\"" + font_color + "\">"+ format_sense(context_entry.getValue().get(cpt_tr)[0]) + "</font>"+end_style+"<br/>";
                }
                else if(format_sense(deletePhoneticNotes(context_entry.getValue().get(cpt_tr)[0])).length()>0) {
                    res_context += style+"<font color=\"" + font_color + "\">"+ format_sense(deletePhoneticNotes(context_entry.getValue().get(cpt_tr)[0])) + "</font>"+end_style+"<br/>";
                }
                if (context_entry.getValue().get(cpt_tr)[1] == null || context_entry.getValue().get(cpt_tr)[2] == null) {
                    System.err.println("pb avec " + context_entry.getKey() + " : texte (ou langue) du sens nul");
                }
            }
            if(res_context.length()>0) {
                res_context += "<br/>";
            }
        }
        return res_context;
    }

    public static ArrayList<String> getSubEntryList(String word) {
        return dict_manager.getSubEntry(word);
    }

    private static String getPdlPdvText(String word)
    {
        String pdl_pdv_text = "";
        ArrayList<String[]> pdl_pdv_list = dict_manager.getPdlPdv(word);
        System.err.println("getPdlPdvText : pour " + word + " pdl_pdv_list.size() = " + pdl_pdv_list.size());
        for (String[] pdl_pdv : pdl_pdv_list)
        {
            System.err.println("getPdlPdvText : pour " + word + " pdl_pdv.getKey() = " + pdl_pdv[0] + " et pdl_pdv.getValue() = " + pdl_pdv[1]);
            pdl_pdv_text += "<br/>" + pdl_pdv[0] + " : " + pdl_pdv[1];
        }
        return pdl_pdv_text;
    }

    // @TODO check if this functions (with only a return line) are useful
    public static String getGrammarFromSQL(String word)
    {
        return dict_manager.getGrammar(word);
    }

    public static ArrayList<ArrayList<AudioRepresentation>> getAudioRepresentationList(String word)
    {
        return dict_manager.getAudioRepresentationList(word);
    }

    public static ArrayList<ArrayList<Illustration>> getIllustrationList(String word)
    {
        return dict_manager.getIllustrationList(word);
    }

    private static String deletePhoneticNotes(String in)
    {
        Pattern p= Pattern.compile(dictData.regex4note);
        Matcher m=p.matcher(in);
        String res = m.replaceAll("<b><font color=\"\\#000000\">$1<\\/font><\\/b>");
        MyApplication.Log("deletePhoneticNotes : SenseString res = " + res);
        return res;
    }

    public static String getWord4lexeme(String lexeme)
    {
        for (int i = 0; i < lex_id_cf_gloss[0].length; i++)
        {
            if (lex_id_cf_gloss[0][i].equals(lexeme))
            {
                return lex_id_cf_gloss[1][i];
            }
        }
        // @TODO send an error
        return "";
    }

    // @TODO supprimer la bidouille ArrayList<String> citation_form_words qui est aussi dans oneWord4citationForm
    public static boolean citationFormExists(String citation_form) {
        ArrayList<String> citation_form_words = new ArrayList<>(Arrays.asList(lex_id_cf_gloss[2]));
        return citation_form_words.contains(citation_form);
    }

    public static boolean oneWord4asciiString(String ascii_word) {
        return words_ascii_version.indexOf(ascii_word) == words_ascii_version.lastIndexOf(ascii_word);
    }

    public static boolean oneWord4citationForm(String citation_form) {
        // true si une seule occurrence
        ArrayList<String> citation_form_words = new ArrayList<>(Arrays.asList(lex_id_cf_gloss[2]));
        // @TODO faire mieux que ca
        // c'est au cas ou on utilise lex plutot que citation_form (et c'est un cas tres répandu)
        if(!citation_form_words.contains(citation_form) && citation_form_words.lastIndexOf(citation_form) == -1)
        {
            ArrayList<String> lex_words = new ArrayList<>(Arrays.asList(lex_id_cf_gloss[0]));
            return lex_words.indexOf(citation_form) == lex_words.lastIndexOf(citation_form);
        }
        return citation_form_words.indexOf(citation_form) == citation_form_words.lastIndexOf(citation_form);
    }

    public static String getWord4AsciiVersion(String ascii_word)
    {
        int index = words_ascii_version.indexOf(ascii_word);
        if(index == -1)
            return "";
        return lex_id_cf_gloss[1][id_index4ascii_words.get(index)];
    }


    public static String getWord4citationForm(String citation_form)
    {
        int index = -1;
        for(int i = 0; i < lex_id_cf_gloss[2].length; i++) {
            if (citation_form.equals(lex_id_cf_gloss[2][i])) {
                index = i;
            }
        }
        return (index != -1?lex_id_cf_gloss[1][index]:"");
    }

    public static int getHmNumber(String id){
        return dict_manager.getHmNumber(id);
    }

    public static ArrayList<String> getWords4asciiWord(String ascii_word)
    {
        ArrayList<String> res = new ArrayList<>();
        int index, new_index;
        index = words_ascii_version.indexOf(ascii_word);
        while (index != -1)
        {
            res.add(lex_id_cf_gloss[2][id_index4ascii_words.get(index)]);
            index++;
            new_index = words_ascii_version.subList(index, words_ascii_version.size()/* - index*/).indexOf(ascii_word);
            if (new_index != -1)
                index += new_index;
            else
                index = new_index;
        }
        Set<String> set = new HashSet<>() ;
        set.addAll(res) ;
        return new ArrayList<>(set);
    }

    public static String getLexeme4word(String word_id)
    {
        for (int i = 0; i < lex_id_cf_gloss[1].length; i++)
        {
            if (lex_id_cf_gloss[1][i].equals(word_id))
            {
                return lex_id_cf_gloss[0][i];
            }
        }
        // @TODO error management
        return "";
    }


    public static String getLocalForm4word(String word_id) {
        // renvoie la forme (lx) ou le cf
        //System.err.println(">>> searchId : /"+ word_id + "/");
        for (int i = 0; i < lex_id_cf_gloss[1].length; i++) {
            if (lex_id_cf_gloss[1][i].equals(word_id)){
                System.err.println(">>> searchForm : /"+ lex_id_cf_gloss[2][i] + "/");
                return !lex_id_cf_gloss[2][i].isEmpty() ?lex_id_cf_gloss[2][i]:lex_id_cf_gloss[0][i];
            }
        }
        // @TODO error management
        return "";
    }

    public static String[] getLocalLetters()
    {
        return dict_manager.getSortedInitialLettersTab();
    }

    public static ArrayList<String> getComplementLetters()
    {
        ArrayList<String> res = dict_manager.getComplementLettersList();
        // @TODO faire ça autrement, ailleurs
        final List<String> ascii_letters = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        res.removeAll(ascii_letters);
        return res;
    }

    public static String[] getLocalLetterList()
    {
        ArrayList<String> keyArrayList = new ArrayList<>(ascii_devanagari_correspondence.keySet());
        String[] res_tab = new String[keyArrayList.indexOf(" ")];
        res_tab = keyArrayList.subList(0, keyArrayList.indexOf(" ")).toArray(res_tab);
        return res_tab;
    }

    private static ArrayList<String> getAsciiWordList()
    {
        return words_ascii_version;
    }

    public static ArrayList<String> getDistinctAsciiWordList()
    {
        Set<String> set = new LinkedHashSet<>() ;
        set.addAll(dictData.getAsciiWordList()) ;
        return new<String>ArrayList<String>(set);
    }

    public static ArrayList<String> getCitationForms()
    {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(lex_id_cf_gloss[2]));
        return new<String>ArrayList<String>(list);
    }

    public static ArrayList<String> getLexemes()
    {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(lex_id_cf_gloss[0]));
        return new<String>ArrayList<String>(list);
    }

    public static String getDictInfo() {
        String res = "";
        ArrayList<String> glob_inf;
        glob_inf = dict_manager.getDictionaryInformation();
        for (int i = 0; i < glob_inf.size(); i++)
        {
            res += glob_inf.get(i) + "\n";
        }
        return res;
    }

    //new
    public static String[][] getCitationFormAndIDList(String letter) {
        ArrayList<String> words4letter = new ArrayList<>();
        ArrayList<String> id4letter = new ArrayList<>();
        ArrayList<String> defFR4letter = new ArrayList<>();
        ArrayList<String> defEN4letter = new ArrayList<>();
        ArrayList<String> partOfspeech = new ArrayList<>();
        char[] first_letter = new char[letter.length()];
        if(letter.length() == 1 && letter.charAt(0) >= 'a' && letter.charAt(0) <= 'z') {
            for (int i = 0; i < lex_id_cf_gloss[2].length; i++) {
                if (lex_id_cf_gloss[2][i].isEmpty())
                    continue;
                if (letter.charAt(0) == lex_id_cf_gloss[2][i].charAt(0)
                        || letter.toUpperCase().charAt(0) == lex_id_cf_gloss[2][i].charAt(0)) {
                    words4letter.add(lex_id_cf_gloss[2][i]);
                    id4letter.add(lex_id_cf_gloss[1][i]);
                    defFR4letter.add(lex_id_cf_gloss[3][i]);
                    defEN4letter.add(lex_id_cf_gloss[4][i]);
                    partOfspeech.add(lex_id_cf_gloss[5][i]);
                }
            }
        }
        else {          //multigraphes
            for (int i = 0; i < lex_id_cf_gloss[2].length; i++) {
                if (lex_id_cf_gloss[2][i].length() < letter.length())
                    continue;
                lex_id_cf_gloss[2][i].getChars(0, letter.length(), first_letter, 0);
                if (letter.equals(new String(first_letter))) {
                    words4letter.add(lex_id_cf_gloss[2][i]);
                    id4letter.add(lex_id_cf_gloss[1][i]);
                    defFR4letter.add(lex_id_cf_gloss[3][i]);
                    defEN4letter.add(lex_id_cf_gloss[4][i]);
                    partOfspeech.add(lex_id_cf_gloss[5][i]);
                }
            }
        }

        // @TODO un petit test que les donnees sont pas trop pourries et les tableaux de meme taille
        String[][] res_tab = new String[5][words4letter.size()];
        res_tab[0] = new String[words4letter.size()];
        res_tab[0] = words4letter.toArray(res_tab[0]);
        res_tab[1] = new String[id4letter.size()];
        res_tab[1] = id4letter.toArray(res_tab[1]);
        res_tab[2] = new String[defFR4letter.size()];
        res_tab[2] = defFR4letter.toArray(res_tab[2]);
        res_tab[3] = new String[defEN4letter.size()];
        res_tab[3] = defEN4letter.toArray(res_tab[3]);
        res_tab[4] = new String[partOfspeech.size()];
        res_tab[4] = partOfspeech.toArray(res_tab[4]);

        return res_tab;
    }

    public static String[][] getLexemeAndIDList(String letter) {
        // @TODO to review
        ArrayList<String> words4letter = new ArrayList<>();
        ArrayList<String> id4letter = new ArrayList<>();
        ArrayList<String> partOfspeech = new ArrayList<>();
        ArrayList<String> defFR4letter = new ArrayList<>();
        ArrayList<String> defEN4letter = new ArrayList<>();
        char[] first_letter = new char[letter.length()];

        if(letter.length() == 1 && letter.charAt(0) >= 'a' && letter.charAt(0) <= 'z') {
            for (int i = 0; i < lex_id_cf_gloss[0].length; i++) {
                if (lex_id_cf_gloss[0][i].length() == 0)
                    continue;
                if (letter.charAt(0) == lex_id_cf_gloss[0][i].charAt(0)
                        || letter.toUpperCase().charAt(0) == lex_id_cf_gloss[0][i].charAt(0)) {
                    words4letter.add(lex_id_cf_gloss[0][i]);
                    id4letter.add(lex_id_cf_gloss[1][i]);
                    defFR4letter.add(lex_id_cf_gloss[3][i]);
                    defEN4letter.add(lex_id_cf_gloss[4][i]);
                    partOfspeech.add(lex_id_cf_gloss[5][i]);
                }
            }
        }
        else {
            for (int i = 0; i < lex_id_cf_gloss[0].length; i++) {
                if (lex_id_cf_gloss[0][i].length() < letter.length())
                    continue;
                lex_id_cf_gloss[0][i].getChars(0, letter.length(), first_letter, 0);
                if (letter.equals(new String(first_letter))) {
                    words4letter.add(lex_id_cf_gloss[0][i]);
                    id4letter.add(lex_id_cf_gloss[1][i]);
                    defFR4letter.add(lex_id_cf_gloss[3][i]);
                    defEN4letter.add(lex_id_cf_gloss[4][i]);
                    partOfspeech.add(lex_id_cf_gloss[5][i]);
                }
            }
        }
        // @TODO un petit test que les donnees sont pas trop pourries et les tableaux de meme taille
        String[][] res_tab = new String[5][words4letter.size()];
        res_tab[0] = new String[words4letter.size()];
        res_tab[0] = words4letter.toArray(res_tab[0]);
        res_tab[1] = new String[id4letter.size()];
        res_tab[1] = id4letter.toArray(res_tab[1]);
        res_tab[2] = new String[defFR4letter.size()];
        res_tab[2] = defFR4letter.toArray(res_tab[2]);
        res_tab[3] = new String[defEN4letter.size()];
        res_tab[3] = defEN4letter.toArray(res_tab[3]);
        res_tab[4] = new String[partOfspeech.size()];
        res_tab[4] = partOfspeech.toArray(res_tab[4]);
        //Arrays.sort(res_tab[0]);
        // l'affichage des items (entrées + s/entrées) est bien dans l'ordre, mais le texte ne correspond pas.

        /*
        Arrays.sort(res_tab, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                String itemIdOne = o1[0];
                String itemIdTwo = o2[0];
                return itemIdOne.compareTo(itemIdTwo);
            }
        });*/
        return res_tab;
    }


    //new
    public static String[][] getCitationFormAndIDList(ArrayList<String> words)
    {
        // @TODO to review
        ArrayList<String> words4letter = new ArrayList<>();
        ArrayList<String> id4letter = new ArrayList<>();
        ArrayList<String> defFR4letter = new ArrayList<>();
        ArrayList<String> defEN4letter = new ArrayList<>();
        ArrayList<String> partOfspeech = new ArrayList<>();
        for (int j = 0; j < words.size(); j++) {
            for (int i = 0; i < lex_id_cf_gloss[2].length; i++) {
                    if (lex_id_cf_gloss[2][i].equals(words.get(j))) {
                        words4letter.add(lex_id_cf_gloss[2][i]);
                        id4letter.add(lex_id_cf_gloss[1][i]);
                        defFR4letter.add(lex_id_cf_gloss[3][i]);
                        defEN4letter.add(lex_id_cf_gloss[4][i]);
                        partOfspeech.add(lex_id_cf_gloss[5][i]);
                    }
                }
        }
        // @TODO un petit test que les donnees sont pas trop pourries et les tableaux de meme taille
        String[][] res_tab = new String[5][words4letter.size()];
        res_tab[0] = new String[words4letter.size()];
        res_tab[0] = words4letter.toArray(res_tab[0]);
        res_tab[1] = new String[id4letter.size()];
        res_tab[1] = id4letter.toArray(res_tab[1]);
        res_tab[2] = new String[defFR4letter.size()];
        res_tab[2] = defFR4letter.toArray(res_tab[2]);
        res_tab[3] = new String[defEN4letter.size()];
        res_tab[3] = defEN4letter.toArray(res_tab[3]);
        res_tab[4] = new String[partOfspeech.size()];
        res_tab[4] = partOfspeech.toArray(res_tab[4]);

        return res_tab;
    }

    // 02/12/2017
    public static String[][] getCitationFormAndIDListForThesaurus(String thesaurus)
    {
        return dict_manager.getLexAndID4Thesaurus(thesaurus);
    }

    public static String[][] getLexAndIDList(ArrayList<String> words)
    {
        // @TODO to review
        ArrayList<String> words4letter = new ArrayList<>();
        ArrayList<String> id4letter = new ArrayList<>();
        ArrayList<String> defFR4letter = new ArrayList<>();
        ArrayList<String> defEN4letter = new ArrayList<>();

        for (int j = 0; j < words.size(); j++)
        {
            for (int i = 0; i < lex_id_cf_gloss[0].length; i++) {
                if (lex_id_cf_gloss[0][i].equals(words.get(j))) {
                    words4letter.add(lex_id_cf_gloss[0][i]);
                    id4letter.add(lex_id_cf_gloss[1][i]);
                    defFR4letter.add(lex_id_cf_gloss[3][i]);
                    defEN4letter.add(lex_id_cf_gloss[4][i]);
                }
            }
        }

        // @TODO un petit test que les donnees sont pas trop pourries et les tableaux de meme taille
        String[][] res_tab = new String[4][words4letter.size()];
        res_tab[0] = new String[words4letter.size()];
        res_tab[0] = words4letter.toArray(res_tab[0]);
        res_tab[1] = new String[id4letter.size()];
        res_tab[1] = id4letter.toArray(res_tab[1]);
        res_tab[2] = new String[defFR4letter.size()];
        res_tab[2] = defFR4letter.toArray(res_tab[2]);
        res_tab[3] = new String[defEN4letter.size()];
        res_tab[3] = defEN4letter.toArray(res_tab[3]);

        return res_tab;
    }

    // @TODO add this function if necessary

    public static String[][] getWordList4searchFromSQL(String searched_string, String search_type, String checkbox_state, String language, ArrayList<String> chosenCAT, ArrayList<String> chosenTH) {
        System.out.println("language : "+ language);
        return dict_manager.getMatchingWords(searched_string, search_type, checkbox_state.equals("true"), language, chosenCAT,chosenTH);
    }

    public static String getSoundFileNormalizedName(String original_file_name) {
        int num_line = dict_manager.getSoundFileRowId(original_file_name);
        if (num_line == 0) System.err.println("WARNING : normalized version of " + original_file_name + " not found!");
        return "a" + num_line;
    }

    public static String getPreviousWord(String word) {
        for (int i = 0; i < lex_id_cf_gloss[1].length; i++) {
            if (lex_id_cf_gloss[1][i].equals(word)) {
                String id = lex_id_cf_gloss[6][i];
                if(! id.equals(word)){
                    return id;
                } else {
                    int Id = Integer.parseInt(id) - 1;
                    return Integer.toString(Id);
                }
            }
        }
        return "";
    }

    public static String getNextWord(String word) {
        for (int i = 0; i < lex_id_cf_gloss[1].length; i++) {
            if (lex_id_cf_gloss[1][i].equals(word)) {
                String id = lex_id_cf_gloss[7][i];
                if (!id.equals(word)) {
                    return id;
                } else {
                    int Id = Integer.parseInt(id) + 1;
                    return Integer.toString(Id);
                }
            }
        }
        return "";
    }


    public static String findMainEntry(String word) {
        String main_entry = dict_manager.getMainEntry(word);
        return main_entry.isEmpty() ? word:main_entry;  // CC plus nécessaire cf getMainEntry
    }

    public static ArrayList<String> getPartOfSpeechChoices(){
        return dict_manager.getPartOfSpeech();
    }

    public static ArrayList<String> getThesaurusChoices(){
        return dict_manager.getThesaurus();
    }

    public static String getUsage(String sense_id, String lang){
        String usage = dict_manager.getUsage(sense_id, lang);
        boolean[] hiddenLanguages = getHidden_languages4gloss();
        if(usage.length()==0){
            return "";
        }
        if(lang.equals("fra")&&!hiddenLanguages[0]){//on ajoute la couleur adaptée (définie dans le fichier de style) en fonction de la langue
            usage = "<font color=\""+ word_descriptionActivity.getOtherExampleColorFR()+"\">" + usage + "</font>";
            return usage;
        }
        else if(lang.equals("eng")&&!hiddenLanguages[1]){
            usage = "<font color=\""+ word_descriptionActivity.getOtherExampleColorEN()+"\">" + usage + "</font>";
            return usage;
        }
        else if(!lang.equals("eng")&&!lang.equals("fra")){
            usage = "<font color=\""+ word_descriptionActivity.getLocalExampleColor()+"\">" + usage + "</font>";
            return usage;
        }
        else return "";

    }

    public static String getVariantes(String idWord) {       // CC 1810
        return dict_manager.getVariantes(idWord);
    }

    //public static ArrayList<String> getWord4Variante(String variante){
    public static String getWord4Variante(String variante){
        return dict_manager.getWord4Variante(variante);
    }

    public static ArrayList<String> getVariantes() {       // CC 2205
        return dict_manager.getVariantes();
    }
}