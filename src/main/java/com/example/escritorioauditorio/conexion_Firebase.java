package com.example.escritorioauditorio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;


import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

public class conexion_Firebase {
    public static Firestore db;
    private static FirebaseDatabase firebaseDatabase;
    public static void conectarFirebase(){
/*

        InputStream serviceAccount = conexion_Firebase.class.getResourceAsStream("bdauditorio.json");

        FirebaseOptions options;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials( GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://BDAuditorio.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading database");
        }

        firebaseDatabase = FirebaseDatabase.getInstance();








        /*try {
            FileInputStream sa = new FileInputStream("bdauditorio.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(sa))
                    .setDatabaseUrl("https://BDAuditorio.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();
            System.out.println("Exito al conectar !!");
        }catch (IOException e){
            System.err.println("Error: "+ e.getMessage());
        }
*/

    }

    public static void main(String[] args) {
        conexion_Firebase.conectarFirebase();
    }
}
