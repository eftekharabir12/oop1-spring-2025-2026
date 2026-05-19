// Declares that this class belongs to the "entity" package (folder)
package pharmacymanagementsystem.entity;

/**
 * Medicine - Data model representing a single medicine record.
 *
 * Each medicine has five pieces of information:
 *   id       - an 8-digit numeric string that uniquely identifies the medicine
 *   name     - the medicine's brand/generic name
 *   category - therapeutic category (e.g. Antibiotic, Analgesic)
 *   price    - unit price stored as a String (validated as a positive double)
 *   stock    - number of units in stock stored as a String (validated as a non-negative int)
 *
 * This class also provides helper methods to convert a Medicine to/from
 * the CSV (comma-separated) format used in the data file (medicines.txt).
 */
public class Medicine {

    // --- Fields (private so only this class can access them directly) ---

    private String id;       // Unique 8-digit medicine ID, e.g. "10000001"
    private String name;     // Medicine name, e.g. "Paracetamol 500mg"
    private String category; // Therapeutic category, e.g. "Analgesic"
    private String price;    // Unit price as text, e.g. "12.50"
    private String stock;    // Units in stock as text, e.g. "200"

    // --- Constructor ---

    /**
     * Creates a new Medicine with all five required fields.
     *
     * @param id       Unique 8-digit medicine ID.
     * @param name     Medicine brand or generic name.
     * @param category Therapeutic category.
     * @param price    Unit price (numeric string).
     * @param stock    Stock quantity (numeric string).
     */
    public Medicine(String id, String name, String category, String price, String stock) {
        this.id       = id;       // Assign the provided id to this object's id field
        this.name     = name;     // Assign the provided name
        this.category = category; // Assign the provided category
        this.price    = price;    // Assign the provided price
        this.stock    = stock;    // Assign the provided stock
    }

    // --- Getters (read-only access to private fields) ---

    /**
     * Returns the medicine's ID.
     *
     * @return 8-digit medicine ID string.
     */
    public String getId() {
        return id; // Return the id field value
    }

    /**
     * Returns the medicine's name.
     *
     * @return Medicine name string.
     */
    public String getName() {
        return name; // Return the name field value
    }

    /**
     * Returns the medicine's therapeutic category.
     *
     * @return Category string.
     */
    public String getCategory() {
        return category; // Return the category field value
    }

    /**
     * Returns the medicine's unit price.
     *
     * @return Price as a string (e.g. "12.50").
     */
    public String getPrice() {
        return price; // Return the price field value
    }

    /**
     * Returns the medicine's stock quantity.
     *
     * @return Stock as a string (e.g. "200").
     */
    public String getStock() {
        return stock; // Return the stock field value
    }

    // --- Setters (allow controlled modification of private fields) ---

    /**
     * Updates the medicine's ID.
     *
     * @param id New 8-digit ID string.
     */
    public void setId(String id) {
        this.id = id; // Replace the current id with the new one
    }

    /**
     * Updates the medicine's name.
     *
     * @param name New name string.
     */
    public void setName(String name) {
        this.name = name; // Replace the current name
    }

    /**
     * Updates the medicine's category.
     *
     * @param category New category string.
     */
    public void setCategory(String category) {
        this.category = category; // Replace the current category
    }

    /**
     * Updates the medicine's price.
     *
     * @param price New price string.
     */
    public void setPrice(String price) {
        this.price = price; // Replace the current price
    }

    /**
     * Updates the medicine's stock quantity.
     *
     * @param stock New stock string.
     */
    public void setStock(String stock) {
        this.stock = stock; // Replace the current stock
    }

    // --- File serialization helpers ---

    /**
     * Converts this Medicine into a single CSV (comma-separated) line
     * suitable for writing to the medicines.txt data file.
     *
     * Example output: "10000001,Paracetamol 500mg,Analgesic,12.50,200"
     *
     * @return A CSV string with id, name, category, price, and stock separated by commas.
     */
    public String toLine() {
        // Concatenate all five fields with commas between them
        return id + "," + name + "," + category + "," + price + "," + stock;
    }

    /**
     * Parses a CSV line read from the data file and creates a Medicine object.
     *
     * This is a static factory method — it belongs to the class, not an instance,
     * so it can be called without first creating a Medicine.
     *
     * Example input: "10000001,Paracetamol 500mg,Analgesic,12.50,200"
     *
     * @param line A CSV string from the data file (may be null or malformed).
     * @return A new Medicine if the line is valid; null otherwise.
     */
    public static Medicine fromLine(String line) {
        // Reject null or blank lines
        if (line == null || line.trim().isEmpty())
            return null;

        // Split the line on commas; limit=-1 keeps empty trailing fields
        String[] data = line.split(",", -1);

        // A valid line must have exactly 5 fields: id, name, category, price, stock
        if (data.length != 5)
            return null; // Malformed line — ignore it

        // Build and return a new Medicine from the parsed fields
        return new Medicine(data[0], data[1], data[2], data[3], data[4]);
    }

    /**
     * Converts this Medicine into an Object array row for display in a JTable.
     *
     * JTable rows are represented as Object arrays where each element
     * corresponds to a column: [ID, Name, Category, Price, Stock].
     *
     * @return Object array with five elements ready for table insertion.
     */
    public Object[] toRow() {
        // Return a new array containing all five fields in column order
        return new Object[] { id, name, category, price, stock };
    }
}
