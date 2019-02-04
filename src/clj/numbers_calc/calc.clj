(ns numbers-calc.calc
  (:require
   [mount.core :as mount]
   [numbers-calc.config :refer [env]]
   [cuerdas.core :as str]
   [medley.core :as medley]
   [clojure.tools.logging :as log]))


(def char->num {"a" 1, "b" 2, "c" 3, "d" 4, "e" 5, "f" 8, "g" 3, "h" 5,
                "i" 1, "j" 1, "k" 2, "l" 3, "m" 4, "n" 5, "o" 7, "p" 8,
                "q" 1, "r" 2, "s" 3, "t" 4, "u" 6, "v" 6, "w" 6, "x" 5,
                "y" 1, "z" 7})


(defn calc-num
  [c]
  (cond
    (str/numeric? c) (str/parse-number c)
    (string? c)      (get char->num c 0)
    :else            0))


(defn get-number
  [s]
  (let [s   (-> s
                str/lower
                str/clean)
        num (->> s
                 (str/chars)
                 (map calc-num)
                 (apply +))]
    {:str s :number num}))


(defn process-line [line seperator]
  (->>
    (str/split line seperator)
    (filter #(not (str/empty-or-nil? %)))
    (map get-number)))


(defn process-file
  ([{:keys [file]}]
   (process-file file " "))
  ([file separator]
   (with-open [reader (clojure.java.io/reader file)]
     (loop [xs     (line-seq reader)
            result []]
       (if (empty? xs)
         (do
           (log/info result)
           result)
         (recur (rest xs) (into result (process-line (first xs) separator))))))))


(comment (calc-num " a ")
         (calc-num "a")
         (get-number "numbers-calc")
         (get-number "charlie chaplin")
         (process-file {:file "names1.txt"})
         (group-by :number (process-file {:file "names1.txt"}))
         (sort-by :number (process-file {:file "names1.txt"}))
         (map vals (sort-by :str (process-file {:file "names1.txt"})))
         )

(comment
  ;; external resources

  (defn non-blank? [s]
    (not (clojure.string/blank? s)))

  (defn non-blank-lines-seq [file-name]
    (let [reader (clojure.java.io/reader file-name)]
      (filter non-blank? (line-seq reader))))

  (defn non-blank-lines [file-name]
    (with-open [reader (clojure.java.io/reader file-name)]
      (into [] (filter non-blank?) (line-seq reader))))

  (defn non-blank-lines-eduction [reader]
    (eduction (filter non-blank?) (line-seq reader)))


  (defn line-count [file-name]
    (with-open [reader (clojure.java.io/reader file-name)]
      (reduce (fn [cnt el] (inc cnt)) 0 (non-blank-lines-eduction reader))))

  (non-blank-lines-seq "project.clj")
  (non-blank-lines "project.clj")
  (line-count "project.clj"))
