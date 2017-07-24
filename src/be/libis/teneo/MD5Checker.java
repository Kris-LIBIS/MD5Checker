/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.libis.teneo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
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
    }

    /**
     * Creates new form MD5Checker
     */
    public MD5Checker() {
        this.filesInfo = new TreeMap();
        initComponents();
    }

    private DefaultTableModel getTableModel() {
        return (DefaultTableModel) tblFileInfo.getModel();
    }

    private void getFileInfos() {
        this.filesInfo.clear();
        this.pbarFolder.setValue(0);
        getTableModel().setRowCount(0);

        new Checksummer(this.folder).execute();
    }

    private void addFileInfo(FileInfo fileInfo) {
        this.filesInfo.put(fileInfo.file.getName(), fileInfo);
        getTableModel().addRow(new Object[]{fileInfo.file.getName(), fileInfo.checksum, fileInfo.status.toString()});
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
        pbarFolder = new javax.swing.JProgressBar();
        pnlTable = new javax.swing.JScrollPane();
        tblFileInfo = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LIBIS Teneo MD5 Checker");

        lblFolder.setText("Folder:");

        txtFolder.setText("Folder name ...");

        btnFolder.setText("Select");
        btnFolder.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnFolder.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFolderActionPerformed(evt);
            }
        });

        tblFileInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File name", "Checksum", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pnlTable.setViewportView(tblFileInfo);
        if (tblFileInfo.getColumnModel().getColumnCount() > 0) {
            tblFileInfo.getColumnModel().getColumn(0).setPreferredWidth(400);
            tblFileInfo.getColumnModel().getColumn(1).setPreferredWidth(32);
            tblFileInfo.getColumnModel().getColumn(2).setPreferredWidth(16);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFolder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnFolder, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pbarFolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE))
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
                .addComponent(pnlTable, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pbarFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFolderActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MD5Checker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MD5Checker().setVisible(true);
        });
    }

    private class Checksummer extends SwingWorker<Void, FileInfo> {

        private final File folder;
        private final Map<String, String> checksumInfo;
        private static final String CHECKSUM_FILE = "md5sums";

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
                    processChecksumLine(scanner.nextLine());
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MD5Checker.class.getName()).log(Level.INFO, "Checksum file not found", ex);
            }
        }

        private void processChecksumLine(String line) {
            Scanner scanner = new Scanner(line);
            scanner.useDelimiter(Pattern.compile(" +[*]?"));
            if (scanner.hasNext()) {
                String checksum = scanner.next();
                String filename = scanner.next();
                this.checksumInfo.put(filename, checksum);
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
            setProgress(0);
            int total = fileList.size();
            int i = 0;
            for (String fileName : fileList) {
                File file = new File(this.folder, fileName);
                FileInfo fileInfo = new FileInfo();
                fileInfo.file = file;
                if (file.exists()) {
                    fileInfo.checksum = getFileChecksum(file);
                }
                fileInfo.status
                        = this.checksumInfo.get(fileName) == null ? Status.NEW
                        : (fileInfo.checksum == null ? Status.DELETED
                                : (this.checksumInfo.get(fileName).equals(fileInfo.checksum) ? Status.OK : Status.CHANGED));
                i++;
                setProgress(100 * i / total);
                publish(fileInfo);
            }
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
        protected void process(List<FileInfo> items) {
            for (FileInfo fileInfo : items) {
                addFileInfo(fileInfo);
                pbarFolder.setValue(getProgress());
            };
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFolder;
    private javax.swing.JLabel lblFolder;
    private javax.swing.JProgressBar pbarFolder;
    private javax.swing.JScrollPane pnlTable;
    private javax.swing.JTable tblFileInfo;
    private javax.swing.JTextField txtFolder;
    // End of variables declaration//GEN-END:variables
}