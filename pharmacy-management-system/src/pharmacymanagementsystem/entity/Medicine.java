
package pharmacymanagementsystem.entity;


public class Medicine {

   

    private String id;       
    private String name;     
    private String category; 
    private String price;  
    private String stock;   

    
    public Medicine(String id, String name, String category, String price, String stock) {
        this.id       = id;     
        this.name     = name;   
        this.category = category;
        this.price    = price; 
        this.stock    = stock;
    }

    
    public String getId() {
        return id;
    }

    
    public String getName() {
        return name; 
    }

    
    public String getCategory() {
        return category; 
    }

   
    public String getPrice() {
        return price;
    }


    public String getStock() {
        return stock;
    }

   
    public void setId(String id) {
        this.id = id; 
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public void setPrice(String price) {
        this.price = price;
    }

 
    public void setStock(String stock) {
        this.stock = stock;
    }

   
    public String toLine() {
       
        return id + "," + name + "," + category + "," + price + "," + stock;
    }

   
    public static Medicine fromLine(String line) {
      
        if (line == null || line.trim().isEmpty())
            return null;

       
        String[] data = line.split(",", -1);

        
        if (data.length != 5)
            return null; 
        return new Medicine(data[0], data[1], data[2], data[3], data[4]);
    }

   
    public Object[] toRow() {
       
        return new Object[] { id, name, category, price, stock };
    }
}
