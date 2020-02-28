package ru.otus;


import java.util.*;

public class Main {

    public static void main(String[] args) {


        ATM atm = new ATMFactory.ATMBuilder()
                    .nameATM("ATM99")
                    .addCassette(BankNote.R100, 0)
                    .addCassette(BankNote.R200,0)
                    .addCassette(BankNote.R1000,0)
                    .addCassette(BankNote.R5000,0)
                    .build()
                .create();


        //System.out.println(atm);

        Map<BankNote,Integer> putMoneyPack1 = new HashMap<>();
        putMoneyPack1.put(BankNote.R200, 0);
        putMoneyPack1.put(BankNote.R100, 0);
        putMoneyPack1.put(BankNote.R500, 2);
        putMoneyPack1.put(BankNote.R1000, 0);
        putMoneyPack1.put(BankNote.R5000, 2);
        atm.putMoneyPack(putMoneyPack1);
        System.out.println(atm);


        System.out.println("снял: 5100" +atm.requiredAmount(5100));
        System.out.println(atm);


        Map<BankNote,Integer> putMoneyPack2 = new HashMap<>();
        putMoneyPack2.put(BankNote.R200, 3);
        putMoneyPack2.put(BankNote.R100, 0);
        putMoneyPack2.put(BankNote.R500, 6);
        putMoneyPack2.put(BankNote.R5000, 1);
        atm.putMoneyPack(putMoneyPack2);
        System.out.println(atm);


        System.out.println("снял: 6200" + atm.requiredAmount(6200));
        System.out.println(atm);


    }
}