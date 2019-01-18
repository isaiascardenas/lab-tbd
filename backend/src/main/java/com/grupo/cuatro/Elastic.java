package com.grupo.cuatro;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.grupo.cuatro.Neo4j.GrafoDB;
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
import org.apache.lucene.search.*;
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
                Date date = (Date) cur.get("createdAt");
                String auxiliar = DateTools.dateToString(date, DateTools.Resolution.DAY);
                doc.add(new StringField("id", cur.get("_id").toString(), Field.Store.YES));
                doc.add(new TextField("text", cur.get("text").toString(), Field.Store.YES));
                doc.add(new TextField("date", auxiliar, Field.Store.YES));
                if(cur.get("location") != null){
                    doc.add(new TextField("location", cur.get("location").toString(), Field.Store.YES));
                }
                doc.add(new TextField("userScreenName", cur.get("userScreenName").toString(), Field.Store.YES));
                doc.add(new StringField("userFollowersCount", cur.get("userFollowersCount").toString(), Field.Store.YES));
                doc.add(new StringField("favoriteCount", cur.get("favoriteCount").toString(), Field.Store.YES));
                doc.add(new StringField("userFriendsCount", cur.get("userFriendsCount").toString(), Field.Store.YES));


                if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                    System.out.println("Usuario del tweet: " + doc.get("userScreenName") + "\n");
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

        public ArrayList<Long> getCantidadFecha(){

            ArrayList<Long> resultados = new ArrayList<>();
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
                    long aux=0;
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

    public int getInfluenciaPais(String pais){
        int aux=0;
        int i = 0;
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
            IndexSearcher searcher = new IndexSearcher(reader);

            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("text", analyzer);
            Query query = parser.parse("(location:"+pais+")");
            TopDocs result = searcher.search(query, 25000);
            ScoreDoc[] hits = result.scoreDocs;
            for (int j = 0; j < hits.length; j++) {
                Document doc = searcher.doc(hits[j].doc);
                aux+= Integer.parseInt(doc.get("userFollowersCount"));
                aux+= Integer.parseInt(doc.get("favoriteCount"));
                aux+= Integer.parseInt(doc.get("userFriendsCount"));
                //System.out.println(aux);
                i = j;

            }
            aux = aux/getTotalUsers();
            reader.close();
        }
        catch(IOException | ParseException ex)
        {
            Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);

        }
        return aux;
    }

    public void crearRelacionUserDeporte(){
        int i = 0;
        ArrayList<String> usuarios = new ArrayList<>();
        GrafoDB grafo = new GrafoDB();
        grafo.connect("bolt://localhost", "neo4j", "secret");
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
            IndexSearcher searcher = new IndexSearcher(reader);

            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("text", analyzer);
            Query aux = parser.parse("userScreenName: biobio");
            Query query = new MatchAllDocsQuery();
            TopDocs result = searcher.search(query, 100000);
            ScoreDoc[] hits = result.scoreDocs;
            for (int j = 0; j < hits.length; j++) {
                Document doc = searcher.doc(hits[j].doc);
                if(Integer.parseInt(doc.get("userFollowersCount")) > 1000000){
                    String lugar = doc.get("location");
                    String usuario = doc.get("userScreenName");
                    //System.out.println("El usuario es: "+usuario);
                    //System.out.println("El lugar es: "+lugar);
                    if(lugar != null){
                        if(lugar.contains("Chile") || lugar.contains("Santiago")){
                            if(!usuarios.contains(usuario)){
                                grafo.crearRelacionUsuarioPais("Chile", limpiar(usuario));
                                usuarios.add(usuario);
                            }
                        }
                        else if(lugar.contains("Colombia")){
                            if(!usuarios.contains(usuario)){
                            grafo.crearRelacionUsuarioPais("Colombia", limpiar(usuario));
                            usuarios.add(usuario);
                            }
                        }
                        else if(lugar.contains("Paraguay")){
                            if(!usuarios.contains(usuario)) {
                                grafo.crearRelacionUsuarioPais("Paraguay", limpiar(usuario));
                                usuarios.add(usuario);
                            }
                        }
                        else if(lugar.contains("Uruguay")){
                            if(!usuarios.contains(usuario)){
                            grafo.crearRelacionUsuarioPais("Uruguay", limpiar(usuario));
                            usuarios.add(usuario);
                            }
                        }
                        else if(lugar.contains("España") || lugar.contains("Madrid")){
                            if(!usuarios.contains(usuario)) {
                                grafo.crearRelacionUsuarioPais("España", limpiar(usuario));
                                usuarios.add(usuario);
                            }
                        }
                        else if(lugar.contains("Mexico") || lugar.contains("México")){
                            if(!usuarios.contains(usuario)) {
                                grafo.crearRelacionUsuarioPais("Mexico", limpiar(usuario));
                                usuarios.add(usuario);
                            }
                        }
                        else if(lugar.contains("Venezuela")){
                            if(!usuarios.contains(usuario)) {
                                grafo.crearRelacionUsuarioPais("Venezuela", limpiar(usuario));
                                usuarios.add(usuario);
                            }
                        }
                        else if(lugar.contains("Ecuador")){
                            if(!usuarios.contains(usuario)) {
                                grafo.crearRelacionUsuarioPais("Ecuador", limpiar(usuario));
                                usuarios.add(usuario);
                            }
                        }
                        else if(lugar.contains("Argentina") || lugar.contains("Buenos Aires")){
                            if(!usuarios.contains(usuario)) {
                                grafo.crearRelacionUsuarioPais("Argentina", limpiar(usuario));
                                usuarios.add(usuario);
                            }
                        }
                    }
                }

            }
            reader.close();
        }
        catch(IOException | ParseException ex)
        {
            Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);

        }
    }

    public String limpiar(String nombre){
        nombre=nombre.replace("'","");
        nombre=nombre.replace("/","");
        nombre=nombre.replace("\"","");
        nombre=nombre.replace("_","");
        nombre=nombre.replace("¯(ツ)¯","");
        nombre=nombre.replace("|","");
        nombre=nombre.replace("°","");
        nombre=nombre.replace("¬","");
        nombre=nombre.replace("!","");
        nombre=nombre.replace("#","");
        nombre=nombre.replace("$","");
        nombre=nombre.replace("%","");
        nombre=nombre.replace("&","");
        nombre=nombre.replace("/","");
        nombre=nombre.replace("(","");
        nombre=nombre.replace(")","");
        nombre=nombre.replace("=","");
        nombre=nombre.replace("?","");
        nombre=nombre.replace("\\","");
        nombre=nombre.replace("¡","");
        nombre=nombre.replace("¿","");
        nombre=nombre.replace("@","");
        nombre=nombre.replace("*","");
        nombre=nombre.replace("+","");
        nombre=nombre.replace("~","");
        nombre=nombre.replace("{","");
        nombre=nombre.replace("}","");
        nombre=nombre.replace("[","");
        nombre=nombre.replace("]","");
        nombre=nombre.replace(";","");
        nombre=nombre.replace(",","");
        nombre=nombre.replace(":","");
        nombre=nombre.replace(".","");
        nombre=nombre.replace("_","");
        nombre=nombre.replace("-","");

        nombre=nombre.replace("AND","(and)");
        if(nombre.equals("AND Noticias")){
            nombre=nombre.replace("AND","aanndd");
        }
        return nombre;
    }

    public ArrayList<String> getUsers(){
        String aux = "hola";
        ArrayList<String> usuarios = new ArrayList<>();
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
            IndexSearcher searcher = new IndexSearcher(reader);

            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("userScreenName", analyzer);
            Query hhh = parser.parse("userScreenName: biobio");
            Query query = new MatchAllDocsQuery();
            TopDocs result = searcher.search(query, 25000);
            ScoreDoc[] hits = result.scoreDocs;
            for(int j = 0; j < hits.length; j++){
                Document doc = searcher.doc(hits[j].doc);
                //System.out.println(doc.get("userScreenName"));
                String hola = doc.get("userScreenName");
                if(Integer.parseInt(doc.get("userFollowersCount")) > 2800000){
                    if(!usuarios.contains(hola))
                    usuarios.add(hola);
                }
            }
            reader.close();
        }
        catch(IOException | ParseException ex)
        {
            Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);

        }
        return usuarios;
    }

    public String getUserInfluencia(String Usuario){
        int aux=0;
        int i = 0;
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
            IndexSearcher searcher = new IndexSearcher(reader);

            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("text", analyzer);
            Query query = parser.parse("(userScreenName:"+Usuario+")");
            TopDocs result = searcher.search(query, 25000);
            ScoreDoc[] hits = result.scoreDocs;
            for (int j = 0; j < hits.length; j++) {
                Document doc = searcher.doc(hits[j].doc);
                aux+= Integer.parseInt(doc.get("userFollowersCount"));
                //aux+= Integer.parseInt(doc.get("favoriteCount"));
                //aux+= Integer.parseInt(doc.get("userFriendsCount"));
                //System.out.println(aux);
                i = j;

            }
            //aux = aux/i;
            reader.close();
        }
        catch(IOException | ParseException ex)
        {
            Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);

        }
        return Integer.toString(aux);
    }

    public ArrayList<String> usuarioHabla(String usuario){
        //System.out.println(usuario);
        ArrayList<String> deportes = new ArrayList<>();
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
            IndexSearcher searcher = new IndexSearcher(reader);

            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("userScreenName", analyzer);
            Query query = parser.parse("(userScreenName:"+usuario+")");
            TopDocs result = searcher.search(query, 100000);
            ScoreDoc[] hits = result.scoreDocs;
            for(int j = 0; j < hits.length; j++){
                //System.out.println("Entre al for y que paha");
                Document doc = searcher.doc(hits[j].doc);
                //System.out.println(doc.get("text"));
                if(doc.get("text").contains("tenis")){
                    if(!deportes.contains("Tenis")){
                        deportes.add("Tenis");
                    }
                }
                else if(doc.get("text").contains("boxeo") || doc.get("text").contains("ganchos") || doc.get("text").contains("boxeador")
                || doc.get("text").contains("Boxeo")){
                    if(!deportes.contains("Boxeo")){
                        deportes.add("Boxeo");
                    }

                }
                else if(doc.get("text").contains("rugby")){
                    if(!deportes.contains("Rugby")){
                        deportes.add("Rugby");
                    }

                }
                else if(doc.get("text").contains("futbol")){
                    if(!deportes.contains("Futbol Femenino")){
                        deportes.add("Futbol Femenino");
                    }

                }
                else if(doc.get("text").contains("basketball")){
                    if(deportes.contains("Basketball")){
                        deportes.add("Basketball");
                    }

                }
                else if(doc.get("text").contains("volleyball")){
                    if(!deportes.contains("Volleyball")){
                        deportes.add("Volleyball");
                    }

                }
                else if(doc.get("text").contains("natacion")){
                    if(!deportes.contains("Natacion")){
                        deportes.add("Natacion");
                    }

                }
                else if(deportes.isEmpty()){
                    deportes.add("Tenis");
                }
            }
            reader.close();
        }
        catch(IOException | ParseException ex)
        {
            Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);

        }
        //System.out.println(deportes);
        return deportes;
    }

    public ArrayList<String> paisHabla(String pais){
        //System.out.println("Entre a paisHabla");
        //System.out.println(pais);
        ArrayList<String> deportes = new ArrayList<>();
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
            IndexSearcher searcher = new IndexSearcher(reader);

            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("location", analyzer);
            Query query = parser.parse("(location:"+pais+")");
            TopDocs result = searcher.search(query, 100000);
            ScoreDoc[] hits = result.scoreDocs;
            for(int j = 0; j < hits.length; j++){
                //System.out.println("Entre al for");
                Document doc = searcher.doc(hits[j].doc);
                String followers = doc.get("userFollowersCount");
                int aux = Integer.parseInt(followers);

                if(aux > 100000) {


                    //System.out.println(doc.get("text"));
                    if (doc.get("text").contains("tenis")) {
                        if (!deportes.contains("Tenis")) {
                            deportes.add("Tenis");
                        }
                    } else if (doc.get("text").contains("boxeo") || doc.get("text").contains("ganchos") || doc.get("text").contains("boxeador")
                            || doc.get("text").contains("Boxeo")) {
                        if (!deportes.contains("Boxeo")) {
                            deportes.add("Boxeo");
                        }

                    } else if (doc.get("text").contains("rugby")) {
                        if (!deportes.contains("Rugby")) {
                            deportes.add("Rugby");
                        }

                    } else if (doc.get("text").contains("futbol")) {
                        if (!deportes.contains("Futbol Femenino")) {
                            deportes.add("Futbol Femenino");
                        }

                    } else if (doc.get("text").contains("basketball")) {
                        if (deportes.contains("Basketball")) {
                            deportes.add("Basketball");
                        }

                    } else if (doc.get("text").contains("volleyball")) {
                        if (!deportes.contains("Volleyball")) {
                            deportes.add("Volleyball");
                        }

                    } else if (doc.get("text").contains("natacion")) {
                        if (!deportes.contains("Natacion")) {
                            deportes.add("Natacion");
                        }

                    }
                }
            }
            reader.close();
        }
        catch(IOException | ParseException ex)
        {
            Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);

        }
        //System.out.println(deportes);
        return deportes;
    }

    public ArrayList<String> getUserPais(String usuario){
        //System.out.println(usuario);
        ArrayList<String> pais = new ArrayList<>();
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
            IndexSearcher searcher = new IndexSearcher(reader);

            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("userScreenName", analyzer);
            Query query = parser.parse("(userScreenName:"+usuario+")");
            TopDocs result = searcher.search(query, 100000);
            ScoreDoc[] hits = result.scoreDocs;
            for(int j = 0; j < hits.length; j++){
                //System.out.println("Entre al for y que paha");
                Document doc = searcher.doc(hits[j].doc);
                String lugar = doc.get("location");
                //System.out.println(lugar);
                if(lugar != null) {


                    if (lugar.contains("Chile") && !pais.contains("Chile")) {
                        pais.add("Chile");
                    } else if ((lugar.contains("Argentina") || lugar.contains("Buenos Aire")) && !pais.contains("Argentina")) {
                        pais.add("Argentina");
                    } else if (lugar.contains("México") && !pais.contains("México")) {
                        pais.add("México");
                    } else if (lugar.contains("Venezuela") && !pais.contains("Venezuela")) {
                        pais.add("Venezuela");
                    } else if (lugar.contains("Paraguay") && !pais.contains("Paraguay")) {
                        pais.add("Paraguay");
                    } else if (lugar.contains("Uruguay") && !pais.contains("Uruguay")) {
                        pais.add("Uruguay");
                    } else if (lugar.contains("Ecuador") && !pais.contains("Ecuador")) {
                        pais.add("Ecuador");
                    } else if (lugar.contains("España") || lugar.contains("Barcelona") || lugar.contains("Madrid")) {
                        if (!pais.contains("España")) {
                            pais.add("España");
                        }
                    } else if (lugar.contains("Colombia")) {
                        pais.add("Colombia");
                    }
                    else if(pais.isEmpty()){
                        pais.add("Ecuador");
                    }
                }

                else{
                    if(!pais.contains("Chile")){
                        pais.add("Chile");
                    }
                }

            }
            reader.close();
        }
        catch(IOException | ParseException ex)
        {
            Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);

        }
        //System.out.println(pais);
        return pais;
    }

    public ArrayList<String> getUsersDos(){
        String aux = "hola";
        ArrayList<String> usuarios = new ArrayList<>();
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
            IndexSearcher searcher = new IndexSearcher(reader);

            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("userScreenName", analyzer);
            Query hhh = parser.parse("userScreenName: biobio");
            Query query = new MatchAllDocsQuery();
            TopDocs result = searcher.search(query, 25000);
            ScoreDoc[] hits = result.scoreDocs;
            for(int j = 0; j < hits.length; j++){
                Document doc = searcher.doc(hits[j].doc);
                //System.out.println(doc.get("userScreenName"));
                String hola = doc.get("userScreenName");
                if(Integer.parseInt(doc.get("userFollowersCount")) > 2000000){
                    if(!usuarios.contains(hola))
                        usuarios.add(hola);
                }
            }
            reader.close();
        }
        catch(IOException | ParseException ex)
        {
            Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);

        }
        return usuarios;
    }

    public int getTotalUsers(){
        String aux = "hola";
        int i = 0;
        ArrayList<String> usuarios = new ArrayList<>();
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("indice/")));
            IndexSearcher searcher = new IndexSearcher(reader);

            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser("userScreenName", analyzer);
            Query hhh = parser.parse("userScreenName: biobio");
            Query query = new MatchAllDocsQuery();
            TopDocs result = searcher.search(query, 1000000);
            ScoreDoc[] hits = result.scoreDocs;
            for(int j = 0; j < hits.length; j++){
                Document doc = searcher.doc(hits[j].doc);
                i++;
            }
            reader.close();
        }
        catch(IOException | ParseException ex)
        {
            Logger.getLogger(Elastic.class.getName()).log(Level.SEVERE,null,ex);

        }
        return i;
    }

    }
