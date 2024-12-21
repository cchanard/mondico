package com.infolangues.mondico;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class helpActivity extends Activity {
    //private TextView textView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Intent intent = getIntent();
        String chosenLanguage = intent.getStringExtra("chosenLanguage");
        TextView textView = findViewById(R.id.textHelp);
        String Text = "";
        String TextEn = "<h2>Help</h2>" +
                "<h3>Main search</h3>" +
                "<img src=\"@drawable/loupe\"><ul><li>From the main menu, you can search a word by clicking the <i>loup</i> button," +
                " then type the word where the text <i>Search a word</i> appears.<br>" +
                "After the second letter you typed for the word, a list of entries matching the input is displayed " +
                "allowing you to choose a word in the list or you can continue to type to reduce the choice.<br>" +
                "Once you click a word in the list, its complete entry will be displayed.</li></ul>" +
                "<ul><img src=\"@drawable/languages\"><li>You can choose the language in which you want to search, by clicking the language tab.<br>" +
                "By default the word is searched in the <i>definition</i> field.<br>" +
                "It's possible to search using in the examples too (cf. <i>Advanced Search</i> below).</li></ul>" +
                "<h3>List of words</h3>" +
                "<img src=\"@drawable/carrousel\"><ul><li>The horizontal scrolling bar of letters allows you to display all the words " +
                "that begin with the letter you chose, with their part of speech and definition.</li>" +
                "<li>From that vertical scrollable list of words, when you click a word, you will open the complete " +
                "entry of the word.</li>" +
                "<li>From the entry of a word, you can go to the next word with the  <img src=\"@drawable/fleched\"> button " +
                "or to the previous one with the <img src=\"@drawable/flecheg\"> button</li>" +
                "<li>The <i>home</i> button allows you to return to the main page.</li></ul>" +
                "<h3>Advanced Search</h3>" +
                "<img src=\"@drawable/advancedsearch\"><ul><li>You can open the <i>Advanced Search</i> by clicking the v button in the <i>Advanced Search</i> bar " +
                "and choose various options. Then <i>Advanced Research</i> can be turned on and off using the slider.</li>" +
                "<li>There you can select where you want to search : <i>word</i> and <i>example</i> " +
                "for Nengee and <i>definition</i> and <i>example</i> for Français or English.</li>" +
                "<li>You can also list all the words that share the same grammatical <i>category</i> or the same <i>thesaurus</i> " +
                "keys by checking those keys and search for the joker *.</li>" +
                "<li>Don't forget to turn the <i>advanced research</i> off if you want to search without these constraints.</li></ul><p>";

        String TextFr = "<h2>Aide</h2>" +
                "<h3>Recherche simple</h3>" +
                "<img src=\"@drawable/loupe\">" +
                "<ul><li>Depuis le menu principal, vous pouvez rechercher un mot dans 'Search a word' en cliquant sur le bouton <i>loupe</i>," +
                " puis tapez le mot où le texte <i>Rechercher un mot</i> apparaît.<br>" +
                "Après la deuxième lettre que vous avez tapée pour le mot, une liste d'entrées correspondant à ces lettres s'affiche " +
                "vous permettant de choisir un mot dans la liste ou vous pouvez continuer à taper pour réduire le choix.<br>" +
                "Une fois que vous aurez cliqué sur un mot dans la liste, son entrée complète s'affichera.</li></ul>" +
                "<img src=\"@drawable/languages\">" +
                "<ul><li>Vous pouvez choisir la langue dans laquelle vous souhaitez effectuer la recherche en cliquant sur l'onglet Langue.<br>" +
                "Par défaut le mot est recherché dans le champ <i>définition.</i></li>" +
                "<li>Il est également possible de chercher dans les exemples (cf. <i>Recherche avancée</i> ci-dessous).</li></ul>" +
                "<h3>Liste de mots</h3>" +
                "<img src=\"@drawable/carrousel\"><ul><li>La barre de défilement horizontale des lettres permet d'afficher tous les mots " +
                "qui commencent par la lettre que vous choisissez, avec leur partie du discours et leur définition.</li>" +
                "<li>À partir de cette liste déroulante verticale de mots, lorsque vous cliquez sur un mot, vous ouvrirez l'entrée complète du mot.</li>" +
                "<li>Depuis l'entrée d'un mot, vous pouvez passer au mot suivant avec le bouton <img src=\"@drawable/fleched\"> " +
                "ou au précédent avec le bouton <img src=\"@drawable/flecheg\">bouton</li>" +
                "<li>Le bouton <i>accueil</i> vous permet de revenir à la page principale.</li></ul>" +
                "<ul><h3>Recherche avancée</h3><img src=\"@drawable/advancedsearch\">" +
                "<li>Vous pouvez ouvrir la <i>Recherche avancée</i> en cliquant sur le bouton v dans la <i>Recherche avancée</i > barre " +
                "et choisissez différentes options. La <i>Recherche avancée</i> peut ensuite être activée et désactivée à l'aide du curseur.</li>" +
                "<li>Là, vous pouvez sélectionner les champs dans lesquels vous souhaitez rechercher : <i>word</i> et <i>example</i> pour le nengee, et <i>definition</i> et <i>example</i> pour le français ou l'anglais " +
                ".</li>" +
                "<li>Vous pouvez aussi lister tous les mots d'une même <i>catégorie</i> grammaticale ou bien appartenant au même champ lexical (<i>thésaurus</i>) " +
                "en sélectionnant ces clés et en recherchant le joker *.</li>" +
                "<li>N'oubliez pas de désactiver la <i>recherche avancée</i> si vous souhaitez effectuer une recherche sans ces contraintes.</li></ul><p>";

        String TextNg = "<h2>Yeepi</h2>" +
                "<h3> Suku wowtu</h3>" +
                "<img src=\"@drawable/loupe\"><ul><li> Te i opo a wowtubuku, i mu banda a <i>loupe</i> mayki " +
                "fu suku wan wowtu. Na pe i si  <i>Search a word</i>, i e sikiifi a wowtu di i e suku.<br>" +
                "Te i kaba naki den fosi tu leteli, omen wowtu e kon. " +
                "I sa fende a wowtu di i suku de efuso i e sikiifi a hii wowtu go doo.<br>" +
                "Te i si a wowtu di i e suku, da i banda en, da i o si ala den sani fu en.</li></ul>" +

                "<img src=\"@drawable/languages\"><ul><li>I sa suku wowtu a ini (Busi)nenge(e) Tongo, Faansi anga Ingiisi. Te i banda a <i>loupe</i> mayki, den nen fu den dii tongo e kon, da i banda di i wani.<br>" +
                "Baka dati, da i sikiifi a wowtu di i e suku a ini a tongo di i teke na pe a e taki <i>Search a word</i>." +
                "I sa suku a ini den ‘entrées’ fu den wowtu tu. I mu banda <i>advanced search</i>, da i teke a sani di i wani suku (kande adv = adverbe) anga a tongo, da i poti a sitali * efu ibiwan taa leteli a ini <i>Search a word</i> a tapu e doloki a <i>loupe</i> maliki te a ondoo.</li></ul>" +

                "<h3> Leysiti fu den wowtu</h3>" +
                "<img src=\"@drawable/carrousel\"><ul><li>Te i banda wan fu den leteli di i si a tapu, da ala den wowtu" +
                " di e bigin anga a leteli de e kon. I e si a wowtu anga san a e wani taki a ini Faansi anga Inigiisi. </li>" +
                "<li>Te i banda a wowtu now, da i e si ala sani fu en.</li>" +
                "<ul><h3> A entrée fu a wowtu</h3>" +
                "<li>Te i luku go a tapu da i e si tu guun ga, disi ya <img src=\"@drawable/fleched\"> e tyay i go na a wowtu di e kon na en fesi, " +
                "da a taa wan<img src=\"@drawable/flecheg\">o tyay i go na a wowtu di e kon na en baka.</li>" +
                "<li>A pikin osu di i e si na a kukutuse a tapu e tyay i go na a bigin fu a wowtubuku baka.</li></ul>" +

                "<h3> Suku fini</h3>" +
                "&nbsp;<img src=\"@drawable/advancedsearch\"><ul><li>Te i banda a <i>loupe</i> mayki, i e si <i>advanced search</i> na den tongo nen ondoo. Te i banda en efu a pikin baaka ga v na a se fu <i>advanced search</i> now, wan fensee e opo. " +
                "Na ape i e fende omen fasi fu suku sani a ini a wowtubuku. Anga a weti lontu, i e tapu e opo <i>advanced research</i>.</li>" +
                "<li>Na ape i sa fende sani di o yeepi i suku: <i>word</i> (efu i wani suku soso a ini den wowtu a ini ala den dii tongo); <i>definition</i> (efu i wani suku a ini san den wowtu e wani taki a ini Faansi anga Ingiisi); <i>example</i> (efu I wani suku a ini den eksempee a ini ala den dii tongo)." +
                "<li>I sa suku tu ala den wowtu di e tan na a seefi fasi enke ala den ‘verbe’ efu den wowtu di de a ini a seefi famii wowtu (thesaurus: Agriculture)</i>." +
                "<li>Fu suku now, i mu naki a fokanti na en fesi e poti a sitali * efu ibiwan taa leteli a ini <i>Search a word</i>.</li>" +
                "<li>Ná feegete fu kii a <i>advanced research</i> te i wani suku wowtu kowownu fasi.</li></ul>";

        if (chosenLanguage.equals("en")) {
            Text = TextEn;
        } else if (chosenLanguage.equals("fr")) {
            Text = TextFr;
        } else if (chosenLanguage.equals("ng")) {
            Text = TextNg;
        }
        textView.setText(Html.fromHtml(Text, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int resourceId = getResources().getIdentifier(source, "drawable", getPackageName());
                Drawable drawable = getResources().getDrawable(resourceId);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            }
        }, null));
    }
}