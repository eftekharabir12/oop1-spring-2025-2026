// Declares that this class belongs to the "gui" package (folder)
package pharmacymanagementsystem.gui;

// Import all Swing and AWT components needed for the GUI
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

// Import the data model and file I/O classes from sibling packages
import pharmacymanagementsystem.entity.Medicine;
import pharmacymanagementsystem.fileio.MedicineFileIO;

/**
 * PharmacyGUI - Main graphical user interface for the Pharmacy Management System.
 *
 * Layout overview:
 *  +----------------------------------------------------------+
 *  |  HEADER PANEL  (logo + title)                            |
 *  +------------------+---------------------------------------+
 *  |  LEFT PANEL      |  RIGHT PANEL                          |
 *  |  (Input Form)    |  (Search bar + JTable)                |
 *  |                  |                                       |
 *  |  [ID]            |  [Search field] [Search] [Show All]   |
 *  |  [Name]          |  +---------------------------------+   |
 *  |  [Category]      |  | ID | Name | Category | Price | Stock |
 *  |  [Price]         |  +---------------------------------+   |
 *  |  [Stock]         |  |  ...rows...                    |   |
 *  |                  |  +---------------------------------+   |
 *  |  [Add][Update]   |                                       |
 *  |  [Delete][Clear] |                                       |
 *  +------------------+---------------------------------------+
 *  |  STATUS BAR                                               |
 *  +----------------------------------------------------------+
 *
 * Colour palette: deep teal header, white form panel, light grey table area.
 */
public class PharmacyGUI extends JFrame {

    // =========================================================================
    // COLOUR & FONT CONSTANTS
    // =========================================================================

    // Deep pharmacy-green used for the header and button accents
    private static final Color CLR_HEADER     = new Color(0, 102, 102);
    // Slightly lighter teal for hover/secondary use
    private static final Color CLR_ACCENT     = new Color(0, 140, 130);
    // Danger red for the Delete button
    private static final Color CLR_DANGER     = new Color(192, 57, 43);
    // Neutral grey for the Clear button
    private static final Color CLR_NEUTRAL    = new Color(90, 90, 90);
    // Light background for the right panel
    private static final Color CLR_BG_RIGHT   = new Color(245, 248, 248);
    // Alternating row colour for the table
    private static final Color CLR_ROW_ALT    = new Color(224, 242, 241);
    // White background for the form panel
    private static final Color CLR_BG_FORM    = Color.WHITE;
    // Table header background
    private static final Color CLR_TBL_HEADER = new Color(0, 102, 102);

    private static final Font FONT_TITLE  = new Font("SansSerif", Font.BOLD,  22);
    private static final Font FONT_LABEL  = new Font("SansSerif", Font.BOLD,  13);
    private static final Font FONT_FIELD  = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_BTN    = new Font("SansSerif", Font.BOLD,  13);
    private static final Font FONT_STATUS = new Font("SansSerif", Font.PLAIN, 12);

    // =========================================================================
    // FORM FIELDS
    // =========================================================================

    private JTextField tfId;       // Medicine ID input (exactly 8 digits)
    private JTextField tfName;     // Medicine name input
    private JTextField tfCategory; // Therapeutic category input
    private JTextField tfPrice;    // Unit price input (positive decimal)
    private JTextField tfStock;    // Stock quantity input (non-negative integer)

    // =========================================================================
    // SEARCH & TABLE COMPONENTS
    // =========================================================================

    private JTextField       tfSearch;     // Keyword search field
    private JTable           table;        // Displays medicine records
    private DefaultTableModel tableModel;  // Backing data model for the table

    // =========================================================================
    // CONTROL BUTTONS
    // =========================================================================

    private JButton btnAdd;     // Adds a new medicine record
    private JButton btnUpdate;  // Updates the selected/edited record
    private JButton btnDelete;  // Deletes the selected record (with confirmation)
    private JButton btnClear;   // Clears all input fields
    private JButton btnSearch;  // Triggers a keyword search
    private JButton btnShowAll; // Reloads the full medicine list

    // =========================================================================
    // STATUS BAR
    // =========================================================================

    private JLabel lblStatus; // Shows feedback messages at the bottom of the window

    // =========================================================================
    // CONSTRUCTOR — builds the entire window
    // =========================================================================

    /**
     * Constructs and displays the Pharmacy Management System window.
     *
     * Sets the window title, size, default-close behaviour, and centres it on screen.
     * Then assembles the header, left form panel, right table panel, and status bar.
     */
    public PharmacyGUI() {
        // --- Window properties ---
        setTitle("Pharmacy Management System"); // Window title bar text
        setSize(1050, 650);                     // Initial window size (width x height)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // End the JVM when X is clicked
        setLayout(new BorderLayout(0, 0));      // Root layout: North/Center/South regions

        // --- Centre the window on screen ---
        setLocationRelativeTo(null); // null = centre of the primary display

        // --- Assemble the layout regions ---
        add(buildHeaderPanel(), BorderLayout.NORTH);   // Green header strip
        add(buildCenterPanel(), BorderLayout.CENTER);  // Left form + right table
        add(buildStatusBar(),   BorderLayout.SOUTH);   // Status feedback bar

        // --- Populate the table with any existing records ---
        refreshTable();

        // --- Make the window visible ---
        setVisible(true);
    }

    // =========================================================================
    // HEADER PANEL
    // =========================================================================

    /**
     * Builds the top header panel containing a pill icon and the system title.
     *
     * @return A fully constructed JPanel for the NORTH border region.
     */
    private JPanel buildHeaderPanel() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14));
        header.setBackground(CLR_HEADER); // Deep teal background
        header.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // Pill/cross emoji used as a lightweight icon (no external image needed)
        JLabel icon = new JLabel("\u2695"); // ⚕ caduceus symbol
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

    // =========================================================================
    // CENTER PANEL (left form + right table)
    // =========================================================================

    /**
     * Builds the main centre panel, split into a left form and a right table area.
     *
     * @return A JSplitPane with the form on the left and the table on the right.
     */
    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout());

        // Left and right panels share horizontal space in a fixed split
        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                buildFormPanel(),  // Left: input form
                buildTablePanel()  // Right: search + table
        );
        split.setDividerLocation(320);   // Fixed divider position in pixels
        split.setDividerSize(4);         // Thin divider line
        split.setEnabled(false);         // Prevent the user from dragging the divider
        split.setBorder(null);

        center.add(split, BorderLayout.CENTER);
        return center;
    }

    // =========================================================================
    // LEFT — INPUT FORM PANEL
    // =========================================================================

    /**
     * Builds the left-side input form with five labelled fields and four action buttons.
     *
     * @return A JPanel containing the complete input form.
     */
    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(CLR_BG_FORM);
        outer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // --- Section title ---
        JLabel sectionTitle = new JLabel("Medicine Details");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        sectionTitle.setForeground(CLR_HEADER);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        outer.add(sectionTitle, BorderLayout.NORTH);

        // --- Fields panel using GridBagLayout for clean label/field alignment ---
        JPanel fields = new JPanel(new GridBagLayout());
        fields.setBackground(CLR_BG_FORM);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 0, 7, 8); // Vertical + horizontal padding
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Row 0 — Medicine ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        fields.add(makeLabel("Medicine ID *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        tfId = makeTextField("Enter 8-digit ID");
        // Restrict input: only allow digits and cap at 8 characters
        tfId.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Block non-digit characters
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume(); // Swallow the keystroke so it does not appear
                    return;
                }
                // Block if already at 8 characters
                if (tfId.getText().length() >= 8)
                    e.consume();
            }
        });
        fields.add(tfId, gbc);

        // Row 1 — Name
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        fields.add(makeLabel("Medicine Name *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        tfName = makeTextField("e.g. Paracetamol 500mg");
        fields.add(tfName, gbc);

        // Row 2 — Category
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        fields.add(makeLabel("Category *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        tfCategory = makeTextField("e.g. Analgesic");
        fields.add(tfCategory, gbc);

        // Row 3 — Price
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        fields.add(makeLabel("Unit Price (BDT) *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        tfPrice = makeTextField("e.g. 12.50");
        fields.add(tfPrice, gbc);

        // Row 4 — Stock
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        fields.add(makeLabel("Stock Qty *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        tfStock = makeTextField("e.g. 200");
        fields.add(tfStock, gbc);

        // Push remaining space downward so fields stay at the top
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.weighty = 1; gbc.fill = GridBagConstraints.BOTH;
        fields.add(Box.createVerticalGlue(), gbc);

        outer.add(fields, BorderLayout.CENTER);

        // --- Button row ---
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        btnPanel.setBackground(CLR_BG_FORM);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        btnAdd    = makeButton("+ Add",    CLR_ACCENT);
        btnUpdate = makeButton("✎ Update", CLR_HEADER);
        btnDelete = makeButton("✕ Delete", CLR_DANGER);
        btnClear  = makeButton("↺ Clear",  CLR_NEUTRAL);

        // Attach action handlers
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

    // =========================================================================
    // RIGHT — SEARCH + TABLE PANEL
    // =========================================================================

    /**
     * Builds the right-side panel containing the search bar and the data table.
     *
     * @return A JPanel with a search toolbar at the top and a scrollable JTable below.
     */
    private JPanel buildTablePanel() {
        JPanel outer = new JPanel(new BorderLayout(0, 10));
        outer.setBackground(CLR_BG_RIGHT);
        outer.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // --- Search toolbar ---
        JPanel searchBar = new JPanel(new BorderLayout(8, 0));
        searchBar.setBackground(CLR_BG_RIGHT);

        JLabel searchIcon = new JLabel("\uD83D\uDD0D "); // 🔍 emoji
        searchIcon.setFont(new Font("SansSerif", Font.PLAIN, 16));
        searchBar.add(searchIcon, BorderLayout.WEST);

        tfSearch = new JTextField();
        tfSearch.setFont(FONT_FIELD);
        tfSearch.setToolTipText("Search by Medicine ID or Name");
        tfSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 210, 210), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        // Allow pressing Enter in the search field to trigger search
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

        // --- Table ---
        String[] columns = { "Medicine ID", "Name", "Category", "Price (BDT)", "Stock" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // Cells are not directly editable; editing goes through the form
            }
        };

        table = new JTable(tableModel);
        table.setFont(FONT_FIELD);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only one row at a time
        table.setGridColor(new Color(210, 230, 230));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Styled table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(CLR_TBL_HEADER);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false); // Prevent column drag-reordering

        // Custom renderer for alternating row colours
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable tbl, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                        tbl, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    // Alternate between white and light teal rows
                    c.setBackground(row % 2 == 0 ? Color.WHITE : CLR_ROW_ALT);
                } else {
                    // Selected row uses the default blue highlight
                    c.setBackground(tbl.getSelectionBackground());
                }
                return c;
            }
        });

        // Column width hints (the table will still resize with the window)
        table.getColumnModel().getColumn(0).setPreferredWidth(100); // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        table.getColumnModel().getColumn(2).setPreferredWidth(120); // Category
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Price
        table.getColumnModel().getColumn(4).setPreferredWidth(70);  // Stock

        // When a row is clicked, populate the input form with that row's data
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) // Ignore intermediate events during drag
                populateFormFromSelection();
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 210)));
        outer.add(scroll, BorderLayout.CENTER);

        return outer;
    }

    // =========================================================================
    // STATUS BAR
    // =========================================================================

    /**
     * Builds the slim status bar displayed at the very bottom of the window.
     *
     * @return A JPanel containing the status label.
     */
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

    // =========================================================================
    // ACTION HANDLERS
    // =========================================================================

    /**
     * Validates the input fields and adds a new medicine record to the database.
     *
     * Validation rules applied (in order):
     *   1. All five fields must be non-empty.
     *   2. ID must be exactly 8 digits.
     *   3. ID must not already exist in the database.
     *   4. Price must be a positive decimal number.
     *   5. Stock must be a non-negative integer.
     */
    private void handleAdd() {
        // Step 1: Read and trim all field values
        String id       = tfId.getText().trim();
        String name     = tfName.getText().trim();
        String category = tfCategory.getText().trim();
        String price    = tfPrice.getText().trim();
        String stock    = tfStock.getText().trim();

        // Step 2: Check all fields are filled
        if (id.isEmpty() || name.isEmpty() || category.isEmpty()
                || price.isEmpty() || stock.isEmpty()) {
            showError("All fields are required. Please fill in every field before adding.");
            return;
        }

        // Step 3: Validate ID — must be exactly 8 digits
        if (!id.matches("\\d{8}")) {
            showError("Medicine ID must be exactly 8 digits (numbers only).");
            return;
        }

        // Step 4: Check for duplicate ID
        if (MedicineFileIO.idExists(id)) {
            showError("Medicine ID \"" + id + "\" already exists. Each ID must be unique.");
            return;
        }

        // Step 5: Validate price — must be a positive number
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

        // Step 6: Validate stock — must be a non-negative integer
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

        // All validation passed — create and save the Medicine object
        try {
            MedicineFileIO.addMedicine(new Medicine(id, name, category, price, stock));
            refreshTable();   // Reload the table to show the new record
            clearFields();    // Reset the form for the next entry
            setStatus("Medicine \"" + name + "\" (ID: " + id + ") added successfully.");
        } catch (IOException ex) {
            showError("File error while adding: " + ex.getMessage());
        }
    }

    /**
     * Validates the input fields and updates the matching medicine record.
     *
     * The ID field identifies which record to update; it must already exist.
     */
    private void handleUpdate() {
        // Read and trim all field values
        String id       = tfId.getText().trim();
        String name     = tfName.getText().trim();
        String category = tfCategory.getText().trim();
        String price    = tfPrice.getText().trim();
        String stock    = tfStock.getText().trim();

        // All fields are required
        if (id.isEmpty() || name.isEmpty() || category.isEmpty()
                || price.isEmpty() || stock.isEmpty()) {
            showError("All fields are required to update a record.");
            return;
        }

        // ID must be exactly 8 digits
        if (!id.matches("\\d{8}")) {
            showError("Medicine ID must be exactly 8 digits.");
            return;
        }

        // Validate price
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

        // Validate stock
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

        // Attempt to update the record
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

    /**
     * Deletes the medicine whose ID is currently in the ID field.
     *
     * Prompts the user for confirmation before performing the deletion.
     */
    private void handleDelete() {
        String id = tfId.getText().trim();

        // ID field must be filled
        if (id.isEmpty()) {
            showError("Please select a record from the table or enter a Medicine ID to delete.");
            return;
        }

        // ID must be exactly 8 digits
        if (!id.matches("\\d{8}")) {
            showError("Medicine ID must be exactly 8 digits.");
            return;
        }

        // --- Confirmation dialog before permanent deletion ---
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to permanently delete Medicine ID \"" + id + "\"?\n"
                        + "This action cannot be undone.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION)
            return; // User cancelled — do nothing

        // Perform the deletion
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

    /**
     * Searches the database using the keyword in the search field.
     *
     * Matching is performed against Medicine ID and Name (case-insensitive partial match).
     * An empty search field defaults to showing all records.
     */
    private void handleSearch() {
        String keyword = tfSearch.getText().trim();

        if (keyword.isEmpty()) {
            // If the search field is blank, reload the full list
            refreshTable();
            setStatus("Search cleared — showing all medicines.");
            return;
        }

        // Perform the search and load results into the table
        Object[][] results = MedicineFileIO.searchMedicines(keyword);
        loadTableData(results);

        if (results.length == 0) {
            setStatus("No medicines matched \"" + keyword + "\".");
        } else {
            setStatus("Found " + results.length + " medicine(s) matching \"" + keyword + "\".");
        }
    }

    // =========================================================================
    // HELPER METHODS
    // =========================================================================

    /**
     * Reloads all medicine records from the database into the table.
     *
     * Called after every Add / Update / Delete and when "Show All" is clicked.
     */
    private void refreshTable() {
        Object[][] data = MedicineFileIO.getAllMedicines(); // Fetch current records
        loadTableData(data);                               // Push data into the model
        setStatus("Showing all " + data.length + " medicine record(s).");
    }

    /**
     * Replaces the table's current data with the supplied 2D array.
     *
     * Clears all existing rows first to avoid duplicates.
     *
     * @param data 2D Object array where each row is a medicine record.
     */
    private void loadTableData(Object[][] data) {
        tableModel.setRowCount(0); // Remove all existing rows from the model

        // Add each record as a new row
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    /**
     * Reads the selected row from the table and populates the input form fields.
     *
     * Called automatically whenever the user clicks a row in the table.
     */
    private void populateFormFromSelection() {
        int selectedRow = table.getSelectedRow(); // -1 if no row is selected

        if (selectedRow == -1)
            return; // Nothing selected — leave the form as is

        // Pull each cell value from the selected row and put it in the matching field
        tfId.setText((String) tableModel.getValueAt(selectedRow, 0));
        tfName.setText((String) tableModel.getValueAt(selectedRow, 1));
        tfCategory.setText((String) tableModel.getValueAt(selectedRow, 2));
        tfPrice.setText((String) tableModel.getValueAt(selectedRow, 3));
        tfStock.setText((String) tableModel.getValueAt(selectedRow, 4));

        setStatus("Loaded Medicine ID: " + tfId.getText() + " into the form. Edit fields, then click Update.");
    }

    /**
     * Clears all five input form fields and deselects any table row.
     */
    private void clearFields() {
        tfId.setText("");       // Clear the ID field
        tfName.setText("");     // Clear the Name field
        tfCategory.setText(""); // Clear the Category field
        tfPrice.setText("");    // Clear the Price field
        tfStock.setText("");    // Clear the Stock field
        table.clearSelection(); // Deselect any highlighted table row
        tfId.requestFocus();    // Return focus to the first field for quick entry
    }

    /**
     * Sets the status bar message to a success/informational message (dark teal text).
     *
     * @param message The message to display.
     */
    private void setStatus(String message) {
        lblStatus.setForeground(new Color(0, 80, 80)); // Dark teal for normal messages
        lblStatus.setText(message);
    }

    /**
     * Sets the status bar message to an error message (dark red text) AND
     * shows a popup dialog so the user cannot miss it.
     *
     * @param message The error description.
     */
    private void showError(String message) {
        lblStatus.setForeground(new Color(160, 0, 0)); // Dark red for errors
        lblStatus.setText("Error: " + message);
        JOptionPane.showMessageDialog(this, message, "Validation Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // =========================================================================
    // FACTORY HELPERS — avoid repeated boilerplate
    // =========================================================================

    /**
     * Creates a consistently styled label for the form.
     *
     * @param text Label text.
     * @return Styled JLabel.
     */
    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(new Color(40, 80, 80)); // Dark teal for form labels
        return lbl;
    }

    /**
     * Creates a consistently styled text field with placeholder-style tooltip.
     *
     * @param placeholder Tooltip text shown on hover.
     * @return Styled JTextField.
     */
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

    /**
     * Creates a consistently styled action button.
     *
     * @param text  Button label.
     * @param color Background colour.
     * @return Styled JButton.
     */
    private JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);        // Remove the focus rectangle
        btn.setBorderPainted(false);       // Flat look — no border line
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand pointer on hover
        btn.setPreferredSize(new Dimension(120, 36));
        return btn;
    }
}
