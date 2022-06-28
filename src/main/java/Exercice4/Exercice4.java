package Exercice4;

import commun.Commun;

import java.util.HashMap;


public class Exercice4 {

   static final private Commun redis = new Commun();


    public static void main(String[] args) {
        insertOrUpdateOne();
        insertOrUpdateMany();
        deleteOneField();
        deleteManyField();
        deleteOneAsin();
        deleteManyAsin();

    }

    public static void insertOrUpdateOne(){
        long value = redis.setAsin("B0012345678","1234567891012","Insertion1");
        System.out.println(value);
    }

    public static void insertOrUpdateMany(){
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.put(String.valueOf(i),"insert"+i);
        }

        long value = redis.setAsin("B00123456789",map);
        System.out.println(value);
    }

    public static void deleteOneField(){
        long value = redis.deleteField("B0012345678","1234567891012");
        System.out.println(value);
    }

    public static void deleteManyField(){
        String[] field = new String[9];
        for (int i = 0; i < 9; i++) {
            field[i] = Commun.NAME_FIELD_FEEDBACK +":" + String.valueOf(i);
        }

        long value = redis.deleteField("B00123456789",field);
        System.out.println(value);
    }

    public static void deleteOneAsin(){
        long value = redis.deleteAsin("B001C0IDVU");
        System.out.println(value);
    }

    public static void deleteManyAsin(){
        String[] asin = new String[3];
        asin[0] = Commun.NAME_HASH_FEEDBACK+":"+ "B001TSCINE";
        asin[1] = Commun.NAME_HASH_FEEDBACK+":"+"B005DG5TZ2";
        asin[2] = Commun.NAME_HASH_FEEDBACK+":"+"B00AM4K6K8";


        long value = redis.deleteAsin(asin);
        System.out.println(value);
    }






}

