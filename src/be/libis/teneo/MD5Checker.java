/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.libis.teneo;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kris
 */
public class MD5Checker extends javax.swing.JFrame {

    private File folder = null;
    private final Map<String, FileInfo> filesInfo;

    public enum Status {
        OK, NEW, DELETED, CHANGED
    };

    class FileInfo {

        public File file;
        public String checksum;
        public Status status;
        public boolean ignored;
    }

    class ProcessingUpdate {

        public File started;
        public FileInfo done;

        public ProcessingUpdate(File file) {
            started = file;
        }

        public ProcessingUpdate(FileInfo info) {
            done = info;
        }
    }

    /**
     * Creates new form MD5Checker
     */
    public MD5Checker() {
        this.filesInfo = new TreeMap();
        initComponents();
        initialState();
    }

    private void initialState() {
        btnFolder.setEnabled(true);
        tabPanel.setVisible(false);
        btnSaveChecksum.setVisible(false);
        btnSuccess.setVisible(false);
        pnlStatus.setVisible(false);
    }

    private void processingState() {
        btnFolder.setEnabled(false);
        filesInfo.clear();
        pbarFolder.setValue(0);
        tabPanel.setVisible(true);
        btnSaveChecksum.setVisible(false);
        btnSuccess.setVisible(false);
        pnlStatus.setVisible(true);
        txtProcessing.setText(" >>> Starting <<< ");
        getTableModel().setRowCount(0);
        getSummaryModel().setRowCount(0);
        getSummaryModel().addRow(new Object[]{"Unmodified files", 0, "OK"});
        getSummaryModel().addRow(new Object[]{"New files", 0, "NEW"});
        getSummaryModel().addRow(new Object[]{"Changed files", 0, "CHANGED"});
        getSummaryModel().addRow(new Object[]{"Deleted files", 0, "DELETED"});
        getSummaryModel().addRow(new Object[]{"Ignored files", 0, "IGNORED"});
        getSummaryModel().addRow(new Object[]{"TOTAL", 0, ""});
    }

    private void processingDoneState() {
        btnFolder.setEnabled(true);
        pnlStatus.setVisible(false);
        if (checksumsChanged()) {
            btnSaveChecksum.setVisible(true);
        } else {
            btnSuccess.setVisible(true);
        }
    }

    private DefaultTableModel getTableModel() {
        return (DefaultTableModel) tblFileInfo.getModel();
    }

    private DefaultTableModel getSummaryModel() {
        return (DefaultTableModel) tblSummary.getModel();
    }

    private void getFileInfos() {
        processingState();

        new Checksummer(this.folder).execute();
    }

    private void startFileProcessing(File started) {
        txtProcessing.setText(started.getName());
    }

    private void addFileInfo(FileInfo fileInfo) {
        this.filesInfo.put(fileInfo.file.getName(), fileInfo);
        getTableModel().addRow(new Object[]{fileInfo.file.getName(), fileInfo.checksum, fileInfo.status.toString()});
        pbarFolder.setValue(pbarFolder.getValue() + 1);
        DefaultTableModel summaryData = getSummaryModel();
        switch (fileInfo.status) {
            case OK:
                summaryData.setValueAt((int) summaryData.getValueAt(0, 1) + 1, 0, 1);
                break;
            case NEW:
                summaryData.setValueAt((int) summaryData.getValueAt(1, 1) + 1, 1, 1);
                break;
            case CHANGED:
                summaryData.setValueAt((int) summaryData.getValueAt(2, 1) + 1, 2, 1);
                break;
            case DELETED:
                summaryData.setValueAt((int) summaryData.getValueAt(3, 1) + 1, 3, 1);
                break;
        }
        if (fileInfo.ignored) {
            summaryData.setValueAt((int) summaryData.getValueAt(4, 1) + 1, 4, 1);
        }
        summaryData.setValueAt((int) summaryData.getValueAt(5, 1) + 1, 5, 1);
    }

    private void checksumFinished() {
        processingDoneState();
    }

    private boolean checksumsChanged() {
        return this.filesInfo.values().stream()
                .anyMatch((fileInfo) -> (fileInfo.status != Status.OK));
    }

    private void writeChecksum() {
        try {
            File checksum = new File(this.folder, Checksummer.CHECKSUM_FILE);
            try (PrintWriter writer = new PrintWriter(checksum, "UTF-8")) {
                this.filesInfo.values().forEach((fileInfo) -> {
                    if (fileInfo.checksum != null) {
                        writer.printf("%s *%s\n", fileInfo.checksum, fileInfo.file.getName());
                    }
                });
            }
            JOptionPane.showMessageDialog(null, "Checksum file saved.");
            initialState();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(MD5Checker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblFolder = new javax.swing.JLabel();
        txtFolder = new javax.swing.JTextField();
        btnFolder = new javax.swing.JButton();
        tabPanel = new javax.swing.JTabbedPane();
        pnlOverview = new javax.swing.JPanel();
        tblSummary = new javax.swing.JTable();
        pnlTable = new javax.swing.JScrollPane();
        tblFileInfo = new javax.swing.JTable();
        pnlLayers = new javax.swing.JLayeredPane();
        btnSaveChecksum = new javax.swing.JButton();
        btnSuccess = new javax.swing.JButton();
        pnlStatus = new javax.swing.JPanel();
        lblProcessing = new javax.swing.JLabel();
        txtProcessing = new javax.swing.JTextField();
        pbarFolder = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LIBIS Teneo MD5 Checker");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lblFolder.setText("Folder:");

        txtFolder.setEditable(false);
        txtFolder.setText("Folder name ...");

        btnFolder.setText("Select");
        btnFolder.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnFolder.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFolderActionPerformed(evt);
            }
        });

        tblSummary.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Summary", "File count", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSummary.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        tblSummary.getTableHeader().setReorderingAllowed(false);

        javax.swing.GroupLayout pnlOverviewLayout = new javax.swing.GroupLayout(pnlOverview);
        pnlOverview.setLayout(pnlOverviewLayout);
        pnlOverviewLayout.setHorizontalGroup(
            pnlOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tblSummary, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
        );
        pnlOverviewLayout.setVerticalGroup(
            pnlOverviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tblSummary, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
        );

        if (tblSummary.getColumnModel().getColumnCount() > 0) {
            tblSummary.getColumnModel().getColumn(0).setResizable(false);
            tblSummary.getColumnModel().getColumn(0).setCellRenderer(new ColorRenderer());
            tblSummary.getColumnModel().getColumn(1).setResizable(false);
            tblSummary.getColumnModel().getColumn(1).setCellRenderer(new ColorRenderer());
            tblSummary.getColumnModel().getColumn(2).setResizable(false);
            tblSummary.getColumnModel().getColumn(2).setPreferredWidth(0);
            tblSummary.getColumnModel().getColumn(2).setCellRenderer(new ColorRenderer());
        }

        tabPanel.addTab("Overview", pnlOverview);

        tblFileInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File name", "Checksum", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFileInfo.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        tblFileInfo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFileInfoMouseClicked(evt);
            }
        });
        pnlTable.setViewportView(tblFileInfo);
        if (tblFileInfo.getColumnModel().getColumnCount() > 0) {
            tblFileInfo.getColumnModel().getColumn(0).setPreferredWidth(400);
            tblFileInfo.getColumnModel().getColumn(0).setCellRenderer(new ColorRenderer());
            tblFileInfo.getColumnModel().getColumn(1).setPreferredWidth(32);
            tblFileInfo.getColumnModel().getColumn(1).setCellRenderer(new ColorRenderer());
            tblFileInfo.getColumnModel().getColumn(2).setPreferredWidth(16);
            tblFileInfo.getColumnModel().getColumn(2).setCellRenderer(new ColorRenderer());
        }

        tabPanel.addTab("Detail", pnlTable);

        btnSaveChecksum.setBackground(new java.awt.Color(255, 102, 102));
        btnSaveChecksum.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btnSaveChecksum.setText("Click to update checksum file");
        btnSaveChecksum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveChecksumActionPerformed(evt);
            }
        });

        btnSuccess.setBackground(new java.awt.Color(153, 255, 153));
        btnSuccess.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btnSuccess.setText("Checksum is up to date");
        btnSuccess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuccessActionPerformed(evt);
            }
        });

        lblProcessing.setText("Processing: ");

        txtProcessing.setEditable(false);
        txtProcessing.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtProcessing.setText(">>> STARTING <<<");
        txtProcessing.setFocusable(false);

        pbarFolder.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N

        javax.swing.GroupLayout pnlStatusLayout = new javax.swing.GroupLayout(pnlStatus);
        pnlStatus.setLayout(pnlStatusLayout);
        pnlStatusLayout.setHorizontalGroup(
            pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatusLayout.createSequentialGroup()
                .addComponent(lblProcessing, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtProcessing))
            .addComponent(pbarFolder, javax.swing.GroupLayout.DEFAULT_SIZE, 875, Short.MAX_VALUE)
        );
        pnlStatusLayout.setVerticalGroup(
            pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProcessing)
                    .addComponent(txtProcessing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pbarFolder, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
        );

        pnlLayers.setLayer(btnSaveChecksum, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlLayers.setLayer(btnSuccess, javax.swing.JLayeredPane.DEFAULT_LAYER);
        pnlLayers.setLayer(pnlStatus, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout pnlLayersLayout = new javax.swing.GroupLayout(pnlLayers);
        pnlLayers.setLayout(pnlLayersLayout);
        pnlLayersLayout.setHorizontalGroup(
            pnlLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnSuccess, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnSaveChecksum, javax.swing.GroupLayout.DEFAULT_SIZE, 875, Short.MAX_VALUE))
        );
        pnlLayersLayout.setVerticalGroup(
            pnlLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnSuccess, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
            .addGroup(pnlLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(pnlStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlLayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(btnSaveChecksum, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabPanel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFolder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlLayers))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFolder)
                    .addComponent(txtFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFolder))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlLayers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFolderActionPerformed
        JFileChooser chooser = new JFileChooser();
        File pwd = this.folder == null ? new File(".") : this.folder;
        chooser.setCurrentDirectory(pwd);
        chooser.setDialogTitle("Select folder for checking MD5 checksums");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            this.folder = chooser.getSelectedFile();
            this.txtFolder.setText(this.folder.getAbsolutePath());
            getTableModel().setRowCount(0);

            getFileInfos();
        }
    }//GEN-LAST:event_btnFolderActionPerformed

    private void btnSaveChecksumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveChecksumActionPerformed
        writeChecksum();
        btnSaveChecksum.setVisible(false);
    }//GEN-LAST:event_btnSaveChecksumActionPerformed

    private void btnSuccessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuccessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSuccessActionPerformed

    private void tblFileInfoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFileInfoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblFileInfoMouseClicked

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
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MD5Checker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MD5Checker().setVisible(true);
        });
    }

    public class ColorRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setForeground(Color.BLACK);
            String status = (String) table.getModel().getValueAt(row, 2);
            if (status != null) {
                switch (status) {
                    case "OK":
                        setForeground(Color.BLACK);
                        break;
                    case "NEW":
                        setForeground(Color.GREEN.darker().darker());
                        break;
                    case "DELETED":
                        setForeground(Color.RED.darker());
                        break;
                    case "CHANGED":
                        setForeground(Color.ORANGE.darker());
                        break;
                    case "IGNORED":
                        setForeground(Color.GRAY);
                        break;
                }
            }

            return this;
        }
    }

    private class Checksummer extends SwingWorker<Void, ProcessingUpdate> {
        
        /*
        TODO:
            - add ignored support in detail list
            - add action to toggle ignore in detail list
            - flag, but ignore checksum mismatches for ignored files
        */

        private final File folder;
        private final Map<String, ChecksumInfo> checksumInfo;
        private static final String CHECKSUM_FILE = "md5sums";

        class ChecksumInfo {

            public String checksum;
            public boolean ignored;

            public ChecksumInfo(String _checksum) {
                this.checksum = _checksum;
                this.ignored = false;
                if (this.checksum.startsWith("#")) {
                    this.checksum = this.checksum.substring(1);
                    this.ignored = true;
                }
            }

        }

        public Checksummer(File folder) {
            super();
            this.folder = folder;
            this.checksumInfo = new TreeMap();
        }

        @Override
        protected Void doInBackground() {
            readChecksumFile();
            getFileInfos();
            return null;
        }

        private void readChecksumFile() {
            this.checksumInfo.clear();
            File checksumFile = new File(this.folder, CHECKSUM_FILE);
            try {
                Scanner scanner = new Scanner(checksumFile);
                while (scanner.hasNextLine()) {
                    String[] data = scanner.nextLine().split(" +[*]?", 2);
                    if (data.length == 2) {
                        this.checksumInfo.put(data[1], new ChecksumInfo(data[0]));
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MD5Checker.class.getName()).log(Level.INFO, "Checksum file not found");
            }
        }

        private void getFileInfos() {
            Set<String> fileList = new TreeSet<>();
            for (File file : this.folder.listFiles()) {
                if (file.isFile() && !file.getName().equals(CHECKSUM_FILE)) {
                    fileList.add(file.getName());
                }
            }
            this.checksumInfo.keySet().forEach((filename) -> {
                fileList.add(filename);
            });
            pbarFolder.setMaximum(fileList.size());
            fileList.stream().map((String fileName) -> {
                File file = new File(this.folder, fileName);
                publish(new ProcessingUpdate(file));
                FileInfo fileInfo = new FileInfo();
                fileInfo.file = file;
                if (file.exists()) {
                    fileInfo.checksum = getFileChecksum(file);
                }
                ChecksumInfo info = this.checksumInfo.get(fileName);
                if (info == null) {
                    fileInfo.status = Status.NEW;
                } else if (fileInfo.checksum == null) {
                    fileInfo.status = Status.DELETED;
                } else if (info.checksum.equals(fileInfo.checksum)) {
                    fileInfo.status = Status.OK;
                } else {
                    fileInfo.status = Status.CHANGED;
                }
                fileInfo.ignored = info.ignored;
                return fileInfo;
            }).forEachOrdered((fileInfo) -> {
                publish(new ProcessingUpdate(fileInfo));
            });
        }

        private String getFileChecksum(File file) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                InputStream is = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int numRead;

                while ((numRead = is.read(buffer)) != -1) {
                    md5.update(buffer, 0, numRead);
                }
                
                is.close();
                return bytesToHex(md5.digest());
            } catch (NoSuchAlgorithmException | IOException ex) {
                Logger.getLogger(MD5Checker.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        private final char[] HEXARRAY = "0123456789abcdef".toCharArray();

        public String bytesToHex(byte[] bytes) {
            char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = HEXARRAY[v >>> 4];
                hexChars[j * 2 + 1] = HEXARRAY[v & 0x0F];
            }
            return new String(hexChars);
        }

        @Override
        protected void process(List<ProcessingUpdate> items) {
            for (ProcessingUpdate update : items) {
                if (update.done != null) {
                    addFileInfo(update.done);
                }
                if (update.started != null) {
                    startFileProcessing(update.started);
                }
            }
        }

        @Override
        protected void done() {
            checksumFinished();
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFolder;
    private javax.swing.JButton btnSaveChecksum;
    private javax.swing.JButton btnSuccess;
    private javax.swing.JLabel lblFolder;
    private javax.swing.JLabel lblProcessing;
    private javax.swing.JProgressBar pbarFolder;
    private javax.swing.JLayeredPane pnlLayers;
    private javax.swing.JPanel pnlOverview;
    private javax.swing.JPanel pnlStatus;
    private javax.swing.JScrollPane pnlTable;
    private javax.swing.JTabbedPane tabPanel;
    private javax.swing.JTable tblFileInfo;
    private javax.swing.JTable tblSummary;
    private javax.swing.JTextField txtFolder;
    private javax.swing.JTextField txtProcessing;
    // End of variables declaration//GEN-END:variables
}
