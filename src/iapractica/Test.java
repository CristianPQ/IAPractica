/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iapractica;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author Jona
 */
public class Test extends javax.swing.JFrame {

    /**
     * Creates new form Test
     */
    public Test() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jcmbEstado = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jcmbDemanda = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jcmbBeneficio = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jtxtEstaciones = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtxtBicicletas = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jtxtSemilla = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jtxtFurgonetas = new javax.swing.JTextField();
        jtxtIteraciones = new javax.swing.JTextField();
        jbtnHillC = new javax.swing.JButton();
        jbtnSimulatetA = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Estado Inicial: ");

        jcmbEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Estado Inicial Vacío", "Estado Inicial Random", "Estado Inicial Lógico" }));

        jLabel2.setText("Demanda: ");

        jcmbDemanda.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Equilibrada", "Hora Punta" }));

        jLabel3.setText("Heurística: ");

        jcmbBeneficio.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Beneficio", "Demanda" }));

        jLabel4.setText("Estaciones: ");

        jLabel5.setText("Bicicletas: ");

        jLabel6.setText("Semilla:");

        jLabel7.setText("(vacía para random)");

        jLabel8.setText("Max Iteraciones :");

        jLabel9.setText("Furgonetas: ");

        jbtnHillC.setText("Ejecutar: Hill Climbing");
        jbtnHillC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnHillCActionPerformed(evt);
            }
        });

        jbtnSimulatetA.setText("Ejecutar: Simulatet Annealing");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcmbDemanda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcmbBeneficio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtxtIteraciones, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jbtnHillC, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jtxtSemilla, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jtxtFurgonetas, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jtxtBicicletas, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jtxtEstaciones, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jbtnSimulatetA))
                .addGap(66, 66, 66))
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 636, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jcmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jtxtEstaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jcmbDemanda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jtxtBicicletas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jcmbBeneficio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jtxtFurgonetas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jtxtSemilla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jtxtIteraciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnHillC)
                    .addComponent(jbtnSimulatetA))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnHillCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnHillCActionPerformed
        
        Escenario escenario;
        double mediatiempo = 0, mediabeneficio = 0, medianodos = 0, mediaKilometros = 0;
        String estadoIni = "", heuristico = "";
        for (int i = 0; i < 10000; i++) {
            /*if (!semilla.getText().equals("")) {
                escenario = new DGBoard(Integer.valueOf(nGasolineras.getText()), Integer.valueOf(nCentros.getText()), Integer.valueOf(ncamiones.getText()), Integer.valueOf(semilla.getText()));
            } else {
                escenario = new DGBoard(Integer.valueOf(nGasolineras.getText()), Integer.valueOf(nCentros.getText()), Integer.valueOf(ncamiones.getText()));
            }*/
            int heuris = 0;
            
            if(jcmbBeneficio.getSelectedIndex() == 1){
                heuris = 1;
            }
            
            int seed;
            if(jtxtSemilla.getText().equals("")){
                Random r = new Random();
                seed = r.nextInt(100);
            }else{
                seed = Integer.valueOf(jtxtSemilla.getText());
            }
            
            escenario = new Escenario(Integer.valueOf(jtxtBicicletas.getText()),Integer.valueOf(jtxtEstaciones.getText()),Integer.valueOf(jtxtFurgonetas.getText()),heuris,seed);
            
           if (jcmbEstado.getSelectedIndex() == 0) {
                estadoIni = "Estado Inicial: Vacio";
                escenario.generarEstadoInicialVacio();
            } else if (jcmbEstado.getSelectedIndex() == 1) {
                estadoIni = "Estado Inicial: Random";
                escenario.generarEstadoInicialRandom();
            } else {
                estadoIni = "Estado Inicial: Lógico";
                escenario.generarEstadoInicialLogico();
            }
            System.out.println(escenario.Beneficios());
            try {
                Problem problem;
                /*if (Heuristico.getSelectedIndex() == 0) {
                    heuristico = "Heuristico: 1";
                    problem = new Problem(escenario, new DGSuccessorFunction(), new DGGoalTest(), new DGHeuristicFunction());
                } else {
                    heuristico = "Heuristico: 2";
                    problem = new Problem(escenario, new DGSuccessorFunction(), new DGGoalTest(), new DGHeuristicFunction1());
                }*/
                
                problem = new Problem(escenario, new BSuccessorFunction(), new BGoalTest(), new BHeuristicFunction());
                
                Search search = new HillClimbingSearch();
                Date d1, d2;
                long temps;
                Calendar a, b;
                d1 = new Date();
                SearchAgent agent = new SearchAgent(problem, search);
                d2 = new Date();
                a = Calendar.getInstance();
                b = Calendar.getInstance();
                a.setTime(d1);
                b.setTime(d2);
                temps = b.getTimeInMillis() - a.getTimeInMillis();
                mediatiempo += temps;
                Iterator keys = agent.getInstrumentation().keySet().iterator();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    String property = agent.getInstrumentation().getProperty(key);
                    medianodos += Double.valueOf(property);
                }
                Escenario board2 = (Escenario) search.getGoalState();
                
                mediabeneficio = board2.Beneficios();
                //mediaKilometros += board2.getKilometros();
                System.out.println("Beneficio: " + mediabeneficio);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        
        
    }//GEN-LAST:event_jbtnHillCActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Test().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton jbtnHillC;
    private javax.swing.JButton jbtnSimulatetA;
    private javax.swing.JComboBox jcmbBeneficio;
    private javax.swing.JComboBox jcmbDemanda;
    private javax.swing.JComboBox jcmbEstado;
    private javax.swing.JTextField jtxtBicicletas;
    private javax.swing.JTextField jtxtEstaciones;
    private javax.swing.JTextField jtxtFurgonetas;
    private javax.swing.JTextField jtxtIteraciones;
    private javax.swing.JTextField jtxtSemilla;
    // End of variables declaration//GEN-END:variables
}
