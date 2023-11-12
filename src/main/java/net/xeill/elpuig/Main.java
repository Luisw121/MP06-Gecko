package net.xeill.elpuig;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        try {
            Scrapper scrapper = new Scrapper();
            int a = 0;

            scrapper.sacarStatsArmas();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
