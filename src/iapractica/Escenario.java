<<<<<<< HEAD
package iapractica;
//PRUEBA GITHUB
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import IA.Bicing.Estacion;
import IA.Bicing.Estaciones;
import static aima.basic.Util.max;
import aima.search.framework.Successor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Escenario {

    private static int NUMEROMAXIMOVIAJES = 2;
    private static int NMAXBICISFURGONETA = 30;
    private static int nEstaciones;
    private static int nBicicletas; // > nEstaciones*50
    private static int nFurgonetas;
    private Estaciones estaciones;
    //private TreeMap<Integer, Integer> estacionesSinDemanda;
    //private TreeMap<Integer, Integer> estacionesConDemanda;
    private Map<Integer, Integer> estacionesSinDemanda;
    private Map<Integer, Integer> estacionesConDemanda;
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

        estacionesConDemanda = new HashMap<Integer, Integer>();
        estacionesSinDemanda = new HashMap<Integer, Integer>();

        for (int i = 0; i < estaciones.size(); i++) {

            int actuales = estaciones.get(i).getNumBicicletasNoUsadas();
            int next = estaciones.get(i).getNumBicicletasNext();
            int demanda = estaciones.get(i).getDemanda();

            int faltan = (demanda - next);

            if (faltan > 0) {
                estacionesConDemanda.put(new Integer(i), new Integer(faltan));
            } else {

                int disponibles = actuales - (demanda - (next - actuales));

                if (disponibles > 0) {
                    estacionesSinDemanda.put(new Integer(i), new Integer(disponibles));
                }
            }
        }

    }

    /**
     * Constructor de la representacion del estado propuesto, con semilla dada
     *
     * @param e Numero de estaciones
     * @param b Numero de bicicletas
     * @param f Numero de furgonetas
     */
    public Escenario(int e, int b, int f, int dem, int seed) {

        nEstaciones = e;
        nBicicletas = b;
        nFurgonetas = f;
        furgonetas = new ArrayList();
        viajes = new ArrayList();

        estaciones = new Estaciones(e, b, dem, seed);

        HashMap tmpConDemanda = new HashMap();
        HashMap tmpSinDemanda = new HashMap();

        ValueComparator bvc = new ValueComparator(tmpConDemanda);
        ValueComparator bvc2 = new ValueComparator(tmpSinDemanda);

        for (int i = 0; i < estaciones.size(); i++) {

            int actuales = estaciones.get(i).getNumBicicletasNoUsadas();
            int next = estaciones.get(i).getNumBicicletasNext();
            int demanda = estaciones.get(i).getDemanda();

            int faltan = (demanda - next);

            if (faltan > 0) {
                tmpConDemanda.put(new Integer(i), new Integer(faltan));
            } else {

                int disponibles = actuales - (demanda - (next - actuales));

                if (disponibles > 0) {
                    tmpSinDemanda.put(new Integer(i), new Integer(disponibles));
                }
            }
        }

        estacionesSinDemanda = new TreeMap(bvc);
        estacionesConDemanda = new TreeMap(bvc2);

        estacionesSinDemanda.putAll(tmpSinDemanda);
        estacionesConDemanda.putAll(tmpConDemanda);

    }

    public Escenario(Escenario clone) {
        nEstaciones = clone.getnEstaciones();
        nBicicletas = clone.getnBicicletas();
        nFurgonetas = clone.getnFurgonetas();
        estaciones = clone.getEstaciones();
        for (Estacion e : clone.getEstaciones()) {
            Estacion j = new Estacion(e.getCoordX(), e.getCoordY());
            j.setNumBicicletasNoUsadas(e.getNumBicicletasNoUsadas());
            j.setNumBicicletasNext(e.getNumBicicletasNext());
            j.setDemanda(e.getDemanda());
            estaciones.add(j);
        }
        estacionesSinDemanda = clone.getE
        
    }

    public void generarEstadoInicialVacio() {

    }

    /*
    * Crea viajes con origen según el orden de las estaciones de la colección
    * "estaciones" no ordenado. Añade el primer destino de forma aleatoria y el
    * segundo lo deja vacío
    */
    
    public void generarEstadoInicialRandom() {

        int count = 0;

        for (Map.Entry<Integer, Integer> entryCon : estacionesConDemanda.entrySet()) {

            Estacion estacionOrigen = estaciones.get(entryCon.getKey());

            int destTmp = new Random().nextInt(estacionesSinDemanda.size());
            Estacion estacionDest = null;
            int count2 = 0;

            for (Map.Entry<Integer, Integer> entrySin : estacionesSinDemanda.entrySet()) {

                if (destTmp == count2) {
                    estacionDest = estaciones.get(entrySin.getKey());
                }
                count2++;
            }

            Viaje nuevoViaje = new Viaje(estacionOrigen.getCoordX(), estacionOrigen.getCoordY(), estacionDest.getCoordX(), estacionDest.getCoordY(), 0, 0);

            nuevoViaje.setNBDest1(entryCon.getValue());

            viajes.add(nuevoViaje);

            count++;
            if (nFurgonetas < count) {
                break;
            }
        }

    }
    
    /*
    * Crea viajes con origen en las "nFurgonetas" estaciones con más demanda
    * y asigna el primer destino a la estación con demanda más cercana.
    */

    public void generarEstadoInicialLogico() {

        estacionesConDemanda = sortByComparator(estacionesConDemanda, false);
        
        int count = 0;

        for (Map.Entry<Integer, Integer> entryCon : estacionesConDemanda.entrySet()) {

            Estacion estacionOrigen = estaciones.get(entryCon.getKey());

            int minDist = 9999999;
            Estacion estacionDest = null;

            for (Map.Entry<Integer, Integer> entrySin : estacionesSinDemanda.entrySet()) {

                Estacion estacionDesttmp = estaciones.get(entrySin.getKey());

                int dist = calcDistancia(estacionOrigen.getCoordX(), estacionDesttmp.getCoordX(), estacionOrigen.getCoordY(), estacionDesttmp.getCoordY());

                if (minDist > dist) {
                    estacionDest = estaciones.get(entrySin.getKey());
                }
            }

            Viaje nuevoViaje = new Viaje(estacionOrigen.getCoordX(), estacionOrigen.getCoordY(), estacionDest.getCoordX(), estacionDest.getCoordY(), 0, 0);

            nuevoViaje.setNBDest1(entryCon.getValue());

            viajes.add(nuevoViaje);

            count++;
            if (nFurgonetas < count) {
                break;
            }
        }

    }

    //Falta implementar
    public Boolean anadirViajeFurgoneta(int idFurgoneta, int cap) {

        return true;

    }

    public void operadorOrigenesViajes(Viaje v, Estacion e) {
        //if(v.getOrigenx() != e.getCoordX() && v.getOrigeny() != e.getCoordY()) {
        if (e != null) {
            v.setOrigenx(e.getCoordX());
            v.setOrigeny(e.getCoordY());
        }
        else {
            v.setOrigenx(-1);
            v.setOrigeny(-1);
        }
    }

    public void operadorDestino1(Viaje v, Estacion e) {
        if (e != null) {
            if (v.getDest1x() != e.getCoordX() && v.getDest1y() != e.getCoordY()) {
                if (v.getDest2x() != e.getCoordX() && v.getDest2y() != e.getCoordY()) {
                    v.setDest1x(e.getCoordX());
                    v.setDest1y(e.getCoordY());
                }
            }
        }
        else {
            v.setDest1x(-1);
            v.setDest1y(-1);
        }
    }

    public void operadorDestino2(Viaje v, Estacion e) {
        if (e != null) {
            if (v.getDest2x() != e.getCoordX() && v.getDest2y() != e.getCoordY()) {
                if (v.getDest1x() != e.getCoordX() && v.getDest1y() != e.getCoordY()) {
                    v.setDest2x(e.getCoordX());
                    v.setDest2y(e.getCoordY());
                }
            }
        }
        else {
            v.setDest2x(-1);
            v.setDest2y(-1);
        }
    }

    public int valorHeuristico(int h) {
// Cambiar formula
        int beneficio = 0;
        switch (h) {
            case 0:
                for (Estacion e : estaciones) {
                    beneficio += beneficioEstacion(e);
                }
            case 1:
                for (Viaje v : viajes) {
                    beneficio += beneficioViaje(v);
                }
        }
        return beneficio;
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
    public int getnBicicletas() {
        return nBicicletas;
    }

    public int getnEstaciones() {
        return nEstaciones;
    }

    public Estaciones getEstaciones() {
        return estaciones;
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

    public int beneficioViaje(Viaje v) {
        int ox = v.getOrigenx();
        if(ox != -1) {
            int oy = v.getOrigeny();
            int d1x = v.getDest1x();
            int d1y = v.getDest1y();
            int result = Math.abs(ox - d1x) + Math.abs(oy - d1y);
            result = result * v.getCosteTramo1();
            int d2x = v.getDest2x();
            int d2y = v.getDest2y();
            int aux = Math.abs(d1x - d2x) + Math.abs(d1y - d2y);
            aux = aux * v.getCosteTramo2();
            return result + aux;
        }
        return 0;
    }

    public int beneficioEstacion(Estacion e) {
        int x = e.getCoordX();
        int y = e.getCoordY();
        int bt = 0;
        for (Viaje v : viajes) {
            if (v.getDest1x() == x && v.getDest1y() == y) {
                bt = v.getNBDest1();
            } else if (v.getDest2x() == x && v.getDest2y() == y) {
                bt = v.getNBDest2();
            }
        }
        return bt;
    }

    public int Beneficios() {
        int beneficios = 0;
        for (Viaje v : viajes) {
            beneficios = beneficioViaje(v);
        }
        for (Estacion e : estaciones) {
            beneficios += beneficioEstacion(e);
        }
        return beneficios;
    }

    private int getnFurgonetas() {
        return nFurgonetas;
    }

    public static class ValueComparator implements Comparator {

        Map base;

        public ValueComparator(Map base) {
            this.base = base;
        }

        @Override
        public int compare(Object a, Object b) {
            if ((Integer) base.get(a) >= (Integer) base.get(b)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
    
        private static Map<Integer, Integer> sortByComparator(Map<Integer, Integer> unsortMap, final boolean order)
    {

        List<Map.Entry<Integer, Integer>> list = new LinkedList<Map.Entry<Integer, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>()
        {
            public int compare(Map.Entry<Integer, Integer> o1,
                    Map.Entry<Integer, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public TreeMap<Integer, Integer> getEstacionesConDemanda() {
        return estacionesConDemanda;
    }

    public TreeMap<Integer, Integer> getEstacionesSinDemanda() {
        return estacionesSinDemanda;
    }

}
=======
package iapractica;
//PRUEBA GITHUB
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import IA.Bicing.Estacion;
import IA.Bicing.Estaciones;
import static aima.basic.Util.max;
import aima.search.framework.Successor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Escenario {

    private static int NUMEROMAXIMOVIAJES = 2;
    private static int NMAXBICISFURGONETA = 30;
    private static int nEstaciones;
    private static int nBicicletas; // > nEstaciones*50
    private static int nFurgonetas;
    private Estaciones estaciones;
    //private TreeMap<Integer, Integer> estacionesSinDemanda;
    //private TreeMap<Integer, Integer> estacionesConDemanda;
    private Map<Integer, Integer> estacionesSinDemanda;
    private Map<Integer, Integer> estacionesConDemanda;
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

        estacionesConDemanda = new HashMap<Integer, Integer>();
        estacionesSinDemanda = new HashMap<Integer, Integer>();

        for (int i = 0; i < estaciones.size(); i++) {

            int actuales = estaciones.get(i).getNumBicicletasNoUsadas();
            int next = estaciones.get(i).getNumBicicletasNext();
            int demanda = estaciones.get(i).getDemanda();

            int faltan = (demanda - next);

            if (faltan > 0) {
                estacionesConDemanda.put(new Integer(i), new Integer(faltan));
            } else {

                int disponibles = actuales - (demanda - (next - actuales));

                if (disponibles > 0) {
                    estacionesSinDemanda.put(new Integer(i), new Integer(disponibles));
                }
            }
        }

    }

    /**
     * Constructor de la representacion del estado propuesto, con semilla dada
     *
     * @param e Numero de estaciones
     * @param b Numero de bicicletas
     * @param f Numero de furgonetas
     */
    public Escenario(int e, int b, int f, int dem, int seed) {

        nEstaciones = e;
        nBicicletas = b;
        nFurgonetas = f;
        furgonetas = new ArrayList();
        viajes = new ArrayList();

        estaciones = new Estaciones(e, b, dem, seed);

        HashMap tmpConDemanda = new HashMap();
        HashMap tmpSinDemanda = new HashMap();

        ValueComparator bvc = new ValueComparator(tmpConDemanda);
        ValueComparator bvc2 = new ValueComparator(tmpSinDemanda);

        for (int i = 0; i < estaciones.size(); i++) {

            int actuales = estaciones.get(i).getNumBicicletasNoUsadas();
            int next = estaciones.get(i).getNumBicicletasNext();
            int demanda = estaciones.get(i).getDemanda();

            int faltan = (demanda - next);

            if (faltan > 0) {
                tmpConDemanda.put(new Integer(i), new Integer(faltan));
            } else {

                int disponibles = actuales - (demanda - (next - actuales));

                if (disponibles > 0) {
                    tmpSinDemanda.put(new Integer(i), new Integer(disponibles));
                }
            }
        }

        estacionesSinDemanda = new TreeMap(bvc);
        estacionesConDemanda = new TreeMap(bvc2);

        estacionesSinDemanda.putAll(tmpSinDemanda);
        estacionesConDemanda.putAll(tmpConDemanda);

    }

    public Escenario(Escenario clone) {
        nEstaciones = clone.getnEstaciones();
        nBicicletas = clone.getnBicicletas();
        nFurgonetas = clone.getnFurgonetas();
        estaciones = clone.getEstaciones();
        for (Estacion e : clone.getEstaciones()) {
            Estacion j = new Estacion(e.getCoordX(), e.getCoordY());
            j.setNumBicicletasNoUsadas(e.getNumBicicletasNoUsadas());
            j.setNumBicicletasNext(e.getNumBicicletasNext());
            j.setDemanda(e.getDemanda());
            estaciones.add(j);
        }
        estacionesSinDemanda = clone.getE
        
    }

    public void generarEstadoInicialVacio() {

    }

    /*
    * Crea viajes con origen según el orden de las estaciones de la colección
    * "estaciones" no ordenado. Añade el primer destino de forma aleatoria y el
    * segundo lo deja vacío
    */
    
    public void generarEstadoInicialRandom() {

        int count = 0;

        for (Map.Entry<Integer, Integer> entryCon : estacionesConDemanda.entrySet()) {

            Estacion estacionOrigen = estaciones.get(entryCon.getKey());

            int destTmp = new Random().nextInt(estacionesSinDemanda.size());
            Estacion estacionDest = null;
            int count2 = 0;

            for (Map.Entry<Integer, Integer> entrySin : estacionesSinDemanda.entrySet()) {

                if (destTmp == count2) {
                    estacionDest = estaciones.get(entrySin.getKey());
                }
                count2++;
            }

            Viaje nuevoViaje = new Viaje(estacionOrigen.getCoordX(), estacionOrigen.getCoordY(), estacionDest.getCoordX(), estacionDest.getCoordY(), 0, 0);

            nuevoViaje.setNBDest1(entryCon.getValue());

            viajes.add(nuevoViaje);

            count++;
            if (nFurgonetas < count) {
                break;
            }
        }

    }
    
    /*
    * Crea viajes con origen en las "nFurgonetas" estaciones con más demanda
    * y asigna el primer destino a la estación con demanda más cercana.
    */

    public void generarEstadoInicialLogico() {

        estacionesConDemanda = sortByComparator(estacionesConDemanda, false);
        
        int count = 0;

        for (Map.Entry<Integer, Integer> entryCon : estacionesConDemanda.entrySet()) {

            Estacion estacionOrigen = estaciones.get(entryCon.getKey());

            int minDist = 9999999;
            Estacion estacionDest = null;

            for (Map.Entry<Integer, Integer> entrySin : estacionesSinDemanda.entrySet()) {

                Estacion estacionDesttmp = estaciones.get(entrySin.getKey());

                int dist = calcDistancia(estacionOrigen.getCoordX(), estacionDesttmp.getCoordX(), estacionOrigen.getCoordY(), estacionDesttmp.getCoordY());

                if (minDist > dist) {
                    estacionDest = estaciones.get(entrySin.getKey());
                }
            }

            Viaje nuevoViaje = new Viaje(estacionOrigen.getCoordX(), estacionOrigen.getCoordY(), estacionDest.getCoordX(), estacionDest.getCoordY(), 0, 0);

            nuevoViaje.setNBDest1(entryCon.getValue());

            viajes.add(nuevoViaje);

            count++;
            if (nFurgonetas < count) {
                break;
            }
        }

    }

    //Falta implementar
    public Boolean anadirViajeFurgoneta(int idFurgoneta, int cap) {

        return true;

    }

    public void operadorOrigenesViajes(Viaje v, Estacion e) {
        //if(v.getOrigenx() != e.getCoordX() && v.getOrigeny() != e.getCoordY()) {
        v.setOrigenx(e.getCoordX());
        v.setOrigeny(e.getCoordY());
        //}
    }

    public void operadorDestino1(Viaje v, Estacion e) {
        if (v.getDest1x() != e.getCoordX() && v.getDest1y() != e.getCoordY()) {
            if (v.getDest2x() != e.getCoordX() && v.getDest2y() != e.getCoordY()) {
                v.setDest1x(e.getCoordX());
                v.setDest1y(e.getCoordY());
            }
        }
    }

    public void operadorDestino2(Viaje v, Estacion e) {
        if (v.getDest2x() != e.getCoordX() && v.getDest2y() != e.getCoordY()) {
            if (v.getDest1x() != e.getCoordX() && v.getDest1y() != e.getCoordY()) {
                v.setDest2x(e.getCoordX());
                v.setDest2y(e.getCoordY());
            }
        }
    }

    public int valorHeuristico(int h) {
// Cambiar formula
        int beneficio = 0;
        switch (h) {
            case 0:
                for (Estacion e : estaciones) {
                    beneficio += beneficioEstacion(e);
                }
            case 1:
                for (Viaje v : viajes) {
                    beneficio += beneficioViaje(v);
                }
        }
        return beneficio;
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
    public int getnBicicletas() {
        return nBicicletas;
    }

    public int getnEstaciones() {
        return nEstaciones;
    }

    public Estaciones getEstaciones() {
        return estaciones;
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

    public int beneficioViaje(Viaje v) {
        int ox = v.getOrigenx();
        int oy = v.getOrigeny();
        int d1x = v.getDest1x();
        int d1y = v.getDest1y();
        int result = Math.abs(ox - d1x) + Math.abs(oy - d1y);
        result = result * v.getCosteTramo1();
        int d2x = v.getDest2x();
        int d2y = v.getDest2y();
        int aux = Math.abs(d1x - d2x) + Math.abs(d1y - d2y);
        aux = aux * v.getCosteTramo2();
        return result + aux;
    }

    public int beneficioEstacion(Estacion e) {
        int x = e.getCoordX();
        int y = e.getCoordY();
        int bt = 0;
        for (Viaje v : viajes) {
            if (v.getDest1x() == x && v.getDest1y() == y) {
                bt = v.getNBDest1();
            } else if (v.getDest2x() == x && v.getDest2y() == y) {
                bt = v.getNBDest2();
            }
        }
        return bt;
    }

    public int Beneficios() {
        int beneficios = 0;
        for (Viaje v : viajes) {
            beneficios = beneficioViaje(v);
        }
        for (Estacion e : estaciones) {
            beneficios += beneficioEstacion(e);
        }
        return beneficios;
    }

    private int getnFurgonetas() {
        return nFurgonetas;
    }

    public static class ValueComparator implements Comparator {

        Map base;

        public ValueComparator(Map base) {
            this.base = base;
        }

        @Override
        public int compare(Object a, Object b) {
            if ((Integer) base.get(a) >= (Integer) base.get(b)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
    
        private static Map<Integer, Integer> sortByComparator(Map<Integer, Integer> unsortMap, final boolean order)
    {

        List<Map.Entry<Integer, Integer>> list = new LinkedList<Map.Entry<Integer, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>()
        {
            public int compare(Map.Entry<Integer, Integer> o1,
                    Map.Entry<Integer, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public TreeMap<Integer, Integer> getEstacionesConDemanda() {
        return estacionesConDemanda;
    }

    public TreeMap<Integer, Integer> getEstacionesSinDemanda() {
        return estacionesSinDemanda;
    }

}
>>>>>>> origin/master
