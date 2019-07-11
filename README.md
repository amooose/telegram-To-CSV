# telegram to CSV
convert telegram's exported .HTML's to a single .csv

# Usage
1. Export all desired messages with nothing selected under Chat Export Settings (So only text will be exported)
2. Place telegramToCSV in the folder with all the messages*.html
3. run jar with the command 
```java -jar telegramToCSV.jar name1 name2 number OPTIONAL:outputName```  
    ```name1```: Your exact first name in telegram (However if it is John S, you can just put John)  
    ```name2```: chat recipient  
    ```number```: the highest number messages*.html goes to, so if the last html is messages59.html, put 59.  
    ```outputName```: the output name of the .csv, blank will result in the name telegramConverted.csv
4. The .csv will be produced in the same folder as the jar.
