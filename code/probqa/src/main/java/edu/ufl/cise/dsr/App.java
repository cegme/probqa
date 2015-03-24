package edu.ufl.cise.dsr;

import org.openrdf.query.parser.sparql.SPARQLParser;
import org.openrdf.query.parser.ParsedQuery;
import org.openrdf.query.algebra.helpers.QueryModelVisitorBase;
import org.openrdf.query.algebra.helpers.StatementPatternCollector;
import org.openrdf.query.algebra.StatementPattern;

import org.openrdf.query.MalformedQueryException;

import java.util.List;


/**
 * Hello world!
 *
 */
public class App {

  public static void tripleExtractor() throws MalformedQueryException {
    /*final String queryString =
      "PREFIX  dc:  <http://purl.org/dc/elements/1.1/>"+
      "PREFIX  ns:  <http://example.org/ns#>"+
      "SELECT  ?title ?price"+
      "WHERE   { ?x ns:price ?price ."+
      "FILTER (?price < 30.5)"+
      "?x dc:title ?title . }";*/
    final String queryString = 
      "PREFIX fb: <http://rdf.freebase.com/ns/>\n"+
      "SELECT DISTINCT ?x2 ?x2name\n"+
      "WHERE {\n"+
      "\tfb:en.justin_bieber fb:people.person.sibling_s ?x1 .\n"+
      "\t?x1 fb:people.sibling_relationship.sibling ?x2 .\n"+
      "\n"+
      "\tFILTER (?x2 != fb:en.justin_bieber) .\n"+
      "\tOPTIONAL { ?x2 fb:type.object.name ?x2name }\n"+
      "}\n"+
      "LIMIT 10";

    System.out.println(queryString);

    SPARQLParser parser = new SPARQLParser();
    ParsedQuery query = parser.parseQuery(queryString, null); // maybe add freebase

    StatementPatternCollector collector = new StatementPatternCollector();
    query.getTupleExpr().visit(collector);

    java.util.List<StatementPattern> patterns = collector.getStatementPatterns();

    for (StatementPattern p: patterns) {
      System.out.println( p.toString() );
    }

  } 

  public static void main( String[] args ) {
    System.out.println( "Hello World!" );

    try {
      tripleExtractor(); 
    } catch (MalformedQueryException mqe) {
      mqe.printStackTrace();
    }
  }
}
