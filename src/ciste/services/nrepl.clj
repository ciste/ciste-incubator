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
    (try
      (start-server :port (Integer/parseInt port)
                    :bind "0.0.0.0"
                    :handler cider-nrepl-handler)
      (catch Exception ex
        (log/error "Couldn't start nrepl server" ex)))))

(defn stop
  [])
