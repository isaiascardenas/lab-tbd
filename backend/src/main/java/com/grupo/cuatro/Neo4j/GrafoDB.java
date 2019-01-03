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
    
    public void relacionJuegoPlataforma(List<Statistic> statistics) {
    	System.out.println("Entro a crear relaciones");
    	for (Statistic statistic : statistics) {
    	        System.out.println(statistics.size());
    			this.session.run("MATCH (a: Pais),(b: Deporte)"
    					+ " WHERE a.name ='"+statistic.getCountry().getCountryName()+ "' AND b.name ='"+statistic.getSport().getSportName()
    					+ "' CREATE (a)-[r:POSEE]->(b)");
    			
    			/*this.session.run("MATCH (a: VideoGame),(b:Plataforma)"
    					+ " WHERE a.name ='"+videoGame.getName()+ "' AND b.name ='"+plataforma.getName()
    					+ "' CREATE (a)-[r:PERTENECE]->(b)");*/
			
		}
    	
    }
    
    public StatementResult getVideoGamesPlataforma(String name) {
    	
    	
    	StatementResult result=session.run("MATCH (p:Plataforma{name:'"+name+"'})-[POSEE]-(video) return video.name as name");
    	return result;
    }
    
    public StatementResult getUsuariosByGame(String game,Integer k) {
    	//System.out.println("entro a usuariosBygame");
    	String consulta="MATCH (v:VideoGame {name:'"+game+"' })<-[r:TWITTEA]-(user:User) return r.valoration as valoration,user.name as name, user.followers as followers ORDER BY user.followers "
    			+ "DESC LIMIT "+k;
    	StatementResult result=session.run(consulta);
    	return result;
    }
    
    public StatementResult getUsuariosByGame(String game) {
    	//System.out.println("entro a usuariosBygame");
    	String consulta="MATCH (v:VideoGame {name:'"+game+"' })<-[r:TWITTEA]-(user:User) return r.valoration as valoration,user.name as name, user.followers as followers "
    			+ "ORDER BY user.followers ";
    	StatementResult result=session.run(consulta);
    	
   /* 	while(result.hasNext()) {
    		Record record=result.next();
    		System.out.println(record.get("followers"));
    	}*/
    	return result;
    }
    
    
    public StatementResult relacionarUsuariosTitulos() {
    	
    	//IndiceInvertido indice= new IndiceInvertido();
    	StatementResult result = session.run( "MATCH (u:User) return u.name as name");
    	return result;
    }
    
    public Double getMaxFollowers() {
    	
    	StatementResult result = session.run( "MATCH (u:User) return u.followers as followers "
    			+ "ORDER BY u.followers DESC LIMIT 1");
    	
    	return Double.parseDouble(String.valueOf(result.next().get("followers")));
    }
    
    public int getGamesPlatformTw(String user,String plataforma) {
    	String consulta="MATCH (u:User{name:'"+user+"'})-[:TWITTEA]-(v:VideoGame) WITH v,u "
    			+ "MATCH (v)-[:PERTENECE]-(p:Plataforma{name:'"+plataforma+"'}) "
    					+ "return count(v) as cantidad";
    	
    	StatementResult result=this.session.run(consulta);
    	return Integer.parseInt(String.valueOf(result.next().get("cantidad")));
    }
   
    
    public void crearRealacionUsuarioGame(int valoration,String userName,String gameName) {
    	System.out.println(userName+gameName);
    	this.session.run("MATCH (u:User),(v:VideoGame) WHERE u.name='"+userName+"' AND v.name='"+gameName+"'"
    			+ " CREATE (u)-[r:TWITTEA {valoration:"+valoration+"}]->(v)");
    	/*this.session.run("MATCH (a: User),(b:VideoGame)"
				+ " WHERE a.name ='"+userName+ "' AND b.name ='"+gameName
				+ "' CREATE (a)-[r:A]->(b)");*/
    	//this.session.run("CREATE (p:Kimbo {tipo:'perro'})");
    	System.out.println("Termina de crear la relacion");
    }

    public void crearRelacionDeportePais(String deporte, String pais){
        this.session.run("MATCH (u:Pais),(v:Deporte) WHERE u.name='"+pais+"' AND v.name='"+deporte+"'"
                + " CREATE (u)-[r:Habla_de]->(v)");
        System.out.println("Se crea la relacion");
    }

    public void crearRelacionUsuarioDeporte(String deporte, String usuario){
        this.session.run("MATCH (u:User),(v:Deporte) WHERE u.name='"+usuario+"' AND v.name='"+deporte+"'"
                + " CREATE (u)-[r:Twittea]->(v)");
        System.out.println("Se crea la relacion usuario-deporte");
    }

    public void crearRelacionUsuarioPais(String pais, String usuario){
        this.session.run("MATCH (u:User),(v:Pais) WHERE u.name='"+usuario+"' AND v.name='"+pais+"'"
                + " CREATE (u)-[r:Pertenece]->(v)");
        System.out.println("Se crea la relacion usuario-deporte");
    }
    
    public StatementResult getUsuariosAndCantidadByPlayaforma(String plataforma) {
    	
    	String consulta="MATCH (p:Plataforma{name:'"+plataforma+"'})-[:POSEE]-(v:VideoGame) WITH v,p"
    			+ " MATCH (v)-[r:Habla_de]-(u:User) return u.name as name, SUM(r.valoration) as valoration, u.followers as followers";
    	  	
    	return  this.session.run(consulta);
    }
    
    public int getCantidadValoracionByPlataforma(String plataforma) {
    	
    	String consulta= "MATCH (p:Plataforma{name:'"+plataforma+"'})-[:POSEE]-(v:VideoGame) "
    			+ "WITH v,p MATCH (v)-[r:TWITTEA]-(u:User) "
    			+ "WITH  u, SUM(r.valoration) as c return  c ORDER BY c DESC LIMIT 1";
    	StatementResult result=this.session.run(consulta);
    	return Integer.parseInt(String.valueOf(result.next().get("c", 0)));
    	
    }
    
    public Double getCantidadFollowersByPlataforma(String plataforma) {
    	
    	String consulta= "MATCH (p:Plataforma{name:'"+plataforma+"'})-[:POSEE]-(v:VideoGame) WITH v,p MATCH "
    			+ "(v)-[r:TWITTEA]-(u:User) return MAX(u.followers) as followers";
    	
    	StatementResult result=this.session.run(consulta);
    	return  Double.parseDouble(String.valueOf(result.next().get("followers")));
    	
    }
    
    
  

}
