package Chessbot2.botBehaviour;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * OpeningTable
 */
public class OpeningTable 
{
    /**This file reads a csv file
     * 
     * @param path the path of the csv file to read
     * @return
     */
    public Opening[] readCSV(String path) 
    {
        BufferedReader csvReader;
        String row;
        List<Opening> returnList = new ArrayList<>();
        try 
        {
            csvReader = new BufferedReader(new FileReader(path));
            try 
            {
                while ((row = csvReader.readLine()) != null) 
                {
                    String[] data = row.split(",");
                    returnList.add(new Opening(Arrays.copyOfRange(data, 0, 7), Arrays.copyOfRange(data, 8, 15), (Integer.parseInt(data[16]))));
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } 
        catch (FileNotFoundException e1) 
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return (Opening[]) returnList.toArray();
    }
}