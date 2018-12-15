package com.grupo.cuatro;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.Block;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;



import javax.print.Doc;

public class Elastic {
    private ArrayList<Integer> resultList;
    private int positiveResult;
    private int negativeResult;
    private int neutralResult;
    private ArrayList<Integer> resultListGeneral;
    private int positiveResultGeneral;
    private int negativeResultGeneral;
    private int neutralResultGeneral;


    public void indexCreate()
    {
        try{
            Directory dir = FSDirectory.open(Paths.get("indice/"));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(OpenMode.CREATE);
            IndexWriter writer = new IndexWriter(dir,iwc);
            //MONGUITO
            MongoConnection mongo = MongoConnection.getMongo();
            mongo.OpenMongoClient();
            DBCursor cursor = mongo.getTweets();
            System.out.println(cursor);
            Document doc = null;
            
            while (cursor.hasNext()) {
                DBObject cur = cursor.next();
                if (cur.get("retweet").toString().equals("false")) {
                    doc = new Document();
                    doc.add(new StringField("id", cur.get("_id").toString(), Field.Store.YES));
                    doc.add(new TextField("text", cur.get("text").toString(), Field.Store.YES));
                    //doc.add(new StringField("analysis", cur.get("analysis").toString(), Field.Store.YES));
                    //doc.add(new StringField("finalCountry",cur.get("finalCountry").toString(),Field.Store.YES));
                    doc.add(new StringField("userScreenName", cur.get("userScreenName").toString(), Field.Store.YES));
                    doc.add(new StringField("userFollowersCount", cur.get("userFollowersCount").toString(), Field.Store.YES));
                    doc.add(new StringField("favoriteCount", cur.get("favoriteCount").toString(), Field.Store.YES));

                    if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                        System.out.println("Indexando el tweet: " + doc.get("text") + "\n");
                        writer.addDocument(doc);
                        System.out.println(doc);
                    } else {
                        System.out.println("Actualizando el tweet: " + doc.get("text") + "\n");
                        writer.updateDocument(new Term("text" + cur.get("text")), doc);
                        System.out.println(doc);
                    }
                }
            }
            cursor.close();
            writer.close();
        }
        catch(IOException ioe){
            System.out.println(" Error en "+ ioe.getClass() + "\n mensaje: " + ioe.getMessage());
        }
        public ArrayList<Integer> getCantidad(String Deporte)
        {
            resultList = null ;
            total = 0;

            try{
                IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
                IndexSearcher searcher = new IndexSearcher(reader);
                Analyzer analyzer = new StandardAnalyzer();
                this.resultList = new ArrayList<>();
                QueryParser parser = new QueryParser("text",analyzer);
                Query query = parser.parse(Deporte);
                TopDocs result = searcher.search(query,25000);
                ScoreDoc[] hits =result.scoreDocs;

                for (int i=0; i < hits.length; i++) {
                    positiveResult++;
                    reader.close();
                }
            catch(IOException | ParseException ex)
            {
                Logger.getLogger(Lucene.class.getName()).log(Level.SEVERE,null,ex);

            }
            resultList.add(positiveResult);
            resultList.add(negativeResult);
            resultList.add(neutralResult);

            return resultList;
        }
    }
}