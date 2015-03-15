(ns ciste.services.http-kit
  (:require [ciste.config :refer [config describe-config]]
            [clojure.string :as string]
            [org.httpkit.server :refer [run-server]]))

(defonce server (atom nil))

(describe-config [:http :port] :number
  "The port the http server should run on")

(describe-config [:http :websocket] :boolean
  "Should websocket support be enabled?")

(describe-config [:http :handler] :string
  "A string pointing to a fully namespace-qualified http handler")

(defn start
  "Start a http server"
  []
  (let [handler (config :http :handler)]
    ;; Require handler namespace
    (-> handler (string/split #"/")
        first symbol require)

    ;; start server
    (let [handler-var (resolve (symbol handler))
          stop-function (run-server handler-var
                                    {:port (Integer/parseInt (config :http :port))})]
      (dosync
       (reset! server stop-function)))))

(defn stop
  "Stop http server"
  []
  (when-not (nil? @server)
    (@server)
    (reset! server nil)))
