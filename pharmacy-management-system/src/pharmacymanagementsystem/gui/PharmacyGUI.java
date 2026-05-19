
package pharmacymanagementsystem.gui;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.*;
import pharmacymanagementsystem.entity.Medicine;
import pharmacymanagementsystem.fileio.MedicineFileIO;

public class PharmacyGUI extends JFrame {

   
    private static final Color CLR_HEADER     = new Color(0, 102, 102);
    private static final Color CLR_ACCENT     = new Color(0, 140, 130);
    private static final Color CLR_DANGER     = new Color(192, 57, 43);
    private static final Color CLR_NEUTRAL    = new Color(90, 90, 90);
    private static final Color CLR_BG_RIGHT   = new Color(245, 248, 248);
    private static final Color CLR_ROW_ALT    = new Color(224, 242, 241);
    private static final Color CLR_BG_FORM    = Color.WHITE;
    private static final Color CLR_TBL_HEADER = new Color(0, 102, 102);

    private static final Font FONT_TITLE  = new Font("SansSerif", Font.BOLD,  22);
    private static final Font FONT_LABEL  = new Font("SansSerif", Font.BOLD,  13);
    private static final Font FONT_FIELD  = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_BTN    = new Font("SansSerif", Font.BOLD,  13);
    private static final Font FONT_STATUS = new Font("SansSerif", Font.PLAIN, 12);

    private JTextField tfId;      
    private JTextField tfName;     
    private JTextField tfCategory; 
    private JTextField tfPrice;    
    private JTextField tfStock;   

    private JTextField       tfSearch;    
    private JTable           table;       
    private DefaultTableModel tableModel; 
    private JButton btnAdd;     
    private JButton btnUpdate;  
    private JButton btnDelete; 
    private JButton btnClear;  
    private JButton btnSearch; 
    private JButton btnShowAll; 

    private JLabel lblStatus; 

  
    public PharmacyGUI() {
    
        setTitle("Pharmacy Management System"); 
        setSize(1050, 650);                     
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLayout(new BorderLayout(0, 0));      

       
        setLocationRelativeTo(null); 

        
        add(buildHeaderPanel(), BorderLayout.NORTH);   
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildStatusBar(),   BorderLayout.SOUTH);  

       
        refreshTable();

        setVisible(true);
    }

    private JPanel buildHeaderPanel() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14));
        header.setBackground(CLR_HEADER); 
        header.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

       
        JLabel icon = new JLabel("\u2695"); 
        icon.setFont(new Font("SansSerif", Font.PLAIN, 32));
        icon.setForeground(Color.WHITE);

        JLabel title = new JLabel("Pharmacy Management System");
        title.setFont(FONT_TITLE);
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("  —  Medicine Inventory & Records");
        sub.setFont(new Font("SansSerif", Font.ITALIC, 14));
        sub.setForeground(new Color(180, 230, 230));

        header.add(icon);
        header.add(title);
        header.add(sub);
        return header;
    }

    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout());
        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                buildFormPanel(), 
                buildTablePanel()  
        );
        split.setDividerLocation(320);   
        split.setDividerSize(4);         
        split.setEnabled(false);         
        split.setBorder(null);

        center.add(split, BorderLayout.CENTER);
        return center;
    }
  
    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(CLR_BG_FORM);
        outer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel sectionTitle = new JLabel("Medicine Details");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        sectionTitle.setForeground(CLR_HEADER);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        outer.add(sectionTitle, BorderLayout.NORTH);

    
        JPanel fields = new JPanel(new GridBagLayout());
        fields.setBackground(CLR_BG_FORM);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 0, 7, 8); 
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        fields.add(makeLabel("Medicine ID *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        tfId = makeTextField("Enter 8-digit ID");
        tfId.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                    return;
                }
                
                if (tfId.getText().length() >= 8)
                    e.consume();
            }
        });
        fields.add(tfId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        fields.add(makeLabel("Medicine Name *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        tfName = makeTextField("e.g. Paracetamol 500mg");
        fields.add(tfName, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        fields.add(makeLabel("Category *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        tfCategory = makeTextField("e.g. Analgesic");
        fields.add(tfCategory, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        fields.add(makeLabel("Unit Price (BDT) *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        tfPrice = makeTextField("e.g. 12.50");
        fields.add(tfPrice, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        fields.add(makeLabel("Stock Qty *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        tfStock = makeTextField("e.g. 200");
        fields.add(tfStock, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.weighty = 1; gbc.fill = GridBagConstraints.BOTH;
        fields.add(Box.createVerticalGlue(), gbc);

        outer.add(fields, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        btnPanel.setBackground(CLR_BG_FORM);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        btnAdd    = makeButton("+ Add",    CLR_ACCENT);
        btnUpdate = makeButton("✎ Update", CLR_HEADER);
        btnDelete = makeButton("✕ Delete", CLR_DANGER);
        btnClear  = makeButton("↺ Clear",  CLR_NEUTRAL);

        btnAdd.addActionListener(e -> handleAdd());
        btnUpdate.addActionListener(e -> handleUpdate());
        btnDelete.addActionListener(e -> handleDelete());
        btnClear.addActionListener(e -> clearFields());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        outer.add(btnPanel, BorderLayout.SOUTH);
        return outer;
    }
private JPanel buildTablePanel() {
        JPanel outer = new JPanel(new BorderLayout(0, 10));
        outer.setBackground(CLR_BG_RIGHT);
        outer.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel searchBar = new JPanel(new BorderLayout(8, 0));
        searchBar.setBackground(CLR_BG_RIGHT);

        JLabel searchIcon = new JLabel("\uD83D\uDD0D "); 
        searchIcon.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchBar.add(searchIcon, BorderLayout.WEST);

        tfSearch = new JTextField();
        tfSearch.setFont(FONT_FIELD);
        tfSearch.setToolTipText("Search by Medicine ID or Name");
        tfSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 210), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        tfSearch.addActionListener(e -> handleSearch());
        searchBar.add(tfSearch, BorderLayout.CENTER);

        JPanel searchBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        searchBtns.setBackground(CLR_BG_RIGHT);
        btnSearch  = makeButton("Search",   CLR_HEADER);
        btnShowAll = makeButton("Show All", CLR_NEUTRAL);
        btnSearch.setPreferredSize(new Dimension(90,  34));
        btnShowAll.setPreferredSize(new Dimension(90, 34));
        btnSearch.addActionListener(e -> handleSearch());
        btnShowAll.addActionListener(e -> refreshTable());
        searchBtns.add(btnSearch);
        searchBtns.add(btnShowAll);
        searchBar.add(searchBtns, BorderLayout.EAST);

        outer.add(searchBar, BorderLayout.NORTH);
        String[] columns = { "Medicine ID", "Name", "Category", "Price (BDT)", "Stock" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; 
                
            }
        };

        table = new JTable(tableModel);
        table.setFont(FONT_FIELD);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        table.setGridColor(new Color(210, 230, 230));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(CLR_TBL_HEADER);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable tbl, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                        tbl, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    
                    c.setBackground(row % 2 == 0 ? Color.WHITE : CLR_ROW_ALT);
                } else {
                    
                    c.setBackground(tbl.getSelectionBackground());
                }
                return c;
            }
        });

        table.getColumnModel().getColumn(0).setPreferredWidth(100); 
        table.getColumnModel().getColumn(1).setPreferredWidth(200); 
        table.getColumnModel().getColumn(2).setPreferredWidth(120); 
        table.getColumnModel().getColumn(3).setPreferredWidth(100); 
        table.getColumnModel().getColumn(4).setPreferredWidth(70);  

       
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) 
                populateFormFromSelection();
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 210)));
        outer.add(scroll, BorderLayout.CENTER);

        return outer;
    }

    
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 5));
        bar.setBackground(new Color(230, 245, 245));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(180, 210, 210)));

        lblStatus = new JLabel("Ready — system loaded successfully.");
        lblStatus.setFont(FONT_STATUS);
        lblStatus.setForeground(new Color(40, 80, 80));
        bar.add(lblStatus);
        return bar;
    }

    private void handleAdd() {
        String id       = tfId.getText().trim();
        String name     = tfName.getText().trim();
        String category = tfCategory.getText().trim();
        String price    = tfPrice.getText().trim();
        String stock    = tfStock.getText().trim();

        if (id.isEmpty() || name.isEmpty() || category.isEmpty()
                || price.isEmpty() || stock.isEmpty()) {
            showError("All fields are required. Please fill in every field before adding.");
            return;
        }

        if (!id.matches("\\d{8}")) {
            showError("Medicine ID must be exactly 8 digits (numbers only).");
            return;
        }

        if (MedicineFileIO.idExists(id)) {
            showError("Medicine ID \"" + id + "\" already exists. Each ID must be unique.");
            return;
        }

        try {
            double priceVal = Double.parseDouble(price);
            if (priceVal <= 0) {
                showError("Unit Price must be a positive number (e.g. 12.50).");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Unit Price must be a valid decimal number (e.g. 12.50).");
            return;
        }

        try {
            int stockVal = Integer.parseInt(stock);
            if (stockVal < 0) {
                showError("Stock Quantity cannot be negative.");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Stock Quantity must be a whole number (e.g. 200).");
            return;
        }

        try {
            MedicineFileIO.addMedicine(new Medicine(id, name, category, price, stock));
            refreshTable();   
            clearFields();    
            setStatus("Medicine \"" + name + "\" (ID: " + id + ") added successfully.");
        } catch (IOException ex) {
            showError("File error while adding: " + ex.getMessage());
        }
    }

    private void handleUpdate() {
        String id       = tfId.getText().trim();
        String name     = tfName.getText().trim();
        String category = tfCategory.getText().trim();
        String price    = tfPrice.getText().trim();
        String stock    = tfStock.getText().trim();

        if (id.isEmpty() || name.isEmpty() || category.isEmpty()
                || price.isEmpty() || stock.isEmpty()) {
            showError("All fields are required to update a record.");
            return;
        }

        if (!id.matches("\\d{8}")) {
            showError("Medicine ID must be exactly 8 digits.");
            return;
        }

        try {
            double priceVal = Double.parseDouble(price);
            if (priceVal <= 0) {
                showError("Unit Price must be a positive number.");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Unit Price must be a valid decimal number.");
            return;
        }

        try {
            int stockVal = Integer.parseInt(stock);
            if (stockVal < 0) {
                showError("Stock Quantity cannot be negative.");
                return;
            }
        } catch (NumberFormatException ex) {
            showError("Stock Quantity must be a whole number.");
            return;
        }

        try {
            boolean updated = MedicineFileIO.updateMedicine(
                    new Medicine(id, name, category, price, stock));

            if (updated) {
                refreshTable();
                clearFields();
                setStatus("Medicine ID \"" + id + "\" updated successfully.");
            } else {
                showError("No record found with ID \"" + id + "\". Update failed.");
            }
        } catch (IOException ex) {
            showError("File error while updating: " + ex.getMessage());
        }
    }

    private void handleDelete() {
        String id = tfId.getText().trim();


        if (id.isEmpty()) {
            showError("Please select a record from the table or enter a Medicine ID to delete.");
            return;
        }
        if (!id.matches("\\d{8}")) {
            showError("Medicine ID must be exactly 8 digits.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to permanently delete Medicine ID \"" + id + "\"?\n"
                        + "This action cannot be undone.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION)
            return; 
        try {
            boolean deleted = MedicineFileIO.deleteMedicine(id);

            if (deleted) {
                refreshTable();
                clearFields();
                setStatus("Medicine ID \"" + id + "\" deleted successfully.");
            } else {
                showError("No record found with ID \"" + id + "\". Nothing was deleted.");
            }
        } catch (IOException ex) {
            showError("File error while deleting: " + ex.getMessage());
        }
    }
    private void handleSearch() {
        String keyword = tfSearch.getText().trim();

        if (keyword.isEmpty()) {
            
            refreshTable();
            setStatus("Search cleared — showing all medicines.");
            return;
        }

        Object[][] results = MedicineFileIO.searchMedicines(keyword);
        loadTableData(results);

        if (results.length == 0) {
            setStatus("No medicines matched \"" + keyword + "\".");
        } else {
            setStatus("Found " + results.length + " medicine(s) matching \"" + keyword + "\".");
        }
    }

    private void refreshTable() {
        Object[][] data = MedicineFileIO.getAllMedicines(); 
        loadTableData(data);                               
        setStatus("Showing all " + data.length + " medicine record(s).");
    }

    private void loadTableData(Object[][] data) {
        tableModel.setRowCount(0); 

        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    
    private void populateFormFromSelection() {
        int selectedRow = table.getSelectedRow(); 

        if (selectedRow == -1)
            return; 

        tfId.setText((String) tableModel.getValueAt(selectedRow, 0));
        tfName.setText((String) tableModel.getValueAt(selectedRow, 1));
        tfCategory.setText((String) tableModel.getValueAt(selectedRow, 2));
        tfPrice.setText((String) tableModel.getValueAt(selectedRow, 3));
        tfStock.setText((String) tableModel.getValueAt(selectedRow, 4));

        setStatus("Loaded Medicine ID: " + tfId.getText() + " into the form. Edit fields, then click Update.");
    }
    private void clearFields() {
        tfId.setText("");       
        tfName.setText("");     
        tfCategory.setText(""); 
        tfPrice.setText("");    
        tfStock.setText("");    
        table.clearSelection(); 
        tfId.requestFocus();    
    }

    private void setStatus(String message) {
        lblStatus.setForeground(new Color(0, 80, 80)); 
        lblStatus.setText(message);
    }

    private void showError(String message) {
        lblStatus.setForeground(new Color(160, 0, 0)); 
        lblStatus.setText("Error: " + message);
        JOptionPane.showMessageDialog(this, message, "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(new Color(40, 80, 80)); 
        return lbl;
    }

    private JTextField makeTextField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(FONT_FIELD);
        tf.setToolTipText(placeholder);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 210), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return tf;
    }

    private JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);        
        btn.setBorderPainted(false);       
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        btn.setPreferredSize(new Dimension(120, 36));
        return btn;
    }
}
