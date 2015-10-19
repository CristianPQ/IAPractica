package iapractica;

public class Viaje {
    //public int id; viene dado por el origen dest1 y dest2, em principio no hace falta un I
    private int origenx;
    private int origeny;
    private int dest1x;
    private int dest1y;
    private int dest2x;
    private int dest2y;
    private int NBDest1 = 0;
    private int NBDest2 = 0;
    
    public Viaje(int origenx, int origeny, int dest1x, int dest1y, int dest2x, int dest2y) {
        this.origenx = origenx;
        this.origeny = origeny;
        this.dest1x = dest1x;
        this.dest1y = dest2y;
        this.dest2x = dest2x;
        this.dest2y = dest2y;
    }
    
    public int getOrigenx() {
        return origenx;
    }
    
    public int getOrigeny() {
        return origeny;
    }
    
    public int getDest1x() {
        return dest1x;
    }
    
    public int getDest1y() {
        return dest1y;
    }
    
    public int getDest2x() {
        return dest2x;
    }
    
    public int getDest2y() {
        return dest2y;
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
    
    public void setOrigenx(int orig) {
        origenx = orig;
    }
    
    public void setOrigeny(int orig) {
        origeny = orig;
    }
    
    public void setDest1x(int dest) {
        dest1x = dest;
    }
    
    
    public void setDest1y(int dest) {
        dest1y = dest;
    }
    
    
    public void setDest2x(int dest) {
        dest2x = dest;
    }
    
    public void setDest2y(int dest) {
         dest2y = dest;
    }
    
    public void setNBDest1(int b) {
        NBDest1 = b;
    }
    
    public void setNBDest2(int b) {
        NBDest2 = b;
    }
    
    public int getBeneficioHoy() {
        return 0;
    }
    
    public int getBeneficioManana() {
        return 0;
    }
}
