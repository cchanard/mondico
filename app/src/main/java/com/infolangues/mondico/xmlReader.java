package com.infolangues.mondico;

//import android.net.Uri;

/* update_database not used anymore
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//import java.io.File;
//import java.io.IOException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import android.content.Context;
//import android.graphics.Path;
//import android.os.Environment;
//import android.widget.Toast;
//import dalvik.system.PathClassLoader;
*/

/**
 * Created by bonnet on 27/08/2015.
 * Data tier
 */
/* update_database not used anymore
class xmlReader {

    private static SAXParser p = null;
    // test
    private static Context context;

    private static ArrayList<String> glob_inf; // @TODO delete


    public static LMFWord searchDefinition(String word) {
        LMFWord found_word = new LMFWord();

        context = MyApplication.getAppContext();

        try {
            InputStream dict_is;
            dict_is = context.getAssets().open("dictionary.xml");
            try {
                p.parse(dict_is, new DefSearchHandler(word, found_word));
            } catch (SAXException saxe) {
                System.err.println("SAX Error.");
                System.err.println(saxe.toString());
                System.exit(1);
            } catch (IOException ioe) {
                System.err.println("Error reading from file.");
                System.err.println(ioe.toString());
                System.exit(1);
            }
            dict_is.close();
        } catch (IOException ioe) {
            System.err.println("Error opening file.");
            System.exit(1);
        }

        return found_word;
    }


//    public static String searchDef(String word) {
//    public static String[] searchDef(String word, AudioRepresentation[] ar_tab) {


//    public static String[] searchDef(String word, ArrayList<ArrayList<AudioRepresentation>> ar_tab_list, StringBuilder gramText) {
//
//
//
//        //File dict = new File("dictionary.xml");
//        LMFWord found_word = new LMFWord();
//        String font_color = "555555";
//        String res_def;
//
//        // en cours
//        //ArrayList<AudioRepresentation> ar_list = new ArrayList<>();
//
//        context = MyApplication.getAppContext();
//
//        try {
//            InputStream dictis;
//            dictis = context.getAssets().open("dictionary.xml");
//
//            try {
//                // en cours
//                p.parse(dictis, new DefSearchHandler(word, found_word));
//            } catch (SAXException saxe) {
//                System.err.println("SAX Error.");
//                System.err.println(saxe.toString());
//                System.exit(1);
//            } catch (IOException ioe) {
//                System.err.println("Error reading from file.");
//                System.err.println(ioe.toString());
//                System.exit(1);
//            }
//
//
//            dictis.close();
//            // en cours
//            //System.out.println("ar_list.size() = " + ar_list.size());
//        } catch (IOException ioe) {
//            System.err.println("Erreur d\'ouverture du fichier.");
////            System.err.println(ioe);
//            System.exit(1);
//        }
//
//
////        try {
////            p.parse(dict, new DefSearchHandler(word, found_word));
////        } catch (SAXException saxe) {
////            System.err.println("SAX Error.");
////            System.err.println(saxe);
////            System.exit(1);
////        } catch (IOException ioe) {
////            System.err.println("Error reading from file.");
////            System.err.println(ioe);
////            System.exit(1);
////        }
//
//        String[] def_tab = new String[found_word.entry_list.size()];
//
//        //ar_tab = new AudioRepresentation[found_word.entry_list.size()];
//        //ar_tab = new ArrayList<>();
//
//        String line_head;
//
//        for (int cpt_entry = 0; cpt_entry < found_word.entry_list.size(); cpt_entry++)
//        {
//
//            for (int cpt_gramfeat = 0; cpt_gramfeat < found_word.entry_list.get(cpt_entry).gramfeat_list.size(); cpt_gramfeat++)
//            {
//
//                for (int cpt_rad = 0; cpt_rad < found_word.entry_list.get(cpt_entry).gramfeat_list.get(cpt_gramfeat).radical_list.size(); cpt_rad++) {
//                    System.out.println("€€€€€€€€ rad : " + found_word.entry_list.get(cpt_entry).gramfeat_list.get(cpt_gramfeat).radical_list.get(cpt_rad));
//                    line_head = "<b><font color=\"#" + (cpt_rad % 2 != 0 ? "3BB9FF" : "000000") + "\">";
//                    gramText.append(line_head).append(found_word.entry_list.get(cpt_entry).gramfeat_list.get(cpt_gramfeat).radical_list.get(cpt_rad)).append("</font></b>&nbsp;");
//                }
//
//                System.out.println("€€€€€€€€ person = " + found_word.entry_list.get(cpt_entry).gramfeat_list.get(cpt_gramfeat).person +
//                        " & €€€€€€€€ nb = " + found_word.entry_list.get(cpt_entry).gramfeat_list.get(cpt_gramfeat).nb);
//                //gramText.append(found_word.entry_list.get(cpt_entry).gramfeat_list.get(cpt_gramfeat).person + "&nbsp;" + found_word.entry_list.get(cpt_entry).gramfeat_list.get(cpt_gramfeat).nb);
//                gramText.append(found_word.entry_list.get(cpt_entry).gramfeat_list.get(cpt_gramfeat).person).append("&nbsp;").append(found_word.entry_list.get(cpt_entry).gramfeat_list.get(cpt_gramfeat).nb);
//                gramText.append("<br/>");
//            }
//
//
//            res_def = found_word.entry_list.get(cpt_entry).partofspeech;
//
//            for (int cpt_lemma = 0; cpt_lemma < found_word.entry_list.get(cpt_entry).lemma_list.size(); cpt_lemma++)
//            {
//                res_def += "<br/>"; // la ou ailleurs
//                // rmq : found_word.entry_list.get(cpt_entry).audio_representation.fileName
//                for (int cpt_fr = 0; cpt_fr < found_word.entry_list.get(cpt_entry).lemma_list.get(cpt_lemma).size(); cpt_fr++)
//                {
//                    res_def += "<b>["+found_word.entry_list.get(cpt_entry).lemma_list.get(cpt_lemma).get(cpt_fr).citationForm+"]</b>"+(cpt_fr==found_word.entry_list.get(cpt_entry).lemma_list.get(cpt_lemma).size()-1?"":"&nbsp;&nbsp;");
//                }
//            }
//
//
//            for (int cpt_sense = 0; cpt_sense < found_word.entry_list.get(cpt_entry).sense_list.size(); cpt_sense++)
//            {
//                for (Map.Entry<String, String> entry : found_word.entry_list.get(cpt_entry).sense_list.get(cpt_sense).definition_list.entrySet())
//                {
//                    res_def += "<br/>" + entry.getValue();
//                }
//
//                //on affiche maintenant les contexts...
//                for (int cpt_context = 0; cpt_context < found_word.entry_list.get(cpt_entry).sense_list.get(cpt_sense).context_list.size(); cpt_context++)
//                {
//                    res_def += "<br/>";
//                    for (int cpt_tr = 0; cpt_tr < found_word.entry_list.get(cpt_entry).sense_list.get(cpt_sense).context_list.get(cpt_context).size(); cpt_tr++)
//                    {
//                        if (found_word.entry_list.get(cpt_entry).sense_list.get(cpt_sense).context_list.get(cpt_context).get(cpt_tr).Language != null)
//                        {
//                            switch (found_word.entry_list.get(cpt_entry).sense_list.get(cpt_sense).context_list.get(cpt_context).get(cpt_tr).Language)
//                            {
//                                case "klr":
//                                    if (found_word.entry_list.get(cpt_entry).sense_list.get(cpt_sense).context_list.get(cpt_context).get(cpt_tr).scriptName != null) {
//                                        if (found_word.entry_list.get(cpt_entry).sense_list.get(cpt_sense).context_list.get(cpt_context).get(cpt_tr).scriptName.equals("ipa"))
//                                            font_color = "999966";
//                                        if (found_word.entry_list.get(cpt_entry).sense_list.get(cpt_sense).context_list.get(cpt_context).get(cpt_tr).scriptName.equals("devanagari"))
//                                            font_color = "993366";
//                                    } else
//                                        font_color = "555555";
//                                    break;
//                                case "eng":
//                                    font_color = "999966";
//                                    break;
//                                default:
//                                    font_color = "555555";
//                            }
//                        }
//                        res_def += "<br/><font color=\"#" + font_color + "\">" + found_word.entry_list.get(cpt_entry).sense_list.get(cpt_sense).context_list.get(cpt_context).get(cpt_tr).writtenForm + "</font>";
//                    }
//                }
//            }
//
//            def_tab[cpt_entry] = res_def + (cpt_entry == found_word.entry_list.size() - 1?"":"<br/><br/>");
//            //ar_tab[cpt_entry] = found_word.entry_list.get(cpt_entry).audio_representation;
//            // en cours suppression >> ar_tab.add(cpt_entry, found_word.entry_list.get(cpt_entry).audio_representation);
//            ar_tab_list.add(cpt_entry, found_word.entry_list.get(cpt_entry).audio_representation_list);
//        }
//        //return res_def;
//        return def_tab;
//    }






    public static String[] getLetters(ArrayList<Letter> formated_letter_list) {
        ArrayList<String> letter_list = new ArrayList<>();
        //ArrayList<Letter> formated_letter_list = new ArrayList<>();
        context = MyApplication.getAppContext();

        try {
            InputStream sois;
            sois = context.getAssets().open("sort_order.xml");

            try {
                // 08/10/2016
                //formated_letter_list = new ArrayList<>();

                p.parse(sois, new SortOrderHandler(letter_list, formated_letter_list));
                System.err.println(" %%%% %%%% %%%% %%%% %%%% %%%% %%%% %%%% ICI formated_letter_list.size() = " + formated_letter_list.size());
            } catch (SAXException saxe) {
                System.err.println("SAX Error.");
                System.err.println(saxe.toString());
                System.exit(1);
            } catch (IOException ioe) {
                System.err.println("Error reading file.");
                System.err.println(ioe.toString());
                System.exit(1);
            }

            sois.close();
        } catch (IOException ioe) {
            System.err.println("Error opening file.");
            System.exit(1);
        }


        String res_tab[] = new String[letter_list.size()];
        res_tab = letter_list.toArray(res_tab);

        return res_tab;
    }


    public static String[] getLetters() {

//        String filename = "";
//        File dir = new File(System.getProperty("user.dir"));
//        File[] files = dir.listFiles();
//        for (File file2 : files) {
//            filename += file2;

//            if (file2.isDirectory()) {
//                filename += file2;
//                //Toast.makeText(this, "directory", Toast.LENGTH_LONG).show();
//            } else {
//                if (file2.getName().equals(DATABASE_NAME)) {
//                    //Toast.makeText(this, "File found",Toast.LENGTH_LONG).show();
//                } else {
//                    //Toast.makeText(this, "File not found",Toast.LENGTH_LONG).show();
//                }
//            }

//        }

        //File f = new File("/Users/bonnet/javatest/sort_order.xml");


        //File f = new File(new URI("file:///android_asset/mot.txt"));
        //InputStream is = getAssets().open("mot.txt");

//        File f = new File("/Users/bonnet/javatest/sort_order.xml");

        ArrayList<String> letter_list = new ArrayList<>();

        context = MyApplication.getAppContext();

        try {
            InputStream sois;
            sois = context.getAssets().open("sort_order.xml");

            try {
                System.err.println(">>>> " + letter_list.size());
                for(int i = 0; i < letter_list.size(); i++)

                p.parse(sois, new SortOrderHandler(letter_list));
            } catch (SAXException saxe) {
                System.err.println("SAX Error.");
                System.err.println(saxe.toString());
                System.exit(1);
            } catch (IOException ioe) {
                System.err.println("Error reading file.");
                System.err.println(ioe.toString());
                System.exit(1);
            }


            sois.close();
        } catch (IOException ioe) {
            System.err.println("Error opening file.");
//            System.err.println(ioe);
            System.exit(1);
        }


        String res_tab[] = new String[letter_list.size()];
        res_tab = letter_list.toArray(res_tab);

        return res_tab; //new String[] { "?", "k", "g", "h", "L"};
    }

    public static void initXmlReader() {
        //File f = new File("sort_order.xml");
        // On crée maintenant un parseur SAX pour lire le fichier sort_order
        try {
            p = SAXParserFactory.newInstance().newSAXParser();
            // On active les namespaces, sinon, on ne récupèrera pas le nom
            // des éléments dans les méthodes start/endElement
            p.getXMLReader().setFeature("http://xml.org/sax/features/namespaces", true);
        } catch (ParserConfigurationException pce) {
            System.err.println("Impossible to create SAX parser.");
            //System.err.println(pce);
            System.exit(1);
        } catch (SAXException saxe) {
            System.err.println("SAX Error.");
            //System.err.println(saxe);
            System.exit(1);
        }

    }


    public static String[][] initDictData() {
        return initDictData(null, null);
    }


    private static String[][] initDictData(ArrayList<String> languages_list, ArrayList<String> alphabets_list) {

        //File dict = new File("dictionary.xml"); // the static variable p is used in this function

        ArrayList<String> word_list = new ArrayList<>();
        ArrayList<String> id_list = new ArrayList<>();
        //ArrayList<String> languages_list = new ArrayList<>();
        //ArrayList<String> alphabets_list = new ArrayList<>();
        if(languages_list == null) languages_list = new ArrayList<>();
        if(alphabets_list == null) alphabets_list = new ArrayList<>();

        glob_inf = new ArrayList<>();

        try {
            InputStream dicois;
            dicois = context.getAssets().open("dictionary.xml");

            try {
                p.parse(dicois, new DictHandler(word_list, id_list, glob_inf, languages_list, alphabets_list));
            } catch (SAXException saxe) {
                System.err.println("SAX Error.");
                //System.err.println(saxe);
                System.exit(1);
            } catch (IOException ioe) {
                System.err.println("Error reading file.");
                //System.err.println(ioe);
                System.exit(1);
            }

            dicois.close();
        } catch (IOException ioe) {
            System.err.println("Error opening file.");
            //System.err.println(ioe);
            System.exit(1);
        }

        //System.out.println("???????????????? word_list.get(4) = " + word_list.get(4));
        //System.out.println("???????????????? id_list.get(4) = " + id_list.get(4));

        // @TODO petites verifs sur taille des arraylists, trucs comme ca
        String res_tab1[] = new String[word_list.size()];
        res_tab1 = word_list.toArray(res_tab1);
        String res_tab2[] = new String[id_list.size()];
        res_tab2 = id_list.toArray(res_tab2);

        //String res_tab[][] = new String[2][word_list.size()];
        String res_tab[][] = new String[2][];
        res_tab[0] = new String[word_list.size()];
        res_tab[0] = res_tab1;
        res_tab[1] = new String[word_list.size()];
        res_tab[1] = res_tab2;

        //System.out.println("???????????????? res_tab[0][4] = " + res_tab[0][4]);
        //System.out.println("???????????????? res_tab[1][4] = " + res_tab[1][4]);

        return res_tab;
    }


    // refonte!!!!
    public static String[][] initDataFromDictionary(ArrayList<String> languages_list, ArrayList<String> alphabets_list, ArrayList<String> dictionary_informations) {
        //File dict = new File("dictionary.xml"); // the static variable p is used in this function

        long debut = System.currentTimeMillis();

        ArrayList<String> word_list = new ArrayList<>();
        ArrayList<String> id_list = new ArrayList<>();

        // attention ici word_list devient citation_form_list
        ArrayList<String> citation_form_list = new ArrayList<>();
        // ca c'est normal ca sert plus : ArrayList<String> id_list = new ArrayList<>();
        // attention ici def_list devient gloss_list
        ArrayList<String> gloss_list = new ArrayList<>();

        if(languages_list == null) languages_list = new ArrayList<>();
        if(alphabets_list == null) alphabets_list = new ArrayList<>();
        if(dictionary_informations == null) dictionary_informations = new ArrayList<>();

        glob_inf = new ArrayList<>();

        System.out.println("initDataFromDictionary 1 : " + (System.currentTimeMillis() - debut));

        try {
            InputStream dicois;
            dicois = context.getAssets().open("dictionary.xml");

            try {
                p.parse(dicois, new InitDictHandler(word_list, id_list, citation_form_list, gloss_list, dictionary_informations, languages_list, alphabets_list));
            } catch (SAXException saxe) {
                System.err.println("SAX Error.");
                //System.err.println(saxe);
                System.exit(1);
            } catch (IOException ioe) {
                System.err.println("Error reading file.");
                //System.err.println(ioe);
                System.exit(1);
            }

            dicois.close();
        } catch (IOException ioe) {
            System.err.println("Error opening file.");
            //System.err.println(ioe);
            System.exit(1);
        }

        System.out.println("initDataFromDictionary 2 : " + (System.currentTimeMillis() - debut));

        //System.out.println("???????????????? word_list.get(4) = " + word_list.get(4));
        //System.out.println("???????????????? id_list.get(4) = " + id_list.get(4));

        // @TODO petites verifs sur taille des arraylists, trucs comme ca
        String res_tab1[] = new String[word_list.size()];
        res_tab1 = word_list.toArray(res_tab1);
        String res_tab2[] = new String[id_list.size()];
        res_tab2 = id_list.toArray(res_tab2);


        //String res_tab[][] = new String[2][word_list.size()];
        String res_tab[][] = new String[4][];
        res_tab[0] = new String[word_list.size()];
        res_tab[0] = res_tab1;
        res_tab[1] = new String[word_list.size()];
        res_tab[1] = res_tab2;

        // citation form
        String res_tab3[] = new String[citation_form_list.size()];
        res_tab3 = citation_form_list.toArray(res_tab3);
        res_tab[2] = new String[word_list.size()];
        res_tab[2] = res_tab3;

        // gloss
        String res_tab4[] = new String[gloss_list.size()];
        res_tab4 = gloss_list.toArray(res_tab4);
        res_tab[3] = new String[word_list.size()];
        res_tab[3] = res_tab4;

        System.out.println("initDataFromDictionary 3 : " + (System.currentTimeMillis() - debut));
        System.err.println("++++ ++++ ++++ ++++ dictionary_informations.size() = " + dictionary_informations.size());

        return res_tab;
    }

    // @TODO delete this function if never used

//    public static String[][] initCitationFormData(ArrayList<String> alphabets_list)
//    {
//        ArrayList<String> word_list = new ArrayList<>();
//        ArrayList<String> id_list = new ArrayList<>();
//        ArrayList<String> def_list = new ArrayList<>();
//        glob_inf = new ArrayList<>();
//
//        try {
//            InputStream dicois;
//            dicois = context.getAssets().open("dictionary.xml");        try {
//            p.parse(dicois, new CitationFormHandler(word_list, id_list, def_list, glob_inf, alphabets_list));
//        } catch (SAXException saxe) {
//            System.err.println("SAX Error.");
//            //System.err.println(saxe);
//            System.exit(1);
//        } catch (IOException ioe) {
//            System.err.println("Error reading file.");
//            //System.err.println(ioe);
//            System.exit(1);
//        }
//            dicois.close();
//        } catch (IOException ioe) {
//            System.err.println("Error opening dictionary file.");
//            //System.err.println(ioe);
//            System.exit(1);
//        }
//        String res_tab1[] = new String[word_list.size()];
//        res_tab1 = word_list.toArray(res_tab1);
//        String res_tab2[] = new String[id_list.size()];
//        res_tab2 = id_list.toArray(res_tab2);
//        String res_tab3[] = new String[def_list.size()];
//        res_tab3 = def_list.toArray(res_tab3);
//
//        //String res_tab[][] = new String[2][word_list.size()];
//        String res_tab[][] = new String[3][];
//        res_tab[0] = new String[word_list.size()];
//        res_tab[0] = res_tab1;
//        res_tab[1] = new String[word_list.size()];
//        res_tab[1] = res_tab2;
//        res_tab[2] = new String[word_list.size()];
//        res_tab[2] = res_tab3;
//        return res_tab;
//    }

    public static String[] searchMatchingWords(String def, String search_type, Boolean contains) {
        System.err.println("- On met ici la recherche avec " + def + ", " + search_type + ", " + contains.toString());

        ArrayList<String> word_list = new ArrayList<>();
        // SAXParser p = null;  // the static variable p is used in this function


        try {
            InputStream dicois;
            dicois = context.getAssets().open("dictionary.xml");

            try {
                p.parse(dicois, new DictSearchHandler(def, search_type, contains, word_list));
            } catch (SAXException saxe) {
                System.err.println("SAX Error.");
                System.err.println(saxe.toString());
                System.exit(1);
            } catch (IOException ioe) {
                System.err.println("Error reading file.");
                System.err.println(ioe.toString());
                System.exit(1);
            }

            // 28/09
            dicois.close();
        } catch (IOException ioe) {
            System.err.println("Error opening dictionary file.");
            //System.err.println(ioe);
            System.exit(1);
        }

        String res_tab[] = new String[word_list.size()];
        res_tab = word_list.toArray(res_tab);
        return res_tab;
    }


    // @todo use the language variable to select
    public static String[][] searchMatchingWords(String def, String search_type, Boolean contains, String language) {
        System.err.println("On met ici la recherche avec " + def + ", " + search_type + ", " + contains.toString());

        ArrayList<String> word_list = new ArrayList<>();
        ArrayList<String> word_definition_list = new ArrayList<>();
        // SAXParser p = null;  // the static variable p is used in this function


        try {
            InputStream dict_is;
            dict_is = context.getAssets().open("dictionary.xml");

            try {
                p.parse(dict_is, new DictSearchHandler(def, search_type, contains, word_list, word_definition_list, language));
            } catch (SAXException saxe) {
                System.err.println("SAX Error.");
                System.err.println(saxe.toString());
                System.exit(1);
            } catch (IOException ioe) {
                System.err.println("Error reading file.");
                System.err.println(ioe.toString());
                System.exit(1);
            }

            // 28/09
            dict_is.close();
        } catch (IOException ioe) {
            System.err.println("Error while opening dictionary.xml file.");
            //System.err.println(ioe);
            System.exit(1);
        }

        //
        if (word_definition_list.size() != word_list.size())
            System.err.println("[ERR] word_definition_list.size() != word_list.size()");

        String res_tab[][] = new String[2][];
        res_tab[0] = new String[word_list.size()];
        res_tab[0] = word_list.toArray(res_tab[0]);
        res_tab[1] = new String[word_definition_list.size()];
        res_tab[1] = word_definition_list.toArray(res_tab[1]);

        return res_tab;
    }

    // NWEW
    public static Map<String,String> initAsciiDevanagariCorrespondence()
    {
        //Map<String,String> ascii_devanagari_list = new HashMap<String,String>();
        Map<String,String> ascii_devanagari_list = new LinkedHashMap<>();

        context = MyApplication.getAppContext();

        try {
            InputStream corresfile;
            corresfile = context.getAssets().open("ascii_correspondence.xml");

            try {
                p.parse(corresfile, new AsciiDevanagariCorrespondenceHandler(ascii_devanagari_list));

            } catch (SAXException saxe) {
                System.err.println("SAX Error.");
                //System.err.println(saxe);
                System.exit(1);
            } catch (IOException ioe) {
                System.err.println("Error reading from correspondence file.");
                //System.err.println(ioe);

                System.exit(1);
            }

            corresfile.close();
        } catch (IOException ioe) {
            System.err.println("Error opening correspondence file.");
            //System.err.println(ioe);

            // 11/08/2016 :
            return ascii_devanagari_list;
            // 11/08/2016 >> System.exit(1);
        }

        return ascii_devanagari_list;
    }

}


class SortOrderHandler extends DefaultHandler {

    private ArrayList<String> letter_list;
    private ArrayList<Letter> formatted_letter_list = new ArrayList<>();
    private Letter current_letter = new Letter();

    public SortOrderHandler(ArrayList<String> letter_list)
    {
        this.letter_list = letter_list;
    }

    public SortOrderHandler(ArrayList<String> letter_list, ArrayList<Letter> formatted_letter_list)
    {
        this.letter_list = letter_list;
        this.formatted_letter_list = formatted_letter_list;
    }


    // Appelée avant toutes les autres méthodes, au début du document.
    public void startDocument() {
        //System.out.println("--Début du document--");
    }

    // Appelée après toutes les autres méthodes, à la fin du document.
    public void endDocument() {
//        System.out.println();
//        System.out.println("--Fin du document--");
    }


//     * Appelée à chaque fois qu'un élément est rencontré.
//     *  - uri est l'URI de l'espace de nom auquel appartient l'élément
//     *  - localName est le nom de l'élément (sans préfixe d'espace de nom)
//     *  - qName est le nom qualifié de l'élément (avec préfixe d'espace de nom)
//     *  - attributes est la liste des attributs de l'élément

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) {
        //System.out.print("<" + localName);
        for (int i = 0; i < attributes.getLength(); i++) {
            //System.out.print(" " + attributes.getLocalName(i)
            //                     + "=\"" + "xxx" + attributes.getValue(i) + "xxx" + "\"");
            if (attributes.getLocalName(i).equals("str"))
            {
                //System.out.println(attributes.getValue(i));
                letter_list.add(attributes.getValue(i));

                // 08/10/2016
                current_letter.str = attributes.getValue(i);
            }
            // 08/10/2016
            if (attributes.getLocalName(i).equals("rank"))
            {
                //System.out.println(attributes.getValue(i));
                current_letter.rank = Float.parseFloat(attributes.getValue(i));
            }
        }
        //   System.out.print(">");
    }

    // Appelée à chaque fois qu'une fin d'élément est rencontrée.

    public void endElement(String uri, String localName, String qName) {
        if(localName.equals("rule"))
        {
            formatted_letter_list.add(current_letter);
            System.err.println("add letter " + current_letter.str + " at rank " + current_letter.rank);
            current_letter = new Letter();
        }
    }

    // Fournit les données trouvées à l'intérieur d'un élément.
    public void characters(char[] ch, int start, int length) {
//    System.out.print(new String(ch, start, length));
    }

    // Fournit des espaces ignorables trouvés à l'intérieur d'un élément.
    public void ignorableWhitespace(char[] ch, int start, int length) {
//      System.out.print('_');
    }
//    System.out.print(new String(ch, start, length));

}








// refonte
// TODO regler le probleme des parametres stockes dans des vbl locales
class InitDictHandler extends DefaultHandler {

    private static final StringBuilder open_parenthesis = new StringBuilder("(");

    private boolean info_on = false;
    private ArrayList<String> word_list;
    private ArrayList<String> id_list;
    private ArrayList<String> gloss_list;
    private ArrayList<String> glob_inf;
    private ArrayList<String> languages;
    private ArrayList<String> alphabets;

    private boolean in_local_alphabet = false;
    private String citation_form;
    private ArrayList<String> citation_form_list;
    private boolean in_def = false;
    private boolean in_local_def = false;
    //private String def_gloss;
    // 30/08
    private StringBuilder def_gloss;

    // 25/08/2016
    private boolean ignore = false;


    public InitDictHandler(ArrayList<String> word_list, ArrayList<String> id_list, ArrayList<String> citation_form_list, ArrayList<String> gloss_list, ArrayList<String> glob_inf, ArrayList<String> languages, ArrayList<String> alphabets)
    {
        this.word_list = word_list;
        this.id_list = id_list;
        this.gloss_list = gloss_list;
        this.citation_form_list = citation_form_list;
        this.glob_inf = glob_inf;
        this.languages = languages;
        this.alphabets = alphabets;
    }


    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) {

        // TODO ignore WordForm & Audio (and more?)
        // 25/08/2016
        if (ignore)
            return;
        if(localName.equals("WordForm") || localName.equals("Audio"))
        {
            ignore = true;
            //25 return;
        }


        if(localName.equals("LexicalEntry"))
        {
            if (attributes.getLocalName(0).equals("id")) {
                id_list.add(attributes.getValue(0));
                // @TODO separate data and display
                //def_gloss = "(";
                // 30/08
                def_gloss = new StringBuilder(open_parenthesis);
            }
            //25 return;
        }


        if(localName.equals("GlobalInformation"))
        {
            info_on = true;
            System.err.println(" ++++ #### ++++ #### GlobalInformation IN ++++ #### ++++ #### ");

            for (int i = 0; i < attributes.getLength(); i++) {
                //System.out.print(" " + attributes.getLocalName(i)
                //                     + "=\"" + "xxx" + attributes.getValue(i) + "xxx" + "\"");
                System.err.println(" ++++ #### ++++ #### GlobalInformation IN 2 ++++ #### ++++ #### ");

                if (info_on && attributes.getLocalName(i).equals("att"))
                {
                    System.err.println(" ++++ #### ++++ #### GlobalInformation IN 4 ++++ #### ++++ #### ");
                    String attribute_name;
                    attribute_name = attributes.getValue(i);
                    //attribute_name.replaceFirst()
                    //attribute_name.toUpperCase(0);  @TODO appliquer modif a attribute_name
                    System.err.println(" ++++ #### ++++ #### " + attributes.getValue(i) + " : " + attributes.getValue(i + 1));
                    glob_inf.add(attribute_name + " : " + attributes.getValue(i+1));
                }
            }
        }


        if(localName.equals("Definition"))
        {
            in_def = true;
            //25 return;
        }

        if(localName.equals("feat") && attributes.getLocalName(0).equals("att")) {
            switch (attributes.getValue(0)) {
                case "lexeme":
                    if (attributes.getLocalName(1).equals("val")) {
                        word_list.add(attributes.getValue(1));
                    }
                    break;
                case "citationForm":
                    if (in_local_alphabet && attributes.getLocalName(1).equals("val")) {
                        citation_form = attributes.getValue(1);
                    }
                    break;
                case "scriptName":
                    if (!alphabets.contains(attributes.getValue(1))) {
                        alphabets.add(attributes.getValue(1));
                        System.out.println("YŸY æ " + attributes.getValue(1));
                        // TODO delete hard coded strings
                        //in_local_alphabet = attributes.getValue(1).equals(alphabets.get(1));
                        in_local_alphabet = attributes.getValue(1).equals("devanagari");
                    }
                    break;
                case "language" :
                    // @// TODO: 16/06/2016 check if language in glob_inf could be useful
                    if (!languages.contains(attributes.getValue(1))) {
                        languages.add(attributes.getValue(1));
                        System.out.println("YŸY " + attributes.getValue(1));
                    }
                    if (in_def) {
                        // @TODO Replace hard coded variable
                        in_local_def = attributes.getValue(1).equals("cmn");
                    }
                    break;

                case "gloss" :
                    if (in_def && in_local_def) {
                        //def_gloss += (def_gloss.equals("(") ? "" : " - ") + attributes.getValue(1);
                        // 30/08 (equals can't be true for two StringBuilders -even if they contain the same letters-)
                        def_gloss.append((def_gloss.length() == 1 ? "" : " - ")).append(attributes.getValue(1));
                    }
                    break;
            }
        }



        // @todo pas sur que ce soit le bon endroit (faire un lecteur specifique pour les infos globales)

        for (int i = 0; i < attributes.getLength(); i++) {
            //System.out.print(" " + attributes.getLocalName(i)
            //                     + "=\"" + "xxx" + attributes.getValue(i) + "xxx" + "\"");

            if (info_on && attributes.getLocalName(i).equals("att"))
            {
                String attribute_name;
                attribute_name = attributes.getValue(i);
                //attribute_name.replaceFirst()
                //attribute_name.toUpperCase(0);  @TODO appliquer modif a attribute_name
                //System.out.println(attributes.getValue(i) + " : " + attributes.getValue(i + 1));
                glob_inf.add(attribute_name + " : " + attributes.getValue(i+1));
            }
        }
    }

    public void endElement(String uri, String localName, String qName) {

        if(localName.equals("GlobalInformation"))
        {
            info_on = false;
        }

        // 25/08/2016
        if(localName.equals("WordForm") || localName.equals("Audio"))
        {
            ignore = false;
            return;
        }
        if (ignore)
            return;


        if(localName.equals("Definition"))
        {
            //def_list.add(def_gloss + ")");
            in_def = false;
            //25 return;
        }

        if(localName.equals("LexicalEntry"))
        {
            gloss_list.add(def_gloss + ")");
            //in_def = false;
            if (citation_form != null && citation_form.length() > 0)
                citation_form_list.add(citation_form);
            else
                citation_form_list.add(word_list.get(word_list.size()-1));
        }
    }

    // TODO use one of this functions if necessary
    // provide data found in an element.
    // public void characters(char[] ch, int start, int length) {}


    // provide ignorable spaces found in an element.
    // public void ignorableWhitespace(char[] ch, int start, int length) {}
}







class DictHandler extends DefaultHandler {

    private boolean info_on = false;
    private ArrayList<String> word_list;
    private ArrayList<String> id_list;
    private ArrayList<String> glob_inf;
    private ArrayList<String> languages;
    private ArrayList<String> alphabets;

    public DictHandler(ArrayList<String> word_list, ArrayList<String> id_list, ArrayList<String> glob_inf, ArrayList<String> languages, ArrayList<String> alphabets)
    {
        this.word_list = word_list;
        this.id_list = id_list;
        this.glob_inf = glob_inf;
        this.languages = languages;
        this.alphabets = alphabets;
    }


    public void startDocument() {
        //System.out.println("--Début du document--");
    }

    public void endDocument() {
//        System.out.println();
//        System.out.println("--Fin du document--");
    }

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) {
        //System.out.print("<" + localName);

        if(localName.equals("GlobalInformation"))
        {
            info_on = true;
        }

// <LexicalEntry id="per">
// <feat att="lexeme" val="per"/>
// jusqu"a present je prennais l'id de LexicalEntry
        if(localName.equals("LexicalEntry"))
        {
            if (attributes.getLocalName(0).equals("id")) {
                id_list.add(attributes.getValue(0));
            }
        }


        if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("lexeme"))
        {
            if (attributes.getLocalName(1).equals("val")) {
                word_list.add(attributes.getValue(1));
            }
        }

        if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("language"))
        {
            // @// TODO: 16/06/2016 check if language in glob_inf could be useful
            if(!languages.contains(attributes.getValue(1))) {
                languages.add(attributes.getValue(1));
                System.out.println("YŸY " + attributes.getValue(1));
            }
        }

        // @// TODO: 17/06/2016 check this risky add
        if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("scriptName"))
        {
            if(!alphabets.contains(attributes.getValue(1))) {
                alphabets.add(attributes.getValue(1));
                System.out.println("YŸY æ " + attributes.getValue(1));
            }
        }

        // @todo pas sur que ce soit le bon endroit (faire un lecteur specifique pour les infos globales)
        for (int i = 0; i < attributes.getLength(); i++) {
            //System.out.print(" " + attributes.getLocalName(i)
            //                     + "=\"" + "xxx" + attributes.getValue(i) + "xxx" + "\"");

            if (info_on && attributes.getLocalName(i).equals("att"))
            {
                String attribute_name;
                attribute_name = attributes.getValue(i);
                //attribute_name.replaceFirst()
                //attribute_name.toUpperCase(0);  @TODO appliquer modif a attribute_name
                System.err.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ " + attributes.getValue(i) + " : " + attributes.getValue(i + 1));
                glob_inf.add(attribute_name + " : " + attributes.getValue(i+1));
            }
        }
        //   System.out.print(">");
    }

    public void endElement(String uri, String localName, String qName) {
//    System.out.print("</" + localName + ">");
        if(localName.equals("GlobalInformation"))
        {
            info_on = false;
        }
    }

    // Fournit les données trouvées à l'intérieur d'un élément.
    public void characters(char[] ch, int start, int length) {
//    System.out.print(new String(ch, start, length));
    }

    // Fournit des espaces ignorables trouvés à l'intérieur d'un élément.
    public void ignorableWhitespace(char[] ch, int start, int length) {
//    System.out.print(new String(ch, start, length));
    }
}






class DefSearchHandler extends DefaultHandler {
    private String searched_string = "";
    private boolean found = false;
    private boolean in_def = false;
    private boolean in_context = false;
    private boolean in_sense = false;
    private boolean in_lemma = false;
    // add 16.12
    private boolean in_wordform = false;
    // never used? private boolean in_wffr = false;

    private boolean in_tr = false;
    private boolean in_audio = false;
    // @TODO voir si c'est utile et de toute facon mettre a false quand on sort
    private boolean in_fr = false;

    // added for picture
    private boolean in_picture = false;

    private LMFWord res;
    private Entry current_entry;
    private Sense current_sense;
    private TextRepresentation current_tr;
    private FormRepresentation current_fr;
    private AudioRepresentation current_ar;

    // added for picture
    private Illustration current_illustration;

    private  GrammarFeatures current_gramfeat;

    //private ArrayList<GrammarFeatures> gramfeat_list;

    private ArrayList<TextRepresentation> current_context;
    private ArrayList<FormRepresentation> current_lemma;
    private ArrayList<AudioRepresentation> ar_list;

    // added for picture
    private ArrayList<Illustration> illustration_list;

    private String def_lgg;
    private String def_gloss;

    public DefSearchHandler(String searched_string, LMFWord out_word)
    {
        this.searched_string = searched_string;
        res = out_word;
        //ar_list = in_ar_list;
    }

    public void startDocument() {}
    public void endDocument() {}

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) {
        if(localName.equals("LexicalEntry"))
        {
            if (attributes.getLocalName(0).equals("id") && attributes.getValue(0).equals(searched_string))
            {
                current_entry = new Entry();
                found = true;
                // en cours
                ar_list = new ArrayList<>();
                // a priori pas dans cette partie >> gramfeat_list = new ArrayList<>();

                // added for picture
                illustration_list = new ArrayList<>();
            }
        }

        if (found)
        {
            if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("partOfSpeech"))
            {
                current_entry.partofspeech = attributes.getValue(1);
            }

            // @TODO je pense qu'il va falloir supprimer cette condition (voir ci-dessous)
            //if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("citationForm"))

            if(localName.equals("Lemma"))
            {
                in_lemma = true;
                current_lemma = new ArrayList<>();
            }

            if(localName.equals("Sense"))
            {
                in_sense = true;
                current_sense = new Sense();
            }

            if(localName.equals("Definition"))
            {
                in_def = true;
            }

            // add 16.12
            if(localName.equals("WordForm"))
            {
                in_wordform = true;
                current_gramfeat = new GrammarFeatures();
            }

            // @TODO mettre ce bloc dans le bloc in_sense
            if (in_def)
            {
                if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("language"))
                {
                    def_lgg = attributes.getValue(1);
                    //System.out.println("@@@@@@@@@@@@@@@@@@@@@@ def_lgg = " + def_lgg);
                }

                if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("gloss"))
                {
                    def_gloss = attributes.getValue(1);
                    //System.out.println("@@@@@@@@@@@@@@@@@@@@@@ def_gloss = " + def_gloss);
                }
            }

            // @TODO put this bloc in in_sense bloc
            if(localName.equals("Context"))
            {
                in_context = true;
                current_context = new ArrayList<>();
            }

            if (in_context)
            {
                if(localName.equals("TextRepresentation"))
                {
                    in_tr = true;
                    current_tr = new TextRepresentation();
                }
            }

            // @TODO mettre ce  bloc dans le precedent (in_context)
            if (in_tr)
            {
                if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("language"))
                {
                    current_tr.Language = attributes.getValue(1);
                }

                if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("scriptName"))
                {
                    current_tr.scriptName = attributes.getValue(1);
                }

                if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("writtenForm"))
                {
                    current_tr.writtenForm = attributes.getValue(1);
                }
            }


            // add 16.12
            if (in_wordform)
            {
                if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("grammaticalNumber"))
                {
                    //System.out.println("$$$$$$$$ et voila le nb : " + attributes.getValue(1));
                    current_gramfeat.nb = attributes.getValue(1);
                }
                if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("person"))
                {
                    //System.out.println("$$$$$$$$ et voila la person. : " + attributes.getValue(1));
                    current_gramfeat.person = attributes.getValue(1);
                }

                if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("writtenForm"))
                {
                    //System.out.println("$$$$$$$$ et voila le \"radical\" : " + attributes.getValue(1));
                    current_gramfeat.radical_list.add(attributes.getValue(1));
                }

            }


            if (in_lemma)
            {

                if(localName.equals("FormRepresentation"))
                {
                    in_fr = true;
                    current_fr = new FormRepresentation();
                }

                if(in_fr)
                {
                    if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("citationForm"))
                    {
                        current_fr.citationForm  = attributes.getValue(1);
                    }

                    if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("spellingVariant"))
                    {
                        current_fr.spellingVariant  = attributes.getValue(1);
                    }

                    if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("scriptName"))
                    {
                        current_fr.scriptName  = attributes.getValue(1);
                    }

                    // @TODO remplacer cette bidouille infame par un vrai traitement de l'audio (j'avais pas vu qu'on utilisait aussi "FormRepresentation" pour l'audio)
                    if(localName.equals("Audio"))
                    {
                        in_fr = false;
                        current_ar = new AudioRepresentation();
                        in_audio = true;
                    }

                    // added for picture
                    if(localName.equals("Picture"))
                    {
                        // @todo regarder si la ligne suivante a un interet
                        //in_fr = false;
                        current_illustration = new Illustration();
                        in_picture = true;
                        System.out.println(">>>> >>>> >>>> >>>> ICI ON A TROUVE UNE IMAGE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }

                    // added for picture
                    if(in_picture)
                    {
                        if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("format"))
                        {
                            current_illustration.format  = attributes.getValue(1);
                        }
                        if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("mediaType"))
                        {
                            current_illustration.mediaType  = attributes.getValue(1);
                        }
                        if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("fileName"))
                        {
                            current_illustration.fileName  = attributes.getValue(1);
                        }
                    }

                }

                if(in_audio)
                {
                    if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("startPosition"))
                    {
                        current_ar.startPosition  = attributes.getValue(1);
                    }

                    if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("audioFileFormat"))
                    {
                        current_ar.audioFileFormat  = attributes.getValue(1);
                    }

                    if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("mediaType"))
                    {
                        current_ar.mediaType  = attributes.getValue(1);
                    }

                    if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("fileName"))
                    {
                        current_ar.fileName  = attributes.getValue(1);
                    }

                    if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("quality"))
                    {
                        current_ar.quality  = attributes.getValue(1);
                    }
                }
            }
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if (found)
        {
            if (localName.equals("LexicalEntry"))
            {
                found = false;
                res.entry_list.add(current_entry);
                for (Sense sense : current_entry.sense_list) {
                    for (Map.Entry<String,String> definition : sense.definition_list.entrySet())
                    System.out.println("¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶¶ lgg = " + definition.getKey() + " def = " + definition.getValue());
                }
            }

            if (in_def)
            {
                if (localName.equals("Definition"))
                {
                    in_def = false;
                    // 30/08 condition added (several definitions for the same language)
                    if (current_sense.definition_list.containsKey(def_lgg))
                        current_sense.definition_list.put(def_lgg,
                                current_sense.definition_list.get(def_lgg) + " / " + def_gloss);
                    else
                        current_sense.definition_list.put(def_lgg, def_gloss);
                }
            }

            if (in_sense)
            {
                if (localName.equals("Sense"))
                {
                    current_entry.sense_list.add(current_sense);
                    in_sense = false;
                }
            }


            if (in_lemma)
            {
                if (localName.equals("Lemma"))
                {
                    current_entry.lemma_list.add(current_lemma);
                    in_lemma = false;
                }

                if (in_fr)
                {
                    if (localName.equals("FormRepresentation"))
                    {
                        current_lemma.add(current_fr);
                        in_fr = false;
                    }
                }
            }

            // add 16.12
            if (in_wordform) {
                if (localName.equals("WordForm")) {
// ++                    //current_entry.lemma_list.add(current_lemma);
                    in_wordform = false;

                    // add 2016
                    //gramfeat_list.add(current_gramfeat);
                    current_entry.gramfeat_list.add(current_gramfeat);
                }
            }


                // @TODO : mettre dans la boucle de dessus
            if (in_context)
            {
                if (localName.equals("Context"))
                {
                    in_context = false;
                    current_sense.context_list.add(current_context);
                }

                if (in_tr)
                {
                    if (localName.equals("TextRepresentation"))
                    {
                        in_tr = false;
                        current_context.add(current_tr);
                    }
                }

                if (in_audio)
                {
                    if (localName.equals("Audio"))
                    {
                        ar_list.add(current_ar);
                    }
                    if (localName.equals("Lemma"))
                    {
                        current_entry.audio_representation_list = ar_list;
                    }
                }
            }

            // added for picture
            if (in_picture)
            {
                if (localName.equals("Picture"))
                {
                    illustration_list.add(current_illustration);
                    System.out.println(">>>> >>>> >>>> >>>>> current_illustration.fileName = " + current_illustration.fileName);
                }
                // @// TODO: 25/11/2016 tiens c'est un peu plus bizarre ça)
                if (localName.equals("Lemma"))
                {
                    current_entry.illustration_list = illustration_list;
                }
            }
        }
    }

    public void characters(char[] ch, int start, int length) {}
    public void ignorableWhitespace(char[] ch, int start, int length) {}
}









class DictSearchHandler extends DefaultHandler {

    private String current_name = "";
    private String searched_string = "";
    // @TODO check attr_name deletion (a priori we only use definitions in this function)
    private String attr_name = "";
    private String mark_name = "";
    private Boolean contains = false;
    private String chosen_language = "";

    private ArrayList<String> word_list;

    private Boolean in_def = false;
    private String def_lgg = "";
    private String def_gloss = "";
    private ArrayList<String> word_definition_list;

    public DictSearchHandler(String searched_string, String search_type, Boolean contains, ArrayList<String> word_list) {
        this(searched_string, search_type, contains, word_list, null, "");
    }

    public DictSearchHandler(String searched_string, String search_type, Boolean contains, ArrayList<String> word_list, ArrayList<String> word_definition_list, String language)
    {
        this.searched_string = searched_string;
        //this.mark_name = search_type[0];
        //this.attr_name = search_type[1];
        this.chosen_language = language;

        // @TODO check attr_name deletion (a priori we only use definitions in this function)
        if (search_type.equals("definition"))
        {
            this.mark_name ="Definition";
            this.attr_name ="gloss";
        }
        if (search_type.equals("example"))
        {
            this.mark_name ="TextRepresentation";
            this.attr_name ="writtenForm";
        }

        if (search_type.equals("citation form"))
        {
            this.mark_name ="FormRepresentation";
            this.attr_name ="citationForm";
        }


        this.contains = contains;

        this.word_list = word_list;
        this.word_definition_list = word_definition_list;
    }

    public void startDocument() {
        //System.out.println("--Début du document--");
    }

    public void endDocument() {
        //System.out.println("--Fin du document--");
    }

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) {
        //System.out.print("<" + localName);


        if(localName.equals("LexicalEntry"))
        {
            if (attributes.getLocalName(0).equals("id"))
                current_name = attributes.getValue(0);
        }

        // @TODO : faut quand meme faire un truc avec Definition et TextRepresentation (voir Context)
//        if(contains)
//        {
//            //if (localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals(attr_name) && attributes.getValue(1).indexOf(searched_string) != -1)
//            if (localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals(attr_name) && attributes.getValue(1).contains(searched_string))
//            {
//                //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> current_name = " + current_name);
//                word_list.add(current_name);
//                //current_word = current_name;
//                if(this.attr_name.equals("gloss") && word_definition_list != null)
//                {
//                    word_definition_list.add(attributes.getValue(1));
//                    //current_definition = attributes.getValue(1);
//                }
//            }
//        }
//        else
//        {
//            if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals(attr_name) && attributes.getValue(1).equals(searched_string))
//            {
//                //System.out.println(current_name);
//                word_list.add(current_name);
//                //current_word = current_name;
//            }
//
//            if(this.attr_name.equals("gloss") && word_definition_list != null)
//            {
//                word_definition_list.add(attributes.getValue(1));
//                //current_definition = attributes.getValue(1);
//            }
//        }


        if(localName.equals(mark_name))
        {
            in_def = true;
        }
        if (in_def)
        {
            if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("language"))
            {
                def_lgg = attributes.getValue(1);
            }

            if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals(attr_name))
            {
                def_gloss = attributes.getValue(1);
            }
        }
    }

    public void endElement(String uri, String localName, String qName) {

        // @TODO 21/06/2016 on est en plein ajout
//        if(localName.equals("LexicalEntry"))
//        {
//            if (current_word.length() > 0) {
//                System.err.println(("!!!! current_word = " + current_word + " current_definition = " + current_definition));
//                //def_list.add(def_gloss + ")");
//
//                current_word = "";
//            }
//        }


        if(localName.equals("Definition")) {
            in_def = false;

            if (contains) {
                if (def_gloss.contains(searched_string) && def_lgg.equals(chosen_language))
                {
                    System.err.println(("???? current_name = " + current_name + " def_gloss = " + def_gloss + " def_lgg = " + def_lgg));
                    word_list.add(current_name);
                    word_definition_list.add(def_gloss);

                }
            } else {
                if (def_gloss.equals(searched_string) && def_lgg.equals(chosen_language)) {
                    System.err.println(("???? current_name = " + current_name + " def_gloss = " + def_gloss + " def_lgg = " + def_lgg));
                    word_list.add(current_name);
                    word_definition_list.add(def_gloss);
                }
            }
        }
    }
}




// NEW!!
class AsciiDevanagariCorrespondenceHandler extends DefaultHandler {

    private Map<String, String> ascii_devanagari_correspondence_list;

    public AsciiDevanagariCorrespondenceHandler(Map<String, String> ascii_devanagari_correspondence_list)
    {
        this.ascii_devanagari_correspondence_list= ascii_devanagari_correspondence_list;
    }

    public void startDocument() {
        // @TODO?
    }

    public void endDocument() {
        // @TODO?
    }

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) {
        //System.out.print("<" + localName);
        for (int i = 0; i < attributes.getLength(); i++) {
            if (attributes.getLocalName(i).equals("devanagari"))
            {
                System.out.println(attributes.getValue(i));
                // @TODO faire plus joli et sur
                ascii_devanagari_correspondence_list.put(attributes.getValue(i), attributes.getValue(i+1));
//         letter_list.add(attributes.getValue(i));
            }
        }
    }


    public void endElement(String uri, String localName, String qName) {
        if (localName.equals("consonant"))
        {
            ascii_devanagari_correspondence_list.put(" ", " ");
        }
    }

    public void characters(char[] ch, int start, int length) {
    }

    public void ignorableWhitespace(char[] ch, int start, int length) {
    }
}
*/

// @TODO delete this class if never used (e.g. if initCitationFormData is deleted)
/*
class CitationFormHandler extends DefaultHandler {
    private boolean info_on = false;
    private boolean in_devanagari = false;
    private boolean in_def = false;
    private boolean in_local_def = false;
    private String def_gloss;

    private ArrayList<String> word_list;
    private ArrayList<String> id_list;
    private ArrayList<String> glob_inf;

    private ArrayList<String> alphabets_list;


    // @TODO mettre en parametre
    ArrayList<String> def_list;
    public CitationFormHandler(ArrayList<String> word_list, ArrayList<String> id_list, ArrayList<String> def_list, ArrayList<String> glob_inf, ArrayList<String> alphabets_list)
    {
        this.word_list = word_list;
        this.id_list = id_list;
        this.glob_inf = glob_inf;
        // @TODO a remplacer par un truc
        this.def_list = def_list;

        this.alphabets_list = alphabets_list;
    }

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes) {

        if(localName.equals("GlobalInformation"))
        {
            info_on = true;
        }

        if(localName.equals("LexicalEntry"))
        {
            if (attributes.getLocalName(0).equals("id")) {
                id_list.add(attributes.getValue(0));
                def_gloss = "(";
            }
        }

        if(alphabets_list.size() > 0)
        {
            if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("scriptName"))
                in_devanagari = attributes.getValue(1).equals("devanagari");

            if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("citationForm"))
            {
                if (in_devanagari && attributes.getLocalName(1).equals("val")) {
                    word_list.add(attributes.getValue(1));
                }
            }
        }
        else
        {
            if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("lexeme"))
            {
                if (attributes.getLocalName(1).equals("val")) {
                    word_list.add(attributes.getValue(1));
                }
            }
        }


        for (int i = 0; i < attributes.getLength(); i++) {
            if (info_on && attributes.getLocalName(i).equals("att"))
            {
                String attribute_name;
                attribute_name = attributes.getValue(i);
                glob_inf.add(attribute_name + " : " + attributes.getValue(i+1));
            }
        }


        if(localName.equals("Definition"))
        {
            in_def = true;
        }

        if (in_def)
        {
            if(localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("language"))
            {
                // @TODO mettre une vbl pour connaitre la lg locale
                in_local_def = attributes.getValue(1).equals("cmn");
            }

            if(in_local_def) {
                if (localName.equals("feat") && attributes.getLocalName(0).equals("att") && attributes.getValue(0).equals("gloss")) {
                    def_gloss += (def_gloss.equals("(")?"":" - ") + attributes.getValue(1);
                }
            }
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if(localName.equals("GlobalInformation"))
        {
            info_on = false;
        }

        if(localName.equals("Definition"))
        {
            //def_list.add(def_gloss + ")");
            in_def = false;
        }

        if(localName.equals("LexicalEntry"))
        {
            def_list.add(def_gloss + ")");
            //in_def = false;
        }
    }

    public void characters(char[] ch, int start, int length) {
    }
}





class LMFWord {
    public final ArrayList<Entry> entry_list = new ArrayList<>();
}


class Entry {
    public ArrayList<Sense> sense_list = new ArrayList<>();
    public ArrayList<ArrayList<FormRepresentation>> lemma_list = new ArrayList<>();
    public ArrayList<AudioRepresentation> audio_representation_list = new ArrayList<>();
    public String partofspeech;

    // added for picture
    public ArrayList<Illustration> illustration_list = new ArrayList<>();

    public ArrayList<GrammarFeatures> gramfeat_list = new ArrayList<>();
}

class Sense {
    Map<String,String> definition_list = new HashMap<>(); // a priori only one definition per language pour un meme sens -> false
    public ArrayList<ArrayList<TextRepresentation>> context_list = new ArrayList<>();
}

class TextRepresentation {
    public String scriptName;
    public String writtenForm;
    public String Language;
}

class FormRepresentation {
    public String scriptName;
    public String spellingVariant;
    public String citationForm;
}

class Letter{
    public String str;
    public float rank;
    public boolean is_consonant;
}
*/

class AudioRepresentation{
    public String startPosition;
    public String audioFileFormat;
    public String mediaType;
    public String fileName;
    public String quality;
}

// added for picture
class Illustration{
    public String format;
    public String mediaType;
    public String fileName;
}
