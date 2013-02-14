(ns qbits.hayt
  (:refer-clojure :exlude [set])
  (:require [qbits.hayt.cql :as cql]))

(defprotocol PQuery
  (as-cql [this] "Returns the query as raw string")
  (as-prepared [this] "Returns a 2 arg vector [query values-vector] ready to be used as prepared statement, the values are raw (still java/clojure natives"))

(defrecord Query [template query]
  PQuery
  (as-cql [this]
    (cql/apply-cql query template))

  (as-prepared [this]
    (cql/apply-prepared query template)))

(defn select
  ""
  [table]
  (Query. ["SELECT" :columns "FROM" :table :where :order-by :limit]
          {:table table
           :columns []}))

(defn insert
  ""
  [table]
  (Query. ["INSERT INTO" :table :values :using]
          {:table table}))

(defn update
  ""
  [table]
  (Query. ["UPDATE" :table :using :set :where]
          {:table table}))

(defn delete
  ""
  [table]
  (Query. ["DELETE" :columns "FROM" :table :using :where]
          {:table table
           :columns []}))

(defn truncate
  ""
  [table]
  (Query. ["TRUNCATE" :table]
          {:table table}))

(defn drop-keyspace
  ""
  [keyspace]
  (Query. ["DROP KEYSPACE" :keyspace]
          {:keyspace keyspace}))

(defn drop-table
  ""
  [table]
  (Query. ["DROP TABLE" :table]
          {:table table}))

(defn drop-index
  ""
  [index]
  (Query. ["DROP INDEX" :index]
          {:index index}))

(defn create-index
  ""
  [table column]
  (Query. ["CREATE INDEX" :index-name "ON" :table "(" :column ")"]
          {:table table :column column}))


(defn batch
  ""
  [& queries]
  (Query. ["BATCH" :using "\n" :queries  "\nAPPLY BATCH"]
          {:queries queries}))


;; clauses

(defn columns
  ""
  [q & columns]
  (assoc-in q [:query :columns] columns))

(defn using
  ""
  [q & args]
  (assoc-in q [:query :using] args))

(defn limit
  ""
  [q n]
  (assoc-in q [:query :limit] n))

(defn order-by
  ""
  [q & fields]
  (assoc-in q [:query :order-by] fields))

(defn where
  ""
  [q args]
  (assoc-in q [:query :where] args))

(defn values
  ""
  [q values]
  (assoc-in q [:query :values] values))

(defn set
  ""
  [q values]
  (assoc-in q [:query :set] values))

;; (defn def-cols [q values]
;;   (update-in q [:query :defs] merge values))

;; (defn def-pk [q & values]
;;   (assoc-in q [:query :defs :pk] values))

(defn with
  ""
  [q values]
  (assoc-in q [:query :with] values))

(defn index-name
  ""
  [q value]
  (assoc-in q [:query :index-name] value))