(ns ciste.services.nrepl
  (:use [ciste.config :only [config describe-config]]
        [clojure.tools.nrepl.server :only (start-server stop-server)]
        )
  )

(defn start
  []
  (start-server :port (config ::port)))

(defn stop
  [])
