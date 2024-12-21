package com.infolangues.mondico;

import android.app.Application;
import android.content.Context;

/**
 * Created by bonnet on 01/09/2015.
 * used to call the context anywhere in the app
 */
// @TODO find an other name
public class MyApplication extends Application {
    private static Context context;

    private static final boolean display_log = false;

    static /*final*/ boolean offline_dictionary = false;
    static final boolean word_duplication_without_prefix = true;

    //static /*final*/ boolean linguist_version = false;
    // update_database not used anymore >> static  boolean update_database = false;

    static final String CHOSEN_LETTER = "chosen_letter";
    static final String CHOSEN_WORD = "chosen_word";
    static final String CHOSEN_ALPHABET = "chosen_alphabet";
    static final String SEARCHED_STRING = "searched_string";
    static final String CHECKBOX_STATE = "checkbox_state";
    static final String CHOSEN_LANGUAGE = "chosen_language";
    static final String CURRENT_SEARCH = "current_search";
    static final String CHOSEN_THESAURUS = "chosen_thesaurus";



        public void onCreate(){
            super.onCreate();
            MyApplication.context = getApplicationContext();
        }

        public static Context getAppContext() {
            return MyApplication.context;
        }

    public static void Log(String message){
        if(display_log)
            System.out.println(message);
    }

}