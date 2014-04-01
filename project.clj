(defproject ciste/ciste-incubator "0.1.0-SNAPSHOT"
  :description "Misc. Ciste code"
  :url "http://github.com/duck1123/ciste-incubator"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ciste "0.5.0-SNAPSHOT"]
                 [org.clojure/tools.nrepl "0.2.3"]
                 [org.clojure/tools.logging "0.2.6"]]
  :profiles {:dev
             {:dependencies
              [[log4j "1.2.17"]
               [midje "1.6.3"]]}}
  :plugins [[lein-midje "3.1.3"]
            [codox "0.6.7"]])
