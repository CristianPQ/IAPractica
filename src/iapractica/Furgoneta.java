package iapractica;

import java.util.ArrayList;

public class Furgoneta {

    private int id;
    private int coordx;
    private int coordy;
    private int Nbicis;
    //es al reves, un viaje viene dado por una furgoneta ademas de los destinos y el origen
    //private ArrayList<Viaje> viajes;
    private int kilometrosRecorridos = 0;
    
    public Furgoneta(int id) {
        this.id = id;
    }

    public void Furgoneta(int id, int coordx, int coordy, int Nbicis) {
        this.id = id;
        this.coordx = coordx;
        this.coordy = coordy;
        this.Nbicis = Nbicis;

    }

    public int getId() {
        return id;
    }

    public int getCoordx() {
        return coordx;
    }

    public int getCoordy() {
        return coordy;
    }

    public int getNbicis() {
        return Nbicis;
    }

    /*public ArrayList<Viaje> getViajes() {
        return viajes;
    }*/
    
    public int getKilometrosRecorridos() {
        return kilometrosRecorridos;
    }

    public void setCoordx(int x) {
        coordx = x;
    }

    public void setCoordy(int y) {
        coordy = y;
    }

    public void setNbicis(int bicis) {
        Nbicis = bicis;
    }
    /* Discutir: Como introducir viajes y como consultarlos.
     public void setViaje(int origen, int dest1, int dest2) {
      
     }
   
     */
    
    public void addKilometrosRecorridos(int kilometrosRecorridos) {
        this.kilometrosRecorridos += kilometrosRecorridos;
    }
}
