package net.xeill.elpuig;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Scanner entrada = new Scanner(System.in);
        boolean N = true;
        while (N){
            System.out.println("------MENU------");
            System.out.println("1. Ver todas las armas");
            System.out.println("2. Ver todas las llaves");
            System.out.println("3. Ver todas las cajas");
            System.out.println("4. Ver las skins que te pueden tocar en cada caja");
            int V = entrada.nextInt();
            switch (V) {
                case 1:
                    try {
                        Scrapper scrapper = new Scrapper();
                        scrapper.sacarStatsArmas();
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        Scrapper scrapper = new Scrapper();
                        scrapper.sacarllaves();

                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        Scrapper scrapper = new Scrapper();
                        scrapper.sacarCajas();
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        Scrapper scrapper = new Scrapper();
                        scrapper.scarSkins();
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }

    }
}