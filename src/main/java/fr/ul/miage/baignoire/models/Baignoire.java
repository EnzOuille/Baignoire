package fr.ul.miage.baignoire.models;

import java.util.ArrayList;

public class Baignoire {

    private int volume;
    private int volumeActuel;
    private int conso;
    private int debordage;
    private int fuite;
    private ArrayList<Trou> trous;
    private Robinet robinet;
    private int nbTrous;

    public Baignoire(int volume, int remplissage, int nbTrous){
        this.volume = volume;
        this.robinet = new Robinet(remplissage);
        this.nbTrous = nbTrous;
    }

    public void ajouterTrou(int debit){
        if(this.trous == null){
            this.trous = new ArrayList<>();
        }
        this.trous.add(new Trou(debit));
    }

    public void viderBaignoire(){
        this.trous.forEach(x -> {
            this.diminuerVolume(x.getDebit());
        });
    }

    public void diminuerVolume(int i ){
        if (this.volumeActuel - i <= 0){
            int reste = i - this.volumeActuel;
            this.volumeActuel = 0;
            this.fuite += reste;
        }else{
            this.volumeActuel -= i;
            this.fuite += i;
        }
    }

    public void augmenterVolume(){
        int i = this.robinet.getDebit();
        this.conso += i;
        if (this.volumeActuel + i >= this.volume){
            int reste = i - (this.volume - this.volumeActuel);
            this.volumeActuel += i - reste;
            this.debordage += reste;
        }else{
            this.volumeActuel += i;
        }
    }

    public int getVolume(){
        return this.volume;
    }

    public int getVolumeActuel(){
        return this.volumeActuel;
    }

    public int getConso(){
        return this.conso;
    }

    public int getDebordage(){
        return this.debordage;
    }

    public int getFuite(){
        return this.fuite;
    }

    public int getNbTrous(){
        return this.nbTrous;
    }
}
