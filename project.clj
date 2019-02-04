(defproject numbers-calc "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [cheshire "5.8.1"]
                 [clojure.java-time "0.3.2"]
                 [me.raynes/fs "1.4.6"]
                 [cprop "0.1.13"]
                 [nrepl "0.5.3"]
                 [mount "0.1.15"]
                 [funcool/cuerdas "2.1.0"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [cli-matic "0.3.3"]
                 [org.clojure/tools.logging "0.4.1"]
                 [funcool/struct "1.3.0"]
                 [semantic-csv "0.2.1-alpha1"]
                 [medley "1.1.0"]]

  :min-lein-version "2.0.0"

  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :resource-paths ["resources"]
  :target-path "target/%s/"
  :main ^:skip-aot numbers-calc.core
  :bin {:name          "numbers-calc"
        :bin-path      "./"
        :bootclasspath false
        ;; :jvm-opts      ["-server" "-Dfile.encoding=utf-8" "$JVM_OPTS" ]
        }
  :plugins [[lein-binplus "0.6.4"]
            [lein-eftest "0.5.1"]
            [lein-kibit "0.1.6"]
            [io.taylorwood/lein-native-image "0.3.0"] ]
  :native-image {:graal-bin "/Users/shyam/Projects/graalvm-ce-1.0.0-rc9/Contents/Home/bin"
                 :opts      ["-Dclojure.compiler.direct-linking=true"
                             "-H:EnableURLProtocols=http"
                             "-H:ReflectionConfigurationFiles=./graal.json"
                             "--report-unsupported-elements-at-runtime" ;; ignore native-image build errors
                             "--no-server" ;; TODO issue with subsequent builds failing on same server
                             "--verbose"]
                 :name      "server"}
  :profiles
  {:uberjar {:omit-source    true
             :aot            :all
             :uberjar-name   "numbers-calc.jar"
             :source-paths   ["env/prod/clj"]
             :resource-paths ["env/prod/resources"]}

   :dev  [:project/dev :profiles/dev]
   :test [:project/dev :project/test :profiles/test]

   :project/dev   {:jvm-opts       ["-Dconf=dev-config.edn"]
                   :dependencies   [[expound "0.7.2"]
                                    [pjstadig/humane-test-output "0.9.0"]
                                    [prone "1.6.1"]
                                    [lein-binplus "0.6.4"]]
                   :plugins        [[com.jakemccrary/lein-test-refresh "0.23.0"]]
                   :source-paths   ["env/dev/clj"]
                   :resource-paths ["env/dev/resources"]
                   :repl-options   {:init-ns user}
                   :injections     [(require 'pjstadig.humane-test-output)
                                    (pjstadig.humane-test-output/activate!)]}
   :project/test  {:jvm-opts       ["-Dconf=test-config.edn"]
                   :resource-paths ["env/test/resources"]}
   :profiles/dev  {}
   :profiles/test {}}


  )
