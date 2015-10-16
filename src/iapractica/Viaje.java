package iapractica;

public class Viaje {
    //public int id; viene dado por el origen dest1 y dest2, em principio no hace falta un ID
    
    private int origen;
    private int dest1;
    private int dest2;
    private int NBDest1;
    private int NBDest2;
    
    public Viaje(int origen, int dest1, int dest2) {
        this.origen = origen;
        this.dest1 = dest1;
        this.dest2 = dest2;
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
    
    public int getNBDest1() {
        return NBDest1;
    }
    
    public int getNBDest2(){
        return NBDest2;
    }
    
    public int getCosteTramo1(){
        int coste = (NBDest1+NBDest2+9)/10;
        return coste;
    }
    
    public int getCosteTramo2() {
        int coste = (NBDest2+9)/10;
        return coste;
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
