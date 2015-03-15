(ns ciste.services.nrepl
  (:require [cider.nrepl :refer [cider-nrepl-handler]]
            [ciste.config :refer [config describe-config]]
            [clojure.tools.logging :as log]
            [clojure.tools.nrepl.server :refer [default-handler
                                                start-server
                                                stop-server]]))

(defn start
  []
  (let [port (config :nrepl :port)]
    (log/infof "Starting nrepl server on port %s" port)
    (start-server :port (Integer/parseInt port)
                  :handler cider-nrepl-handler)))

(defn stop
  [])
