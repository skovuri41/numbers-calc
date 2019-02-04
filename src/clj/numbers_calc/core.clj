(ns numbers-calc.core
  (:require
   [mount.core :as mount]
   [numbers-calc.config :refer [env]]
   [numbers-calc.calc :refer [process-file]]
   [clojure.tools.logging :as log]
   [cli-matic.core :refer [run-cmd]])
  (:gen-class))


(defn stop-app []
  (doseq [component (:stopped (mount/stop))]
    (log/info component "stopped"))
  (shutdown-agents))


(defn start-app []
  (doseq [component (:started (mount/start))]
    (log/info component "started"))
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app)))


(def CONFIGURATION
  {:app      {:command     "numbers-calc"
              :description "A command-line app for trade monitor enterprise api's"
              :version     "0.0.1"}
   :commands [{:command     "numbers"
               :description ["process file"]
               :opts        [{:option "file" :short "f" :as "file" :type :string}]
               :runs        process-file}
              ]})


(defn -main
  [& args]
  (start-app)
  (run-cmd args CONFIGURATION))


(comment
  (mount/start #'numbers-calc.config/env))
