package com.grupo.cuatro;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.DateTools;
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
import org.elasticsearch.search.DocValueFormat;
import org.joda.time.DateTime;


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


    public void indexCreate() {
        try {
            Directory dir = FSDirectory.open(Paths.get("indice/"));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(OpenMode.CREATE);
            IndexWriter writer = new IndexWriter(dir, iwc);
            //MONGUITO
            MongoConnection mongo = MongoConnection.getMongo();
            mongo.OpenMongoClient();
            DBCursor cursor = mongo.getTweets();
            System.out.println(cursor);
            Document doc = null;
            Document user = null;

            /*DB db = mongo.getDB("twitter");
            CommandResult result = db.command("db.getCollection('statusJSONImpl').find({'user.location': 'Argentina'})");
            JSONPObject resultJson = new JSONPObject(result.toString());
            System.out.println(resultJson.toString());
            return;*/

            while (cursor.hasNext()) {
                DBObject cur = cursor.next();
                doc = new Document();
                String date = cur.get("createdAt").toString();
                doc.add(new StringField("id", cur.get("_id").toString(), Field.Store.YES));
                doc.add(new TextField("text", cur.get("text").toString(), Field.Store.YES));
                doc.add(new TextField("date", new DateTime(cur.get("createdAt")).toString(), Field.Store.YES));
                if(cur.get("location") != null){
                    doc.add(new TextField("location", cur.get("location").toString(), Field.Store.YES));
                }
                //doc.add(new StringField("location", aux.get("location").toString(), Field.Store.YES));
                //doc.add(new StringField("analysis", cur.get("analysis").toString(), Field.Store.YES));
                //doc.add(new StringField("finalCountry",cur.get("finalCountry").toString(),Field.Store.YES));
                //doc.add(new StringField("screenName", cur.get("user.location").toString(), Field.Store.YES));
                //doc.add(new StringField("userFollowersCount", cur.get("userFollowersCount").toString(), Field.Store.YES));
                //doc.add(new StringField("favoriteCount", cur.get("favoriteCount").toString(), Field.Store.YES));

                if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                    System.out.println("Indexando el tweet: " + doc.get("text") + "\n");
                    System.out.println("Fecha del tweet :" + doc.get("date") + "\n");
                    writer.addDocument(doc);
                    System.out.println(doc);
                } else {
                    System.out.println("Actualizando el tweet: " + doc.get("text") + "\n");
                    writer.updateDocument(new Term("text" + cur.get("text")), doc);
                    System.out.println(doc);
                }
            }
            cursor.close();
            writer.close();
        } catch (IOException ioe) {
            System.out.println(" Error en " + ioe.getClass() + "\n mensaje: " + ioe.getMessage());
        }
    }

    public int getCantidad(String deporte)
        {
            //ArrayListresultList = null ;
            int total = 0;

            try {
                IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
                IndexSearcher searcher = new IndexSearcher(reader);
                Analyzer analyzer = new StandardAnalyzer();
                //this.resultList = new ArrayList<>();
                QueryParser parser = new QueryParser("text", analyzer);
                Query query = parser.parse(deporte);
                TopDocs result = searcher.search(query, 25000);
                ScoreDoc[] hits = result.scoreDocs;

                for (int i = 0; i < hits.length; i++) {
                    total++;
                }
                reader.close();
            }
            catch(IOException | ParseException ex)
            {
                Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);

            }

            return total;
        }

        public int getCantidadPais(String pais){
            int total = 0;

            try {
                IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
                IndexSearcher searcher = new IndexSearcher(reader);
                Analyzer analyzer = new StandardAnalyzer();
                //this.resultList = new ArrayList<>();
                QueryParser parser = new QueryParser("location", analyzer);
                Query query = parser.parse(pais);
                TopDocs result = searcher.search(query, 25000);
                ScoreDoc[] hits = result.scoreDocs;
                for (int i = 0; i < hits.length; i++) {
                    total++;
                }
                reader.close();
            }
            catch(IOException | ParseException ex)
            {
                Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);

            }

            return total;
        }

        public ArrayList<Integer> getCantidadFecha(){

            ArrayList<Integer> resultados=new ArrayList<>();
            try {
                IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
                IndexSearcher searcher = new IndexSearcher(reader);

                Analyzer analyzer = new StandardAnalyzer();
                QueryParser parser = new QueryParser("date", analyzer);

                for(int i=1;i<32;i++){
                    Query query;
                    if(i <= 9){
                        query = parser.parse("2018120"+i);
                    }
                    else{
                        query = parser.parse("201812"+i);
                    }
                    TopDocs result = searcher.search(query, 25000);
                    ScoreDoc[] hits = result.scoreDocs;
                    int aux=0;
                    for (int j = 0; j < hits.length; j++) {
                        aux++;
                    }
                    resultados.add(aux);
                }
                reader.close();
            }
            catch(IOException | ParseException ex)
            {
                Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);

            }
            return resultados;
        }

    public int getCantidadDeportePais(String deporte, String pais){

        int aux=0;
        String auxiliar = deporte+"AND"+"location:"+pais;
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
            IndexSearcher searcher = new IndexSearcher(reader);

            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("text", analyzer);
            Query query = parser.parse("(text:"+deporte+"*"+") AND (location:"+pais+")");
            TopDocs result = searcher.search(query, 25000);
            ScoreDoc[] hits = result.scoreDocs;
            for (int j = 0; j < hits.length; j++) {
                aux++;
            }
            reader.close();
        }
        catch(IOException | ParseException ex)
        {
            Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);

        }
        return aux;
    }
    }
