// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]
grails.config.locations = [  "file:${userHome}/.grails/${appName}-external-config.groovy" ]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }


grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']


// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// enable query caching by default
grails.hibernate.cache.queries = true

// set per-environment serverURL stem for creating absolute links
environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}

//custom configuration venu alla
//this is where the incoming form params to registry columns is mapped
//this allows to hide the internal column names and provide for a json api that is user friendly
//most important, this decouples the internal column names from external api, so if a column name changes,
//api users do not need to change anything, admin needs to just update this map.
/*
idMatch.schemaMap = [
     empId : 'attr1',
     ssn : 'attr2',
     dob : 'attr3',
     fName : 'attr4',
     lName : 'attr5',
]

//this is where the rules will go
//map keys are registry columns, not incoming form parameters
//use the same keys as in schemaMap
//this decouples it from database column name changes
//idMatch.ruleSet = [
//    empId: [exactMatchScore:"50", likeMatchScore : "20", algorithm: "EditDistance", distance:"1"],
//    ssn : [exactMatchScore:"50", likeMatchScore : "30", algorithm: "EditDistance", distance:"1"],
//    dob: [exactMatchScore:"10", likeMatchScore : "5", algorithm: "EditDistance", distance:"1"],
//    fName : [ exactMatchScore:"20", likeMatchScore : "10", algorithm: "Soundex"],     
//    lName : [ exactMatchScore:"30", likeMatchScore : "20", algorithm: "Soundex"],     
//]
idMatch.ruleSet = [
    ssn : [exactMatchScore:"50", likeMatchScore : "30", algorithm: "EditDistance", distance:"1"]
]

idMatch.cutOffScoreMap = [ exact : '100', recon : '80' ]

idMatch.algorithmSet = ["Soundex","NYSIIS","EditDistance","DaitchMakotoff"]

idMatch.securityKeys = [

   tester1 : '123456',
   tester2 : '234567',
   tester3 : '345678'

]
*/
