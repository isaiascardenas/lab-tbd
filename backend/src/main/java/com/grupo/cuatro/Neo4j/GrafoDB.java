package com.grupo.cuatro.Neo4j;

import java.util.Arrays;
import java.util.List;

import com.grupo.cuatro.model.Country;
import com.grupo.cuatro.model.Sport;
import com.grupo.cuatro.model.Statistic;
import com.grupo.cuatro.repository.CountryRepository;
import com.grupo.cuatro.repository.StatisticRepository;
import com.mongodb.*;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GrafoDB {

	private Driver driver;
	private Session session;
	
	@Autowired
	private CountryRepository countryRepository;

	@Autowired
    private StatisticRepository statisticRepository;
	
	public void connect(String uri, String username, String password)
	{
	System.out.println("Holiwo");
	this.driver = GraphDatabase.driver( uri, AuthTokens.basic( username, password ) );
	this.session = driver.session();
	}
	 public void disconnect() {
	        session.close();
	        driver.close();
	    }
	
	public void deleteAll() {
        this.session.run("match (a)-[r]->(b) delete r");
        this.session.run("match (n) delete n");
    }
	
	public void crearNodoUsuarios(){
        MongoClient mongoo = new MongoClient("localhost", 27017);
		DB db= mongoo.getDB("twitter2");
		DBCollection col=db.getCollection("tweet");

        DBObject group = new BasicDBObject("$group", new BasicDBObject("_id", "$userScreenName")
                .append("seguidores", new BasicDBObject("$avg", "$userFollowersCount")));

        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("seguidores", -1));
        DBObject limit= new BasicDBObject("$limit",100);
        AggregationOutput output = col.aggregate(group,sort,limit);
        int cantidad =output.hashCode();
        int i=0;
        for (DBObject result : output.results()) {
//            System.out.println(result);
            i++;
            session.run("create (a:User {name:'"+limpiar(result.get("_id").toString())+"', followers:"+result.get("seguidores")+"})");
        }
//

        System.out.println("Usuarios agregados");
        mongoo.close();
    }

    public void nodosUsuario(String user, Double influencia){
        session.run("create (a:Usuario {name:'"+user+"', followers:"+influencia+"})");

    }

    public void crearNodoPais(String pais, String influencia){
        session.run("create (a:Paisito {name:'"+pais+"', influencia:"+influencia+"})");
        System.out.println("Se creo nodo pais");
    }

    public void crearNodoDeporte(String deporte){
        session.run("create (a:Deportesito {name:'"+deporte+"'})");
        System.out.println("Se creo nodo deporte");
    }

    public void crearNodoUsuario(String usuario, String influencia){
        session.run("create (a:Usuario {name:'"+usuario+"', influencia:"+influencia+"})");
        System.out.println("Se creo nodo usuario");
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

    public void crearRelacionDeportePais(String deporte, String pais){
        this.session.run("MATCH (u:Pais),(v:Deporte) WHERE u.name='"+pais+"' AND v.name='"+deporte+"'"
                + " CREATE (u)-[r:Habla_de]->(v)");
        System.out.println("Se crea la relacion");
    }

    public void crearRelacionUsuarioDeporte(String deporte, String usuario){
        this.session.run("MATCH (u:Usuario),(v:Deporte) WHERE u.name='"+usuario+"' AND v.name='"+deporte+"'"
                + " CREATE (u)-[r:Twittea]->(v)");
        System.out.println("Se crea la relacion usuario-deporte");
    }

    public void crearRelacionUsuarioPais(String pais, String usuario){
        this.session.run("MATCH (u:User),(v:Pais) WHERE u.name='"+usuario+"' AND v.name='"+pais+"'"
                + " CREATE (u)-[r:Pertenece]->(v)");
        System.out.println("Se crea la relacion usuario-deporte");
    }

}
