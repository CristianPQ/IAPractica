package iapractica;

public class Viaje {
    //public int id; viene dado por el origen dest1 y dest2, em principio no hace falta un ID
    
    //que es esto?
    public int NBsol;
    public int origen;
    public int dest1;
    public int dest2;
    
    //modif Cristian
    public int furgoneta;
    
    //Porque void? --> es la constructora
    public Viaje(/*int id, */int NBsol, int origen, int dest1, int dest2) {
        //this.id = id;
        this.NBsol = NBsol;
        this.origen = origen;
        this.dest1 = dest1;
        this.dest2 = dest2;
    }
    
    /*public int getId() {
        return id;
    }*/
    
    public int getIDFurgoneta(){
        return furgoneta;
    }
    
    public int getNBsol() {
        return NBsol;
    }
    
    public int getOrigen() {
        return origen;
    }
    
    public int getDest1() {
        return dest1;
    }
    
    public int getDest2() {
        return dest2;
    }
    
    public void setNBsol(int sol) {
        NBsol = sol;
    }
    
    public void setOrigen(int orig) {
        origen = orig;
    }
    
    public void setDest1(int dest) {
        dest1 = dest;
    }
    
    public void setDest2(int dest) {
         dest2 = dest;
    }
    
    public int getBeneficioHoy() {
        return 0;
    }
    
    public int getBeneficioManana() {
        return 0;
    }
}
