package com.uvsq.SPARQL;

/**
 * 
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jena.iri.impl.Main;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;	
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.uvsq.Interface.MainForm;
import com.uvsq.Jung.JUNG;
import com.uvsq.Model.TupleRDF;
import com.uvsq.RDF.RDFManager;



/**
 * @author Bruce GONG
 *
 */
public class SPARQL {
	
	
	public static void createTreeSPARQL(String filePath, String keywords)
	{
		//1. split keywords and generate a complete SPARQL command.
		String queryString = getQueryString(keywords);
		
		/*///////////////////////// Construct Query
		Query query = QueryFactory.create(queryString) ;
		QueryExecution qexec = QueryExecutionFactory.create(query, RDFManager.mod) ;
		Model resultModel = qexec.execConstruct() ;
		JUNG.createGraph(resultModel, "Test");
		qexec.close() ;
		/////////////////////////
*/		
		
		//System.out.println(query);
		//2. execute this SPARQL and transform ResultSet into Object[][]
		Object[][] objss = launchSPARQL(queryString);
		if(objss.length != 0)
		{
			//System.out.println("SPARQL Found 2 : " + objss.length);
			//3. generate a SPARQL graph
			JUNG.createTree(objss, "Graphe de resultat (Requ¨ºte SPARQL)");
		}else{
			System.out.println("SPARQL Found nothing !");
		}
	}
	
	public static List<TupleRDF> enrichGraph(List<TupleRDF> ts)
	{
		Set<String> resSet = new HashSet<String>();
		String sparqlStr = "select *\n"
				+ "where {\n";
		for(int i = 0; i < ts.size(); i++){
			TupleRDF t = ts.get(i);
			resSet.add(t.getSujet().toString());
			if(i==0){
				sparqlStr += t.getSPARQL();
			}else{
				sparqlStr += "\n UNION \n" + t.getSPARQL();
			}
		}
		sparqlStr += "}";
		System.out.println(sparqlStr);
		Object[][] result = launchSPARQL(sparqlStr);
		List<TupleRDF> tsResult = JUNG.convertirTuples(result);
		for(TupleRDF t : tsResult){
			if(resSet.contains(t.getSujet().toString())){
				ts.add(t);
			}
		}		
		return ts;
	}
		

	public static Object[][] launchSPARQL(String queryString)
	{
		/*FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());
		Model mod = FileManager.get().loadModel(MainForm.filePath);
		System.out.println(MainForm.filePath);*/
		Model mod = RDFManager.mod;
		 
		Query query=QueryFactory.create(queryString) ; 
		QueryExecution queryEx = QueryExecutionFactory.create(query,mod);
		ResultSet rs = null;
		List<Object[]> objl = new ArrayList<Object[]>();
		try
		{
			rs = queryEx.execSelect();
			int count = 0;
			while(rs.hasNext()) 
			{
				QuerySolution qS = rs.nextSolution();
				Resource s = qS.getResource("s");
				RDFNode p = qS.get("p");
				RDFNode o  = qS.get("o");
				Object[] obj = new Object[3];
				obj[0] = s.toString();
				obj[1] = p.toString();
				obj[2] = o.toString();
				objl.add(obj);
				System.out.println(s + "        =>        " + p + "        =>        " + o);
				count++;
			}
			System.out.println("SPARQL Found : " +count + " hits.");
		}
		finally {queryEx.close();}
		return objl.toArray(new Object[objl.size()][3]);
	}
		
	public static String getQueryString(String keywords){
		String[] words = keywords.split(" ");
		String query = "SELECT * \n"
		//String query = "CONSTRUCT { ?r ?p ?o.} \n"
				+ "WHERE \n"
				+ "{?s ?p ?o. \n";
		String contraints = "";
		for(String s : words)
		{
			contraints += " FILTER regex(?o, \"" + s.trim() + "\", \"i\") \n";
		}
		query += contraints + " } ";
		return query;
	}
}
