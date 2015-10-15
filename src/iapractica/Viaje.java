package iapractica;

public class Viaje {
    public int id;
    public int NBsol;
    public int origen;
    public int dest1;
    public int dest2;
    
    //Porque void?
    public Viaje(int id, int NBsol, int origen, int dest1, int dest2) {
        this.id = id;
        this.NBsol = NBsol;
        this.origen = origen;
        this.dest1 = dest1;
        this.dest2 = dest2;
    }
    
    public int getId() {
        return id;
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
}
