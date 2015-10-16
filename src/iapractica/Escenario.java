
package iapractica;
//PRUEBA GITHUB
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import IA.Bicing.Estacion;
import IA.Bicing.Estaciones;
import aima.search.framework.Successor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

public class Escenario {

    private static int NUMEROMAXIMOVIAJES = 2;
    private static int NMAXBICISFURGONETA = 30;
    private static int nEstaciones;
    private static int nBicicletas; // > nEstaciones*50
    private static int nFurgonetas;
    private Estaciones estaciones;
    private TreeMap<Integer, Integer> estacionesSinDemanda;
    private TreeMap<Integer, Integer> estacionesConDemanda;
    //id furgos = pos en Array+1
    private ArrayList<Furgoneta> furgonetas;
    private ArrayList<Viaje> viajes;
    private Random r = new Random();

    /**
     * Constructor de la representacion del estado propuesto, con semilla
     * aleatoria
     *
     * 
     * 
     * @param e Numero de estaciones
     * @param b Numero de bicicletas
     * @param f Numero de furgonetas
     * @param dem Tipo de demanda
     */
    public Escenario(int e, int b, int dem, int f) {
        int seed = r.nextInt(100);
        nEstaciones = e;
        nBicicletas = b;
        nFurgonetas = f;
        furgonetas = new ArrayList();
        viajes = new ArrayList();

        estaciones = new Estaciones(e, b, dem, seed);
        
        for (int i = 0; i < estaciones.size(); i++) {
        
        int demanda = estaciones.get(i).getDemanda();
        estacionesSinDemanda = new HashMap<String, Integer>();
        }
        int prueba = 1;
        
        for (int i = 0; i < f; i++) {
            furgonetas.add(new Furgoneta(i+1));
        }
        
        
        
    }

    /**
     * Constructor de la representacion del estado propuesto, con semilla dada
     *
     * @param e Numero de estaciones
     * @param b Numero de bicicletas
     * @param f Numero de furgonetas
     */
    public Escenario(int e, int b, int f, int dem,int seed) {

        nEstaciones = e;
        nBicicletas = b;
        nFurgonetas = f;
        furgonetas = new ArrayList();
        viajes = new ArrayList();

        Estaciones estacionesGeneradas = new Estaciones(e, b, dem, seed);

        for (int i = 0; i < f; i++) {
            furgonetas.add(new Furgoneta(i+1));
        }
    }

    public Escenario(Escenario clone) {
        nEstaciones = clone.getnEstaciones();
        estaciones = clone.getEstaciones();
        /*for (Gasolinera g : clone.getGasolineras()) {
         gasolineras.add(new Gasolinera(g.getId(), g.getX(), g.getY()));
         }*/
        //viajes = new ArrayList(clone.getViajes().size());
        //for (Viaje v : clone.getViajes()) {
        //    viajes.add(new Viaje(/*v.getId(), */v.getNBsol(), v.getOrigen(), v.getDest1(), v.getDest2()));
        //}
        //furgonetas = new ArrayList(clone.getFurgonetas().size());
        //for (Furgoneta f : clone.getFurgonetas()) {
            /*ArrayList<Viaje> newViajes = new int[5][2];
            ArrayList<Viaje> oldViajes = f.getViajes();
            for (int i = 0; i < oldViajes.length; i++) {
                newViajes[i][0] = oldViajes[i][0];
                newViajes[i][1] = oldViajes[i][1];
            }*/
            //furgonetas.add(new Furgoneta(/*c.getId(), c.getX(), c.getY(), newViajes, c.getKilometrosRecorridos()*/));
        //}
    }

    //Falta implementar
        public Boolean anadirViajeFurgoneta(int idFurgoneta, int cap) {
        
            return true;
            
        }
    
    public int heuristicValue(int z) {

        // Cambiar formula
        
        int beneficio = 0;
        switch (z) {
         case 0:
             
         //que hace exactamente este for?
         for (Viaje v : viajes) {
            if (v.getIDFurgoneta() != -1) {
                beneficio += v.getBeneficioHoy();
            } else {
                beneficio -= (v.getBeneficioHoy() - v.getBeneficioManana());
            }
         }
         return -1 * (int) Math.round(beneficio - (getKilometros()/* * PRECIOKILOMETRO*/));

         case 1:
         for (Viaje v : viajes) {
            if (v.getIDFurgoneta() != -1) {
                beneficio += v.getBeneficioHoy() - v.getBeneficioManana();
            }
         }
         return -1 * (int) Math.round(0.9 * beneficio - 0.1 * (getKilometros() /** PRECIOKILOMETRO*/));
         default:
         return 500;
         }
    }

    /**
     * Devuelve todos los sucesores usando solo el operador de intercambiar (de
     * momento creo que es suficiente, ya que podemos intercambiar los camiones
     * entre dos peticiones aunque no tengan ningún camión asignado).
     *
     * @return Lista de sucesores (DGBoards)
     */
    public ArrayList getTodosSucesores() {
        ArrayList ret = new ArrayList();
        for (int i = 0; i < viajes.size(); i++) {
            for (int j = i + 1; j < viajes.size(); j++) {
                Escenario e = new Escenario(this);
                e.swapFurgonetas(i, j);
                int idFurgonetaI = viajes.get(i).getIdFurgoneta();
                int idFurgonetaJ = viajes.get(j).getIdFurgoneta();
                Boolean valido = true;
                if (idFurgonetaI != -1) {
                    valido &= furgonetas.get(idFurgonetaI).getKilometrosRecorridos() <= NKILOMETROSDIAS;
                }
                if (idFurgonetaJ != -1) {
                    valido &= furgonetas.get(idFurgonetaJ).getKilometrosRecorridos() <= NKILOMETROSDIAS;
                }
                if (valido) {
                    ret.add(new Successor("Intercambiadas " + i + " y " + j + " H:" + e.heuristicValue(1), e));
                }
            }
        }

        for (int i = 0; i < viajes.size(); i++) {
            if (viajes.get(i).getIdFurgoneta() == -1) {
                for (int k = 0; k < furgonetas.size(); k++) {
                    Escenario b = new Escenario(this);
                    if (b.addPeticionACamionFirstPlace(b.furgonetas.get(k).getId(), b.viajes.get(i).getIdViaje(), NKILOMETROSDIAS)) {
                        ret.add(new Successor("Añadida peticion " + i + " a camion" + b.furgonetas.get(k).getId() + "H:" + b.heuristicValue(1), b));
                    }

                }

            }

        }

        return ret;
    }
    
        public ArrayList getSucesorAleatorio() {
        ArrayList<Successor> ret = new ArrayList();

        int i = r.nextInt(viajes.size());
        int j = 0;
        // Por si acaso solo hubiese una petición
        if (viajes.size() > 1) {
            do {
                j = r.nextInt(viajes.size());
            } while (i == j);
        }

        Escenario b = new Escenario(this);
        b.swapFurrgonetas(i, j);
        ret.add(new Successor("Intercambiadas " + i + " y " + j, b));

        Escenario addBoard = new Escenario(this);
        if (viajes.size() > 1) {
            int peticion = 0;
            boolean peticionValida = false;
            for (int k = 0; k < 1000; k++) {
                peticion = r.nextInt(viajes.size());
                if (viajes.get(peticion).getIdFurgoneta() == -1) {
                    peticionValida = true;
                    break;
                }
            }
            if (peticionValida) {
                boolean colocada = false;
                int camion = 0;
                for (int k = 0; k < 1000; k++) {
                    camion = r.nextInt(furgonetas.size());
                    colocada = addBoard.addPeticionACamionFirstPlace(furgonetas.get(camion).getId(), viajes.get(peticion).getIdViaje(), NKILOMETROSDIAS);
                    if (colocada) {
                        break;
                    }
                }
                if (colocada) {
                    ret.add(new Successor("Añadida peticion " + peticion + " a camion" + addBoard.furgonetas.get(camion).getId() + "H:" + addBoard.heuristicValue(1), addBoard));
                }
            }
        }

        if (ret.size() == 2) {
            ret.remove(r.nextInt(2));
        }

        return ret;
    }
    
    public boolean isGoalState() {
        return (false);
    }

    public int getKilometros() {
        int kilometros = 0;
        for (Furgoneta f : furgonetas) {
            kilometros += f.getKilometrosRecorridos();
        }
        return kilometros;
    }

        public static int getNUMEROMAXIMOVIAJES() {
        return NUMEROMAXIMOVIAJES;
    }

    /*public static int getNMAXBICISFURGONETA() {
        return NMAXBICISFURGONETA;
    }*/
    public int getnEstaciones() {
        return nEstaciones;
    }

    public Estaciones getEstaciones() {
        return estaciones;
    }

    public ArrayList<Furgoneta> getFurgonetas() {
        return furgonetas;
    }

    public ArrayList<Viaje> getViajes() {
        return viajes;
    }
    
    // No se usan de momento:
    
        public int getPeticionesServidas() {
        int peticionesServidas = 0;
        for (Viaje peticion : viajes) {
            if (peticion.getIdFurgoneta() != -1) {
                peticionesServidas++;
            }
        }
        return peticionesServidas;
    }

    public int getPeticionesNoServidas() {
        int peticionesServidas = 0;
        for (Viaje peticion : viajes) {
            if (peticion.getIdFurgoneta() == -1) {
                peticionesServidas++;
            }
        }
        return peticionesServidas;
    }
    
       public double calcularBeneficio() {
        double beneficio = 0;
        for (Viaje v : viajes) {
            if (v.getIdFurgoneta() != -1) {
                beneficio += v.getBeneficioHoy();
            }
        }
        return beneficio - (getKilometros() * PRECIOKILOMETRO);
    }

    /**
     * Revisar si la formula está correcta
     */
    private int calcDistancia(int x1, int y1, int x2, int y2) {
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }
    
    public int CosteViaje(Viaje v) {
        int ox = v.getOrigenx();
        int oy = v.getOrigeny();
        int d1x = v.getDest1x();
        int d1y = v.getDest1y();
        int result = Math.abs(ox-d1x) + Math.abs(oy-d1y);
        result = result*v.getCosteTramo1();
        int d2x = v.getDest2x();
        int d2y = v.getDest2y();
        int aux = Math.abs(d1x-d2x) + Math.abs(d1y-d2y);
        aux = aux*v.getCosteTramo2();
        return result + aux;
    }
    
    public int CosteEstacion(Estacion e) {
        int x = e.getCoordX();
        int y = e.getCoordY();
        int bt = 0;
        for (Viaje v : viajes) {
            if (v.getDest1x() == x && v.getDest1y() == y) bt = v.getNBDest1();
            else if (v.getDest2x() == x && v.getDest2y()== y) bt = v.getNBDest2();
        }
        
    }

}