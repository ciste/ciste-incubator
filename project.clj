(defproject ciste/ciste-incubator "0.1.0-SNAPSHOT"
  :description "Misc. Ciste code"
  :url "http://github.com/duck1123/ciste-incubator"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ciste/ciste-core "0.4.0-SNAPSHOT"]
                 [org.clojure/tools.nrepl "0.2.0-beta8"]
                 [org.clojure/tools.logging "0.2.4"]]
  :profiles {:dev
             {:dependencies
              [[log4j "1.2.17"]
               [midje "1.4.0"]]}}
  :plugins [[lein-midje "2.0.0-SNAPSHOT"]
            [codox "0.6.1"]])
