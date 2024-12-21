package com.infolangues.mondico;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by bonnet on 13/09/2016.
 * test
 */
public class SQLiteWrapper extends SQLiteOpenHelper {

    private final Context mycontext;
    private static SQLiteWrapper sInstance;

    private static final int DATABASE_VERSION = 10; // l'incrément appelle onUpgrade(), décrément => onDowngrade()
    private final String DATABASE_PATH; // path defined in constructor
    //private static final String DATABASE_NAME = "db.sqlite";
    private static final String DATABASE_NAME = "dico.db";

    public static synchronized SQLiteWrapper getInstance(Context context) {
        if (sInstance == null) { sInstance = new SQLiteWrapper(context); }
        return sInstance;
    }

    // Constructor
    private SQLiteWrapper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mycontext=context;
        String filesDir = context.getFilesDir().getPath(); // /data/data/com.package.nom/files/
        DATABASE_PATH = filesDir.substring(0, filesDir.lastIndexOf("/")) + "/databases/"; // /data/data/com.package.nom/databases/

        if (DATABASE_NAME.equals("dico.db"))
            mycontext.deleteDatabase(DATABASE_NAME);
        else
            System.err.println("au moins on efface pas la base qui fonctionne a peu pres");

        // Si la bdd n'existe pas dans le dossier de l'app
        if (!checkdatabase()) {
            System.out.println("EXISTE PAS!!!!!!!!!!!!!!!!!");
            // copy db de 'assets' vers DATABASE_PATH
            copydatabase();
        }
        // @TODO supprimer le else
        else
            System.out.println("EXISTE!!!!!!!!!!!!!!!!!");
    }

    private boolean checkdatabase() {
        // retourne true/false si la bdd existe dans le dossier de l'app
        File dbfile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbfile.exists();
    }

    // On copie la base de "assets" vers "/data/data/com.package.nom/databases"
    // ceci est fait au premier lancement de l'application
    private void copydatabase() {

        final String outFileName = DATABASE_PATH + DATABASE_NAME;

        InputStream myInput;
        try {
            // Ouvre la bdd de 'assets' en lecture
            // @TODO delete println
            System.err.println(">> On passe par SQLiteWrapper.java!!!!");
            myInput = mycontext.getAssets().open(DATABASE_NAME);

            // @TODO delete println
            System.err.println(">> myInput.toString() = " + myInput);

            // dossier de destination
            File pathFile = new File(DATABASE_PATH);
            // @TODO delete println
            System.err.println(">> " + DATABASE_PATH + " pathFile.exists() : " + pathFile.exists());
            if(!pathFile.exists()) {
                if(!pathFile.mkdirs()) {
                    Toast.makeText(mycontext, "Erreur : copydatabase(), pathFile.mkdirs()", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // @TODO delete println
            System.err.println(">> DATABASE_NAME = " + DATABASE_NAME + " outFileName = " + outFileName);

            // @TODO delete println
            String[] listefichiers = pathFile.list();
            for(int i=0;i<listefichiers.length;i++){
                System.err.println("\t>> listefichiers[" + i + "] = " + listefichiers[i]);
            }

            // Ouverture en écriture du fichier bdd de destination
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfert de inputfile vers outputfile
            byte[] buffer = new byte[1024];
            int length;
            // @TODO delete println
            System.err.println(">> Si tout se passe bien, on fera un truc dans SQLiteWrapper.java!!!!");
            while ((length = myInput.read(buffer)) > 0) {
                // @TODO delete println
                //System.err.println(">> On fait un truc dans SQLiteWrapper.java!!!!");
                myOutput.write(buffer, 0, length);
            }

            // Fermeture
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mycontext, "Erreur : copydatabase()", Toast.LENGTH_SHORT).show();
        }

        // on greffe le numéro de version
        try{
            // @TODO delete println
            System.err.println(">> On passe par SQLiteWrapper.java!!!! 2");
            SQLiteDatabase checkdb = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            checkdb.setVersion(DATABASE_VERSION);
        }
        catch(SQLiteException e) {
            System.err.println("SQLiteException: " + e.getMessage());
        }

    } // copydatabase()

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion){
            //Log.d("debug", "onUpgrade() : oldVersion=" + oldVersion + ",newVersion=" + newVersion);
            mycontext.deleteDatabase(DATABASE_NAME);
            copydatabase();
        }
    } // onUpgrade

}
