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
import static java.lang.Integer.min;
import java.util.ArrayList;
;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;



public class Escenario {

    private static int NUMEROMAXIMOVIAJES = 2;
    private static int NMAXBICISFURGONETA = 30;
    private static int nEstaciones;
    private static int nBicicletas; // > nEstaciones*50
    private static int nFurgonetas;
    private static Estaciones estaciones;
    private Map<Integer, Integer> estacionesSinDemanda; //estaciones origen
    private ArrayList<Boolean> estacionesDisponibilidad;
    private Map<Integer, Integer> estacionesConDemanda; //estaciones destino
    //id furgos = pos en Array+1
    //private ArrayList<Furgoneta> furgonetas;
    private ArrayList<Viaje> viajes;
    private Random r = new Random();

    /**
     * Constructor de la representacion del estado propuesto
     *
     * @param e Numero de estaciones
     * @param b Numero de bicicletas
     * @param f Numero de furgonetas
     * @param dem Tipo de demanda
     * @param seed Semilla
     */
    public Escenario(int e, int b, int f, int dem, int seed) {

        nEstaciones = e;
        nBicicletas = b;
        nFurgonetas = f;

        viajes = new ArrayList();
        estaciones = new Estaciones(e, b, dem, seed);
        estacionesDisponibilidad = new ArrayList(e);
        for (int m = 0; m < e; ++m) {
            estacionesDisponibilidad.add(m, Boolean.TRUE);
        }

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

                int disponibles = (next - demanda);
                disponibles = min(disponibles, actuales);

                if (disponibles > 0) {
                    estacionesSinDemanda.put(new Integer(i), new Integer(disponibles));
                }
            }
        }
    }

    public ArrayList<Boolean> getEstacionesDisponibilidad() {
        return estacionesDisponibilidad;
    }

    public Escenario(Escenario clone) {
        nEstaciones = clone.getnEstaciones();
        nBicicletas = clone.getnBicicletas();
        nFurgonetas = clone.getnFurgonetas();
        estaciones = clone.getEstaciones();

        estacionesSinDemanda = new HashMap<Integer, Integer>(clone.getEstacionesSinDemanda());

        estacionesDisponibilidad = new ArrayList(clone.getEstacionesDisponibilidad());
        System.out.println("tamaño estacioensDiponibilidad: " + estacionesDisponibilidad.size());

        estacionesConDemanda = new HashMap<Integer, Integer>(clone.getEstacionesConDemanda());

        viajes = new ArrayList<Viaje>(clone.getViajes());

    }

    public void generarEstadoInicialVacio() {
        int count = 0;
        while (count < estacionesSinDemanda.size() && count < nFurgonetas) {
            Viaje v = new Viaje(-1, -1, -1, -1, -1, -1);
            viajes.add(v);
            ++count;
        }
    }

    /*
     * Crea viajes con origen según el orden de las estaciones de la colección
     * "estaciones" no ordenado. Añade el primer destino de forma aleatoria y el
     * segundo lo deja vacío
     */
    public void generarEstadoInicialRandom() {
        int count = 0;

        for (Map.Entry<Integer, Integer> entrySin : estacionesSinDemanda.entrySet()) {

            Viaje v = new Viaje(-1, -1, -1, -1, -1, -1);
            asignarOrigen(v, entrySin.getKey());

            int destTmp = new Random().nextInt(estacionesConDemanda.size());
            int count2 = 0;

            for (Map.Entry<Integer, Integer> entryCon : estacionesConDemanda.entrySet()) {
                if (destTmp == count2) {
                    asignarDestino1(v, entryCon.getKey());
                }
                count2++;
            }

            viajes.add(v);

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

    public boolean op0(int v, int e) {
        return asignarOrigen(viajes.get(v), e);
    }

    public boolean op1(int v, int e) {
        return asignarDestino1(viajes.get(v), e);
    }

    public boolean op2(int v, int e) {
        return asignarDestino2(viajes.get(v), e);
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
            System.out.println("getTodosSucesores estacionesSinDemanda: " + estacionesSinDemanda.size());
            for (Map.Entry<Integer, Integer> est : estacionesSinDemanda.entrySet()) {
                Escenario b = new Escenario(this);
                if (b.op0(i, est.getKey())) {
                    ret.add(new Successor("Asignar Origen " + estaciones.get(est.getKey()) + " a viaje" + viajes.get(i) + "H:" + b.valorHeuristico(1), b));
                }
            }
            Escenario aux1 = new Escenario(this);
            aux1.op0(i, -1);
            ret.add(new Successor("Asignar Origen " + (-1) + " a viaje" + viajes.get(i) + "H:" + aux1.valorHeuristico(1), aux1));
            System.out.println("getTodosSucesores estacionesConDemanda: " + estacionesConDemanda.size());
            for (Map.Entry<Integer, Integer> est : estacionesConDemanda.entrySet()) {
                Escenario b = new Escenario(this);
                if (b.op1(i, est.getKey())) {
                    ret.add(new Successor("Asignar Destino1 " + estaciones.get(est.getKey()) + " a viaje" + viajes.get(i) + "H:" + b.valorHeuristico(1), b));
                }
            }
            Escenario aux2 = new Escenario(this);
            aux2.op2(i, -1);
            ret.add(new Successor("Asignar Destino1 " + (-1) + " a viaje" + viajes.get(i) + "H:" + aux2.valorHeuristico(1), aux2));

            System.out.println("getTodosSucesores estacionesConDemanda: " + estacionesConDemanda.size());
            for (Map.Entry<Integer, Integer> est : estacionesConDemanda.entrySet()) {
                Escenario b = new Escenario(this);
                if (b.op2(i, est.getKey())) {
                    ret.add(new Successor("Asignar Destino2 " + estaciones.get(est.getKey()) + " a viaje" + viajes.get(i) + "H:" + b.valorHeuristico(1), b));
                }
            }
            Escenario aux3 = new Escenario(this);
            aux3.op2(i, -1);
            ret.add(new Successor("Asignar Destino1 " + (-1) + " a viaje" + viajes.get(i) + "H:" + aux3.valorHeuristico(1), aux3));
        }

        return ret;
    }

    public ArrayList getSucesorAleatorio() {
        ArrayList<Successor> ret = new ArrayList();
        int i = r.nextInt(viajes.size());
        int j = r.nextInt(estacionesSinDemanda.size());
        // Por si acaso solo hubiese una petición
        Escenario b = new Escenario(this);

        boolean asignar = false;
        int cont = 0;
        int posE = -1;
        while (!asignar) {
            for (Map.Entry<Integer, Integer> e : estacionesSinDemanda.entrySet()) {
                if (cont == j) {
                    Estacion es = estaciones.get(e.getKey());
                    posE = getEstacion(es.getCoordX(), es.getCoordY());
                    asignar = b.op0(i, posE);
                    break;
                }
                ++cont;
            }
        }
        //ret.add(new Successor("Intercambiadas " + i + " y " + j, b));
        if (asignar) {
            ret.add(new Successor("Asignar Origen " + estaciones.get(posE) + " a viaje" + viajes.get(i) + "H:" + b.valorHeuristico(1), b));
        }
        Escenario aux = new Escenario(this);
        aux.op0(i, -1);
        ret.add(new Successor("Asignar Origen " + (-1) + " a viaje" + viajes.get(i) + "H:" + aux.valorHeuristico(1), aux));
        Escenario d = new Escenario(this);
        j = r.nextInt(estacionesConDemanda.size());
        asignar = false;
        cont = 0;
        posE = -1;
        while (!asignar) {
            for (Map.Entry<Integer, Integer> e : estacionesConDemanda.entrySet()) {
                if (cont == j) {
                    Estacion es = estaciones.get(e.getKey());
                    posE = getEstacion(es.getCoordX(), es.getCoordY());
                    asignar = b.op1(i, posE);
                    break;
                }
                ++cont;
            }
        }
        if (asignar) {
            ret.add(new Successor("Asignar Destino1 " + estaciones.get(posE) + " a viaje" + viajes.get(i) + "H:" + d.valorHeuristico(1), d));
        }
        Escenario auxd = new Escenario(this);
        auxd.op1(i, -1);
        ret.add(new Successor("Asignar Destino1 " + (-1) + " a viaje" + viajes.get(i) + "H:" + auxd.valorHeuristico(1), auxd));

        Escenario d2 = new Escenario(this);
        asignar = false;
        cont = 0;
        posE = -1;
        while (!asignar) {
            for (Map.Entry<Integer, Integer> e : estacionesConDemanda.entrySet()) {
                if (cont == j) {
                    Estacion es = estaciones.get(e.getKey());
                    posE = getEstacion(es.getCoordX(), es.getCoordY());
                    asignar = b.op2(i, posE);
                    break;
                }
                ++cont;
            }
        }
        if (asignar) {
            ret.add(new Successor("Asignar Destino2 " + estaciones.get(posE) + " a viaje" + viajes.get(i) + "H:" + d2.valorHeuristico(1), d2));
        }

        Escenario auxd2 = new Escenario(this);
        auxd2.op2(i, -1);
        ret.add(new Successor("Asignar Destino2 " + (-1) + " a viaje" + viajes.get(i) + "H:" + auxd2.valorHeuristico(1), auxd2));
        return ret;

    }

    public boolean isGoalState() {
        return (false);
    }

    /*public int getKilometros() {
     int kilometros = 0;
     for (Furgoneta f : furgonetas) {
     kilometros += f.getKilometrosRecorridos();
     }
     return kilometros;
     }*/
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

    private int calcDistancia(int x1, int y1, int x2, int y2) {
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }

    public int beneficioViaje(Viaje v) {
        int ox = v.getOrigenx();
        if (ox != -1) {
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

    private static Map<Integer, Integer> sortByComparator(Map<Integer, Integer> unsortMap, final boolean order) {

        List<Map.Entry<Integer, Integer>> list = new LinkedList<Map.Entry<Integer, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1,
                    Map.Entry<Integer, Integer> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public Map<Integer, Integer> getEstacionesConDemanda() {
        return estacionesConDemanda;
    }

    public Map<Integer, Integer> getEstacionesSinDemanda() {
        return estacionesSinDemanda;
    }

    //para pasarle un elemento vacio es suficiente con pasarle -1
    //posE es la posicion en estaciones de la estacion que se le va a asignar
    //v es el viaje donde se van a realizar als modificaciones
    public boolean asignarOrigen(Viaje v, int posE) {
        if (estacionesDisponibilidad.size() < 1) {
            return false;
        }
        int posAnt = getEstacion(v.getOrigenx(), v.getOrigeny());

        if (posE >= 0) {
            if (!estacionesDisponibilidad.get(posE)) {
                return false;
            }
            Estacion e = estaciones.get(posE);
            if (posAnt != -1) {
                Estacion eAnt = estaciones.get(posAnt);
            }

            int disponibles = estacionesSinDemanda.get(posE);
            if (disponibles < 1) {
                return false;
            }

            int antNecesarias = v.getNBDest1() + v.getNBDest2();

            int posDest1 = getEstacion(v.getDest1x(), v.getDest1y());
            int demDest1 = 0;
            if (posDest1 != -1) {
                demDest1 = estacionesConDemanda.get(posDest1);
            }

            int posDest2 = getEstacion(v.getDest2x(), v.getDest2y());
            int demDest2 = 0;
            if (posDest1 != -1) {
                demDest2 = estacionesConDemanda.get(posDest2);
            }

            if (disponibles > antNecesarias) {

                int asignables = disponibles - antNecesarias;
                if (demDest1 > 0) {
                    if (asignables > demDest1) {
                        asignables -= demDest1;
                        v.setNBDest1(v.getNBDest1() + demDest1);
                        estacionesConDemanda.put(posDest1, 0);
                        if (demDest2 > 0) {
                            if (asignables > demDest2) {
                                asignables -= demDest2;
                                v.setNBDest2(v.getNBDest2() + demDest2);
                                estacionesConDemanda.put(posDest2, 0);
                            } else {
                                int resDest2 = demDest2 - asignables;
                                v.setNBDest2(v.getNBDest2() + demDest2 - resDest2);
                                estacionesConDemanda.put(posDest2, demDest2 - asignables);
                                asignables = 0;
                            }
                        }
                    } else {
                        int resDest1 = demDest1 - asignables;
                        v.setNBDest1(v.getNBDest1() + demDest1 - resDest1);
                        estacionesConDemanda.put(posDest1, resDest1);
                        asignables = 0;
                    }
                } else if (demDest2 > 0) {
                    if (asignables > demDest2) {
                        asignables -= demDest2;
                        v.setNBDest2(v.getNBDest2() + demDest2);
                        estacionesConDemanda.put(posDest2, 0);
                    } else {
                        int resDest2 = demDest2 - asignables;
                        v.setNBDest2(v.getNBDest2() + demDest2 - resDest2);
                        estacionesConDemanda.put(posDest2, demDest2 - asignables);
                        asignables = 0;
                    }
                }
            } else {
                if (disponibles >= v.getNBDest1()) {
                    int antInDest2 = v.getNBDest2();
                    v.setNBDest2(disponibles - v.getNBDest1());
                    estacionesConDemanda.put(posDest2, demDest2 + antInDest2 - v.getNBDest2());
                } else {
                    estacionesConDemanda.put(posDest2, demDest2 + v.getNBDest2());
                    v.setNBDest2(0);
                    int antInDest1 = v.getNBDest1();
                    v.setNBDest1(disponibles);
                    estacionesConDemanda.put(posDest1, demDest1 + antInDest1 - v.getNBDest1());
                }
            }
            if (posAnt != -1) {
                estacionesDisponibilidad.add(posAnt, Boolean.TRUE);
            }
            estacionesDisponibilidad.add(posE, Boolean.FALSE);
            v.setOrigenx(e.getCoordX());
            v.setOrigeny(e.getCoordY());
        } else {
            if (posAnt != -1) {
                estacionesDisponibilidad.add(posAnt, Boolean.TRUE);
            }
            v.setOrigenx(-1);
            v.setOrigeny(-1);
            v.setDest1x(-1);
            v.setDest1y(-1);
            v.setNBDest1(0);
            v.setDest2x(-1);
            v.setDest2y(-1);
            v.setNBDest2(0);
        }
        return true;
    }

    public int bicisDisponibles(Estacion e) {
        return e.getNumBicicletasNoUsadas() - (e.getDemanda() - (e.getNumBicicletasNext() - e.getNumBicicletasNoUsadas()));
    }

    //para pasarle un elemento vacio es suficiente con pasarle -1
    public boolean asignarDestino1(Viaje v, int posE) {
        //mirar si la estacion del viaje esta vacio
        if (v.getOrigenx() < 0) {
            return false;
        }
        if (v.getDest1x() >= 0) {
            int posAnt = getEstacion(v.getDest1x(), v.getDest1y());
            System.out.print("posAnt: " + posAnt);
            if (posE >= 0) {
                Estacion e = estaciones.get(posE);
                int eDemanda = estacionesConDemanda.get(posE);
                if (eDemanda < 1) {
                    return false;
                }

                Estacion eAnt = estaciones.get(posAnt);

                estacionesDisponibilidad.add(posAnt, Boolean.TRUE);
                estacionesConDemanda.put(posAnt, estacionesConDemanda.get(posAnt) + v.getNBDest1());

                estacionesDisponibilidad.add(posE, Boolean.FALSE);
                v.setDest1x(e.getCoordX());
                v.setDest1y(e.getCoordY());

                int posOrig = getEstacion(v.getOrigenx(), v.getOrigeny());
                int posibles = estacionesSinDemanda.get(posOrig);

                int posDest2 = getEstacion(v.getDest2x(), v.getDest2y());

                //si no tiene dest 2
                if (v.getDest2x() < 0) {
                    //hay mas bicis disponibles que la demanda
                    if (posibles >= eDemanda) {
                        estacionesConDemanda.put(posE, 0);
                        v.setNBDest1(eDemanda);
                    } else {
                        //menos bicis disponibles que la demanda
                        estacionesConDemanda.put(posE, eDemanda - posibles);
                        v.setNBDest1(posibles);
                    }
                } else {
                    int disp = posibles - v.getNBDest2();
                    if (disp >= eDemanda) {
                        estacionesConDemanda.put(posE, 0);
                        v.setNBDest1(eDemanda);
                        disp -= eDemanda;
                        int demDest2 = estacionesConDemanda.get(posDest2);
                        if (demDest2 > 0) {
                            if (disp >= demDest2) {
                                v.setNBDest2(v.getNBDest2() + demDest2);
                                estacionesConDemanda.put(posDest2, 0);
                                disp -= demDest2;
                            } else {
                                v.setNBDest2(v.getNBDest2() + disp);
                                estacionesConDemanda.put(posDest2, estacionesConDemanda.get(posDest2) - disp);
                            }
                        }

                    } else {
                        //si hay un dest2 se mantiene que se dejan el maximo en dest1 antes que en dest2
                        int necesario = eDemanda - disp;
                        if (v.getNBDest2() > necesario) {
                            v.setNBDest2(v.getNBDest2() - necesario);
                            estacionesConDemanda.put(posDest2, estacionesConDemanda.get(posDest2) + necesario);
                            estacionesConDemanda.put(posE, 0);
                            v.setNBDest1(eDemanda);
                        } else {
                            estacionesConDemanda.put(posDest2, estacionesConDemanda.get(posDest2) + v.getNBDest2());
                            estacionesConDemanda.put(posE, estacionesConDemanda.get(posE) - v.getNBDest2());
                            v.setNBDest1(v.getNBDest2());
                            v.setNBDest2(0);
                            v.setDest2x(-1);
                            v.setDest2y(-1);
                        }
                    }
                }

            } else {
                estacionesConDemanda.put(posAnt, estacionesConDemanda.get(posAnt) + v.getNBDest1());
                int posDest2 = getEstacion(v.getDest2x(), v.getDest2y());
                estacionesConDemanda.put(posDest2, estacionesConDemanda.get(posDest2) + v.getNBDest2());
                v.setDest1x(-1);
                v.setDest1y(-1);
                v.setNBDest1(0);
                v.setDest2x(-1);
                v.setDest2y(-1);
                v.setNBDest2(0);
            }
        } else if (posE >= 0) {
            //si la estacion en viaje estaba vacia
            v.setDest1x(estaciones.get(posE).getCoordX());
            v.setDest1y(estaciones.get(posE).getCoordY());
            int posOrig = getEstacion(v.getOrigenx(), v.getOrigeny());
            int disponibles = estacionesSinDemanda.get(posOrig) - v.getNBDest2();
            int nec = estacionesConDemanda.get(posE);
            if (disponibles >= nec) {
                v.setNBDest1(nec);
                estacionesConDemanda.put(posE, 0);
            } else {
                if (v.getDest2x() >= 0) {
                    int posDest2 = getEstacion(v.getDest2x(), v.getDest2y());
                    int necesario = nec - disponibles;
                    if (necesario > v.getNBDest2()) {
                        estacionesConDemanda.put(posDest2, estacionesConDemanda.get(posDest2) + v.getNBDest2());
                        estacionesConDemanda.put(posE, estacionesConDemanda.get(posE) - v.getNBDest2());
                        v.setNBDest1(v.getNBDest2() + disponibles);
                        v.setNBDest2(0);
                        v.setDest2x(-1);
                        v.setDest2y(-1);
                    } else {
                        v.setNBDest2(v.getNBDest2() - necesario);
                        estacionesConDemanda.put(posDest2, estacionesConDemanda.get(posDest2) + necesario);
                        v.setNBDest2(v.getNBDest2() - necesario);

                        estacionesConDemanda.put(posE, 0);
                        v.setNBDest1(nec);
                    }
                } else {
                    v.setNBDest1(disponibles);
                    estacionesConDemanda.put(posE, estacionesConDemanda.get(posE) - disponibles);
                }

            }

        }
        return true;
    }

    public boolean asignarDestino2(Viaje v, int posE) {
        //reasignado anterior dest2
        if (v.getOrigenx() < 0) {
            return false;
        }
        int posOrig = getEstacion(v.getOrigenx(), v.getOrigeny());
        if (v.getDest2x() >= 0) {
            int posDest2 = getEstacion(v.getDest2x(), v.getDest2y());
            estacionesConDemanda.put(posDest2, estacionesConDemanda.get(posDest2) + v.getNBDest2());
            if (posE >= 0) {
                if (1 > estacionesConDemanda.get(posE)) {
                    return false;
                }

                int disponibles = estacionesSinDemanda.get(posOrig) - v.getNBDest1();
                int aAsignar;
                if (disponibles >= estacionesConDemanda.get(posE)) {
                    aAsignar = estacionesConDemanda.get(posE);
                } else {
                    aAsignar = disponibles;
                }
                estacionesConDemanda.put(posE, aAsignar);
                v.setNBDest2(aAsignar);
            } else {
                v.setDest2x(-1);
                v.setDest2y(-1);
                v.setNBDest2(0);
            }
        } else if (posE >= 0) {
            if (1 > estacionesConDemanda.get(posE)) {
                return false;
            }
            int max;
            if (estacionesConDemanda.get(posE) > estacionesSinDemanda.get(posOrig) - v.getNBDest1()) {
                max = estacionesSinDemanda.get(posOrig) - v.getNBDest1();
            } else {
                max = estacionesConDemanda.get(posE);
            }
            v.setNBDest2(max);
            estacionesConDemanda.put(posE, estacionesConDemanda.get(posE) - max);
            v.setDest2x(-1);
            v.setDest2y(-1);
        }
        return true;
    }

    public int getEstacion(int X, int Y) {
        for (int i = 0; i < estaciones.size(); ++i) {
            Estacion e = estaciones.get(i);
            if (e.getCoordX() == X && e.getCoordY() == Y) {
                return i;
            }
        }
        return -1;
    }

}
