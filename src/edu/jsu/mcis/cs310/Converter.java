package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

public class Converter {
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String result = ""; // default return value; replace later!
        
        try {
        
            // INSERT YOUR CODE HERE
            CSVReader csvReader= new CSVReader (new StringReader(csvString));
            List <String[]> csvData = csvReader.readAll();
            csvReader.close();
            
            if (csvData.isEmpty()){
                return result;
            }
            
            JsonObject jsonObject = new JsonObject();
            JsonArray prodNums = new JsonArray();
            JsonArray colHeading = new JsonArray();
            JsonArray data = new JsonArray();
            
            //Headers
            String[] headers = csvData.get(0);
            colHeading.addAll(Arrays.asList(headers));
            
            //data
            for (int i = 1; i < csvData.size(); i++) {
                String[] row = csvData.get(i);
                prodNums.add(row[0]);
                
                JsonArray rowData = new JsonArray();
                               
                rowData.add(row[1]);
                rowData.add(Integer.parseInt(row[2]));
                rowData.add(Integer.parseInt(row[3]));
                rowData.add(row[4]);
                rowData.add(row[5]);
                rowData.add(row[6]);
                
                data.add(rowData);  
            }
            
            jsonObject.put("ProdNums", prodNums);
            jsonObject.put("ColHeadings", colHeading);
            jsonObject.put("Data", data);
            
            result = Jsoner.serialize(jsonObject);
           
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        String result = ""; // default return value; replace later!
        
        try {
            
            // INSERT YOUR CODE HERE
            JsonObject json = Jsoner.deserialize(jsonString, new JsonObject());
            List<Object> colHeadingsObj = (List<Object>) json.get("ColHeadings");
            List<String> colHeadings = new ArrayList<>();
                for (Object obj : colHeadingsObj){
                    colHeadings.add(obj.toString());
                }
            List<String> prodNum = (List<String>) json.get("ProdNums");
            List<List<Object>> data = (List<List<Object>>) json.get("Data");
            
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer);
            
            csvWriter.writeNext(colHeadings.toArray(new String[0]));
            
            for (int i = 0; i < data.size(); i++){
                JsonArray row = (JsonArray) data.get(i);
                String[] rowArray = new String[colHeadings.size()];
                
                rowArray[0] = prodNum.get(i);
                
                
                
                for (int j = 0; j<row.size(); j++){
                    String colName = colHeadings.get(j+1);
                    
                    if(colName.equals("Episode")){
                        rowArray[j + 1] = String.format("%02d", Integer.parseInt(row.getString(j).toString()));
                    
                    }
                    else{
                        rowArray[j + 1] = row.get(j).toString();
                    }
                }   
                
                csvWriter.writeNext(rowArray);
            }
            
            csvWriter.flush();
            result = writer.toString();
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
}
