/**
 * Created by Dylan on 2/18/2019.
 */

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Date;

public class csvConvert {
    private static String NEW_CSV = "telegramConverted.csv";

    public static StringBuilder readInMessage(StringBuilder contentBuilder, int num){
        String numS = Integer.toString(num);
        if(num==1){ numS = "";}
        try {
            BufferedReader in = new BufferedReader(new FileReader("messages"+numS+".html"));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
            System.out.println("ERROR: messages"+numS+".html not found");
        }
        return contentBuilder;
    }


    public static void processFile(String name1,String name2, String sMSG_AMOUNT) throws IOException {
        int MSG_AMOUNT = Integer.parseInt(sMSG_AMOUNT);
        int counter = 0;
        timeConvert timeConverter = new timeConvert();
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(NEW_CSV));
                CSVPrinter csvPrinter = new CSVPrinter(writer,
                        CSVFormat.DEFAULT.withHeader("name","month","day","hour","date","mstime","words","length","message")))
        {
            for (int j = 1; j <= MSG_AMOUNT; j++) {
                System.out.println("Processing #"+j);

                StringBuilder contentBuilder = new StringBuilder();
                contentBuilder=readInMessage(contentBuilder,j);

                String html = contentBuilder.toString();
                Document doc = Jsoup.parse(html);
                Elements links = doc.select("div.body");

                String name = "";
                for (int i = 1; i < links.size(); i++) {
                    if (!links.get(i).className().equals("forwarded body")) {
                        String date = links.get(i).select("div.pull_right").attr("title");
                        String tempName = links.get(i).select("div.from_name").text();
                        if (!tempName.equals(""))
                            name = tempName;

                        String message = links.get(i).select("div.text").text();
                        int msgWordCount = 0;
                        if (!message.equals("")) {
                            if(name.contains(name1)){
                                name = name1;
                                TextIterator readWords = new TextIterator();
                                readWords.readTextString(message);
                                while (readWords.hasNext()) {
                                    msgWordCount++;
                                    readWords.next();
                                }
                            }else{
                                name=name2;
                                TextIterator readWords = new TextIterator();
                                readWords.readTextString(message);
                                while (readWords.hasNext()) {
                                    msgWordCount++;
                                    readWords.next();
                                }
                            }

                            Date msgDate = timeConverter.parseDate(date);
                            message = message.replace(",","");
                            message = message.replace("\"","");
                            csvPrinter.printRecord(name, timeConverter.msToMonth(msgDate.getTime()),
                                    timeConverter.dayStringFormat(msgDate.getTime()),timeConverter.time(msgDate.getTime()),
                                    timeConverter.date(msgDate.getTime()),msgDate.getTime(),
                                    msgWordCount,message.length(),message.toLowerCase());
                            counter++;
                        }
                    }
                    csvPrinter.flush();
                }
            }
        }
        DecimalFormat df = new DecimalFormat("#,###");
        System.out.println("\n"+df.format(counter) + " total messages processed & converted");

    }

    public static void main(String[] args) {
        try{
            if(args.length >2) {
                if(args.length==4){
                    NEW_CSV = args[3]+".csv";
                }
                processFile(args[0], args[1], args[2]);
            }else{
                System.out.println("ERROR: Invalid # of args. (name1, name2, # of messages)");
            }
        }catch(IOException e){}
    }
}
