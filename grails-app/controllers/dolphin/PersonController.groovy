/* 
 * Copyright ©2012. The Regents of the University of California (Regents).
 * All Rights Reserved. Permission to use, copy, modify, and distribute this
 * software and its documentation for educational, research, and not-for-profit
 * purposes, without fee and without a signed licensing agreement, is hereby
 * granted, provided that the above copyright notice, this paragraph and the
 * following two paragraphs appear in all copies, modifications, and
 * distributions. Contact The Office of Technology Licensing, UC Berkeley, 2150
 * Shattuck Avenue, Suite 510, Berkeley, CA 94720-1620, (510) 643-7201, for
 * commercial licensing opportunities.
 * 
 * Created by Venu Alla, CalNet, IST, University of California, Berkeley
 * 
 * IN NO EVENT SHALL REGENTS BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
 * SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
 * ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
 * REGENTS HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * REGENTS SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE. THE SOFTWARE AND ACCOMPANYING DOCUMENTATION, IF ANY, PROVIDED
 * HEREUNDER IS PROVIDED "AS IS". REGENTS HAS NO OBLIGATION TO PROVIDE
 * MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */
package dolphin


import grails.converters.JSON
import grails.converters.XML
import org.codehaus.groovy.grails.web.servlet.mvc.*;


class PersonController {

  def grailsApplication; //config object  
  def matchingService;

  def scaffold = true;

  def search(){
       def persons = Person.list();
       return [ persons: persons ]
   }
  
//this is a web form action? 
  def match(){
     println "params passed are ${params}"
     def persons = Person.list();
     def rules = MatchRule.list();
     def exactMatchMap= [:];
     def reconMatchMap = [:];
     persons.each(){person ->

        def personScore = 0;
        rules.each() { rule ->
            personScore = personScore + matchingService.executeRule(rule,person,params);
        }
       if(personScore >= 90 ) exactMatchMap.put(person.firstName+"-"+person.lastName,personScore);
       if(personScore < 90)  reconMatchMap.put(person.firstName+"-"+person.lastName,personScore);

      }
     return [ persons: persons, exactMatchMap: exactMatchMap, reconMatchMap: reconMatchMap]
   }

//this is json action?
  def match2(){
     println "params passed are ${params}"
     def jsonElement = JSON.parse(request);
     println "json element is ${jsonElement}"
     println "json data element is ${jsonElement.data}"
     def persons = Person.list();
     def rules = MatchRule.list();
     def exactMatchMap= [:];
     def reconMatchMap = [:];
     persons.each(){person ->

        def personScore = 0;
        rules.each() { rule ->
            personScore = personScore + matchingService.executeRule(rule,person,jsonElement.data);
        }
       def resultPerson = person;
       if(personScore >= 90 ) exactMatchMap.put(person.uid,resultPerson);
       if(personScore < 90  && personScore > 50)  reconMatchMap.put(person.uid,resultPerson);
      }
      def resultList = [];
      resultList.add(exactMatchMap);
      resultList.add(reconMatchMap);
      render resultList as JSON; 
  }


//this is json  action using file based configuration rules
/*
  for each rule, get key
  get value from json input using this key
  get value from registry using this key and schemaMap
  if(compareSimple(inputVal,registryVal)) then add simple score
  else (compareViaAlgorithm(inputVal,registryVal,algorithm), and add score
  
*/
def match3(){
    
      def persons = Person.list();
      def rules = grailsApplication.config.idMatch.ruleSet;
      def ruleKeySet = rules.keySet();
      def schemaMap = grailsApplication.config.idMatch.schemaMap;
      def cutOffScoreMap = grailsApplication.config.idMatch.cutOffScoreMap;
      println "cutOffScoreMap is "+cutOffScoreMap;
      def exactCutOffScore = cutOffScoreMap.get("exact") as int;
      def reconCutOffScore = cutOffScoreMap.get("recon") as int;
      def jsonDataMap = JSON.parse(request).data;
      println "json map is "+ jsonDataMap;
      def exactResults = [];
      def reconResults = [];
      persons.each(){ person ->
            println "person is "+person;
            def personMatchScore = 0;
            ruleKeySet.each() { ruleKey ->
             println "rule Key is " + ruleKey;
             def jsonDataValue = jsonDataMap.get(ruleKey).toString();
             println "json data value is "+jsonDataValue;
             def dbColName = schemaMap.get(ruleKey);
             println "schemaMap value for ruleKey is ${dbColName}";
             def registryValue = person."${dbColName}";
             println "person col value for this key is ${registryValue}";
             def ruleConfigMap = rules.get(ruleKey);
             println "rule config is "+ruleConfigMap;
             def ruleScore = matchingService.executeRule(ruleConfigMap, jsonDataValue,registryValue);
             println "ruleScore is "+ruleScore;
             personMatchScore = personMatchScore + ruleScore;
 
         }
             println "personMatchScore is "+personMatchScore; 
             if (personMatchScore == exactCutOffScore.intValue() ) {
                exactResults.add(person.uid +":"+personMatchScore);
                println "exact match results are "+exactResults;
             }
             else if ((personMatchScore > reconCutOffScore.intValue())
                       &&
                       (personMatchScore <  exactCutOffScore.intValue() )
                      )
                     {  reconResults.add(person.uid+":"+personMatchScore); println "recon match results are "+reconResults; }
      }
       def response = [:];
       response.put("exact" , exactResults);
       response.put("recon" , reconResults);
       render response as JSON;

}


   

   def xml(){
      def persons = Person.list();
      render persons as XML;
   }
 
   def json(){
      def persons = Person.list();
      render persons as JSON;
   }

}
