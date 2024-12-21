package com.infolangues.mondico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class infoActivity extends Activity {
    //private TextView textView;
    private LinearLayout panelAS;
    /*private WebView webViewNg;
    private WebView webViewFr;
    private WebView webViewEn;*/
    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        String chosenLanguage = intent.getStringExtra("chosenLanguage");
        WebView webViewEnd = findViewById(R.id.textInfoEnd);
        String TextNg ="<h4>Sani fu a wowtubuku fu Businenge(e) Tongo – Faansi - Ingiisi</h4>\n" +
            "A wowtubuku ya de a ini a wooko fu dicosguy. A wooko na fu meke den wowtubuku fu den tongo fu Lagwiyani. A wowtubuku ya na fu Businenge(e) Tongo, dati wani taki Aluku, Ndyuka (efu Okanisi) anga Pamaka.  Moo sani fu a wooko de <a href=\"https://corporan.huma-num.fr/Lexiques/dicoNengee.html\">ya</a>.\n" +
                "<p> \n" +
                "Na ape wi e soy fa u sikiifi Businenge(e) Tongo anga fa a wowtubuku meke. Na ape i sa go na a wowtubuku tu. Efu i wani sabi moo sani, da i sa sende wan bosikopu na : bettinamigge@ucd.ie\n" +
                "<p>\n" +
                "Den sama di be de a ini a wooko na: Clara Adam, Johanes Ateni, Jean Bonana, Bruno Djani, Ingrid Joachim, Amiélie Kago, Bettina Migge, Mièfi Moese, Bernhard Maafoo, Ricardo Simiesong.\n" +
                "<p>\n" +
                "Den wooko peesi ya tyay yeepi gi a wooko: Académie de Guyane, IRD Cayenne, LLACAN, L’Université de Guyane, SeDyL, University College Dublin.\n" +
                "<p>\n" +
                "Yeepi u tyay a wowotubuku go a fesi. Te i si fowtu efu wan wowtu di e mankey, gaantangi, piki u anga a guun maliki contact di de a ini ibi ‘entrée) fu a wowtubuku efu sende wan bosikopu gi bettinamigge@ucd.ie.";
        if (chosenLanguage.equals("ng")){
            WebView webViewNg = findViewById(R.id.textInfoNg);
            webViewNg.loadDataWithBaseURL(null, TextNg, "text/html", "utf-8", null);
        }
        String TextFr = "<h4>Informations sur le dictionnaire Businenge(e) Tongo – Français – Anglais</h4>\n" +
            "Ce dictionnaire fait partie du projet DicosGuy qui visait à créer des dictionnaires pour plusieurs langues parlées en Guyane dont le businenge(e) tongo dans les trois variantes Aluku, Ndyuka (ou Okanisi), Pamaka. Vous pouvez trouver plus de détails y inclus des films qui expliquent l’architecture et l’orthographe utilisés dans ce dictionnaire sur le site suivant. Vous pouvez également avoir accès au dictionnaire sur ce <a href=\"https://corporan.huma-num.fr/Lexiques/dicoNengee.html\">site</a>.\n" +
                "<p>\n" +
                "Si vous avez besoin plus des informations, vous pouvez contacter : bettinamigge@ucd.ie\n" +
                "<p>\n" +
                "Les personnes suivantes ont participé au projet : Clara Adam, Johanes Ateni, Jean Bonana, Bruno Djani, Ingrid Joachim, Amiélie Kago, Bettina Migge, Miéfi Moese, Bernhard Maafoo, Ricardo Simiesong.\n" +
                "<p>\n" +
                "Les institutions qui ont aidé : Académie de Guyane, IRD Cayenne, CNRS-LLACAN, L’Université de Guyane, CNRS-SeDyL, University College Dublin.\n" +
                "<p>\n" +
                "Vous êtes vivement invité d’apporter des commentaires, corrections et ajouts. Pour cela vous pouvez utiliser la fonction contact (bouton vert à droit dans chaque entrée) ou bien envoyer un email à bettinamigge@ucd.ie.";
        if (chosenLanguage.equals("fr")){
            WebView webViewFr = findViewById(R.id.textInfoFr);
            webViewFr.loadDataWithBaseURL(null, TextFr, "text/html", "utf-8", null);
        }
        String TextEn = "<h4>Information about the Businenge(e) Tongo – French – English dictionary</h4>\n" +
            "This dictionary is part of the project ‘Dictionaries of Guyane’ whose aim was to create dictionaries for several languages spoken in French Guiana such as the Businenge(e) Tongo also called Eastern Maroon Creole which includes the three varieties Aluku, Ndyuka (or Okanisi) and Pamaka. You can find more information including films that explain the makeup of the dictionary and the orthography used and access the dictionary on the following <a href=\"https://corporan.huma-num.fr/Lexiques/dicoNengee.html\">site</a>. \n" +
                "<p>\n" +
                "Additional information can be obtained from: bettinamigge@ucd.ie\n" +
                "<p>\n" +
                "The following people were involved in the production of the dictionary: Clara Adam, Johanes Ateni, Jean Bonana, Bruno Djani, Ingrid Joachim, Amiélie Kago, Bettina Migge, Mièfi Moese, Bernhard Maafoo, Ricardo Simiesong<p>\n" +
                "The following institutions contributed to the project: Académie de Guyane, IRD Cayenne, LLACAN , L’Université de Guyane, SeDyL, University College Dublin.\n" +
                "<p>\n" +
                "Please help us improve the dictionary. Comments, additions and corrections can be sent to using the green contact button found in every entry of the dictionary or you can send an email to bettinamigge@ucd.ie.";
        if (chosenLanguage.equals("en")){
            WebView webViewEn = findViewById(R.id.textInfoEn);
            webViewEn.loadDataWithBaseURL(null, TextEn, "text/html", "utf-8", null);
        }
        String TextEnd = "<h4>Wantu sani di i sa leysi / bibliographie sélective / selected reference </h4>\n" +
            "<ul><li>Goury, Laurence & Migge, Bettina. 2003/2017. Grammaire du nengee: introduction aux langues aluku, ndjuka et pamaka. Paris: Editions IRD.</li>\n" +
            "<li>Huttar, George & Mary Huttar. 1994. Ndyuka. London and New York: Routledge.</li>\n" +
            "<li>Léglise, Isabelle & Migge, Bettina (eds.). 2007. Pratiques et attitudes linguistiques en Guyane : regards croisés. Paris: Editions IRD.</li>\n" +
            "<li>Migge, Bettina. 2021. Creoles and variation. In The Cambridge Handbook of Language Standardization, Wendy Bennett & John Bellamy (eds.), 371-393. Cambridge: CUP.</li>\n" +
            "<li>Migge, Bettina 2003. Creole formation as language contact: The case of the Suriname Creoles. Amsterdam: John Benjamins.</li>\n" +
            "<li>Migge, Bettina & Léglise, Isabelle. 2015. Assessing the sociolinguistic situation of the Maroon Creoles. Journal of Pidgin and Creole Languages 30 (1): 63-115.</li>\n" +
            "<li>Migge, Bettina & Léglise, Isabelle. 2010. Integrating local languages and cultures into the education system of French Guiana: A discussion of current programs and initiatives. In Creoles in education: An appraisal of current programs and projects: A discussion of current programs and initiatives, Bettina Migge & Isabelle Léglise & Angela Bartens (eds.), 107-132. Amsterdam: John Benjamins.</li>\n" +
            "<li>Price, Richard. 2013. Research note. The Maroon population explosion: Suriname and Guyane. New West Indian Guide 87(3&4): 323–327.</li>\n" +
            "<li>Price, Richard & Sally Price. 2003. Les Marrons. Chateauneuf-le-Rouge France: Vents d’ailleurs. </li></ul>\n" +
            "<h4>Den abréviation fu a wowtubuku / Liste des abréviations / List of abbreviations</h4>\n" +
                "<ul><li>adj - adjective </li>\n" +
                "<li>adv - adverb </li>\n" +
                "<li>aux - auxiliary </li>\n" +
                "<li>conj - conjunction </li>\n" +
                "<li>cop - copula </li>\n" +
                "<li>dem - demonstrative </li>\n" +
                "<li>det - determiner </li>\n" +
                "<li>DM - discourse marker </li>\n" +
                "<li>emph - emphatic </li>\n" +
                "<li>f - feminine </li>\n" +
                "<li>idéo - ideophone </li>\n" +
                "<li>inter - interrogative </li>\n" +
                "<li>intj - interjection </li>\n" +
                "<li>loc - locative </li>\n" +
                "<li>n - noun </li>\n" +
                "<li>neg - negative </li>\n" +
                "<li>num - numeral </li>\n" +
                "<li>pl - plural </li>\n" +
                "<li>pro - pronoun </li>\n" +
                "<li>v - verb</li></ul>";
        //webViewNg.loadDataWithBaseURL(null, TextNg, "text/html", "utf-8", null);
        //webViewEn.loadDataWithBaseURL(null, TextEn, "text/html", "utf-8", null);
        webViewEnd.loadDataWithBaseURL(null, TextEnd, "text/html", "utf-8", null);

        //webView.loadDataWithBaseURL(null, Text+TextEn+TextFr+TextEnd, "text/html", "utf-8", null);
        //webView.loadUrl("javascript:document.getElementById(\"Ng\").setAttribute(\"style\",\"display:none;\");");
        //webViewEn.setVisibility(View.GONE);
        //webView.loadDataWithBaseURL(null, TextF, "text/html", "utf-8", null);
        /*RadioGroup langueGroup =  (RadioGroup) findViewById(R.id.checkbox);
        langueGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedId = group.getCheckedRadioButtonId();
                //RadioButton selectedLang = (RadioButton) findViewById(checkedId);
                switch (checkedId) {
                    case 1:
                        webViewNg.setVisibility(View.VISIBLE);
                        webViewEn.setVisibility(View.GONE);
                        webViewFr.setVisibility(View.GONE);
                    case 2:
                        webViewEn.setVisibility(View.GONE);
                    case 3:
                        webViewEn.setVisibility(View.GONE);
                }
            }
        });*/
    }
}