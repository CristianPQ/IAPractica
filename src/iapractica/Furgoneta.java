
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jose
 */
public class Furgoneta {
   private int id;
   private int coordx;
   private int coordy;
   private int Nbicis;
   private ArrayList<Viaje> viajes;
   
   public void Furgoneta(int id, int coordx, int coordy, int Nbicis){
       this.id = id;
       this.coordx = coordx;
       this.coordy = coordy;
       this.Nbicis = Nbicis;
       
   }
   
   public int getId(){
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
}
