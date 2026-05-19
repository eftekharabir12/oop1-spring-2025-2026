
package pharmacymanagementsystem.fileio;
import java.io.*;
import pharmacymanagementsystem.entity.Medicine;

public class MedicineFileIO {

    private static final String FILE_NAME = "src/pharmacymanagementsystem/fileio/medicines.txt";

    private static final String TEMP_FILE = "src/pharmacymanagementsystem/fileio/temp.txt";

    public static void createFileIfNotExists() throws IOException {
        File file = new File(FILE_NAME); 

        
        if (!file.exists())
            file.createNewFile(); 
    }

    public static boolean idExists(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                Medicine m = Medicine.fromLine(line); 

                if (m != null && m.getId().equals(id))
                    return true;
            }
        } catch (IOException ignored) {

        }
        return false; 
    }

    
    public static int countRecords() {
        int count = 0; 

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                
                if (Medicine.fromLine(line) != null)
                    count++; 
            }
        } catch (IOException ignored) {
           
        }
        return count;
    }

    public static void addMedicine(Medicine m) throws IOException {

        try (PrintWriter pw = new PrintWriter(
                new BufferedWriter(new FileWriter(FILE_NAME, true)))) {
            pw.println(m.toLine()); 
        }
    }

   
    public static boolean updateMedicine(Medicine m) throws IOException {
        File inputFile = new File(FILE_NAME); 
        File tempFile  = new File(TEMP_FILE); 
        boolean found  = false;               
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                Medicine existing = Medicine.fromLine(line); 
                
                if (existing != null && existing.getId().equals(m.getId())) {
                    bw.write(m.toLine()); 
                    found = true;        
                } else {
                    bw.write(line);     
                }
                bw.newLine(); 
            }
        }

        if (found) {
           
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                throw new IOException("Could not finalize update."); 
            }
        } else {
           
            tempFile.delete();
        }
        return found;
    }

    
    public static boolean deleteMedicine(String id) throws IOException {
        File inputFile = new File(FILE_NAME); 
        File tempFile  = new File(TEMP_FILE); 
        boolean found  = false;             

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                Medicine existing = Medicine.fromLine(line); 

                if (existing != null && existing.getId().equals(id)) {
                    found = true;
                    continue; 
                }

                
                bw.newLine(); 
            }
        }

        if (found) {
           
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                throw new IOException("Could not finalize delete."); 
            }
        } else {
           
            tempFile.delete();
        }
        return found;
    }

   
    public static Object[][] getAllMedicines() {
        int total        = countRecords();       
        Object[][] rows  = new Object[total][5]; 
        int idx          = 0;                   
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null && idx < total) {
                Medicine m = Medicine.fromLine(line); 

                if (m != null) { 
                    Object[] row  = m.toRow();   
                    rows[idx][0]  = row[0];       
                    rows[idx][1]  = row[1];      
                    rows[idx][2]  = row[2];       
                    rows[idx][3]  = row[3];        
                    rows[idx][4]  = row[4];       
                    idx++;                         
                }
            }
        } catch (IOException ignored) {
           
        }
        return rows; 
    }

    
    public static Object[][] searchMedicines(String keyword) {
       
        String kw = keyword.toLowerCase();

      
        int matchCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                Medicine m = Medicine.fromLine(line); 

                if (m != null && (m.getId().toLowerCase().contains(kw)
                        || m.getName().toLowerCase().contains(kw))) {
                    matchCount++; 
                }
            }
        } catch (IOException ignored) {
           
        }

       
        Object[][] results = new Object[matchCount][5]; 
        int idx = 0;                                    
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

           
            while ((line = br.readLine()) != null && idx < matchCount) {
                Medicine m = Medicine.fromLine(line); 

               
                if (m != null && (m.getId().toLowerCase().contains(kw)
                        || m.getName().toLowerCase().contains(kw))) {
                    Object[] row     = m.toRow();   
                    results[idx][0]  = row[0];       
                    results[idx][1]  = row[1];       
                    results[idx][2]  = row[2];       
                    results[idx][3]  = row[3];        
                    results[idx][4]  = row[4];        
                    idx++;                            
                }
            }
        } catch (IOException ignored) {
           
        }
        return results;
    }
}
